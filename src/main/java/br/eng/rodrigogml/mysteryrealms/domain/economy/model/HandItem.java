package br.eng.rodrigogml.mysteryrealms.domain.economy.model;

import br.eng.rodrigogml.mysteryrealms.domain.economy.enums.HandItemCategory;
import br.eng.rodrigogml.mysteryrealms.domain.economy.enums.HandItemSubtype;

/**
 * Atributos comuns de um Item de Mão — RF-EI-03.
 *
 * Subclasses devem declarar os canais de bônus canônicos (RF-EI-04):
 * {@code bonusItemPrecisao}, {@code bonusItemDano}, {@code bonusItemDefesa}, {@code bonusItemBloqueio}.
 */
public abstract class HandItem {

    private final String name;
    private final HandItemSubtype subtype;
    /** 1 ou 2 — RF-EI-03. */
    private final int handsRequired;
    private final HandItemCategory useCategory;
    /** >= 0 — RF-EI-03. */
    private final double weightKg;
    private final MonetaryValue basePrice;

    protected HandItem(
            String name,
            HandItemSubtype subtype,
            int handsRequired,
            HandItemCategory useCategory,
            double weightKg,
            MonetaryValue basePrice) {

        if (name == null || name.isBlank())
            throw new IllegalArgumentException("nome do item não pode ser vazio");
        if (subtype == null)
            throw new IllegalArgumentException("subtipo não pode ser nulo");
        if (handsRequired != 1 && handsRequired != 2)
            throw new IllegalArgumentException("maosNecessarias deve ser 1 ou 2, recebido: " + handsRequired);
        if (useCategory == null)
            throw new IllegalArgumentException("categoriaUso não pode ser nulo");
        if (weightKg < 0)
            throw new IllegalArgumentException("pesoKg deve ser >= 0, recebido: " + weightKg);
        if (basePrice == null)
            throw new IllegalArgumentException("precoBase não pode ser nulo");

        this.name = name;
        this.subtype = subtype;
        this.handsRequired = handsRequired;
        this.useCategory = useCategory;
        this.weightKg = weightKg;
        this.basePrice = basePrice;
    }

    public String getName() { return name; }
    public HandItemSubtype getSubtype() { return subtype; }
    public int getHandsRequired() { return handsRequired; }
    public HandItemCategory getUseCategory() { return useCategory; }
    public double getWeightKg() { return weightKg; }
    public MonetaryValue getBasePrice() { return basePrice; }

    // ── Canais de bônus canônicos — RF-EI-04 ─────────────────────────────────

    /** Bônus plano de precisão alimentado em precisao_final — RF-EI-04. */
    public int getItemPrecisionBonusFlat() { return 0; }

    /** Bônus percentual de precisão — RF-EI-04. */
    public double getItemPrecisionBonusPct() { return 0.0; }

    /** Bônus plano de dano alimentado em dano_final — RF-EI-04. */
    public int getItemDamageBonusFlat() { return 0; }

    /** Bônus percentual de dano — RF-EI-04. */
    public double getItemDamageBonusPct() { return 0.0; }

    /** Bônus plano de defesa alimentado em defesa_final — RF-EI-04. */
    public int getItemDefenseBonusFlat() { return 0; }

    /** Bônus percentual de defesa — RF-EI-04. */
    public double getItemDefenseBonusPct() { return 0.0; }

    /** Bônus plano de bloqueio alimentado em bloqueio_final — RF-EI-04. */
    public int getItemBlockBonusFlat() { return 0; }

    /** Bônus percentual de bloqueio — RF-EI-04. */
    public double getItemBlockBonusPct() { return 0.0; }
}
