package br.eng.rodrigogml.mysteryrealms.domain.physiology.service;

import br.eng.rodrigogml.mysteryrealms.domain.physiology.model.PhysiologicalState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes do serviço de estado fisiológico — RF-EF-01 a RF-EF-16.
 */
class PhysiologicalServiceTest {

    private static final double FADIGA_MAX = 600.0;    // constituicao=3, vontade=3
    private static final double PV_MAX     = 30.0;     // constituicao=3

    private PhysiologicalState freshState() {
        return PhysiologicalState.initial(FADIGA_MAX, PV_MAX);
    }

    // ── RF-EF-01: tick de minuto ──────────────────────────────────────────────

    @Test
    void minuteTick_incrementaFadigaMin() {
        PhysiologicalState s = freshState();
        double expectedDelta = FADIGA_MAX / 1440.0;
        PhysiologicalService.applyMinuteTick(s);
        assertEquals(expectedDelta, s.getFadigaMin(), 1e-9);
    }

    @Test
    void minuteTick_fadigaAtualNuncaMenorQueFadigaMin() {
        PhysiologicalState s = freshState();
        s.setFadigaAtual(0.0);
        PhysiologicalService.applyMinuteTick(s);
        assertTrue(s.getFadigaAtual() >= s.getFadigaMin(),
                "fadiga_atual deve ser >= fadiga_min após tick");
    }

    @Test
    void minuteTick_incrementaSedeCorretamente() {
        PhysiologicalState s = freshState();
        double expectedDelta = 100.0 / (48.0 * 60.0);
        PhysiologicalService.applyMinuteTick(s);
        assertEquals(expectedDelta, s.getSedePct(), 1e-9);
    }

    @Test
    void minuteTick_incrementaFomeCorretamente() {
        PhysiologicalState s = freshState();
        double expectedDelta = 100.0 / (7.0 * 24.0 * 60.0);
        PhysiologicalService.applyMinuteTick(s);
        assertEquals(expectedDelta, s.getFomePct(), 1e-9);
    }

    @Test
    void minuteTick_sedeLimitadaA100() {
        PhysiologicalState s = freshState();
        s.setSedePct(99.99);
        PhysiologicalService.applyMinuteTick(s);
        assertEquals(100.0, s.getSedePct(), 1e-6);
    }

    @Test
    void minuteTick_fomeLimitadaA100() {
        PhysiologicalState s = freshState();
        s.setFomePct(99.9999);
        PhysiologicalService.applyMinuteTick(s);
        assertEquals(100.0, s.getFomePct(), 1e-4);
    }

    // ── RF-EF-02: estados de fadiga ───────────────────────────────────────────

    @Test
    void fatigueState_normalAbaixoDe100Pct() {
        PhysiologicalState s = freshState();
        s.setFadigaAtual(FADIGA_MAX * 0.99);
        assertEquals(PhysiologicalService.FatigueStateId.NORMAL,
                PhysiologicalService.fatigueState(s));
    }

    @Test
    void fatigueState_exaustaoEntre100e120Pct() {
        PhysiologicalState s = freshState();
        s.setFadigaAtual(FADIGA_MAX * 1.0);
        assertEquals(PhysiologicalService.FatigueStateId.EXAUSTAO,
                PhysiologicalService.fatigueState(s));

        s.setFadigaAtual(FADIGA_MAX * 1.19);
        assertEquals(PhysiologicalService.FatigueStateId.EXAUSTAO,
                PhysiologicalService.fatigueState(s));
    }

    @Test
    void fatigueState_colapsoAPartirDe120Pct() {
        PhysiologicalState s = freshState();
        s.setFadigaAtual(FADIGA_MAX * 1.2);
        assertEquals(PhysiologicalService.FatigueStateId.COLAPSO_FADIGA,
                PhysiologicalService.fatigueState(s));
    }

    // ── RF-EF-03: estados de sede ─────────────────────────────────────────────

    @Test
    void thirstState_normal0a24() {
        PhysiologicalState s = freshState();
        s.setSedePct(0.0);
        assertEquals(PhysiologicalService.ThirstStateId.NORMAL, PhysiologicalService.thirstState(s));
        s.setSedePct(24.9);
        assertEquals(PhysiologicalService.ThirstStateId.NORMAL, PhysiologicalService.thirstState(s));
    }

    @Test
    void thirstState_sede25a64() {
        PhysiologicalState s = freshState();
        s.setSedePct(25.0);
        assertEquals(PhysiologicalService.ThirstStateId.SEDE, PhysiologicalService.thirstState(s));
        s.setSedePct(64.9);
        assertEquals(PhysiologicalService.ThirstStateId.SEDE, PhysiologicalService.thirstState(s));
    }

    @Test
    void thirstState_sedeAgravada65a99() {
        PhysiologicalState s = freshState();
        s.setSedePct(65.0);
        assertEquals(PhysiologicalService.ThirstStateId.SEDE_AGRAVADA, PhysiologicalService.thirstState(s));
        s.setSedePct(99.9);
        assertEquals(PhysiologicalService.ThirstStateId.SEDE_AGRAVADA, PhysiologicalService.thirstState(s));
    }

    @Test
    void thirstState_colapso100() {
        PhysiologicalState s = freshState();
        s.setSedePct(100.0);
        assertEquals(PhysiologicalService.ThirstStateId.COLAPSO_SEDE, PhysiologicalService.thirstState(s));
    }

    // ── RF-EF-04: estados de fome ─────────────────────────────────────────────

    @Test
    void hungerState_normal0a42() {
        PhysiologicalState s = freshState();
        s.setFomePct(42.9);
        assertEquals(PhysiologicalService.HungerStateId.NORMAL, PhysiologicalService.hungerState(s));
    }

    @Test
    void hungerState_fome43a84() {
        PhysiologicalState s = freshState();
        s.setFomePct(43.0);
        assertEquals(PhysiologicalService.HungerStateId.FOME, PhysiologicalService.hungerState(s));
        s.setFomePct(84.9);
        assertEquals(PhysiologicalService.HungerStateId.FOME, PhysiologicalService.hungerState(s));
    }

    @Test
    void hungerState_fomeAgravada85a99() {
        PhysiologicalState s = freshState();
        s.setFomePct(85.0);
        assertEquals(PhysiologicalService.HungerStateId.FOME_AGRAVADA, PhysiologicalService.hungerState(s));
    }

    @Test
    void hungerState_colapso100() {
        PhysiologicalState s = freshState();
        s.setFomePct(100.0);
        assertEquals(PhysiologicalService.HungerStateId.COLAPSO_FOME, PhysiologicalService.hungerState(s));
    }

    // ── RF-EF-12: estados de moral ────────────────────────────────────────────

    @Test
    void moralState_colapsoEmocional0a20() {
        PhysiologicalState s = freshState();
        s.setMoral(0);
        assertEquals(PhysiologicalService.MoralStateId.COLAPSO_EMOCIONAL, PhysiologicalService.moralState(s));
        s.setMoral(20);
        assertEquals(PhysiologicalService.MoralStateId.COLAPSO_EMOCIONAL, PhysiologicalService.moralState(s));
    }

    @Test
    void moralState_moralBaixa21a50() {
        PhysiologicalState s = freshState();
        s.setMoral(21);
        assertEquals(PhysiologicalService.MoralStateId.MORAL_BAIXA, PhysiologicalService.moralState(s));
        s.setMoral(50);
        assertEquals(PhysiologicalService.MoralStateId.MORAL_BAIXA, PhysiologicalService.moralState(s));
    }

    @Test
    void moralState_estavel51a80() {
        PhysiologicalState s = freshState();
        s.setMoral(51);
        assertEquals(PhysiologicalService.MoralStateId.MORAL_ESTAVEL, PhysiologicalService.moralState(s));
        s.setMoral(75);
        assertEquals(PhysiologicalService.MoralStateId.MORAL_ESTAVEL, PhysiologicalService.moralState(s));
    }

    @Test
    void moralState_alta81a100() {
        PhysiologicalState s = freshState();
        s.setMoral(81);
        assertEquals(PhysiologicalService.MoralStateId.MORAL_ALTA, PhysiologicalService.moralState(s));
        s.setMoral(100);
        assertEquals(PhysiologicalService.MoralStateId.MORAL_ALTA, PhysiologicalService.moralState(s));
    }

    // ── RF-EF-07: recuperação por descanso ───────────────────────────────────

    @Test
    void restRecovery_reduzhFadigaAtual() {
        PhysiologicalState s = freshState();
        s.setFadigaAtual(300.0); // 50% da máx
        PhysiologicalService.applyRestRecoveryTick(s, 1.0);
        assertTrue(s.getFadigaAtual() < 300.0, "fadiga_atual deve diminuir após descanso");
    }

    @Test
    void restRecovery_naoDescePorFadigaMin() {
        PhysiologicalState s = freshState();
        s.setFadigaMin(200.0);
        s.setFadigaAtual(200.1);
        PhysiologicalService.applyRestRecoveryTick(s, 1.0);
        assertTrue(s.getFadigaAtual() >= s.getFadigaMin(),
                "fadiga_atual não pode descer abaixo de fadiga_min");
    }

    // ── RF-EF-08: fator de qualidade do sono ─────────────────────────────────

    @Test
    void sleepQualityFactor_minimoMeiaUm() {
        double factor = PhysiologicalService.sleepQualityFactor(0, 100, 0);
        assertTrue(factor >= 0.5, "Fator de qualidade não pode ser < 0,5");
    }

    @Test
    void sleepQualityFactor_maximoUm() {
        double factor = PhysiologicalService.sleepQualityFactor(100, 0, 100);
        assertEquals(1.0, factor, 1e-9);
    }

    @Test
    void sleepQualityFactor_limiteClampBaixo() {
        // C=0, R=100, S=0 → raw bem abaixo de 0,5 → clamp a 0,5
        double factor = PhysiologicalService.sleepQualityFactor(0, 100, 0);
        assertEquals(0.5, factor, 1e-9);
    }

    // ── RF-EF-10: critérios de despertar ─────────────────────────────────────

    @Test
    void canWakeFromFaint_trueQuandoCondicoesAtendidas() {
        PhysiologicalState s = freshState();
        s.setPontosVida(PV_MAX * 0.9);
        s.setFadigaAtual(FADIGA_MAX * 0.05);
        s.setFadigaMin(FADIGA_MAX * 0.05);
        assertTrue(PhysiologicalService.canWakeFromFaint(s));
    }

    @Test
    void canWakeFromFaint_falseComPVBaixo() {
        PhysiologicalState s = freshState();
        s.setPontosVida(PV_MAX * 0.89);
        s.setFadigaAtual(0.0);
        s.setFadigaMin(0.0);
        assertFalse(PhysiologicalService.canWakeFromFaint(s));
    }

    @Test
    void canWakeFromFaint_falseComFadigaAlta() {
        PhysiologicalState s = freshState();
        s.setPontosVida(PV_MAX);
        s.setFadigaAtual(FADIGA_MAX * 0.11);
        s.setFadigaMin(0.0);
        assertFalse(PhysiologicalService.canWakeFromFaint(s));
    }

    // ── RF-EF-13: deltas de moral ─────────────────────────────────────────────

    @Test
    void moralDelta_quedaDeAliado() {
        PhysiologicalState s = freshState();
        s.setMoral(75);
        PhysiologicalService.applyMoralDeltaAllyFallen(s);
        assertEquals(67, s.getMoral());
    }

    @Test
    void moralDelta_vitoriaCombate() {
        PhysiologicalState s = freshState();
        s.setMoral(60);
        PhysiologicalService.applyMoralDeltaCombatVictory(s);
        assertEquals(66, s.getMoral());
    }

    @Test
    void moralDelta_limitadoA100() {
        PhysiologicalState s = freshState();
        s.setMoral(98);
        PhysiologicalService.applyMoralDeltaGoodSleep(s);
        assertEquals(100, s.getMoral());
    }

    @Test
    void moralDelta_limitadoA0() {
        PhysiologicalState s = freshState();
        s.setMoral(5);
        PhysiologicalService.applyMoralDeltaFainted(s);
        assertEquals(0, s.getMoral());
    }
}
