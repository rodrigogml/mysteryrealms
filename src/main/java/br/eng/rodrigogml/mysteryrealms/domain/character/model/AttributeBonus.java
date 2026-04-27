package br.eng.rodrigogml.mysteryrealms.domain.character.model;

/**
 * Bônus de attributes (pode conter zeros). Utilizado para bônus de classe
 * que são somados à base da raça na criação do personagem.
 */
public record AttributeBonus(
        int strength,
        int dexterity,
        int constitution,
        int intellect,
        int perception,
        int charisma,
        int willpower) {

    public static AttributeBonus zero() {
        return new AttributeBonus(0, 0, 0, 0, 0, 0, 0);
    }
}
