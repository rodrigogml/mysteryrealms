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
    private final int bonusItemDefesaFlatValue;
    private final double bonusItemDefesaPctValue;
    private final int bonusItemBloqueioFlatValue;
    private final double bonusItemBloqueioPctValue;

    public Escudo(
            String nome,
            double pesoKg,
            ValorMonetario precoBase,
            int valorBaseBloqueio,
            String cobertura,
            int penaldadeDestreza,
            int bonusItemDefesaFlat,
            double bonusItemDefesaPct,
            int bonusItemBloqueioFlat,
            double bonusItemBloqueioPct) {

        super(nome, SubtipoItemMao.ESCUDO, 1, CategoriaItemMao.DEFESA, pesoKg, precoBase);

        this.valorBaseBloqueio = valorBaseBloqueio;
        this.cobertura = cobertura;
        this.penaldadeDestreza = penaldadeDestreza;
        this.bonusItemDefesaFlatValue = bonusItemDefesaFlat;
        this.bonusItemDefesaPctValue = bonusItemDefesaPct;
        this.bonusItemBloqueioFlatValue = bonusItemBloqueioFlat;
        this.bonusItemBloqueioPctValue = bonusItemBloqueioPct;
    }

    public int getValorBaseBloqueio() { return valorBaseBloqueio; }
    public String getCobertura() { return cobertura; }
    public int getPenaldadeDestreza() { return penaldadeDestreza; }

    @Override public int getBonusItemDefesaFlat() { return bonusItemDefesaFlatValue; }
    @Override public double getBonusItemDefesaPct() { return bonusItemDefesaPctValue; }
    @Override public int getBonusItemBloqueioFlat() { return bonusItemBloqueioFlatValue; }
    @Override public double getBonusItemBloqueioPct() { return bonusItemBloqueioPctValue; }
}
