package br.eng.rodrigogml.mysteryrealms.domain.combat.service;

import br.eng.rodrigogml.mysteryrealms.domain.combat.enums.AfflictionType;
import br.eng.rodrigogml.mysteryrealms.domain.combat.enums.DamageType;
import br.eng.rodrigogml.mysteryrealms.domain.combat.enums.MovementType;
import br.eng.rodrigogml.mysteryrealms.domain.combat.enums.ResistanceType;
import br.eng.rodrigogml.mysteryrealms.domain.combat.model.DiceRoller;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Map;

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
        assertEquals(17, CombatService.testResult(5, 2, FIXO_10));
    }

    @Test
    void testResult_semModificadores() {
        assertEquals(14, CombatService.testResult(4, 0, FIXO_10));
    }

    // ── RF-CT-02: success por CD ──────────────────────────────────────────────

    @Test
    void isSuccess_igualAoCdÉSucesso() { assertTrue(CombatService.isSuccess(12, 12)); }

    @Test
    void isSuccess_maiorQueCdÉSucesso() { assertTrue(CombatService.isSuccess(15, 12)); }

    @Test
    void isSuccess_menorQueCdÉFalha() { assertFalse(CombatService.isSuccess(11, 12)); }

    // ── RF-CT-03: vantagem e desvantagem ─────────────────────────────────────

    @Test
    void rollWithAdvantage_retornaMaiorDosDoisDados() { assertEquals(10, CombatService.rollWithAdvantage(FIXO_10)); }

    @Test
    void rollWithDisadvantage_retornaMenorDosDoisDados() { assertEquals(10, CombatService.rollWithDisadvantage(FIXO_10)); }

    @Test
    void rollWithAdvantage_retornaMaiorQuandoValoresDiferentes() {
        int[] vals = {1, 20};
        int[] idx = {0};
        DiceRoller alternating = sides -> vals[idx[0]++ % 2];
        assertEquals(20, CombatService.rollWithAdvantage(alternating));
    }

    @Test
    void rollWithDisadvantage_retornaMenorQuandoValoresDiferentes() {
        int[] vals = {1, 20};
        int[] idx = {0};
        DiceRoller alternating = sides -> vals[idx[0]++ % 2];
        assertEquals(1, CombatService.rollWithDisadvantage(alternating));
    }

    // ── RF-CT-04: iniciativa ─────────────────────────────────────────────────

    @Test
    void rollInitiative_somaDadoMaisAtributos() { assertEquals(15, CombatService.rollInitiative(3, 2, FIXO_10)); }

    @Test
    void determineInitiativeWinner_maiorIniciativaVence() {
        assertEquals(1, CombatService.determineInitiativeWinner(15, 1, 1, 14, 99, 99, FIXO_10));
    }

    @Test
    void determineInitiativeWinner_empateDesempatadoPorDestreza() {
        assertEquals(1, CombatService.determineInitiativeWinner(15, 4, 1, 15, 3, 99, FIXO_10));
    }

    @Test
    void determineInitiativeWinner_empateDesempatadoPorPercepcao() {
        assertEquals(2, CombatService.determineInitiativeWinner(15, 4, 1, 15, 4, 2, FIXO_10));
    }

    @Test
    void determineInitiativeWinner_empateTotalUsaSorteio() {
        assertEquals(1, CombatService.determineInitiativeWinner(15, 4, 2, 15, 4, 2, FIXO_20));
        assertEquals(2, CombatService.determineInitiativeWinner(15, 4, 2, 15, 4, 2, FIXO_1));
    }

    // ── RF-CT-08: teste de acerto ────────────────────────────────────────────

    @Test
    void rollAttack_dado20MaisPrecisao() { assertEquals(13, CombatService.rollAttack(3, FIXO_10)); }

    @Test
    void isHit_igualADefesaAcerta() { assertTrue(CombatService.hit(10, 10)); }

    @Test
    void isHit_maiorQueDefesaAcerta() { assertTrue(CombatService.hit(11, 10)); }

    @Test
    void isHit_menorQueDefesaErra() { assertFalse(CombatService.hit(9, 10)); }

    // ── RF-CT-09/10/07: dano com múltiplos modificadores ─────────────────────

    @Test
    void resolveDamage_deveManterDanoMinimoZeroComBloqueioEResistenciaNoMaximo() {
        int danoFinal = CombatService.resolveDamage(20, 100, 1.0);
        assertEquals(0, danoFinal);
    }

    @Test
    void resolveDamage_devePreservarDanoMaximoQuandoSemBloqueioESemResistencia() {
        int danoBruto = 27;
        int danoFinal = CombatService.resolveDamage(danoBruto, 0, 0.0);
        assertEquals(danoBruto, danoFinal);
    }

    @Test
    void resolveDamage_deveAplicarResistenciaTotalParaDamageTypeComResistanceTypeCorrespondente() {
        CombatTestFixture fixture = CombatTestFixture.defaultFixture()
                .withResistance(DamageType.FIRE, 1.0);

        int resistencia = fixture.resistanceFor(DamageType.FIRE);
        int danoFinal = CombatService.resolveDamage(35, 0, resistencia / 100.0);

        assertEquals(ResistanceType.FIRE, fixture.resistanceTypeFor(DamageType.FIRE));
        assertEquals(0, danoFinal);
    }

    @Test
    void resolveDamage_deveAplicarResistenciaParcialComArredondamentoParaBaixoPorTipo() {
        CombatTestFixture fixture = CombatTestFixture.defaultFixture()
                .withResistance(DamageType.SLASHING, 0.25);

        int danoFinal = CombatService.resolveDamage(30, 0, fixture.resistanceFor(DamageType.SLASHING) / 100.0);

        assertEquals(22, danoFinal);
    }

    // ── RF-CT-11: aflições ───────────────────────────────────────────────────

    @Test
    void afflictionChance_deveAplicarConcorrrenciaDeAflicoesSemInterferenciaEntreTipos() {
        CombatTestFixture fixture = CombatTestFixture.defaultFixture()
                .withAfflictionResistance(AfflictionType.FEAR, 0.20)
                .withAfflictionResistance(AfflictionType.PARALYSIS, 0.60);

        double chanceFear = CombatService.afflictionChance(0.80, fixture.afflictionResistanceFor(AfflictionType.FEAR), 0.05);
        double chanceParalisia = CombatService.afflictionChance(0.80, fixture.afflictionResistanceFor(AfflictionType.PARALYSIS), 0.05);

        assertEquals(0.64, chanceFear, 1e-9);
        assertEquals(0.32, chanceParalisia, 1e-9);
    }

    @Test
    void afflictionChance_deveZerarSomenteAfliccaoComImunidadeExplicitaEmAplicacaoConcorrente() {
        double chanceParalisia = CombatService.afflictionChance(0.70, 0.10, 0.05, true);
        double chanceCegueira = CombatService.afflictionChance(0.70, 0.10, 0.05, false);

        assertEquals(0.0, chanceParalisia, 1e-9);
        assertEquals(0.63, chanceCegueira, 1e-9);
    }

    // ── RF-CT-06: ciclo de batalha — movimentação ─────────────────────────────

    @Test
    void movementCombatPenalty_deveAplicarPenalidadesDiferentesConformeMovementType() {
        assertEquals(0, CombatService.movementCombatPenalty(MovementType.SHORT));
        assertEquals(0, CombatService.movementCombatPenalty(MovementType.STANDARD));
        assertEquals(-2, CombatService.movementCombatPenalty(MovementType.EXTENDED));
    }
}
