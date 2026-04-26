package br.eng.rodrigogml.mysteryrealms.domain.world.model;

import br.eng.rodrigogml.mysteryrealms.domain.world.enums.ClusterNavigationMode;

import java.util.List;

/**
 * Cluster de zonas/ambientes numa localidade — RF-MN-05.
 */
public record Cluster(
        String idCluster,
        String nome,
        String idLocalidade,
        List<String> pontos,
        List<String> conexoes,
        ClusterNavigationMode modoNavegacao) {

    public Cluster {
        if (idCluster == null || idCluster.isBlank())
            throw new IllegalArgumentException("idCluster não pode ser vazio");
        if (nome == null || nome.isBlank())
            throw new IllegalArgumentException("nome não pode ser vazio");
        if (idLocalidade == null || idLocalidade.isBlank())
            throw new IllegalArgumentException("idLocalidade não pode ser vazio");
        if (pontos == null || pontos.isEmpty())
            throw new IllegalArgumentException("pontos deve ter pelo menos 1 elemento");
        if (conexoes == null)
            throw new IllegalArgumentException("conexoes não pode ser nulo");
        if (modoNavegacao == null)
            throw new IllegalArgumentException("modoNavegacao não pode ser nulo");
        pontos = List.copyOf(pontos);
        conexoes = List.copyOf(conexoes);
    }
}
