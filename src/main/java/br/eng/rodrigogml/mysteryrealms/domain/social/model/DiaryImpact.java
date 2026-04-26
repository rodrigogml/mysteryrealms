package br.eng.rodrigogml.mysteryrealms.domain.social.model;

import java.util.List;
import java.util.Map;

/**
 * Impactos registrados numa entrada de diário — RF-SS-08.
 */
public record DiaryImpact(
        Map<String, Integer> deltasRelacionamentoNpc,
        Map<String, Integer> deltasReputacaoLocalidade,
        Map<String, Integer> deltasReputacaoFaccao,
        List<String> marcadoresAlterados) {

    public DiaryImpact {
        if (deltasRelacionamentoNpc == null)
            deltasRelacionamentoNpc = Map.of();
        if (deltasReputacaoLocalidade == null)
            deltasReputacaoLocalidade = Map.of();
        if (deltasReputacaoFaccao == null)
            deltasReputacaoFaccao = Map.of();
        if (marcadoresAlterados == null)
            marcadoresAlterados = List.of();
        deltasRelacionamentoNpc = Map.copyOf(deltasRelacionamentoNpc);
        deltasReputacaoLocalidade = Map.copyOf(deltasReputacaoLocalidade);
        deltasReputacaoFaccao = Map.copyOf(deltasReputacaoFaccao);
        marcadoresAlterados = List.copyOf(marcadoresAlterados);
    }

    public static DiaryImpact empty() {
        return new DiaryImpact(Map.of(), Map.of(), Map.of(), List.of());
    }
}
