package br.eng.rodrigogml.mysteryrealms.domain.character.enums;

import br.eng.rodrigogml.mysteryrealms.domain.character.model.AttributeSet;
import br.eng.rodrigogml.mysteryrealms.domain.character.model.SkillBonuses;

/**
 * Raças jogáveis, conforme RF-FP-07.
 *
 * Atributos base e bônus de habilidades derivados da documentação legada (docs/legado/racas.wiki).
 *
 * Nota: O legado registra total=23 para Elfo, mas a soma das colunas resulta em 21.
 * Os valores das células foram preservados (fonte de verdade da tabela), não o total.
 *
 * "Conhecimento (Natureza)" existe no legado de raças apenas como referência textual;
 * não há bônus numérico de raça para essa habilidade na tabela. A habilidade canônica
 * mais próxima, Sobrevivência, é usada onde aplicável.
 */
public enum Race {

    HUMAN("Humano",
            new AttributeSet(3, 3, 3, 3, 3, 3, 3),
            new int[]{75, 65, 70},
            SkillBonuses.empty()),

    ELF("Elfo",
            new AttributeSet(2, 4, 2, 4, 4, 2, 3),
            new int[]{60, 50, 55},
            SkillBonuses.of(Skill.ARCANE_KNOWLEDGE, 2)),

    HALF_ELF("Meio-elfo",
            new AttributeSet(2, 3, 3, 3, 3, 4, 3),
            new int[]{67, 57, 62},
            SkillBonuses.of(Skill.PERSUASION, 1)),

    DWARF("Anão",
            new AttributeSet(3, 2, 5, 2, 3, 2, 3),
            new int[]{85, 75, 80},
            SkillBonuses.of(Skill.SURVIVAL, 2)),

    HALF_ORC("Meio-orc",
            new AttributeSet(5, 3, 4, 1, 2, 1, 2),
            new int[]{95, 85, 90},
            SkillBonuses.of(Skill.INTIMIDATION, 1)),

    TIEFLING("Tiefling",
            new AttributeSet(2, 3, 2, 4, 2, 4, 3),
            new int[]{70, 60, 65},
            SkillBonuses.of(Skill.RELIC_KNOWLEDGE, 2)),

    DRAGONBORN("Draconato",
            new AttributeSet(4, 2, 4, 3, 2, 3, 2),
            new int[]{100, 90, 95},
            SkillBonuses.of(Skill.INTIMIDATION, 1)),

    HALFLING("Halfling",
            new AttributeSet(1, 5, 3, 2, 3, 2, 4),
            new int[]{40, 35, 37},
            SkillBonuses.of(Skill.STEALTH, 2));

    private final String name;
    private final AttributeSet baseAttributes;
    /** Peso base em kg: índice 0=MALE, 1=FEMALE, 2=OTHER */
    private final int[] baseWeightKg;
    private final SkillBonuses skillBonuses;

    Race(String name, AttributeSet baseAttributes, int[] baseWeightKg, SkillBonuses skillBonuses) {
        this.name = name;
        this.baseAttributes = baseAttributes;
        this.baseWeightKg = baseWeightKg;
        this.skillBonuses = skillBonuses;
    }

    public String getName() {
        return name;
    }

    public AttributeSet getBaseAttributes() {
        return baseAttributes;
    }

    public int getBaseWeightKg(Gender gender) {
        return switch (gender) {
            case MALE -> baseWeightKg[0];
            case FEMALE -> baseWeightKg[1];
            case OTHER -> baseWeightKg[2];
        };
    }

    public SkillBonuses getSkillBonuses() {
        return skillBonuses;
    }
}
