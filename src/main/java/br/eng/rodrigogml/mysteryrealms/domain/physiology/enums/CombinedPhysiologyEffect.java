package br.eng.rodrigogml.mysteryrealms.domain.physiology.enums;

/**
 * Efeitos adicionais quando estados fisiológicos coocorrem, conforme RF-EF-05.
 */
public enum CombinedPhysiologyEffect {

    /** Nenhuma combinação crítica ativa. */
    NONE,

    /**
     * {@code sede_agravada + exaustao}: -1 dexterity e -10% precisão — RF-EF-05.
     */
    THIRST_EXHAUSTION,

    /**
     * {@code fome_agravada + exaustao}: -1 strength e -10% bloqueio — RF-EF-05.
     */
    HUNGER_EXHAUSTION,

    /**
     * {@code fome_agravada + sede_agravada}: -15 morale instantâneo — RF-EF-05.
     */
    SEVERE_HUNGER_THIRST
}
