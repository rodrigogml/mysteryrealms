package br.eng.rodrigogml.mysteryrealms.common.exception;

public class DomainException extends RuntimeException {
    private final String errorKey;

    public DomainException(String errorKey) {
        super(errorKey);
        this.errorKey = errorKey;
    }

    public DomainException(String errorKey, Throwable cause) {
        super(errorKey, cause);
        this.errorKey = errorKey;
    }

    public String getErrorKey() {
        return errorKey;
    }
}
