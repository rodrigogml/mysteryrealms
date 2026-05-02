package br.eng.rodrigogml.mysteryrealms.application.character.controller;

import br.eng.rodrigogml.mysteryrealms.application.character.dto.ApiErrorDTO;
import br.eng.rodrigogml.mysteryrealms.common.exception.DomainException;
import br.eng.rodrigogml.mysteryrealms.common.exception.ValidationException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

/**
 * Handler global para erros da API.
 *
 * @author ?
 * @since 02-05-2026
 */
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler({ValidationException.class, DomainException.class})
    public ResponseEntity<ApiErrorDTO> handleBusiness(RuntimeException ex) {
        return ResponseEntity.badRequest().body(new ApiErrorDTO(ex.getMessage(), ex.getMessage(), Instant.now()));
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
    public ResponseEntity<ApiErrorDTO> handleBeanValidation(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiErrorDTO("request.error.invalidPayload", ex.getMessage(), Instant.now()));
    }
}
