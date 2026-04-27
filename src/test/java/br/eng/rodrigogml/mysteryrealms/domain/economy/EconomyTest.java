package br.eng.rodrigogml.mysteryrealms.domain.economy;

import br.eng.rodrigogml.mysteryrealms.domain.combat.enums.TipoDano;
import br.eng.rodrigogml.mysteryrealms.domain.economy.enums.CategoriaItemMao;
import br.eng.rodrigogml.mysteryrealms.domain.economy.enums.SubtipoItemMao;
import br.eng.rodrigogml.mysteryrealms.domain.economy.model.*;
import br.eng.rodrigogml.mysteryrealms.domain.economy.service.ServicoPrecos;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes de Economia e Inventário — RF-EI-01 a RF-EI-08.
 */
class EconomyTest {

    // ── RF-EI-01: ValorMonetario ───────────────────────────────────────────────

    @Test
    void monetaryValue_formatoCanonicoAmbas() {
        assertEquals("10$25", ValorMonetario.de(10, 25).formatar());
    }

    @Test
    void monetaryValue_formatoSoMP() {
        assertEquals("10$", ValorMonetario.deMp(10).formatar());
    }

    @Test
    void monetaryValue_formatoSoMS() {
        assertEquals("$25", ValorMonetario.deMs(25).formatar());
    }

    @Test
    void monetaryValue_pesoEmKg() {
        // ((2 × 6) + (100 × 5)) / 1000 = 0,512
        assertEquals(0.512, ValorMonetario.de(2, 100).pesoKg(), 1e-9);
    }

    @Test
    void monetaryValue_pesoZeroSemMoedas() {
        assertEquals(0.0, ValorMonetario.zero().pesoKg(), 1e-9);
    }

    @Test
    void monetaryValue_totalMs_conversao() {
        // 1 MP = 100 MS
        assertEquals(125L, ValorMonetario.de(1, 25).totalEmMs());
    }

    @Test
    void monetaryValue_lancaExcecaoParaValorNegativo() {
        assertThrows(IllegalArgumentException.class, () -> ValorMonetario.de(-1, 0));
        assertThrows(IllegalArgumentException.class, () -> ValorMonetario.de(0, -1));
    }

    @Test
    void monetaryValue_normalized_ajustaExcesso() {
        // 150 MS → 1 MP + 50 MS
        ValorMonetario norm = ValorMonetario.de(0, 150).normalizado();
        assertEquals(1, norm.mp());
        assertEquals(50, norm.ms());
    }

    // ── RF-EI-03: ItemMao ────────────────────────────────────────────────────

    @Test
    void handItem_validacaoNomeVazio() {
        assertThrows(IllegalArgumentException.class,
                () -> buildArma(""));
    }

    @Test
    void handItem_validacaoMaosNecessariasInvalido() {
        assertThrows(IllegalArgumentException.class,
                () -> new Arma("Espada", 3, 1.5, ValorMonetario.deMp(10),
                        "tipo_espada", "1d8", TipoDano.CORTE, "curto", "20/x2", 0, 0, 0, 0));
    }

    @Test
    void handItem_maosNecessariasValidos() {
        Arma arma1 = buildArma("Espada Curta");
        assertEquals(1, arma1.getMaosNecessarias());

        Arma arma2 = new Arma("Machado Grande", 2, 4.0, ValorMonetario.deMp(15),
                "tipo_machado", "2d6", TipoDano.CORTE, "curto", "20/x3", 0, 0, 0, 0);
        assertEquals(2, arma2.getMaosNecessarias());
    }

    @Test
    void handItem_pesoNegativoLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new Arma("Espada", 1, -0.1, ValorMonetario.deMp(10),
                        "tipo_espada", "1d8", TipoDano.CORTE, "curto", "20/x2", 0, 0, 0, 0));
    }

    // ── RF-EI-04: Canais de bônus ─────────────────────────────────────────────

    @Test
    void weapon_bonusPrecisaoEDano() {
        Arma arma = new Arma("Espada Mágica", 1, 1.5, ValorMonetario.deMp(100),
                "tipo_espada", "1d8", TipoDano.CORTE, "curto", "20/x2", 2, 0.1, 1, 0.0);
        assertEquals(2, arma.getBonusItemPrecisaoPlano());
        assertEquals(0.1, arma.getBonusItemPrecisaoPct(), 1e-9);
        assertEquals(1, arma.getBonusItemDanoPlano());
        assertEquals(0, arma.getBonusItemBloqueioPlano());
    }

    @Test
    void shield_bonusDefesaEBloqueio() {
        Escudo escudo = buildEscudo("Escudo de Ferro", 30, 5, 0, 10, 0.1);
        assertEquals(5, escudo.getBonusItemDefesaPlano());
        assertEquals(0.1, escudo.getBonusItemDefesaPct(), 1e-9);
        assertEquals(10, escudo.getBonusItemBloqueioPlano());
        assertEquals(30, escudo.getValorBaseBloqueio());
    }

    // ── RF-EI-07: Ficha de arma ───────────────────────────────────────────────

    @Test
    void weapon_camposObrigatorios() {
        Arma arma = buildArma("Lança");
        assertEquals("tipo_lanca", arma.getTipoArmaId());
        assertEquals("1d6", arma.getDadoDanoBase());
        assertEquals(TipoDano.PERFURACAO, arma.getTipoDano());
        assertEquals(SubtipoItemMao.ARMA, arma.getSubtipo());
        assertEquals(CategoriaItemMao.ATAQUE, arma.getCategoriaUso());
    }

    @Test
    void weapon_tipoDanoNuloLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new Arma("Espada", 1, 1.5, ValorMonetario.deMp(10),
                        "tipo_espada", "1d8", null, "curto", "20/x2", 0, 0, 0, 0));
    }

    // ── RF-EI-08: Ficha de escudo ─────────────────────────────────────────────

    @Test
    void shield_camposObrigatorios() {
        Escudo escudo = buildEscudo("Escudo de Madeira", 20, 0, 0, 0, 0.0);
        assertEquals(SubtipoItemMao.ESCUDO, escudo.getSubtipo());
        assertEquals(CategoriaItemMao.DEFESA, escudo.getCategoriaUso());
        assertEquals(1, escudo.getMaosNecessarias());
        assertEquals(20, escudo.getValorBaseBloqueio());
    }

    // ── RF-EI-02: fórmula de preço ────────────────────────────────────────────

    @Test
    void pricing_fatores1_retornaPrecoBase() {
        // RF-EI-02
        ValorMonetario base = ValorMonetario.de(10, 0); // 10 MP = 1000 MS
        ValorMonetario resultado = ServicoPrecos.aplicarPreco(base, 1.0, 1.0, 1.0, 1.0, 1.0);
        assertEquals(10L, resultado.mp());
        assertEquals(0L, resultado.ms());
    }

    @Test
    void pricing_fatorDobra_dobraPreco() {
        // RF-EI-02
        ValorMonetario base = ValorMonetario.de(10, 0); // 1000 MS
        ValorMonetario resultado = ServicoPrecos.aplicarPreco(base, 2.0, 1.0, 1.0, 1.0, 1.0);
        assertEquals(2000, resultado.totalEmMs());
    }

    @Test
    void pricing_fatorMetade_metadeDoPreco() {
        // RF-EI-02
        ValorMonetario base = ValorMonetario.de(10, 0); // 1000 MS
        ValorMonetario resultado = ServicoPrecos.aplicarPreco(base, 0.5, 1.0, 1.0, 1.0, 1.0);
        assertEquals(500, resultado.totalEmMs());
    }

    @Test
    void pricing_fatorForaDaFaixa_lancaExcecao() {
        // RF-EI-02
        ValorMonetario base = ValorMonetario.deMp(10);
        assertThrows(IllegalArgumentException.class,
                () -> ServicoPrecos.aplicarPreco(base, 0.4, 1.0, 1.0, 1.0, 1.0));
        assertThrows(IllegalArgumentException.class,
                () -> ServicoPrecos.aplicarPreco(base, 1.0, 2.1, 1.0, 1.0, 1.0));
    }

    @Test
    void pricing_resultadoNormalizadoCorreto() {
        // RF-EI-02 — verificar que MP e MS estão normalizados
        ValorMonetario base = ValorMonetario.de(0, 50); // 50 MS
        ValorMonetario resultado = ServicoPrecos.aplicarPreco(base, 2.0, 1.0, 1.0, 1.0, 1.0);
        // 50 × 2 = 100 MS = 1 MP 0 MS
        assertEquals(1L, resultado.mp());
        assertEquals(0L, resultado.ms());
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private Arma buildArma(String nome) {
        return new Arma(nome, 1, 1.0, ValorMonetario.deMp(5),
                "tipo_lanca", "1d6", TipoDano.PERFURACAO, "médio", "20/x2", 0, 0, 0, 0);
    }

    private Escudo buildEscudo(String nome, int bloqueio, int defesaFlat, int penDex,
                                int bloqueioFlat, double defesaPct) {
        return new Escudo(nome, 3.0, ValorMonetario.deMp(8),
                bloqueio, null, penDex, defesaFlat, defesaPct, bloqueioFlat, 0.0);
    }
}
