package br.eng.rodrigogml.mysteryrealms.domain.combat.enums;

/**
 * Tipos de dano canônicos, conforme RF-MAR-01.
 */
public enum DamageType {

    CORTE("corte"),
    PERFURACAO("perfuracao"),
    ESMAGAMENTO("esmagamento"),
    FOGO("fogo"),
    GELO("gelo"),
    RAIO("raio"),
    ACIDO("acido"),
    MAGIA_PURA("magia_pura"),
    SANGRAMENTO("sangramento"),
    VENENO_LETAL("veneno_letal");

    private final String chave;

    DamageType(String chave) {
        this.chave = chave;
    }

    public String getChave() {
        return chave;
    }

    /**
     * Compatibilidade legada: "Eletricidade" mapeia para RAIO, conforme RF-MAR-01.
     */
    public static DamageType fromLegacy(String nome) {
        if ("eletricidade".equalsIgnoreCase(nome)) {
            return RAIO;
        }
        for (DamageType dt : values()) {
            if (dt.chave.equalsIgnoreCase(nome)) return dt;
        }
        throw new IllegalArgumentException("Tipo de dano desconhecido: " + nome);
    }
}
