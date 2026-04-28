package br.eng.rodrigogml.mysteryrealms.domain.economy.model;

/**
 * Perfil de crítico de um tipo de arma ou de uma arma específica — RF-EI-06 / RF-EI-07.
 *
 * <p>Define a faixa mínima do dado a partir da qual um acerto é considerado crítico
 * e o multiplicador de dano aplicado nessa situação.
 *
 * @param minDieValue valor mínimo do dado (d20) que configura um crítico. Deve estar em [1, 20].
 * @param multiplier  multiplicador de dano aplicado no crítico. Deve ser {@code >= 2}.
 *
 * @author ?
 * @since 28-04-2026
 */
public record CriticalProfile(int minDieValue, int multiplier) {

    /**
     * Valida os parâmetros ao construir o registro.
     *
     * @throws IllegalArgumentException se {@code minDieValue} não estiver em [1, 20]
     *                                  ou se {@code multiplier} for menor que 2
     */
    public CriticalProfile {
        if (minDieValue < 1 || minDieValue > 20) {
            throw new IllegalArgumentException(
                    "minDieValue deve estar em [1, 20], recebido: " + minDieValue);
        }
        if (multiplier < 2) {
            throw new IllegalArgumentException(
                    "multiplier deve ser >= 2, recebido: " + multiplier);
        }
    }

    /**
     * Verifica se o valor do dado configura um crítico.
     *
     * @param dieValue valor obtido no dado (d20)
     * @return {@code true} se {@code dieValue >= minDieValue}
     */
    public boolean isCritical(int dieValue) {
        return dieValue >= minDieValue;
    }
}
