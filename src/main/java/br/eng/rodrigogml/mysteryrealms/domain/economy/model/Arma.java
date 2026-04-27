package br.eng.rodrigogml.mysteryrealms.domain.economy.model;

import br.eng.rodrigogml.mysteryrealms.domain.combat.enums.TipoDano;
import br.eng.rodrigogml.mysteryrealms.domain.economy.enums.CategoriaItemMao;
import br.eng.rodrigogml.mysteryrealms.domain.economy.enums.SubtipoItemMao;

/**
 * Ficha de arma — RF-EI-07.
 *
 * Inclui todos os atributos comuns de Item de Mão (RF-EI-03) mais campos específicos de arma.
 */
public class Arma extends ItemMao {

    private final String tipoArmaId;
    /** Notação de dado, ex.: {@code 1d8}, {@code 2d6}. */
    private final String dadoDanoBase;
    private final TipoDano tipoDano;
    private final String alcance;
    /** Faixa mínima do dado e multiplicador de crítico (textual, ex.: "19-20/x2"). */
    private final String perfilCritico;
    private final int bonusItemPrecisaoPlano;
    private final double bonusItemPrecisaoPct;
    private final int bonusItemDanoPlano;
    private final double bonusItemDanoPct;

    public Arma(
            String nome,
            int maosNecessarias,
            double pesoKg,
            ValorMonetario precoBase,
            String tipoArmaId,
            String dadoDanoBase,
            TipoDano tipoDano,
            String alcance,
            String perfilCritico,
            int bonusItemPrecisaoPlano,
            double bonusItemPrecisaoPct,
            int bonusItemDanoPlano,
            double bonusItemDanoPct) {

        super(nome, SubtipoItemMao.ARMA, maosNecessarias, CategoriaItemMao.ATAQUE, pesoKg, precoBase);

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
        this.bonusItemPrecisaoPlano = bonusItemPrecisaoPlano;
        this.bonusItemPrecisaoPct = bonusItemPrecisaoPct;
        this.bonusItemDanoPlano = bonusItemDanoPlano;
        this.bonusItemDanoPct = bonusItemDanoPct;
    }

    public String getTipoArmaId() { return tipoArmaId; }
    public String getDadoDanoBase() { return dadoDanoBase; }
    public TipoDano getTipoDano() { return tipoDano; }
    public String getAlcance() { return alcance; }
    public String getPerfilCritico() { return perfilCritico; }

    @Override public int getBonusItemPrecisaoPlano() { return bonusItemPrecisaoPlano; }
    @Override public double getBonusItemPrecisaoPct() { return bonusItemPrecisaoPct; }
    @Override public int getBonusItemDanoPlano() { return bonusItemDanoPlano; }
    @Override public double getBonusItemDanoPct() { return bonusItemDanoPct; }
}
