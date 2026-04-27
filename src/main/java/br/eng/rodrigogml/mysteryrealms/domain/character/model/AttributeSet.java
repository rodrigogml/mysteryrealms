package br.eng.rodrigogml.mysteryrealms.domain.character.model;

/**
 * Os 7 atributos principais do personagem, conforme RF-FP-02.
 * Valores iniciais devem ser >= 1.
 */
public record AttributeSet(
        int forca,
        int destreza,
        int constituicao,
        int intelecto,
        int percepcao,
        int carisma,
        int vontade) {

    public AttributeSet {
        if (forca < 1) throw new IllegalArgumentException("forca deve ser >= 1");
        if (destreza < 1) throw new IllegalArgumentException("destreza deve ser >= 1");
        if (constituicao < 1) throw new IllegalArgumentException("constituicao deve ser >= 1");
        if (intelecto < 1) throw new IllegalArgumentException("intelecto deve ser >= 1");
        if (percepcao < 1) throw new IllegalArgumentException("percepcao deve ser >= 1");
        if (carisma < 1) throw new IllegalArgumentException("carisma deve ser >= 1");
        if (vontade < 1) throw new IllegalArgumentException("vontade deve ser >= 1");
    }

    /**
     * Soma componente a componente com um bônus (que pode ter valores zero).
     */
    public AttributeSet plus(BonusAtributo bonus) {
        return new AttributeSet(
                forca + bonus.forca(),
                destreza + bonus.destreza(),
                constituicao + bonus.constituicao(),
                intelecto + bonus.intelecto(),
                percepcao + bonus.percepcao(),
                carisma + bonus.carisma(),
                vontade + bonus.vontade());
    }
}
