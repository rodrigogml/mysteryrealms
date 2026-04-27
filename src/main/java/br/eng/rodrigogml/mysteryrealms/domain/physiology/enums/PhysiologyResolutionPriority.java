package br.eng.rodrigogml.mysteryrealms.domain.physiology.enums;

/**
 * Tabela de precedência de estados fisiológicos, conforme RF-EF-06.
 *
 * Define a ordem obrigatória de resolução dentro de um tick.
 * Prioridade 1 é a mais alta.
 */
public enum PhysiologyResolutionPriority {

    /** Colapso / Inconsciência: desmaio_fadiga, desmaio_sede, desmaio_fome. */
    COLLAPSE_UNCONSCIOUSNESS(1),

    /** Estado crítico de vida: pv_zerado, estado_critico. */
    CRITICAL_HP_STATE(2),

    /** Estados fisiológicos graves: exaustao, sede_agravada, fome_agravada. */
    SEVERE_PHYSIOLOGY_STATES(3),

    /** Aflições debilitantes: medo, paralisia, doenca_magica, etc. */
    COMBAT_AFFLICTIONS(4),

    /** Estados fisiológicos moderados: sede, fome. */
    MODERATE_PHYSIOLOGY_STATES(5);

    private final int priority;

    PhysiologyResolutionPriority(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }
}
