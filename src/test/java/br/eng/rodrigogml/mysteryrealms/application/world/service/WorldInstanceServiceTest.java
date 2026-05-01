package br.eng.rodrigogml.mysteryrealms.application.world.service;

import br.eng.rodrigogml.mysteryrealms.application.world.entity.MarkerType;
import br.eng.rodrigogml.mysteryrealms.application.world.entity.QuestState;
import br.eng.rodrigogml.mysteryrealms.application.world.entity.WorldInstanceEntity;
import br.eng.rodrigogml.mysteryrealms.application.world.entity.WorldLocationStateEntity;
import br.eng.rodrigogml.mysteryrealms.application.world.entity.WorldMarkerEntity;
import br.eng.rodrigogml.mysteryrealms.application.world.entity.WorldNpcStateEntity;
import br.eng.rodrigogml.mysteryrealms.application.world.entity.WorldQuestStateEntity;
import br.eng.rodrigogml.mysteryrealms.application.world.repository.DiaryEntryRepository;
import br.eng.rodrigogml.mysteryrealms.application.world.repository.WorldInstanceRepository;
import br.eng.rodrigogml.mysteryrealms.application.world.repository.WorldLocationStateRepository;
import br.eng.rodrigogml.mysteryrealms.application.world.repository.WorldMarkerRepository;
import br.eng.rodrigogml.mysteryrealms.application.world.repository.WorldNpcStateRepository;
import br.eng.rodrigogml.mysteryrealms.application.world.repository.WorldQuestStateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import br.eng.rodrigogml.mysteryrealms.common.exception.DomainException;
import br.eng.rodrigogml.mysteryrealms.common.exception.ValidationException;

/**
 * Testes do serviço de instância de mundo - RF-IM-01 a RF-IM-05.
 *
 * @author ?
 * @since 28-04-2026
 */
@ExtendWith(MockitoExtension.class)
class WorldInstanceServiceTest {

    @Mock private WorldInstanceRepository worldInstanceRepository;
    @Mock private WorldQuestStateRepository questStateRepository;
    @Mock private WorldNpcStateRepository npcStateRepository;
    @Mock private WorldLocationStateRepository locationStateRepository;
    @Mock private WorldMarkerRepository markerRepository;
    @Mock private DiaryEntryRepository diaryEntryRepository;

    private WorldInstanceService service;

    @BeforeEach
    void setUp() {
        service = new WorldInstanceService(worldInstanceRepository, questStateRepository,
                npcStateRepository, locationStateRepository, markerRepository, diaryEntryRepository);
    }

    @Test
    void createWorldInstance_persisteInstanciaVinculadaAoPersonagem() {
        when(worldInstanceRepository.findByIdCharacter(42L)).thenReturn(Optional.empty());
        when(worldInstanceRepository.save(any())).thenAnswer(i -> {
            WorldInstanceEntity w = i.getArgument(0);
            w.setId(1L);
            return w;
        });

        WorldInstanceEntity result = service.createWorldInstance(42L);

        assertEquals(42L, result.getIdCharacter());
        assertEquals(0L, result.getCurrentTimeMin());
        assertNull(result.getCurrentLocationId());
    }

    @Test
    void createWorldInstance_tempoInicialZero() {
        when(worldInstanceRepository.findByIdCharacter(1L)).thenReturn(Optional.empty());
        when(worldInstanceRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        WorldInstanceEntity result = service.createWorldInstance(1L);

        assertEquals(0L, result.getCurrentTimeMin());
    }

    @Test
    void createWorldInstance_instanciaJaExistente_lancaExcecao() {
        WorldInstanceEntity existing = new WorldInstanceEntity();
        existing.setId(5L);
        existing.setIdCharacter(42L);
        when(worldInstanceRepository.findByIdCharacter(42L)).thenReturn(Optional.of(existing));

        ValidationException ex = assertThrows(ValidationException.class,
                () -> service.createWorldInstance(42L));

        assertEquals("world.error.instanceAlreadyExists", ex.getMessage());
        verify(worldInstanceRepository, never()).save(any());
    }

    @Test
    void createWorldInstance_personagemInvalido_lancaExcecaoSemConsultarRepositorio() {
        ValidationException ex = assertThrows(ValidationException.class,
                () -> service.createWorldInstance(0L));

        assertEquals("world.error.invalidCharacterId", ex.getMessage());
        verify(worldInstanceRepository, never()).findByIdCharacter(0L);
        verify(worldInstanceRepository, never()).save(any());
    }

    @Test
    void loadWorldInstance_instanciaExistente_retornaInstancia() {
        WorldInstanceEntity instance = new WorldInstanceEntity();
        instance.setId(1L);
        instance.setIdCharacter(10L);
        when(worldInstanceRepository.findByIdCharacter(10L)).thenReturn(Optional.of(instance));

        WorldInstanceEntity result = service.loadWorldInstance(10L);

        assertEquals(10L, result.getIdCharacter());
    }

    @Test
    void loadWorldInstance_instanciaNaoEncontrada_lancaExcecao() {
        when(worldInstanceRepository.findByIdCharacter(99L)).thenReturn(Optional.empty());
        assertThrows(ValidationException.class, () -> service.loadWorldInstance(99L));
    }

    @Test
    void saveWorldInstance_persisteEstadoAtualizado() {
        WorldInstanceEntity instance = new WorldInstanceEntity();
        instance.setIdCharacter(1L);
        instance.setCurrentTimeMin(60L);
        when(worldInstanceRepository.save(instance)).thenReturn(instance);

        WorldInstanceEntity result = service.saveWorldInstance(instance);

        assertEquals(60L, result.getCurrentTimeMin());
        verify(worldInstanceRepository).save(instance);
    }

    @Test
    void saveWorldInstance_tempoNegativo_lancaExcecao() {
        WorldInstanceEntity instance = new WorldInstanceEntity();
        instance.setIdCharacter(1L);
        instance.setCurrentTimeMin(-1L);

        ValidationException ex = assertThrows(ValidationException.class,
                () -> service.saveWorldInstance(instance));

        assertEquals("world.error.invalidCurrentTime", ex.getMessage());
        verify(worldInstanceRepository, never()).save(any());
    }

    @Test
    void createWorldInstance_personagensDiferentes_instanciasSeparadas() {
        when(worldInstanceRepository.findByIdCharacter(1L)).thenReturn(Optional.empty());
        when(worldInstanceRepository.findByIdCharacter(2L)).thenReturn(Optional.empty());
        when(worldInstanceRepository.save(any())).thenAnswer(i -> {
            WorldInstanceEntity w = i.getArgument(0);
            w.setId(w.getIdCharacter());
            return w;
        });

        WorldInstanceEntity w1 = service.createWorldInstance(1L);
        WorldInstanceEntity w2 = service.createWorldInstance(2L);

        assertNotEquals(w1.getId(), w2.getId());
        assertNotEquals(w1.getIdCharacter(), w2.getIdCharacter());
    }

    @Test
    void getQuestState_existente_retornaEstado() {
        WorldQuestStateEntity qs = new WorldQuestStateEntity();
        qs.setQuestId("quest001");
        qs.setState(QuestState.ACTIVE);
        when(questStateRepository.findByIdWorldInstanceAndQuestId(1L, "quest001")).thenReturn(Optional.of(qs));

        Optional<WorldQuestStateEntity> result = service.getQuestState(1L, "quest001");

        assertTrue(result.isPresent());
        assertEquals(QuestState.ACTIVE, result.get().getState());
    }

    @Test
    void getQuestState_inexistente_retornaVazio() {
        when(questStateRepository.findByIdWorldInstanceAndQuestId(1L, "quest999")).thenReturn(Optional.empty());

        Optional<WorldQuestStateEntity> result = service.getQuestState(1L, "quest999");

        assertFalse(result.isPresent());
    }

    @Test
    void setQuestState_novaQuest_persisteEstado() {
        when(questStateRepository.findByIdWorldInstanceAndQuestId(1L, "quest001")).thenReturn(Optional.empty());
        when(questStateRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        service.setQuestState(1L, "quest001", QuestState.COMPLETED);

        ArgumentCaptor<WorldQuestStateEntity> captor = ArgumentCaptor.forClass(WorldQuestStateEntity.class);
        verify(questStateRepository).save(captor.capture());
        assertEquals(QuestState.COMPLETED, captor.getValue().getState());
        assertEquals("quest001", captor.getValue().getQuestId());
    }

    @Test
    void setQuestState_estadoNulo_lancaExcecaoSemPersistir() {
        ValidationException ex = assertThrows(ValidationException.class,
                () -> service.setQuestState(1L, "quest001", null));

        assertEquals("world.error.invalidQuestState", ex.getMessage());
        verify(questStateRepository, never()).save(any());
    }

    @Test
    void setQuestState_questExistente_atualizaEstado() {
        WorldQuestStateEntity existing = new WorldQuestStateEntity();
        existing.setQuestId("quest001");
        existing.setState(QuestState.ACTIVE);
        when(questStateRepository.findByIdWorldInstanceAndQuestId(1L, "quest001")).thenReturn(Optional.of(existing));
        when(questStateRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        service.setQuestState(1L, "quest001", QuestState.COMPLETED);

        assertEquals(QuestState.COMPLETED, existing.getState());
    }

    @Test
    void getNpcState_existente_retornaEstado() {
        WorldNpcStateEntity npc = new WorldNpcStateEntity();
        npc.setNpcId("npc001");
        npc.setAlive(true);
        when(npcStateRepository.findByIdWorldInstanceAndNpcId(1L, "npc001")).thenReturn(Optional.of(npc));

        Optional<WorldNpcStateEntity> result = service.getNpcState(1L, "npc001");

        assertTrue(result.isPresent());
        assertTrue(result.get().isAlive());
    }

    @Test
    void setNpcState_novoNpc_persisteEstado() {
        when(npcStateRepository.findByIdWorldInstanceAndNpcId(1L, "npc001")).thenReturn(Optional.empty());
        when(npcStateRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        service.setNpcState(1L, "npc001", false, "morto");

        ArgumentCaptor<WorldNpcStateEntity> captor = ArgumentCaptor.forClass(WorldNpcStateEntity.class);
        verify(npcStateRepository).save(captor.capture());
        assertFalse(captor.getValue().isAlive());
        assertEquals("morto", captor.getValue().getDialogState());
    }

    @Test
    void getLocationState_existente_retornaEstado() {
        WorldLocationStateEntity loc = new WorldLocationStateEntity();
        loc.setLocationId("loc001");
        loc.setDiscovered(true);
        loc.setAccessible(true);
        when(locationStateRepository.findByIdWorldInstanceAndLocationId(1L, "loc001")).thenReturn(Optional.of(loc));

        Optional<WorldLocationStateEntity> result = service.getLocationState(1L, "loc001");

        assertTrue(result.isPresent());
        assertTrue(result.get().isDiscovered());
    }

    @Test
    void setLocationState_novaLocalidade_persisteEstado() {
        when(locationStateRepository.findByIdWorldInstanceAndLocationId(1L, "loc001")).thenReturn(Optional.empty());
        when(locationStateRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        service.setLocationState(1L, "loc001", true, true);

        ArgumentCaptor<WorldLocationStateEntity> captor = ArgumentCaptor.forClass(WorldLocationStateEntity.class);
        verify(locationStateRepository).save(captor.capture());
        assertTrue(captor.getValue().isDiscovered());
        assertTrue(captor.getValue().isAccessible());
    }

    @Test
    void getMarker_existente_retornaMarcador() {
        WorldMarkerEntity marker = new WorldMarkerEntity();
        marker.setMarkerId("flag001");
        marker.setMarkerType(MarkerType.FLAG);
        marker.setFlagValue(true);
        when(markerRepository.findByIdWorldInstanceAndMarkerId(1L, "flag001")).thenReturn(Optional.of(marker));

        Optional<WorldMarkerEntity> result = service.getMarker(1L, "flag001");

        assertTrue(result.isPresent());
        assertTrue(result.get().getFlagValue());
    }

    @Test
    void setMarker_tipoFlag_persisteFlagValue() {
        when(markerRepository.findByIdWorldInstanceAndMarkerId(1L, "flag001")).thenReturn(Optional.empty());
        when(markerRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        service.setMarker(1L, "flag001", MarkerType.FLAG, Boolean.TRUE);

        ArgumentCaptor<WorldMarkerEntity> captor = ArgumentCaptor.forClass(WorldMarkerEntity.class);
        verify(markerRepository).save(captor.capture());
        assertEquals(MarkerType.FLAG, captor.getValue().getMarkerType());
        assertTrue(captor.getValue().getFlagValue());
        assertNull(captor.getValue().getIntValue());
    }

    @Test
    void setMarker_tipoCounter_persisteIntValue() {
        when(markerRepository.findByIdWorldInstanceAndMarkerId(1L, "counter001")).thenReturn(Optional.empty());
        when(markerRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        service.setMarker(1L, "counter001", MarkerType.COUNTER, 5);

        ArgumentCaptor<WorldMarkerEntity> captor = ArgumentCaptor.forClass(WorldMarkerEntity.class);
        verify(markerRepository).save(captor.capture());
        assertEquals(5, captor.getValue().getIntValue());
        assertNull(captor.getValue().getFlagValue());
    }

    @Test
    void setMarker_valorNulo_limpaMarcador() {
        when(markerRepository.findByIdWorldInstanceAndMarkerId(1L, "flag001")).thenReturn(Optional.empty());
        when(markerRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        service.setMarker(1L, "flag001", MarkerType.FLAG, null);

        ArgumentCaptor<WorldMarkerEntity> captor = ArgumentCaptor.forClass(WorldMarkerEntity.class);
        verify(markerRepository).save(captor.capture());
        assertNull(captor.getValue().getFlagValue());
        assertNull(captor.getValue().getIntValue());
    }

    @Test
    void setMarker_tipoFlagComValorInteiro_lancaExcecao() {
        ValidationException ex = assertThrows(ValidationException.class,
                () -> service.setMarker(1L, "flag001", MarkerType.FLAG, 1));

        assertEquals("world.error.invalidMarkerValue", ex.getMessage());
        verify(markerRepository, never()).save(any());
    }

    @Test
    void getMarker_idMarcadorEmBranco_lancaExcecao() {
        ValidationException ex = assertThrows(ValidationException.class,
                () -> service.getMarker(1L, " "));

        assertEquals("world.error.invalidMarkerId", ex.getMessage());
        verify(markerRepository, never()).findByIdWorldInstanceAndMarkerId(any(), any());
    }

    @Test
    void createWorldInstance_bootstrapCompletoMantemConsistenciaEntreEntidades() {
        when(worldInstanceRepository.findByIdCharacter(42L)).thenReturn(Optional.empty());
        when(worldInstanceRepository.save(any())).thenAnswer(i -> {
            WorldInstanceEntity entity = i.getArgument(0);
            if (entity.getId() == null) {
                entity.setId(100L);
            }
            return entity;
        });
        when(questStateRepository.findByIdWorldInstanceAndQuestId(100L, "quest.bootstrap")).thenReturn(Optional.empty());
        when(npcStateRepository.findByIdWorldInstanceAndNpcId(100L, "npc.bootstrap")).thenReturn(Optional.empty());
        when(locationStateRepository.findByIdWorldInstanceAndLocationId(100L, "loc.bootstrap")).thenReturn(Optional.empty());
        when(markerRepository.findByIdWorldInstanceAndMarkerId(100L, "flag.bootstrap")).thenReturn(Optional.empty());

        WorldInstanceEntity created = service.createWorldInstance(42L);
        service.setQuestState(created.getId(), "quest.bootstrap", QuestState.ACTIVE);
        service.setNpcState(created.getId(), "npc.bootstrap", true, "greeting");
        service.setLocationState(created.getId(), "loc.bootstrap", true, true);
        service.setMarker(created.getId(), "flag.bootstrap", MarkerType.FLAG, true);

        ArgumentCaptor<WorldQuestStateEntity> questCaptor = ArgumentCaptor.forClass(WorldQuestStateEntity.class);
        ArgumentCaptor<WorldNpcStateEntity> npcCaptor = ArgumentCaptor.forClass(WorldNpcStateEntity.class);
        ArgumentCaptor<WorldLocationStateEntity> locationCaptor = ArgumentCaptor.forClass(WorldLocationStateEntity.class);
        ArgumentCaptor<WorldMarkerEntity> markerCaptor = ArgumentCaptor.forClass(WorldMarkerEntity.class);
        verify(questStateRepository).save(questCaptor.capture());
        verify(npcStateRepository).save(npcCaptor.capture());
        verify(locationStateRepository).save(locationCaptor.capture());
        verify(markerRepository).save(markerCaptor.capture());

        assertNotNull(created.getId());
        assertEquals(created.getId(), questCaptor.getValue().getIdWorldInstance());
        assertEquals(created.getId(), npcCaptor.getValue().getIdWorldInstance());
        assertEquals(created.getId(), locationCaptor.getValue().getIdWorldInstance());
        assertEquals(created.getId(), markerCaptor.getValue().getIdWorldInstance());
    }

    @Test
    void loadWorldInstance_recargaMantemConsistenciaDosEstadosPersistidos() {
        WorldInstanceEntity persisted = new WorldInstanceEntity();
        persisted.setId(200L);
        persisted.setIdCharacter(50L);
        persisted.setCurrentTimeMin(120L);
        persisted.setCurrentLocationId("loc.saved");
        when(worldInstanceRepository.findByIdCharacter(50L)).thenReturn(Optional.of(persisted));

        WorldQuestStateEntity quest = new WorldQuestStateEntity();
        quest.setIdWorldInstance(200L);
        quest.setQuestId("quest.saved");
        quest.setState(QuestState.COMPLETED);
        when(questStateRepository.findByIdWorldInstanceAndQuestId(200L, "quest.saved")).thenReturn(Optional.of(quest));

        WorldNpcStateEntity npc = new WorldNpcStateEntity();
        npc.setIdWorldInstance(200L);
        npc.setNpcId("npc.saved");
        npc.setAlive(false);
        npc.setDialogState("done");
        when(npcStateRepository.findByIdWorldInstanceAndNpcId(200L, "npc.saved")).thenReturn(Optional.of(npc));

        WorldLocationStateEntity location = new WorldLocationStateEntity();
        location.setIdWorldInstance(200L);
        location.setLocationId("loc.saved");
        location.setDiscovered(true);
        location.setAccessible(false);
        when(locationStateRepository.findByIdWorldInstanceAndLocationId(200L, "loc.saved")).thenReturn(Optional.of(location));

        WorldMarkerEntity marker = new WorldMarkerEntity();
        marker.setIdWorldInstance(200L);
        marker.setMarkerId("counter.saved");
        marker.setMarkerType(MarkerType.COUNTER);
        marker.setIntValue(8);
        when(markerRepository.findByIdWorldInstanceAndMarkerId(200L, "counter.saved")).thenReturn(Optional.of(marker));

        WorldInstanceEntity reloaded = service.loadWorldInstance(50L);

        assertEquals(200L, reloaded.getId());
        assertEquals(120L, reloaded.getCurrentTimeMin());
        assertEquals("loc.saved", reloaded.getCurrentLocationId());
        assertEquals(QuestState.COMPLETED, service.getQuestState(200L, "quest.saved").orElseThrow().getState());
        assertFalse(service.getNpcState(200L, "npc.saved").orElseThrow().isAlive());
        assertFalse(service.getLocationState(200L, "loc.saved").orElseThrow().isAccessible());
        assertEquals(8, service.getMarker(200L, "counter.saved").orElseThrow().getIntValue());
    }

    @Test
    void estadosSaoIsoladosEntrePersonagensDiferentes() {
        when(worldInstanceRepository.findByIdCharacter(1L)).thenReturn(Optional.empty());
        when(worldInstanceRepository.findByIdCharacter(2L)).thenReturn(Optional.empty());
        when(worldInstanceRepository.save(any())).thenAnswer(i -> {
            WorldInstanceEntity entity = i.getArgument(0);
            if (entity.getId() == null) {
                entity.setId(entity.getIdCharacter() == 1L ? 301L : 302L);
            }
            return entity;
        });

        WorldQuestStateEntity quest1 = new WorldQuestStateEntity();
        quest1.setIdWorldInstance(301L);
        quest1.setQuestId("quest.shared");
        quest1.setState(QuestState.ACTIVE);
        WorldQuestStateEntity quest2 = new WorldQuestStateEntity();
        quest2.setIdWorldInstance(302L);
        quest2.setQuestId("quest.shared");
        quest2.setState(QuestState.FAILED);
        when(questStateRepository.findByIdWorldInstanceAndQuestId(301L, "quest.shared")).thenReturn(Optional.of(quest1));
        when(questStateRepository.findByIdWorldInstanceAndQuestId(302L, "quest.shared")).thenReturn(Optional.of(quest2));

        WorldInstanceEntity w1 = service.createWorldInstance(1L);
        WorldInstanceEntity w2 = service.createWorldInstance(2L);

        assertNotEquals(w1.getId(), w2.getId());
        assertEquals(QuestState.ACTIVE, service.getQuestState(w1.getId(), "quest.shared").orElseThrow().getState());
        assertEquals(QuestState.FAILED, service.getQuestState(w2.getId(), "quest.shared").orElseThrow().getState());
    }
}
