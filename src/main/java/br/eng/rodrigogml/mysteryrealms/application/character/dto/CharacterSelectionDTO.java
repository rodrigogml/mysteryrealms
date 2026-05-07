package br.eng.rodrigogml.mysteryrealms.application.character.dto;

import br.eng.rodrigogml.mysteryrealms.domain.character.enums.CharacterClass;
import br.eng.rodrigogml.mysteryrealms.domain.character.enums.Race;

import java.time.LocalDateTime;

/**
 * DTO com o contrato REST de seleção de personagem e dados mínimos para iniciar a tela de jogo.
 *
 * @author ?
 * @since 07-05-2026
 */
public class CharacterSelectionDTO {

    private Long characterId;
    private Long worldInstanceId;
    private LocalDateTime lastAccessedAt;
    private String characterName;
    private Race race;
    private CharacterClass characterClass;
    private Integer currentLevel;
    private String currentLocationId;

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

    public LocalDateTime getLastAccessedAt() {
        return lastAccessedAt;
    }

    public void setLastAccessedAt(LocalDateTime lastAccessedAt) {
        this.lastAccessedAt = lastAccessedAt;
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
}
