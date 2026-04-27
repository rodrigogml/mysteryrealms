package br.eng.rodrigogml.mysteryrealms.domain.social;

import br.eng.rodrigogml.mysteryrealms.domain.social.enums.MarkerType;
import br.eng.rodrigogml.mysteryrealms.domain.social.enums.RelationshipBand;
import br.eng.rodrigogml.mysteryrealms.domain.social.enums.SpeechStyle;
import br.eng.rodrigogml.mysteryrealms.domain.social.enums.SpeechStyleValuation;
import br.eng.rodrigogml.mysteryrealms.domain.social.model.*;
import br.eng.rodrigogml.mysteryrealms.domain.social.service.SocialService;
import br.eng.rodrigogml.mysteryrealms.domain.world.model.*;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes do sistema social — RF-SS-01 a RF-SS-09.
 */
class SocialSystemTest {

    // ── RF-SS-01: DialogueNode ────────────────────────────────────────────────

    @Test
    void dialogueNode_criacaoValida() {
        DialogueNode node = buildNode();
        assertEquals("dlg_001", node.dialogoId());
        assertEquals("npc_ferreiro", node.npcId());
        assertEquals(1, node.opcoes().size());
    }

    @Test
    void dialogueNode_idSemPrefixoLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new DialogueNode("001", "npc_x", "Olá", List.of(buildOption("op1"))));
    }

    @Test
    void dialogueNode_semOpcoesLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new DialogueNode("dlg_001", "npc_x", "Olá", List.of()));
    }

    @Test
    void dialogueNode_textVazioLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new DialogueNode("dlg_001", "npc_x", "", List.of(buildOption("op1"))));
    }

    // ── RF-SS-01: DialogueOption ──────────────────────────────────────────────

    @Test
    void dialogueOption_comTesteRequerEfeitosFalha() {
        assertThrows(IllegalArgumentException.class,
                () -> new DialogueOption("op1", SpeechStyle.FALA_DIPLOMATICA, "Opção",
                        SocialTest.cdFixa("persuasao", 12),
                        DialogueEffects.empty(),
                        null));
    }

    @Test
    void dialogueOption_semTesteSemEfeitosFalhaOk() {
        DialogueOption op = new DialogueOption("op1", SpeechStyle.FALA_DIPLOMATICA,
                "Opção", null, DialogueEffects.empty(), null);
        assertNull(op.testeSocial());
        assertNull(op.efeitosFalha());
    }

    // ── RF-SS-03: Estilos de fala ─────────────────────────────────────────────

    @Test
    void speechStyle_chavesPrefixoFala() {
        for (SpeechStyle style : SpeechStyle.values()) {
            assertTrue(style.getChave().startsWith("fala_"),
                    "Chave deve começar com 'fala_': " + style.getChave());
        }
    }

    // ── RF-SS-04: Valoração de estilo ─────────────────────────────────────────

    @Test
    void speechStyleValuation_ajustesCorretos() {
        assertEquals(2, SpeechStyleValuation.GOSTA.getAjusteTeste());
        assertEquals(0, SpeechStyleValuation.TOLERA.getAjusteTeste());
        assertEquals(-2, SpeechStyleValuation.REJEITA.getAjusteTeste());
    }

    // ── RF-SS-05: Teste social ────────────────────────────────────────────────

    @Test
    void socialService_ajustaTestePorEstiloGosta() {
        assertEquals(17, SocialService.ajustarPorEstiloFala(15, SpeechStyleValuation.GOSTA));
    }

    @Test
    void socialService_ajustaTestePorEstiloTolera() {
        assertEquals(15, SocialService.ajustarPorEstiloFala(15, SpeechStyleValuation.TOLERA));
    }

    @Test
    void socialService_ajustaTestePorEstiloRejeita() {
        assertEquals(13, SocialService.ajustarPorEstiloFala(15, SpeechStyleValuation.REJEITA));
    }

    @Test
    void socialService_ajustaTestePorNuloRetornaMesmoValor() {
        assertEquals(10, SocialService.ajustarPorEstiloFala(10, null));
    }

    // ── RF-SS-06: Relacionamento com NPC ────────────────────────────────────

    @Test
    void socialService_aplicaDeltaRelacionamento() {
        assertEquals(30, SocialService.aplicarDeltaRelacionamento(25, 5));
        assertEquals(-15, SocialService.aplicarDeltaRelacionamento(-10, -5));
    }

    @Test
    void socialService_clampMaxRelacionamento() {
        assertEquals(100, SocialService.aplicarDeltaRelacionamento(98, 5));
    }

    @Test
    void socialService_clampMinRelacionamento() {
        assertEquals(-100, SocialService.aplicarDeltaRelacionamento(-95, -10));
    }

    @Test
    void socialService_faixaRelacionamento_neutro() {
        assertEquals(RelationshipBand.NEUTRAL, SocialService.faixaRelacionamento(0));
    }

    @Test
    void socialService_faixaRelacionamento_aliado() {
        assertEquals(RelationshipBand.ALLY, SocialService.faixaRelacionamento(100));
    }

    @Test
    void socialService_faixaRelacionamento_inimigoMortal() {
        assertEquals(RelationshipBand.INIMIGO_MORTAL, SocialService.faixaRelacionamento(-100));
    }

    @Test
    void socialService_faixaRelacionamento_hostil() {
        assertEquals(RelationshipBand.HOSTIL, SocialService.faixaRelacionamento(-50));
    }

    @Test
    void socialService_faixaRelacionamento_favorable() {
        assertEquals(RelationshipBand.FAVORABLE, SocialService.faixaRelacionamento(40));
    }

    // ── RF-SS-07: Reputação ──────────────────────────────────────────────────

    @Test
    void socialService_aplicaDeltaReputacao_semClamp() {
        assertEquals(200, SocialService.aplicarDeltaReputacao(150, 50));
        assertEquals(-500, SocialService.aplicarDeltaReputacao(-490, -10));
    }

    // ── RF-SS-08: DiaryEntry ─────────────────────────────────────────────────

    @Test
    void diaryEntry_criacaoValida() {
        DiaryEntry entry = buildDiaryEntry();
        assertEquals("diary_001", entry.entradaId());
        assertEquals("Encontro com o Ferreiro", entry.titulo());
    }

    @Test
    void diaryEntry_idSemPrefixoLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new DiaryEntry("001", "Titulo", "Resumo.", "D1-08:00",
                        "dlg_001", "op1", DiaryImpact.empty()));
    }

    @Test
    void diaryEntry_tituloCom9PalavrasLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new DiaryEntry("diary_001",
                        "Esta titulo tem exatamente nove palavras no total aqui",
                        "Resumo.", "D1-08:00", "dlg_001", "op1", DiaryImpact.empty()));
    }

    @Test
    void diaryEntry_dataJogoFormatoInvalido() {
        assertThrows(IllegalArgumentException.class,
                () -> new DiaryEntry("diary_001", "Titulo", "Resumo.", "Dia1-08:00",
                        "dlg_001", "op1", DiaryImpact.empty()));
    }

    @Test
    void diaryEntry_dataJogoFormatoValido() {
        DiaryEntry e = buildDiaryEntry();
        assertTrue(e.dataJogo().matches("D\\d+-\\d{2}:\\d{2}"));
    }

    // ── RF-SS-09: Marcadores ─────────────────────────────────────────────────

    @Test
    void marker_flagAtivoPorPadrao() {
        Marker m = Marker.flag("mk_quest_iniciada");
        assertEquals(MarkerType.FLAG, m.getTipo());
        assertTrue(m.isFlagAtivo());
    }

    @Test
    void marker_flagInativo() {
        Marker m = Marker.flagInativo("mk_quest_iniciada");
        assertFalse(m.isFlagAtivo());
    }

    @Test
    void marker_setFlag() {
        Marker m = Marker.flag("mk_quest_iniciada");
        m.setFlag(false);
        assertFalse(m.isFlagAtivo());
    }

    @Test
    void marker_stage_incremento() {
        Marker m = Marker.stage("mk_quest_estagio");
        assertEquals(0, m.getValorInteiro());
        m.increment(2);
        assertEquals(2, m.getValorInteiro());
    }

    @Test
    void marker_counter_incremento() {
        Marker m = Marker.counter("mk_npc_visitas");
        m.increment(1);
        m.increment(1);
        assertEquals(2, m.getValorInteiro());
    }

    @Test
    void marker_setStage() {
        Marker m = Marker.stage("mk_quest_estagio");
        m.setStage(3);
        assertEquals(3, m.getValorInteiro());
    }

    @Test
    void marker_idSemPrefixoLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new Marker("quest_iniciada", MarkerType.FLAG, Boolean.TRUE));
    }

    @Test
    void marker_flagNaoTemValorInteiro() {
        Marker m = Marker.flag("mk_x");
        assertThrows(IllegalStateException.class, m::getValorInteiro);
    }

    @Test
    void marker_incrementFlagLancaExcecao() {
        Marker m = Marker.flag("mk_x");
        assertThrows(IllegalStateException.class, () -> m.increment(1));
    }

    // ── RF-SS-02: Ciclo social obrigatório ───────────────────────────────────

    @Test
    void socialCycle_semTeste_sucesso() {
        // RF-SS-02
        SocialCycleResult result = SocialService.executeSocialCycle(
                50, null, buildNode(), "op1", false, 480L, buildWorldConfig());
        assertTrue(result.isSuccess(), "Sem teste social, ciclo deve ser sucesso");
    }

    @Test
    void socialCycle_comTeste_sucesso() {
        // RF-SS-02
        DialogueOption opcaoComTeste = new DialogueOption(
                "op2", SpeechStyle.FALA_DIPLOMATICA, "Quero um desconto.",
                SocialTest.cdFixa("persuasao", 15),
                new DialogueEffects(Map.of("npc_ferreiro", 10), Map.of(), Map.of(), "Desconto concedido!"),
                DialogueEffects.empty());
        DialogueNode node = new DialogueNode("dlg_002", "npc_ferreiro", "E aí?", List.of(opcaoComTeste));

        SocialCycleResult result = SocialService.executeSocialCycle(
                0, null, node, "op2", true, 480L, buildWorldConfig());
        assertTrue(result.isSuccess());
        assertNotNull(result.getNovoRelacionamentoNpc(), "Delta de relacionamento deve ser calculado");
        assertEquals(10, result.getNovoRelacionamentoNpc()); // 0 + 10
    }

    @Test
    void socialCycle_comTeste_falha() {
        // RF-SS-02
        DialogueOption opcaoComTeste = new DialogueOption(
                "op3", SpeechStyle.FALA_DIPLOMATICA, "Tento enganar.",
                SocialTest.cdFixa("enganacao", 15),
                DialogueEffects.empty(),
                new DialogueEffects(Map.of("npc_ferreiro", -5), Map.of(), Map.of(), "Falha na enganação!"));
        DialogueNode node = new DialogueNode("dlg_003", "npc_ferreiro", "E aí?", List.of(opcaoComTeste));

        SocialCycleResult result = SocialService.executeSocialCycle(
                0, null, node, "op3", false, 480L, buildWorldConfig());
        assertFalse(result.isSuccess());
        assertNotNull(result.getNovoRelacionamentoNpc());
        assertEquals(-5, result.getNovoRelacionamentoNpc()); // 0 + (-5)
    }

    @Test
    void socialCycle_opcaoNaoEncontrada_lancaExcecao() {
        // RF-SS-02
        assertThrows(IllegalArgumentException.class,
                () -> SocialService.executeSocialCycle(
                        null, null, buildNode(), "op_inexistente", true, 0L, buildWorldConfig()));
    }

    @Test
    void socialCycle_comDeltaRelacionamento_geraDiario() {
        // RF-SS-02
        DialogueOption opcao = new DialogueOption(
                "op4", SpeechStyle.FALA_DIPLOMATICA, "Faço uma oferta generosa.",
                null,
                new DialogueEffects(Map.of("npc_ferreiro", 20), Map.of(), Map.of(), "Oferta aceita!"),
                null);
        DialogueNode node = new DialogueNode("dlg_004", "npc_ferreiro", "Proposta?", List.of(opcao));

        SocialCycleResult result = SocialService.executeSocialCycle(
                50, null, node, "op4", true, 480L, buildWorldConfig());
        assertNotNull(result.getDiaryEntry(), "Deve gerar entrada no diário quando há delta");
        assertTrue(result.getDiaryEntry().entradaId().startsWith("diary_"));
        assertTrue(result.getDiaryEntry().dataJogo().matches("D\\d+-\\d{2}:\\d{2}"));
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

    private DialogueNode buildNode() {
        return new DialogueNode("dlg_001", "npc_ferreiro", "Posso ajudá-lo?",
                List.of(buildOption("op1")));
    }

    private DialogueOption buildOption(String id) {
        return new DialogueOption(id, SpeechStyle.FALA_DIPLOMATICA, "Sim, preciso de ajuda.",
                null, DialogueEffects.empty(), null);
    }

    private DiaryEntry buildDiaryEntry() {
        return new DiaryEntry("diary_001", "Encontro com o Ferreiro",
                "O herói conheceu o ferreiro e recebeu uma espada.",
                "D1-08:00", "dlg_001", "op1", DiaryImpact.empty());
    }
}
