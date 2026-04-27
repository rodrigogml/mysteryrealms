package br.eng.rodrigogml.mysteryrealms.domain.economy.model;

import br.eng.rodrigogml.mysteryrealms.domain.economy.enums.CategoriaItemMao;
import br.eng.rodrigogml.mysteryrealms.domain.economy.enums.SubtipoItemMao;

/**
 * Atributos comuns de um Item de Mão — RF-EI-03.
 *
 * Subclasses devem declarar os canais de bônus canônicos (RF-EI-04):
 * {@code bonusItemPrecisao}, {@code bonusItemDano}, {@code bonusItemDefesa}, {@code bonusItemBloqueio}.
 */
public abstract class ItemMao {

    private final String nome;
    private final SubtipoItemMao subtipo;
    /** 1 ou 2 — RF-EI-03. */
    private final int maosNecessarias;
    private final CategoriaItemMao categoriaUso;
    /** >= 0 — RF-EI-03. */
    private final double pesoKg;
    private final ValorMonetario precoBase;

    protected ItemMao(
            String nome,
            SubtipoItemMao subtipo,
            int maosNecessarias,
            CategoriaItemMao categoriaUso,
            double pesoKg,
            ValorMonetario precoBase) {

        if (nome == null || nome.isBlank())
            throw new IllegalArgumentException("nome do item não pode ser vazio");
        if (subtipo == null)
            throw new IllegalArgumentException("subtipo não pode ser nulo");
        if (maosNecessarias != 1 && maosNecessarias != 2)
            throw new IllegalArgumentException("maosNecessarias deve ser 1 ou 2, recebido: " + maosNecessarias);
        if (categoriaUso == null)
            throw new IllegalArgumentException("categoriaUso não pode ser nulo");
        if (pesoKg < 0)
            throw new IllegalArgumentException("pesoKg deve ser >= 0, recebido: " + pesoKg);
        if (precoBase == null)
            throw new IllegalArgumentException("precoBase não pode ser nulo");

        this.nome = nome;
        this.subtipo = subtipo;
        this.maosNecessarias = maosNecessarias;
        this.categoriaUso = categoriaUso;
        this.pesoKg = pesoKg;
        this.precoBase = precoBase;
    }

    public String getNome() { return nome; }
    public SubtipoItemMao getSubtipo() { return subtipo; }
    public int getMaosNecessarias() { return maosNecessarias; }
    public CategoriaItemMao getCategoriaUso() { return categoriaUso; }
    public double getPesoKg() { return pesoKg; }
    public ValorMonetario getPrecoBase() { return precoBase; }

    // ── Canais de bônus canônicos — RF-EI-04 ─────────────────────────────────

    /** Bônus plano de precisão alimentado em precisao_final — RF-EI-04. */
    public int getBonusItemPrecisaoPlano() { return 0; }

    /** Bônus percentual de precisão — RF-EI-04. */
    public double getBonusItemPrecisaoPct() { return 0.0; }

    /** Bônus plano de dano alimentado em dano_final — RF-EI-04. */
    public int getBonusItemDanoPlano() { return 0; }

    /** Bônus percentual de dano — RF-EI-04. */
    public double getBonusItemDanoPct() { return 0.0; }

    /** Bônus plano de defesa alimentado em defesa_final — RF-EI-04. */
    public int getBonusItemDefesaPlano() { return 0; }

    /** Bônus percentual de defesa — RF-EI-04. */
    public double getBonusItemDefesaPct() { return 0.0; }

    /** Bônus plano de bloqueio alimentado em bloqueio_final — RF-EI-04. */
    public int getBonusItemBloqueioPlano() { return 0; }

    /** Bônus percentual de bloqueio — RF-EI-04. */
    public double getBonusItemBloqueioPct() { return 0.0; }
}
