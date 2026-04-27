package br.eng.rodrigogml.mysteryrealms.domain.social.model;

import br.eng.rodrigogml.mysteryrealms.domain.social.enums.DiscourseStyle;

import java.util.Map;

/**
 * Efeitos de success ou falha de uma opção de diálogo.
 *
 * Armazena deltas de relacionamento/reputação e marcadores a serem alterados.
 */
public record DialogEffects(
        /** Deltas de relacionamento com NPCs: npc_id → delta. */
        Map<String, Integer> npcRelationshipDeltas,
        /** Deltas de reputação por localidade: loc_id → delta. */
        Map<String, Integer> localityReputationDeltas,
        /** Deltas de reputação por facção: faccao_id → delta. */
        Map<String, Integer> factionReputationDeltas,
        /** Texto narrativo exibível ao jogador. */
        String narrativeText) {

    public static DialogEffects empty() {
        return new DialogEffects(Map.of(), Map.of(), Map.of(), "");
    }
}
