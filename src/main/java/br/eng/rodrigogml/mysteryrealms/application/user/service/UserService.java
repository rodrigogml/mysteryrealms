package br.eng.rodrigogml.mysteryrealms.application.user.service;

import br.eng.rodrigogml.mysteryrealms.application.user.entity.AccountLockEntity;
import br.eng.rodrigogml.mysteryrealms.application.user.entity.EmailConfirmationEntity;
import br.eng.rodrigogml.mysteryrealms.application.user.entity.EmailConfirmationType;
import br.eng.rodrigogml.mysteryrealms.application.user.entity.LoginAttemptEntity;
import br.eng.rodrigogml.mysteryrealms.application.user.entity.PasswordResetEntity;
import br.eng.rodrigogml.mysteryrealms.application.user.entity.RecoveryCodeEntity;
import br.eng.rodrigogml.mysteryrealms.application.user.entity.SessionEntity;
import br.eng.rodrigogml.mysteryrealms.application.user.entity.TwoFactorAuthEntity;
import br.eng.rodrigogml.mysteryrealms.application.user.entity.TwoFactorMethod;
import br.eng.rodrigogml.mysteryrealms.application.user.entity.UserEntity;
import br.eng.rodrigogml.mysteryrealms.application.user.entity.UserStatus;
import br.eng.rodrigogml.mysteryrealms.application.user.repository.AccountLockRepository;
import br.eng.rodrigogml.mysteryrealms.application.user.repository.EmailConfirmationRepository;
import br.eng.rodrigogml.mysteryrealms.application.user.repository.LoginAttemptRepository;
import br.eng.rodrigogml.mysteryrealms.application.user.repository.PasswordResetRepository;
import br.eng.rodrigogml.mysteryrealms.application.user.repository.RecoveryCodeRepository;
import br.eng.rodrigogml.mysteryrealms.application.user.repository.SessionRepository;
import br.eng.rodrigogml.mysteryrealms.application.user.repository.TwoFactorAuthRepository;
import br.eng.rodrigogml.mysteryrealms.application.user.repository.UnlockCodeRepository;
import br.eng.rodrigogml.mysteryrealms.application.user.repository.UserRepository;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Serviço de aplicação responsável por todas as operações de usuário:
 * registro, autenticação, gerenciamento de senha, e-mail, sessão e autenticação de dois fatores.
 *
 * @author ?
 * @since 28-04-2026
 */
public class UserService {

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final LoginAttemptRepository loginAttemptRepository;
    private final AccountLockRepository accountLockRepository;
    private final UnlockCodeRepository unlockCodeRepository;
    private final EmailConfirmationRepository emailConfirmationRepository;
    private final PasswordResetRepository passwordResetRepository;
    private final TwoFactorAuthRepository twoFactorAuthRepository;
    private final RecoveryCodeRepository recoveryCodeRepository;

    /**
     * Cria o serviço com as dependências necessárias.
     *
     * @param userRepository             repositório de usuários
     * @param sessionRepository          repositório de sessões
     * @param loginAttemptRepository     repositório de tentativas de login
     * @param accountLockRepository      repositório de bloqueios de conta
     * @param unlockCodeRepository       repositório de códigos de desbloqueio
     * @param emailConfirmationRepository repositório de confirmações de e-mail
     * @param passwordResetRepository    repositório de redefinições de senha
     * @param twoFactorAuthRepository    repositório de autenticação de dois fatores
     * @param recoveryCodeRepository     repositório de códigos de recuperação
     */
    public UserService(UserRepository userRepository, SessionRepository sessionRepository,
            LoginAttemptRepository loginAttemptRepository, AccountLockRepository accountLockRepository,
            UnlockCodeRepository unlockCodeRepository, EmailConfirmationRepository emailConfirmationRepository,
            PasswordResetRepository passwordResetRepository, TwoFactorAuthRepository twoFactorAuthRepository,
            RecoveryCodeRepository recoveryCodeRepository) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.loginAttemptRepository = loginAttemptRepository;
        this.accountLockRepository = accountLockRepository;
        this.unlockCodeRepository = unlockCodeRepository;
        this.emailConfirmationRepository = emailConfirmationRepository;
        this.passwordResetRepository = passwordResetRepository;
        this.twoFactorAuthRepository = twoFactorAuthRepository;
        this.recoveryCodeRepository = recoveryCodeRepository;
    }

    // ── Hashing ───────────────────────────────────────────────────────────────

    private String hashPassword(String raw) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(raw.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }

    // ── Validações de entrada ─────────────────────────────────────────────────

    private void validateUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("user.error.usernameBlank");
        }
        if (username.length() < 3) {
            throw new IllegalArgumentException("user.error.usernameTooShort");
        }
        if (username.contains(" ")) {
            throw new IllegalArgumentException("user.error.usernameHasSpaces");
        }
    }

    private void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("user.error.emailBlank");
        }
        if (!email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new IllegalArgumentException("user.error.emailInvalid");
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("user.error.passwordBlank");
        }
        if (password.length() < 8) {
            throw new IllegalArgumentException("user.error.passwordTooShort");
        }
        if (!password.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*[\\d\\W]).*$")) {
            throw new IllegalArgumentException("user.error.passwordWeak");
        }
    }

    // ── Registro e confirmação ────────────────────────────────────────────────

    /**
     * Registra um novo usuário no sistema com status pendente de confirmação de e-mail.
     *
     * @param username    o nome de usuário desejado
     * @param email       o e-mail do usuário
     * @param rawPassword a senha em texto plano
     * @return o usuário criado
     * @throws IllegalArgumentException se qualquer validação falhar ou o nome/e-mail já existir
     */
    public UserEntity register(String username, String email, String rawPassword) {
        validateUsername(username);
        validateEmail(email);
        validatePassword(rawPassword);

        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("user.error.usernameTaken");
        }
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("user.error.emailTaken");
        }

        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash(hashPassword(rawPassword));
        user.setEmailConfirmed(false);
        user.setStatus(UserStatus.PENDING_CONFIRMATION);
        user.setCreatedAt(LocalDateTime.now());
        user = userRepository.save(user);

        EmailConfirmationEntity confirmation = new EmailConfirmationEntity();
        confirmation.setIdUser(user.getId());
        confirmation.setToken(UUID.randomUUID().toString());
        confirmation.setType(EmailConfirmationType.REGISTRATION);
        confirmation.setNewEmail(null);
        confirmation.setExpiresAt(LocalDateTime.now().plusHours(48));
        emailConfirmationRepository.save(confirmation);

        return user;
    }

    /**
     * Confirma o e-mail de um usuário a partir do token enviado.
     *
     * @param token o token de confirmação
     * @return o usuário com e-mail confirmado e status ativo
     * @throws IllegalArgumentException se o token não for encontrado, expirado ou o usuário não existir
     */
    public UserEntity confirmEmail(String token) {
        EmailConfirmationEntity confirmation = emailConfirmationRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("user.error.tokenNotFound"));

        if (LocalDateTime.now().isAfter(confirmation.getExpiresAt())) {
            throw new IllegalArgumentException("user.error.tokenExpired");
        }

        UserEntity user = userRepository.findById(confirmation.getIdUser())
                .orElseThrow(() -> new IllegalArgumentException("user.error.userNotFound"));

        user.setEmailConfirmed(true);
        user.setStatus(UserStatus.ACTIVE);
        user = userRepository.save(user);
        emailConfirmationRepository.delete(confirmation);

        return user;
    }

    // ── Autenticação ──────────────────────────────────────────────────────────

    /**
     * Realiza o login do usuário e cria uma sessão autenticada.
     *
     * @param email       o e-mail do usuário
     * @param rawPassword a senha em texto plano
     * @param ipAddress   o endereço IP da solicitação
     * @return a sessão criada
     * @throws IllegalArgumentException se as credenciais forem inválidas ou a conta estiver bloqueada
     */
    public SessionEntity login(String email, String rawPassword, String ipAddress) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("user.error.userNotFound"));

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new IllegalArgumentException("user.error.userNotActive");
        }

        // Verifica bloqueio de conta
        Optional<AccountLockEntity> lock = accountLockRepository.findTopByIdUserOrderByLockedAtDesc(user.getId());
        if (lock.isPresent() && LocalDateTime.now().isBefore(lock.get().getUnlockAt())) {
            throw new IllegalArgumentException("user.error.accountLocked");
        }

        if (!hashPassword(rawPassword).equals(user.getPasswordHash())) {
            // Registra tentativa falha
            LoginAttemptEntity attempt = new LoginAttemptEntity();
            attempt.setIdUser(user.getId());
            attempt.setIpAddress(ipAddress);
            attempt.setAttemptTime(LocalDateTime.now());
            attempt.setSuccess(false);
            loginAttemptRepository.save(attempt);

            checkBruteForce(user.getId(), ipAddress);
            throw new IllegalArgumentException("user.error.passwordMismatch");
        }

        // Registra tentativa bem-sucedida
        LoginAttemptEntity attempt = new LoginAttemptEntity();
        attempt.setIdUser(user.getId());
        attempt.setIpAddress(ipAddress);
        attempt.setAttemptTime(LocalDateTime.now());
        attempt.setSuccess(true);
        loginAttemptRepository.save(attempt);

        SessionEntity session = new SessionEntity();
        session.setIdUser(user.getId());
        session.setToken(UUID.randomUUID().toString());
        session.setCreatedAt(LocalDateTime.now());
        session.setExpiresAt(LocalDateTime.now().plusHours(24));
        return sessionRepository.save(session);
    }

    /**
     * Encerra a sessão do usuário identificada pelo token.
     *
     * @param token o token da sessão
     * @throws IllegalArgumentException se o token não for encontrado
     */
    public void logout(String token) {
        SessionEntity session = sessionRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("user.error.tokenNotFound"));
        sessionRepository.delete(session);
    }

    private void checkBruteForce(Long userId, String ipAddress) {
        LocalDateTime since = LocalDateTime.now().minusMinutes(30);
        long failedCount = loginAttemptRepository.countByIdUserAndSuccessFalseAndAttemptTimeAfter(userId, since);
        if (failedCount >= 5) {
            AccountLockEntity lock = new AccountLockEntity();
            lock.setIdUser(userId);
            lock.setLockedAt(LocalDateTime.now());
            lock.setUnlockAt(LocalDateTime.now().plusMinutes(30));
            accountLockRepository.save(lock);
            throw new IllegalArgumentException("user.error.accountLocked");
        }
    }

    // ── Redefinição de senha ──────────────────────────────────────────────────

    /**
     * Solicita a redefinição de senha para o e-mail informado.
     * Por segurança, retorna um token mesmo que o e-mail não esteja cadastrado.
     *
     * @param email o e-mail do usuário
     * @return o token de redefinição (ou um UUID fictício se o e-mail não existir)
     */
    public String requestPasswordReset(String email) {
        Optional<UserEntity> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            // Não revela se o e-mail existe
            return UUID.randomUUID().toString();
        }

        UserEntity user = userOpt.get();
        String token = UUID.randomUUID().toString();

        PasswordResetEntity reset = new PasswordResetEntity();
        reset.setIdUser(user.getId());
        reset.setToken(token);
        reset.setExpiresAt(LocalDateTime.now().plusHours(1));
        reset.setUsed(false);
        passwordResetRepository.save(reset);

        return token;
    }

    /**
     * Redefine a senha do usuário a partir de um token válido.
     *
     * @param token       o token de redefinição
     * @param newPassword a nova senha em texto plano
     * @throws IllegalArgumentException se o token não for encontrado, expirado ou a senha for inválida
     */
    public void resetPassword(String token, String newPassword) {
        PasswordResetEntity reset = passwordResetRepository.findByTokenAndUsedFalse(token)
                .orElseThrow(() -> new IllegalArgumentException("user.error.tokenNotFound"));

        if (LocalDateTime.now().isAfter(reset.getExpiresAt())) {
            throw new IllegalArgumentException("user.error.tokenExpired");
        }

        validatePassword(newPassword);

        UserEntity user = userRepository.findById(reset.getIdUser())
                .orElseThrow(() -> new IllegalArgumentException("user.error.userNotFound"));

        user.setPasswordHash(hashPassword(newPassword));
        userRepository.save(user);

        reset.setUsed(true);
        passwordResetRepository.save(reset);

        sessionRepository.deleteAllByIdUser(user.getId());
        accountLockRepository.deleteAllByIdUser(user.getId());
    }

    /**
     * Altera a senha do usuário após verificar a senha atual.
     *
     * @param userId          o ID do usuário
     * @param currentPassword a senha atual em texto plano
     * @param newPassword     a nova senha em texto plano
     * @throws IllegalArgumentException se a senha atual estiver incorreta ou a nova senha for inválida
     */
    public void changePassword(Long userId, String currentPassword, String newPassword) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("user.error.userNotFound"));

        if (!hashPassword(currentPassword).equals(user.getPasswordHash())) {
            throw new IllegalArgumentException("user.error.passwordMismatch");
        }

        validatePassword(newPassword);
        user.setPasswordHash(hashPassword(newPassword));
        userRepository.save(user);
    }

    // ── Gerenciamento de conta ────────────────────────────────────────────────

    /**
     * Altera o nome de usuário.
     *
     * @param userId      o ID do usuário
     * @param newUsername o novo nome de usuário
     * @throws IllegalArgumentException se o nome for inválido ou já estiver em uso
     */
    public void changeUsername(Long userId, String newUsername) {
        validateUsername(newUsername);

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("user.error.userNotFound"));

        if (userRepository.existsByUsername(newUsername)) {
            throw new IllegalArgumentException("user.error.usernameTaken");
        }

        user.setUsername(newUsername);
        userRepository.save(user);
    }

    /**
     * Solicita a alteração do e-mail do usuário.
     *
     * @param userId   o ID do usuário
     * @param newEmail o novo e-mail desejado
     * @throws IllegalArgumentException se o e-mail for inválido ou já estiver em uso
     */
    public void requestEmailChange(Long userId, String newEmail) {
        validateEmail(newEmail);

        if (userRepository.existsByEmail(newEmail)) {
            throw new IllegalArgumentException("user.error.emailTaken");
        }

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("user.error.userNotFound"));

        EmailConfirmationEntity confirmation = new EmailConfirmationEntity();
        confirmation.setIdUser(userId);
        confirmation.setToken(UUID.randomUUID().toString());
        confirmation.setType(EmailConfirmationType.EMAIL_CHANGE);
        confirmation.setNewEmail(newEmail);
        confirmation.setExpiresAt(LocalDateTime.now().plusHours(24));
        emailConfirmationRepository.save(confirmation);
    }

    /**
     * Exclui permanentemente a conta do usuário e todos os seus dados relacionados.
     *
     * @param userId o ID do usuário a excluir
     * @throws IllegalArgumentException se o usuário não for encontrado
     */
    public void deleteAccount(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("user.error.userNotFound"));

        sessionRepository.deleteAllByIdUser(userId);
        emailConfirmationRepository.deleteAllByIdUser(userId);
        passwordResetRepository.deleteAllByIdUser(userId);
        accountLockRepository.deleteAllByIdUser(userId);

        // Remove tentativas de login
        List<LoginAttemptEntity> attempts = loginAttemptRepository
                .findByIdUserAndAttemptTimeAfter(userId, LocalDateTime.of(1900, 1, 1, 0, 0));
        loginAttemptRepository.deleteAll(attempts);

        unlockCodeRepository.deleteAllByIdUser(userId);
        twoFactorAuthRepository.deleteAllByIdUser(userId);
        recoveryCodeRepository.deleteAllByIdUser(userId);
        userRepository.delete(user);
    }

    // ── Autenticação de dois fatores ──────────────────────────────────────────

    /**
     * Habilita a autenticação de dois fatores para um usuário e gera códigos de recuperação.
     *
     * @param userId o ID do usuário
     * @param method o método de autenticação de dois fatores
     * @return lista com 10 códigos de recuperação em texto plano
     * @throws IllegalArgumentException se o usuário não for encontrado
     */
    public List<String> enableTwoFactor(Long userId, TwoFactorMethod method) {
        userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("user.error.userNotFound"));

        // Remove configuração anterior se existir
        twoFactorAuthRepository.findByIdUser(userId).ifPresent(twoFactorAuthRepository::delete);
        recoveryCodeRepository.deleteAllByIdUser(userId);

        TwoFactorAuthEntity tfa = new TwoFactorAuthEntity();
        tfa.setIdUser(userId);
        tfa.setMethod(method);
        // Gera segredo aleatório Base64 para TOTP
        if (method == TwoFactorMethod.TOTP) {
            byte[] secretBytes = new byte[20];
            new SecureRandom().nextBytes(secretBytes);
            tfa.setSecret(Base64.getEncoder().encodeToString(secretBytes));
        }
        tfa.setActive(true);
        twoFactorAuthRepository.save(tfa);

        // Gera e salva 10 códigos de recuperação
        List<String> plainCodes = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            String plain = generateRecoveryCode();
            plainCodes.add(plain);

            RecoveryCodeEntity codeEntity = new RecoveryCodeEntity();
            codeEntity.setIdUser(userId);
            codeEntity.setCodeHash(hashPassword(plain));
            codeEntity.setUsed(false);
            recoveryCodeRepository.save(codeEntity);
        }

        return plainCodes;
    }

    /**
     * Desabilita a autenticação de dois fatores de um usuário.
     *
     * @param userId o ID do usuário
     * @throws IllegalArgumentException se o usuário não for encontrado ou o 2FA não estiver habilitado
     */
    public void disableTwoFactor(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("user.error.userNotFound"));

        TwoFactorAuthEntity tfa = twoFactorAuthRepository.findByIdUser(userId)
                .orElseThrow(() -> new IllegalArgumentException("user.error.twoFactorNotEnabled"));

        twoFactorAuthRepository.delete(tfa);
        recoveryCodeRepository.deleteAllByIdUser(userId);
    }

    /**
     * Valida um código TOTP ou código de recuperação para um usuário.
     *
     * @param userId o ID do usuário
     * @param code   o código a validar
     * @return true se o código for válido
     * @throws IllegalArgumentException se o 2FA não estiver habilitado
     */
    public boolean validateTotpCode(Long userId, String code) {
        TwoFactorAuthEntity tfa = twoFactorAuthRepository.findByIdUser(userId)
                .orElseThrow(() -> new IllegalArgumentException("user.error.twoFactorNotEnabled"));

        // Valida código TOTP
        if (tfa.getMethod() == TwoFactorMethod.TOTP && tfa.getSecret() != null) {
            if (validateTotp(tfa.getSecret(), code)) {
                return true;
            }
        }

        // Verifica códigos de recuperação
        String codeHash = hashPassword(code);
        List<RecoveryCodeEntity> unused = recoveryCodeRepository.findAllByIdUserAndUsedFalse(userId);
        for (RecoveryCodeEntity rc : unused) {
            if (rc.getCodeHash().equals(codeHash)) {
                rc.setUsed(true);
                recoveryCodeRepository.save(rc);
                return true;
            }
        }

        return false;
    }

    // ── Métodos privados TOTP ─────────────────────────────────────────────────

    private String generateRecoveryCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(8);
        for (int i = 0; i < 8; i++) sb.append(chars.charAt(random.nextInt(chars.length())));
        return sb.toString();
    }

    private boolean validateTotp(String secret, String code) {
        long timeStep = System.currentTimeMillis() / 1000 / 30;
        for (long t = timeStep - 1; t <= timeStep + 1; t++) {
            if (generateTotp(secret, t).equals(code)) return true;
        }
        return false;
    }

    private String generateTotp(String secret, long timeCounter) {
        try {
            byte[] keyBytes = Base64.getDecoder().decode(secret);
            byte[] data = new byte[8];
            long value = timeCounter;
            for (int i = 7; i >= 0; i--) {
                data[i] = (byte) (value & 0xFF);
                value >>= 8;
            }
            SecretKeySpec signKey = new SecretKeySpec(keyBytes, "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signKey);
            byte[] hash = mac.doFinal(data);
            int offset = hash[19] & 0xF;
            int truncated = ((hash[offset] & 0x7F) << 24)
                    | ((hash[offset + 1] & 0xFF) << 16)
                    | ((hash[offset + 2] & 0xFF) << 8)
                    | (hash[offset + 3] & 0xFF);
            return String.format("%06d", truncated % 1_000_000);
        } catch (Exception e) {
            throw new IllegalStateException("TOTP generation failed", e);
        }
    }
}
