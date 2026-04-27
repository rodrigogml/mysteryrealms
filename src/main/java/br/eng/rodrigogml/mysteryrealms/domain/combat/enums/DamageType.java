package br.eng.rodrigogml.mysteryrealms.domain.combat.enums;

/**
 * Tipos de dano canônicos, conforme RF-MAR-01.
 */
public enum DamageType {

    SLASHING("corte"),
    PIERCING("perfuracao"),
    BLUDGEONING("esmagamento"),
    FIRE("fogo"),
    ICE("gelo"),
    LIGHTNING("raio"),
    ACID("acido"),
    PURE_MAGIC("magia_pura"),
    BLEEDING("sangramento"),
    LETHAL_POISON("veneno_letal");

    private final String key;

    DamageType(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    /**
     * Compatibilidade legada: "Eletricidade" mapeia para LIGHTNING, conforme RF-MAR-01.
     */
    public static DamageType fromLegacy(String name) {
        if ("eletricidade".equalsIgnoreCase(name)) {
            return LIGHTNING;
        }
        for (DamageType dt : values()) {
            if (dt.key.equalsIgnoreCase(name)) return dt;
        }
        throw new IllegalArgumentException("Tipo de dano desconhecido: " + name);
    }
}
