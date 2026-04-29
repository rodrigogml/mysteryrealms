package br.eng.rodrigogml.mysteryrealms.domain.combat.service;

import br.eng.rodrigogml.mysteryrealms.domain.combat.enums.MovementType;
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

    // ── RF-CT-02: success por CD ──────────────────────────────────────────────

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
        // dado=10, dexterity=3, perception=2 → 15
        assertEquals(15, CombatService.rollInitiative(3, 2, FIXO_10));
    }

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
    void rollAttack_dado20MaisPrecisao() {
        // dado=10, precisao=3 → 13
        assertEquals(13, CombatService.rollAttack(3, FIXO_10));
    }

    @Test
    void isHit_igualADefesaAcerta() {
        assertTrue(CombatService.hit(10, 10));
    }

    @Test
    void isHit_maiorQueDefesaAcerta() {
        assertTrue(CombatService.hit(11, 10));
    }

    @Test
    void isHit_menorQueDefesaErra() {
        assertFalse(CombatService.hit(9, 10));
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
        assertEquals(0.80, CombatService.limitPlayerResistance(0.90), 1e-9);
        assertEquals(0.75, CombatService.limitPlayerResistance(0.75), 1e-9);
    }

    @Test
    void clampPlayerResistance_valoresNegativosVirAmZero() {
        assertEquals(0.0, CombatService.limitPlayerResistance(-0.10), 1e-9);
    }

    @Test
    void damageAfterResistance_comLimiteJogador_naoZera() {
        double resistEncampada = CombatService.limitPlayerResistance(0.90);
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
    void afflictionChance_imunidadeExplicitaZeraChance() {
        assertEquals(0.0, CombatService.afflictionChance(0.5, 1.0, 0.05, true), 1e-9);
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

    // ── RF-CT-05: percepção ──────────────────────────────────────────────────

    @Test
    void rollPerception_somaDadoMaisAtributo() {
        // RF-CT-05
        assertEquals(13, CombatService.rollPerception(3, FIXO_10));
    }

    @Test
    void detectsTarget_maiorQueOcultoDetecta() {
        // RF-CT-05
        assertTrue(CombatService.detectTarget(15, 12));
    }

    @Test
    void detectsTarget_empateNaoDetecta() {
        // RF-CT-05 — empate favorece o oculto
        assertFalse(CombatService.detectTarget(12, 12));
    }

    @Test
    void detectsTarget_menorQueOcultoNaoDetecta() {
        // RF-CT-05
        assertFalse(CombatService.detectTarget(10, 15));
    }

    @Test
    void detectsVsCd_igualAoCdDetecta() {
        // RF-CT-05
        assertTrue(CombatService.detectVersusCD(12, 12));
    }

    @Test
    void detectsVsCd_menorQueCdNaoDetecta() {
        // RF-CT-05
        assertFalse(CombatService.detectVersusCD(11, 12));
    }

    // ── RF-CT-12: ação preparada ──────────────────────────────────────────────

    @Test
    void isPreparedActionCancelled_nenhumaCondicao_naoCancel() {
        // RF-CT-12
        assertFalse(CombatService.preparedActionCanceled(
                false, false, false, false, false, false));
    }

    @Test
    void isPreparedActionCancelled_paralisia_cancela() {
        // RF-CT-12
        assertTrue(CombatService.preparedActionCanceled(
                true, false, false, false, false, false));
    }

    @Test
    void isPreparedActionCancelled_sonoTorpor_cancela() {
        // RF-CT-12
        assertTrue(CombatService.preparedActionCanceled(
                false, true, false, false, false, false));
    }

    @Test
    void isPreparedActionCancelled_cegueiraSemAlvo_cancela() {
        // RF-CT-12
        assertTrue(CombatService.preparedActionCanceled(
                false, false, true, false, false, false));
    }

    @Test
    void isPreparedActionCancelled_deslocamentoForcado_cancela() {
        // RF-CT-12
        assertTrue(CombatService.preparedActionCanceled(
                false, false, false, true, false, false));
    }

    @Test
    void isPreparedActionCancelled_gatilhoInvalido_cancela() {
        // RF-CT-12
        assertTrue(CombatService.preparedActionCanceled(
                false, false, false, false, true, false));
    }

    @Test
    void isPreparedActionCancelled_proximoTurno_cancela() {
        // RF-CT-12
        assertTrue(CombatService.preparedActionCanceled(
                false, false, false, false, false, true));
    }

    // ── RF-CT-06: ciclo de batalha — movimentação ─────────────────────────────

    @Test
    void movementType_short_limite3m_semCustoEPenalidadeAcao() {
        // RF-CT-06
        assertEquals(3.0, MovementType.SHORT.getMaxDistanceMeters(), 1e-9);
        assertFalse(MovementType.SHORT.isConsumesMainAction());
        assertFalse(MovementType.SHORT.isAppliesPenalty());
    }

    @Test
    void movementType_standard_limite9m_consumeAcaoPrincipal() {
        // RF-CT-06
        assertEquals(9.0, MovementType.STANDARD.getMaxDistanceMeters(), 1e-9);
        assertTrue(MovementType.STANDARD.isConsumesMainAction());
        assertFalse(MovementType.STANDARD.isAppliesPenalty());
    }

    @Test
    void movementType_extended_limite18m_consumeAcaoEAplicaPenalidade() {
        // RF-CT-06
        assertEquals(18.0, MovementType.EXTENDED.getMaxDistanceMeters(), 1e-9);
        assertTrue(MovementType.EXTENDED.isConsumesMainAction());
        assertTrue(MovementType.EXTENDED.isAppliesPenalty());
    }

    @Test
    void movementType_penalty_valorMenosDois() {
        // RF-CT-06 — penalidade do movimento estendido é -2
        assertEquals(-2, MovementType.PENALTY);
    }

    @Test
    void movementTypeFor_3m_retornaShort() {
        // RF-CT-06
        assertEquals(MovementType.SHORT, CombatService.movementTypeFor(3.0));
    }

    @Test
    void movementTypeFor_distanciaNegativa_retornaNull() {
        assertNull(CombatService.movementTypeFor(-0.1));
    }

    @Test
    void movementTypeFor_9m_retornaStandard() {
        // RF-CT-06 — 4 m não cabe em SHORT, cabe em STANDARD
        assertEquals(MovementType.STANDARD, CombatService.movementTypeFor(4.0));
    }

    @Test
    void movementTypeFor_18m_retornaExtended() {
        // RF-CT-06
        assertEquals(MovementType.EXTENDED, CombatService.movementTypeFor(18.0));
    }

    @Test
    void movementTypeFor_acimaDe18m_retornaNull() {
        // RF-CT-06 — distância acima do limite máximo não tem tipo válido
        assertNull(CombatService.movementTypeFor(18.1));
    }

    @Test
    void movementCombatPenalty_extended_retornaMenosDois() {
        // RF-CT-06
        assertEquals(-2, CombatService.movementCombatPenalty(MovementType.EXTENDED));
    }

    @Test
    void movementCombatPenalty_short_retornaZero() {
        // RF-CT-06 — movimento curto não aplica penalidade
        assertEquals(0, CombatService.movementCombatPenalty(MovementType.SHORT));
    }

    @Test
    void movementCombatPenalty_standard_retornaZero() {
        // RF-CT-06
        assertEquals(0, CombatService.movementCombatPenalty(MovementType.STANDARD));
    }
}
