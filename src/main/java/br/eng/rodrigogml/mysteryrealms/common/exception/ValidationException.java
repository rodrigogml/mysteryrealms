package br.eng.rodrigogml.mysteryrealms.common.exception;

import br.eng.rodrigogml.mysteryrealms.common.error.ErrorCode;

/**
 * Exceção de validação para entradas inválidas.
 *
 * @author ?
 * @since 01-05-2026
 */
public class ValidationException extends DomainException {
    public ValidationException(String errorKey) {
        super(errorKey, ErrorCode.VALIDATION);
    }

    public ValidationException(String errorKey, Throwable cause) {
        super(errorKey, ErrorCode.VALIDATION, cause);
    }
}
