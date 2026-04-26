package br.eng.rodrigogml.mysteryrealms.domain.combat.service;

import br.eng.rodrigogml.mysteryrealms.domain.combat.model.DiceRoller;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes de resolução de combate — RF-CT-01 a RF-CT-13.
 */
class CombatServiceTest {

    private static final DiceRoller FIXO_10 = DiceRoller.fixed(10);
    private static final DiceRoller FIXO_1  = DiceRoller.fixed(1);
    private static final DiceRoller FIXO_20 = DiceRoller.fixed(20);

    // ── RF-CT-01: fórmula de teste ────────────────────────────────────────────

    @Test
    void testResult_somaBaseModificadoresEDado() {
        // valorBase=5, dado=10, modificadores=2 → 17
        assertEquals(17, CombatService.testResult(5, 2, FIXO_10));
    }

    @Test
    void testResult_semModificadores() {
        // valorBase=4, dado=10 → 14
        assertEquals(14, CombatService.testResult(4, 0, FIXO_10));
    }

    // ── RF-CT-02: sucesso por CD ──────────────────────────────────────────────

    @Test
    void isSuccess_igualAoCdÉSucesso() {
        assertTrue(CombatService.isSuccess(12, 12));
    }

    @Test
    void isSuccess_maiorQueCdÉSucesso() {
        assertTrue(CombatService.isSuccess(15, 12));
    }

    @Test
    void isSuccess_menorQueCdÉFalha() {
        assertFalse(CombatService.isSuccess(11, 12));
    }

    // ── RF-CT-03: vantagem e desvantagem ─────────────────────────────────────

    @Test
    void rollWithAdvantage_retornaMaiorDosDoisDados() {
        // DiceRoller que retorna 10 sempre → ambos 10 → resultado 10
        assertEquals(10, CombatService.rollWithAdvantage(FIXO_10));
    }

    @Test
    void rollWithDisadvantage_retornaMenorDosDoisDados() {
        assertEquals(10, CombatService.rollWithDisadvantage(FIXO_10));
    }

    @Test
    void rollWithAdvantage_retornaMaiorQuandoValoresDiferentes() {
        // Roller alterna 1 e 20 → vantagem deve retornar 20
        int[] vals = {1, 20};
        int[] idx   = {0};
        DiceRoller alternating = sides -> vals[idx[0]++ % 2];
        assertEquals(20, CombatService.rollWithAdvantage(alternating));
    }

    @Test
    void rollWithDisadvantage_retornaMenorQuandoValoresDiferentes() {
        int[] vals = {1, 20};
        int[] idx   = {0};
        DiceRoller alternating = sides -> vals[idx[0]++ % 2];
        assertEquals(1, CombatService.rollWithDisadvantage(alternating));
    }

    // ── RF-CT-04: iniciativa ─────────────────────────────────────────────────

    @Test
    void rollInitiative_somaDadoMaisAtributos() {
        // dado=10, destreza=3, percepcao=2 → 15
        assertEquals(15, CombatService.rollInitiative(3, 2, FIXO_10));
    }

    // ── RF-CT-08: teste de acerto ────────────────────────────────────────────

    @Test
    void rollAttack_dado20MaisPrecisao() {
        // dado=10, precisao=3 → 13
        assertEquals(13, CombatService.rollAttack(3, FIXO_10));
    }

    @Test
    void isHit_igualADefesaAcerta() {
        assertTrue(CombatService.isHit(10, 10));
    }

    @Test
    void isHit_maiorQueDefesaAcerta() {
        assertTrue(CombatService.isHit(11, 10));
    }

    @Test
    void isHit_menorQueDefesaErra() {
        assertFalse(CombatService.isHit(9, 10));
    }

    // ── RF-CT-09: bloqueio ───────────────────────────────────────────────────

    @Test
    void damageAfterBlock_semBloqueio() {
        assertEquals(20, CombatService.damageAfterBlock(20, 0));
    }

    @Test
    void damageAfterBlock_bloqueio50Pct() {
        // 20 × (1 - 0,50) = 10
        assertEquals(10, CombatService.damageAfterBlock(20, 50));
    }

    @Test
    void damageAfterBlock_bloqueio100Pct() {
        assertEquals(0, CombatService.damageAfterBlock(20, 100));
    }

    @Test
    void damageAfterBlock_bloqueioAcimaDeCapClampado() {
        // bloqueio > 100 deve ser clampado a 1.0
        assertEquals(0, CombatService.damageAfterBlock(20, 200));
    }

    @Test
    void damageAfterBlock_nunca_negativo() {
        assertEquals(0, CombatService.damageAfterBlock(0, 0));
    }

    @Test
    void damageAfterBlock_aplicaFloor() {
        // 11 × (1 - 0.50) = 5.5 → floor = 5
        assertEquals(5, CombatService.damageAfterBlock(11, 50));
    }

    // ── RF-CT-10: resistência ─────────────────────────────────────────────────

    @Test
    void damageAfterResistance_semResistencia() {
        assertEquals(20, CombatService.damageAfterResistance(20, 0.0));
    }

    @Test
    void damageAfterResistance_50Pct() {
        assertEquals(10, CombatService.damageAfterResistance(20, 0.5));
    }

    @Test
    void damageAfterResistance_100Pct_imunidade() {
        assertEquals(0, CombatService.damageAfterResistance(20, 1.0));
    }

    @Test
    void clampPlayerResistance_limita80Pct() {
        assertEquals(0.80, CombatService.clampPlayerResistance(0.90), 1e-9);
        assertEquals(0.75, CombatService.clampPlayerResistance(0.75), 1e-9);
    }

    @Test
    void damageAfterResistance_comLimiteJogador_naoZera() {
        double resistEncampada = CombatService.clampPlayerResistance(0.90);
        int dano = CombatService.damageAfterResistance(100, resistEncampada);
        assertTrue(dano > 0, "Dano de jogador resistente (80%) não deve ser zero");
    }

    // ── RF-CT-11: aflições ───────────────────────────────────────────────────

    @Test
    void afflictionChance_semResistencia() {
        assertEquals(0.5, CombatService.afflictionChance(0.5, 0.0, 0.05), 1e-9);
    }

    @Test
    void afflictionChance_comResistencia50Pct() {
        assertEquals(0.25, CombatService.afflictionChance(0.5, 0.5, 0.05), 1e-9);
    }

    @Test
    void afflictionChance_naoAbaixoDoMinimo() {
        double min = 0.05;
        double chance = CombatService.afflictionChance(0.5, 1.0, min);
        assertEquals(min, chance, 1e-9);
    }

    @Test
    void afflictionDuration_semResistencia() {
        assertEquals(5, CombatService.afflictionDuration(5, 0.0));
    }

    @Test
    void afflictionDuration_comResistenciaTotal() {
        // max(1, floor(5 × 0)) = max(1, 0) = 1
        assertEquals(1, CombatService.afflictionDuration(5, 1.0));
    }

    @Test
    void afflictionIntensity_semResistencia() {
        assertEquals(3, CombatService.afflictionIntensity(3, 0.0));
    }

    @Test
    void afflictionIntensity_comResistencia50Pct() {
        // floor(3 × 0.5) = 1
        assertEquals(1, CombatService.afflictionIntensity(3, 0.5));
    }

    // ── RF-CT-07: pipeline de resolução ──────────────────────────────────────

    @Test
    void resolveDamage_fullPipeline() {
        // 20 dano bruto, bloqueio 50 → 10 pós-bloqueio, resistência 25% → 7 (floor(10×0.75))
        assertEquals(7, CombatService.resolveDamage(20, 50, 0.25));
    }

    @Test
    void resolveDamage_semBloqueioSemResistencia() {
        assertEquals(15, CombatService.resolveDamage(15, 0, 0.0));
    }
}
