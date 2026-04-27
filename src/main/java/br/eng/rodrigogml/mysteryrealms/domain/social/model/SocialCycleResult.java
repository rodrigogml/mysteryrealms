package br.eng.rodrigogml.mysteryrealms.domain.social.model;

import java.util.List;

/**
 * Resultado de um ciclo social — RF-SS-02.
 */
public record SocialCycleResult(
        boolean success,
        DiaryEntry diaryEntry,
        List<Marker> alteredMarkers,
        Integer newNpcRelationship,
        Integer newReputation) {

    public SocialCycleResult {
        alteredMarkers = alteredMarkers != null ? List.copyOf(alteredMarkers) : List.of();
    }
}
