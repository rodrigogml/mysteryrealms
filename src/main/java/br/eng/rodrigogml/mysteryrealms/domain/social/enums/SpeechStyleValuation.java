package br.eng.rodrigogml.mysteryrealms.domain.social.enums;

/**
 * Valoração de estilo de fala pelo NPC/facção — RF-SS-04.
 *
 * Impacto nos testes sociais:
 * - GOSTA: +2 no resultado final.
 * - TOLERA: +0.
 * - REJEITA: -2 no resultado final.
 */
public enum SpeechStyleValuation {
    GOSTA(2),
    TOLERA(0),
    REJEITA(-2);

    private final int ajusteTeste;

    SpeechStyleValuation(int ajusteTeste) {
        this.ajusteTeste = ajusteTeste;
    }

    public int getAjusteTeste() {
        return ajusteTeste;
    }
}
