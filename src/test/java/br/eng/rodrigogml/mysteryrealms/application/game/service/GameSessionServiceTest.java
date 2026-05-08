package br.eng.rodrigogml.mysteryrealms.application.game.service;

import br.eng.rodrigogml.mysteryrealms.application.character.entity.CharacterEntity;
import br.eng.rodrigogml.mysteryrealms.application.character.repository.CharacterRepository;
import br.eng.rodrigogml.mysteryrealms.application.game.dto.GameSnapshotDTO;
import br.eng.rodrigogml.mysteryrealms.application.user.entity.SessionEntity;
import br.eng.rodrigogml.mysteryrealms.application.user.service.UserService;
import br.eng.rodrigogml.mysteryrealms.application.world.entity.WorldInstanceEntity;
import br.eng.rodrigogml.mysteryrealms.application.world.entity.WorldLocationStateEntity;
import br.eng.rodrigogml.mysteryrealms.application.world.repository.WorldInstanceRepository;
import br.eng.rodrigogml.mysteryrealms.application.world.repository.WorldLocationStateRepository;
import br.eng.rodrigogml.mysteryrealms.common.exception.ValidationException;
import br.eng.rodrigogml.mysteryrealms.domain.character.enums.CharacterClass;
import br.eng.rodrigogml.mysteryrealms.domain.character.enums.Race;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameSessionServiceTest {

    @Mock private UserService userService;
    @Mock private CharacterRepository characterRepository;
    @Mock private WorldInstanceRepository worldInstanceRepository;
    @Mock private WorldLocationStateRepository worldLocationStateRepository;

    private GameSessionService service;

    @BeforeEach
    void setUp() {
        service = new GameSessionService(userService, characterRepository, worldInstanceRepository,
                worldLocationStateRepository);
    }

    @Test
    void loadSnapshot_comEstadoIntegro_retornaContratoDaTelaDeJogo() {
        SessionEntity session = new SessionEntity();
        session.setIdUser(1L);
        CharacterEntity character = character();
        LocalDateTime lastAccessedAt = character.getLastAccessedAt();
        when(userService.validateSession("tok")).thenReturn(session);
        when(characterRepository.findById(10L)).thenReturn(Optional.of(character));
        when(worldInstanceRepository.findByIdCharacter(10L)).thenReturn(Optional.of(worldInstance()));
        when(worldLocationStateRepository.findByIdWorldInstanceAndLocationId(20L, "zona_langur_praca_das_vozes"))
                .thenReturn(Optional.of(locationState()));

        GameSnapshotDTO snapshot = service.loadSnapshotForSelectedCharacter("tok", 10L);

        assertEquals(10L, snapshot.getCharacterId());
        assertEquals(20L, snapshot.getWorldInstanceId());
        assertEquals("Arlen", snapshot.getCharacterName());
        assertEquals(Race.HUMAN, snapshot.getRace());
        assertEquals(CharacterClass.WARRIOR, snapshot.getCharacterClass());
        assertEquals(3, snapshot.getCurrentLevel());
        assertEquals("zona_langur_praca_das_vozes", snapshot.getCurrentLocationId());
        assertEquals(75L, snapshot.getCurrentTimeMin());
        assertEquals(18.0, snapshot.getHealthPoints());
        assertEquals(20.0, snapshot.getMaxHealthPoints());
        assertEquals(4.0, snapshot.getCurrentFatigue());
        assertEquals(12.5, snapshot.getHungerPct());
        assertEquals(8.0, snapshot.getThirstPct());
        assertEquals(6, snapshot.getMorale());
        assertEquals(3, snapshot.getActions().size());
        assertFalse(snapshot.getActions().get(0).isAvailable());
        assertEquals(lastAccessedAt, character.getLastAccessedAt());
        verify(characterRepository, never()).save(any(CharacterEntity.class));
    }

    @Test
    void loadSnapshot_quandoPersonagemNaoPertenceAoUsuario_lancaValidacao() {
        SessionEntity session = new SessionEntity();
        session.setIdUser(99L);
        when(userService.validateSession("tok")).thenReturn(session);
        when(characterRepository.findById(10L)).thenReturn(Optional.of(character()));

        ValidationException exception = assertThrows(ValidationException.class,
                () -> service.loadSnapshotForSelectedCharacter("tok", 10L));

        assertEquals("character.error.notOwned", exception.getErrorKey());
    }

    @Test
    void loadSnapshot_quandoLocalidadeAtualNaoExiste_lancaValidacao() {
        SessionEntity session = new SessionEntity();
        session.setIdUser(1L);
        when(userService.validateSession("tok")).thenReturn(session);
        when(characterRepository.findById(10L)).thenReturn(Optional.of(character()));
        when(worldInstanceRepository.findByIdCharacter(10L)).thenReturn(Optional.of(worldInstance()));
        when(worldLocationStateRepository.findByIdWorldInstanceAndLocationId(20L, "zona_langur_praca_das_vozes"))
                .thenReturn(Optional.empty());

        ValidationException exception = assertThrows(ValidationException.class,
                () -> service.loadSnapshotForSelectedCharacter("tok", 10L));

        assertEquals("world.error.currentLocationStateNotFound", exception.getErrorKey());
    }

    private CharacterEntity character() {
        CharacterEntity character = new CharacterEntity();
        character.setId(10L);
        character.setIdUser(1L);
        character.setName("Arlen");
        character.setRace(Race.HUMAN);
        character.setCharacterClass(CharacterClass.WARRIOR);
        character.setCurrentLevel(3);
        character.setHealthPoints(18.0);
        character.setMaxHealthPoints(20.0);
        character.setCurrentFatigue(4.0);
        character.setHungerPct(12.5);
        character.setThirstPct(8.0);
        character.setMorale(6);
        character.setLastAccessedAt(LocalDateTime.of(2026, 5, 8, 12, 0));
        return character;
    }

    private WorldInstanceEntity worldInstance() {
        WorldInstanceEntity worldInstance = new WorldInstanceEntity();
        worldInstance.setId(20L);
        worldInstance.setIdCharacter(10L);
        worldInstance.setCurrentTimeMin(75L);
        worldInstance.setCurrentLocationId("zona_langur_praca_das_vozes");
        return worldInstance;
    }

    private WorldLocationStateEntity locationState() {
        WorldLocationStateEntity locationState = new WorldLocationStateEntity();
        locationState.setIdWorldInstance(20L);
        locationState.setLocationId("zona_langur_praca_das_vozes");
        locationState.setDiscovered(true);
        locationState.setAccessible(true);
        return locationState;
    }
}
