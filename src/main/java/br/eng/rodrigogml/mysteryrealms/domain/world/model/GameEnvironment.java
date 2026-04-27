package br.eng.rodrigogml.mysteryrealms.domain.world.model;

import br.eng.rodrigogml.mysteryrealms.domain.world.enums.LocationType;

/**
 * Ambiente navegável — RF-MN-03.
 *
 * ID deve usar prefixo {@code amb_} e ser único globalmente.
 * {@code tipo_navegavel} é sempre {@link LocationType#ENVIRONMENT}.
 */
public record GameEnvironment(
        String environmentId,
        String name,
        String zoneId,
        double coordX,
        double coordY,
        boolean accessible,
        /** Texto de ambientação (opcional). */
        String description) {

    public static final LocationType TIPO_NAVEGAVEL = LocationType.ENVIRONMENT;

    public GameEnvironment {
        if (environmentId == null || environmentId.isBlank())
            throw new IllegalArgumentException("idAmbiente não pode ser vazio");
        if (!environmentId.startsWith("amb_"))
            throw new IllegalArgumentException("idAmbiente deve começar com 'amb_': " + environmentId);
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("nome não pode ser vazio");
        if (zoneId == null || zoneId.isBlank())
            throw new IllegalArgumentException("idZona não pode ser vazio");
    }
}
