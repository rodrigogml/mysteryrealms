package br.eng.rodrigogml.mysteryrealms.domain.social.enums;

/**
 * Tipos de marcador de progresso social — RF-SS-09.
 */
public enum TipoMarcador {
    /** Fato ocorreu / não ocorreu (boolean). */
    SINALIZADOR,
    /** Estágio de progresso (inteiro, ex.: 0..3). */
    ESTAGIO,
    /** Contagem de recorrência (inteiro). */
    CONTADOR
}
