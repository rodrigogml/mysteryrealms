package br.eng.rodrigogml.mysteryrealms.application.game.service;

import br.eng.rodrigogml.mysteryrealms.application.character.entity.CharacterEntity;
import br.eng.rodrigogml.mysteryrealms.application.character.repository.CharacterRepository;
import br.eng.rodrigogml.mysteryrealms.application.game.dto.GameActionDTO;
import br.eng.rodrigogml.mysteryrealms.application.game.dto.GameSnapshotDTO;
import br.eng.rodrigogml.mysteryrealms.application.user.entity.SessionEntity;
import br.eng.rodrigogml.mysteryrealms.application.user.service.UserService;
import br.eng.rodrigogml.mysteryrealms.application.world.entity.WorldInstanceEntity;
import br.eng.rodrigogml.mysteryrealms.application.world.entity.WorldLocationStateEntity;
import br.eng.rodrigogml.mysteryrealms.application.world.repository.WorldInstanceRepository;
import br.eng.rodrigogml.mysteryrealms.application.world.repository.WorldLocationStateRepository;
import br.eng.rodrigogml.mysteryrealms.common.exception.ValidationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Serviço de aplicação responsável por montar o snapshot validado da sessão de jogo.
 *
 * @author ?
 * @since 08-05-2026
 */
@Service
@Transactional(readOnly = true)
public class GameSessionService {

    private static final String ACTION_EXPLORE = "explore";
    private static final String ACTION_TRAVEL = "travel";
    private static final String ACTION_REST = "rest";
    private static final String BLOCKED_CONTENT_UNAVAILABLE = "ui.game.contentUnavailable";

    private final UserService userService;
    private final CharacterRepository characterRepository;
    private final WorldInstanceRepository worldInstanceRepository;
    private final WorldLocationStateRepository worldLocationStateRepository;

    /**
     * Cria o serviço com as dependências necessárias para validar a sessão de jogo.
     *
     * @param userService serviço de autenticação/sessão
     * @param characterRepository repositório de personagens
     * @param worldInstanceRepository repositório de instâncias de mundo
     * @param worldLocationStateRepository repositório de estados de localidades
     */
    public GameSessionService(UserService userService, CharacterRepository characterRepository,
            WorldInstanceRepository worldInstanceRepository,
            WorldLocationStateRepository worldLocationStateRepository) {
        this.userService = userService;
        this.characterRepository = characterRepository;
        this.worldInstanceRepository = worldInstanceRepository;
        this.worldLocationStateRepository = worldLocationStateRepository;
    }

    /**
     * Carrega o contrato de aplicação da tela de jogo para um personagem selecionado.
     *
     * @param authToken token da sessão autenticada
     * @param selectedCharacterId identificador do personagem selecionado na UI
     * @return snapshot pronto para renderização da tela de jogo
     */
    public GameSnapshotDTO loadSnapshot(String authToken, Long selectedCharacterId) {
        if (authToken == null || authToken.isBlank()) {
            throw new ValidationException("user.error.invalidSession");
        }
        requirePositiveId(selectedCharacterId, "character.error.invalidCharacterId");

        SessionEntity authenticatedSession = userService.validateSession(authToken);
        if (authenticatedSession == null || authenticatedSession.getIdUser() == null) {
            throw new ValidationException("user.error.invalidSession");
        }

        CharacterEntity character = characterRepository.findById(selectedCharacterId)
                .orElseThrow(() -> new ValidationException("character.error.notFound"));
        if (!authenticatedSession.getIdUser().equals(character.getIdUser())) {
            throw new ValidationException("character.error.notOwned");
        }

        WorldInstanceEntity worldInstance = worldInstanceRepository.findByIdCharacter(selectedCharacterId)
                .orElseThrow(() -> new ValidationException("world.error.instanceNotFound"));
        WorldLocationStateEntity currentLocation = loadCurrentLocation(worldInstance);

        return toSnapshot(character, worldInstance, currentLocation);
    }

    private WorldLocationStateEntity loadCurrentLocation(WorldInstanceEntity worldInstance) {
        if (worldInstance.getId() == null) {
            throw new ValidationException("world.error.invalidWorldInstanceId");
        }
        if (worldInstance.getCurrentTimeMin() < 0L) {
            throw new ValidationException("world.error.invalidCurrentTime");
        }
        String currentLocationId = worldInstance.getCurrentLocationId();
        if (currentLocationId == null || currentLocationId.isBlank()) {
            throw new ValidationException("world.error.invalidLocationId");
        }

        return worldLocationStateRepository
                .findByIdWorldInstanceAndLocationId(worldInstance.getId(), currentLocationId)
                .orElseThrow(() -> new ValidationException("world.error.currentLocationStateNotFound"));
    }

    private GameSnapshotDTO toSnapshot(CharacterEntity character, WorldInstanceEntity worldInstance,
            WorldLocationStateEntity currentLocation) {
        GameSnapshotDTO snapshot = new GameSnapshotDTO();
        snapshot.setCharacterId(character.getId());
        snapshot.setWorldInstanceId(worldInstance.getId());
        snapshot.setCharacterName(character.getName());
        snapshot.setRace(character.getRace());
        snapshot.setCharacterClass(character.getCharacterClass());
        snapshot.setCurrentLevel(character.getCurrentLevel());
        snapshot.setCurrentLocationId(currentLocation.getLocationId());
        snapshot.setCurrentTimeMin(worldInstance.getCurrentTimeMin());
        snapshot.setHealthPoints(character.getHealthPoints());
        snapshot.setMaxHealthPoints(character.getMaxHealthPoints());
        snapshot.setCurrentFatigue(character.getCurrentFatigue());
        snapshot.setHungerPct(character.getHungerPct());
        snapshot.setThirstPct(character.getThirstPct());
        snapshot.setMorale(character.getMorale());
        snapshot.setActions(defaultBlockedActions());
        return snapshot;
    }

    private List<GameActionDTO> defaultBlockedActions() {
        return List.of(
                blockedAction(ACTION_EXPLORE, "ui.game.actionExplore", BLOCKED_CONTENT_UNAVAILABLE),
                blockedAction(ACTION_TRAVEL, "ui.game.actionTravel", BLOCKED_CONTENT_UNAVAILABLE),
                blockedAction(ACTION_REST, "ui.game.actionRest", BLOCKED_CONTENT_UNAVAILABLE));
    }

    private GameActionDTO blockedAction(String actionId, String labelMessageKey, String blockedReasonMessageKey) {
        GameActionDTO action = new GameActionDTO();
        action.setActionId(actionId);
        action.setLabelMessageKey(labelMessageKey);
        action.setAvailable(false);
        action.setBlockedReasonMessageKey(blockedReasonMessageKey);
        return action;
    }

    private void requirePositiveId(Long id, String message) {
        if (id == null || id <= 0L) {
            throw new ValidationException(message);
        }
    }
}
