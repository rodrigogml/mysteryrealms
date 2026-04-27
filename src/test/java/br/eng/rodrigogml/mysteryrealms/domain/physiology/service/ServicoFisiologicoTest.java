package br.eng.rodrigogml.mysteryrealms.domain.physiology.service;

import br.eng.rodrigogml.mysteryrealms.domain.physiology.enums.EfeitoFisiologicoCombinado;
import br.eng.rodrigogml.mysteryrealms.domain.physiology.enums.ResultadoTesteConsciencia;
import br.eng.rodrigogml.mysteryrealms.domain.physiology.model.PhysiologicalState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes do serviço de estado fisiológico — RF-EF-01 a RF-EF-16.
 */
class ServicoFisiologicoTest {

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
        ServicoFisiologico.aplicarTickDeMinuto(s);
        assertEquals(expectedDelta, s.getFadigaMin(), 1e-9);
    }

    @Test
    void minuteTick_fadigaAtualNuncaMenorQueFadigaMin() {
        PhysiologicalState s = freshState();
        s.setFadigaAtual(0.0);
        ServicoFisiologico.aplicarTickDeMinuto(s);
        assertTrue(s.getFadigaAtual() >= s.getFadigaMin(),
                "fadiga_atual deve ser >= fadiga_min após tick");
    }

    @Test
    void minuteTick_incrementaSedeCorretamente() {
        PhysiologicalState s = freshState();
        double expectedDelta = 100.0 / (48.0 * 60.0);
        ServicoFisiologico.aplicarTickDeMinuto(s);
        assertEquals(expectedDelta, s.getSedePct(), 1e-9);
    }

    @Test
    void minuteTick_incrementaFomeCorretamente() {
        PhysiologicalState s = freshState();
        double expectedDelta = 100.0 / (7.0 * 24.0 * 60.0);
        ServicoFisiologico.aplicarTickDeMinuto(s);
        assertEquals(expectedDelta, s.getFomePct(), 1e-9);
    }

    @Test
    void minuteTick_sedeLimitadaA100() {
        PhysiologicalState s = freshState();
        s.setSedePct(99.99);
        ServicoFisiologico.aplicarTickDeMinuto(s);
        assertEquals(100.0, s.getSedePct(), 1e-6);
    }

    @Test
    void minuteTick_fomeLimitadaA100() {
        PhysiologicalState s = freshState();
        s.setFomePct(99.9999);
        ServicoFisiologico.aplicarTickDeMinuto(s);
        assertEquals(100.0, s.getFomePct(), 1e-4);
    }

    // ── RF-EF-02: estados de fadiga ───────────────────────────────────────────

    @Test
    void fatigueState_normalAbaixoDe100Pct() {
        PhysiologicalState s = freshState();
        s.setFadigaAtual(FADIGA_MAX * 0.99);
        assertEquals(ServicoFisiologico.EstadoFadiga.NORMAL,
                ServicoFisiologico.estadoFadiga(s));
    }

    @Test
    void fatigueState_exaustaoEntre100e120Pct() {
        PhysiologicalState s = freshState();
        s.setFadigaAtual(FADIGA_MAX * 1.0);
        assertEquals(ServicoFisiologico.EstadoFadiga.EXAUSTAO,
                ServicoFisiologico.estadoFadiga(s));

        s.setFadigaAtual(FADIGA_MAX * 1.19);
        assertEquals(ServicoFisiologico.EstadoFadiga.EXAUSTAO,
                ServicoFisiologico.estadoFadiga(s));
    }

    @Test
    void fatigueState_colapsoAPartirDe120Pct() {
        PhysiologicalState s = freshState();
        s.setFadigaAtual(FADIGA_MAX * 1.2);
        assertEquals(ServicoFisiologico.EstadoFadiga.COLAPSO_FADIGA,
                ServicoFisiologico.estadoFadiga(s));
    }

    // ── RF-EF-03: estados de sede ─────────────────────────────────────────────

    @Test
    void thirstState_normal0a24() {
        PhysiologicalState s = freshState();
        s.setSedePct(0.0);
        assertEquals(ServicoFisiologico.EstadoSede.NORMAL, ServicoFisiologico.estadoSede(s));
        s.setSedePct(24.9);
        assertEquals(ServicoFisiologico.EstadoSede.NORMAL, ServicoFisiologico.estadoSede(s));
    }

    @Test
    void thirstState_sede25a64() {
        PhysiologicalState s = freshState();
        s.setSedePct(25.0);
        assertEquals(ServicoFisiologico.EstadoSede.SEDE, ServicoFisiologico.estadoSede(s));
        s.setSedePct(64.9);
        assertEquals(ServicoFisiologico.EstadoSede.SEDE, ServicoFisiologico.estadoSede(s));
    }

    @Test
    void thirstState_sedeAgravada65a99() {
        PhysiologicalState s = freshState();
        s.setSedePct(65.0);
        assertEquals(ServicoFisiologico.EstadoSede.SEDE_AGRAVADA, ServicoFisiologico.estadoSede(s));
        s.setSedePct(99.9);
        assertEquals(ServicoFisiologico.EstadoSede.SEDE_AGRAVADA, ServicoFisiologico.estadoSede(s));
    }

    @Test
    void thirstState_colapso100() {
        PhysiologicalState s = freshState();
        s.setSedePct(100.0);
        assertEquals(ServicoFisiologico.EstadoSede.COLAPSO_SEDE, ServicoFisiologico.estadoSede(s));
    }

    // ── RF-EF-04: estados de fome ─────────────────────────────────────────────

    @Test
    void hungerState_normal0a42() {
        PhysiologicalState s = freshState();
        s.setFomePct(42.9);
        assertEquals(ServicoFisiologico.EstadoFome.NORMAL, ServicoFisiologico.estadoFome(s));
    }

    @Test
    void hungerState_fome43a84() {
        PhysiologicalState s = freshState();
        s.setFomePct(43.0);
        assertEquals(ServicoFisiologico.EstadoFome.FOME, ServicoFisiologico.estadoFome(s));
        s.setFomePct(84.9);
        assertEquals(ServicoFisiologico.EstadoFome.FOME, ServicoFisiologico.estadoFome(s));
    }

    @Test
    void hungerState_fomeAgravada85a99() {
        PhysiologicalState s = freshState();
        s.setFomePct(85.0);
        assertEquals(ServicoFisiologico.EstadoFome.FOME_AGRAVADA, ServicoFisiologico.estadoFome(s));
    }

    @Test
    void hungerState_colapso100() {
        PhysiologicalState s = freshState();
        s.setFomePct(100.0);
        assertEquals(ServicoFisiologico.EstadoFome.COLAPSO_FOME, ServicoFisiologico.estadoFome(s));
    }

    // ── RF-EF-12: estados de moral ────────────────────────────────────────────

    @Test
    void moralState_colapsoEmocional0a20() {
        PhysiologicalState s = freshState();
        s.setMoral(0);
        assertEquals(ServicoFisiologico.EstadoMoral.COLAPSO_EMOCIONAL, ServicoFisiologico.estadoMoral(s));
        s.setMoral(20);
        assertEquals(ServicoFisiologico.EstadoMoral.COLAPSO_EMOCIONAL, ServicoFisiologico.estadoMoral(s));
    }

    @Test
    void moralState_moralBaixa21a50() {
        PhysiologicalState s = freshState();
        s.setMoral(21);
        assertEquals(ServicoFisiologico.EstadoMoral.MORAL_BAIXA, ServicoFisiologico.estadoMoral(s));
        s.setMoral(50);
        assertEquals(ServicoFisiologico.EstadoMoral.MORAL_BAIXA, ServicoFisiologico.estadoMoral(s));
    }

    @Test
    void moralState_estavel51a80() {
        PhysiologicalState s = freshState();
        s.setMoral(51);
        assertEquals(ServicoFisiologico.EstadoMoral.MORAL_ESTAVEL, ServicoFisiologico.estadoMoral(s));
        s.setMoral(75);
        assertEquals(ServicoFisiologico.EstadoMoral.MORAL_ESTAVEL, ServicoFisiologico.estadoMoral(s));
    }

    @Test
    void moralState_alta81a100() {
        PhysiologicalState s = freshState();
        s.setMoral(81);
        assertEquals(ServicoFisiologico.EstadoMoral.MORAL_ALTA, ServicoFisiologico.estadoMoral(s));
        s.setMoral(100);
        assertEquals(ServicoFisiologico.EstadoMoral.MORAL_ALTA, ServicoFisiologico.estadoMoral(s));
    }

    // ── RF-EF-07: recuperação por descanso ───────────────────────────────────

    @Test
    void restRecovery_reduzhFadigaAtual() {
        PhysiologicalState s = freshState();
        s.setFadigaAtual(300.0); // 50% da máx
        ServicoFisiologico.aplicarTickDescanso(s, 1.0);
        assertTrue(s.getFadigaAtual() < 300.0, "fadiga_atual deve diminuir após descanso");
    }

    @Test
    void restRecovery_naoDescePorFadigaMin() {
        PhysiologicalState s = freshState();
        s.setFadigaMin(200.0);
        s.setFadigaAtual(200.1);
        ServicoFisiologico.aplicarTickDescanso(s, 1.0);
        assertTrue(s.getFadigaAtual() >= s.getFadigaMin(),
                "fadiga_atual não pode descer abaixo de fadiga_min");
    }

    // ── RF-EF-08: fator de qualidade do sono ─────────────────────────────────

    @Test
    void sleepQualityFactor_minimoMeiaUm() {
        double factor = ServicoFisiologico.fatorQualidadeSono(0, 100, 0);
        assertTrue(factor >= 0.5, "Fator de qualidade não pode ser < 0,5");
    }

    @Test
    void sleepQualityFactor_maximoUm() {
        double factor = ServicoFisiologico.fatorQualidadeSono(100, 0, 100);
        assertEquals(1.0, factor, 1e-9);
    }

    @Test
    void sleepQualityFactor_limiteClampBaixo() {
        // C=0, R=100, S=0 → raw bem abaixo de 0,5 → clamp a 0,5
        double factor = ServicoFisiologico.fatorQualidadeSono(0, 100, 0);
        assertEquals(0.5, factor, 1e-9);
    }

    // ── RF-EF-10: critérios de despertar ─────────────────────────────────────

    @Test
    void canWakeFromFaint_trueQuandoCondicoesAtendidas() {
        PhysiologicalState s = freshState();
        s.setPontosVida(PV_MAX * 0.9);
        s.setFadigaAtual(FADIGA_MAX * 0.05);
        s.setFadigaMin(FADIGA_MAX * 0.05);
        assertTrue(ServicoFisiologico.podeDespertarDoDesmaio(s));
    }

    @Test
    void canWakeFromFaint_falseComPVBaixo() {
        PhysiologicalState s = freshState();
        s.setPontosVida(PV_MAX * 0.89);
        s.setFadigaAtual(0.0);
        s.setFadigaMin(0.0);
        assertFalse(ServicoFisiologico.podeDespertarDoDesmaio(s));
    }

    @Test
    void canWakeFromFaint_falseComFadigaAlta() {
        PhysiologicalState s = freshState();
        s.setPontosVida(PV_MAX);
        s.setFadigaAtual(FADIGA_MAX * 0.11);
        s.setFadigaMin(0.0);
        assertFalse(ServicoFisiologico.podeDespertarDoDesmaio(s));
    }

    // ── RF-EF-13: deltas de moral ─────────────────────────────────────────────

    @Test
    void moralDelta_quedaDeAliado() {
        PhysiologicalState s = freshState();
        s.setMoral(75);
        ServicoFisiologico.aplicarDeltaMoralAliadoCaido(s);
        assertEquals(67, s.getMoral());
    }

    @Test
    void moralDelta_vitoriaCombate() {
        PhysiologicalState s = freshState();
        s.setMoral(60);
        ServicoFisiologico.aplicarDeltaMoralVitoriaCombate(s);
        assertEquals(66, s.getMoral());
    }

    @Test
    void moralDelta_limitadoA100() {
        PhysiologicalState s = freshState();
        s.setMoral(98);
        ServicoFisiologico.aplicarDeltaMoralBomSono(s);
        assertEquals(100, s.getMoral());
    }

    @Test
    void moralDelta_limitadoA0() {
        PhysiologicalState s = freshState();
        s.setMoral(5);
        ServicoFisiologico.aplicarDeltaMoralDesmaio(s);
        assertEquals(0, s.getMoral());
    }

    // ── RF-EF-05: interações combinadas ──────────────────────────────────────

    @Test
    void combinedEffect_none_semEstadosGraves() {
        // RF-EF-05
        PhysiologicalState s = freshState();
        assertEquals(EfeitoFisiologicoCombinado.NENHUM,
                ServicoFisiologico.efeitoCombinado(s));
    }

    @Test
    void combinedEffect_sedeExaustao() {
        // RF-EF-05
        PhysiologicalState s = freshState();
        s.setSedePct(70.0);                    // sede_agravada
        s.setFadigaAtual(FADIGA_MAX * 1.05);   // exaustao
        assertEquals(EfeitoFisiologicoCombinado.SEDE_EXAUSTAO,
                ServicoFisiologico.efeitoCombinado(s));
    }

    @Test
    void combinedEffect_fomeExaustao() {
        // RF-EF-05
        PhysiologicalState s = freshState();
        s.setFomePct(90.0);                    // fome_agravada
        s.setFadigaAtual(FADIGA_MAX * 1.05);   // exaustao
        assertEquals(EfeitoFisiologicoCombinado.FOME_EXAUSTAO,
                ServicoFisiologico.efeitoCombinado(s));
    }

    @Test
    void combinedEffect_fomeSede_aplicaMoralDelta() {
        // RF-EF-05
        PhysiologicalState s = freshState();
        s.setMoral(75);
        s.setFomePct(90.0);  // fome_agravada
        s.setSedePct(70.0);  // sede_agravada
        ServicoFisiologico.efeitoCombinado(s);
        assertEquals(60, s.getMoral(), "moral deve cair -15 com fome_agravada + sede_agravada");
    }

    // ── RF-EF-09: recuperação por itens ──────────────────────────────────────

    @Test
    void itemRecovery_pvAumentaClampado() {
        // RF-EF-09
        PhysiologicalState s = freshState();
        s.setPontosVida(20.0);
        ServicoFisiologico.aplicarRecuperacaoPorItem(s, 100.0, 0, 0, 0, 0);
        assertEquals(PV_MAX, s.getPontosVida(), "PV não deve ultrapassar pontosVidaMax");
    }

    @Test
    void itemRecovery_fadigaReducaoNaoAbaixoDeMin() {
        // RF-EF-09
        PhysiologicalState s = freshState();
        s.setFadigaMin(100.0);
        s.setFadigaAtual(105.0);
        ServicoFisiologico.aplicarRecuperacaoPorItem(s, 0, -50.0, 0, 0, 0);
        assertEquals(100.0, s.getFadigaAtual(), "fadiga_atual não pode descer abaixo de fadiga_min");
    }

    @Test
    void itemRecovery_fomeReduzidaClampado() {
        // RF-EF-09
        PhysiologicalState s = freshState();
        s.setFomePct(5.0);
        ServicoFisiologico.aplicarRecuperacaoPorItem(s, 0, 0, -50.0, 0, 0);
        assertEquals(0.0, s.getFomePct(), 1e-9);
    }

    @Test
    void itemRecovery_moralLimitadoA100() {
        // RF-EF-09
        PhysiologicalState s = freshState();
        s.setMoral(95);
        ServicoFisiologico.aplicarRecuperacaoPorItem(s, 0, 0, 0, 0, 10);
        assertEquals(100, s.getMoral());
    }

    // ── RF-EF-11: estado crítico por PV ──────────────────────────────────────

    @Test
    void isPvCritical_trueQuandoPvZero() {
        // RF-EF-11
        PhysiologicalState s = freshState();
        s.setPontosVida(0.0);
        assertTrue(ServicoFisiologico.ehPvCritico(s));
    }

    @Test
    void isPvCritical_falseComPvPositivo() {
        // RF-EF-11
        PhysiologicalState s = freshState();
        s.setPontosVida(1.0);
        assertFalse(ServicoFisiologico.ehPvCritico(s));
    }

    @Test
    void consciousnessTest_success_setPv1eFadiga90Pct() {
        // RF-EF-11
        PhysiologicalState s = freshState();
        s.setPontosVida(0.0);
        var result = ServicoFisiologico.testeConsciencia(s, true);
        assertEquals(ResultadoTesteConsciencia.SUCESSO, result);
        assertEquals(1.0, s.getPontosVida(), 1e-9);
        assertEquals(0.90 * FADIGA_MAX, s.getFadigaAtual(), 1e-9);
    }

    @Test
    void consciousnessTest_fainted_naoModificaState() {
        // RF-EF-11
        PhysiologicalState s = freshState();
        s.setPontosVida(0.0);
        double pvAntes = s.getPontosVida();
        var result = ServicoFisiologico.testeConsciencia(s, false);
        assertEquals(ResultadoTesteConsciencia.DESMAIADO, result);
        assertEquals(pvAntes, s.getPontosVida(), 1e-9, "state não deve ser modificado em DESMAIADO");
    }

    // ── RF-EF-14: moral interage com fadiga ──────────────────────────────────

    @Test
    void moralFatigueCostMultiplier_colapso_110Pct() {
        // RF-EF-14
        PhysiologicalState s = freshState();
        s.setMoral(10);
        assertEquals(1.10, ServicoFisiologico.multiplicadorCustoFadigaPorMoral(s), 1e-9);
    }

    @Test
    void moralFatigueCostMultiplier_alta_90Pct() {
        // RF-EF-14
        PhysiologicalState s = freshState();
        s.setMoral(90);
        assertEquals(0.90, ServicoFisiologico.multiplicadorCustoFadigaPorMoral(s), 1e-9);
    }

    @Test
    void moralFatigueCostMultiplier_normal_semAjuste() {
        // RF-EF-14
        PhysiologicalState s = freshState();
        s.setMoral(60);
        assertEquals(1.0, ServicoFisiologico.multiplicadorCustoFadigaPorMoral(s), 1e-9);
    }

    // ── RF-EF-15: moral por repouso ───────────────────────────────────────────

    @Test
    void moralDelta_quietRest_mais5() {
        // RF-EF-15
        PhysiologicalState s = freshState();
        s.setMoral(70);
        ServicoFisiologico.aplicarDeltaMoralDescansoQuieto(s);
        assertEquals(75, s.getMoral());
    }

    @Test
    void moralDelta_interruptedSleep_menos8() {
        // RF-EF-15
        PhysiologicalState s = freshState();
        s.setMoral(50);
        ServicoFisiologico.aplicarDeltaMoralSonoInterrompido(s);
        assertEquals(42, s.getMoral());
    }
}
