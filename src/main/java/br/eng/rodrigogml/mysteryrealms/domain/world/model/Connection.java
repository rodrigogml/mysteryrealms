package br.eng.rodrigogml.mysteryrealms.domain.world.model;

import br.eng.rodrigogml.mysteryrealms.domain.world.enums.ConnectionClassification;

import java.util.List;

/**
 * Conexão entre zonas/ambientes — RF-MN-04.
 *
 * {@code routePenaltyPct} ∈ [0, 15].
 * {@code interruptionChancePerKmPct} ∈ [0, 100].
 * Deve ter pelo menos 1 destino em {@code prioritizedDestinations}.
 */
public record Connection(
        String connectionId,
        String originId,
        List<String> prioritizedDestinations,
        boolean bidirectional,
        ConnectionClassification classification,
        double routePenaltyPct,
        double interruptionChancePerKmPct,
        String interruptionTableId) {

    public Connection {
        if (connectionId == null || connectionId.isBlank())
            throw new IllegalArgumentException("idConexao não pode ser vazio");
        if (originId == null || originId.isBlank())
            throw new IllegalArgumentException("origemId não pode ser vazio");
        if (prioritizedDestinations == null || prioritizedDestinations.isEmpty())
            throw new IllegalArgumentException("destinosPriorizados deve ter pelo menos 1 destino");
        if (classification == null)
            throw new IllegalArgumentException("classificacao não pode ser nula");
        if (routePenaltyPct < 0 || routePenaltyPct > 15)
            throw new IllegalArgumentException("penaldadeRotaPct deve estar em [0, 15]: " + routePenaltyPct);
        if (interruptionChancePerKmPct < 0 || interruptionChancePerKmPct > 100)
            throw new IllegalArgumentException("chanceInterrupcaoKmPct deve estar em [0, 100]: " + interruptionChancePerKmPct);
        if (interruptionTableId == null || interruptionTableId.isBlank())
            throw new IllegalArgumentException("tabelaInterrupcoesId não pode ser vazio");
        prioritizedDestinations = List.copyOf(prioritizedDestinations);
    }
}
