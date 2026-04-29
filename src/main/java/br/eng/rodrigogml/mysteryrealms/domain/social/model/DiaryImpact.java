package br.eng.rodrigogml.mysteryrealms.domain.social.model;

import br.eng.rodrigogml.mysteryrealms.domain.social.enums.MarkerType;

import java.util.List;
import java.util.Map;

/**
 * Impactos registrados numa entrada de diario, conforme RF-SS-08.
 *
 * @author ?
 * @since 29-04-2026
 */
public record DiaryImpact(
        Map<String, Integer> npcRelationshipDeltas,
        Map<String, Integer> localityReputationDeltas,
        Map<String, Integer> factionReputationDeltas,
        List<String> alteredMarkers) {

    public DiaryImpact {
        npcRelationshipDeltas = copyAndValidateDeltas(npcRelationshipDeltas, "npc_", "relacionamento");
        localityReputationDeltas = copyAndValidateDeltas(localityReputationDeltas, "loc_", "reputacao local");
        factionReputationDeltas = copyAndValidateDeltas(factionReputationDeltas, "faccao_", "reputacao de faccao");
        alteredMarkers = copyAndValidateMarkers(alteredMarkers);
    }

    public static DiaryImpact empty() {
        return new DiaryImpact(Map.of(), Map.of(), Map.of(), List.of());
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

    private static List<String> copyAndValidateMarkers(List<String> markerIds) {
        if (markerIds == null)
            return List.of();
        for (String markerId : markerIds) {
            if (markerId == null || markerId.isBlank())
                throw new IllegalArgumentException("id de marcador alterado nao pode ser vazio");
            new Marker(markerId, MarkerType.FLAG, Boolean.TRUE);
        }
        return List.copyOf(markerIds);
    }
}
