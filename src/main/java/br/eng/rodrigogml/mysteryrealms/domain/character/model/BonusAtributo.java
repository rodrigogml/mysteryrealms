package br.eng.rodrigogml.mysteryrealms.domain.character.model;

/**
 * Bônus de atributos (pode conter zeros). Utilizado para bônus de classe
 * que são somados à base da raça na criação do personagem.
 */
public record BonusAtributo(
        int forca,
        int destreza,
        int constituicao,
        int intelecto,
        int percepcao,
        int carisma,
        int vontade) {

    public static BonusAtributo zero() {
        return new BonusAtributo(0, 0, 0, 0, 0, 0, 0);
    }
}
