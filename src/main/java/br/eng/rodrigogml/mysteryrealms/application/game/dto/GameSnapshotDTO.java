package br.eng.rodrigogml.mysteryrealms.application.game.dto;

import br.eng.rodrigogml.mysteryrealms.domain.character.enums.CharacterClass;
import br.eng.rodrigogml.mysteryrealms.domain.character.enums.Race;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * DTO com o contrato de aplicação da tela de jogo para renderização do estado atual.
 *
 * @author ?
 * @since 08-05-2026
 */
public class GameSnapshotDTO {

    private Long characterId;
    private Long worldInstanceId;
    private String characterName;
    private Race race;
    private CharacterClass characterClass;
    private Integer currentLevel;
    private String currentLocationId;
    private Long currentTimeMin;
    private Double healthPoints;
    private Double maxHealthPoints;
    private Double currentFatigue;
    private Double hungerPct;
    private Double thirstPct;
    private Integer morale;
    private List<GameActionDTO> actions = new ArrayList<>();

    public Long getCharacterId() {
        return characterId;
    }

    public void setCharacterId(Long characterId) {
        this.characterId = characterId;
    }

    public Long getWorldInstanceId() {
        return worldInstanceId;
    }

    public void setWorldInstanceId(Long worldInstanceId) {
        this.worldInstanceId = worldInstanceId;
    }

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public CharacterClass getCharacterClass() {
        return characterClass;
    }

    public void setCharacterClass(CharacterClass characterClass) {
        this.characterClass = characterClass;
    }

    public Integer getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(Integer currentLevel) {
        this.currentLevel = currentLevel;
    }

    public String getCurrentLocationId() {
        return currentLocationId;
    }

    public void setCurrentLocationId(String currentLocationId) {
        this.currentLocationId = currentLocationId;
    }

    public Long getCurrentTimeMin() {
        return currentTimeMin;
    }

    public void setCurrentTimeMin(Long currentTimeMin) {
        this.currentTimeMin = currentTimeMin;
    }

    public Double getHealthPoints() {
        return healthPoints;
    }

    public void setHealthPoints(Double healthPoints) {
        this.healthPoints = healthPoints;
    }

    public Double getMaxHealthPoints() {
        return maxHealthPoints;
    }

    public void setMaxHealthPoints(Double maxHealthPoints) {
        this.maxHealthPoints = maxHealthPoints;
    }

    public Double getCurrentFatigue() {
        return currentFatigue;
    }

    public void setCurrentFatigue(Double currentFatigue) {
        this.currentFatigue = currentFatigue;
    }

    public Double getHungerPct() {
        return hungerPct;
    }

    public void setHungerPct(Double hungerPct) {
        this.hungerPct = hungerPct;
    }

    public Double getThirstPct() {
        return thirstPct;
    }

    public void setThirstPct(Double thirstPct) {
        this.thirstPct = thirstPct;
    }

    public Integer getMorale() {
        return morale;
    }

    public void setMorale(Integer morale) {
        this.morale = morale;
    }

    public List<GameActionDTO> getActions() {
        return Collections.unmodifiableList(actions);
    }

    public void setActions(List<GameActionDTO> actions) {
        this.actions = actions == null ? new ArrayList<>() : new ArrayList<>(actions);
    }
}
