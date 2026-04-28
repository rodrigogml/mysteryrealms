package br.eng.rodrigogml.mysteryrealms.domain.combat.enums;

/**
 * Tipos de movimentação disponíveis em um turno de batalha — RF-CT-06.
 *
 * Define os limites de distância, custo de ação e penalidades associadas
 * a cada modo de deslocamento dentro do ciclo de batalha.
 *
 * @author ?
 * @since 28-04-2026
 */
public enum MovementType {

    /**
     * Movimento curto: até 3 m, gratuito, pode ser acoplado à ação de pré-turno.
     * Não consome ação principal nem aplica penalidade.
     */
    SHORT(3.0, false, false),

    /**
     * Movimento padrão: até 9 m, consome ação principal.
     * Não aplica penalidade de combate.
     */
    STANDARD(9.0, true, false),

    /**
     * Movimento estendido: até 18 m, consome ação principal e aplica
     * penalidade de {@code -2} em testes de acerto e defesa até o próximo turno.
     */
    EXTENDED(18.0, true, true);

    /** Distância máxima permitida em metros para este tipo de movimento. */
    private final double maxDistanceMeters;

    /** Indica se este tipo de movimento consome a ação principal do turno. */
    private final boolean consumesMainAction;

    /**
     * Indica se este tipo de movimento aplica penalidade de {@link #PENALTY} nos
     * testes de acerto e defesa até o início do próximo turno.
     */
    private final boolean appliesPenalty;

    /** Valor da penalidade aplicada pelo movimento estendido em testes de acerto/defesa — RF-CT-06. */
    public static final int PENALTY = -2;

    MovementType(double maxDistanceMeters, boolean consumesMainAction, boolean appliesPenalty) {
        this.maxDistanceMeters = maxDistanceMeters;
        this.consumesMainAction = consumesMainAction;
        this.appliesPenalty = appliesPenalty;
    }

    /** Distância máxima em metros para este tipo de movimento. */
    public double getMaxDistanceMeters() {
        return maxDistanceMeters;
    }

    /** {@code true} se este movimento consome a ação principal do turno. */
    public boolean isConsumesMainAction() {
        return consumesMainAction;
    }

    /**
     * {@code true} se este movimento aplica {@link #PENALTY} em testes de acerto
     * e defesa até o próximo turno.
     */
    public boolean isAppliesPenalty() {
        return appliesPenalty;
    }
}
