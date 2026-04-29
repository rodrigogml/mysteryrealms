package br.eng.rodrigogml.mysteryrealms.domain.modifier.model;

/**
 * Duracao de um modificador.
 */
public record ModifierDuration(int value, String unidade, boolean permanente) {

    public ModifierDuration {
        if (permanente) {
            if (value != 0) {
                throw new IllegalArgumentException("duracao permanente deve ter valor 0");
            }
            if (unidade != null) {
                throw new IllegalArgumentException("duracao permanente nao pode ter unidade");
            }
        } else {
            if (value < 1) {
                throw new IllegalArgumentException("duracao temporaria deve ser >= 1");
            }
            if (!"turnos".equals(unidade) && !"minutos".equals(unidade)) {
                throw new IllegalArgumentException("unidade de duracao invalida: " + unidade);
            }
        }
    }

    public static ModifierDuration ofPermanent() {
        return new ModifierDuration(0, null, true);
    }

    public static ModifierDuration turns(int turns) {
        if (turns < 1) {
            throw new IllegalArgumentException("turnos deve ser >= 1");
        }
        return new ModifierDuration(turns, "turnos", false);
    }

    public static ModifierDuration minutes(int minutes) {
        if (minutes < 1) {
            throw new IllegalArgumentException("minutos deve ser >= 1");
        }
        return new ModifierDuration(minutes, "minutos", false);
    }

    @Override
    public String toString() {
        return permanente ? "permanente" : value + " " + unidade;
    }
}
