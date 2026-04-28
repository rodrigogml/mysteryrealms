package br.eng.rodrigogml.mysteryrealms.domain.physiology.service;

import br.eng.rodrigogml.mysteryrealms.domain.physiology.enums.CombinedPhysiologyEffect;
import br.eng.rodrigogml.mysteryrealms.domain.physiology.enums.ConsciousnessTestResult;
import br.eng.rodrigogml.mysteryrealms.domain.physiology.enums.PhysiologyResolutionPriority;
import br.eng.rodrigogml.mysteryrealms.domain.physiology.model.PhysiologyState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes do serviço de estado fisiológico — RF-EF-01 a RF-EF-16.
 */
class PhysiologyServiceTest {

    private static final double FADIGA_MAX = 600.0;    // constitution=3, willpower=3
    private static final double PV_MAX     = 30.0;     // constitution=3

    private PhysiologyState freshState() {
        return PhysiologyState.initial(FADIGA_MAX, PV_MAX);
    }

    // ── RF-EF-01: tick de minuto ──────────────────────────────────────────────

    @Test
    void minuteTick_incrementaFadigaMin() {
        PhysiologyState s = freshState();
        double expectedDelta = FADIGA_MAX / 1440.0;
        PhysiologyService.applyMinuteTick(s);
        assertEquals(expectedDelta, s.getMinFatigue(), 1e-9);
    }

    @Test
    void minuteTick_fadigaAtualNuncaMenorQueFadigaMin() {
        PhysiologyState s = freshState();
        s.setCurrentFatigue(0.0);
        PhysiologyService.applyMinuteTick(s);
        assertTrue(s.getCurrentFatigue() >= s.getMinFatigue(),
                "fadiga_atual deve ser >= fadiga_min após tick");
    }

    @Test
    void minuteTick_incrementaSedeCorretamente() {
        PhysiologyState s = freshState();
        double expectedDelta = 100.0 / (48.0 * 60.0);
        PhysiologyService.applyMinuteTick(s);
        assertEquals(expectedDelta, s.getThirstPct(), 1e-9);
    }

    @Test
    void minuteTick_incrementaFomeCorretamente() {
        PhysiologyState s = freshState();
        double expectedDelta = 100.0 / (7.0 * 24.0 * 60.0);
        PhysiologyService.applyMinuteTick(s);
        assertEquals(expectedDelta, s.getHungerPct(), 1e-9);
    }

    @Test
    void minuteTick_sedeLimitadaA100() {
        PhysiologyState s = freshState();
        s.setThirstPct(99.99);
        PhysiologyService.applyMinuteTick(s);
        assertEquals(100.0, s.getThirstPct(), 1e-6);
    }

    @Test
    void minuteTick_fomeLimitadaA100() {
        PhysiologyState s = freshState();
        s.setHungerPct(99.9999);
        PhysiologyService.applyMinuteTick(s);
        assertEquals(100.0, s.getHungerPct(), 1e-4);
    }

    // ── RF-EF-02: estados de fadiga ───────────────────────────────────────────

    @Test
    void fatigueState_normalAbaixoDe100Pct() {
        PhysiologyState s = freshState();
        s.setCurrentFatigue(FADIGA_MAX * 0.99);
        assertEquals(PhysiologyService.EstadoFadiga.NORMAL,
                PhysiologyService.fatigueState(s));
    }

    @Test
    void fatigueState_exaustaoEntre100e120Pct() {
        PhysiologyState s = freshState();
        s.setCurrentFatigue(FADIGA_MAX * 1.0);
        assertEquals(PhysiologyService.EstadoFadiga.EXAUSTAO,
                PhysiologyService.fatigueState(s));

        s.setCurrentFatigue(FADIGA_MAX * 1.19);
        assertEquals(PhysiologyService.EstadoFadiga.EXAUSTAO,
                PhysiologyService.fatigueState(s));
    }

    @Test
    void fatigueState_colapsoAPartirDe120Pct() {
        PhysiologyState s = freshState();
        s.setCurrentFatigue(FADIGA_MAX * 1.2);
        assertEquals(PhysiologyService.EstadoFadiga.COLAPSO_FADIGA,
                PhysiologyService.fatigueState(s));
    }

    // ── RF-EF-03: estados de sede ─────────────────────────────────────────────

    @Test
    void thirstState_normal0a24() {
        PhysiologyState s = freshState();
        s.setThirstPct(0.0);
        assertEquals(PhysiologyService.EstadoSede.NORMAL, PhysiologyService.thirstState(s));
        s.setThirstPct(24.9);
        assertEquals(PhysiologyService.EstadoSede.NORMAL, PhysiologyService.thirstState(s));
    }

    @Test
    void thirstState_sede25a64() {
        PhysiologyState s = freshState();
        s.setThirstPct(25.0);
        assertEquals(PhysiologyService.EstadoSede.SEDE, PhysiologyService.thirstState(s));
        s.setThirstPct(64.9);
        assertEquals(PhysiologyService.EstadoSede.SEDE, PhysiologyService.thirstState(s));
    }

    @Test
    void thirstState_sedeAgravada65a99() {
        PhysiologyState s = freshState();
        s.setThirstPct(65.0);
        assertEquals(PhysiologyService.EstadoSede.SEDE_AGRAVADA, PhysiologyService.thirstState(s));
        s.setThirstPct(99.9);
        assertEquals(PhysiologyService.EstadoSede.SEDE_AGRAVADA, PhysiologyService.thirstState(s));
    }

    @Test
    void thirstState_colapso100() {
        PhysiologyState s = freshState();
        s.setThirstPct(100.0);
        assertEquals(PhysiologyService.EstadoSede.COLAPSO_SEDE, PhysiologyService.thirstState(s));
    }

    // ── RF-EF-04: estados de fome ─────────────────────────────────────────────

    @Test
    void hungerState_normal0a42() {
        PhysiologyState s = freshState();
        s.setHungerPct(42.9);
        assertEquals(PhysiologyService.EstadoFome.NORMAL, PhysiologyService.hungerState(s));
    }

    @Test
    void hungerState_fome43a84() {
        PhysiologyState s = freshState();
        s.setHungerPct(43.0);
        assertEquals(PhysiologyService.EstadoFome.FOME, PhysiologyService.hungerState(s));
        s.setHungerPct(84.9);
        assertEquals(PhysiologyService.EstadoFome.FOME, PhysiologyService.hungerState(s));
    }

    @Test
    void hungerState_fomeAgravada85a99() {
        PhysiologyState s = freshState();
        s.setHungerPct(85.0);
        assertEquals(PhysiologyService.EstadoFome.FOME_AGRAVADA, PhysiologyService.hungerState(s));
    }

    @Test
    void hungerState_colapso100() {
        PhysiologyState s = freshState();
        s.setHungerPct(100.0);
        assertEquals(PhysiologyService.EstadoFome.COLAPSO_FOME, PhysiologyService.hungerState(s));
    }

    // ── RF-EF-12: estados de morale ────────────────────────────────────────────

    @Test
    void moralState_colapsoEmocional0a20() {
        PhysiologyState s = freshState();
        s.setMorale(0);
        assertEquals(PhysiologyService.EstadoMoral.COLAPSO_EMOCIONAL, PhysiologyService.moraleState(s));
        s.setMorale(20);
        assertEquals(PhysiologyService.EstadoMoral.COLAPSO_EMOCIONAL, PhysiologyService.moraleState(s));
    }

    @Test
    void moralState_moralBaixa21a50() {
        PhysiologyState s = freshState();
        s.setMorale(21);
        assertEquals(PhysiologyService.EstadoMoral.MORAL_BAIXA, PhysiologyService.moraleState(s));
        s.setMorale(50);
        assertEquals(PhysiologyService.EstadoMoral.MORAL_BAIXA, PhysiologyService.moraleState(s));
    }

    @Test
    void moralState_estavel51a80() {
        PhysiologyState s = freshState();
        s.setMorale(51);
        assertEquals(PhysiologyService.EstadoMoral.MORAL_ESTAVEL, PhysiologyService.moraleState(s));
        s.setMorale(75);
        assertEquals(PhysiologyService.EstadoMoral.MORAL_ESTAVEL, PhysiologyService.moraleState(s));
    }

    @Test
    void moralState_alta81a100() {
        PhysiologyState s = freshState();
        s.setMorale(81);
        assertEquals(PhysiologyService.EstadoMoral.MORAL_ALTA, PhysiologyService.moraleState(s));
        s.setMorale(100);
        assertEquals(PhysiologyService.EstadoMoral.MORAL_ALTA, PhysiologyService.moraleState(s));
    }

    // ── RF-EF-07: recuperação por descanso ───────────────────────────────────

    @Test
    void restRecovery_reduzhFadigaAtual() {
        PhysiologyState s = freshState();
        s.setCurrentFatigue(300.0); // 50% da máx
        PhysiologyService.applyRestTick(s, 1.0);
        assertTrue(s.getCurrentFatigue() < 300.0, "fadiga_atual deve diminuir após descanso");
    }

    @Test
    void restRecovery_naoDescePorFadigaMin() {
        PhysiologyState s = freshState();
        s.setMinFatigue(200.0);
        s.setCurrentFatigue(200.1);
        PhysiologyService.applyRestTick(s, 1.0);
        assertTrue(s.getCurrentFatigue() >= s.getMinFatigue(),
                "fadiga_atual não pode descer abaixo de fadiga_min");
    }

    // ── RF-EF-08: fator de qualidade do sono ─────────────────────────────────

    @Test
    void sleepQualityFactor_minimoMeiaUm() {
        double factor = PhysiologyService.sleepQualityFactor(0, 100, 0);
        assertTrue(factor >= 0.5, "Fator de qualidade não pode ser < 0,5");
    }

    @Test
    void sleepQualityFactor_maximoUm() {
        double factor = PhysiologyService.sleepQualityFactor(100, 0, 100);
        assertEquals(1.0, factor, 1e-9);
    }

    @Test
    void sleepQualityFactor_limiteClampBaixo() {
        // C=0, R=100, S=0 → raw bem abaixo de 0,5 → clamp a 0,5
        double factor = PhysiologyService.sleepQualityFactor(0, 100, 0);
        assertEquals(0.5, factor, 1e-9);
    }

    // ── RF-EF-08: tick de sono ───────────────────────────────────────────────

    @Test
    void sleepTick_reduzFadigaAtual() {
        // RF-EF-08
        PhysiologyState s = freshState();
        s.setCurrentFatigue(300.0);
        PhysiologyService.applySleepTick(s, 1.0);
        assertTrue(s.getCurrentFatigue() < 300.0,
                "fadiga_atual deve diminuir após tick de sono");
    }

    @Test
    void sleepTick_reduzFadigaMin() {
        // RF-EF-08 — sono também reduz fadiga_min
        PhysiologyState s = freshState();
        s.setMinFatigue(100.0);
        s.setCurrentFatigue(200.0);
        PhysiologyService.applySleepTick(s, 1.0);
        assertTrue(s.getMinFatigue() < 100.0,
                "fadiga_min deve diminuir após tick de sono");
    }

    @Test
    void sleepTick_fadigaAtualNuncaMenorQueFadigaMin() {
        // RF-EF-08 — invariante: fadiga_atual >= fadiga_min
        PhysiologyState s = freshState();
        s.setMinFatigue(500.0);
        s.setCurrentFatigue(500.0);
        PhysiologyService.applySleepTick(s, 1.0);
        assertTrue(s.getCurrentFatigue() >= s.getMinFatigue(),
                "fadiga_atual não pode ser menor que fadiga_min após tick de sono");
    }

    @Test
    void sleepTick_recuperaPv() {
        // RF-EF-08 — sono recupera PV
        PhysiologyState s = freshState();
        s.setHealthPoints(PV_MAX * 0.5);
        double pvAntes = s.getHealthPoints();
        PhysiologyService.applySleepTick(s, 1.0);
        assertTrue(s.getHealthPoints() > pvAntes,
                "PV deve aumentar após tick de sono");
    }

    @Test
    void sleepTick_pvNaoUltrapassaMax() {
        // RF-EF-08 — PV não ultrapassa o máximo
        PhysiologyState s = freshState();
        s.setHealthPoints(PV_MAX);
        PhysiologyService.applySleepTick(s, 1.0);
        assertEquals(PV_MAX, s.getHealthPoints(), 1e-9,
                "PV não deve ultrapassar pontosVidaMax após tick de sono");
    }

    @Test
    void sleepTick_fatorQualidadeMaiorRecuperaMais() {
        // RF-EF-08 — fator de qualidade maior resulta em mais recuperação de fadiga
        PhysiologyState s1 = freshState();
        s1.setCurrentFatigue(400.0);
        PhysiologyService.applySleepTick(s1, 1.0);

        PhysiologyState s2 = freshState();
        s2.setCurrentFatigue(400.0);
        PhysiologyService.applySleepTick(s2, 0.5);

        assertTrue(s1.getCurrentFatigue() < s2.getCurrentFatigue(),
                "Fator de qualidade maior deve recuperar mais fadiga");
    }

    @Test
    void hpRecoveryPerMinute_calculoCorreto() {
        // RF-EF-08 — constituicao = maxPv/10; recuperacao = (const/120) × fator
        PhysiologyState s = freshState(); // PV_MAX = 30 → constitution = 3
        double expected = (3.0 / 120.0) * 1.0;
        assertEquals(expected, PhysiologyService.hpRecoveryPerMinute(s, 1.0), 1e-9);
    }

    // ── RF-EF-10: critérios de despertar ─────────────────────────────────────

    @Test
    void canWakeFromFaint_trueQuandoCondicoesAtendidas() {
        PhysiologyState s = freshState();
        s.setHealthPoints(PV_MAX * 0.9);
        s.setCurrentFatigue(FADIGA_MAX * 0.05);
        s.setMinFatigue(FADIGA_MAX * 0.05);
        assertTrue(PhysiologyService.canWakeFromFaint(s));
    }

    @Test
    void canWakeFromFaint_falseComPVBaixo() {
        PhysiologyState s = freshState();
        s.setHealthPoints(PV_MAX * 0.89);
        s.setCurrentFatigue(0.0);
        s.setMinFatigue(0.0);
        assertFalse(PhysiologyService.canWakeFromFaint(s));
    }

    @Test
    void canWakeFromFaint_falseComFadigaAlta() {
        PhysiologyState s = freshState();
        s.setHealthPoints(PV_MAX);
        s.setCurrentFatigue(FADIGA_MAX * 0.11);
        s.setMinFatigue(0.0);
        assertFalse(PhysiologyService.canWakeFromFaint(s));
    }

    // ── RF-EF-13: deltas de morale ─────────────────────────────────────────────

    @Test
    void moralDelta_quedaDeAliado() {
        PhysiologyState s = freshState();
        s.setMorale(75);
        PhysiologyService.applyMoraleDeltaAllyDown(s);
        assertEquals(67, s.getMorale());
    }

    @Test
    void moralDelta_vitoriaCombate() {
        PhysiologyState s = freshState();
        s.setMorale(60);
        PhysiologyService.applyMoraleDeltaCombatVictory(s);
        assertEquals(66, s.getMorale());
    }

    @Test
    void moralDelta_limitadoA100() {
        PhysiologyState s = freshState();
        s.setMorale(98);
        PhysiologyService.applyMoraleDeltaGoodSleep(s);
        assertEquals(100, s.getMorale());
    }

    @Test
    void moralDelta_limitadoA0() {
        PhysiologyState s = freshState();
        s.setMorale(5);
        PhysiologyService.applyMoraleDeltaFaint(s);
        assertEquals(0, s.getMorale());
    }

    // ── RF-EF-05: interações combinadas ──────────────────────────────────────

    @Test
    void combinedEffect_none_semEstadosGraves() {
        // RF-EF-05
        PhysiologyState s = freshState();
        assertEquals(CombinedPhysiologyEffect.NONE,
                PhysiologyService.combinedEffect(s));
    }

    @Test
    void combinedEffect_sedeExaustao() {
        // RF-EF-05
        PhysiologyState s = freshState();
        s.setThirstPct(70.0);                    // sede_agravada
        s.setCurrentFatigue(FADIGA_MAX * 1.05);   // exaustao
        assertEquals(CombinedPhysiologyEffect.THIRST_EXHAUSTION,
                PhysiologyService.combinedEffect(s));
    }

    @Test
    void combinedEffect_fomeExaustao() {
        // RF-EF-05
        PhysiologyState s = freshState();
        s.setHungerPct(90.0);                    // fome_agravada
        s.setCurrentFatigue(FADIGA_MAX * 1.05);   // exaustao
        assertEquals(CombinedPhysiologyEffect.HUNGER_EXHAUSTION,
                PhysiologyService.combinedEffect(s));
    }

    @Test
    void combinedEffect_fomeSede_aplicaMoralDelta() {
        // RF-EF-05
        PhysiologyState s = freshState();
        s.setMorale(75);
        s.setHungerPct(90.0);  // fome_agravada
        s.setThirstPct(70.0);  // sede_agravada
        PhysiologyService.combinedEffect(s);
        assertEquals(60, s.getMorale(), "moral deve cair -15 com fome_agravada + sede_agravada");
    }

    // ── RF-EF-09: recuperação por itens ──────────────────────────────────────

    @Test
    void itemRecovery_pvAumentaClampado() {
        // RF-EF-09
        PhysiologyState s = freshState();
        s.setHealthPoints(20.0);
        PhysiologyService.applyItemRecovery(s, 100.0, 0, 0, 0, 0);
        assertEquals(PV_MAX, s.getHealthPoints(), "PV não deve ultrapassar pontosVidaMax");
    }

    @Test
    void itemRecovery_fadigaReducaoNaoAbaixoDeMin() {
        // RF-EF-09
        PhysiologyState s = freshState();
        s.setMinFatigue(100.0);
        s.setCurrentFatigue(105.0);
        PhysiologyService.applyItemRecovery(s, 0, -50.0, 0, 0, 0);
        assertEquals(100.0, s.getCurrentFatigue(), "fadiga_atual não pode descer abaixo de fadiga_min");
    }

    @Test
    void itemRecovery_fomeReduzidaClampado() {
        // RF-EF-09
        PhysiologyState s = freshState();
        s.setHungerPct(5.0);
        PhysiologyService.applyItemRecovery(s, 0, 0, -50.0, 0, 0);
        assertEquals(0.0, s.getHungerPct(), 1e-9);
    }

    @Test
    void itemRecovery_moralLimitadoA100() {
        // RF-EF-09
        PhysiologyState s = freshState();
        s.setMorale(95);
        PhysiologyService.applyItemRecovery(s, 0, 0, 0, 0, 10);
        assertEquals(100, s.getMorale());
    }

    // ── RF-EF-11: estado crítico por PV ──────────────────────────────────────

    @Test
    void isPvCritical_trueQuandoPvZero() {
        // RF-EF-11
        PhysiologyState s = freshState();
        s.setHealthPoints(0.0);
        assertTrue(PhysiologyService.isHealthCritical(s));
    }

    @Test
    void isPvCritical_falseComPvPositivo() {
        // RF-EF-11
        PhysiologyState s = freshState();
        s.setHealthPoints(1.0);
        assertFalse(PhysiologyService.isHealthCritical(s));
    }

    @Test
    void consciousnessTest_success_setPv1eFadiga90Pct() {
        // RF-EF-11
        PhysiologyState s = freshState();
        s.setHealthPoints(0.0);
        var result = PhysiologyService.consciousnessTest(s, true);
        assertEquals(ConsciousnessTestResult.SUCCESS, result);
        assertEquals(1.0, s.getHealthPoints(), 1e-9);
        assertEquals(0.90 * FADIGA_MAX, s.getCurrentFatigue(), 1e-9);
    }

    @Test
    void consciousnessTest_fainted_naoModificaState() {
        // RF-EF-11
        PhysiologyState s = freshState();
        s.setHealthPoints(0.0);
        double pvAntes = s.getHealthPoints();
        var result = PhysiologyService.consciousnessTest(s, false);
        assertEquals(ConsciousnessTestResult.FAINTED, result);
        assertEquals(pvAntes, s.getHealthPoints(), 1e-9, "state não deve ser modificado em DESMAIADO");
    }

    // ── RF-EF-14: morale interage com fadiga ──────────────────────────────────

    @Test
    void moralFatigueCostMultiplier_colapso_110Pct() {
        // RF-EF-14
        PhysiologyState s = freshState();
        s.setMorale(10);
        assertEquals(1.10, PhysiologyService.fatigueCostMultiplierByMorale(s), 1e-9);
    }

    @Test
    void moralFatigueCostMultiplier_alta_90Pct() {
        // RF-EF-14
        PhysiologyState s = freshState();
        s.setMorale(90);
        assertEquals(0.90, PhysiologyService.fatigueCostMultiplierByMorale(s), 1e-9);
    }

    @Test
    void moralFatigueCostMultiplier_normal_semAjuste() {
        // RF-EF-14
        PhysiologyState s = freshState();
        s.setMorale(60);
        assertEquals(1.0, PhysiologyService.fatigueCostMultiplierByMorale(s), 1e-9);
    }

    // ── RF-EF-15: morale por repouso ───────────────────────────────────────────

    @Test
    void moralDelta_quietRest_mais5() {
        // RF-EF-15
        PhysiologyState s = freshState();
        s.setMorale(70);
        PhysiologyService.applyMoraleDeltaQuietRest(s);
        assertEquals(75, s.getMorale());
    }

    @Test
    void moralDelta_interruptedSleep_menos8() {
        // RF-EF-15
        PhysiologyState s = freshState();
        s.setMorale(50);
        PhysiologyService.applyMoraleDeltaInterruptedSleep(s);
        assertEquals(42, s.getMorale());
    }

    // ── RF-EF-06: tabela de precedência de estados ────────────────────────────

    @Test
    void resolutionPriority_colapsoEhPrioridade1() {
        // RF-EF-06
        assertEquals(1, PhysiologyResolutionPriority.COLLAPSE_UNCONSCIOUSNESS.getPriority());
    }

    @Test
    void resolutionPriority_estadoCriticoEhPrioridade2() {
        // RF-EF-06
        assertEquals(2, PhysiologyResolutionPriority.CRITICAL_HP_STATE.getPriority());
    }

    @Test
    void resolutionPriority_estadosGravesEhPrioridade3() {
        // RF-EF-06
        assertEquals(3, PhysiologyResolutionPriority.SEVERE_PHYSIOLOGY_STATES.getPriority());
    }

    @Test
    void resolutionPriority_aflicoesDebilitantesEhPrioridade4() {
        // RF-EF-06
        assertEquals(4, PhysiologyResolutionPriority.COMBAT_AFFLICTIONS.getPriority());
    }

    @Test
    void resolutionPriority_estadosModeradosEhPrioridade5() {
        // RF-EF-06
        assertEquals(5, PhysiologyResolutionPriority.MODERATE_PHYSIOLOGY_STATES.getPriority());
    }

    @Test
    void resolutionPriority_ordemCorreta_colapsoAntesCritico() {
        // RF-EF-06 — prioridade 1 < prioridade 2 (menor = mais prioritário)
        assertTrue(
                PhysiologyResolutionPriority.COLLAPSE_UNCONSCIOUSNESS.getPriority()
                        < PhysiologyResolutionPriority.CRITICAL_HP_STATE.getPriority());
    }

    @Test
    void resolutionPriority_ordemCorreta_todosCincoValoresDistintos() {
        // RF-EF-06 — tabela deve ter exatamente 5 categorias com prioridades únicas
        PhysiologyResolutionPriority[] values = PhysiologyResolutionPriority.values();
        assertEquals(5, values.length);
        long distinct = java.util.Arrays.stream(values)
                .mapToInt(PhysiologyResolutionPriority::getPriority)
                .distinct()
                .count();
        assertEquals(5, distinct);
    }
}
