package br.eng.rodrigogml.mysteryrealms.domain.social.model;

import java.util.List;
import java.util.Map;

/**
 * Impactos registrados numa entrada de diário — RF-SS-08.
 */
public record DiaryImpact(
        Map<String, Integer> npcRelationshipDeltas,
        Map<String, Integer> localityReputationDeltas,
        Map<String, Integer> factionReputationDeltas,
        List<String> alteredMarkers) {

    public DiaryImpact {
        if (npcRelationshipDeltas == null)
            npcRelationshipDeltas = Map.of();
        if (localityReputationDeltas == null)
            localityReputationDeltas = Map.of();
        if (factionReputationDeltas == null)
            factionReputationDeltas = Map.of();
        if (alteredMarkers == null)
            alteredMarkers = List.of();
        npcRelationshipDeltas = Map.copyOf(npcRelationshipDeltas);
        localityReputationDeltas = Map.copyOf(localityReputationDeltas);
        factionReputationDeltas = Map.copyOf(factionReputationDeltas);
        alteredMarkers = List.copyOf(alteredMarkers);
    }

    public static DiaryImpact empty() {
        return new DiaryImpact(Map.of(), Map.of(), Map.of(), List.of());
    }
}
