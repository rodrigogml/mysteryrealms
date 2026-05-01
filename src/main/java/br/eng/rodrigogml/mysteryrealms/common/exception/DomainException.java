package br.eng.rodrigogml.mysteryrealms.common.exception;

import br.eng.rodrigogml.mysteryrealms.common.error.ErrorCode;

import java.util.UUID;

/**
 * Exceção base de domínio com metadados de rastreabilidade.
 *
 * @author ?
 * @since 01-05-2026
 */
public class DomainException extends RuntimeException {
    private final String errorKey;
    private final ErrorCode errorCode;
    private final String traceId;

    public DomainException(String errorKey) {
        this(errorKey, ErrorCode.BUSINESS_RULE, null);
    }

    public DomainException(String errorKey, Throwable cause) {
        this(errorKey, ErrorCode.BUSINESS_RULE, cause);
    }

    public DomainException(String errorKey, ErrorCode errorCode) {
        this(errorKey, errorCode, null);
    }

    public DomainException(String errorKey, ErrorCode errorCode, Throwable cause) {
        super(errorKey, cause);
        this.errorKey = errorKey;
        this.errorCode = errorCode;
        this.traceId = UUID.randomUUID().toString();
    }

    public String getErrorKey() {
        return errorKey;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public String getTraceId() {
        return traceId;
    }
}
