package br.eng.rodrigogml.mysteryrealms.application.user.session;

import br.eng.rodrigogml.mysteryrealms.application.user.entity.SessionEntity;
import br.eng.rodrigogml.mysteryrealms.application.user.repository.SessionRepository;
import br.eng.rodrigogml.mysteryrealms.common.exception.ValidationException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/**
 * Resolve o usuário autenticado para endpoints REST a partir de um token de sessão validado.
 *
 * @author ?
 * @since 06-05-2026
 */
@Component
public class AuthenticatedUserContext {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String SESSION_TOKEN_HEADER = "X-Session-Token";
    private static final int SESSION_HOURS = 24;

    private final HttpServletRequest request;
    private final SessionRepository sessionRepository;

    public AuthenticatedUserContext(HttpServletRequest request, SessionRepository sessionRepository) {
        this.request = request;
        this.sessionRepository = sessionRepository;
    }

    /**
     * Obtém o ID do usuário autenticado pelo token de sessão enviado na request atual.
     *
     * @return o ID do usuário dono da sessão validada
     * @throws ValidationException se o token não for enviado, não existir ou estiver expirado
     */
    @Transactional
    public Long authenticatedUserId() {
        String token = resolveToken();
        SessionEntity session = sessionRepository.findByToken(token)
                .orElseThrow(() -> new ValidationException("user.error.tokenNotFound"));

        if (LocalDateTime.now().isAfter(session.getExpiresAt())) {
            sessionRepository.delete(session);
            throw new ValidationException("user.error.sessionExpired");
        }

        session.setExpiresAt(LocalDateTime.now().plusHours(SESSION_HOURS));
        return sessionRepository.save(session).getIdUser();
    }

    private String resolveToken() {
        String authorization = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(authorization) && authorization.startsWith(BEARER_PREFIX)) {
            String token = authorization.substring(BEARER_PREFIX.length()).trim();
            if (StringUtils.hasText(token)) {
                return token;
            }
        }

        String sessionToken = request.getHeader(SESSION_TOKEN_HEADER);
        if (StringUtils.hasText(sessionToken)) {
            return sessionToken.trim();
        }

        throw new ValidationException("user.error.tokenNotFound");
    }
}
