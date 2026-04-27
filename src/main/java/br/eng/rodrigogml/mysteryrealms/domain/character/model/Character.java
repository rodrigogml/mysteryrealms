package br.eng.rodrigogml.mysteryrealms.domain.character.model;

import br.eng.rodrigogml.mysteryrealms.domain.character.enums.CharacterClass;
import br.eng.rodrigogml.mysteryrealms.domain.character.enums.Gender;
import br.eng.rodrigogml.mysteryrealms.domain.character.enums.Race;
import br.eng.rodrigogml.mysteryrealms.domain.character.service.CharacterAttributeService;
import br.eng.rodrigogml.mysteryrealms.domain.character.service.ProgressionService;
import br.eng.rodrigogml.mysteryrealms.domain.economy.model.HandItem;
import br.eng.rodrigogml.mysteryrealms.domain.economy.model.MonetaryValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Agregado raiz do personagem jogador — RF-FP-01 a RF-FP-09.
 *
 * Os attributes derivados (PV máximo, fadiga máxima, peso etc.) são calculados
 * automaticamente a partir dos attributes base usando {@link CharacterAttributeService}.
 */
public class Character {

    // ── RF-FP-01: Identidade ──────────────────────────────────────────────────
    private final String name;
    private final String surname;
    private final Gender gender;
    private final Race raca;
    private final CharacterClass classe;
    private final int initialAge;

    // ── RF-FP-02: Atributos principais ───────────────────────────────────────
    private AttributeSet attributes;

    // ── RF-FP-04: Estado dinâmico ─────────────────────────────────────────────
    private long accumulatedXp;       // >= 0
    private int currentLevel;         // >= 1
    private String balanceVersion;  // ex.: "BAL-1.0.0"
    private double healthPoints;      // [0, maxHealthPoints]
    private double maxHealthPoints;   // = constitution × 10
    private double currentFatigue;     // >= minFatigue
    private double minFatigue;       // cresce com o tempo acordado
    private double maxFatigue;       // = (const + vont) × 100
    private double hungerPct;         // [0, 100]
    private double thirstPct;         // [0, 100]
    private int morale;              // [0, 100]

    // ── RF-FP-05: Inventário e recursos ──────────────────────────────────────
    private long primaryCurrencyQty;   // >= 0
    private long secondaryCurrencyQty; // >= 0
    /** Máximo de 2 mãos — RF-EI-05. */
    private final List<HandItem> equippedItems;
    private final List<HandItem> backpackItems;

    // ── RF-FP-09: Relacionamento e reputação ─────────────────────────────────
    /** Escala [-100, 100] — RF-FP-09. */
    private final Map<String, Integer> npcRelationship;
    /** Sem limite fixed — RF-FP-09. */
    private final Map<String, Integer> localityReputation;
    /** Sem limite fixed — RF-FP-09. */
    private final Map<String, Integer> factionReputation;

    /**
     * Cria um personagem de nível 1 com attributes derivados da combinação raça + classe — RF-FP-07, RF-FP-08.
     */
    public Character(
            String name,
            String surname,
            Gender gender,
            Race raca,
            CharacterClass classe,
            int initialAge) {

        if (name == null || name.isBlank())
            throw new IllegalArgumentException("nome não pode ser vazio");
        if (surname == null || surname.isBlank())
            throw new IllegalArgumentException("sobrenome não pode ser vazio");
        if (gender == null)
            throw new IllegalArgumentException("genero não pode ser nulo");
        if (raca == null)
            throw new IllegalArgumentException("raca não pode ser nula");
        if (classe == null)
            throw new IllegalArgumentException("classe não pode ser nula");
        if (initialAge < 1)
            throw new IllegalArgumentException("idadeInicial deve ser >= 1, recebido: " + initialAge);

        this.name = name;
        this.surname = surname;
        this.gender = gender;
        this.raca = raca;
        this.classe = classe;
        this.initialAge = initialAge;

        // Atributos: base da raça + bônus da classe (RF-FP-07, RF-FP-08)
        this.attributes = raca.getBaseAttributes().add(classe.getAttributeBonus());

        // Estado initial
        this.accumulatedXp = 0;
        this.currentLevel = 1;
        this.balanceVersion = ProgressionService.BALANCE_VERSION;
        this.maxHealthPoints = CharacterAttributeService.maxHealthPoints(attributes.constitution());
        this.healthPoints = this.maxHealthPoints;
        this.maxFatigue = CharacterAttributeService.maxFatigue(attributes.constitution(), attributes.willpower());
        this.minFatigue = 0.0;
        this.currentFatigue = 0.0;
        this.hungerPct = 0.0;
        this.thirstPct = 0.0;
        this.morale = 75;

        // Inventário empty
        this.primaryCurrencyQty = 0;
        this.secondaryCurrencyQty = 0;
        this.equippedItems = new ArrayList<>();
        this.backpackItems = new ArrayList<>();

        // Relacionamentos vazios
        this.npcRelationship = new HashMap<>();
        this.localityReputation = new HashMap<>();
        this.factionReputation = new HashMap<>();
    }

    // ── Identidade ────────────────────────────────────────────────────────────
    public String getName() { return name; }
    public String getSurname() { return surname; }
    public Gender getGender() { return gender; }
    public Race getRace() { return raca; }
    public CharacterClass getCharacterClass() { return classe; }
    public int getInitialAge() { return initialAge; }

    // ── Atributos ─────────────────────────────────────────────────────────────
    public AttributeSet getAttributes() { return attributes; }

    /**
     * Atualiza os attributes e recalcula todos os derivados — RF-FP-10.
     */
    public void setAttributes(AttributeSet attributes) {
        if (attributes == null) throw new IllegalArgumentException("atributos não podem ser nulos");
        this.attributes = attributes;
        recalculateDerived();
    }

    // ── Estado dinâmico ───────────────────────────────────────────────────────
    public long getAccumulatedXp() { return accumulatedXp; }

    public void setAccumulatedXp(long accumulatedXp) {
        if (accumulatedXp < 0) throw new IllegalArgumentException("xpAcumulado deve ser >= 0");
        this.accumulatedXp = accumulatedXp;
    }

    public int getCurrentLevel() { return currentLevel; }

    public void setCurrentLevel(int currentLevel) {
        if (currentLevel < 1) throw new IllegalArgumentException("nivelAtual deve ser >= 1");
        this.currentLevel = currentLevel;
    }

    public String getBalanceVersion() { return balanceVersion; }
    public void setBalanceVersion(String balanceVersion) { this.balanceVersion = balanceVersion; }

    public double getHealthPoints() { return healthPoints; }

    public void setHealthPoints(double healthPoints) {
        this.healthPoints = Math.max(0.0, Math.min(maxHealthPoints, healthPoints));
    }

    public double getMaxHealthPoints() { return maxHealthPoints; }

    public double getCurrentFatigue() { return currentFatigue; }

    public void setCurrentFatigue(double currentFatigue) {
        this.currentFatigue = Math.max(minFatigue, currentFatigue);
    }

    public double getMinFatigue() { return minFatigue; }

    public void setMinFatigue(double minFatigue) {
        this.minFatigue = Math.max(0.0, minFatigue);
        // Garante invariante: currentFatigue >= minFatigue
        if (this.currentFatigue < this.minFatigue) {
            this.currentFatigue = this.minFatigue;
        }
    }

    public double getMaxFatigue() { return maxFatigue; }

    public double getHungerPct() { return hungerPct; }

    public void setHungerPct(double hungerPct) {
        this.hungerPct = Math.max(0.0, Math.min(100.0, hungerPct));
    }

    public double getThirstPct() { return thirstPct; }

    public void setThirstPct(double thirstPct) {
        this.thirstPct = Math.max(0.0, Math.min(100.0, thirstPct));
    }

    public int getMorale() { return morale; }

    public void setMorale(int morale) {
        this.morale = Math.max(0, Math.min(100, morale));
    }

    // ── Inventário ────────────────────────────────────────────────────────────
    public long getPrimaryCurrencyQty() { return primaryCurrencyQty; }

    public void setPrimaryCurrencyQty(long primaryCurrencyQty) {
        if (primaryCurrencyQty < 0) throw new IllegalArgumentException("qtdMoedaPrimaria deve ser >= 0");
        this.primaryCurrencyQty = primaryCurrencyQty;
    }

    public long getSecondaryCurrencyQty() { return secondaryCurrencyQty; }

    public void setSecondaryCurrencyQty(long secondaryCurrencyQty) {
        if (secondaryCurrencyQty < 0) throw new IllegalArgumentException("qtdMoedaSecundaria deve ser >= 0");
        this.secondaryCurrencyQty = secondaryCurrencyQty;
    }

    public MonetaryValue getCurrency() {
        return MonetaryValue.of(primaryCurrencyQty, secondaryCurrencyQty);
    }

    /** Itens equipados (máx. 2 mãos) — RF-EI-05. */
    public List<HandItem> getEquippedItems() {
        return Collections.unmodifiableList(equippedItems);
    }

    public List<HandItem> getBackpackItems() {
        return Collections.unmodifiableList(backpackItems);
    }

    /**
     * Equipa um item nas mãos do personagem — RF-EI-05.
     * Valida que não excedam 2 mãos ocupáveis.
     */
    public void equipItem(HandItem item) {
        if (item == null) throw new IllegalArgumentException("item não pode ser nulo");
        int maosOcupadas = equippedItems.stream().mapToInt(HandItem::getHandsRequired).sum();
        if (maosOcupadas + item.getHandsRequired() > 2) {
            throw new IllegalStateException("Não há mãos disponíveis para equipar o item '" + item.getName() + "'");
        }
        equippedItems.add(item);
    }

    /** Remove o item equipado pelo índice (0-based). */
    public HandItem unequipItem(int index) {
        return equippedItems.remove(index);
    }

    public void addToBackpack(HandItem item) {
        if (item == null) throw new IllegalArgumentException("item não pode ser nulo");
        backpackItems.add(item);
    }

    public HandItem removeFromBackpack(int index) {
        return backpackItems.remove(index);
    }

    // ── Carga ─────────────────────────────────────────────────────────────────

    /** Carga atual em kg (equipados + mochila + moedas) — RF-FP-06.4. */
    public double getCurrentLoadKg() {
        double pesoEquipados = equippedItems.stream().mapToDouble(HandItem::getWeightKg).sum();
        double pesoBolsa = backpackItems.stream().mapToDouble(HandItem::getWeightKg).sum();
        double currencyWeight = MonetaryValue.of(primaryCurrencyQty, secondaryCurrencyQty).weightKg();
        return CharacterAttributeService.currentLoad(pesoEquipados, pesoBolsa, currencyWeight);
    }

    // ── RF-FP-09: Relacionamento e reputação ─────────────────────────────────

    /**
     * Retorna o value de relacionamento com o NPC indicado (0 se não registrado).
     */
    public int getNpcRelationship(String npcId) {
        return npcRelationship.getOrDefault(npcId, 0);
    }

    /**
     * Define o relacionamento com um NPC, clampado em [-100, 100] — RF-FP-09.
     */
    public void setNpcRelationship(String npcId, int value) {
        if (npcId == null || npcId.isBlank()) throw new IllegalArgumentException("npcId não pode ser vazio");
        npcRelationship.put(npcId, Math.max(-100, Math.min(100, value)));
    }

    public Map<String, Integer> getNpcRelationships() {
        return Collections.unmodifiableMap(npcRelationship);
    }

    public int getLocalityReputation(String locId) {
        return localityReputation.getOrDefault(locId, 0);
    }

    public void setLocalityReputation(String locId, int value) {
        if (locId == null || locId.isBlank()) throw new IllegalArgumentException("locId não pode ser vazio");
        localityReputation.put(locId, value);
    }

    public Map<String, Integer> getLocalityReputations() {
        return Collections.unmodifiableMap(localityReputation);
    }

    public int getFactionReputation(String faccaoId) {
        return factionReputation.getOrDefault(faccaoId, 0);
    }

    public void setFactionReputation(String faccaoId, int value) {
        if (faccaoId == null || faccaoId.isBlank()) throw new IllegalArgumentException("faccaoId não pode ser vazio");
        factionReputation.put(faccaoId, value);
    }

    public Map<String, Integer> getFactionReputations() {
        return Collections.unmodifiableMap(factionReputation);
    }

    // ── RF-FP-10: Recálculo de derivados ─────────────────────────────────────

    /**
     * Recalcula attributes derivados sempre que os attributes base mudam — RF-FP-10.
     */
    private void recalculateDerived() {
        double novoPvMax = CharacterAttributeService.maxHealthPoints(attributes.constitution());
        double novaFadigaMax = CharacterAttributeService.maxFatigue(attributes.constitution(), attributes.willpower());

        // Ajustar PV proporcionalmente se o max mudou
        if (maxHealthPoints > 0) {
            this.healthPoints = Math.min(healthPoints, novoPvMax);
        }
        this.maxHealthPoints = novoPvMax;
        this.maxFatigue = novaFadigaMax;
    }
}
