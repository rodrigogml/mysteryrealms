package br.eng.rodrigogml.mysteryrealms.domain.physiology.enums;

/**
 * Efeitos adicionais quando estados fisiológicos coocorrem, conforme RF-EF-05.
 */
public enum EfeitoFisiologicoCombinado {

    /** Nenhuma combinação crítica ativa. */
    NENHUM,

    /**
     * {@code sede_agravada + exaustao}: -1 destreza e -10% precisão — RF-EF-05.
     */
    SEDE_EXAUSTAO,

    /**
     * {@code fome_agravada + exaustao}: -1 forca e -10% bloqueio — RF-EF-05.
     */
    FOME_EXAUSTAO,

    /**
     * {@code fome_agravada + sede_agravada}: -15 moral instantâneo — RF-EF-05.
     */
    FOME_SEDE_AGRAVADA
}
