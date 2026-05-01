package br.eng.rodrigogml.mysteryrealms.application.user.service;

import br.eng.rodrigogml.mysteryrealms.application.character.entity.CharacterEntity;
import br.eng.rodrigogml.mysteryrealms.application.character.repository.CharacterRepository;
import br.eng.rodrigogml.mysteryrealms.application.character.service.CharacterService;
import br.eng.rodrigogml.mysteryrealms.application.user.entity.AccountLockEntity;
import br.eng.rodrigogml.mysteryrealms.application.user.entity.EmailConfirmationEntity;
import br.eng.rodrigogml.mysteryrealms.application.user.entity.EmailConfirmationType;
import br.eng.rodrigogml.mysteryrealms.application.user.entity.LoginAttemptEntity;
import br.eng.rodrigogml.mysteryrealms.application.user.entity.PasswordResetEntity;
import br.eng.rodrigogml.mysteryrealms.application.user.entity.RecoveryCodeEntity;
import br.eng.rodrigogml.mysteryrealms.application.user.entity.SessionEntity;
import br.eng.rodrigogml.mysteryrealms.application.user.entity.TwoFactorAuthEntity;
import br.eng.rodrigogml.mysteryrealms.application.user.entity.TwoFactorMethod;
import br.eng.rodrigogml.mysteryrealms.application.user.entity.UnlockCodeEntity;
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

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import br.eng.rodrigogml.mysteryrealms.common.exception.DomainException;
import br.eng.rodrigogml.mysteryrealms.common.exception.ValidationException;

/**
 * Serviço de aplicação responsável por todas as operações de usuário:
 * registro, autenticação, gerenciamento de senha, e-mail, sessão e autenticação de dois fatores.
 *
 * @author ?
 * @since 28-04-2026
 */
@Service
@Transactional
public class UserService {

    private static final int REGISTRATION_CONFIRMATION_HOURS = 48;
    private static final int EMAIL_CHANGE_CONFIRMATION_HOURS = 24;
    private static final int ACCOUNT_DELETION_CONFIRMATION_HOURS = 24;
    private static final int PASSWORD_RESET_HOURS = 1;
    private static final int SESSION_HOURS = 24;
    private static final int BRUTE_FORCE_WINDOW_MINUTES = 30;
    private static final int BRUTE_FORCE_MAX_ATTEMPTS = 5;
    private static final int ACCOUNT_LOCK_MINUTES = 30;
    private static final int UNLOCK_CODE_MINUTES = 15;
    private static final int EMAIL_OTP_MINUTES = 10;
    private static final int NUMERIC_CODE_LENGTH = 6;

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final LoginAttemptRepository loginAttemptRepository;
    private final AccountLockRepository accountLockRepository;
    private final UnlockCodeRepository unlockCodeRepository;
    private final EmailConfirmationRepository emailConfirmationRepository;
    private final PasswordResetRepository passwordResetRepository;
    private final TwoFactorAuthRepository twoFactorAuthRepository;
    private final RecoveryCodeRepository recoveryCodeRepository;
    private final CharacterRepository characterRepository;
    private final CharacterService characterService;
    private final EmailService emailService;
    private final UserInputValidationService userInputValidationService;

    /**
     * Cria o serviço com as dependências necessárias.
     *
     * @param userRepository repositório de usuários
     * @param sessionRepository repositório de sessões
     * @param loginAttemptRepository repositório de tentativas de login
     * @param accountLockRepository repositório de bloqueios de conta
     * @param unlockCodeRepository repositório de códigos de desbloqueio
     * @param emailConfirmationRepository repositório de confirmações de e-mail
     * @param passwordResetRepository repositório de redefinições de senha
     * @param twoFactorAuthRepository repositório de autenticação de dois fatores
     * @param recoveryCodeRepository repositório de códigos de recuperação
     * @param characterRepository repositório de personagens
     * @param characterService serviço de personagens para remoção em cascata
     * @param emailService serviço de envio de e-mails transacionais
     */
    public UserService(UserRepository userRepository, SessionRepository sessionRepository,
            LoginAttemptRepository loginAttemptRepository, AccountLockRepository accountLockRepository,
            UnlockCodeRepository unlockCodeRepository, EmailConfirmationRepository emailConfirmationRepository,
            PasswordResetRepository passwordResetRepository, TwoFactorAuthRepository twoFactorAuthRepository,
            RecoveryCodeRepository recoveryCodeRepository, CharacterRepository characterRepository,
            CharacterService characterService, EmailService emailService,
            UserInputValidationService userInputValidationService) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.loginAttemptRepository = loginAttemptRepository;
        this.accountLockRepository = accountLockRepository;
        this.unlockCodeRepository = unlockCodeRepository;
        this.emailConfirmationRepository = emailConfirmationRepository;
        this.passwordResetRepository = passwordResetRepository;
        this.twoFactorAuthRepository = twoFactorAuthRepository;
        this.recoveryCodeRepository = recoveryCodeRepository;
        this.characterRepository = characterRepository;
        this.characterService = characterService;
        this.emailService = emailService;
        this.userInputValidationService = userInputValidationService;
    }

    private String hashPassword(String raw) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(raw.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new DomainException("system.error.sha256Unavailable", e);
        }
    }


    /**
     * Registra um novo usuário no sistema com status pendente de confirmação de e-mail.
     *
     * @param username o nome de usuário desejado
     * @param email o e-mail do usuário
     * @param rawPassword a senha em texto plano
     * @return o usuário criado
     * @throws IllegalArgumentException se qualquer validação falhar ou o nome/e-mail já existir
     */
    public UserEntity register(String username, String email, String rawPassword) {
        purgeExpiredPendingRegistrations();
        userInputValidationService.validateUsername(username);
        userInputValidationService.validateEmail(email);
        userInputValidationService.validatePassword(rawPassword);

        if (userRepository.existsByUsername(username)) {
            throw new ValidationException("user.error.usernameTaken");
        }
        if (userRepository.existsByEmail(email)) {
            throw new ValidationException("user.error.emailTaken");
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
        confirmation.setExpiresAt(LocalDateTime.now().plusHours(REGISTRATION_CONFIRMATION_HOURS));
        emailConfirmationRepository.save(confirmation);
        emailService.sendRegistrationConfirmation(user, confirmation.getToken());

        return user;
    }

    /**
     * Confirma o e-mail de um usuário a partir do token enviado.
     *
     * @param token o token de confirmação
     * @return o usuário atualizado
     * @throws IllegalArgumentException se o token não for encontrado, expirado ou o usuário não existir
     */
    public UserEntity confirmEmail(String token) {
        EmailConfirmationEntity confirmation = emailConfirmationRepository.findByToken(token)
                .orElseThrow(() -> new ValidationException("user.error.tokenNotFound"));

        if (LocalDateTime.now().isAfter(confirmation.getExpiresAt())) {
            throw new ValidationException("user.error.tokenExpired");
        }

        UserEntity user = userRepository.findById(confirmation.getIdUser())
                .orElseThrow(() -> new ValidationException("user.error.userNotFound"));

        if (confirmation.getType() == EmailConfirmationType.REGISTRATION) {
            user.setEmailConfirmed(true);
            user.setStatus(UserStatus.ACTIVE);
        } else if (confirmation.getType() == EmailConfirmationType.EMAIL_CHANGE) {
            String newEmail = confirmation.getNewEmail();
            userInputValidationService.validateEmail(newEmail);
            if (!user.getEmail().equals(newEmail) && userRepository.existsByEmail(newEmail)) {
                throw new ValidationException("user.error.emailTaken");
            }
            user.setEmail(newEmail);
        } else {
            throw new ValidationException("user.error.tokenNotFound");
        }

        user = userRepository.save(user);
        emailConfirmationRepository.delete(confirmation);
        return user;
    }

    /**
     * Realiza a primeira etapa do login do usuário.
     *
     * @param email o e-mail do usuário
     * @param rawPassword a senha em texto plano
     * @param ipAddress o endereço IP da solicitação
     * @return o resultado do login, autenticado imediatamente ou pendente de segundo fator
     * @throws IllegalArgumentException se as credenciais forem inválidas ou a conta estiver bloqueada
     */
    public LoginResultVO login(String email, String rawPassword, String ipAddress) {
        // TODO detectar dispositivo/IP nao reconhecido para notificacao de seguranca.
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ValidationException("user.error.userNotFound"));

        reconcileExpiredAccountLock(user);
        ensureActiveUser(user);
        ensureAccountNotLocked(user.getId());

        if (!hashPassword(rawPassword).equals(user.getPasswordHash())) {
            registerLoginAttempt(user.getId(), ipAddress, false);
            checkBruteForce(user);
            throw new ValidationException("user.error.passwordMismatch");
        }

        registerLoginAttempt(user.getId(), ipAddress, true);

        Optional<TwoFactorAuthEntity> twoFactorOpt = twoFactorAuthRepository.findByIdUser(user.getId());
        if (twoFactorOpt.isEmpty() || !twoFactorOpt.get().isActive()) {
            return LoginResultVO.authenticated(createSession(user.getId()));
        }

        TwoFactorAuthEntity twoFactor = twoFactorOpt.get();
        String challengeCode = null;
        if (twoFactor.getMethod() == TwoFactorMethod.EMAIL) {
            challengeCode = issueEmailSecondFactorCode(user.getId());
        }
        return LoginResultVO.secondFactorRequired(user.getId(), twoFactor.getMethod(), challengeCode);
    }

    /**
     * Conclui o login de um usuário que possui segundo fator habilitado.
     *
     * @param userId o ID do usuário
     * @param code o código do segundo fator ou código de recuperação
     * @return a sessão autenticada criada
     * @throws IllegalArgumentException se o usuário não existir, estiver bloqueado ou o código for inválido
     */
    public SessionEntity completeTwoFactorLogin(Long userId, String code) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ValidationException("user.error.userNotFound"));

        reconcileExpiredAccountLock(user);
        ensureActiveUser(user);
        ensureAccountNotLocked(userId);
        if (!validateSecondFactorCode(userId, code)) {
            throw new ValidationException("user.error.invalidSecondFactorCode");
        }

        return createSession(userId);
    }

    /**
     * Encerra a sessão do usuário identificada pelo token.
     *
     * @param token o token da sessão
     * @throws IllegalArgumentException se o token não for encontrado
     */
    public void logout(String token) {
        SessionEntity session = sessionRepository.findByToken(token)
                .orElseThrow(() -> new ValidationException("user.error.tokenNotFound"));
        sessionRepository.delete(session);
    }

    /**
     * Valida uma sessão autenticada e renova sua expiração quando ainda está ativa.
     *
     * @param token o token da sessão
     * @return a sessão renovada
     * @throws IllegalArgumentException se o token não for encontrado ou a sessão já estiver expirada
     */
    public SessionEntity validateSession(String token) {
        SessionEntity session = sessionRepository.findByToken(token)
                .orElseThrow(() -> new ValidationException("user.error.tokenNotFound"));

        if (!isSessionActive(session)) {
            sessionRepository.delete(session);
            throw new ValidationException("user.error.sessionExpired");
        }

        renewSessionExpiration(session);
        return sessionRepository.save(session);
    }

    /**
     * Desbloqueia imediatamente a conta do usuário por meio de código numérico.
     *
     * @param userId o ID do usuário
     * @param code o código de desbloqueio
     * @throws IllegalArgumentException se o código não existir, estiver expirado ou for inválido
     */
    public void unlockAccount(Long userId, String code) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ValidationException("user.error.userNotFound"));

        Optional<AccountLockEntity> lock = accountLockRepository.findTopByIdUserOrderByLockedAtDesc(userId);
        if (lock.isEmpty() || LocalDateTime.now().isAfter(lock.get().getUnlockAt())) {
            throw new ValidationException("user.error.unlockCodeNotFound");
        }

        UnlockCodeEntity unlockCode = findLatestUnlockCode(userId)
                .orElseThrow(() -> new ValidationException("user.error.unlockCodeNotFound"));

        if (LocalDateTime.now().isAfter(unlockCode.getExpiresAt())) {
            throw new ValidationException("user.error.tokenExpired");
        }
        if (!unlockCode.getCode().equals(code)) {
            throw new ValidationException("user.error.unlockCodeInvalid");
        }

        unlockCode.setUsed(true);
        unlockCodeRepository.save(unlockCode);
        accountLockRepository.deleteAllByIdUser(userId);
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);
    }

    /**
     * Remove contas pendentes cuja confirmação de cadastro expirou.
     *
     * @return quantidade de contas pendentes removidas
     */
    public int purgeExpiredPendingRegistrations() {
        List<EmailConfirmationEntity> expiredConfirmations = emailConfirmationRepository
                .findAllByTypeAndExpiresAtBefore(EmailConfirmationType.REGISTRATION, LocalDateTime.now());
        Set<Long> processedUsers = new HashSet<>();
        int removedUsers = 0;

        for (EmailConfirmationEntity confirmation : expiredConfirmations) {
            Long userId = confirmation.getIdUser();
            if (userId == null || !processedUsers.add(userId)) {
                emailConfirmationRepository.delete(confirmation);
                continue;
            }

            Optional<UserEntity> userOpt = userRepository.findById(userId);
            if (userOpt.isPresent()
                    && userOpt.get().getStatus() == UserStatus.PENDING_CONFIRMATION
                    && !userOpt.get().isEmailConfirmed()) {
                deleteAccountData(userId, userOpt.get());
                removedUsers++;
            } else {
                emailConfirmationRepository.delete(confirmation);
            }
        }

        return removedUsers;
    }

    private void checkBruteForce(UserEntity user) {
        LocalDateTime since = LocalDateTime.now().minusMinutes(BRUTE_FORCE_WINDOW_MINUTES);
        long failedCount = countConsecutiveFailedAttempts(user.getId(), since);
        if (failedCount >= BRUTE_FORCE_MAX_ATTEMPTS) {
            AccountLockEntity lock = new AccountLockEntity();
            lock.setIdUser(user.getId());
            lock.setLockedAt(LocalDateTime.now());
            lock.setUnlockAt(LocalDateTime.now().plusMinutes(ACCOUNT_LOCK_MINUTES));
            accountLockRepository.save(lock);

            user.setStatus(UserStatus.LOCKED);
            userRepository.save(user);
            issueUnlockCode(user.getId());
            throw new ValidationException("user.error.accountLocked");
        }
    }

    private long countConsecutiveFailedAttempts(Long userId, LocalDateTime since) {
        long consecutiveFailures = 0;
        List<LoginAttemptEntity> recentAttempts = loginAttemptRepository
                .findByIdUserAndAttemptTimeAfterOrderByAttemptTimeDesc(userId, since);
        for (LoginAttemptEntity attempt : recentAttempts) {
            if (attempt.isSuccess()) {
                break;
            }
            consecutiveFailures++;
        }
        return consecutiveFailures;
    }

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
            return UUID.randomUUID().toString();
        }

        UserEntity user = userOpt.get();
        String token = UUID.randomUUID().toString();

        PasswordResetEntity reset = new PasswordResetEntity();
        reset.setIdUser(user.getId());
        reset.setToken(token);
        reset.setExpiresAt(LocalDateTime.now().plusHours(PASSWORD_RESET_HOURS));
        reset.setUsed(false);
        passwordResetRepository.save(reset);

        return token;
    }

    /**
     * Redefine a senha do usuário a partir de um token válido.
     *
     * @param token o token de redefinição
     * @param newPassword a nova senha em texto plano
     * @throws IllegalArgumentException se o token não for encontrado, expirado ou a senha for inválida
     */
    public void resetPassword(String token, String newPassword) {
        PasswordResetEntity reset = passwordResetRepository.findByTokenAndUsedFalse(token)
                .orElseThrow(() -> new ValidationException("user.error.tokenNotFound"));

        if (LocalDateTime.now().isAfter(reset.getExpiresAt())) {
            throw new ValidationException("user.error.tokenExpired");
        }

        userInputValidationService.validatePassword(newPassword);

        UserEntity user = userRepository.findById(reset.getIdUser())
                .orElseThrow(() -> new ValidationException("user.error.userNotFound"));

        user.setPasswordHash(hashPassword(newPassword));
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);

        reset.setUsed(true);
        passwordResetRepository.save(reset);

        sessionRepository.deleteAllByIdUser(user.getId());
        accountLockRepository.deleteAllByIdUser(user.getId());
    }

    /**
     * Altera a senha do usuário após verificar a senha atual.
     *
     * @param userId o ID do usuário
     * @param currentPassword a senha atual em texto plano
     * @param newPassword a nova senha em texto plano
     * @throws IllegalArgumentException se a senha atual estiver incorreta ou a nova senha for inválida
     */
    public void changePassword(Long userId, String currentPassword, String newPassword) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ValidationException("user.error.userNotFound"));

        if (!hashPassword(currentPassword).equals(user.getPasswordHash())) {
            throw new ValidationException("user.error.passwordMismatch");
        }

        userInputValidationService.validatePassword(newPassword);
        user.setPasswordHash(hashPassword(newPassword));
        userRepository.save(user);
    }

    /**
     * Altera o nome de usuário.
     *
     * @param userId o ID do usuário
     * @param newUsername o novo nome de usuário
     * @throws IllegalArgumentException se o nome for inválido ou já estiver em uso
     */
    public void changeUsername(Long userId, String newUsername) {
        userInputValidationService.validateUsername(newUsername);

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ValidationException("user.error.userNotFound"));

        if (userRepository.existsByUsername(newUsername)) {
            throw new ValidationException("user.error.usernameTaken");
        }

        user.setUsername(newUsername);
        userRepository.save(user);
    }

    /**
     * Solicita a alteração do e-mail do usuário.
     *
     * @param userId o ID do usuário
     * @param newEmail o novo e-mail desejado
     * @throws IllegalArgumentException se o e-mail for inválido ou já estiver em uso
     */
    public void requestEmailChange(Long userId, String newEmail) {
        userInputValidationService.validateEmail(newEmail);

        if (userRepository.existsByEmail(newEmail)) {
            throw new ValidationException("user.error.emailTaken");
        }

        userRepository.findById(userId)
                .orElseThrow(() -> new ValidationException("user.error.userNotFound"));

        EmailConfirmationEntity confirmation = new EmailConfirmationEntity();
        confirmation.setIdUser(userId);
        confirmation.setToken(UUID.randomUUID().toString());
        confirmation.setType(EmailConfirmationType.EMAIL_CHANGE);
        confirmation.setNewEmail(newEmail);
        confirmation.setExpiresAt(LocalDateTime.now().plusHours(EMAIL_CHANGE_CONFIRMATION_HOURS));
        emailConfirmationRepository.save(confirmation);
        emailService.sendEmailChangeConfirmation(newEmail, confirmation.getToken());
    }

    /**
     * Solicita a exclusao permanente da conta do usuario.
     *
     * @param userId o ID do usuario a excluir
     * @param secondFactorCode o codigo do segundo fator atual, quando o 2FA estiver ativo
     * @return o token de confirmacao emitido para a exclusao
     * @throws IllegalArgumentException se o usuario nao for encontrado ou o segundo fator for invalido
     */
    public String requestAccountDeletion(Long userId, String secondFactorCode) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ValidationException("user.error.userNotFound"));

        Optional<TwoFactorAuthEntity> twoFactorOpt = twoFactorAuthRepository.findByIdUser(userId);
        if (twoFactorOpt.isPresent() && twoFactorOpt.get().isActive() && !validateSecondFactorCode(userId, secondFactorCode)) {
            throw new ValidationException("user.error.invalidSecondFactorCode");
        }

        EmailConfirmationEntity confirmation = new EmailConfirmationEntity();
        confirmation.setIdUser(user.getId());
        confirmation.setToken(UUID.randomUUID().toString());
        confirmation.setType(EmailConfirmationType.ACCOUNT_DELETION);
        confirmation.setNewEmail(null);
        confirmation.setExpiresAt(LocalDateTime.now().plusHours(ACCOUNT_DELETION_CONFIRMATION_HOURS));
        emailConfirmationRepository.save(confirmation);
        emailService.sendAccountDeletionConfirmation(user, confirmation.getToken());
        return confirmation.getToken();
    }

    /**
     * Confirma a exclusao permanente da conta do usuario pelo token emitido.
     *
     * @param token o token de confirmacao de exclusao
     * @throws IllegalArgumentException se o token nao existir, estiver expirado ou nao pertencer a exclusao de conta
     */
    public void confirmAccountDeletion(String token) {
        EmailConfirmationEntity confirmation = emailConfirmationRepository.findByToken(token)
                .orElseThrow(() -> new ValidationException("user.error.tokenNotFound"));

        if (confirmation.getType() != EmailConfirmationType.ACCOUNT_DELETION) {
            throw new ValidationException("user.error.tokenNotFound");
        }
        if (LocalDateTime.now().isAfter(confirmation.getExpiresAt())) {
            throw new ValidationException("user.error.tokenExpired");
        }

        UserEntity user = userRepository.findById(confirmation.getIdUser())
                .orElseThrow(() -> new ValidationException("user.error.userNotFound"));

        deleteAccountData(user.getId(), user);
    }

    /**
     * Habilita a autenticação de dois fatores para um usuário e gera códigos de recuperação.
     *
     * @param userId o ID do usuário
     * @param method o método de autenticação de dois fatores
     * @return configuracao ativada com segredo textual TOTP, quando aplicavel
     * @throws IllegalArgumentException se o usuário não for encontrado
     */
    public TwoFactorActivationVO enableTwoFactor(Long userId, TwoFactorMethod method) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ValidationException("user.error.userNotFound"));

        twoFactorAuthRepository.findByIdUser(userId).ifPresent(twoFactorAuthRepository::delete);
        recoveryCodeRepository.deleteAllByIdUser(userId);

        TwoFactorAuthEntity tfa = new TwoFactorAuthEntity();
        tfa.setIdUser(userId);
        tfa.setMethod(method);
        if (method == TwoFactorMethod.TOTP) {
            byte[] secretBytes = new byte[20];
            new SecureRandom().nextBytes(secretBytes);
            tfa.setSecret(Base64.getEncoder().encodeToString(secretBytes));
            // TODO integrar geracao de QR Code para o segredo textual do TOTP.
        }
        tfa.setActive(true);
        twoFactorAuthRepository.save(tfa);

        return new TwoFactorActivationVO(method, tfa.getSecret(), generateRecoveryCodes(userId));
    }

    /**
     * Regenera todos os codigos de recuperacao de um usuario com 2FA ativo.
     *
     * @param userId o ID do usuario
     * @return nova lista com 10 codigos de recuperacao em texto plano
     * @throws IllegalArgumentException se o usuario nao for encontrado ou o 2FA nao estiver habilitado
     */
    public List<String> regenerateRecoveryCodes(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ValidationException("user.error.userNotFound"));

        ensureTwoFactorEnabled(userId);
        recoveryCodeRepository.deleteAllByIdUser(userId);
        return generateRecoveryCodes(userId);
    }

    /**
     * Desabilita a autenticação de dois fatores de um usuário.
     *
     * @param userId o ID do usuário
     * @param code o codigo do segundo fator ativo ou um codigo de recuperacao valido
     * @throws IllegalArgumentException se o usuário não for encontrado, o 2FA não estiver habilitado ou o código for inválido
     */
    public void disableTwoFactor(Long userId, String code) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ValidationException("user.error.userNotFound"));

        TwoFactorAuthEntity tfa = ensureTwoFactorEnabled(userId);
        if (!validateSecondFactorCode(userId, code)) {
            throw new ValidationException("user.error.invalidSecondFactorCode");
        }

        twoFactorAuthRepository.delete(tfa);
        recoveryCodeRepository.deleteAllByIdUser(userId);
    }

    private void deleteAccountData(Long userId, UserEntity user) {
        for (CharacterEntity character : characterRepository.findAllByIdUser(userId)) {
            characterService.deleteCharacter(userId, character.getId());
        }

        sessionRepository.deleteAllByIdUser(userId);
        emailConfirmationRepository.deleteAllByIdUser(userId);
        passwordResetRepository.deleteAllByIdUser(userId);
        accountLockRepository.deleteAllByIdUser(userId);

        List<LoginAttemptEntity> attempts = loginAttemptRepository
                .findByIdUserAndAttemptTimeAfter(userId, LocalDateTime.of(1900, 1, 1, 0, 0));
        loginAttemptRepository.deleteAll(attempts);

        unlockCodeRepository.deleteAllByIdUser(userId);
        twoFactorAuthRepository.deleteAllByIdUser(userId);
        recoveryCodeRepository.deleteAllByIdUser(userId);
        userRepository.delete(user);
    }

    /**
     * Valida um código de segundo fator ou código de recuperação para um usuário.
     *
     * @param userId o ID do usuário
     * @param code o código a validar
     * @return true se o código for válido
     * @throws IllegalArgumentException se o 2FA não estiver habilitado
     */
    public boolean validateTotpCode(Long userId, String code) {
        return validateSecondFactorCode(userId, code);
    }

    private boolean validateSecondFactorCode(Long userId, String code) {
        TwoFactorAuthEntity tfa = ensureTwoFactorEnabled(userId);
        if (code == null || code.isBlank()) {
            return false;
        }

        if (tfa.getMethod() == TwoFactorMethod.EMAIL) {
            Optional<UnlockCodeEntity> secondFactorCode = unlockCodeRepository
                    .findTopByIdUserAndUsedFalseOrderByExpiresAtDesc(userId);
            if (secondFactorCode.isPresent()
                    && !LocalDateTime.now().isAfter(secondFactorCode.get().getExpiresAt())
                    && secondFactorCode.get().getCode().equals(code)) {
                UnlockCodeEntity entity = secondFactorCode.get();
                entity.setUsed(true);
                unlockCodeRepository.save(entity);
                return true;
            }
        }

        if (tfa.getMethod() == TwoFactorMethod.TOTP && tfa.getSecret() != null) {
            if (validateTotp(tfa.getSecret(), code)) {
                return true;
            }
        }

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

    private TwoFactorAuthEntity ensureTwoFactorEnabled(Long userId) {
        return twoFactorAuthRepository.findByIdUser(userId)
                .filter(TwoFactorAuthEntity::isActive)
                .orElseThrow(() -> new ValidationException("user.error.twoFactorNotEnabled"));
    }

    private void ensureActiveUser(UserEntity user) {
        if (user.getStatus() == UserStatus.PENDING_CONFIRMATION || !user.isEmailConfirmed()) {
            throw new ValidationException("user.error.userNotActive");
        }
    }

    private void ensureAccountNotLocked(Long userId) {
        Optional<AccountLockEntity> lock = accountLockRepository.findTopByIdUserOrderByLockedAtDesc(userId);
        if (lock.isPresent() && LocalDateTime.now().isBefore(lock.get().getUnlockAt())) {
            throw new ValidationException("user.error.accountLocked");
        }
    }

    private void reconcileExpiredAccountLock(UserEntity user) {
        Optional<AccountLockEntity> lock = accountLockRepository.findTopByIdUserOrderByLockedAtDesc(user.getId());
        if (lock.isPresent() && !LocalDateTime.now().isBefore(lock.get().getUnlockAt())) {
            accountLockRepository.deleteAllByIdUser(user.getId());
            if (user.getStatus() == UserStatus.LOCKED) {
                user.setStatus(UserStatus.ACTIVE);
                userRepository.save(user);
            }
        }
    }

    private void registerLoginAttempt(Long userId, String ipAddress, boolean success) {
        LoginAttemptEntity attempt = new LoginAttemptEntity();
        attempt.setIdUser(userId);
        attempt.setIpAddress(ipAddress);
        attempt.setAttemptTime(LocalDateTime.now());
        attempt.setSuccess(success);
        loginAttemptRepository.save(attempt);
    }

    private SessionEntity createSession(Long userId) {
        SessionEntity session = new SessionEntity();
        session.setIdUser(userId);
        session.setToken(UUID.randomUUID().toString());
        session.setCreatedAt(LocalDateTime.now());
        renewSessionExpiration(session);
        return sessionRepository.save(session);
    }

    private boolean isSessionActive(SessionEntity session) {
        return !LocalDateTime.now().isAfter(session.getExpiresAt());
    }

    private void renewSessionExpiration(SessionEntity session) {
        session.setExpiresAt(LocalDateTime.now().plusHours(SESSION_HOURS));
    }

    private List<String> generateRecoveryCodes(Long userId) {
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

    private String generateRecoveryCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    private Optional<UnlockCodeEntity> findLatestUnlockCode(Long userId) {
        return unlockCodeRepository.findTopByIdUserAndUsedFalseOrderByExpiresAtDesc(userId);
    }

    private String issueUnlockCode(Long userId) {
        unlockCodeRepository.deleteAllByIdUser(userId);
        String code = generateNumericCode(NUMERIC_CODE_LENGTH);

        UnlockCodeEntity unlockCode = new UnlockCodeEntity();
        unlockCode.setIdUser(userId);
        unlockCode.setCode(code);
        unlockCode.setExpiresAt(LocalDateTime.now().plusMinutes(UNLOCK_CODE_MINUTES));
        unlockCode.setUsed(false);
        unlockCodeRepository.save(unlockCode);
        return code;
    }

    private String issueEmailSecondFactorCode(Long userId) {
        unlockCodeRepository.deleteAllByIdUser(userId);
        String code = generateNumericCode(NUMERIC_CODE_LENGTH);

        UnlockCodeEntity challenge = new UnlockCodeEntity();
        challenge.setIdUser(userId);
        challenge.setCode(code);
        challenge.setExpiresAt(LocalDateTime.now().plusMinutes(EMAIL_OTP_MINUTES));
        challenge.setUsed(false);
        unlockCodeRepository.save(challenge);
        return code;
    }

    private String generateNumericCode(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    private boolean validateTotp(String secret, String code) {
        long timeStep = System.currentTimeMillis() / 1000 / 30;
        for (long t = timeStep - 1; t <= timeStep + 1; t++) {
            if (generateTotp(secret, t).equals(code)) {
                return true;
            }
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
            throw new DomainException("system.error.totpGenerationFailed", e);
        }
    }
}
