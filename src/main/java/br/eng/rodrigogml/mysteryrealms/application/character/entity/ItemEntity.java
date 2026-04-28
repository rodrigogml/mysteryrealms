package br.eng.rodrigogml.mysteryrealms.application.character.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidade JPA que representa um item do jogo (arma, armadura, objeto).
 *
 * @author ?
 * @since 28-04-2026
 */
@Entity
@Table(name = "item")
public class ItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String subtype;

    @Column(nullable = false, length = 50)
    private String useCategory;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private int handsRequired;

    @Column(nullable = false)
    private double weightKg;

    @Column(nullable = false)
    private long priceBaseMp;

    @Column(nullable = false)
    private long priceBaseMs;

    @Column(nullable = true, length = 100)
    private String weaponTypeId;

    @Column(nullable = true, length = 20)
    private String baseDamageDie;

    @Column(nullable = true, length = 50)
    private String damageType;

    @Column(nullable = true, length = 50)
    private String range;

    @Column(nullable = true, length = 20)
    private String criticalProfile;

    @Column(nullable = false)
    private int itemPrecisionBonusFlat;

    @Column(nullable = false)
    private double itemPrecisionBonusPct;

    @Column(nullable = false)
    private int itemDamageBonusFlat;

    @Column(nullable = false)
    private double itemDamageBonusPct;

    @Column(nullable = true)
    private Integer baseBlockValue;

    @Column(nullable = true, length = 255)
    private String coverageDescription;

    @Column(nullable = true)
    private Integer penaltyDexterity;

    @Column(nullable = false)
    private int itemDefenseBonusFlat;

    @Column(nullable = false)
    private double itemDefenseBonusPct;

    @Column(nullable = false)
    private int itemBlockBonusFlat;

    @Column(nullable = false)
    private double itemBlockBonusPct;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getSubtype() { return subtype; }

    public void setSubtype(String subtype) { this.subtype = subtype; }

    public String getUseCategory() { return useCategory; }

    public void setUseCategory(String useCategory) { this.useCategory = useCategory; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public int getHandsRequired() { return handsRequired; }

    public void setHandsRequired(int handsRequired) { this.handsRequired = handsRequired; }

    public double getWeightKg() { return weightKg; }

    public void setWeightKg(double weightKg) { this.weightKg = weightKg; }

    public long getPriceBaseMp() { return priceBaseMp; }

    public void setPriceBaseMp(long priceBaseMp) { this.priceBaseMp = priceBaseMp; }

    public long getPriceBaseMs() { return priceBaseMs; }

    public void setPriceBaseMs(long priceBaseMs) { this.priceBaseMs = priceBaseMs; }

    public String getWeaponTypeId() { return weaponTypeId; }

    public void setWeaponTypeId(String weaponTypeId) { this.weaponTypeId = weaponTypeId; }

    public String getBaseDamageDie() { return baseDamageDie; }

    public void setBaseDamageDie(String baseDamageDie) { this.baseDamageDie = baseDamageDie; }

    public String getDamageType() { return damageType; }

    public void setDamageType(String damageType) { this.damageType = damageType; }

    public String getRange() { return range; }

    public void setRange(String range) { this.range = range; }

    public String getCriticalProfile() { return criticalProfile; }

    public void setCriticalProfile(String criticalProfile) { this.criticalProfile = criticalProfile; }

    public int getItemPrecisionBonusFlat() { return itemPrecisionBonusFlat; }

    public void setItemPrecisionBonusFlat(int itemPrecisionBonusFlat) { this.itemPrecisionBonusFlat = itemPrecisionBonusFlat; }

    public double getItemPrecisionBonusPct() { return itemPrecisionBonusPct; }

    public void setItemPrecisionBonusPct(double itemPrecisionBonusPct) { this.itemPrecisionBonusPct = itemPrecisionBonusPct; }

    public int getItemDamageBonusFlat() { return itemDamageBonusFlat; }

    public void setItemDamageBonusFlat(int itemDamageBonusFlat) { this.itemDamageBonusFlat = itemDamageBonusFlat; }

    public double getItemDamageBonusPct() { return itemDamageBonusPct; }

    public void setItemDamageBonusPct(double itemDamageBonusPct) { this.itemDamageBonusPct = itemDamageBonusPct; }

    public Integer getBaseBlockValue() { return baseBlockValue; }

    public void setBaseBlockValue(Integer baseBlockValue) { this.baseBlockValue = baseBlockValue; }

    public String getCoverageDescription() { return coverageDescription; }

    public void setCoverageDescription(String coverageDescription) { this.coverageDescription = coverageDescription; }

    public Integer getPenaltyDexterity() { return penaltyDexterity; }

    public void setPenaltyDexterity(Integer penaltyDexterity) { this.penaltyDexterity = penaltyDexterity; }

    public int getItemDefenseBonusFlat() { return itemDefenseBonusFlat; }

    public void setItemDefenseBonusFlat(int itemDefenseBonusFlat) { this.itemDefenseBonusFlat = itemDefenseBonusFlat; }

    public double getItemDefenseBonusPct() { return itemDefenseBonusPct; }

    public void setItemDefenseBonusPct(double itemDefenseBonusPct) { this.itemDefenseBonusPct = itemDefenseBonusPct; }

    public int getItemBlockBonusFlat() { return itemBlockBonusFlat; }

    public void setItemBlockBonusFlat(int itemBlockBonusFlat) { this.itemBlockBonusFlat = itemBlockBonusFlat; }

    public double getItemBlockBonusPct() { return itemBlockBonusPct; }

    public void setItemBlockBonusPct(double itemBlockBonusPct) { this.itemBlockBonusPct = itemBlockBonusPct; }
}
