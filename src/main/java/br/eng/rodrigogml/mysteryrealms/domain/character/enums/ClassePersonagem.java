package br.eng.rodrigogml.mysteryrealms.domain.character.enums;

import br.eng.rodrigogml.mysteryrealms.domain.character.model.BonusAtributo;
import br.eng.rodrigogml.mysteryrealms.domain.character.model.BonusHabilidades;

/**
 * Classes jogáveis, conforme RF-FP-08.
 *
 * Bônus de atributos e habilidades derivados da documentação legada (docs/legado/classes.wiki).
 *
 * Nota: o legado inclui "Conhecimento (Natureza)" (Caçador +3, Alquimista +1, Sábio +1, Ladrão +2),
 * mas essa habilidade não faz parte do catálogo canônico (RF-FP-03). Esses bônus foram omitidos
 * até que o usuário defina o mapeamento correto.
 *
 * Afinidades com tipos de armas (RF-FP-08) não constam no legado com valores numéricos;
 * serão definidas em requisito futuro.
 */
public enum ClassePersonagem {

    // ── Combate ───────────────────────────────────────────────────────────────

    GUERREIRO("Guerreiro",
            new BonusAtributo(3, 1, 2, 0, 0, 0, 0),
            BonusHabilidades.de(Habilidade.INTIMIDACAO, 1)),

    CACADOR("Caçador",
            new BonusAtributo(0, 2, 1, 0, 2, 0, 0),
            // Conhecimento (Natureza) +3 omitido (não canônico); Sobrevivência +3
            BonusHabilidades.de(Habilidade.SOBREVIVENCIA, 3)),

    DUELISTA("Duelista",
            new BonusAtributo(1, 3, 0, 0, 1, 0, 0),
            BonusHabilidades.de(Habilidade.FURTIVIDADE, 2, Habilidade.SOBREVIVENCIA, 1)),

    // ── Magia ─────────────────────────────────────────────────────────────────

    MAGO("Mago",
            new BonusAtributo(0, 0, 0, 4, 0, 0, 1),
            BonusHabilidades.de(Habilidade.CONHECIMENTO_ARCANO, 3, Habilidade.CONHECIMENTO_RELIQUIAS, 1)),

    ALQUIMISTA("Alquimista",
            new BonusAtributo(0, 0, 0, 3, 0, 0, 1),
            // Conhecimento (Natureza) +1 omitido (não canônico)
            BonusHabilidades.de(Habilidade.CONHECIMENTO_ARCANO, 1, Habilidade.CONHECIMENTO_RELIQUIAS, 3)),

    CONJURADOR_ELEMENTAL("Conjurador Elemental",
            new BonusAtributo(0, 0, 1, 2, 1, 0, 3),
            BonusHabilidades.de(Habilidade.CONHECIMENTO_ARCANO, 2)),

    // ── Social / Suporte ──────────────────────────────────────────────────────

    BARDO("Bardo",
            new BonusAtributo(0, 1, 0, 1, 0, 3, 1),
            BonusHabilidades.de(Habilidade.PERSUASAO, 2, Habilidade.FURTIVIDADE, 1)),

    CLERIGO("Clérigo",
            new BonusAtributo(0, 0, 2, 1, 0, 1, 3),
            // Conhecimento (Natureza) +1 omitido; Sobrevivência +1
            BonusHabilidades.de(Habilidade.CONHECIMENTO_RELIQUIAS, 1, Habilidade.SOBREVIVENCIA, 1)),

    SABIO("Sábio",
            new BonusAtributo(0, 0, 0, 3, 2, 0, 1),
            // Conhecimento (Natureza) +1 omitido
            BonusHabilidades.de(Habilidade.PERSUASAO, 1, Habilidade.CONHECIMENTO_ARCANO, 1,
                    Habilidade.CONHECIMENTO_RELIQUIAS, 2)),

    // ── Furtivo / Manipulação ─────────────────────────────────────────────────

    LADRAO("Ladrão",
            new BonusAtributo(0, 2, 0, 0, 2, 0, 0),
            // Conhecimento (Natureza) +2 omitido
            BonusHabilidades.de(Habilidade.FURTIVIDADE, 2, Habilidade.SOBREVIVENCIA, 2,
                    Habilidade.INTIMIDACAO, 1)),

    ASSASSINO("Assassino",
            new BonusAtributo(1, 2, 0, 0, 1, 0, 0),
            BonusHabilidades.de(Habilidade.FURTIVIDADE, 3, Habilidade.SOBREVIVENCIA, 1,
                    Habilidade.INTIMIDACAO, 2)),

    ILUSIONISTA("Ilusionista",
            new BonusAtributo(0, 2, 0, 2, 1, 0, 2),
            BonusHabilidades.de(Habilidade.PERSUASAO, 1, Habilidade.CONHECIMENTO_ARCANO, 2,
                    Habilidade.FURTIVIDADE, 3));

    private final String nome;
    private final BonusAtributo bonusAtributos;
    private final BonusHabilidades bonusHabilidades;

    ClassePersonagem(String nome, BonusAtributo bonusAtributos, BonusHabilidades bonusHabilidades) {
        this.nome = nome;
        this.bonusAtributos = bonusAtributos;
        this.bonusHabilidades = bonusHabilidades;
    }

    public String getNome() {
        return nome;
    }

    public BonusAtributo getBonusAtributos() {
        return bonusAtributos;
    }

    public BonusHabilidades getBonusHabilidades() {
        return bonusHabilidades;
    }
}
