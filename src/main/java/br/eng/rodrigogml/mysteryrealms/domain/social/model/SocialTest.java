package br.eng.rodrigogml.mysteryrealms.domain.social.model;

/**
 * Teste social vinculado a uma opção de diálogo — RF-SS-05.
 */
public record SocialTest(
        /** Chave canônica de atributo ou habilidade usada no teste (ex.: {@code persuasao}, {@code carisma}). */
        String atributoOuHabilidade,
        /** CD fixa do teste (0 se for teste oposto). */
        int dificuldade,
        /** Se verdadeiro, é teste oposto contra o NPC (não usa CD fixa). */
        boolean testeOpostoNpc) {

    public SocialTest {
        if (atributoOuHabilidade == null || atributoOuHabilidade.isBlank())
            throw new IllegalArgumentException("atributoOuHabilidade não pode ser vazio");
        if (!testeOpostoNpc && dificuldade < 0)
            throw new IllegalArgumentException("dificuldade deve ser >= 0 quando não for teste oposto");
    }

    /** Cria um teste contra CD fixa. */
    public static SocialTest cdFixa(String atributoOuHabilidade, int cd) {
        return new SocialTest(atributoOuHabilidade, cd, false);
    }

    /** Cria um teste oposto contra o NPC. */
    public static SocialTest testeOposto(String atributoOuHabilidade) {
        return new SocialTest(atributoOuHabilidade, 0, true);
    }
}
