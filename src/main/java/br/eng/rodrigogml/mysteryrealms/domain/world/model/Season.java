package br.eng.rodrigogml.mysteryrealms.domain.world.model;

/**
 * Estação do ano — RF-MN-12.
 *
 * {@code diaInicio} e {@code diaFim} são números de dia no ano ({@code >= 1}).
 */
public record Season(
        String idEstacao,
        int diaInicio,
        int diaFim) {

    public Season {
        if (idEstacao == null || idEstacao.isBlank())
            throw new IllegalArgumentException("idEstacao não pode ser vazio");
        if (diaInicio < 1)
            throw new IllegalArgumentException("diaInicio deve ser >= 1");
        if (diaFim < diaInicio)
            throw new IllegalArgumentException("diaFim deve ser >= diaInicio");
    }
}
