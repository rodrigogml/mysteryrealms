package br.eng.rodrigogml.mysteryrealms.domain.economy.service;

import br.eng.rodrigogml.mysteryrealms.domain.economy.model.MonetaryValue;
import br.eng.rodrigogml.mysteryrealms.common.exception.DomainException;
import br.eng.rodrigogml.mysteryrealms.common.exception.ValidationException;

/**
 * Serviço de precificação de itens — RF-EI-02.
 *
 * Aplica a fórmula canônica de preço a partir dos fatores de mercado.
 */
public final class PricingService {

    /** Fator mínimo permitido por faixa canônica — RF-EI-02. */
    public static final double FACTOR_MIN = 0.5;

    /** Fator máximo permitido por faixa canônica — RF-EI-02. */
    public static final double FACTOR_MAX = 2.0;

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
     * Cada fator deve estar no intervalo [{@value #FACTOR_MIN}, {@value #FACTOR_MAX}].
     * O resultado é convertido de MS total para {@link MonetaryValue} normalized.
     *
     * @param basePrice          preço base do item
     * @param fatorLugar         custo de vida e logística da localidade
     * @param fatorReputacao     reputação do personagem no local
     * @param fatorRelacionamento vínculo com o NPC comerciante
     * @param fatorOfertaDemanda escassez/abundância do item no mercado
     * @param fatorVenda         penalidade de revenda (deve ser <= 1.0 para desfavorecer revenda)
     * @return preço resultante como {@link MonetaryValue} normalized
     * @throws IllegalArgumentException se qualquer fator estiver fora de [{@value #FACTOR_MIN}, {@value #FACTOR_MAX}]
     */
    public static MonetaryValue applyPrice(
            MonetaryValue basePrice,
            double fatorLugar,
            double fatorReputacao,
            double fatorRelacionamento,
            double fatorOfertaDemanda,
            double fatorVenda) {

        if (basePrice == null) {
            throw new ValidationException("economy.error.basePriceRequired");
        }

        validateFactor("fatorLugar", fatorLugar);
        validateFactor("fatorReputacao", fatorReputacao);
        validateFactor("fatorRelacionamento", fatorRelacionamento);
        validateFactor("fatorOfertaDemanda", fatorOfertaDemanda);
        validateFactor("fatorVenda", fatorVenda);
        if (fatorVenda > 1.0) {
            throw new ValidationException("fatorVenda deve ser <= 1.0, recebido: " + fatorVenda);
        }

        double fatorTotal = fatorLugar * fatorReputacao * fatorRelacionamento
                * fatorOfertaDemanda * fatorVenda;

        long baseMs = basePrice.totalInMinorUnits();
        long resultadoMs = Math.round(baseMs * fatorTotal);

        // Converte de volta para MonetaryValue normalized
        long mp = resultadoMs / 100;
        long ms = resultadoMs % 100;
        return MonetaryValue.of(mp, ms);
    }

    private static void validateFactor(String name, double value) {
        if (!Double.isFinite(value) || value < FACTOR_MIN || value > FACTOR_MAX) {
            throw new ValidationException(
                    name + " deve estar em [" + FACTOR_MIN + ", " + FACTOR_MAX + "], recebido: " + value);
        }
    }
}
