package br.eng.rodrigogml.mysteryrealms.common.error;

import br.eng.rodrigogml.mysteryrealms.common.exception.DomainException;
import org.springframework.stereotype.Service;

/**
 * Serviço de mapeamento de exceções para payloads de erro padronizados.
 *
 * @author ?
 * @since 01-05-2026
 */
@Service
public class ErrorMapperService {

    /**
     * Converte exceções para resposta padronizada.
     *
     * @param exception exceção a ser convertida
     * @return payload de erro com código e trace
     */
    public ErrorResponseVO map(Throwable exception) {
        if (exception instanceof DomainException domainException) {
            return new ErrorResponseVO(domainException.getErrorCode().getCode(),
                    domainException.getErrorKey(),
                    domainException.getTraceId());
        }

        return new ErrorResponseVO(ErrorCode.SYSTEM_FAILURE.getCode(),
                "system.error.unexpected",
                "trace-unavailable");
    }
}
