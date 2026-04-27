package br.eng.rodrigogml.mysteryrealms.domain.character.enums;

/**
 * Habilidades canônicas do personagem, conforme RF-FP-03.
 */
public enum Skill {

    PERSUASION("persuasao"),
    INTIMIDATION("intimidacao"),
    DECEPTION("enganacao"),
    ARCANE_KNOWLEDGE("conhecimento_arcano"),
    HISTORY_KNOWLEDGE("conhecimento_historia"),
    RELIC_KNOWLEDGE("conhecimento_reliquias"),
    HERBALISM("herbalismo"),
    ALCHEMY("alquimia"),
    STEALTH("furtividade"),
    SURVIVAL("sobrevivencia"),
    WEAPON_HANDLING("manuseio_armas"),
    MAGIC_USE("uso_magia");

    private final String key;

    Skill(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
