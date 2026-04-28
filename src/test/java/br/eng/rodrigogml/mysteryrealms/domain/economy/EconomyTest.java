package br.eng.rodrigogml.mysteryrealms.domain.economy;

import br.eng.rodrigogml.mysteryrealms.domain.combat.enums.DamageType;
import br.eng.rodrigogml.mysteryrealms.domain.economy.enums.HandItemCategory;
import br.eng.rodrigogml.mysteryrealms.domain.economy.enums.HandItemSubtype;
import br.eng.rodrigogml.mysteryrealms.domain.economy.model.*;
import br.eng.rodrigogml.mysteryrealms.domain.economy.service.PricingService;
import org.junit.jupiter.api.Test;

import java.util.List;

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
        assertEquals(125L, MonetaryValue.of(1, 25).totalInMinorUnits());
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
                () -> buildArma(""));
    }

    @Test
    void handItem_validacaoMaosNecessariasInvalido() {
        assertThrows(IllegalArgumentException.class,
                () -> new Weapon("Espada", 3, 1.5, MonetaryValue.ofMp(10),
                        "tipo_espada", "1d8", DamageType.SLASHING, "curto", "20/x2", 0, 0, 0, 0));
    }

    @Test
    void handItem_maosNecessariasValidos() {
        Weapon arma1 = buildArma("Espada Curta");
        assertEquals(1, arma1.getHandsRequired());

        Weapon arma2 = new Weapon("Machado Grande", 2, 4.0, MonetaryValue.ofMp(15),
                "tipo_machado", "2d6", DamageType.SLASHING, "curto", "20/x3", 0, 0, 0, 0);
        assertEquals(2, arma2.getHandsRequired());
    }

    @Test
    void handItem_pesoNegativoLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new Weapon("Espada", 1, -0.1, MonetaryValue.ofMp(10),
                        "tipo_espada", "1d8", DamageType.SLASHING, "curto", "20/x2", 0, 0, 0, 0));
    }

    // ── RF-EI-04: Canais de bônus ─────────────────────────────────────────────

    @Test
    void weapon_bonusPrecisaoEDano() {
        Weapon arma = new Weapon("Espada Mágica", 1, 1.5, MonetaryValue.ofMp(100),
                "tipo_espada", "1d8", DamageType.SLASHING, "curto", "20/x2", 2, 0.1, 1, 0.0);
        assertEquals(2, arma.getItemPrecisionBonusFlat());
        assertEquals(0.1, arma.getItemPrecisionBonusPct(), 1e-9);
        assertEquals(1, arma.getItemDamageBonusFlat());
        assertEquals(0, arma.getItemBlockBonusFlat());
    }

    @Test
    void shield_bonusDefesaEBloqueio() {
        Shield escudo = buildEscudo("Escudo de Ferro", 30, 5, 0, 10, 0.1);
        assertEquals(5, escudo.getItemDefenseBonusFlat());
        assertEquals(0.1, escudo.getItemDefenseBonusPct(), 1e-9);
        assertEquals(10, escudo.getItemBlockBonusFlat());
        assertEquals(30, escudo.getBaseBlockValue());
    }

    // ── RF-EI-07: Ficha de arma ───────────────────────────────────────────────

    @Test
    void weapon_camposObrigatorios() {
        Weapon arma = buildArma("Lança");
        assertEquals("tipo_lanca", arma.getWeaponTypeId());
        assertEquals("1d6", arma.getBaseDamageDie());
        assertEquals(DamageType.PIERCING, arma.getDamageType());
        assertEquals(HandItemSubtype.ARMA, arma.getSubtype());
        assertEquals(HandItemCategory.ATTACK, arma.getUseCategory());
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
        Shield escudo = buildEscudo("Escudo de Madeira", 20, 0, 0, 0, 0.0);
        assertEquals(HandItemSubtype.ESCUDO, escudo.getSubtype());
        assertEquals(HandItemCategory.DEFENSE, escudo.getUseCategory());
        assertEquals(1, escudo.getHandsRequired());
        assertEquals(20, escudo.getBaseBlockValue());
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
        assertEquals(2000, resultado.totalInMinorUnits());
    }

    @Test
    void pricing_fatorMetade_metadeDoPreco() {
        // RF-EI-02
        MonetaryValue base = MonetaryValue.of(10, 0); // 1000 MS
        MonetaryValue resultado = PricingService.applyPrice(base, 0.5, 1.0, 1.0, 1.0, 1.0);
        assertEquals(500, resultado.totalInMinorUnits());
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

    private Weapon buildArma(String name) {
        return new Weapon(name, 1, 1.0, MonetaryValue.ofMp(5),
                "tipo_lanca", "1d6", DamageType.PIERCING, "médio", "20/x2", 0, 0, 0, 0);
    }

    private Shield buildEscudo(String name, int bloqueio, int defesaFlat, int penDex,
                                int bloqueioFlat, double defesaPct) {
        return new Shield(name, 3.0, MonetaryValue.ofMp(8),
                bloqueio, null, penDex, defesaFlat, defesaPct, bloqueioFlat, 0.0);
    }

    // ── RF-EI-05: regras de equipamento de mãos ──────────────────────────────

    @Test
    void equippedHands_inicialVazio() {
        // RF-EI-05
        EquippedHands hands = new EquippedHands();
        assertTrue(hands.isEmpty());
        assertNull(hands.getSlotOne());
        assertNull(hands.getSlotTwo());
    }

    @Test
    void equippedHands_equiparItem1Mao_ocupa1Slot() {
        // RF-EI-05
        EquippedHands hands = new EquippedHands();
        Weapon arma = buildArma("Espada");
        hands.equip(arma);
        assertTrue(hands.isEquipped(arma));
        assertTrue(hands.hasFreeSlot());
    }

    @Test
    void equippedHands_equipar2Itens1Mao_ocupaAmbosSlots() {
        // RF-EI-05
        EquippedHands hands = new EquippedHands();
        Weapon arma1 = buildArma("Espada");
        Weapon arma2 = buildArma("Adaga");
        hands.equip(arma1);
        hands.equip(arma2);
        assertTrue(hands.isEquipped(arma1));
        assertTrue(hands.isEquipped(arma2));
        assertFalse(hands.hasFreeSlot());
    }

    @Test
    void equippedHands_equiparItem2Maos_ocupaAmbosSlots() {
        // RF-EI-05 — item de 2 mãos ocupa ambos os slots
        EquippedHands hands = new EquippedHands();
        Weapon arma2m = new Weapon("Machado Grande", 2, 4.0, MonetaryValue.ofMp(20),
                "tipo_machado", "2d6", DamageType.SLASHING, "curto", "20/x3", 0, 0, 0, 0);
        hands.equip(arma2m);
        assertTrue(hands.isEquipped(arma2m));
        assertFalse(hands.hasFreeSlot());
        assertSame(arma2m, hands.getSlotOne());
        assertSame(arma2m, hands.getSlotTwo());
    }

    @Test
    void equippedHands_equiparItem2MaosComSlotOcupado_lancaExcecao() {
        // RF-EI-05
        EquippedHands hands = new EquippedHands();
        Weapon arma1 = buildArma("Adaga");
        Weapon arma2m = new Weapon("Machado Grande", 2, 4.0, MonetaryValue.ofMp(20),
                "tipo_machado", "2d6", DamageType.SLASHING, "curto", "20/x3", 0, 0, 0, 0);
        hands.equip(arma1);
        assertThrows(IllegalStateException.class, () -> hands.equip(arma2m));
    }

    @Test
    void equippedHands_desequipar_liberaSlot() {
        // RF-EI-05
        EquippedHands hands = new EquippedHands();
        Weapon arma = buildArma("Espada");
        hands.equip(arma);
        hands.unequip(arma);
        assertFalse(hands.isEquipped(arma));
        assertTrue(hands.isEmpty());
    }

    @Test
    void equippedHands_desequipar2Maos_liberaAmbosSlots() {
        // RF-EI-05
        EquippedHands hands = new EquippedHands();
        Weapon arma2m = new Weapon("Machado Grande", 2, 4.0, MonetaryValue.ofMp(20),
                "tipo_machado", "2d6", DamageType.SLASHING, "curto", "20/x3", 0, 0, 0, 0);
        hands.equip(arma2m);
        hands.unequip(arma2m);
        assertTrue(hands.isEmpty());
    }

    @Test
    void equippedHands_desequiparItemNaoEquipado_lancaExcecao() {
        // RF-EI-05
        EquippedHands hands = new EquippedHands();
        Weapon arma = buildArma("Espada");
        assertThrows(IllegalStateException.class, () -> hands.unequip(arma));
    }

    @Test
    void equippedHands_itemNaoEquipadoNaoRetornaBonusImplicito() {
        // RF-EI-05 — bônus só valem enquanto equipados
        Weapon armaMagica = new Weapon("Espada Mágica", 1, 1.5, MonetaryValue.ofMp(100),
                "tipo_espada", "1d8", DamageType.SLASHING, "curto", "20/x2", 5, 0.2, 3, 0.0);
        EquippedHands hands = new EquippedHands();
        assertFalse(hands.isEquipped(armaMagica));
    }

    // ── RF-EI-06: tipos de armas ──────────────────────────────────────────────

    @Test
    void weaponType_criacaoValida_camposObrigatorios() {
        // RF-EI-06
        CriticalProfile critico = new CriticalProfile(20, 2);
        WeaponType tipo = new WeaponType("espada_longa", "pesada", "forca", 1, "curto", critico);
        assertEquals("espada_longa", tipo.getTypeId());
        assertEquals("pesada", tipo.getFunction());
        assertEquals("forca", tipo.getPrimaryAttribute());
        assertEquals(1, tipo.getCommonHands());
        assertEquals("curto", tipo.getDefaultRange());
        assertSame(critico, tipo.getCriticalProfile());
        assertTrue(tipo.getCompatibilities().isEmpty());
    }

    @Test
    void weaponType_comCompatibilidades() {
        // RF-EI-06
        CriticalProfile critico = new CriticalProfile(19, 2);
        WeaponType tipo = new WeaponType("arco_longo", "distância", "destreza", 2, "longo",
                critico, List.of("ranger", "elfo"));
        assertEquals(2, tipo.getCompatibilities().size());
        assertTrue(tipo.getCompatibilities().contains("ranger"));
    }

    @Test
    void weaponType_maosComuns2_valido() {
        // RF-EI-06
        CriticalProfile critico = new CriticalProfile(20, 3);
        WeaponType tipo = new WeaponType("alabarda", "pesada", "forca", 2, "médio", critico);
        assertEquals(2, tipo.getCommonHands());
    }

    @Test
    void weaponType_typeIdVazio_lancaExcecao() {
        // RF-EI-06
        assertThrows(IllegalArgumentException.class,
                () -> new WeaponType("", "pesada", "forca", 1, "curto",
                        new CriticalProfile(20, 2)));
    }

    @Test
    void weaponType_maosInvalidas_lancaExcecao() {
        // RF-EI-06
        assertThrows(IllegalArgumentException.class,
                () -> new WeaponType("tipo", "leve", "destreza", 3, "curto",
                        new CriticalProfile(20, 2)));
    }

    @Test
    void weaponType_criticalProfileNulo_lancaExcecao() {
        // RF-EI-06
        assertThrows(IllegalArgumentException.class,
                () -> new WeaponType("tipo", "leve", "destreza", 1, "curto", null));
    }

    @Test
    void criticalProfile_minDieValue20_multiplicador2_valido() {
        // RF-EI-06
        CriticalProfile cp = new CriticalProfile(20, 2);
        assertEquals(20, cp.minDieValue());
        assertEquals(2, cp.multiplier());
        assertTrue(cp.isCritical(20));
        assertFalse(cp.isCritical(19));
    }

    @Test
    void criticalProfile_faixaAmpliaMin19() {
        // RF-EI-06
        CriticalProfile cp = new CriticalProfile(19, 2);
        assertTrue(cp.isCritical(19));
        assertTrue(cp.isCritical(20));
        assertFalse(cp.isCritical(18));
    }

    @Test
    void criticalProfile_minDieValueForaDaFaixa_lancaExcecao() {
        // RF-EI-06
        assertThrows(IllegalArgumentException.class, () -> new CriticalProfile(0, 2));
        assertThrows(IllegalArgumentException.class, () -> new CriticalProfile(21, 2));
    }

    @Test
    void criticalProfile_multiplicadorMenorQue2_lancaExcecao() {
        // RF-EI-06
        assertThrows(IllegalArgumentException.class, () -> new CriticalProfile(20, 1));
    }
}
