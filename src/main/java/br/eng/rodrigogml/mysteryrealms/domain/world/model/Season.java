package br.eng.rodrigogml.mysteryrealms.domain.world.model;

/**
 * Estação do ano — RF-MN-12.
 *
 * {@code startDay} e {@code endDay} são números de dia no ano ({@code >= 1}).
 */
public record Season(
        String seasonId,
        int startDay,
        int endDay) {

    public Season {
        if (seasonId == null || seasonId.isBlank())
            throw new IllegalArgumentException("idEstacao não pode ser vazio");
        if (startDay < 1)
            throw new IllegalArgumentException("diaInicio deve ser >= 1");
        if (endDay < startDay)
            throw new IllegalArgumentException("diaFim deve ser >= diaInicio");
    }
}
