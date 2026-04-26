package br.eng.rodrigogml.mysteryrealms.domain.world.model;

/**
 * Fase do dia — RF-MN-12.
 *
 * {@code inicioMinDia} e {@code fimMinDia} são minutos a partir do início do dia (>= 0).
 */
public record DayPhase(
        String idFase,
        int inicioMinDia,
        int fimMinDia) {

    public DayPhase {
        if (idFase == null || idFase.isBlank())
            throw new IllegalArgumentException("idFase não pode ser vazio");
        if (inicioMinDia < 0)
            throw new IllegalArgumentException("inicioMinDia deve ser >= 0");
        if (fimMinDia < inicioMinDia)
            throw new IllegalArgumentException("fimMinDia deve ser >= inicioMinDia");
    }
}
