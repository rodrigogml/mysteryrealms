package br.eng.rodrigogml.mysteryrealms.domain.world.model;

import br.eng.rodrigogml.mysteryrealms.domain.world.enums.LocationType;

import java.util.regex.Pattern;

/**
 * Zone navegável — RF-MN-02.
 *
 * ID deve usar prefixo {@code zona_} e ser único globalmente.
 * {@code tipo_navegavel} é sempre {@link LocationType#ZONA}.
 */
public record Zone(
        String zoneId,
        String name,
        String localityId,
        double coordX,
        double coordY,
        boolean accessible,
        /** Texto de ambientação (opcional). */
        String description) {

    public static final LocationType TIPO_NAVEGAVEL = LocationType.ZONA;
    private static final Pattern CANONICAL_ID_PATTERN = Pattern.compile("^[a-z0-9_]+$");

    public Zone {
        if (zoneId == null || zoneId.isBlank())
            throw new IllegalArgumentException("idZona não pode ser vazio");
        if (!zoneId.startsWith("zona_"))
            throw new IllegalArgumentException("idZona deve começar com 'zona_': " + zoneId);
        if (!CANONICAL_ID_PATTERN.matcher(zoneId).matches())
            throw new IllegalArgumentException("idZona deve usar apenas letras minúsculas, números e '_': " + zoneId);
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("nome não pode ser vazio");
        if (localityId == null || localityId.isBlank())
            throw new IllegalArgumentException("idLocalidade não pode ser vazio");
        if (!localityId.startsWith("loc_"))
            throw new IllegalArgumentException("idLocalidade deve começar com 'loc_': " + localityId);
        if (!CANONICAL_ID_PATTERN.matcher(localityId).matches())
            throw new IllegalArgumentException("idLocalidade deve usar apenas letras minúsculas, números e '_': " + localityId);
        if (!Double.isFinite(coordX) || !Double.isFinite(coordY))
            throw new IllegalArgumentException("coordenadas da zona devem ser finitas");
    }
}
