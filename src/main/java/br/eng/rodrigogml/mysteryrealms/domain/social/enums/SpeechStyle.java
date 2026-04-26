package br.eng.rodrigogml.mysteryrealms.domain.social.enums;

/**
 * Estilos de fala canônicos do sistema social, conforme RF-SS-03.
 */
public enum SpeechStyle {

    FALA_DIPLOMATICA("fala_diplomatica", "Respeito, negociação, conciliação."),
    FALA_INTIMIDADORA("fala_intimidadora", "Ameaça, imposição, domínio."),
    FALA_EMPATICA("fala_empatica", "Acolhimento, escuta, conexão emocional."),
    FALA_PRAGMATICA("fala_pragmatica", "Objetividade, troca direta, foco em resultado."),
    FALA_IRONICA("fala_ironica", "Sarcasmo, provocação, ambiguidade.");

    private final String chave;
    private final String descricao;

    SpeechStyle(String chave, String descricao) {
        this.chave = chave;
        this.descricao = descricao;
    }

    public String getChave() {
        return chave;
    }

    public String getDescricao() {
        return descricao;
    }
}
