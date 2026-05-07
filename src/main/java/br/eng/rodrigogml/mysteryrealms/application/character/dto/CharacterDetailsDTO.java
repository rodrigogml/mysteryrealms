package br.eng.rodrigogml.mysteryrealms.application.character.dto;

import br.eng.rodrigogml.mysteryrealms.domain.character.enums.CharacterClass;
import br.eng.rodrigogml.mysteryrealms.domain.character.enums.Gender;
import br.eng.rodrigogml.mysteryrealms.domain.character.enums.Race;

import java.time.LocalDateTime;

/**
 * DTO com detalhes seguros do personagem para contratos REST sem expor a entidade JPA.
 *
 * @author ?
 * @since 07-05-2026
 */
public class CharacterDetailsDTO {

    private Long id;
    private String name;
    private String surname;
    private Gender gender;
    private Race race;
    private CharacterClass characterClass;
    private Integer initialAge;
    private Integer currentLevel;
    private Long accumulatedXp;
    private Double healthPoints;
    private Double maxHealthPoints;
    private Double currentFatigue;
    private Double minFatigue;
    private Double maxFatigue;
    private Double hungerPct;
    private Double thirstPct;
    private Integer morale;
    private Long primaryCurrencyQty;
    private Long secondaryCurrencyQty;
    private LocalDateTime createdAt;
    private LocalDateTime lastAccessedAt;
    private Long worldInstanceId;
    private String currentLocationId;

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

    public Integer getInitialAge() {
        return initialAge;
    }

    public void setInitialAge(Integer initialAge) {
        this.initialAge = initialAge;
    }

    public Integer getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(Integer currentLevel) {
        this.currentLevel = currentLevel;
    }

    public Long getAccumulatedXp() {
        return accumulatedXp;
    }

    public void setAccumulatedXp(Long accumulatedXp) {
        this.accumulatedXp = accumulatedXp;
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

    public Double getMinFatigue() {
        return minFatigue;
    }

    public void setMinFatigue(Double minFatigue) {
        this.minFatigue = minFatigue;
    }

    public Double getMaxFatigue() {
        return maxFatigue;
    }

    public void setMaxFatigue(Double maxFatigue) {
        this.maxFatigue = maxFatigue;
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

    public Long getPrimaryCurrencyQty() {
        return primaryCurrencyQty;
    }

    public void setPrimaryCurrencyQty(Long primaryCurrencyQty) {
        this.primaryCurrencyQty = primaryCurrencyQty;
    }

    public Long getSecondaryCurrencyQty() {
        return secondaryCurrencyQty;
    }

    public void setSecondaryCurrencyQty(Long secondaryCurrencyQty) {
        this.secondaryCurrencyQty = secondaryCurrencyQty;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastAccessedAt() {
        return lastAccessedAt;
    }

    public void setLastAccessedAt(LocalDateTime lastAccessedAt) {
        this.lastAccessedAt = lastAccessedAt;
    }

    public Long getWorldInstanceId() {
        return worldInstanceId;
    }

    public void setWorldInstanceId(Long worldInstanceId) {
        this.worldInstanceId = worldInstanceId;
    }

    public String getCurrentLocationId() {
        return currentLocationId;
    }

    public void setCurrentLocationId(String currentLocationId) {
        this.currentLocationId = currentLocationId;
    }
}
