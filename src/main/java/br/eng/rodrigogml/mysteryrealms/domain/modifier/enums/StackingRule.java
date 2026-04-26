package br.eng.rodrigogml.mysteryrealms.domain.modifier.enums;

/**
 * Regra de empilhamento de modificadores — RF-MAR-06.
 */
public enum StackingRule {
    /** Modificadores acumulam (somam) entre si. */
    ACCUMULATES,
    /** O novo modificador substitui o anterior do mesmo tipo/origem. */
    REPLACES,
    /** O novo modificador invalida (cancela) o anterior. */
    INVALIDATES
}
