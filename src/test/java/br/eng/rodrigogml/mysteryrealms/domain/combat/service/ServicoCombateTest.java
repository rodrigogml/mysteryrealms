package br.eng.rodrigogml.mysteryrealms.domain.combat.service;

import br.eng.rodrigogml.mysteryrealms.domain.combat.model.RoladorDados;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes de resolução de combate — RF-CT-01 a RF-CT-13.
 */
class ServicoCombateTest {

    private static final RoladorDados FIXO_10 = RoladorDados.fixo(10);
    private static final RoladorDados FIXO_1  = RoladorDados.fixo(1);
    private static final RoladorDados FIXO_20 = RoladorDados.fixo(20);

    // ── RF-CT-01: fórmula de teste ────────────────────────────────────────────

    @Test
    void testResult_somaBaseModificadoresEDado() {
        // valorBase=5, dado=10, modificadores=2 → 17
        assertEquals(17, ServicoCombate.resultadoTeste(5, 2, FIXO_10));
    }

    @Test
    void testResult_semModificadores() {
        // valorBase=4, dado=10 → 14
        assertEquals(14, ServicoCombate.resultadoTeste(4, 0, FIXO_10));
    }

    // ── RF-CT-02: sucesso por CD ──────────────────────────────────────────────

    @Test
    void isSuccess_igualAoCdÉSucesso() {
        assertTrue(ServicoCombate.ehSucesso(12, 12));
    }

    @Test
    void isSuccess_maiorQueCdÉSucesso() {
        assertTrue(ServicoCombate.ehSucesso(15, 12));
    }

    @Test
    void isSuccess_menorQueCdÉFalha() {
        assertFalse(ServicoCombate.ehSucesso(11, 12));
    }

    // ── RF-CT-03: vantagem e desvantagem ─────────────────────────────────────

    @Test
    void rollWithAdvantage_retornaMaiorDosDoisDados() {
        // RoladorDados que retorna 10 sempre → ambos 10 → resultado 10
        assertEquals(10, ServicoCombate.rolarComVantagem(FIXO_10));
    }

    @Test
    void rollWithDisadvantage_retornaMenorDosDoisDados() {
        assertEquals(10, ServicoCombate.rolarComDesvantagem(FIXO_10));
    }

    @Test
    void rollWithAdvantage_retornaMaiorQuandoValoresDiferentes() {
        // Roller alterna 1 e 20 → vantagem deve retornar 20
        int[] vals = {1, 20};
        int[] idx   = {0};
        RoladorDados alternating = sides -> vals[idx[0]++ % 2];
        assertEquals(20, ServicoCombate.rolarComVantagem(alternating));
    }

    @Test
    void rollWithDisadvantage_retornaMenorQuandoValoresDiferentes() {
        int[] vals = {1, 20};
        int[] idx   = {0};
        RoladorDados alternating = sides -> vals[idx[0]++ % 2];
        assertEquals(1, ServicoCombate.rolarComDesvantagem(alternating));
    }

    // ── RF-CT-04: iniciativa ─────────────────────────────────────────────────

    @Test
    void rollInitiative_somaDadoMaisAtributos() {
        // dado=10, destreza=3, percepcao=2 → 15
        assertEquals(15, ServicoCombate.rolarIniciativa(3, 2, FIXO_10));
    }

    // ── RF-CT-08: teste de acerto ────────────────────────────────────────────

    @Test
    void rollAttack_dado20MaisPrecisao() {
        // dado=10, precisao=3 → 13
        assertEquals(13, ServicoCombate.rolarAtaque(3, FIXO_10));
    }

    @Test
    void isHit_igualADefesaAcerta() {
        assertTrue(ServicoCombate.acertou(10, 10));
    }

    @Test
    void isHit_maiorQueDefesaAcerta() {
        assertTrue(ServicoCombate.acertou(11, 10));
    }

    @Test
    void isHit_menorQueDefesaErra() {
        assertFalse(ServicoCombate.acertou(9, 10));
    }

    // ── RF-CT-09: bloqueio ───────────────────────────────────────────────────

    @Test
    void damageAfterBlock_semBloqueio() {
        assertEquals(20, ServicoCombate.danoAposBloqueio(20, 0));
    }

    @Test
    void damageAfterBlock_bloqueio50Pct() {
        // 20 × (1 - 0,50) = 10
        assertEquals(10, ServicoCombate.danoAposBloqueio(20, 50));
    }

    @Test
    void damageAfterBlock_bloqueio100Pct() {
        assertEquals(0, ServicoCombate.danoAposBloqueio(20, 100));
    }

    @Test
    void damageAfterBlock_bloqueioAcimaDeCapClampado() {
        // bloqueio > 100 deve ser clampado a 1.0
        assertEquals(0, ServicoCombate.danoAposBloqueio(20, 200));
    }

    @Test
    void damageAfterBlock_nunca_negativo() {
        assertEquals(0, ServicoCombate.danoAposBloqueio(0, 0));
    }

    @Test
    void damageAfterBlock_aplicaFloor() {
        // 11 × (1 - 0.50) = 5.5 → floor = 5
        assertEquals(5, ServicoCombate.danoAposBloqueio(11, 50));
    }

    // ── RF-CT-10: resistência ─────────────────────────────────────────────────

    @Test
    void damageAfterResistance_semResistencia() {
        assertEquals(20, ServicoCombate.danoAposResistencia(20, 0.0));
    }

    @Test
    void damageAfterResistance_50Pct() {
        assertEquals(10, ServicoCombate.danoAposResistencia(20, 0.5));
    }

    @Test
    void damageAfterResistance_100Pct_imunidade() {
        assertEquals(0, ServicoCombate.danoAposResistencia(20, 1.0));
    }

    @Test
    void clampPlayerResistance_limita80Pct() {
        assertEquals(0.80, ServicoCombate.limitarResistenciaJogador(0.90), 1e-9);
        assertEquals(0.75, ServicoCombate.limitarResistenciaJogador(0.75), 1e-9);
    }

    @Test
    void damageAfterResistance_comLimiteJogador_naoZera() {
        double resistEncampada = ServicoCombate.limitarResistenciaJogador(0.90);
        int dano = ServicoCombate.danoAposResistencia(100, resistEncampada);
        assertTrue(dano > 0, "Dano de jogador resistente (80%) não deve ser zero");
    }

    // ── RF-CT-11: aflições ───────────────────────────────────────────────────

    @Test
    void afflictionChance_semResistencia() {
        assertEquals(0.5, ServicoCombate.chanceAfliccao(0.5, 0.0, 0.05), 1e-9);
    }

    @Test
    void afflictionChance_comResistencia50Pct() {
        assertEquals(0.25, ServicoCombate.chanceAfliccao(0.5, 0.5, 0.05), 1e-9);
    }

    @Test
    void afflictionChance_naoAbaixoDoMinimo() {
        double min = 0.05;
        double chance = ServicoCombate.chanceAfliccao(0.5, 1.0, min);
        assertEquals(min, chance, 1e-9);
    }

    @Test
    void afflictionDuration_semResistencia() {
        assertEquals(5, ServicoCombate.duracaoAfliccao(5, 0.0));
    }

    @Test
    void afflictionDuration_comResistenciaTotal() {
        // max(1, floor(5 × 0)) = max(1, 0) = 1
        assertEquals(1, ServicoCombate.duracaoAfliccao(5, 1.0));
    }

    @Test
    void afflictionIntensity_semResistencia() {
        assertEquals(3, ServicoCombate.intensidadeAfliccao(3, 0.0));
    }

    @Test
    void afflictionIntensity_comResistencia50Pct() {
        // floor(3 × 0.5) = 1
        assertEquals(1, ServicoCombate.intensidadeAfliccao(3, 0.5));
    }

    // ── RF-CT-07: pipeline de resolução ──────────────────────────────────────

    @Test
    void resolveDamage_fullPipeline() {
        // 20 dano bruto, bloqueio 50 → 10 pós-bloqueio, resistência 25% → 7 (floor(10×0.75))
        assertEquals(7, ServicoCombate.resolverDano(20, 50, 0.25));
    }

    @Test
    void resolveDamage_semBloqueioSemResistencia() {
        assertEquals(15, ServicoCombate.resolverDano(15, 0, 0.0));
    }

    // ── RF-CT-05: percepção ──────────────────────────────────────────────────

    @Test
    void rollPerception_somaDadoMaisAtributo() {
        // RF-CT-05
        assertEquals(13, ServicoCombate.rolarPercepcao(3, FIXO_10));
    }

    @Test
    void detectsTarget_maiorQueOcultoDetecta() {
        // RF-CT-05
        assertTrue(ServicoCombate.detectaAlvo(15, 12));
    }

    @Test
    void detectsTarget_empateNaoDetecta() {
        // RF-CT-05 — empate favorece o oculto
        assertFalse(ServicoCombate.detectaAlvo(12, 12));
    }

    @Test
    void detectsTarget_menorQueOcultoNaoDetecta() {
        // RF-CT-05
        assertFalse(ServicoCombate.detectaAlvo(10, 15));
    }

    @Test
    void detectsVsCd_igualAoCdDetecta() {
        // RF-CT-05
        assertTrue(ServicoCombate.detectaVersusCd(12, 12));
    }

    @Test
    void detectsVsCd_menorQueCdNaoDetecta() {
        // RF-CT-05
        assertFalse(ServicoCombate.detectaVersusCd(11, 12));
    }

    // ── RF-CT-12: ação preparada ──────────────────────────────────────────────

    @Test
    void isPreparedActionCancelled_nenhumaCondicao_naoCancel() {
        // RF-CT-12
        assertFalse(ServicoCombate.acaoPreparadaCancelada(
                false, false, false, false, false, false));
    }

    @Test
    void isPreparedActionCancelled_paralisia_cancela() {
        // RF-CT-12
        assertTrue(ServicoCombate.acaoPreparadaCancelada(
                true, false, false, false, false, false));
    }

    @Test
    void isPreparedActionCancelled_sonoTorpor_cancela() {
        // RF-CT-12
        assertTrue(ServicoCombate.acaoPreparadaCancelada(
                false, true, false, false, false, false));
    }

    @Test
    void isPreparedActionCancelled_cegueiraSemAlvo_cancela() {
        // RF-CT-12
        assertTrue(ServicoCombate.acaoPreparadaCancelada(
                false, false, true, false, false, false));
    }

    @Test
    void isPreparedActionCancelled_deslocamentoForcado_cancela() {
        // RF-CT-12
        assertTrue(ServicoCombate.acaoPreparadaCancelada(
                false, false, false, true, false, false));
    }

    @Test
    void isPreparedActionCancelled_gatilhoInvalido_cancela() {
        // RF-CT-12
        assertTrue(ServicoCombate.acaoPreparadaCancelada(
                false, false, false, false, true, false));
    }

    @Test
    void isPreparedActionCancelled_proximoTurno_cancela() {
        // RF-CT-12
        assertTrue(ServicoCombate.acaoPreparadaCancelada(
                false, false, false, false, false, true));
    }
}
