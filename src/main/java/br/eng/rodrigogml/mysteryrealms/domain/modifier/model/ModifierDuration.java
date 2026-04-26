package br.eng.rodrigogml.mysteryrealms.domain.modifier.model;

/**
 * Duração de um modificador — RF-MAR-04.
 *
 * Pode ser permanente ou ter uma duração em turnos/minutos.
 */
public record ModifierDuration(int valor, String unidade, boolean permanente) {

    /** Cria uma duração permanente. */
    public static ModifierDuration dePermanente() {
        return new ModifierDuration(0, null, true);
    }

    /** Cria uma duração em turnos. */
    public static ModifierDuration turnos(int turnos) {
        if (turnos < 1) throw new IllegalArgumentException("turnos deve ser >= 1");
        return new ModifierDuration(turnos, "turnos", false);
    }

    /** Cria uma duração em minutos. */
    public static ModifierDuration minutos(int minutos) {
        if (minutos < 1) throw new IllegalArgumentException("minutos deve ser >= 1");
        return new ModifierDuration(minutos, "minutos", false);
    }

    @Override
    public String toString() {
        return permanente ? "permanente" : valor + " " + unidade;
    }
}
