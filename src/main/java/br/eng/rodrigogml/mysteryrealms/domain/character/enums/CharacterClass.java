package br.eng.rodrigogml.mysteryrealms.domain.character.enums;

import br.eng.rodrigogml.mysteryrealms.domain.character.model.AttributeBonus;
import br.eng.rodrigogml.mysteryrealms.domain.character.model.SkillBonuses;

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
public enum CharacterClass {

    // ── Combate ───────────────────────────────────────────────────────────────

    GUERREIRO("Guerreiro",
            new AttributeBonus(3, 1, 2, 0, 0, 0, 0),
            SkillBonuses.of(Skill.INTIMIDACAO, 1)),

    CACADOR("Caçador",
            new AttributeBonus(0, 2, 1, 0, 2, 0, 0),
            // Conhecimento (Natureza) +3 omitido (não canônico); Sobrevivência +3
            SkillBonuses.of(Skill.SOBREVIVENCIA, 3)),

    DUELISTA("Duelista",
            new AttributeBonus(1, 3, 0, 0, 1, 0, 0),
            SkillBonuses.of(Skill.FURTIVIDADE, 2, Skill.SOBREVIVENCIA, 1)),

    // ── Magia ─────────────────────────────────────────────────────────────────

    MAGO("Mago",
            new AttributeBonus(0, 0, 0, 4, 0, 0, 1),
            SkillBonuses.of(Skill.CONHECIMENTO_ARCANO, 3, Skill.CONHECIMENTO_RELIQUIAS, 1)),

    ALQUIMISTA("Alquimista",
            new AttributeBonus(0, 0, 0, 3, 0, 0, 1),
            // Conhecimento (Natureza) +1 omitido (não canônico)
            SkillBonuses.of(Skill.CONHECIMENTO_ARCANO, 1, Skill.CONHECIMENTO_RELIQUIAS, 3)),

    CONJURADOR_ELEMENTAL("Conjurador Elemental",
            new AttributeBonus(0, 0, 1, 2, 1, 0, 3),
            SkillBonuses.of(Skill.CONHECIMENTO_ARCANO, 2)),

    // ── Social / Suporte ──────────────────────────────────────────────────────

    BARDO("Bardo",
            new AttributeBonus(0, 1, 0, 1, 0, 3, 1),
            SkillBonuses.of(Skill.PERSUASAO, 2, Skill.FURTIVIDADE, 1)),

    CLERIGO("Clérigo",
            new AttributeBonus(0, 0, 2, 1, 0, 1, 3),
            // Conhecimento (Natureza) +1 omitido; Sobrevivência +1
            SkillBonuses.of(Skill.CONHECIMENTO_RELIQUIAS, 1, Skill.SOBREVIVENCIA, 1)),

    SABIO("Sábio",
            new AttributeBonus(0, 0, 0, 3, 2, 0, 1),
            // Conhecimento (Natureza) +1 omitido
            SkillBonuses.of(Skill.PERSUASAO, 1, Skill.CONHECIMENTO_ARCANO, 1,
                    Skill.CONHECIMENTO_RELIQUIAS, 2)),

    // ── Furtivo / Manipulação ─────────────────────────────────────────────────

    LADRAO("Ladrão",
            new AttributeBonus(0, 2, 0, 0, 2, 0, 0),
            // Conhecimento (Natureza) +2 omitido
            SkillBonuses.of(Skill.FURTIVIDADE, 2, Skill.SOBREVIVENCIA, 2,
                    Skill.INTIMIDACAO, 1)),

    ASSASSINO("Assassino",
            new AttributeBonus(1, 2, 0, 0, 1, 0, 0),
            SkillBonuses.of(Skill.FURTIVIDADE, 3, Skill.SOBREVIVENCIA, 1,
                    Skill.INTIMIDACAO, 2)),

    ILUSIONISTA("Ilusionista",
            new AttributeBonus(0, 2, 0, 2, 1, 0, 2),
            SkillBonuses.of(Skill.PERSUASAO, 1, Skill.CONHECIMENTO_ARCANO, 2,
                    Skill.FURTIVIDADE, 3));

    private final String nome;
    private final AttributeBonus bonusAtributos;
    private final SkillBonuses bonusHabilidades;

    CharacterClass(String nome, AttributeBonus bonusAtributos, SkillBonuses bonusHabilidades) {
        this.nome = nome;
        this.bonusAtributos = bonusAtributos;
        this.bonusHabilidades = bonusHabilidades;
    }

    public String getNome() {
        return nome;
    }

    public AttributeBonus getBonusAtributos() {
        return bonusAtributos;
    }

    public SkillBonuses getBonusHabilidades() {
        return bonusHabilidades;
    }
}
