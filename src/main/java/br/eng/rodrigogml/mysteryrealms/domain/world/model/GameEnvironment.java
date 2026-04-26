package br.eng.rodrigogml.mysteryrealms.domain.world.model;

import br.eng.rodrigogml.mysteryrealms.domain.world.enums.LocationType;

/**
 * Ambiente navegável — RF-MN-03.
 *
 * ID deve usar prefixo {@code amb_} e ser único globalmente.
 * {@code tipo_navegavel} é sempre {@link LocationType#AMBIENTE}.
 */
public record GameEnvironment(
        String idAmbiente,
        String nome,
        String idZona,
        double coordX,
        double coordY,
        boolean acessivel,
        /** Texto de ambientação (opcional). */
        String descricao) {

    public static final LocationType TIPO_NAVEGAVEL = LocationType.AMBIENTE;

    public GameEnvironment {
        if (idAmbiente == null || idAmbiente.isBlank())
            throw new IllegalArgumentException("idAmbiente não pode ser vazio");
        if (!idAmbiente.startsWith("amb_"))
            throw new IllegalArgumentException("idAmbiente deve começar com 'amb_': " + idAmbiente);
        if (nome == null || nome.isBlank())
            throw new IllegalArgumentException("nome não pode ser vazio");
        if (idZona == null || idZona.isBlank())
            throw new IllegalArgumentException("idZona não pode ser vazio");
    }
}
