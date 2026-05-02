package br.eng.rodrigogml.mysteryrealms.application.character.dto;

import br.eng.rodrigogml.mysteryrealms.domain.character.enums.CharacterClass;
import br.eng.rodrigogml.mysteryrealms.domain.character.enums.Race;

import java.time.LocalDateTime;

/**
 * DTO de resumo da listagem de personagens pós-login.
 *
 * @author ?
 * @since 01-05-2026
 */
public class CharacterSummaryDTO {

    private Long id;
    private String name;
    private Race race;
    private CharacterClass characterClass;
    private Integer currentLevel;
    private LocalDateTime lastAccessedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public LocalDateTime getLastAccessedAt() {
        return lastAccessedAt;
    }

    public void setLastAccessedAt(LocalDateTime lastAccessedAt) {
        this.lastAccessedAt = lastAccessedAt;
    }
}
