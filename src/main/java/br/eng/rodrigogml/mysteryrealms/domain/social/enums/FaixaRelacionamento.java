package br.eng.rodrigogml.mysteryrealms.domain.social.enums;

/**
 * Faixas canônicas de relacionamento com NPC — RF-SS-06 / RF-FP-09.
 */
public enum FaixaRelacionamento {

    INIMIGO_MORTAL(-100, -61, "Inimigo Mortal"),
    HOSTIL(-60, -21, "Hostil"),
    NEUTRO(-20, 20, "Neutra"),
    FAVORAVEL(21, 60, "Favorável"),
    ALIADO(61, 100, "Aliado");

    private final int minInclusive;
    private final int maxInclusive;
    private final String exibicao;

    FaixaRelacionamento(int minInclusive, int maxInclusive, String exibicao) {
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
    public static FaixaRelacionamento de(int valor) {
        for (FaixaRelacionamento band : values()) {
            if (valor >= band.minInclusive && valor <= band.maxInclusive) {
                return band;
            }
        }
        throw new IllegalArgumentException("Valor de relacionamento fora do intervalo [-100, 100]: " + valor);
    }
}
