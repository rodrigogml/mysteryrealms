package br.eng.rodrigogml.mysteryrealms.domain.combat.enums;

/**
 * Tipos de resistência canônicos, conforme RF-MAR-03.
 */
public enum ResistanceType {

    SLASHING("corte"),
    PIERCING("perfuracao"),
    BLUDGEONING("esmagamento"),
    FIRE("fogo"),
    ICE("gelo"),
    LIGHTNING("raio"),
    ACID("acido"),
    PURE_MAGIC("magia_pura"),
    ENCHANTMENT("encantamento"),
    POISON("veneno"),
    DISEASE("doenca"),
    BLEEDING("sangramento"),
    FATIGUE("fadiga"),
    PAIN("dor"),
    SOUND("som"),
    CONFUSION("confusao"),
    ILLUSION("ilusao"),
    MIND_CONTROL("controle_mental"),
    SPIRITUAL_CORRUPTION("corrupcao_espiritual");

    private final String key;

    ResistanceType(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
