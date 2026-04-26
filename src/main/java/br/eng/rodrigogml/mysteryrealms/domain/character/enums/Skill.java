package br.eng.rodrigogml.mysteryrealms.domain.character.enums;

/**
 * Habilidades canônicas do personagem, conforme RF-FP-03.
 */
public enum Skill {

    PERSUASAO("persuasao"),
    INTIMIDACAO("intimidacao"),
    ENGANACAO("enganacao"),
    CONHECIMENTO_ARCANO("conhecimento_arcano"),
    CONHECIMENTO_HISTORIA("conhecimento_historia"),
    CONHECIMENTO_RELIQUIAS("conhecimento_reliquias"),
    HERBALISMO("herbalismo"),
    ALQUIMIA("alquimia"),
    FURTIVIDADE("furtividade"),
    SOBREVIVENCIA("sobrevivencia"),
    MANUSEIO_ARMAS("manuseio_armas"),
    USO_MAGIA("uso_magia");

    private final String chave;

    Skill(String chave) {
        this.chave = chave;
    }

    public String getChave() {
        return chave;
    }
}
