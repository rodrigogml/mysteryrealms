package br.eng.rodrigogml.mysteryrealms.application.user.service;

import br.eng.rodrigogml.mysteryrealms.application.user.entity.TwoFactorMethod;

import java.util.List;

/**
 * Value object imutavel que representa o resultado da ativacao de 2FA.
 *
 * @author ?
 * @since 28-04-2026
 */
public record TwoFactorActivationVO(
        TwoFactorMethod method,
        String secret,
        List<String> recoveryCodes) {

    /**
     * Cria uma instancia imutavel com copia defensiva dos codigos.
     *
     * @param method o metodo ativado
     * @param secret o segredo textual do TOTP, quando aplicavel
     * @param recoveryCodes os codigos de recuperacao exibidos uma unica vez
     */
    public TwoFactorActivationVO {
        recoveryCodes = List.copyOf(recoveryCodes);
    }
}
