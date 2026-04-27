package br.eng.rodrigogml.mysteryrealms.domain.economy.model;

import br.eng.rodrigogml.mysteryrealms.domain.combat.enums.DamageType;
import br.eng.rodrigogml.mysteryrealms.domain.economy.enums.HandItemCategory;
import br.eng.rodrigogml.mysteryrealms.domain.economy.enums.HandItemSubtype;

/**
 * Ficha de arma — RF-EI-07.
 *
 * Inclui todos os attributes comuns de Item de Mão (RF-EI-03) mais campos específicos de arma.
 */
public class Weapon extends HandItem {

    private final String weaponTypeId;
    /** Notação de dado, ex.: {@code 1d8}, {@code 2d6}. */
    private final String baseDamageDie;
    private final DamageType damageType;
    private final String range;
    /** Faixa mínima do dado e multiplicador de crítico (textual, ex.: "19-20/x2"). */
    private final String criticalProfile;
    private final int itemPrecisionBonusFlat;
    private final double itemPrecisionBonusPct;
    private final int itemDamageBonusFlat;
    private final double itemDamageBonusPct;

    public Weapon(
            String name,
            int handsRequired,
            double weightKg,
            MonetaryValue basePrice,
            String weaponTypeId,
            String baseDamageDie,
            DamageType damageType,
            String range,
            String criticalProfile,
            int itemPrecisionBonusFlat,
            double itemPrecisionBonusPct,
            int itemDamageBonusFlat,
            double itemDamageBonusPct) {

        super(name, HandItemSubtype.ARMA, handsRequired, HandItemCategory.ATTACK, weightKg, basePrice);

        if (weaponTypeId == null || weaponTypeId.isBlank())
            throw new IllegalArgumentException("tipoArmaId não pode ser vazio");
        if (baseDamageDie == null || baseDamageDie.isBlank())
            throw new IllegalArgumentException("dadoDanoBase não pode ser vazio");
        if (damageType == null)
            throw new IllegalArgumentException("tipoDano não pode ser nulo");
        if (range == null || range.isBlank())
            throw new IllegalArgumentException("alcance não pode ser vazio");
        if (criticalProfile == null || criticalProfile.isBlank())
            throw new IllegalArgumentException("perfilCritico não pode ser vazio");

        this.weaponTypeId = weaponTypeId;
        this.baseDamageDie = baseDamageDie;
        this.damageType = damageType;
        this.range = range;
        this.criticalProfile = criticalProfile;
        this.itemPrecisionBonusFlat = itemPrecisionBonusFlat;
        this.itemPrecisionBonusPct = itemPrecisionBonusPct;
        this.itemDamageBonusFlat = itemDamageBonusFlat;
        this.itemDamageBonusPct = itemDamageBonusPct;
    }

    public String getWeaponTypeId() { return weaponTypeId; }
    public String getBaseDamageDie() { return baseDamageDie; }
    public DamageType getDamageType() { return damageType; }
    public String getRange() { return range; }
    public String getCriticalProfile() { return criticalProfile; }

    @Override public int getItemPrecisionBonusFlat() { return itemPrecisionBonusFlat; }
    @Override public double getItemPrecisionBonusPct() { return itemPrecisionBonusPct; }
    @Override public int getItemDamageBonusFlat() { return itemDamageBonusFlat; }
    @Override public double getItemDamageBonusPct() { return itemDamageBonusPct; }
}
