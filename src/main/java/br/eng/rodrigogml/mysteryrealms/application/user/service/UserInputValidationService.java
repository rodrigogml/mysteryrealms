package br.eng.rodrigogml.mysteryrealms.application.user.service;

import br.eng.rodrigogml.mysteryrealms.common.exception.ValidationException;
import org.springframework.stereotype.Service;

/**
 * Camada explícita de validação para entradas de fluxos de usuário.
 *
 * @author ?
 * @since 01-05-2026
 */
@Service
public class UserInputValidationService {

    /**
     * Valida username de cadastro.
     *
     * @param username nome de usuário
     */
    public void validateUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new ValidationException("user.error.usernameBlank");
        }
        if (username.length() < 3) {
            throw new ValidationException("user.error.usernameTooShort");
        }
        if (username.chars().anyMatch(Character::isWhitespace)) {
            throw new ValidationException("user.error.usernameHasSpaces");
        }
    }

    /**
     * Valida e-mail.
     *
     * @param email endereço de e-mail
     */
    public void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new ValidationException("user.error.emailBlank");
        }
        if (!email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new ValidationException("user.error.emailInvalid");
        }
    }

    /**
     * Valida senha.
     *
     * @param password senha em texto plano
     */
    public void validatePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new ValidationException("user.error.passwordBlank");
        }
        if (password.length() < 8) {
            throw new ValidationException("user.error.passwordTooShort");
        }
        if (!password.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*[\\d\\W]).*$")) {
            throw new ValidationException("user.error.passwordWeak");
        }
    }
}
