package br.eng.rodrigogml.mysteryrealms.domain.social.model;

import java.util.Map;

/**
 * Efeitos de sucesso ou falha de uma opcao de dialogo.
 *
 * Armazena deltas de relacionamento/reputacao e marcadores a serem alterados.
 *
 * @author ?
 * @since 29-04-2026
 */
public record DialogEffects(
        /** Deltas de relacionamento com NPCs: npc_id -> delta. */
        Map<String, Integer> npcRelationshipDeltas,
        /** Deltas de reputacao por localidade: loc_id -> delta. */
        Map<String, Integer> localityReputationDeltas,
        /** Deltas de reputacao por faccao: faccao_id -> delta. */
        Map<String, Integer> factionReputationDeltas,
        /** Texto narrativo exibivel ao jogador. */
        String narrativeText) {

    public DialogEffects {
        npcRelationshipDeltas = copyAndValidateDeltas(npcRelationshipDeltas, "npc_", "relacionamento");
        localityReputationDeltas = copyAndValidateDeltas(localityReputationDeltas, "loc_", "reputacao local");
        factionReputationDeltas = copyAndValidateDeltas(factionReputationDeltas, "faccao_", "reputacao de faccao");
        if (narrativeText == null)
            narrativeText = "";
    }

    public static DialogEffects empty() {
        return new DialogEffects(Map.of(), Map.of(), Map.of(), "");
    }

    private static Map<String, Integer> copyAndValidateDeltas(Map<String, Integer> deltas, String prefix, String label) {
        if (deltas == null)
            return Map.of();
        for (Map.Entry<String, Integer> entry : deltas.entrySet()) {
            String id = entry.getKey();
            if (id == null || id.isBlank())
                throw new IllegalArgumentException("alvo de " + label + " nao pode ser vazio");
            if (!id.startsWith(prefix))
                throw new IllegalArgumentException("alvo de " + label + " deve comecar com '" + prefix + "': " + id);
            if (entry.getValue() == null)
                throw new IllegalArgumentException("delta de " + label + " nao pode ser nulo: " + id);
        }
        return Map.copyOf(deltas);
    }
}
