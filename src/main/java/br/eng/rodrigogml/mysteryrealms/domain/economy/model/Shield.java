package br.eng.rodrigogml.mysteryrealms.domain.economy.model;

import br.eng.rodrigogml.mysteryrealms.domain.economy.enums.HandItemCategory;
import br.eng.rodrigogml.mysteryrealms.domain.economy.enums.HandItemSubtype;

/**
 * Ficha de escudo — RF-EI-08.
 *
 * Inclui todos os attributes comuns de Item de Mão (RF-EI-03) mais campos específicos de escudo.
 */
public class Shield extends HandItem {

    private final int baseBlockValue;
    /** Descrição de coverage especial (opcional). */
    private final String coverage;
    /** Penalidade de dexterity para não proficientes (opcional, 0 = sem penalidade). */
    private final int dexPenalty;
    private final int itemDefenseBonusFlat;
    private final double itemDefenseBonusPct;
    private final int itemBlockBonusFlat;
    private final double itemBlockBonusPct;

    public Shield(
            String name,
            double weightKg,
            MonetaryValue basePrice,
            int baseBlockValue,
            String coverage,
            int dexPenalty,
            int itemDefenseBonusFlat,
            double itemDefenseBonusPct,
            int itemBlockBonusFlat,
            double itemBlockBonusPct) {

        super(name, HandItemSubtype.ESCUDO, 1, HandItemCategory.DEFENSE, weightKg, basePrice);

        if (baseBlockValue < 0)
            throw new IllegalArgumentException("baseBlockValue deve ser >= 0, recebido: " + baseBlockValue);

        this.baseBlockValue = baseBlockValue;
        this.coverage = coverage;
        this.dexPenalty = dexPenalty;
        this.itemDefenseBonusFlat = itemDefenseBonusFlat;
        this.itemDefenseBonusPct = itemDefenseBonusPct;
        this.itemBlockBonusFlat = itemBlockBonusFlat;
        this.itemBlockBonusPct = itemBlockBonusPct;
    }

    public int getBaseBlockValue() { return baseBlockValue; }
    public String getCoverage() { return coverage; }
    public int getDexPenalty() { return dexPenalty; }

    @Override public int getItemDefenseBonusFlat() { return itemDefenseBonusFlat; }
    @Override public double getItemDefenseBonusPct() { return itemDefenseBonusPct; }
    @Override public int getItemBlockBonusFlat() { return itemBlockBonusFlat; }
    @Override public double getItemBlockBonusPct() { return itemBlockBonusPct; }
}
