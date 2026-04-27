package br.eng.rodrigogml.mysteryrealms.domain.modifier.enums;

/**
 * Regra de empilhamento de modificadores — RF-MAR-06.
 */
public enum RegraEmpilhamento {
    /** Modificadores acumulam (somam) entre si. */
    ACUMULA,
    /** O novo modificador substitui o anterior do mesmo tipo/origem. */
    SUBSTITUI,
    /** O novo modificador invalida (cancela) o anterior. */
    INVALIDA
}
