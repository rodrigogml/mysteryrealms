package br.eng.rodrigogml.mysteryrealms.domain.economy.model;

import br.eng.rodrigogml.mysteryrealms.domain.economy.enums.CategoriaItemMao;
import br.eng.rodrigogml.mysteryrealms.domain.economy.enums.SubtipoItemMao;

/**
 * Ficha de escudo — RF-EI-08.
 *
 * Inclui todos os atributos comuns de Item de Mão (RF-EI-03) mais campos específicos de escudo.
 */
public class Escudo extends ItemMao {

    private final int valorBaseBloqueio;
    /** Descrição de cobertura especial (opcional). */
    private final String cobertura;
    /** Penalidade de destreza para não proficientes (opcional, 0 = sem penalidade). */
    private final int penaldadeDestreza;
    private final int bonusItemDefesaPlano;
    private final double bonusItemDefesaPct;
    private final int bonusItemBloqueioPlano;
    private final double bonusItemBloqueioPct;

    public Escudo(
            String nome,
            double pesoKg,
            ValorMonetario precoBase,
            int valorBaseBloqueio,
            String cobertura,
            int penaldadeDestreza,
            int bonusItemDefesaPlano,
            double bonusItemDefesaPct,
            int bonusItemBloqueioPlano,
            double bonusItemBloqueioPct) {

        super(nome, SubtipoItemMao.ESCUDO, 1, CategoriaItemMao.DEFESA, pesoKg, precoBase);

        this.valorBaseBloqueio = valorBaseBloqueio;
        this.cobertura = cobertura;
        this.penaldadeDestreza = penaldadeDestreza;
        this.bonusItemDefesaPlano = bonusItemDefesaPlano;
        this.bonusItemDefesaPct = bonusItemDefesaPct;
        this.bonusItemBloqueioPlano = bonusItemBloqueioPlano;
        this.bonusItemBloqueioPct = bonusItemBloqueioPct;
    }

    public int getValorBaseBloqueio() { return valorBaseBloqueio; }
    public String getCobertura() { return cobertura; }
    public int getPenaldadeDestreza() { return penaldadeDestreza; }

    @Override public int getBonusItemDefesaPlano() { return bonusItemDefesaPlano; }
    @Override public double getBonusItemDefesaPct() { return bonusItemDefesaPct; }
    @Override public int getBonusItemBloqueioPlano() { return bonusItemBloqueioPlano; }
    @Override public double getBonusItemBloqueioPct() { return bonusItemBloqueioPct; }
}
