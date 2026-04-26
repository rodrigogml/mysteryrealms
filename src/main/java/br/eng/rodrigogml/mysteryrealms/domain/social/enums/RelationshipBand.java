package br.eng.rodrigogml.mysteryrealms.domain.social.enums;

/**
 * Faixas canônicas de relacionamento com NPC — RF-SS-06 / RF-FP-09.
 */
public enum RelationshipBand {

    INIMIGO_MORTAL(-100, -61, "Inimigo Mortal"),
    HOSTIL(-60, -21, "Hostil"),
    NEUTRAL(-20, 20, "Neutra"),
    FAVORABLE(21, 60, "Favorável"),
    ALLY(61, 100, "Aliado");

    private final int minInclusive;
    private final int maxInclusive;
    private final String exibicao;

    RelationshipBand(int minInclusive, int maxInclusive, String exibicao) {
        this.minInclusive = minInclusive;
        this.maxInclusive = maxInclusive;
        this.exibicao = exibicao;
    }

    public int getMinInclusive() { return minInclusive; }
    public int getMaxInclusive() { return maxInclusive; }
    public String getExibicao() { return exibicao; }

    /**
     * Retorna a faixa correspondente ao valor de relacionamento fornecido.
     */
    public static RelationshipBand of(int valor) {
        for (RelationshipBand band : values()) {
            if (valor >= band.minInclusive && valor <= band.maxInclusive) {
                return band;
            }
        }
        throw new IllegalArgumentException("Valor de relacionamento fora do intervalo [-100, 100]: " + valor);
    }
}
