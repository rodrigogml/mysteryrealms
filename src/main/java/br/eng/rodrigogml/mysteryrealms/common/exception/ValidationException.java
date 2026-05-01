package br.eng.rodrigogml.mysteryrealms.common.exception;

public class ValidationException extends DomainException {
    public ValidationException(String errorKey) {
        super(errorKey);
    }

    public ValidationException(String errorKey, Throwable cause) {
        super(errorKey, cause);
    }
}
