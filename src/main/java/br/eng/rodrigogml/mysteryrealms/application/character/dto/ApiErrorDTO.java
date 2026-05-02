package br.eng.rodrigogml.mysteryrealms.application.character.dto;

import java.time.Instant;

/**
 * DTO de erro padronizado para APIs.
 *
 * @author ?
 * @since 02-05-2026
 */
public class ApiErrorDTO {

    private String code;
    private String message;
    private Instant timestamp;

    public ApiErrorDTO() {
    }

    public ApiErrorDTO(String code, String message, Instant timestamp) {
        this.code = code;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }
}
