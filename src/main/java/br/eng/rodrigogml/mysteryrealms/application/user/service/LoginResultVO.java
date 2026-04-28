package br.eng.rodrigogml.mysteryrealms.application.user.service;

import br.eng.rodrigogml.mysteryrealms.application.user.entity.SessionEntity;
import br.eng.rodrigogml.mysteryrealms.application.user.entity.TwoFactorMethod;

/**
 * Value object imutável que representa o resultado da primeira etapa de autenticação.
 *
 * @author ?
 * @since 28-04-2026
 */
public record LoginResultVO(
        Status status,
        SessionEntity session,
        Long userId,
        TwoFactorMethod secondFactorMethod,
        String challengeCode) {

    /**
     * Estados possíveis do resultado de login.
     *
     * @author ?
     * @since 28-04-2026
     */
    public enum Status {
        AUTHENTICATED,
        SECOND_FACTOR_REQUIRED
    }

    /**
     * Cria um resultado autenticado com sessão emitida.
     *
     * @param session a sessão criada
     * @return o resultado autenticado
     */
    public static LoginResultVO authenticated(SessionEntity session) {
        return new LoginResultVO(Status.AUTHENTICATED, session, null, null, null);
    }

    /**
     * Cria um resultado pendente de segundo fator.
     *
     * @param userId o ID do usuário autenticado na primeira etapa
     * @param secondFactorMethod o método de segundo fator exigido
     * @param challengeCode o código de desafio gerado para integração externa, quando aplicável
     * @return o resultado pendente de segundo fator
     */
    public static LoginResultVO secondFactorRequired(Long userId, TwoFactorMethod secondFactorMethod,
            String challengeCode) {
        return new LoginResultVO(Status.SECOND_FACTOR_REQUIRED, null, userId, secondFactorMethod, challengeCode);
    }
}
