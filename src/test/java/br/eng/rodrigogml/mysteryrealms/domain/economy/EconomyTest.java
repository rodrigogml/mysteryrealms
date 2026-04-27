package br.eng.rodrigogml.mysteryrealms.domain.economy;

import br.eng.rodrigogml.mysteryrealms.domain.combat.enums.DamageType;
import br.eng.rodrigogml.mysteryrealms.domain.economy.enums.HandItemCategory;
import br.eng.rodrigogml.mysteryrealms.domain.economy.enums.HandItemSubtype;
import br.eng.rodrigogml.mysteryrealms.domain.economy.model.*;
import br.eng.rodrigogml.mysteryrealms.domain.economy.service.PricingService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes de Economia e Inventário — RF-EI-01 a RF-EI-08.
 */
class EconomyTest {

    // ── RF-EI-01: MonetaryValue ───────────────────────────────────────────────

    @Test
    void monetaryValue_formatoCanonicoAmbas() {
        assertEquals("10$25", MonetaryValue.of(10, 25).format());
    }

    @Test
    void monetaryValue_formatoSoMP() {
        assertEquals("10$", MonetaryValue.ofMp(10).format());
    }

    @Test
    void monetaryValue_formatoSoMS() {
        assertEquals("$25", MonetaryValue.ofMs(25).format());
    }

    @Test
    void monetaryValue_pesoEmKg() {
        // ((2 × 6) + (100 × 5)) / 1000 = 0,512
        assertEquals(0.512, MonetaryValue.of(2, 100).weightKg(), 1e-9);
    }

    @Test
    void monetaryValue_pesoZeroSemMoedas() {
        assertEquals(0.0, MonetaryValue.zero().weightKg(), 1e-9);
    }

    @Test
    void monetaryValue_totalMs_conversao() {
        // 1 MP = 100 MS
        assertEquals(125L, MonetaryValue.of(1, 25).toTotalMs());
    }

    @Test
    void monetaryValue_lancaExcecaoParaValorNegativo() {
        assertThrows(IllegalArgumentException.class, () -> MonetaryValue.of(-1, 0));
        assertThrows(IllegalArgumentException.class, () -> MonetaryValue.of(0, -1));
    }

    @Test
    void monetaryValue_normalized_ajustaExcesso() {
        // 150 MS → 1 MP + 50 MS
        MonetaryValue norm = MonetaryValue.of(0, 150).normalized();
        assertEquals(1, norm.mp());
        assertEquals(50, norm.ms());
    }

    // ── RF-EI-03: HandItem ────────────────────────────────────────────────────

    @Test
    void handItem_validacaoNomeVazio() {
        assertThrows(IllegalArgumentException.class,
                () -> buildWeapon(""));
    }

    @Test
    void handItem_validacaoMaosNecessariasInvalido() {
        assertThrows(IllegalArgumentException.class,
                () -> new Weapon("Espada", 3, 1.5, MonetaryValue.ofMp(10),
                        "tipo_espada", "1d8", DamageType.CORTE, "curto", "20/x2", 0, 0, 0, 0));
    }

    @Test
    void handItem_maosNecessariasValidos() {
        Weapon arma1 = buildWeapon("Espada Curta");
        assertEquals(1, arma1.getMaosNecessarias());

        Weapon arma2 = new Weapon("Machado Grande", 2, 4.0, MonetaryValue.ofMp(15),
                "tipo_machado", "2d6", DamageType.CORTE, "curto", "20/x3", 0, 0, 0, 0);
        assertEquals(2, arma2.getMaosNecessarias());
    }

    @Test
    void handItem_pesoNegativoLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new Weapon("Espada", 1, -0.1, MonetaryValue.ofMp(10),
                        "tipo_espada", "1d8", DamageType.CORTE, "curto", "20/x2", 0, 0, 0, 0));
    }

    // ── RF-EI-04: Canais de bônus ─────────────────────────────────────────────

    @Test
    void weapon_bonusPrecisaoEDano() {
        Weapon arma = new Weapon("Espada Mágica", 1, 1.5, MonetaryValue.ofMp(100),
                "tipo_espada", "1d8", DamageType.CORTE, "curto", "20/x2", 2, 0.1, 1, 0.0);
        assertEquals(2, arma.getBonusItemPrecisaoFlat());
        assertEquals(0.1, arma.getBonusItemPrecisaoPct(), 1e-9);
        assertEquals(1, arma.getBonusItemDanoFlat());
        assertEquals(0, arma.getBonusItemBloqueioFlat());
    }

    @Test
    void shield_bonusDefesaEBloqueio() {
        Shield escudo = buildShield("Escudo de Ferro", 30, 5, 0, 10, 0.1);
        assertEquals(5, escudo.getBonusItemDefesaFlat());
        assertEquals(0.1, escudo.getBonusItemDefesaPct(), 1e-9);
        assertEquals(10, escudo.getBonusItemBloqueioFlat());
        assertEquals(30, escudo.getValorBaseBloqueio());
    }

    // ── RF-EI-07: Ficha de arma ───────────────────────────────────────────────

    @Test
    void weapon_camposObrigatorios() {
        Weapon arma = buildWeapon("Lança");
        assertEquals("tipo_lanca", arma.getTipoArmaId());
        assertEquals("1d6", arma.getDadoDanoBase());
        assertEquals(DamageType.PERFURACAO, arma.getTipoDano());
        assertEquals(HandItemSubtype.ARMA, arma.getSubtipo());
        assertEquals(HandItemCategory.ATAQUE, arma.getCategoriaUso());
    }

    @Test
    void weapon_tipoDanoNuloLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new Weapon("Espada", 1, 1.5, MonetaryValue.ofMp(10),
                        "tipo_espada", "1d8", null, "curto", "20/x2", 0, 0, 0, 0));
    }

    // ── RF-EI-08: Ficha de escudo ─────────────────────────────────────────────

    @Test
    void shield_camposObrigatorios() {
        Shield escudo = buildShield("Escudo de Madeira", 20, 0, 0, 0, 0.0);
        assertEquals(HandItemSubtype.ESCUDO, escudo.getSubtipo());
        assertEquals(HandItemCategory.DEFESA, escudo.getCategoriaUso());
        assertEquals(1, escudo.getMaosNecessarias());
        assertEquals(20, escudo.getValorBaseBloqueio());
    }

    // ── RF-EI-02: fórmula de preço ────────────────────────────────────────────

    @Test
    void pricing_fatores1_retornaPrecoBase() {
        // RF-EI-02
        MonetaryValue base = MonetaryValue.of(10, 0); // 10 MP = 1000 MS
        MonetaryValue resultado = PricingService.applyPrice(base, 1.0, 1.0, 1.0, 1.0, 1.0);
        assertEquals(10L, resultado.mp());
        assertEquals(0L, resultado.ms());
    }

    @Test
    void pricing_fatorDobra_dobraPreco() {
        // RF-EI-02
        MonetaryValue base = MonetaryValue.of(10, 0); // 1000 MS
        MonetaryValue resultado = PricingService.applyPrice(base, 2.0, 1.0, 1.0, 1.0, 1.0);
        assertEquals(2000, resultado.toTotalMs());
    }

    @Test
    void pricing_fatorMetade_metadeDoPreco() {
        // RF-EI-02
        MonetaryValue base = MonetaryValue.of(10, 0); // 1000 MS
        MonetaryValue resultado = PricingService.applyPrice(base, 0.5, 1.0, 1.0, 1.0, 1.0);
        assertEquals(500, resultado.toTotalMs());
    }

    @Test
    void pricing_fatorForaDaFaixa_lancaExcecao() {
        // RF-EI-02
        MonetaryValue base = MonetaryValue.ofMp(10);
        assertThrows(IllegalArgumentException.class,
                () -> PricingService.applyPrice(base, 0.4, 1.0, 1.0, 1.0, 1.0));
        assertThrows(IllegalArgumentException.class,
                () -> PricingService.applyPrice(base, 1.0, 2.1, 1.0, 1.0, 1.0));
    }

    @Test
    void pricing_resultadoNormalizadoCorreto() {
        // RF-EI-02 — verificar que MP e MS estão normalizados
        MonetaryValue base = MonetaryValue.of(0, 50); // 50 MS
        MonetaryValue resultado = PricingService.applyPrice(base, 2.0, 1.0, 1.0, 1.0, 1.0);
        // 50 × 2 = 100 MS = 1 MP 0 MS
        assertEquals(1L, resultado.mp());
        assertEquals(0L, resultado.ms());
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private Weapon buildWeapon(String nome) {
        return new Weapon(nome, 1, 1.0, MonetaryValue.ofMp(5),
                "tipo_lanca", "1d6", DamageType.PERFURACAO, "médio", "20/x2", 0, 0, 0, 0);
    }

    private Shield buildShield(String nome, int bloqueio, int defesaFlat, int penDex,
                                int bloqueioFlat, double defesaPct) {
        return new Shield(nome, 3.0, MonetaryValue.ofMp(8),
                bloqueio, null, penDex, defesaFlat, defesaPct, bloqueioFlat, 0.0);
    }
}
