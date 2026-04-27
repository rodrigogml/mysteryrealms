package br.eng.rodrigogml.mysteryrealms.domain.modifier.enums;

/**
 * Regra de empilhamento de modificadores — RF-MAR-06.
 */
public enum StackingRule {
    /** Modificadores acumulam (somam) entre si. */
    ACCUMULATE,
    /** O novo modificador substitui o anterior do mesmo type/origin. */
    REPLACE,
    /** O novo modificador invalida (cancela) o anterior. */
    INVALIDATE
}
