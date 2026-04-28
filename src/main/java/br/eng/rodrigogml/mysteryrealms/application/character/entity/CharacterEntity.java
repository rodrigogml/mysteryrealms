package br.eng.rodrigogml.mysteryrealms.application.character.entity;

import br.eng.rodrigogml.mysteryrealms.domain.character.enums.CharacterClass;
import br.eng.rodrigogml.mysteryrealms.domain.character.enums.Gender;
import br.eng.rodrigogml.mysteryrealms.domain.character.enums.Race;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

/**
 * Entidade JPA que representa um personagem jogável do usuário.
 *
 * @author ?
 * @since 28-04-2026
 */
@Entity
@Table(name = "character")
public class CharacterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long idUser;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 100)
    private String surname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Race race;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private CharacterClass characterClass;

    @Column(nullable = false)
    private int initialAge;

    @Column(nullable = false)
    private int strength;

    @Column(nullable = false)
    private int dexterity;

    @Column(nullable = false)
    private int constitution;

    @Column(nullable = false)
    private int intellect;

    @Column(nullable = false)
    private int willpower;

    @Column(nullable = false)
    private int perception;

    @Column(nullable = false)
    private int charisma;

    @Column(nullable = false)
    private int currentLevel;

    @Column(nullable = false)
    private long accumulatedXp;

    @Column(nullable = false, length = 50)
    private String balanceVersion;

    @Column(nullable = false)
    private double healthPoints;

    @Column(nullable = false)
    private double maxHealthPoints;

    @Column(nullable = false)
    private double currentFatigue;

    @Column(nullable = false)
    private double minFatigue;

    @Column(nullable = false)
    private double maxFatigue;

    @Column(nullable = false)
    private double hungerPct;

    @Column(nullable = false)
    private double thirstPct;

    @Column(nullable = false)
    private int morale;

    @Column(nullable = false)
    private long primaryCurrencyQty;

    @Column(nullable = false)
    private long secondaryCurrencyQty;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = true)
    private LocalDateTime lastAccessedAt;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Long getIdUser() { return idUser; }

    public void setIdUser(Long idUser) { this.idUser = idUser; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getSurname() { return surname; }

    public void setSurname(String surname) { this.surname = surname; }

    public Gender getGender() { return gender; }

    public void setGender(Gender gender) { this.gender = gender; }

    public Race getRace() { return race; }

    public void setRace(Race race) { this.race = race; }

    public CharacterClass getCharacterClass() { return characterClass; }

    public void setCharacterClass(CharacterClass characterClass) { this.characterClass = characterClass; }

    public int getInitialAge() { return initialAge; }

    public void setInitialAge(int initialAge) { this.initialAge = initialAge; }

    public int getStrength() { return strength; }

    public void setStrength(int strength) { this.strength = strength; }

    public int getDexterity() { return dexterity; }

    public void setDexterity(int dexterity) { this.dexterity = dexterity; }

    public int getConstitution() { return constitution; }

    public void setConstitution(int constitution) { this.constitution = constitution; }

    public int getIntellect() { return intellect; }

    public void setIntellect(int intellect) { this.intellect = intellect; }

    public int getWillpower() { return willpower; }

    public void setWillpower(int willpower) { this.willpower = willpower; }

    public int getPerception() { return perception; }

    public void setPerception(int perception) { this.perception = perception; }

    public int getCharisma() { return charisma; }

    public void setCharisma(int charisma) { this.charisma = charisma; }

    public int getCurrentLevel() { return currentLevel; }

    public void setCurrentLevel(int currentLevel) { this.currentLevel = currentLevel; }

    public long getAccumulatedXp() { return accumulatedXp; }

    public void setAccumulatedXp(long accumulatedXp) { this.accumulatedXp = accumulatedXp; }

    public String getBalanceVersion() { return balanceVersion; }

    public void setBalanceVersion(String balanceVersion) { this.balanceVersion = balanceVersion; }

    public double getHealthPoints() { return healthPoints; }

    public void setHealthPoints(double healthPoints) { this.healthPoints = healthPoints; }

    public double getMaxHealthPoints() { return maxHealthPoints; }

    public void setMaxHealthPoints(double maxHealthPoints) { this.maxHealthPoints = maxHealthPoints; }

    public double getCurrentFatigue() { return currentFatigue; }

    public void setCurrentFatigue(double currentFatigue) { this.currentFatigue = currentFatigue; }

    public double getMinFatigue() { return minFatigue; }

    public void setMinFatigue(double minFatigue) { this.minFatigue = minFatigue; }

    public double getMaxFatigue() { return maxFatigue; }

    public void setMaxFatigue(double maxFatigue) { this.maxFatigue = maxFatigue; }

    public double getHungerPct() { return hungerPct; }

    public void setHungerPct(double hungerPct) { this.hungerPct = hungerPct; }

    public double getThirstPct() { return thirstPct; }

    public void setThirstPct(double thirstPct) { this.thirstPct = thirstPct; }

    public int getMorale() { return morale; }

    public void setMorale(int morale) { this.morale = morale; }

    public long getPrimaryCurrencyQty() { return primaryCurrencyQty; }

    public void setPrimaryCurrencyQty(long primaryCurrencyQty) { this.primaryCurrencyQty = primaryCurrencyQty; }

    public long getSecondaryCurrencyQty() { return secondaryCurrencyQty; }

    public void setSecondaryCurrencyQty(long secondaryCurrencyQty) { this.secondaryCurrencyQty = secondaryCurrencyQty; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getLastAccessedAt() { return lastAccessedAt; }

    public void setLastAccessedAt(LocalDateTime lastAccessedAt) { this.lastAccessedAt = lastAccessedAt; }
}
