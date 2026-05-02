package br.eng.rodrigogml.mysteryrealms.application.character.dto;

import br.eng.rodrigogml.mysteryrealms.domain.character.enums.CharacterClass;
import br.eng.rodrigogml.mysteryrealms.domain.character.enums.Gender;
import br.eng.rodrigogml.mysteryrealms.domain.character.enums.Race;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO mutável para criação de personagem no fluxo pós-login.
 *
 * @author ?
 * @since 01-05-2026
 */
public class CharacterCreationDTO {

    @NotBlank
    @Size(max = 100)
    private String name;
    @NotBlank
    @Size(max = 100)
    private String surname;
    @NotNull
    private Gender gender;
    @NotNull
    @Min(12)
    @Max(120)
    private Integer initialAge;
    @NotNull
    private Race race;
    @NotNull
    private CharacterClass characterClass;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Integer getInitialAge() {
        return initialAge;
    }

    public void setInitialAge(Integer initialAge) {
        this.initialAge = initialAge;
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
}
