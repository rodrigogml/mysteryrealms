package br.eng.rodrigogml.mysteryrealms.domain.world.model;

/**
 * Fase do dia — RF-MN-12.
 *
 * {@code startMinOfDay} e {@code endMinOfDay} são minutes a partir do início do dia (>= 0).
 */
public record DayPhase(
        String phaseId,
        int startMinOfDay,
        int endMinOfDay) {

    public DayPhase {
        if (phaseId == null || phaseId.isBlank())
            throw new IllegalArgumentException("idFase não pode ser vazio");
        if (startMinOfDay < 0)
            throw new IllegalArgumentException("inicioMinDia deve ser >= 0");
        if (endMinOfDay < startMinOfDay)
            throw new IllegalArgumentException("fimMinDia deve ser >= inicioMinDia");
    }
}
