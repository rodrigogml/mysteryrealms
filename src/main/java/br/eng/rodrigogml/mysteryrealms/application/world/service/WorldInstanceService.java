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

import java.util.Optional;

/**
 * Serviço de aplicação responsável pelo gerenciamento de instâncias de mundo,
 * estados de quests, NPCs, localidades e marcadores de progresso.
 *
 * @author ?
 * @since 28-04-2026
 */
public class WorldInstanceService {

    private final WorldInstanceRepository worldInstanceRepository;
    private final WorldQuestStateRepository questStateRepository;
    private final WorldNpcStateRepository npcStateRepository;
    private final WorldLocationStateRepository locationStateRepository;
    private final WorldMarkerRepository markerRepository;
    private final DiaryEntryRepository diaryEntryRepository;

    /**
     * Cria o serviço com as dependências necessárias.
     *
     * @param worldInstanceRepository  repositório de instâncias de mundo
     * @param questStateRepository     repositório de estados de quests
     * @param npcStateRepository       repositório de estados de NPCs
     * @param locationStateRepository  repositório de estados de localidades
     * @param markerRepository         repositório de marcadores
     * @param diaryEntryRepository     repositório de entradas de diário
     */
    public WorldInstanceService(WorldInstanceRepository worldInstanceRepository,
            WorldQuestStateRepository questStateRepository,
            WorldNpcStateRepository npcStateRepository,
            WorldLocationStateRepository locationStateRepository,
            WorldMarkerRepository markerRepository,
            DiaryEntryRepository diaryEntryRepository) {
        this.worldInstanceRepository = worldInstanceRepository;
        this.questStateRepository = questStateRepository;
        this.npcStateRepository = npcStateRepository;
        this.locationStateRepository = locationStateRepository;
        this.markerRepository = markerRepository;
        this.diaryEntryRepository = diaryEntryRepository;
    }

    /**
     * Cria uma nova instância de mundo para um personagem.
     *
     * @param characterId o ID do personagem
     * @return a instância de mundo criada
     */
    public WorldInstanceEntity createWorldInstance(Long characterId) {
        requirePositiveId(characterId, "world.error.invalidCharacterId");

        if (worldInstanceRepository.findByIdCharacter(characterId).isPresent()) {
            throw new IllegalArgumentException("world.error.instanceAlreadyExists");
        }

        WorldInstanceEntity instance = new WorldInstanceEntity();
        instance.setIdCharacter(characterId);
        instance.setCurrentTimeMin(0);
        instance.setCurrentLocationId(null);
        return worldInstanceRepository.save(instance);
    }

    /**
     * Carrega a instância de mundo de um personagem.
     *
     * @param characterId o ID do personagem
     * @return a instância de mundo encontrada
     * @throws IllegalArgumentException se a instância não for encontrada
     */
    public WorldInstanceEntity loadWorldInstance(Long characterId) {
        requirePositiveId(characterId, "world.error.invalidCharacterId");

        return worldInstanceRepository.findByIdCharacter(characterId)
                .orElseThrow(() -> new IllegalArgumentException("world.error.instanceNotFound"));
    }

    /**
     * Persiste o estado atual de uma instância de mundo.
     *
     * @param state a instância de mundo a salvar
     * @return a instância salva
     */
    public WorldInstanceEntity saveWorldInstance(WorldInstanceEntity state) {
        if (state == null) {
            throw new IllegalArgumentException("world.error.instanceRequired");
        }
        requirePositiveId(state.getIdCharacter(), "world.error.invalidCharacterId");
        if (state.getCurrentTimeMin() < 0) {
            throw new IllegalArgumentException("world.error.invalidCurrentTime");
        }

        return worldInstanceRepository.save(state);
    }

    /**
     * Obtém o estado de uma quest em uma instância de mundo.
     *
     * @param worldInstanceId o ID da instância de mundo
     * @param questId         o ID da quest
     * @return o estado da quest, ou vazio se não registrado
     */
    public Optional<WorldQuestStateEntity> getQuestState(Long worldInstanceId, String questId) {
        requirePositiveId(worldInstanceId, "world.error.invalidWorldInstanceId");
        requireText(questId, "world.error.invalidQuestId");

        return questStateRepository.findByIdWorldInstanceAndQuestId(worldInstanceId, questId);
    }

    /**
     * Define o estado de uma quest em uma instância de mundo.
     *
     * @param worldInstanceId o ID da instância de mundo
     * @param questId         o ID da quest
     * @param state           o novo estado da quest
     */
    public void setQuestState(Long worldInstanceId, String questId, QuestState state) {
        requirePositiveId(worldInstanceId, "world.error.invalidWorldInstanceId");
        requireText(questId, "world.error.invalidQuestId");
        if (state == null) {
            throw new IllegalArgumentException("world.error.invalidQuestState");
        }

        WorldQuestStateEntity entity = questStateRepository
                .findByIdWorldInstanceAndQuestId(worldInstanceId, questId)
                .orElseGet(WorldQuestStateEntity::new);
        entity.setIdWorldInstance(worldInstanceId);
        entity.setQuestId(questId);
        entity.setState(state);
        questStateRepository.save(entity);
    }

    /**
     * Obtém o estado de um NPC em uma instância de mundo.
     *
     * @param worldInstanceId o ID da instância de mundo
     * @param npcId           o ID do NPC
     * @return o estado do NPC, ou vazio se não registrado
     */
    public Optional<WorldNpcStateEntity> getNpcState(Long worldInstanceId, String npcId) {
        requirePositiveId(worldInstanceId, "world.error.invalidWorldInstanceId");
        requireText(npcId, "world.error.invalidNpcId");

        return npcStateRepository.findByIdWorldInstanceAndNpcId(worldInstanceId, npcId);
    }

    /**
     * Define o estado de um NPC em uma instância de mundo.
     *
     * @param worldInstanceId o ID da instância de mundo
     * @param npcId           o ID do NPC
     * @param alive           se o NPC está vivo
     * @param dialogState     o estado atual do diálogo do NPC
     */
    public void setNpcState(Long worldInstanceId, String npcId, boolean alive, String dialogState) {
        requirePositiveId(worldInstanceId, "world.error.invalidWorldInstanceId");
        requireText(npcId, "world.error.invalidNpcId");

        WorldNpcStateEntity entity = npcStateRepository
                .findByIdWorldInstanceAndNpcId(worldInstanceId, npcId)
                .orElseGet(WorldNpcStateEntity::new);
        entity.setIdWorldInstance(worldInstanceId);
        entity.setNpcId(npcId);
        entity.setAlive(alive);
        entity.setDialogState(dialogState);
        npcStateRepository.save(entity);
    }

    /**
     * Obtém o estado de uma localidade em uma instância de mundo.
     *
     * @param worldInstanceId o ID da instância de mundo
     * @param locationId      o ID da localidade
     * @return o estado da localidade, ou vazio se não registrado
     */
    public Optional<WorldLocationStateEntity> getLocationState(Long worldInstanceId, String locationId) {
        requirePositiveId(worldInstanceId, "world.error.invalidWorldInstanceId");
        requireText(locationId, "world.error.invalidLocationId");

        return locationStateRepository.findByIdWorldInstanceAndLocationId(worldInstanceId, locationId);
    }

    /**
     * Define o estado de uma localidade em uma instância de mundo.
     *
     * @param worldInstanceId o ID da instância de mundo
     * @param locationId      o ID da localidade
     * @param discovered      se a localidade foi descoberta
     * @param accessible      se a localidade está acessível
     */
    public void setLocationState(Long worldInstanceId, String locationId, boolean discovered, boolean accessible) {
        requirePositiveId(worldInstanceId, "world.error.invalidWorldInstanceId");
        requireText(locationId, "world.error.invalidLocationId");

        WorldLocationStateEntity entity = locationStateRepository
                .findByIdWorldInstanceAndLocationId(worldInstanceId, locationId)
                .orElseGet(WorldLocationStateEntity::new);
        entity.setIdWorldInstance(worldInstanceId);
        entity.setLocationId(locationId);
        entity.setDiscovered(discovered);
        entity.setAccessible(accessible);
        locationStateRepository.save(entity);
    }

    /**
     * Obtém um marcador de progresso em uma instância de mundo.
     *
     * @param worldInstanceId o ID da instância de mundo
     * @param markerId        o ID do marcador
     * @return o marcador encontrado, ou vazio
     */
    public Optional<WorldMarkerEntity> getMarker(Long worldInstanceId, String markerId) {
        requirePositiveId(worldInstanceId, "world.error.invalidWorldInstanceId");
        requireText(markerId, "world.error.invalidMarkerId");

        return markerRepository.findByIdWorldInstanceAndMarkerId(worldInstanceId, markerId);
    }

    /**
     * Define um marcador de progresso em uma instância de mundo.
     *
     * @param worldInstanceId o ID da instância de mundo
     * @param markerId        o ID do marcador
     * @param type            o tipo do marcador
     * @param value           o valor do marcador (Boolean para FLAG, Integer para COUNTER/STAGE, null para limpar)
     */
    public void setMarker(Long worldInstanceId, String markerId, MarkerType type, Object value) {
        requirePositiveId(worldInstanceId, "world.error.invalidWorldInstanceId");
        requireText(markerId, "world.error.invalidMarkerId");
        if (type == null) {
            throw new IllegalArgumentException("world.error.invalidMarkerType");
        }
        if (value != null && type == MarkerType.FLAG && !(value instanceof Boolean)) {
            throw new IllegalArgumentException("world.error.invalidMarkerValue");
        }
        if (value != null && type != MarkerType.FLAG && !(value instanceof Integer)) {
            throw new IllegalArgumentException("world.error.invalidMarkerValue");
        }

        WorldMarkerEntity entity = markerRepository
                .findByIdWorldInstanceAndMarkerId(worldInstanceId, markerId)
                .orElseGet(WorldMarkerEntity::new);
        entity.setIdWorldInstance(worldInstanceId);
        entity.setMarkerId(markerId);
        entity.setMarkerType(type);

        if (value instanceof Boolean) {
            entity.setFlagValue((Boolean) value);
            entity.setIntValue(null);
        } else if (value instanceof Integer) {
            entity.setIntValue((Integer) value);
            entity.setFlagValue(null);
        } else {
            entity.setFlagValue(null);
            entity.setIntValue(null);
        }

        markerRepository.save(entity);
    }

    private void requirePositiveId(Long id, String message) {
        if (id == null || id <= 0L) {
            throw new IllegalArgumentException(message);
        }
    }

    private void requireText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
    }
}
