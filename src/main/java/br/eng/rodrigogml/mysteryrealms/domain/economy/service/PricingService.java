package br.eng.rodrigogml.mysteryrealms.domain.economy.service;

import br.eng.rodrigogml.mysteryrealms.domain.economy.model.MonetaryValue;

/**
 * Serviço de precificação de itens — RF-EI-02.
 *
 * Aplica a fórmula canônica de preço a partir dos fatores de mercado.
 */
public final class PricingService {

    /** Fator mínimo permitido por faixa canônica — RF-EI-02. */
    public static final double FATOR_MIN = 0.5;

    /** Fator máximo permitido por faixa canônica — RF-EI-02. */
    public static final double FATOR_MAX = 2.0;

    private PricingService() {}

    // ── RF-EI-02: fórmula de preço ────────────────────────────────────────────

    /**
     * Calcula o preço aplicado de um item com base nos fatores de mercado — RF-EI-02.
     *
     * <pre>
     * fator_total = fatorLugar × fatorReputacao × fatorRelacionamento
     *               × fatorOfertaDemanda × fatorVenda
     * preco_aplicado = preco_base × fator_total
     * </pre>
     *
     * Cada fator deve estar no intervalo [{@value #FATOR_MIN}, {@value #FATOR_MAX}].
     * O resultado é convertido de MS total para {@link MonetaryValue} normalizado.
     *
     * @param precoBase          preço base do item
     * @param fatorLugar         custo de vida e logística da localidade
     * @param fatorReputacao     reputação do personagem no local
     * @param fatorRelacionamento vínculo com o NPC comerciante
     * @param fatorOfertaDemanda escassez/abundância do item no mercado
     * @param fatorVenda         penalidade de revenda (deve ser <= 1.0 para desfavorecer revenda)
     * @return preço resultante como {@link MonetaryValue} normalizado
     * @throws IllegalArgumentException se qualquer fator estiver fora de [{@value #FATOR_MIN}, {@value #FATOR_MAX}]
     */
    public static MonetaryValue applyPrice(
            MonetaryValue precoBase,
            double fatorLugar,
            double fatorReputacao,
            double fatorRelacionamento,
            double fatorOfertaDemanda,
            double fatorVenda) {

        validateFator("fatorLugar", fatorLugar);
        validateFator("fatorReputacao", fatorReputacao);
        validateFator("fatorRelacionamento", fatorRelacionamento);
        validateFator("fatorOfertaDemanda", fatorOfertaDemanda);
        validateFator("fatorVenda", fatorVenda);

        double fatorTotal = fatorLugar * fatorReputacao * fatorRelacionamento
                * fatorOfertaDemanda * fatorVenda;

        long baseMs = precoBase.toTotalMs();
        long resultadoMs = Math.round(baseMs * fatorTotal);

        // Converte de volta para MonetaryValue normalizado
        long mp = resultadoMs / 100;
        long ms = resultadoMs % 100;
        return MonetaryValue.of(mp, ms);
    }

    private static void validateFator(String nome, double valor) {
        if (valor < FATOR_MIN || valor > FATOR_MAX) {
            throw new IllegalArgumentException(
                    nome + " deve estar em [" + FATOR_MIN + ", " + FATOR_MAX + "], recebido: " + valor);
        }
    }
}
