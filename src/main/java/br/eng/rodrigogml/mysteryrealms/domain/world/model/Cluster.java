package br.eng.rodrigogml.mysteryrealms.domain.world.model;

import br.eng.rodrigogml.mysteryrealms.domain.world.enums.ClusterNavigationMode;

import java.util.List;

/**
 * Cluster de zonas/ambientes numa localidade — RF-MN-05.
 */
public record Cluster(
        String clusterId,
        String name,
        String localityId,
        List<String> points,
        List<String> connections,
        ClusterNavigationMode navigationMode) {

    public Cluster {
        if (clusterId == null || clusterId.isBlank())
            throw new IllegalArgumentException("idCluster não pode ser vazio");
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("nome não pode ser vazio");
        if (localityId == null || localityId.isBlank())
            throw new IllegalArgumentException("idLocalidade não pode ser vazio");
        if (points == null || points.isEmpty())
            throw new IllegalArgumentException("pontos deve ter pelo menos 1 elemento");
        if (connections == null)
            throw new IllegalArgumentException("conexoes não pode ser nulo");
        if (navigationMode == null)
            throw new IllegalArgumentException("modoNavegacao não pode ser nulo");
        points = List.copyOf(points);
        connections = List.copyOf(connections);
    }
}
