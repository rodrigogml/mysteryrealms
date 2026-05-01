package br.eng.rodrigogml.mysteryrealms.domain.social.service;

import br.eng.rodrigogml.mysteryrealms.common.exception.ValidationException;
import br.eng.rodrigogml.mysteryrealms.domain.social.enums.RelationshipRange;
import br.eng.rodrigogml.mysteryrealms.domain.social.enums.RelationshipState;
import br.eng.rodrigogml.mysteryrealms.domain.social.enums.ReputationRange;
import br.eng.rodrigogml.mysteryrealms.domain.social.enums.DiscourseStyleEvaluation;
import br.eng.rodrigogml.mysteryrealms.domain.social.model.DiaryEntry;
import br.eng.rodrigogml.mysteryrealms.domain.social.model.DiaryImpact;
import br.eng.rodrigogml.mysteryrealms.domain.social.model.DialogEffects;
import br.eng.rodrigogml.mysteryrealms.domain.social.model.DialogNode;
import br.eng.rodrigogml.mysteryrealms.domain.social.model.DialogOption;
import br.eng.rodrigogml.mysteryrealms.domain.social.model.Marker;
import br.eng.rodrigogml.mysteryrealms.domain.social.model.SocialCycleResult;
import br.eng.rodrigogml.mysteryrealms.domain.world.model.WorldConfig;
import br.eng.rodrigogml.mysteryrealms.domain.world.service.WorldTimeService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Serviço do sistema social — RF-SS-02 a RF-SS-07.
 *
 * Implementa relacionamentos, reputação, testes sociais e ajustes de estilo de fala.
 * Stateless: todos os métodos são estáticos.
 */
public final class SocialService {

    /** Contador monotônico para IDs de diário gerados automaticamente. */
    private static final AtomicLong DIARY_COUNTER = new AtomicLong(1);

    private SocialService() {}

    // ── RF-SS-05: Teste social ────────────────────────────────────────────────

    /**
     * Aplica o ajuste de estilo de fala ao resultado do teste — RF-SS-04 / RF-SS-05.
     *
     * @param testResult resultado base do teste (valor_base + 1d20 + modificadores)
     * @param valoracao      valoração do NPC para o estilo de fala escolhido
     * @return resultado ajustado (+2, 0 ou -2)
     */
    public static int adjustByTalkStyle(int testResult, DiscourseStyleEvaluation valoracao) {
        if (valoracao == null) return testResult;
        return testResult + valoracao.getTestAdjustment();
    }

    // ── RF-SS-06: Relacionamento com NPC ─────────────────────────────────────

    /**
     * Aplica delta de relacionamento com NPC, clampado em [-100, 100] — RF-SS-06.
     *
     * @param valorAtual value atual do relacionamento
     * @param delta      incremento (pode ser negativo)
     * @return novo value clampado
     */
    public static int applyRelationshipDelta(int valorAtual, int delta) {
        return Math.max(-100, Math.min(100, valorAtual + delta));
    }

    /**
     * Retorna a faixa canônica de relacionamento — RF-SS-06.
     */
    public static RelationshipRange relationshipRange(int value) {
        return RelationshipRange.of(value);
    }

    // ── RF-SS-07: Reputação por localidade/facção ─────────────────────────────

    /**
     * Aplica delta de reputação (sem limite fixed de clamp) — RF-SS-07.
     *
     * @param valorAtual value atual de reputação
     * @param delta      incremento (pode ser negativo)
     * @return novo value
     */
    public static int applyReputationDelta(int valorAtual, int delta) {
        int weightedDelta = computeReputationImpact(valorAtual, delta);
        return valorAtual + weightedDelta;
    }

    /**
     * Determina a faixa de reputação para um valor consolidado.
     */
    public static ReputationRange reputationRange(int value) {
        return ReputationRange.of(value);
    }

    /**
     * Converte score num estado semântico de relacionamento.
     */
    public static RelationshipState relationshipState(int value) {
        return RelationshipState.fromScore(value);
    }

    /**
     * Calcula impacto final do delta conforme a faixa atual de reputação.
     *
     * Ações positivas escalam melhor para personagens bem reputados,
     * enquanto ações negativas punem mais personagens infames.
     */
    public static int computeReputationImpact(int valorAtual, int delta) {
        if (delta == 0) {
            return 0;
        }
        ReputationRange currentRange = ReputationRange.of(valorAtual);
        int adjustment = currentRange.impactBonus();
        if (delta > 0) {
            return delta + adjustment;
        }
        return delta - adjustment;
    }

    // ── RF-SS-02: Ciclo social obrigatório ───────────────────────────────────

    /**
     * Executa o ciclo social completo para uma opção de diálogo — RF-SS-02.
     *
     * Ordem obrigatória:
     * escolha → teste social (quando aplicável) → atualização de relacionamento/reputação
     * → registro no diário → atualização de marcadores.
     *
     * @param npcRelacionamentoAtual  value atual do relacionamento com o NPC alvo (pode ser null)
     * @param reputacaoAtual          value atual de reputação no alvo (pode ser null)
     * @param node                    nó de diálogo
     * @param optionId                 ID da opção escolhida pelo jogador
     * @param testePassed             resultado externo do teste social (dado já rolado fora)
     * @param tempoAtualMin           tempo atual em minutes (para o campo gameDate do diário)
     * @param worldConfig             configuração do mundo (para formatar a data)
     * @return resultado completo do ciclo social
     * @throws IllegalArgumentException se optionId não for encontrado no nó
     */
    public static SocialCycleResult executeSocialCycle(
            Integer npcRelacionamentoAtual,
            Integer reputacaoAtual,
            DialogNode node,
            String optionId,
            boolean testePassed,
            long tempoAtualMin,
            WorldConfig worldConfig) {

        // 1. Localizar a opção escolhida
        DialogOption opcao = node.options().stream()
                .filter(o -> o.optionId().equals(optionId))
                .findFirst()
                .orElseThrow(() -> new ValidationException(
                        "opcaoId '" + optionId + "' não encontrado no nó '" + node.dialogId() + "'"));

        // 2. Determinar success: se não há teste, success automático
        boolean success = (opcao.socialTest() == null) || testePassed;

        // 3. Selecionar os efeitos aplicáveis
        DialogEffects efeitos = success ? opcao.successEffects() : opcao.failureEffects();

        // 4. Aplicar deltas de relacionamento e reputação
        Integer novoRelacionamento = null;
        Integer newReputation = null;
        Map<String, Integer> deltasNpc = efeitos.npcRelationshipDeltas();
        Map<String, Integer> deltasLoc = efeitos.localityReputationDeltas();
        Map<String, Integer> deltasFac = efeitos.factionReputationDeltas();

        // Relacionamento com NPC principal do nó
        if (!deltasNpc.isEmpty() && npcRelacionamentoAtual != null) {
            int deltaTotal = deltasNpc.values().stream().mapToInt(Integer::intValue).sum();
            novoRelacionamento = applyRelationshipDelta(npcRelacionamentoAtual, deltaTotal);
        } else if (!deltasNpc.isEmpty()) {
            int deltaTotal = deltasNpc.values().stream().mapToInt(Integer::intValue).sum();
            novoRelacionamento = applyRelationshipDelta(0, deltaTotal);
        }

        // Reputação (localidade ou facção, primeiro encontrado)
        if (!deltasLoc.isEmpty() && reputacaoAtual != null) {
            int deltaTotal = deltasLoc.values().stream().mapToInt(Integer::intValue).sum();
            newReputation = applyReputationDelta(reputacaoAtual, deltaTotal);
        } else if (!deltasFac.isEmpty() && reputacaoAtual != null) {
            int deltaTotal = deltasFac.values().stream().mapToInt(Integer::intValue).sum();
            newReputation = applyReputationDelta(reputacaoAtual, deltaTotal);
        }

        // 5. Verificar se deve registrar no diário
        boolean temImpacto = novoRelacionamento != null
                || newReputation != null
                || !deltasNpc.isEmpty()
                || !deltasLoc.isEmpty()
                || !deltasFac.isEmpty();

        DiaryEntry diaryEntry = null;
        if (temImpacto) {
            String gameDate = formatGameDate(tempoAtualMin, worldConfig);
            String entryId = "diary_" + node.dialogId() + "_" + optionId + "_" + DIARY_COUNTER.getAndIncrement();
            String title = summarizeTitle(opcao.optionText());
            String summary = success ? "Ação bem-sucedida com " + node.npcId() + "."
                    : "Ação falhou com " + node.npcId() + ".";

            // Consolidar todos os deltas para o impacto do diário
            Map<String, Integer> todosNpc = new HashMap<>(deltasNpc);
            Map<String, Integer> todosLoc = new HashMap<>(deltasLoc);
            Map<String, Integer> todosFac = new HashMap<>(deltasFac);
            List<String> marcadoresIds = new ArrayList<>();

            DiaryImpact impacts = new DiaryImpact(todosNpc, todosLoc, todosFac, marcadoresIds);

            diaryEntry = new DiaryEntry(
                    entryId, title, summary, gameDate,
                    node.dialogId(), optionId, impacts);
        }

        // 6. Marcadores (sem implementação de marcadores dinâmicos neste ciclo — RF-SS-09 é gerenciado externamente)
        List<Marker> alteredMarkers = List.of();

        return new SocialCycleResult(success, diaryEntry, alteredMarkers, novoRelacionamento, newReputation);
    }

    /**
     * Formata o tempo do mundo no formato {@code D<n>-HH:MM} — RF-SS-08.
     */
    private static String formatGameDate(long tempoAtualMin, WorldConfig worldConfig) {
        long dia = WorldTimeService.dayOfYear(tempoAtualMin, worldConfig);
        int hora = WorldTimeService.hourOfDay(tempoAtualMin, worldConfig);
        int min = WorldTimeService.minuteOfHour(tempoAtualMin, worldConfig);
        return String.format("D%d-%02d:%02d", dia, hora, min);
    }

    /**
     * Gera um título de no máximo 8 palavras a partir do text da opção — RF-SS-08.
     */
    private static String summarizeTitle(String optionText) {
        String[] palavras = optionText.trim().split("\\s+");
        if (palavras.length <= 8) return optionText.trim();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            if (i > 0) sb.append(' ');
            sb.append(palavras[i]);
        }
        return sb.toString();
    }
}
