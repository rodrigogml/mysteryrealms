package br.eng.rodrigogml.mysteryrealms.domain.combat.enums;

/**
 * Tipos de aflição canônicos, conforme RF-MAR-02.
 */
public enum AfflictionType {

    PSIQUICA("psiquica"),
    ESPIRITUAL("espiritual"),
    MEDO("medo"),
    PARALISIA("paralisia"),
    CEGUEIRA("cegueira"),
    SURDEZ_MUDEZ("surdez_mudez"),
    FADIGA("fadiga"),
    DOENCA_MAGICA("doenca_magica"),
    ALUCINACAO_ILUSAO_PERSISTENTE("alucinacao_ilusao_persistente"),
    SONO_TORPOR("sono_torpor");

    private final String chave;

    AfflictionType(String chave) {
        this.chave = chave;
    }

    public String getChave() {
        return chave;
    }
}
