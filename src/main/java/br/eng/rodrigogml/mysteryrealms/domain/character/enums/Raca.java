package br.eng.rodrigogml.mysteryrealms.domain.character.enums;

import br.eng.rodrigogml.mysteryrealms.domain.character.model.ConjuntoAtributos;
import br.eng.rodrigogml.mysteryrealms.domain.character.model.BonusHabilidades;

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
public enum Raca {

    HUMANO("Humano",
            new ConjuntoAtributos(3, 3, 3, 3, 3, 3, 3),
            new int[]{75, 65, 70},
            BonusHabilidades.vazio()),

    ELFO("Elfo",
            new ConjuntoAtributos(2, 4, 2, 4, 4, 2, 3),
            new int[]{60, 50, 55},
            BonusHabilidades.de(Habilidade.CONHECIMENTO_ARCANO, 2)),

    MEIO_ELFO("Meio-elfo",
            new ConjuntoAtributos(2, 3, 3, 3, 3, 4, 3),
            new int[]{67, 57, 62},
            BonusHabilidades.de(Habilidade.PERSUASAO, 1)),

    ANAO("Anão",
            new ConjuntoAtributos(3, 2, 5, 2, 3, 2, 3),
            new int[]{85, 75, 80},
            BonusHabilidades.de(Habilidade.SOBREVIVENCIA, 2)),

    MEIO_ORC("Meio-orc",
            new ConjuntoAtributos(5, 3, 4, 1, 2, 1, 2),
            new int[]{95, 85, 90},
            BonusHabilidades.de(Habilidade.INTIMIDACAO, 1)),

    TIEFLING("Tiefling",
            new ConjuntoAtributos(2, 3, 2, 4, 2, 4, 3),
            new int[]{70, 60, 65},
            BonusHabilidades.de(Habilidade.CONHECIMENTO_RELIQUIAS, 2)),

    DRACONATO("Draconato",
            new ConjuntoAtributos(4, 2, 4, 3, 2, 3, 2),
            new int[]{100, 90, 95},
            BonusHabilidades.de(Habilidade.INTIMIDACAO, 1)),

    HALFLING("Halfling",
            new ConjuntoAtributos(1, 5, 3, 2, 3, 2, 4),
            new int[]{40, 35, 37},
            BonusHabilidades.de(Habilidade.FURTIVIDADE, 2));

    private final String nome;
    private final ConjuntoAtributos atributosBase;
    /** Peso base em kg: índice 0=MASCULINO, 1=FEMININO, 2=OUTRO */
    private final int[] pesoBaseKg;
    private final BonusHabilidades bonusHabilidades;

    Raca(String nome, ConjuntoAtributos atributosBase, int[] pesoBaseKg, BonusHabilidades bonusHabilidades) {
        this.nome = nome;
        this.atributosBase = atributosBase;
        this.pesoBaseKg = pesoBaseKg;
        this.bonusHabilidades = bonusHabilidades;
    }

    public String getNome() {
        return nome;
    }

    public ConjuntoAtributos getAtributosBase() {
        return atributosBase;
    }

    public int getPesoBaseKg(Genero gender) {
        return switch (gender) {
            case MASCULINO -> pesoBaseKg[0];
            case FEMININO -> pesoBaseKg[1];
            case OUTRO -> pesoBaseKg[2];
        };
    }

    public BonusHabilidades getBonusHabilidades() {
        return bonusHabilidades;
    }
}
