package br.eng.rodrigogml.mysteryrealms.domain.social;

import br.eng.rodrigogml.mysteryrealms.domain.social.enums.MarkerType;
import br.eng.rodrigogml.mysteryrealms.domain.social.enums.RelationshipRange;
import br.eng.rodrigogml.mysteryrealms.domain.social.enums.RelationshipState;
import br.eng.rodrigogml.mysteryrealms.domain.social.enums.ReputationRange;
import br.eng.rodrigogml.mysteryrealms.domain.social.enums.DiscourseStyle;
import br.eng.rodrigogml.mysteryrealms.domain.social.enums.DiscourseStyleEvaluation;
import br.eng.rodrigogml.mysteryrealms.domain.social.model.*;
import br.eng.rodrigogml.mysteryrealms.domain.social.service.SocialService;
import br.eng.rodrigogml.mysteryrealms.application.world.entity.DiaryImpactMarkerEntity;
import br.eng.rodrigogml.mysteryrealms.application.world.entity.DiaryImpactRelationshipEntity;
import br.eng.rodrigogml.mysteryrealms.application.world.entity.DiaryImpactReputationEntity;
import br.eng.rodrigogml.mysteryrealms.domain.world.model.*;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes do sistema social — RF-SS-01 a RF-SS-09.
 */
class SocialSystemTest {

    // ── RF-SS-01: DialogNode ────────────────────────────────────────────────

    @Test
    void dialogueNode_criacaoValida() {
        DialogNode node = buildNode();
        assertEquals("dlg_001", node.dialogId());
        assertEquals("npc_ferreiro", node.npcId());
        assertEquals(1, node.options().size());
    }

    @Test
    void dialogueNode_idSemPrefixoLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new DialogNode("001", "npc_x", "Olá", List.of(buildOption("op1"))));
    }

    @Test
    void dialogueNode_semOpcoesLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new DialogNode("dlg_001", "npc_x", "Olá", List.of()));
    }

    @Test
    void dialogueNode_opcaoDuplicadaLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new DialogNode("dlg_001", "npc_x", "Ola",
                        List.of(buildOption("op1"), buildOption("op1"))));
    }

    @Test
    void dialogueNode_textVazioLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new DialogNode("dlg_001", "npc_x", "", List.of(buildOption("op1"))));
    }

    // ── RF-SS-01: DialogOption ──────────────────────────────────────────────

    @Test
    void dialogueOption_comTesteRequerEfeitosFalha() {
        assertThrows(IllegalArgumentException.class,
                () -> new DialogOption("op1", DiscourseStyle.DIPLOMATIC, "Opção",
                        SocialTest.fixedDC("persuasao", 12),
                        DialogEffects.empty(),
                        null));
    }

    @Test
    void dialogueOption_semTesteSemEfeitosFalhaOk() {
        DialogOption op = new DialogOption("op1", DiscourseStyle.DIPLOMATIC,
                "Opção", null, DialogEffects.empty(), null);
        assertNull(op.socialTest());
        assertNull(op.failureEffects());
    }

    @Test
    void dialogEffects_validaPrefixosDeAlvos() {
        assertThrows(IllegalArgumentException.class,
                () -> new DialogEffects(Map.of("ferreiro", 1), Map.of(), Map.of(), ""));
        assertThrows(IllegalArgumentException.class,
                () -> new DialogEffects(Map.of(), Map.of("cidade", 1), Map.of(), ""));
        assertThrows(IllegalArgumentException.class,
                () -> new DialogEffects(Map.of(), Map.of(), Map.of("guilda", 1), ""));
    }

    @Test
    void dialogEffects_copiaMapasDefensivamente() {
        HashMap<String, Integer> relationship = new HashMap<>();
        relationship.put("npc_ferreiro", 1);
        DialogEffects effects = new DialogEffects(relationship, null, null, null);
        relationship.put("npc_ferreiro", 99);

        assertEquals(1, effects.npcRelationshipDeltas().get("npc_ferreiro"));
        assertTrue(effects.localityReputationDeltas().isEmpty());
        assertTrue(effects.factionReputationDeltas().isEmpty());
        assertEquals("", effects.narrativeText());
    }

    // ── RF-SS-03: Estilos de fala ─────────────────────────────────────────────

    @Test
    void speechStyle_chavesPrefixoFala() {
        for (DiscourseStyle style : DiscourseStyle.values()) {
            assertTrue(style.getKey().startsWith("fala_"),
                    "Chave deve começar com 'fala_': " + style.getKey());
        }
    }

    // ── RF-SS-04: Valoração de estilo ─────────────────────────────────────────

    @Test
    void speechStyleValuation_ajustesCorretos() {
        assertEquals(2, DiscourseStyleEvaluation.LIKES.getTestAdjustment());
        assertEquals(0, DiscourseStyleEvaluation.TOLERATES.getTestAdjustment());
        assertEquals(-2, DiscourseStyleEvaluation.REJECTS.getTestAdjustment());
    }

    // ── RF-SS-05: Teste social ────────────────────────────────────────────────

    @Test
    void socialService_ajustaTestePorEstiloGosta() {
        assertEquals(17, SocialService.adjustByTalkStyle(15, DiscourseStyleEvaluation.LIKES));
    }

    @Test
    void socialService_ajustaTestePorEstiloTolera() {
        assertEquals(15, SocialService.adjustByTalkStyle(15, DiscourseStyleEvaluation.TOLERATES));
    }

    @Test
    void socialService_ajustaTestePorEstiloRejeita() {
        assertEquals(13, SocialService.adjustByTalkStyle(15, DiscourseStyleEvaluation.REJECTS));
    }

    @Test
    void socialService_ajustaTestePorNuloRetornaMesmoValor() {
        assertEquals(10, SocialService.adjustByTalkStyle(10, null));
    }

    // ── RF-SS-06: Relacionamento com NPC ────────────────────────────────────

    @Test
    void socialService_aplicaDeltaRelacionamento() {
        assertEquals(30, SocialService.applyRelationshipDelta(25, 5));
        assertEquals(-15, SocialService.applyRelationshipDelta(-10, -5));
    }

    @Test
    void socialService_clampMaxRelacionamento() {
        assertEquals(100, SocialService.applyRelationshipDelta(98, 5));
    }

    @Test
    void socialService_clampMinRelacionamento() {
        assertEquals(-100, SocialService.applyRelationshipDelta(-95, -10));
    }

    @Test
    void socialService_faixaRelacionamento_neutro() {
        assertEquals(RelationshipRange.NEUTRAL, SocialService.relationshipRange(0));
    }

    @Test
    void socialService_faixaRelacionamento_aliado() {
        assertEquals(RelationshipRange.ALLY, SocialService.relationshipRange(100));
    }

    @Test
    void socialService_faixaRelacionamento_inimigoMortal() {
        assertEquals(RelationshipRange.MORTAL_ENEMY, SocialService.relationshipRange(-100));
    }

    @Test
    void socialService_faixaRelacionamento_hostil() {
        assertEquals(RelationshipRange.HOSTILE, SocialService.relationshipRange(-50));
    }

    @Test
    void socialService_faixaRelacionamento_favorable() {
        assertEquals(RelationshipRange.FAVORABLE, SocialService.relationshipRange(40));
    }

    
    @Test
    void socialService_estadoRelacionamento_hostil() {
        assertEquals(RelationshipState.HOSTILE, SocialService.relationshipState(-45));
    }

    @Test
    void socialService_estadoRelacionamento_aliado() {
        assertEquals(RelationshipState.ALLY, SocialService.relationshipState(85));
    }

// ── RF-SS-07: Reputação ──────────────────────────────────────────────────

    @Test
    void socialService_aplicaDeltaReputacao_comImpactoFaixaPositiva() {
        assertEquals(66, SocialService.applyReputationDelta(60, 4));
    }

    @Test
    void socialService_aplicaDeltaReputacao_comImpactoFaixaNegativa() {
        assertEquals(-66, SocialService.applyReputationDelta(-60, -4));
    }

    @Test
    void socialService_faixaReputacao_respeitado() {
        assertEquals(ReputationRange.RESPECTED, SocialService.reputationRange(22));
    }

    @Test
    void socialService_faixaReputacao_infame() {
        assertEquals(ReputationRange.INFAMOUS, SocialService.reputationRange(-80));
    }


    // ── RF-SS-08: DiaryEntry ─────────────────────────────────────────────────

    @Test
    void diaryEntry_criacaoValida() {
        DiaryEntry entry = buildEntradaDiario();
        assertEquals("diary_001", entry.entryId());
        assertEquals("Encontro com o Ferreiro", entry.title());
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
    void diaryEntry_origemDialogoSemPrefixoLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new DiaryEntry("diary_001", "Titulo", "Resumo.", "D1-08:00",
                        "dialogo_001", "op1", DiaryImpact.empty()));
    }

    @Test
    void diaryImpact_validaAlvosEMarcadores() {
        assertThrows(IllegalArgumentException.class,
                () -> new DiaryImpact(Map.of("ferreiro", 1), Map.of(), Map.of(), List.of()));
        assertThrows(IllegalArgumentException.class,
                () -> new DiaryImpact(Map.of(), Map.of("loc_langur", 1), Map.of("guilda", 1), List.of()));
        assertThrows(IllegalArgumentException.class,
                () -> new DiaryImpact(Map.of(), Map.of(), Map.of(), List.of("mk_x")));
    }

    @Test
    void diaryEntry_dataJogoFormatoValido() {
        DiaryEntry e = buildEntradaDiario();
        assertTrue(e.gameDate().matches("D\\d+-\\d{2}:\\d{2}"));
    }

    // ── RF-SS-09: Marcadores ─────────────────────────────────────────────────

    @Test
    void marker_flagAtivoPorPadrao() {
        Marker m = Marker.flag("mk_quest_iniciada");
        assertEquals(MarkerType.FLAG, m.getType());
        assertTrue(m.isFlagActive());
    }

    @Test
    void marker_flagInativo() {
        Marker m = Marker.inactiveFlag("mk_quest_iniciada");
        assertFalse(m.isFlagActive());
    }

    @Test
    void marker_setFlag() {
        Marker m = Marker.flag("mk_quest_iniciada");
        m.setFlag(false);
        assertFalse(m.isFlagActive());
    }

    @Test
    void marker_stage_incremento() {
        Marker m = Marker.stage("mk_quest_estagio");
        assertEquals(0, m.intValue());
        m.increment(2);
        assertEquals(2, m.intValue());
    }

    @Test
    void marker_counter_incremento() {
        Marker m = Marker.counter("mk_npc_visitas");
        m.increment(1);
        m.increment(1);
        assertEquals(2, m.intValue());
    }

    @Test
    void marker_setStage() {
        Marker m = Marker.stage("mk_quest_estagio");
        m.setStage(3);
        assertEquals(3, m.intValue());
    }

    @Test
    void marker_idSemPrefixoLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new Marker("quest_iniciada", MarkerType.FLAG, Boolean.TRUE));
    }

    @Test
    void marker_flagNaoTemValorInteiro() {
        Marker m = Marker.flag("mk_quest_x");
        assertThrows(IllegalStateException.class, m::intValue);
    }

    @Test
    void marker_incrementFlagLancaExcecao() {
        Marker m = Marker.flag("mk_quest_x");
        assertThrows(IllegalStateException.class, () -> m.increment(1));
    }

    @Test
    void marker_idSemDescricaoCamelCaseLancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> new Marker("mk_x", MarkerType.FLAG, Boolean.TRUE));
        assertThrows(IllegalArgumentException.class,
                () -> new Marker("mk_quest_etapa-final", MarkerType.FLAG, Boolean.TRUE));
    }

    @Test
    void marker_decrementaNumericoEValidaPreCondicao() {
        Marker m = Marker.counter("mk_npc_visitas");
        m.increment(3);
        m.decrement(1);

        assertEquals(2, m.intValue());
        assertTrue(m.matchesExpectedValue(2));
        assertFalse(m.matchesExpectedValue(3));
    }

    @Test
    void marker_valorNumericoNaoPodeFicarNegativo() {
        Marker m = Marker.stage("mk_quest_estagio");

        assertThrows(IllegalArgumentException.class, () -> m.decrement(1));
        assertThrows(IllegalArgumentException.class,
                () -> new Marker("mk_quest_estagio", MarkerType.COUNTER, -1));
        assertThrows(IllegalArgumentException.class, () -> m.setStage(-1));
    }

    // ── RF-SS-02: Ciclo social obrigatório ───────────────────────────────────

    @Test
    void socialCycle_semTeste_sucesso() {
        // RF-SS-02
        SocialCycleResult result = SocialService.executeSocialCycle(
                50, null, buildNode(), "op1", false, 480L, buildConfiguracaoMundo());
        assertTrue(result.success(), "Sem teste social, ciclo deve ser sucesso");
    }

    @Test
    void socialCycle_comTeste_sucesso() {
        // RF-SS-02
        DialogOption opcaoComTeste = new DialogOption(
                "op2", DiscourseStyle.DIPLOMATIC, "Quero um desconto.",
                SocialTest.fixedDC("persuasao", 15),
                new DialogEffects(Map.of("npc_ferreiro", 10), Map.of(), Map.of(), "Desconto concedido!"),
                DialogEffects.empty());
        DialogNode node = new DialogNode("dlg_002", "npc_ferreiro", "E aí?", List.of(opcaoComTeste));

        SocialCycleResult result = SocialService.executeSocialCycle(
                0, null, node, "op2", true, 480L, buildConfiguracaoMundo());
        assertTrue(result.success());
        assertNotNull(result.newNpcRelationship(), "Delta de relacionamento deve ser calculado");
        assertEquals(10, result.newNpcRelationship()); // 0 + 10
    }

    @Test
    void socialCycle_comTeste_falha() {
        // RF-SS-02
        DialogOption opcaoComTeste = new DialogOption(
                "op3", DiscourseStyle.DIPLOMATIC, "Tento enganar.",
                SocialTest.fixedDC("enganacao", 15),
                DialogEffects.empty(),
                new DialogEffects(Map.of("npc_ferreiro", -5), Map.of(), Map.of(), "Falha na enganação!"));
        DialogNode node = new DialogNode("dlg_003", "npc_ferreiro", "E aí?", List.of(opcaoComTeste));

        SocialCycleResult result = SocialService.executeSocialCycle(
                0, null, node, "op3", false, 480L, buildConfiguracaoMundo());
        assertFalse(result.success());
        assertNotNull(result.newNpcRelationship());
        assertEquals(-5, result.newNpcRelationship()); // 0 + (-5)
    }

    @Test
    void socialCycle_opcaoNaoEncontrada_lancaExcecao() {
        // RF-SS-02
        assertThrows(IllegalArgumentException.class,
                () -> SocialService.executeSocialCycle(
                        null, null, buildNode(), "op_inexistente", true, 0L, buildConfiguracaoMundo()));
    }

    @Test
    void socialCycle_comDeltaRelacionamento_geraDiario() {
        // RF-SS-02
        DialogOption opcao = new DialogOption(
                "op4", DiscourseStyle.DIPLOMATIC, "Faço uma oferta generosa.",
                null,
                new DialogEffects(Map.of("npc_ferreiro", 20), Map.of(), Map.of(), "Oferta aceita!"),
                null);
        DialogNode node = new DialogNode("dlg_004", "npc_ferreiro", "Proposta?", List.of(opcao));

        SocialCycleResult result = SocialService.executeSocialCycle(
                50, null, node, "op4", true, 480L, buildConfiguracaoMundo());
        assertNotNull(result.diaryEntry(), "Deve gerar entrada no diário quando há delta");
        assertTrue(result.diaryEntry().entryId().startsWith("diary_"));
        assertTrue(result.diaryEntry().gameDate().matches("D\\d+-\\d{2}:\\d{2}"));
    }

    
    @Test
    void socialCycle_reputacaoGanhaPorAcao() {
        DialogOption opcao = new DialogOption(
                "op5", DiscourseStyle.DIPLOMATIC, "Ajudo os guardas.",
                null,
                new DialogEffects(Map.of(), Map.of("loc_cidade", 5), Map.of(), "Ajuda reconhecida."),
                null);
        DialogNode node = new DialogNode("dlg_005", "npc_guarda", "Precisamos de ajuda", List.of(opcao));

        SocialCycleResult result = SocialService.executeSocialCycle(
                null, 30, node, "op5", true, 480L, buildConfiguracaoMundo());

        assertEquals(36, result.newReputation());
        assertNotNull(result.diaryEntry());
        assertEquals(5, result.diaryEntry().impacts().localityReputationDeltas().get("loc_cidade"));
    }

    @Test
    void socialCycle_reputacaoPerdidaPorAcao() {
        DialogOption opcao = new DialogOption(
                "op6", DiscourseStyle.INTIMIDATING, "Ameaço os mercadores.",
                null,
                new DialogEffects(Map.of(), Map.of(), Map.of("faccao_mercadores", -3), "Ameaça vista por todos."),
                null);
        DialogNode node = new DialogNode("dlg_006", "npc_mercador", "Não faça isso", List.of(opcao));

        SocialCycleResult result = SocialService.executeSocialCycle(
                null, -61, node, "op6", true, 480L, buildConfiguracaoMundo());

        assertEquals(-66, result.newReputation());
        assertNotNull(result.diaryEntry());
        assertEquals(-3, result.diaryEntry().impacts().factionReputationDeltas().get("faccao_mercadores"));
    }


    @Test
    void socialCycle_sucesso_persisteHistoricoRelacionamentoEReputacao() {
        DialogOption opcao = new DialogOption(
                "op7", DiscourseStyle.DIPLOMATIC, "Negocio um acordo com os guardas.",
                SocialTest.fixedDC("persuasao", 14),
                new DialogEffects(
                        Map.of("npc_ferreiro", 8),
                        Map.of("loc_cidade", 3),
                        Map.of(),
                        "Acordo aceito."),
                DialogEffects.empty());
        DialogNode node = new DialogNode("dlg_007", "npc_ferreiro", "Qual sua proposta?", List.of(opcao));

        SocialCycleResult result = SocialService.executeSocialCycle(
                10, 20, node, "op7", true, 485L, buildConfiguracaoMundo());

        assertTrue(result.success());
        assertEquals(18, result.newNpcRelationship());
        assertEquals(24, result.newReputation());
        assertNotNull(result.diaryEntry());
        assertEquals(8, result.diaryEntry().impacts().npcRelationshipDeltas().get("npc_ferreiro"));
        assertEquals(3, result.diaryEntry().impacts().localityReputationDeltas().get("loc_cidade"));

        DiaryImpactRelationshipEntity relationshipEntity = new DiaryImpactRelationshipEntity();
        relationshipEntity.setIdDiaryEntry(7L);
        relationshipEntity.setTargetId("npc_ferreiro");
        relationshipEntity.setDelta(result.diaryEntry().impacts().npcRelationshipDeltas().get("npc_ferreiro"));

        DiaryImpactReputationEntity reputationEntity = new DiaryImpactReputationEntity();
        reputationEntity.setIdDiaryEntry(7L);
        reputationEntity.setTargetId("loc_cidade");
        reputationEntity.setDelta(result.diaryEntry().impacts().localityReputationDeltas().get("loc_cidade"));

        assertEquals(7L, relationshipEntity.getIdDiaryEntry());
        assertEquals("npc_ferreiro", relationshipEntity.getTargetId());
        assertEquals(8, relationshipEntity.getDelta());
        assertEquals(7L, reputationEntity.getIdDiaryEntry());
        assertEquals("loc_cidade", reputationEntity.getTargetId());
        assertEquals(3, reputationEntity.getDelta());
    }

    @Test
    void socialCycle_falha_persisteHistoricoComMarcador() {
        DialogOption opcaoComTeste = new DialogOption(
                "op8", DiscourseStyle.IRONIC, "Provoco o capitão.",
                SocialTest.fixedDC("atuacao", 18),
                DialogEffects.empty(),
                new DialogEffects(
                        Map.of("npc_capitao", -6),
                        Map.of(),
                        Map.of("faccao_guardas", -2),
                        "O capitão ficou ofendido."));
        DialogNode node = new DialogNode("dlg_008", "npc_capitao", "Cuidado com suas palavras", List.of(opcaoComTeste));

        SocialCycleResult result = SocialService.executeSocialCycle(
                -5, -20, node, "op8", false, 500L, buildConfiguracaoMundo());

        assertFalse(result.success());
        assertEquals(-11, result.newNpcRelationship());
        assertEquals(-22, result.newReputation());

        DiaryImpact impactWithMarker = new DiaryImpact(
                result.diaryEntry().impacts().npcRelationshipDeltas(),
                result.diaryEntry().impacts().localityReputationDeltas(),
                result.diaryEntry().impacts().factionReputationDeltas(),
                List.of("mk_guardas_alertados"));

        DiaryImpactMarkerEntity markerEntity = new DiaryImpactMarkerEntity();
        markerEntity.setIdDiaryEntry(8L);
        markerEntity.setMarkerId("mk_guardas_alertados");
        markerEntity.setOperation("set_flag");
        markerEntity.setValue(1);

        assertEquals(-6, impactWithMarker.npcRelationshipDeltas().get("npc_capitao"));
        assertEquals(-2, impactWithMarker.factionReputationDeltas().get("faccao_guardas"));
        assertEquals(List.of("mk_guardas_alertados"), impactWithMarker.alteredMarkers());
        assertEquals(8L, markerEntity.getIdDiaryEntry());
        assertEquals("mk_guardas_alertados", markerEntity.getMarkerId());
        assertEquals("set_flag", markerEntity.getOperation());
        assertEquals(1, markerEntity.getValue());
    }

// ── Helpers ───────────────────────────────────────────────────────────────

    private WorldConfig buildConfiguracaoMundo() {
        return new WorldConfig(
                "mundo_teste",
                60, 24, 360,
                List.of(new DayPhase("dia", 0, 1439)),
                List.of(new Season("unica", 1, 360)),
                0);
    }

    private DialogNode buildNode() {
        return new DialogNode("dlg_001", "npc_ferreiro", "Posso ajudá-lo?",
                List.of(buildOption("op1")));
    }

    private DialogOption buildOption(String id) {
        return new DialogOption(id, DiscourseStyle.DIPLOMATIC, "Sim, preciso de ajuda.",
                null, DialogEffects.empty(), null);
    }

    private DiaryEntry buildEntradaDiario() {
        return new DiaryEntry("diary_001", "Encontro com o Ferreiro",
                "O herói conheceu o ferreiro e recebeu uma espada.",
                "D1-08:00", "dlg_001", "op1", DiaryImpact.empty());
    }
}
