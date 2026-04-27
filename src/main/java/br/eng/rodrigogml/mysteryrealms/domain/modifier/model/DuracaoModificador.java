package br.eng.rodrigogml.mysteryrealms.domain.modifier.model;

/**
 * Duração de um modificador — RF-MAR-04.
 *
 * Pode ser permanente ou ter uma duração em turnos/minutos.
 */
public record DuracaoModificador(int valor, String unidade, boolean permanente) {

    /** Cria uma duração permanente. */
    public static DuracaoModificador dePermanente() {
        return new DuracaoModificador(0, null, true);
    }

    /** Cria uma duração em turnos. */
    public static DuracaoModificador turnos(int turnos) {
        if (turnos < 1) throw new IllegalArgumentException("turnos deve ser >= 1");
        return new DuracaoModificador(turnos, "turnos", false);
    }

    /** Cria uma duração em minutos. */
    public static DuracaoModificador minutos(int minutos) {
        if (minutos < 1) throw new IllegalArgumentException("minutos deve ser >= 1");
        return new DuracaoModificador(minutos, "minutos", false);
    }

    @Override
    public String toString() {
        return permanente ? "permanente" : valor + " " + unidade;
    }
}
