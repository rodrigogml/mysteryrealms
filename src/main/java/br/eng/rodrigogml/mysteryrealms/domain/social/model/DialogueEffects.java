package br.eng.rodrigogml.mysteryrealms.domain.social.model;

import br.eng.rodrigogml.mysteryrealms.domain.social.enums.SpeechStyle;

import java.util.Map;

/**
 * Efeitos de sucesso ou falha de uma opção de diálogo.
 *
 * Armazena deltas de relacionamento/reputação e marcadores a serem alterados.
 */
public record DialogueEffects(
        /** Deltas de relacionamento com NPCs: npc_id → delta. */
        Map<String, Integer> deltasRelacionamentoNpc,
        /** Deltas de reputação por localidade: loc_id → delta. */
        Map<String, Integer> deltasReputacaoLocalidade,
        /** Deltas de reputação por facção: faccao_id → delta. */
        Map<String, Integer> deltasReputacaoFaccao,
        /** Texto narrativo exibível ao jogador. */
        String textoNarrativo) {

    public static DialogueEffects empty() {
        return new DialogueEffects(Map.of(), Map.of(), Map.of(), "");
    }
}
