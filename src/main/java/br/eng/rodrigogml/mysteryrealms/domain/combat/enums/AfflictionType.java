package br.eng.rodrigogml.mysteryrealms.domain.combat.enums;

/**
 * Tipos de aflição canônicos, conforme RF-MAR-02.
 */
public enum AfflictionType {

    PSYCHIC("psiquica"),
    SPIRITUAL("espiritual"),
    FEAR("medo"),
    PARALYSIS("paralisia"),
    BLINDNESS("cegueira"),
    DEAFNESS_MUTENESS("surdez_mudez"),
    FATIGUE("fadiga"),
    MAGICAL_DISEASE("doenca_magica"),
    PERSISTENT_HALLUCINATION("alucinacao_ilusao_persistente"),
    SLEEP_TORPOR("sono_torpor");

    private final String key;

    AfflictionType(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
