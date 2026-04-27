package br.eng.rodrigogml.mysteryrealms.domain.economy.model;

import br.eng.rodrigogml.mysteryrealms.domain.combat.enums.DamageType;
import br.eng.rodrigogml.mysteryrealms.domain.economy.enums.HandItemCategory;
import br.eng.rodrigogml.mysteryrealms.domain.economy.enums.HandItemSubtype;

/**
 * Ficha de arma — RF-EI-07.
 *
 * Inclui todos os atributos comuns de Item de Mão (RF-EI-03) mais campos específicos de arma.
 */
public class Weapon extends HandItem {

    private final String tipoArmaId;
    /** Notação de dado, ex.: {@code 1d8}, {@code 2d6}. */
    private final String dadoDanoBase;
    private final DamageType tipoDano;
    private final String alcance;
    /** Faixa mínima do dado e multiplicador de crítico (textual, ex.: "19-20/x2"). */
    private final String perfilCritico;
    private final int bonusItemPrecisaoFlatValue;
    private final double bonusItemPrecisaoPctValue;
    private final int bonusItemDanoFlatValue;
    private final double bonusItemDanoPctValue;

    public Weapon(
            String nome,
            int maosNecessarias,
            double pesoKg,
            ValorMonetario precoBase,
            String tipoArmaId,
            String dadoDanoBase,
            DamageType tipoDano,
            String alcance,
            String perfilCritico,
            int bonusItemPrecisaoFlat,
            double bonusItemPrecisaoPct,
            int bonusItemDanoFlat,
            double bonusItemDanoPct) {

        super(nome, HandItemSubtype.ARMA, maosNecessarias, HandItemCategory.ATAQUE, pesoKg, precoBase);

        if (tipoArmaId == null || tipoArmaId.isBlank())
            throw new IllegalArgumentException("tipoArmaId não pode ser vazio");
        if (dadoDanoBase == null || dadoDanoBase.isBlank())
            throw new IllegalArgumentException("dadoDanoBase não pode ser vazio");
        if (tipoDano == null)
            throw new IllegalArgumentException("tipoDano não pode ser nulo");
        if (alcance == null || alcance.isBlank())
            throw new IllegalArgumentException("alcance não pode ser vazio");
        if (perfilCritico == null || perfilCritico.isBlank())
            throw new IllegalArgumentException("perfilCritico não pode ser vazio");

        this.tipoArmaId = tipoArmaId;
        this.dadoDanoBase = dadoDanoBase;
        this.tipoDano = tipoDano;
        this.alcance = alcance;
        this.perfilCritico = perfilCritico;
        this.bonusItemPrecisaoFlatValue = bonusItemPrecisaoFlat;
        this.bonusItemPrecisaoPctValue = bonusItemPrecisaoPct;
        this.bonusItemDanoFlatValue = bonusItemDanoFlat;
        this.bonusItemDanoPctValue = bonusItemDanoPct;
    }

    public String getTipoArmaId() { return tipoArmaId; }
    public String getDadoDanoBase() { return dadoDanoBase; }
    public DamageType getTipoDano() { return tipoDano; }
    public String getAlcance() { return alcance; }
    public String getPerfilCritico() { return perfilCritico; }

    @Override public int getBonusItemPrecisaoFlat() { return bonusItemPrecisaoFlatValue; }
    @Override public double getBonusItemPrecisaoPct() { return bonusItemPrecisaoPctValue; }
    @Override public int getBonusItemDanoFlat() { return bonusItemDanoFlatValue; }
    @Override public double getBonusItemDanoPct() { return bonusItemDanoPctValue; }
}
