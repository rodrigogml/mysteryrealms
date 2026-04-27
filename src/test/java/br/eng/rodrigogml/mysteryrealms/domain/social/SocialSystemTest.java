package br.eng.rodrigogml.mysteryrealms.domain.social;

import br.eng.rodrigogml.mysteryrealms.domain.social.enums.TipoMarcador;
import br.eng.rodrigogml.mysteryrealms.domain.social.enums.FaixaRelacionamento;
import br.eng.rodrigogml.mysteryrealms.domain.social.enums.EstiloDiscurso;
import br.eng.rodrigogml.mysteryrealms.domain.social.enums.AvaliacaoEstiloDiscurso;
import br.eng.rodrigogml.mysteryrealms.domain.social.model.*;
import br.eng.rodrigogml.mysteryrealms.domain.social.service.ServicoSocial;
import br.eng.rodrigogml.mysteryrealms.domain.world.model.*;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes do sistema social — RF-SS-01 a RF-SS-09.
 */
class SocialSystemTest {

    // ── RF-SS-01: NoDialogo ────────────────────────────────────────────────

    @Test
    void dialogueNode_criacaoValida() {
        NoDialogo node = buildNode();
        assertEquals("dlg_001", node.dialogoId());
        assertEquals("npc_ferreiro", node.npcId());
        assertEquals(1, node.opcoes().size());
    }

    @Test
    void dialogueNode_idSemPrefixoLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new NoDialogo("001", "npc_x", "Olá", List.of(buildOption("op1"))));
    }

    @Test
    void dialogueNode_semOpcoesLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new NoDialogo("dlg_001", "npc_x", "Olá", List.of()));
    }

    @Test
    void dialogueNode_textVazioLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new NoDialogo("dlg_001", "npc_x", "", List.of(buildOption("op1"))));
    }

    // ── RF-SS-01: OpcaoDialogo ──────────────────────────────────────────────

    @Test
    void dialogueOption_comTesteRequerEfeitosFalha() {
        assertThrows(IllegalArgumentException.class,
                () -> new OpcaoDialogo("op1", EstiloDiscurso.FALA_DIPLOMATICA, "Opção",
                        TesteSocial.cdFixa("persuasao", 12),
                        EfeitosDialogo.vazio(),
                        null));
    }

    @Test
    void dialogueOption_semTesteSemEfeitosFalhaOk() {
        OpcaoDialogo op = new OpcaoDialogo("op1", EstiloDiscurso.FALA_DIPLOMATICA,
                "Opção", null, EfeitosDialogo.vazio(), null);
        assertNull(op.testeSocial());
        assertNull(op.efeitosFalha());
    }

    // ── RF-SS-03: Estilos de fala ─────────────────────────────────────────────

    @Test
    void speechStyle_chavesPrefixoFala() {
        for (EstiloDiscurso style : EstiloDiscurso.values()) {
            assertTrue(style.getChave().startsWith("fala_"),
                    "Chave deve começar com 'fala_': " + style.getChave());
        }
    }

    // ── RF-SS-04: Valoração de estilo ─────────────────────────────────────────

    @Test
    void speechStyleValuation_ajustesCorretos() {
        assertEquals(2, AvaliacaoEstiloDiscurso.GOSTA.getAjusteTeste());
        assertEquals(0, AvaliacaoEstiloDiscurso.TOLERA.getAjusteTeste());
        assertEquals(-2, AvaliacaoEstiloDiscurso.REJEITA.getAjusteTeste());
    }

    // ── RF-SS-05: Teste social ────────────────────────────────────────────────

    @Test
    void socialService_ajustaTestePorEstiloGosta() {
        assertEquals(17, ServicoSocial.ajustarPorEstiloFala(15, AvaliacaoEstiloDiscurso.GOSTA));
    }

    @Test
    void socialService_ajustaTestePorEstiloTolera() {
        assertEquals(15, ServicoSocial.ajustarPorEstiloFala(15, AvaliacaoEstiloDiscurso.TOLERA));
    }

    @Test
    void socialService_ajustaTestePorEstiloRejeita() {
        assertEquals(13, ServicoSocial.ajustarPorEstiloFala(15, AvaliacaoEstiloDiscurso.REJEITA));
    }

    @Test
    void socialService_ajustaTestePorNuloRetornaMesmoValor() {
        assertEquals(10, ServicoSocial.ajustarPorEstiloFala(10, null));
    }

    // ── RF-SS-06: Relacionamento com NPC ────────────────────────────────────

    @Test
    void socialService_aplicaDeltaRelacionamento() {
        assertEquals(30, ServicoSocial.aplicarDeltaRelacionamento(25, 5));
        assertEquals(-15, ServicoSocial.aplicarDeltaRelacionamento(-10, -5));
    }

    @Test
    void socialService_clampMaxRelacionamento() {
        assertEquals(100, ServicoSocial.aplicarDeltaRelacionamento(98, 5));
    }

    @Test
    void socialService_clampMinRelacionamento() {
        assertEquals(-100, ServicoSocial.aplicarDeltaRelacionamento(-95, -10));
    }

    @Test
    void socialService_faixaRelacionamento_neutro() {
        assertEquals(FaixaRelacionamento.NEUTRO, ServicoSocial.faixaRelacionamento(0));
    }

    @Test
    void socialService_faixaRelacionamento_aliado() {
        assertEquals(FaixaRelacionamento.ALIADO, ServicoSocial.faixaRelacionamento(100));
    }

    @Test
    void socialService_faixaRelacionamento_inimigoMortal() {
        assertEquals(FaixaRelacionamento.INIMIGO_MORTAL, ServicoSocial.faixaRelacionamento(-100));
    }

    @Test
    void socialService_faixaRelacionamento_hostil() {
        assertEquals(FaixaRelacionamento.HOSTIL, ServicoSocial.faixaRelacionamento(-50));
    }

    @Test
    void socialService_faixaRelacionamento_favorable() {
        assertEquals(FaixaRelacionamento.FAVORAVEL, ServicoSocial.faixaRelacionamento(40));
    }

    // ── RF-SS-07: Reputação ──────────────────────────────────────────────────

    @Test
    void socialService_aplicaDeltaReputacao_semClamp() {
        assertEquals(200, ServicoSocial.aplicarDeltaReputacao(150, 50));
        assertEquals(-500, ServicoSocial.aplicarDeltaReputacao(-490, -10));
    }

    // ── RF-SS-08: EntradaDiario ─────────────────────────────────────────────────

    @Test
    void diaryEntry_criacaoValida() {
        EntradaDiario entry = buildDiaryEntry();
        assertEquals("diary_001", entry.entradaId());
        assertEquals("Encontro com o Ferreiro", entry.titulo());
    }

    @Test
    void diaryEntry_idSemPrefixoLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new EntradaDiario("001", "Titulo", "Resumo.", "D1-08:00",
                        "dlg_001", "op1", ImpactoDiario.vazio()));
    }

    @Test
    void diaryEntry_tituloCom9PalavrasLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new EntradaDiario("diary_001",
                        "Esta titulo tem exatamente nove palavras no total aqui",
                        "Resumo.", "D1-08:00", "dlg_001", "op1", ImpactoDiario.vazio()));
    }

    @Test
    void diaryEntry_dataJogoFormatoInvalido() {
        assertThrows(IllegalArgumentException.class,
                () -> new EntradaDiario("diary_001", "Titulo", "Resumo.", "Dia1-08:00",
                        "dlg_001", "op1", ImpactoDiario.vazio()));
    }

    @Test
    void diaryEntry_dataJogoFormatoValido() {
        EntradaDiario e = buildDiaryEntry();
        assertTrue(e.dataJogo().matches("D\\d+-\\d{2}:\\d{2}"));
    }

    // ── RF-SS-09: Marcadores ─────────────────────────────────────────────────

    @Test
    void marker_flagAtivoPorPadrao() {
        Marcador m = Marcador.sinalizador("mk_quest_iniciada");
        assertEquals(TipoMarcador.SINALIZADOR, m.getTipo());
        assertTrue(m.sinalizadorAtivo());
    }

    @Test
    void marker_flagInativo() {
        Marcador m = Marcador.sinalizadorInativo("mk_quest_iniciada");
        assertFalse(m.sinalizadorAtivo());
    }

    @Test
    void marker_setFlag() {
        Marcador m = Marcador.sinalizador("mk_quest_iniciada");
        m.definirSinalizador(false);
        assertFalse(m.sinalizadorAtivo());
    }

    @Test
    void marker_stage_incremento() {
        Marcador m = Marcador.estagio("mk_quest_estagio");
        assertEquals(0, m.valorInteiro());
        m.increment(2);
        assertEquals(2, m.valorInteiro());
    }

    @Test
    void marker_counter_incremento() {
        Marcador m = Marcador.contador("mk_npc_visitas");
        m.increment(1);
        m.increment(1);
        assertEquals(2, m.valorInteiro());
    }

    @Test
    void marker_setStage() {
        Marcador m = Marcador.estagio("mk_quest_estagio");
        m.definirEstagio(3);
        assertEquals(3, m.valorInteiro());
    }

    @Test
    void marker_idSemPrefixoLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new Marcador("quest_iniciada", TipoMarcador.SINALIZADOR, Boolean.TRUE));
    }

    @Test
    void marker_flagNaoTemValorInteiro() {
        Marcador m = Marcador.sinalizador("mk_x");
        assertThrows(IllegalStateException.class, m::valorInteiro);
    }

    @Test
    void marker_incrementFlagLancaExcecao() {
        Marcador m = Marcador.sinalizador("mk_x");
        assertThrows(IllegalStateException.class, () -> m.increment(1));
    }

    // ── RF-SS-02: Ciclo social obrigatório ───────────────────────────────────

    @Test
    void socialCycle_semTeste_sucesso() {
        // RF-SS-02
        ResultadoCicloSocial result = ServicoSocial.executarCicloSocial(
                50, null, buildNode(), "op1", false, 480L, buildWorldConfig());
        assertTrue(result.sucesso(), "Sem teste social, ciclo deve ser sucesso");
    }

    @Test
    void socialCycle_comTeste_sucesso() {
        // RF-SS-02
        OpcaoDialogo opcaoComTeste = new OpcaoDialogo(
                "op2", EstiloDiscurso.FALA_DIPLOMATICA, "Quero um desconto.",
                TesteSocial.cdFixa("persuasao", 15),
                new EfeitosDialogo(Map.of("npc_ferreiro", 10), Map.of(), Map.of(), "Desconto concedido!"),
                EfeitosDialogo.vazio());
        NoDialogo node = new NoDialogo("dlg_002", "npc_ferreiro", "E aí?", List.of(opcaoComTeste));

        ResultadoCicloSocial result = ServicoSocial.executarCicloSocial(
                0, null, node, "op2", true, 480L, buildWorldConfig());
        assertTrue(result.sucesso());
        assertNotNull(result.novoRelacionamentoNpc(), "Delta de relacionamento deve ser calculado");
        assertEquals(10, result.novoRelacionamentoNpc()); // 0 + 10
    }

    @Test
    void socialCycle_comTeste_falha() {
        // RF-SS-02
        OpcaoDialogo opcaoComTeste = new OpcaoDialogo(
                "op3", EstiloDiscurso.FALA_DIPLOMATICA, "Tento enganar.",
                TesteSocial.cdFixa("enganacao", 15),
                EfeitosDialogo.vazio(),
                new EfeitosDialogo(Map.of("npc_ferreiro", -5), Map.of(), Map.of(), "Falha na enganação!"));
        NoDialogo node = new NoDialogo("dlg_003", "npc_ferreiro", "E aí?", List.of(opcaoComTeste));

        ResultadoCicloSocial result = ServicoSocial.executarCicloSocial(
                0, null, node, "op3", false, 480L, buildWorldConfig());
        assertFalse(result.sucesso());
        assertNotNull(result.novoRelacionamentoNpc());
        assertEquals(-5, result.novoRelacionamentoNpc()); // 0 + (-5)
    }

    @Test
    void socialCycle_opcaoNaoEncontrada_lancaExcecao() {
        // RF-SS-02
        assertThrows(IllegalArgumentException.class,
                () -> ServicoSocial.executarCicloSocial(
                        null, null, buildNode(), "op_inexistente", true, 0L, buildWorldConfig()));
    }

    @Test
    void socialCycle_comDeltaRelacionamento_geraDiario() {
        // RF-SS-02
        OpcaoDialogo opcao = new OpcaoDialogo(
                "op4", EstiloDiscurso.FALA_DIPLOMATICA, "Faço uma oferta generosa.",
                null,
                new EfeitosDialogo(Map.of("npc_ferreiro", 20), Map.of(), Map.of(), "Oferta aceita!"),
                null);
        NoDialogo node = new NoDialogo("dlg_004", "npc_ferreiro", "Proposta?", List.of(opcao));

        ResultadoCicloSocial result = ServicoSocial.executarCicloSocial(
                50, null, node, "op4", true, 480L, buildWorldConfig());
        assertNotNull(result.entradaDiario(), "Deve gerar entrada no diário quando há delta");
        assertTrue(result.entradaDiario().entradaId().startsWith("diary_"));
        assertTrue(result.entradaDiario().dataJogo().matches("D\\d+-\\d{2}:\\d{2}"));
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private WorldConfig buildWorldConfig() {
        return new WorldConfig(
                "mundo_teste",
                60, 24, 360,
                List.of(new DayPhase("dia", 0, 1439)),
                List.of(new Season("unica", 1, 360)),
                0);
    }

    private NoDialogo buildNode() {
        return new NoDialogo("dlg_001", "npc_ferreiro", "Posso ajudá-lo?",
                List.of(buildOption("op1")));
    }

    private OpcaoDialogo buildOption(String id) {
        return new OpcaoDialogo(id, EstiloDiscurso.FALA_DIPLOMATICA, "Sim, preciso de ajuda.",
                null, EfeitosDialogo.vazio(), null);
    }

    private EntradaDiario buildDiaryEntry() {
        return new EntradaDiario("diary_001", "Encontro com o Ferreiro",
                "O herói conheceu o ferreiro e recebeu uma espada.",
                "D1-08:00", "dlg_001", "op1", ImpactoDiario.vazio());
    }
}
