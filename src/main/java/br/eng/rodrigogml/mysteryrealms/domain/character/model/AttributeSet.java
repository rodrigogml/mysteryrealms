package br.eng.rodrigogml.mysteryrealms.domain.character.model;

/**
 * Os 7 attributes principais do personagem, conforme RF-FP-02.
 * Valores iniciais devem ser >= 1.
 */
public record AttributeSet(
        int strength,
        int dexterity,
        int constitution,
        int intellect,
        int perception,
        int charisma,
        int willpower) {

    public AttributeSet {
        if (strength < 1) throw new IllegalArgumentException("forca deve ser >= 1");
        if (dexterity < 1) throw new IllegalArgumentException("destreza deve ser >= 1");
        if (constitution < 1) throw new IllegalArgumentException("constituicao deve ser >= 1");
        if (intellect < 1) throw new IllegalArgumentException("intelecto deve ser >= 1");
        if (perception < 1) throw new IllegalArgumentException("percepcao deve ser >= 1");
        if (charisma < 1) throw new IllegalArgumentException("carisma deve ser >= 1");
        if (willpower < 1) throw new IllegalArgumentException("vontade deve ser >= 1");
    }

    /**
     * Soma componente a componente com um bônus (que pode ter valores zero).
     */
    public AttributeSet add(AttributeBonus bonus) {
        return new AttributeSet(
                strength + bonus.strength(),
                dexterity + bonus.dexterity(),
                constitution + bonus.constitution(),
                intellect + bonus.intellect(),
                perception + bonus.perception(),
                charisma + bonus.charisma(),
                willpower + bonus.willpower());
    }
}
