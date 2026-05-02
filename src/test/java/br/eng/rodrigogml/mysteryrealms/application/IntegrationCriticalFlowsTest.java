package br.eng.rodrigogml.mysteryrealms.application;

import br.eng.rodrigogml.mysteryrealms.application.character.entity.CharacterEntity;
import br.eng.rodrigogml.mysteryrealms.application.character.repository.CharacterRepository;
import br.eng.rodrigogml.mysteryrealms.application.character.service.CharacterService;
import br.eng.rodrigogml.mysteryrealms.application.coop.entity.CoopSessionEntity;
import br.eng.rodrigogml.mysteryrealms.application.coop.entity.CoopSessionStatus;
import br.eng.rodrigogml.mysteryrealms.application.coop.repository.CoopParticipantRepository;
import br.eng.rodrigogml.mysteryrealms.application.coop.repository.CoopSessionRepository;
import br.eng.rodrigogml.mysteryrealms.application.coop.service.CoopSessionService;
import br.eng.rodrigogml.mysteryrealms.application.user.entity.UserEntity;
import br.eng.rodrigogml.mysteryrealms.application.user.service.UserService;
import br.eng.rodrigogml.mysteryrealms.application.world.entity.WorldInstanceEntity;
import br.eng.rodrigogml.mysteryrealms.application.world.repository.WorldInstanceRepository;
import br.eng.rodrigogml.mysteryrealms.application.world.service.WorldInstanceService;
import br.eng.rodrigogml.mysteryrealms.domain.character.enums.CharacterClass;
import br.eng.rodrigogml.mysteryrealms.domain.character.enums.Race;
import br.eng.rodrigogml.mysteryrealms.support.TestDataFactory;
import br.eng.rodrigogml.mysteryrealms.domain.combat.service.CombatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.Mockito.mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testes de integração para mapear fluxos críticos entre módulos de aplicação.
 *
 * Fluxos críticos cobertos:
 * 1) user -> character: criação de usuário e personagem.
 * 2) character -> world -> coop: criação/entrada em instância de mundo compartilhada.
 * 3) character + domain/combat -> persistência: execução de ação de combate e persistência do estado.
 *
 * @author ?
 * @since 01-05-2026
 */
@SpringBootTest
@Transactional
class IntegrationCriticalFlowsTest {

    @Autowired
    private UserService userService;

    @Autowired
    private CharacterService characterService;

    @Autowired
    private WorldInstanceService worldInstanceService;

    @Autowired
    private CoopSessionService coopSessionService;

    @Autowired
    private CharacterRepository characterRepository;

    @Autowired
    private WorldInstanceRepository worldInstanceRepository;

    @Autowired
    private CoopParticipantRepository coopParticipantRepository;

    @Autowired
    private CoopSessionRepository coopSessionRepository;



    @TestConfiguration
    static class IntegrationTestConfig {
        @Bean
        JavaMailSender javaMailSender() {
            return mock(JavaMailSender.class);
        }
    }

    @Test
    void shouldCreateUserAndCharacterWithInitialWorldInstance() {
        TestDataFactory.RegistrationData registration = TestDataFactory.registration("hero");
        UserEntity user = userService.register(registration.username(), registration.email(), registration.password());

        TestDataFactory.CharacterCreationData characterData = TestDataFactory.warrior("Arannis");
        CharacterEntity character = characterService.createCharacter(
                user.getId(),
                characterData.name(),
                characterData.race(),
                characterData.characterClass());

        assertNotNull(character.getId());
        assertEquals(user.getId(), character.getIdUser());
        assertTrue(worldInstanceRepository.findByIdCharacter(character.getId()).isPresent());
    }

    @Test
    void shouldJoinWorldInstanceThroughCoopSession() {
        TestDataFactory.RegistrationData hostRegistration = TestDataFactory.registration("host");
        UserEntity hostUser = userService.register(hostRegistration.username(), hostRegistration.email(), hostRegistration.password());
        TestDataFactory.CharacterCreationData hostData = TestDataFactory.warrior("Host");
        CharacterEntity host = characterService.createCharacter(hostUser.getId(), hostData.name(), hostData.race(), hostData.characterClass());

        TestDataFactory.RegistrationData guestRegistration = TestDataFactory.registration("guest");
        UserEntity guestUser = userService.register(guestRegistration.username(), guestRegistration.email(), guestRegistration.password());
        TestDataFactory.CharacterCreationData guestData = TestDataFactory.mage("Guest");
        CharacterEntity guest = characterService.createCharacter(guestUser.getId(), guestData.name(), guestData.race(), guestData.characterClass());

        WorldInstanceEntity hostWorld = worldInstanceService.loadWorldInstance(host.getId());
        CoopSessionEntity session = coopSessionService.createSession(host.getId(), hostWorld.getId(), 2);

        session.setStatus(CoopSessionStatus.IN_PROGRESS);
        coopSessionRepository.save(session);
        coopSessionService.joinSession(session.getId(), guest.getId());

        assertEquals(2, coopParticipantRepository.countByIdCoopSessionAndLeftAtIsNull(session.getId()));
    }

    @Test
    void shouldPersistCharacterStateAfterCombatAction() {
        UserEntity user = userService.register("combatUser", "combat@example.com", "StrongP@ss1");
        CharacterEntity attacker = characterService.createCharacter(user.getId(), "Attacker", Race.HUMAN, CharacterClass.WARRIOR);
        CharacterEntity defender = characterService.createCharacter(user.getId(), "Defender", Race.DWARF, CharacterClass.CLERIC);

        int rawDamage = 20;
        int block = 10;
        double resistance = CombatService.limitPlayerResistance(0.25);
        int finalDamage = CombatService.resolveDamage(rawDamage, block, resistance);

        double expectedHp = Math.max(0.0, defender.getHealthPoints() - finalDamage);
        defender.setHealthPoints(expectedHp);
        characterRepository.save(defender);

        CharacterEntity reloaded = characterRepository.findById(defender.getId()).orElseThrow();
        assertEquals(expectedHp, reloaded.getHealthPoints());
        assertNotNull(attacker.getId());
    }
}
