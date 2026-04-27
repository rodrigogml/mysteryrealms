package br.eng.rodrigogml.mysteryrealms.domain.social.enums;

/**
 * Estilos de fala canônicos do sistema social, conforme RF-SS-03.
 */
public enum DiscourseStyle {

    DIPLOMATIC("fala_diplomatica", "Respeito, negociação, conciliação."),
    INTIMIDATING("fala_intimidadora", "Ameaça, imposição, domínio."),
    EMPATHIC("fala_empatica", "Acolhimento, escuta, conexão emocional."),
    PRAGMATIC("fala_pragmatica", "Objetividade, troca direta, foco em resultado."),
    IRONIC("fala_ironica", "Sarcasmo, provocação, ambiguidade.");

    private final String key;
    private final String description;

    DiscourseStyle(String key, String description) {
        this.key = key;
        this.description = description;
    }

    public String getKey() {
        return key;
    }

    public String getDescription() {
        return description;
    }
}
