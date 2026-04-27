package br.eng.rodrigogml.mysteryrealms.domain.modifier.model;

/**
 * Duração de um modificador — RF-MAR-04.
 *
 * Pode ser permanente ou ter uma duração em turns/minutes.
 */
public record ModifierDuration(int value, String unidade, boolean permanente) {

    /** Cria uma duração permanente. */
    public static ModifierDuration ofPermanent() {
        return new ModifierDuration(0, null, true);
    }

    /** Cria uma duração em turns. */
    public static ModifierDuration turns(int turns) {
        if (turns < 1) throw new IllegalArgumentException("turnos deve ser >= 1");
        return new ModifierDuration(turns, "turnos", false);
    }

    /** Cria uma duração em minutes. */
    public static ModifierDuration minutes(int minutes) {
        if (minutes < 1) throw new IllegalArgumentException("minutos deve ser >= 1");
        return new ModifierDuration(minutes, "minutos", false);
    }

    @Override
    public String toString() {
        return permanente ? "permanente" : value + " " + unidade;
    }
}
