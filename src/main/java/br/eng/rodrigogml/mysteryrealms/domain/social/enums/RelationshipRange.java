package br.eng.rodrigogml.mysteryrealms.domain.social.enums;

/**
 * Faixas canônicas de relacionamento com NPC — RF-SS-06 / RF-FP-09.
 */
public enum RelationshipRange {

    MORTAL_ENEMY(-100, -61, "Inimigo Mortal"),
    HOSTILE(-60, -21, "Hostil"),
    NEUTRAL(-20, 20, "Neutra"),
    FAVORABLE(21, 60, "Favorável"),
    ALLY(61, 100, "Aliado");

    private final int minInclusive;
    private final int maxInclusive;
    private final String displayName;

    RelationshipRange(int minInclusive, int maxInclusive, String displayName) {
        this.minInclusive = minInclusive;
        this.maxInclusive = maxInclusive;
        this.displayName = displayName;
    }

    public int getMinInclusive() { return minInclusive; }
    public int getMaxInclusive() { return maxInclusive; }
    public String getDisplayName() { return displayName; }

    /**
     * Retorna a faixa correspondente ao value de relacionamento fornecido.
     */
    public static RelationshipRange of(int value) {
        for (RelationshipRange band : values()) {
            if (value >= band.minInclusive && value <= band.maxInclusive) {
                return band;
            }
        }
        throw new IllegalArgumentException("Valor de relacionamento fora do intervalo [-100, 100]: " + value);
    }
}
