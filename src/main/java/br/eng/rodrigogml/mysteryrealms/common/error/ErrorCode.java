package br.eng.rodrigogml.mysteryrealms.common.error;

/**
 * Códigos de erro padronizados e rastreáveis do sistema.
 *
 * @author ?
 * @since 01-05-2026
 */
public enum ErrorCode {
    VALIDATION("MR-VAL-001"),
    BUSINESS_RULE("MR-BUS-001"),
    SYSTEM_FAILURE("MR-SYS-001");

    private final String code;

    ErrorCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
