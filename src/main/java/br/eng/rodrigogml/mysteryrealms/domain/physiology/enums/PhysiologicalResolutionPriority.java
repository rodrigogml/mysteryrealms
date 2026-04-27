package br.eng.rodrigogml.mysteryrealms.domain.physiology.enums;

/**
 * Tabela de precedência de estados fisiológicos, conforme RF-EF-06.
 *
 * Define a ordem obrigatória de resolução dentro de um tick.
 * Prioridade 1 é a mais alta.
 */
public enum PhysiologicalResolutionPriority {

    /** Colapso / Inconsciência: desmaio_fadiga, desmaio_sede, desmaio_fome. */
    COLAPSO_INCONSCIENCIA(1),

    /** Estado crítico de vida: pv_zerado, estado_critico. */
    ESTADO_CRITICO_PV(2),

    /** Estados fisiológicos graves: exaustao, sede_agravada, fome_agravada. */
    ESTADOS_FISIOLOGICOS_GRAVES(3),

    /** Aflições debilitantes: medo, paralisia, doenca_magica, etc. */
    AFLICOESCOMBATE(4),

    /** Estados fisiológicos moderados: sede, fome. */
    ESTADOS_FISIOLOGICOS_MODERADOS(5);

    private final int priority;

    PhysiologicalResolutionPriority(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }
}
