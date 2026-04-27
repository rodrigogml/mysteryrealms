package br.eng.rodrigogml.mysteryrealms.domain.social.enums;

/**
 * Valoração de estilo de fala pelo NPC/facção — RF-SS-04.
 *
 * Impacto nos testes sociais:
 * - LIKES: +2 no resultado final.
 * - TOLERATES: +0.
 * - REJECTS: -2 no resultado final.
 */
public enum DiscourseStyleEvaluation {
    LIKES(2),
    TOLERATES(0),
    REJECTS(-2);

    private final int testAdjustment;

    DiscourseStyleEvaluation(int testAdjustment) {
        this.testAdjustment = testAdjustment;
    }

    public int getTestAdjustment() {
        return testAdjustment;
    }
}
