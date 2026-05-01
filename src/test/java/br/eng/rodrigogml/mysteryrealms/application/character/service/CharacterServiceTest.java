package br.eng.rodrigogml.mysteryrealms.application.character.service;

import br.eng.rodrigogml.mysteryrealms.application.character.entity.CharacterEntity;
import br.eng.rodrigogml.mysteryrealms.application.character.repository.CharacterBackpackItemRepository;
import br.eng.rodrigogml.mysteryrealms.application.character.repository.CharacterEquippedItemRepository;
import br.eng.rodrigogml.mysteryrealms.application.character.repository.CharacterFactionReputationRepository;
import br.eng.rodrigogml.mysteryrealms.application.character.repository.CharacterLocalityReputationRepository;
import br.eng.rodrigogml.mysteryrealms.application.character.repository.CharacterNpcRelationshipRepository;
import br.eng.rodrigogml.mysteryrealms.application.character.repository.CharacterRepository;
import br.eng.rodrigogml.mysteryrealms.application.character.repository.CharacterSkillPointsRepository;
import br.eng.rodrigogml.mysteryrealms.application.coop.entity.CoopSessionEntity;
import br.eng.rodrigogml.mysteryrealms.application.coop.repository.CoopParticipantRepository;
import br.eng.rodrigogml.mysteryrealms.application.coop.repository.CoopSessionRepository;
import br.eng.rodrigogml.mysteryrealms.application.world.entity.DiaryEntryEntity;
import br.eng.rodrigogml.mysteryrealms.application.world.entity.WorldInstanceEntity;
import br.eng.rodrigogml.mysteryrealms.application.world.repository.DiaryEntryRepository;
import br.eng.rodrigogml.mysteryrealms.application.world.repository.DiaryImpactMarkerRepository;
import br.eng.rodrigogml.mysteryrealms.application.world.repository.DiaryImpactRelationshipRepository;
import br.eng.rodrigogml.mysteryrealms.application.world.repository.DiaryImpactReputationRepository;
import br.eng.rodrigogml.mysteryrealms.application.world.repository.WorldInstanceRepository;
import br.eng.rodrigogml.mysteryrealms.application.world.repository.WorldLocationStateRepository;
import br.eng.rodrigogml.mysteryrealms.application.world.repository.WorldMarkerRepository;
import br.eng.rodrigogml.mysteryrealms.application.world.repository.WorldNpcStateRepository;
import br.eng.rodrigogml.mysteryrealms.application.world.repository.WorldQuestStateRepository;
import br.eng.rodrigogml.mysteryrealms.domain.character.enums.CharacterClass;
import br.eng.rodrigogml.mysteryrealms.domain.character.enums.Gender;
import br.eng.rodrigogml.mysteryrealms.domain.character.enums.Race;
import br.eng.rodrigogml.mysteryrealms.domain.character.service.ProgressionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Testes do serviço de personagens de aplicação — RF-PE-01 a RF-PE-05.
 *
 * @author ?
 * @since 28-04-2026
 */
@ExtendWith(MockitoExtension.class)
class CharacterServiceTest {

    @Mock private CharacterRepository characterRepository;
    @Mock private CharacterNpcRelationshipRepository npcRelationshipRepository;
    @Mock private CharacterLocalityReputationRepository localityReputationRepository;
    @Mock private CharacterFactionReputationRepository factionReputationRepository;
    @Mock private CharacterSkillPointsRepository skillPointsRepository;
    @Mock private CharacterEquippedItemRepository equippedItemRepository;
    @Mock private CharacterBackpackItemRepository backpackItemRepository;
    @Mock private WorldInstanceRepository worldInstanceRepository;
    @Mock private WorldQuestStateRepository worldQuestStateRepository;
    @Mock private WorldNpcStateRepository worldNpcStateRepository;
    @Mock private WorldLocationStateRepository worldLocationStateRepository;
    @Mock private WorldMarkerRepository worldMarkerRepository;
    @Mock private DiaryEntryRepository diaryEntryRepository;
    @Mock private DiaryImpactRelationshipRepository diaryImpactRelationshipRepository;
    @Mock private DiaryImpactReputationRepository diaryImpactReputationRepository;
    @Mock private DiaryImpactMarkerRepository diaryImpactMarkerRepository;
    @Mock private CoopSessionRepository coopSessionRepository;
    @Mock private CoopParticipantRepository coopParticipantRepository;

    private CharacterService service;

    @BeforeEach
    void setUp() {
        service = new CharacterService(characterRepository, npcRelationshipRepository,
                localityReputationRepository, factionReputationRepository, skillPointsRepository,
                equippedItemRepository, backpackItemRepository, worldInstanceRepository,
                worldQuestStateRepository, worldNpcStateRepository, worldLocationStateRepository,
                worldMarkerRepository, diaryEntryRepository, diaryImpactRelationshipRepository,
                diaryImpactReputationRepository, diaryImpactMarkerRepository,
                coopSessionRepository, coopParticipantRepository);
    }

    // ── RF-PE-01: Múltiplos personagens por usuário ──────────────────────────

    @Test
    void createCharacter_limiteAtingido_lancaExcecao() {
        when(characterRepository.countByIdUser(1L)).thenReturn(50L);
        assertThrows(IllegalArgumentException.class,
                () -> service.createCharacter(1L, "Aragorn", Race.HUMAN, CharacterClass.WARRIOR));
    }

    @Test
    void createCharacter_abaixoDoLimite_criaNormalmente() {
        when(characterRepository.countByIdUser(1L)).thenReturn(0L);
        when(characterRepository.existsByIdUserAndName(1L, "Aragorn")).thenReturn(false);
        CharacterEntity saved = buildEntity(1L, 1L, "Aragorn");
        when(characterRepository.save(any())).thenReturn(saved);
        when(worldInstanceRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        CharacterEntity result = service.createCharacter(1L, "Aragorn", Race.HUMAN, CharacterClass.WARRIOR);

        assertNotNull(result);
        assertEquals("Aragorn", result.getName());
    }

    // ── RF-PE-02: Criação de personagem ──────────────────────────────────────

    @Test
    void createCharacter_nomeVazio_lancaExcecao() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.createCharacter(1L, "", Race.HUMAN, CharacterClass.WARRIOR));
        assertEquals("character.error.nameBlank", ex.getMessage());
    }

    @Test
    void createCharacter_usuarioInvalido_lancaExcecaoSemConsultarRepositorio() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.createCharacter(0L, "Aragorn", Race.HUMAN, CharacterClass.WARRIOR));

        assertEquals("character.error.invalidUserId", ex.getMessage());
        verify(characterRepository, never()).countByIdUser(anyLong());
    }

    @Test
    void createCharacter_nomeDuplicado_lancaExcecao() {
        when(characterRepository.countByIdUser(1L)).thenReturn(0L);
        when(characterRepository.existsByIdUserAndName(1L, "Aragorn")).thenReturn(true);
        assertThrows(IllegalArgumentException.class,
                () -> service.createCharacter(1L, "Aragorn", Race.HUMAN, CharacterClass.WARRIOR));
    }

    @Test
    void createCharacter_criaInstanciaDeMundoJuntamente() {
        when(characterRepository.countByIdUser(1L)).thenReturn(0L);
        when(characterRepository.existsByIdUserAndName(1L, "Legolas")).thenReturn(false);
        CharacterEntity saved = buildEntity(10L, 1L, "Legolas");
        when(characterRepository.save(any())).thenReturn(saved);
        when(worldInstanceRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        service.createCharacter(1L, "Legolas", Race.ELF, CharacterClass.HUNTER);

        verify(worldInstanceRepository).save(argThat(w -> w.getIdCharacter().equals(10L)));
    }

    // ── RF-PE-03: Listagem e seleção de personagem ───────────────────────────

    @Test
    void listCharacters_retornaPersonagensDoUsuario() {
        List<CharacterEntity> lista = List.of(buildEntity(1L, 5L, "Gimli"));
        when(characterRepository.findAllByIdUser(5L)).thenReturn(lista);

        List<CharacterEntity> result = service.listCharacters(5L);

        assertEquals(1, result.size());
        assertEquals("Gimli", result.get(0).getName());
    }

    @Test
    void listCharacters_usuarioInvalido_lancaExcecaoSemConsultarRepositorio() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.listCharacters(null));

        assertEquals("character.error.invalidUserId", ex.getMessage());
        verify(characterRepository, never()).findAllByIdUser(any());
    }

    @Test
    void selectCharacter_pertenceAoUsuario_atualizaLastAccessed() {
        CharacterEntity character = buildEntity(1L, 1L, "Frodo");
        when(characterRepository.findById(1L)).thenReturn(Optional.of(character));
        when(characterRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        CharacterEntity result = service.selectCharacter(1L, 1L);

        assertNotNull(result.getLastAccessedAt());
    }

    @Test
    void selectCharacter_naoEncontrado_lancaExcecao() {
        when(characterRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> service.selectCharacter(1L, 99L));
    }

    @Test
    void selectCharacter_personagemInvalido_lancaExcecaoSemConsultarRepositorio() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.selectCharacter(1L, 0L));

        assertEquals("character.error.invalidCharacterId", ex.getMessage());
        verify(characterRepository, never()).findById(anyLong());
    }

    @Test
    void selectCharacter_naoPertencoAoUsuario_lancaExcecao() {
        CharacterEntity character = buildEntity(1L, 2L, "Frodo");
        when(characterRepository.findById(1L)).thenReturn(Optional.of(character));
        assertThrows(IllegalArgumentException.class, () -> service.selectCharacter(1L, 1L));
    }

    // ── RF-PE-04: Renomeação de personagem ───────────────────────────────────

    @Test
    void renameCharacter_nomeDisponivel_atualizaNome() {
        CharacterEntity character = buildEntity(1L, 1L, "Frodo");
        when(characterRepository.findById(1L)).thenReturn(Optional.of(character));
        when(characterRepository.existsByIdUserAndName(1L, "Bilbo")).thenReturn(false);
        when(characterRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        service.renameCharacter(1L, 1L, "Bilbo");

        verify(characterRepository).save(argThat(c -> "Bilbo".equals(c.getName())));
    }

    @Test
    void renameCharacter_usuarioInvalido_lancaExcecaoSemConsultarRepositorio() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.renameCharacter(-1L, 1L, "Bilbo"));

        assertEquals("character.error.invalidUserId", ex.getMessage());
        verify(characterRepository, never()).findById(anyLong());
    }

    @Test
    void renameCharacter_nomeEmBranco_lancaExcecao() {
        CharacterEntity character = buildEntity(1L, 1L, "Frodo");
        when(characterRepository.findById(1L)).thenReturn(Optional.of(character));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.renameCharacter(1L, 1L, "   "));

        assertEquals("character.error.nameBlank", ex.getMessage());
        verify(characterRepository, never()).save(any());
    }

    @Test
    void renameCharacter_mesmoNomeNaoPersisteNovamente() {
        CharacterEntity character = buildEntity(1L, 1L, "Frodo");
        when(characterRepository.findById(1L)).thenReturn(Optional.of(character));

        service.renameCharacter(1L, 1L, " Frodo ");

        verify(characterRepository, never()).existsByIdUserAndName(anyLong(), anyString());
        verify(characterRepository, never()).save(any());
    }

    @Test
    void renameCharacter_nomeDuplicado_lancaExcecao() {
        CharacterEntity character = buildEntity(1L, 1L, "Frodo");
        when(characterRepository.findById(1L)).thenReturn(Optional.of(character));
        when(characterRepository.existsByIdUserAndName(1L, "Bilbo")).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> service.renameCharacter(1L, 1L, "Bilbo"));
    }

    @Test
    void renameCharacter_naoPertencoAoUsuario_lancaExcecao() {
        CharacterEntity character = buildEntity(1L, 2L, "Frodo");
        when(characterRepository.findById(1L)).thenReturn(Optional.of(character));
        assertThrows(IllegalArgumentException.class, () -> service.renameCharacter(1L, 1L, "Bilbo"));
    }

    @Test
    void applyProgressionMilestones_aplicaSubidaDeNivel() {
        CharacterEntity character = buildEntity(1L, 1L, "Frodo");
        character.setCurrentLevel(1);
        character.setAccumulatedXp(ProgressionService.totalXpForLevel(4));
        when(characterRepository.findById(1L)).thenReturn(Optional.of(character));
        when(characterRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        ProgressionService.LevelUpResult result = service.applyProgressionMilestones(1L, 1L);

        assertEquals(4, result.targetLevel());
        assertEquals(3, result.levelsGained());
        verify(characterRepository).save(argThat(c -> c.getCurrentLevel() == 4));
    }

    // ── RF-PE-05: Exclusão de personagem ────────────────────────────────────

    @Test
    void deleteCharacter_pertenceAoUsuario_removeTodasAsDependencias() {
        CharacterEntity character = buildEntity(1L, 1L, "Sam");
        WorldInstanceEntity worldInstance = new WorldInstanceEntity();
        worldInstance.setId(10L);
        worldInstance.setIdCharacter(1L);
        DiaryEntryEntity diaryEntry = new DiaryEntryEntity();
        diaryEntry.setId(100L);
        CoopSessionEntity coopSession = new CoopSessionEntity();
        coopSession.setId(200L);
        when(characterRepository.findById(1L)).thenReturn(Optional.of(character));
        when(worldInstanceRepository.findByIdCharacter(1L)).thenReturn(Optional.of(worldInstance));
        when(diaryEntryRepository.findAllByIdWorldInstance(10L)).thenReturn(List.of(diaryEntry));
        when(coopSessionRepository.findAllByIdWorldInstance(10L)).thenReturn(List.of(coopSession));

        service.deleteCharacter(1L, 1L, true);

        verify(coopParticipantRepository).deleteAllByIdCharacter(1L);
        verify(coopParticipantRepository).deleteAllByIdCoopSession(200L);
        verify(coopSessionRepository).deleteAllByIdWorldInstance(10L);
        verify(diaryImpactRelationshipRepository).deleteAllByIdDiaryEntry(100L);
        verify(diaryImpactReputationRepository).deleteAllByIdDiaryEntry(100L);
        verify(diaryImpactMarkerRepository).deleteAllByIdDiaryEntry(100L);
        verify(diaryEntryRepository).deleteAllByIdWorldInstance(10L);
        verify(worldQuestStateRepository).deleteAllByIdWorldInstance(10L);
        verify(worldNpcStateRepository).deleteAllByIdWorldInstance(10L);
        verify(worldLocationStateRepository).deleteAllByIdWorldInstance(10L);
        verify(worldMarkerRepository).deleteAllByIdWorldInstance(10L);
        verify(worldInstanceRepository).deleteByIdCharacter(1L);
        verify(npcRelationshipRepository).deleteAllByIdCharacter(1L);
        verify(localityReputationRepository).deleteAllByIdCharacter(1L);
        verify(factionReputationRepository).deleteAllByIdCharacter(1L);
        verify(skillPointsRepository).deleteAllByIdCharacter(1L);
        verify(equippedItemRepository).deleteAllByIdCharacter(1L);
        verify(backpackItemRepository).deleteAllByIdCharacter(1L);
        verify(characterRepository).delete(character);
    }

    @Test
    void deleteCharacter_naoEncontrado_lancaExcecao() {
        when(characterRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> service.deleteCharacter(1L, 99L, true));
    }

    @Test
    void deleteCharacter_naoPertencoAoUsuario_lancaExcecao() {
        CharacterEntity character = buildEntity(1L, 2L, "Sam");
        when(characterRepository.findById(1L)).thenReturn(Optional.of(character));
        assertThrows(IllegalArgumentException.class, () -> service.deleteCharacter(1L, 1L, true));
    }

    @Test
    void deleteCharacter_semInstanciaDeMundo_aindaRemoveDependenciasDoPersonagem() {
        CharacterEntity character = buildEntity(1L, 1L, "Sam");
        when(characterRepository.findById(1L)).thenReturn(Optional.of(character));
        when(worldInstanceRepository.findByIdCharacter(1L)).thenReturn(Optional.empty());

        service.deleteCharacter(1L, 1L, true);

        verify(coopParticipantRepository).deleteAllByIdCharacter(1L);
        verify(worldInstanceRepository, never()).deleteByIdCharacter(1L);
        verify(characterRepository).delete(character);
    }

    @Test
    void deleteCharacter_semConfirmacaoExplicita_lancaExcecaoSemConsultarRepositorio() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.deleteCharacter(1L, 1L));

        assertEquals("character.error.deleteConfirmationRequired", ex.getMessage());
        verify(characterRepository, never()).findById(anyLong());
        verify(characterRepository, never()).delete(any());
    }

    @Test
    void deleteCharacter_personagemInvalido_lancaExcecaoSemConsultarRepositorio() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.deleteCharacter(1L, null, true));

        assertEquals("character.error.invalidCharacterId", ex.getMessage());
        verify(characterRepository, never()).findById(anyLong());
        verify(characterRepository, never()).delete(any());
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private CharacterEntity buildEntity(Long id, Long idUser, String name) {
        CharacterEntity c = new CharacterEntity();
        c.setId(id);
        c.setIdUser(idUser);
        c.setName(name);
        c.setSurname("Sobrenome");
        c.setGender(Gender.MALE);
        c.setRace(Race.HUMAN);
        c.setCharacterClass(CharacterClass.WARRIOR);
        c.setInitialAge(25);
        c.setCreatedAt(LocalDateTime.now());
        return c;
    }
}
