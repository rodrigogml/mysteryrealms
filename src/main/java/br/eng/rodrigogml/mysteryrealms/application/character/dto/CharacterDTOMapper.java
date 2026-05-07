package br.eng.rodrigogml.mysteryrealms.application.character.dto;

import br.eng.rodrigogml.mysteryrealms.application.character.entity.CharacterEntity;
import br.eng.rodrigogml.mysteryrealms.application.world.entity.WorldInstanceEntity;

/**
 * Mapeador dos contratos REST de personagem para evitar duplicação entre fluxos de listagem e seleção.
 *
 * @author ?
 * @since 07-05-2026
 */
public final class CharacterDTOMapper {

    private CharacterDTOMapper() {
    }

    public static CharacterSummaryDTO toSummary(CharacterEntity entity) {
        CharacterSummaryDTO summary = new CharacterSummaryDTO();
        summary.setId(entity.getId());
        summary.setName(entity.getName());
        summary.setRace(entity.getRace());
        summary.setCharacterClass(entity.getCharacterClass());
        summary.setCurrentLevel(entity.getCurrentLevel());
        summary.setLastAccessedAt(entity.getLastAccessedAt());
        return summary;
    }

    public static CharacterSelectionDTO toSelection(CharacterEntity character, WorldInstanceEntity worldInstance) {
        CharacterSelectionDTO selection = new CharacterSelectionDTO();
        selection.setCharacterId(character.getId());
        selection.setName(character.getName());
        selection.setRace(character.getRace());
        selection.setCharacterClass(character.getCharacterClass());
        selection.setCurrentLevel(character.getCurrentLevel());
        selection.setLastAccessedAt(character.getLastAccessedAt());
        if (worldInstance != null) {
            selection.setWorldInstanceId(worldInstance.getId());
            selection.setCurrentLocationId(worldInstance.getCurrentLocationId());
        }
        return selection;
    }

    public static CharacterDetailsDTO toDetails(CharacterEntity character, WorldInstanceEntity worldInstance) {
        CharacterDetailsDTO details = new CharacterDetailsDTO();
        details.setId(character.getId());
        details.setName(character.getName());
        details.setSurname(character.getSurname());
        details.setGender(character.getGender());
        details.setRace(character.getRace());
        details.setCharacterClass(character.getCharacterClass());
        details.setInitialAge(character.getInitialAge());
        details.setCurrentLevel(character.getCurrentLevel());
        details.setAccumulatedXp(character.getAccumulatedXp());
        details.setHealthPoints(character.getHealthPoints());
        details.setMaxHealthPoints(character.getMaxHealthPoints());
        details.setCurrentFatigue(character.getCurrentFatigue());
        details.setMinFatigue(character.getMinFatigue());
        details.setMaxFatigue(character.getMaxFatigue());
        details.setHungerPct(character.getHungerPct());
        details.setThirstPct(character.getThirstPct());
        details.setMorale(character.getMorale());
        details.setPrimaryCurrencyQty(character.getPrimaryCurrencyQty());
        details.setSecondaryCurrencyQty(character.getSecondaryCurrencyQty());
        details.setCreatedAt(character.getCreatedAt());
        details.setLastAccessedAt(character.getLastAccessedAt());
        if (worldInstance != null) {
            details.setWorldInstanceId(worldInstance.getId());
            details.setCurrentLocationId(worldInstance.getCurrentLocationId());
        }
        return details;
    }
}
