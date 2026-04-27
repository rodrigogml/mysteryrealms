package br.eng.rodrigogml.mysteryrealms.domain.social.model;

/**
 * Teste social vinculado a uma opção de diálogo — RF-SS-05.
 */
public record SocialTest(
        /** Chave canônica de atributo ou habilidade usada no teste (ex.: {@code persuasao}, {@code charisma}). */
        String attributeOrSkill,
        /** CD fixa do teste (0 se for teste oposto). */
        int difficulty,
        /** Se verdadeiro, é teste oposto contra o NPC (não usa CD fixa). */
        boolean opposedTestVsNpc) {

    public SocialTest {
        if (attributeOrSkill == null || attributeOrSkill.isBlank())
            throw new IllegalArgumentException("atributoOuHabilidade não pode ser vazio");
        if (!opposedTestVsNpc && difficulty < 0)
            throw new IllegalArgumentException("dificuldade deve ser >= 0 quando não for teste oposto");
    }

    /** Cria um teste contra CD fixa. */
    public static SocialTest fixedDC(String attributeOrSkill, int cd) {
        return new SocialTest(attributeOrSkill, cd, false);
    }

    /** Cria um teste oposto contra o NPC. */
    public static SocialTest opposedTest(String attributeOrSkill) {
        return new SocialTest(attributeOrSkill, 0, true);
    }
}
