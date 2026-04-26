package br.eng.rodrigogml.mysteryrealms.domain.character.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes dos atributos derivados do personagem — RF-FP-06.
 */
class CharacterAttributeServiceTest {

    // ── RF-FP-06.1: PV máximo ─────────────────────────────────────────────────

    @Test
    void maxHp_constituicaoMultiplicadaPorDez() {
        assertEquals(50.0, CharacterAttributeService.maxHp(5), 1e-9);
        assertEquals(30.0, CharacterAttributeService.maxHp(3), 1e-9);
        assertEquals(100.0, CharacterAttributeService.maxHp(10), 1e-9);
    }

    // ── RF-FP-06.2: fadiga máxima ─────────────────────────────────────────────

    @Test
    void maxFatigue_somaDosAtributosPorCem() {
        assertEquals(600.0, CharacterAttributeService.maxFatigue(3, 3), 1e-9);
        assertEquals(1000.0, CharacterAttributeService.maxFatigue(5, 5), 1e-9);
        assertEquals(700.0, CharacterAttributeService.maxFatigue(4, 3), 1e-9);
    }

    // ── RF-FP-06.3: peso do personagem ───────────────────────────────────────

    @Test
    void characterWeight_constituicao3semAlteracao() {
        // peso_base × (1 + (3-3) × 0,05) = peso_base × 1.0
        assertEquals(75.0, CharacterAttributeService.characterWeight(75, 3), 1e-9);
    }

    @Test
    void characterWeight_constituicao5aumentaPeso() {
        // peso_base × (1 + (5-3) × 0,05) = peso_base × 1.1
        assertEquals(75.0 * 1.1, CharacterAttributeService.characterWeight(75, 5), 1e-9);
    }

    @Test
    void characterWeight_constituicao1reduzPeso() {
        // peso_base × (1 + (1-3) × 0,05) = peso_base × 0.9
        assertEquals(75.0 * 0.9, CharacterAttributeService.characterWeight(75, 1), 1e-9);
    }

    // ── RF-FP-06.4: carga máxima ──────────────────────────────────────────────

    @Test
    void maxCarryCapacity_forcaMultiplicadaPorDez() {
        assertEquals(30.0, CharacterAttributeService.maxCarryCapacity(3), 1e-9);
        assertEquals(50.0, CharacterAttributeService.maxCarryCapacity(5), 1e-9);
    }

    @Test
    void criticalCarryCapacity_umMeioVezesCapacidade() {
        assertEquals(45.0, CharacterAttributeService.criticalCarryCapacity(30), 1e-9);
    }

    @Test
    void coinWeight_formulaCorreta() {
        // ((2 × 6) + (100 × 5)) / 1000 = (12 + 500) / 1000 = 0,512
        assertEquals(0.512, CharacterAttributeService.coinWeight(2, 100), 1e-9);
    }

    @Test
    void coinWeight_semMoedas() {
        assertEquals(0.0, CharacterAttributeService.coinWeight(0, 0), 1e-9);
    }

    @Test
    void currentLoad_somaComponentes() {
        assertEquals(12.5, CharacterAttributeService.currentLoad(5.0, 7.0, 0.5), 1e-9);
    }

    // ── RF-FP-06.5: precisão ─────────────────────────────────────────────────

    @Test
    void precisionBonus_somaCorreta() {
        assertEquals(8, CharacterAttributeService.precisionBonus(4, 2, 2));
        assertEquals(3, CharacterAttributeService.precisionBonus(3, 0, 0));
    }

    // ── RF-FP-06.6: dano final ────────────────────────────────────────────────

    @Test
    void calculateFinalDamage_semModificadores() {
        // base = 5+2+0+1+0 = 8; × (1+0-0) = 8; floor = 8; + 0 - 0 = 8; max(0,8) = 8
        assertEquals(8, CharacterAttributeService.calculateFinalDamage(5, 2, 0, 1, 0, 0, 0, 0, 0));
    }

    @Test
    void calculateFinalDamage_comModificadorPercentualPositivo() {
        // base = 10; × (1 + 0.5) = 15.0; floor = 15; max(0,15) = 15
        assertEquals(15, CharacterAttributeService.calculateFinalDamage(10, 0, 0, 0, 0, 0.5, 0, 0, 0));
    }

    @Test
    void calculateFinalDamage_comModificadorPercentualNegativo() {
        // base = 10; × (1 - 0.3) = 7.0; floor = 7; max(0,7) = 7
        assertEquals(7, CharacterAttributeService.calculateFinalDamage(10, 0, 0, 0, 0, 0, 0.3, 0, 0));
    }

    @Test
    void calculateFinalDamage_comFlat() {
        // base = 10; × 1.0 = 10; floor = 10; + 5 - 2 = 13; max(0,13) = 13
        assertEquals(13, CharacterAttributeService.calculateFinalDamage(10, 0, 0, 0, 0, 0, 0, 5, 2));
    }

    @Test
    void calculateFinalDamage_nunca_negativoPorFlat() {
        // base = 1; flat negativo grande → max(0,...)
        assertEquals(0, CharacterAttributeService.calculateFinalDamage(1, 0, 0, 0, 0, 0, 0, 0, 100));
    }

    @Test
    void calculateFinalDamage_ordemCorretaFloorAntesFlat() {
        // base = 10; × (1 + 0.15) = 11.5; floor = 11; + 0 - 0 = 11
        assertEquals(11, CharacterAttributeService.calculateFinalDamage(10, 0, 0, 0, 0, 0.15, 0, 0, 0));
    }

    // ── RF-FP-06.7: defesa final ──────────────────────────────────────────────

    @Test
    void calculateFinalDefense_semModificadores() {
        assertEquals(10, CharacterAttributeService.calculateFinalDefense(5, 3, 2, 0, 0, 0, 0, 0, 0));
    }

    @Test
    void calculateFinalDefense_nunca_negativo() {
        assertEquals(0, CharacterAttributeService.calculateFinalDefense(0, 0, 0, 0, 0, 0, 1.5, 0, 0));
    }

    // ── RF-FP-06.8: bloqueio final ────────────────────────────────────────────

    @Test
    void calculateFinalBlock_semModificadores() {
        assertEquals(20, CharacterAttributeService.calculateFinalBlock(15, 5, 0, 0, 0, 0, 0, 0));
    }

    @Test
    void calculateFinalBlock_nunca_negativo() {
        assertEquals(0, CharacterAttributeService.calculateFinalBlock(0, 0, 0, 0, 0, 0, 0, 100));
    }
}
