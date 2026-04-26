package br.eng.rodrigogml.mysteryrealms.domain.combat.enums;

/**
 * Tipos de resistência canônicos, conforme RF-MAR-03.
 */
public enum ResistanceType {

    CORTE("corte"),
    PERFURACAO("perfuracao"),
    ESMAGAMENTO("esmagamento"),
    FOGO("fogo"),
    GELO("gelo"),
    RAIO("raio"),
    ACIDO("acido"),
    MAGIA_PURA("magia_pura"),
    ENCANTAMENTO("encantamento"),
    VENENO("veneno"),
    DOENCA("doenca"),
    SANGRAMENTO("sangramento"),
    FADIGA("fadiga"),
    DOR("dor"),
    SOM("som"),
    CONFUSAO("confusao"),
    ILUSAO("ilusao"),
    CONTROLE_MENTAL("controle_mental"),
    CORRUPCAO_ESPIRITUAL("corrupcao_espiritual");

    private final String chave;

    ResistanceType(String chave) {
        this.chave = chave;
    }

    public String getChave() {
        return chave;
    }
}
