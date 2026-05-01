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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import br.eng.rodrigogml.mysteryrealms.common.exception.DomainException;
import br.eng.rodrigogml.mysteryrealms.common.exception.ValidationException;

/**
 * Testes do serviço de usuário — RF-UA-01 a RF-UA-06.
 *
 * @author ?
 * @since 28-04-2026
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private SessionRepository sessionRepository;
    @Mock private LoginAttemptRepository loginAttemptRepository;
    @Mock private AccountLockRepository accountLockRepository;
    @Mock private UnlockCodeRepository unlockCodeRepository;
    @Mock private EmailConfirmationRepository emailConfirmationRepository;
    @Mock private PasswordResetRepository passwordResetRepository;
    @Mock private TwoFactorAuthRepository twoFactorAuthRepository;
    @Mock private RecoveryCodeRepository recoveryCodeRepository;
    @Mock private CharacterRepository characterRepository;
    @Mock private CharacterService characterService;
    @Mock private EmailService emailService;

    private UserService service;

    @BeforeEach
    void setUp() {
        service = new UserService(userRepository, sessionRepository, loginAttemptRepository,
                accountLockRepository, unlockCodeRepository, emailConfirmationRepository,
                passwordResetRepository, twoFactorAuthRepository, recoveryCodeRepository,
                characterRepository, characterService, emailService);
    }

    @Test
    void register_sucesso_criaUsuarioPendente() {
        when(userRepository.existsByUsername("jogador1")).thenReturn(false);
        when(userRepository.existsByEmail("jogador@email.com")).thenReturn(false);
        UserEntity saved = new UserEntity();
        saved.setId(1L);
        saved.setUsername("jogador1");
        saved.setEmail("jogador@email.com");
        saved.setStatus(UserStatus.PENDING_CONFIRMATION);
        when(userRepository.save(any())).thenReturn(saved);

        UserEntity result = service.register("jogador1", "jogador@email.com", "Senha123!");

        assertEquals(UserStatus.PENDING_CONFIRMATION, result.getStatus());
        verify(emailConfirmationRepository).save(any(EmailConfirmationEntity.class));
        verify(emailService).sendRegistrationConfirmation(eq(saved), any());
    }

    @Test
    void register_nomeUsuarioJaExiste_lancaExcecao() {
        when(userRepository.existsByUsername("jogador1")).thenReturn(true);
        assertThrows(ValidationException.class,
                () -> service.register("jogador1", "jogador@email.com", "Senha123!"));
    }

    @Test
    void register_emailJaExiste_lancaExcecao() {
        when(userRepository.existsByUsername("jogador1")).thenReturn(false);
        when(userRepository.existsByEmail("jogador@email.com")).thenReturn(true);
        assertThrows(ValidationException.class,
                () -> service.register("jogador1", "jogador@email.com", "Senha123!"));
    }

    @Test
    void register_nomeUsuarioMenorQue3_lancaExcecao() {
        assertThrows(ValidationException.class,
                () -> service.register("ab", "jogador@email.com", "Senha123!"));
    }

    @Test
    void register_nomeUsuarioComEspaco_lancaExcecao() {
        assertThrows(ValidationException.class,
                () -> service.register("jo gador", "jogador@email.com", "Senha123!"));
    }

    @Test
    void register_nomeUsuarioComTab_lancaExcecao() {
        assertThrows(ValidationException.class,
                () -> service.register("jo\tgador", "jogador@email.com", "Senha123!"));
    }

    @Test
    void register_emailInvalido_lancaExcecao() {
        assertThrows(ValidationException.class,
                () -> service.register("jogador1", "emailinvalido", "Senha123!"));
    }

    @Test
    void register_senhaFraca_lancaExcecao() {
        assertThrows(ValidationException.class,
                () -> service.register("jogador1", "jogador@email.com", "senhafraca"));
    }

    @Test
    void register_senhaCurtaDemais_lancaExcecao() {
        assertThrows(ValidationException.class,
                () -> service.register("jogador1", "jogador@email.com", "Ab1!"));
    }

    @Test
    void confirmEmail_tokenValido_ativaUsuario() {
        EmailConfirmationEntity conf = new EmailConfirmationEntity();
        conf.setIdUser(1L);
        conf.setToken("tok");
        conf.setType(EmailConfirmationType.REGISTRATION);
        conf.setExpiresAt(LocalDateTime.now().plusHours(1));
        when(emailConfirmationRepository.findByToken("tok")).thenReturn(Optional.of(conf));

        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setStatus(UserStatus.PENDING_CONFIRMATION);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        UserEntity result = service.confirmEmail("tok");

        assertEquals(UserStatus.ACTIVE, result.getStatus());
        assertTrue(result.isEmailConfirmed());
    }

    @Test
    void confirmEmail_emailChange_atualizaEmail() {
        EmailConfirmationEntity conf = new EmailConfirmationEntity();
        conf.setIdUser(1L);
        conf.setToken("troca");
        conf.setType(EmailConfirmationType.EMAIL_CHANGE);
        conf.setNewEmail("novo@email.com");
        conf.setExpiresAt(LocalDateTime.now().plusHours(1));
        when(emailConfirmationRepository.findByToken("troca")).thenReturn(Optional.of(conf));
        when(userRepository.existsByEmail("novo@email.com")).thenReturn(false);

        UserEntity user = activeUser();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        UserEntity result = service.confirmEmail("troca");

        assertEquals("novo@email.com", result.getEmail());
        verify(emailConfirmationRepository).delete(conf);
    }

    @Test
    void confirmEmail_tokenNaoEncontrado_lancaExcecao() {
        when(emailConfirmationRepository.findByToken("invalido")).thenReturn(Optional.empty());
        assertThrows(ValidationException.class, () -> service.confirmEmail("invalido"));
    }

    @Test
    void confirmEmail_tokenExpirado_lancaExcecao() {
        EmailConfirmationEntity conf = new EmailConfirmationEntity();
        conf.setIdUser(1L);
        conf.setToken("tok");
        conf.setExpiresAt(LocalDateTime.now().minusHours(1));
        when(emailConfirmationRepository.findByToken("tok")).thenReturn(Optional.of(conf));
        assertThrows(ValidationException.class, () -> service.confirmEmail("tok"));
    }

    @Test
    void purgeExpiredPendingRegistrations_removeContaPendenteExpirada() {
        EmailConfirmationEntity confirmation = new EmailConfirmationEntity();
        confirmation.setIdUser(1L);
        confirmation.setType(EmailConfirmationType.REGISTRATION);
        confirmation.setExpiresAt(LocalDateTime.now().minusMinutes(1));

        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setStatus(UserStatus.PENDING_CONFIRMATION);
        user.setEmailConfirmed(false);

        when(emailConfirmationRepository.findAllByTypeAndExpiresAtBefore(eq(EmailConfirmationType.REGISTRATION), any()))
                .thenReturn(List.of(confirmation));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(characterRepository.findAllByIdUser(1L)).thenReturn(Collections.emptyList());
        when(loginAttemptRepository.findByIdUserAndAttemptTimeAfter(eq(1L), any())).thenReturn(Collections.emptyList());

        int removed = service.purgeExpiredPendingRegistrations();

        assertEquals(1, removed);
        verify(sessionRepository).deleteAllByIdUser(1L);
        verify(emailConfirmationRepository).deleteAllByIdUser(1L);
        verify(userRepository).delete(user);
    }

    @Test
    void login_credenciaisValidas_retornaSessao() {
        UserEntity user = activeUser();
        when(userRepository.findByEmail("jogador@email.com")).thenReturn(Optional.of(user));
        when(accountLockRepository.findTopByIdUserOrderByLockedAtDesc(1L)).thenReturn(Optional.empty());
        when(twoFactorAuthRepository.findByIdUser(1L)).thenReturn(Optional.empty());
        when(loginAttemptRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        SessionEntity session = new SessionEntity();
        session.setToken("tok");
        when(sessionRepository.save(any())).thenReturn(session);

        LoginResultVO result = service.login("jogador@email.com", "Senha123!", "127.0.0.1");

        assertEquals(LoginResultVO.Status.AUTHENTICATED, result.status());
        assertNotNull(result.session().getToken());
    }

    @Test
    void login_comEmail2fa_retornaSegundoFatorSemCriarSessao() {
        UserEntity user = activeUser();
        TwoFactorAuthEntity tfa = new TwoFactorAuthEntity();
        tfa.setIdUser(1L);
        tfa.setMethod(TwoFactorMethod.EMAIL);
        tfa.setActive(true);

        when(userRepository.findByEmail("jogador@email.com")).thenReturn(Optional.of(user));
        when(accountLockRepository.findTopByIdUserOrderByLockedAtDesc(1L)).thenReturn(Optional.empty());
        when(twoFactorAuthRepository.findByIdUser(1L)).thenReturn(Optional.of(tfa));
        when(loginAttemptRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(unlockCodeRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        LoginResultVO result = service.login("jogador@email.com", "Senha123!", "127.0.0.1");

        assertEquals(LoginResultVO.Status.SECOND_FACTOR_REQUIRED, result.status());
        assertEquals(1L, result.userId());
        assertEquals(TwoFactorMethod.EMAIL, result.secondFactorMethod());
        assertNotNull(result.challengeCode());
        verify(sessionRepository, never()).save(any());
    }

    @Test
    void login_senhaIncorreta_lancaExcecao() {
        UserEntity user = activeUser();
        when(userRepository.findByEmail("jogador@email.com")).thenReturn(Optional.of(user));
        when(accountLockRepository.findTopByIdUserOrderByLockedAtDesc(1L)).thenReturn(Optional.empty());
        when(loginAttemptRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(loginAttemptRepository.findByIdUserAndAttemptTimeAfterOrderByAttemptTimeDesc(eq(1L), any()))
                .thenReturn(List.of(failedAttempt()));

        assertThrows(ValidationException.class,
                () -> service.login("jogador@email.com", "SenhaErrada1!", "127.0.0.1"));
    }

    @Test
    void login_contaBloqueada_lancaExcecao() {
        UserEntity user = activeUser();
        when(userRepository.findByEmail("jogador@email.com")).thenReturn(Optional.of(user));

        AccountLockEntity lock = new AccountLockEntity();
        lock.setUnlockAt(LocalDateTime.now().plusMinutes(20));
        when(accountLockRepository.findTopByIdUserOrderByLockedAtDesc(1L)).thenReturn(Optional.of(lock));

        assertThrows(ValidationException.class,
                () -> service.login("jogador@email.com", "Senha123!", "127.0.0.1"));
    }

    @Test
    void login_bloqueioExpirado_desbloqueiaAutomaticamente() {
        UserEntity user = activeUser();
        user.setStatus(UserStatus.LOCKED);
        AccountLockEntity lock = new AccountLockEntity();
        lock.setUnlockAt(LocalDateTime.now().minusMinutes(1));

        when(userRepository.findByEmail("jogador@email.com")).thenReturn(Optional.of(user));
        when(accountLockRepository.findTopByIdUserOrderByLockedAtDesc(1L)).thenReturn(Optional.of(lock));
        when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(twoFactorAuthRepository.findByIdUser(1L)).thenReturn(Optional.empty());
        when(loginAttemptRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        SessionEntity session = new SessionEntity();
        session.setToken("tok");
        when(sessionRepository.save(any())).thenReturn(session);

        LoginResultVO result = service.login("jogador@email.com", "Senha123!", "127.0.0.1");

        assertEquals(LoginResultVO.Status.AUTHENTICATED, result.status());
        assertEquals(UserStatus.ACTIVE, user.getStatus());
        verify(accountLockRepository).deleteAllByIdUser(1L);
    }

    @Test
    void login_usuarioNaoAtivo_lancaExcecao() {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setStatus(UserStatus.PENDING_CONFIRMATION);
        when(userRepository.findByEmail("jogador@email.com")).thenReturn(Optional.of(user));

        assertThrows(ValidationException.class,
                () -> service.login("jogador@email.com", "Senha123!", "127.0.0.1"));
    }

    @Test
    void checkBruteForce_apos5Falhas_bloqueiaContaEGeraCodigo() {
        UserEntity user = activeUser();
        when(userRepository.findByEmail("jogador@email.com")).thenReturn(Optional.of(user));
        when(accountLockRepository.findTopByIdUserOrderByLockedAtDesc(1L)).thenReturn(Optional.empty());
        when(loginAttemptRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(loginAttemptRepository.findByIdUserAndAttemptTimeAfterOrderByAttemptTimeDesc(eq(1L), any()))
                .thenReturn(List.of(
                        failedAttempt(),
                        failedAttempt(),
                        failedAttempt(),
                        failedAttempt(),
                        failedAttempt()));
        when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(unlockCodeRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        assertThrows(ValidationException.class,
                () -> service.login("jogador@email.com", "SenhaErrada1!", "127.0.0.1"));
        verify(accountLockRepository).save(any(AccountLockEntity.class));
        verify(unlockCodeRepository).save(any(UnlockCodeEntity.class));
    }

    @Test
    void login_cincoFalhasNaoConsecutivas_naoBloqueiaConta() {
        UserEntity user = activeUser();
        when(userRepository.findByEmail("jogador@email.com")).thenReturn(Optional.of(user));
        when(accountLockRepository.findTopByIdUserOrderByLockedAtDesc(1L)).thenReturn(Optional.empty());
        when(loginAttemptRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(loginAttemptRepository.findByIdUserAndAttemptTimeAfterOrderByAttemptTimeDesc(eq(1L), any()))
                .thenReturn(List.of(
                        failedAttempt(),
                        successfulAttempt(),
                        failedAttempt(),
                        failedAttempt(),
                        failedAttempt(),
                        failedAttempt()));

        ValidationException ex = assertThrows(ValidationException.class,
                () -> service.login("jogador@email.com", "SenhaErrada1!", "127.0.0.1"));

        assertEquals("user.error.passwordMismatch", ex.getMessage());
        verify(accountLockRepository, never()).save(any(AccountLockEntity.class));
        verify(unlockCodeRepository, never()).save(any(UnlockCodeEntity.class));
    }

    @Test
    void completeTwoFactorLogin_comCodigoEmailValido_criaSessao() {
        UserEntity user = activeUser();
        TwoFactorAuthEntity tfa = new TwoFactorAuthEntity();
        tfa.setIdUser(1L);
        tfa.setMethod(TwoFactorMethod.EMAIL);
        tfa.setActive(true);
        UnlockCodeEntity challenge = new UnlockCodeEntity();
        challenge.setIdUser(1L);
        challenge.setCode("123456");
        challenge.setExpiresAt(LocalDateTime.now().plusMinutes(10));
        challenge.setUsed(false);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(accountLockRepository.findTopByIdUserOrderByLockedAtDesc(1L)).thenReturn(Optional.empty());
        when(twoFactorAuthRepository.findByIdUser(1L)).thenReturn(Optional.of(tfa));
        when(unlockCodeRepository.findTopByIdUserAndUsedFalseOrderByExpiresAtDesc(1L))
                .thenReturn(Optional.of(challenge));
        when(unlockCodeRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        SessionEntity session = new SessionEntity();
        session.setToken("tok");
        when(sessionRepository.save(any())).thenReturn(session);

        SessionEntity result = service.completeTwoFactorLogin(1L, "123456");

        assertEquals("tok", result.getToken());
        assertTrue(challenge.isUsed());
    }

    @Test
    void completeTwoFactorLogin_usuarioNaoAtivo_lancaExcecao() {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setStatus(UserStatus.PENDING_CONFIRMATION);
        user.setEmailConfirmed(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(accountLockRepository.findTopByIdUserOrderByLockedAtDesc(1L)).thenReturn(Optional.empty());

        ValidationException ex = assertThrows(ValidationException.class,
                () -> service.completeTwoFactorLogin(1L, "123456"));

        assertEquals("user.error.userNotActive", ex.getMessage());
        verify(sessionRepository, never()).save(any());
    }

    @Test
    void logout_tokenValido_removeSessao() {
        SessionEntity session = new SessionEntity();
        session.setToken("tok");
        when(sessionRepository.findByToken("tok")).thenReturn(Optional.of(session));

        service.logout("tok");

        verify(sessionRepository).delete(session);
    }

    @Test
    void logout_tokenInvalido_lancaExcecao() {
        when(sessionRepository.findByToken("invalido")).thenReturn(Optional.empty());
        assertThrows(ValidationException.class, () -> service.logout("invalido"));
    }

    @Test
    void validateSession_tokenValido_renovaExpiracao() {
        SessionEntity session = new SessionEntity();
        session.setIdUser(1L);
        session.setToken("tok");
        session.setCreatedAt(LocalDateTime.now().minusHours(2));
        session.setExpiresAt(LocalDateTime.now().plusMinutes(5));
        when(sessionRepository.findByToken("tok")).thenReturn(Optional.of(session));
        when(sessionRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        LocalDateTime previousExpiration = session.getExpiresAt();
        SessionEntity result = service.validateSession("tok");

        assertEquals("tok", result.getToken());
        assertTrue(result.getExpiresAt().isAfter(previousExpiration));
        verify(sessionRepository).save(session);
    }

    @Test
    void validateSession_tokenExpirado_removeSessaoELancaExcecao() {
        SessionEntity session = new SessionEntity();
        session.setToken("tok");
        session.setExpiresAt(LocalDateTime.now().minusSeconds(1));
        when(sessionRepository.findByToken("tok")).thenReturn(Optional.of(session));

        ValidationException ex = assertThrows(ValidationException.class,
                () -> service.validateSession("tok"));

        assertEquals("user.error.sessionExpired", ex.getMessage());
        verify(sessionRepository).delete(session);
        verify(sessionRepository, never()).save(any());
    }

    @Test
    void validateSession_tokenInvalido_lancaExcecao() {
        when(sessionRepository.findByToken("invalido")).thenReturn(Optional.empty());

        ValidationException ex = assertThrows(ValidationException.class,
                () -> service.validateSession("invalido"));

        assertEquals("user.error.tokenNotFound", ex.getMessage());
    }

    @Test
    void requestPasswordReset_emailExistente_salvaToken() {
        UserEntity user = activeUser();
        when(userRepository.findByEmail("jogador@email.com")).thenReturn(Optional.of(user));
        when(passwordResetRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        String token = service.requestPasswordReset("jogador@email.com");

        assertNotNull(token);
        verify(passwordResetRepository).save(any(PasswordResetEntity.class));
    }

    @Test
    void requestPasswordReset_emailInexistente_retornaTokenFicticio() {
        when(userRepository.findByEmail("inexistente@email.com")).thenReturn(Optional.empty());

        String token = service.requestPasswordReset("inexistente@email.com");

        assertNotNull(token);
        verify(passwordResetRepository, never()).save(any());
    }

    @Test
    void resetPassword_tokenValido_atualizaSenhaEInvalidaSessoes() {
        PasswordResetEntity reset = new PasswordResetEntity();
        reset.setIdUser(1L);
        reset.setToken("tok");
        reset.setExpiresAt(LocalDateTime.now().plusHours(1));
        reset.setUsed(false);
        when(passwordResetRepository.findByTokenAndUsedFalse("tok")).thenReturn(Optional.of(reset));

        UserEntity user = activeUser();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(passwordResetRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        service.resetPassword("tok", "NovaSenha1!");

        verify(userRepository).save(any());
        verify(sessionRepository).deleteAllByIdUser(1L);
        verify(accountLockRepository).deleteAllByIdUser(1L);
    }

    @Test
    void resetPassword_tokenExpirado_lancaExcecao() {
        PasswordResetEntity reset = new PasswordResetEntity();
        reset.setToken("tok");
        reset.setExpiresAt(LocalDateTime.now().minusHours(1));
        reset.setUsed(false);
        when(passwordResetRepository.findByTokenAndUsedFalse("tok")).thenReturn(Optional.of(reset));

        assertThrows(ValidationException.class, () -> service.resetPassword("tok", "NovaSenha1!"));
    }

    @Test
    void changeUsername_nomeDisponivel_atualizaUsername() {
        UserEntity user = activeUser();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.existsByUsername("novoNome")).thenReturn(false);
        when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        service.changeUsername(1L, "novoNome");

        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    void changeUsername_nomeEmUso_lancaExcecao() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(activeUser()));
        when(userRepository.existsByUsername("novoNome")).thenReturn(true);
        assertThrows(ValidationException.class, () -> service.changeUsername(1L, "novoNome"));
    }

    @Test
    void changePassword_senhaAtualCorreta_atualizaSenha() {
        UserEntity user = activeUser();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        service.changePassword(1L, "Senha123!", "NovaSenha1!");

        verify(userRepository).save(any());
    }

    @Test
    void changePassword_senhaAtualIncorreta_lancaExcecao() {
        UserEntity user = activeUser();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertThrows(ValidationException.class,
                () -> service.changePassword(1L, "SenhaErrada1!", "NovaSenha1!"));
    }

    @Test
    void requestEmailChange_emailDisponivel_salvaConfirmacao() {
        when(userRepository.existsByEmail("novo@email.com")).thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(activeUser()));
        when(emailConfirmationRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        service.requestEmailChange(1L, "novo@email.com");

        ArgumentCaptor<EmailConfirmationEntity> captor = ArgumentCaptor.forClass(EmailConfirmationEntity.class);
        verify(emailConfirmationRepository).save(captor.capture());
        assertEquals(EmailConfirmationType.EMAIL_CHANGE, captor.getValue().getType());
        assertEquals("novo@email.com", captor.getValue().getNewEmail());
    }

    @Test
    void requestEmailChange_emailEmUso_lancaExcecao() {
        when(userRepository.existsByEmail("novo@email.com")).thenReturn(true);
        assertThrows(ValidationException.class, () -> service.requestEmailChange(1L, "novo@email.com"));
    }

    @Test
    void requestAccountDeletion_sem2fa_emiteTokenDeConfirmacao() {
        UserEntity user = activeUser();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(twoFactorAuthRepository.findByIdUser(1L)).thenReturn(Optional.empty());
        when(emailConfirmationRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        String token = service.requestAccountDeletion(1L, null);

        assertNotNull(token);
        ArgumentCaptor<EmailConfirmationEntity> captor = ArgumentCaptor.forClass(EmailConfirmationEntity.class);
        verify(emailConfirmationRepository).save(captor.capture());
        assertEquals(EmailConfirmationType.ACCOUNT_DELETION, captor.getValue().getType());
    }

    @Test
    void requestAccountDeletion_com2faValido_emiteTokenDeConfirmacao() {
        UserEntity user = activeUser();
        TwoFactorAuthEntity tfa = new TwoFactorAuthEntity();
        tfa.setIdUser(1L);
        tfa.setMethod(TwoFactorMethod.EMAIL);
        tfa.setActive(true);
        UnlockCodeEntity challenge = new UnlockCodeEntity();
        challenge.setIdUser(1L);
        challenge.setCode("123456");
        challenge.setExpiresAt(LocalDateTime.now().plusMinutes(10));
        challenge.setUsed(false);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(twoFactorAuthRepository.findByIdUser(1L)).thenReturn(Optional.of(tfa));
        when(unlockCodeRepository.findTopByIdUserAndUsedFalseOrderByExpiresAtDesc(1L))
                .thenReturn(Optional.of(challenge));
        when(unlockCodeRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(emailConfirmationRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        String token = service.requestAccountDeletion(1L, "123456");

        assertNotNull(token);
        assertTrue(challenge.isUsed());
    }

    @Test
    void requestAccountDeletion_com2faInvalido_lancaExcecao() {
        UserEntity user = activeUser();
        TwoFactorAuthEntity tfa = new TwoFactorAuthEntity();
        tfa.setIdUser(1L);
        tfa.setMethod(TwoFactorMethod.EMAIL);
        tfa.setActive(true);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(twoFactorAuthRepository.findByIdUser(1L)).thenReturn(Optional.of(tfa));
        when(unlockCodeRepository.findTopByIdUserAndUsedFalseOrderByExpiresAtDesc(1L)).thenReturn(Optional.empty());
        when(recoveryCodeRepository.findAllByIdUserAndUsedFalse(1L)).thenReturn(Collections.emptyList());

        ValidationException ex = assertThrows(ValidationException.class,
                () -> service.requestAccountDeletion(1L, "000000"));
        assertEquals("user.error.invalidSecondFactorCode", ex.getMessage());
    }

    @Test
    void confirmAccountDeletion_tokenValido_removeTodasAsDependencias() {
        EmailConfirmationEntity confirmation = new EmailConfirmationEntity();
        confirmation.setIdUser(1L);
        confirmation.setToken("delete-token");
        confirmation.setType(EmailConfirmationType.ACCOUNT_DELETION);
        confirmation.setExpiresAt(LocalDateTime.now().plusHours(1));

        UserEntity user = activeUser();
        CharacterEntity character = new CharacterEntity();
        character.setId(10L);
        character.setIdUser(1L);
        when(emailConfirmationRepository.findByToken("delete-token")).thenReturn(Optional.of(confirmation));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(loginAttemptRepository.findByIdUserAndAttemptTimeAfter(eq(1L), any())).thenReturn(Collections.emptyList());
        when(characterRepository.findAllByIdUser(1L)).thenReturn(List.of(character));

        service.confirmAccountDeletion("delete-token");

        verify(characterService).deleteCharacter(1L, 10L);
        verify(sessionRepository).deleteAllByIdUser(1L);
        verify(emailConfirmationRepository).deleteAllByIdUser(1L);
        verify(passwordResetRepository).deleteAllByIdUser(1L);
        verify(accountLockRepository).deleteAllByIdUser(1L);
        verify(twoFactorAuthRepository).deleteAllByIdUser(1L);
        verify(recoveryCodeRepository).deleteAllByIdUser(1L);
        verify(userRepository).delete(user);
    }

    @Test
    void confirmAccountDeletion_tokenExpirado_lancaExcecao() {
        EmailConfirmationEntity confirmation = new EmailConfirmationEntity();
        confirmation.setIdUser(1L);
        confirmation.setToken("delete-token");
        confirmation.setType(EmailConfirmationType.ACCOUNT_DELETION);
        confirmation.setExpiresAt(LocalDateTime.now().minusMinutes(1));

        when(emailConfirmationRepository.findByToken("delete-token")).thenReturn(Optional.of(confirmation));

        assertThrows(ValidationException.class, () -> service.confirmAccountDeletion("delete-token"));
    }

    @Test
    void confirmAccountDeletion_tokenNaoEncontrado_lancaExcecao() {
        when(emailConfirmationRepository.findByToken("delete-token")).thenReturn(Optional.empty());

        assertThrows(ValidationException.class, () -> service.confirmAccountDeletion("delete-token"));
    }

    @Test
    void enableTwoFactor_emailOtp_gera10CodigosDeRecuperacao() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(activeUser()));
        when(twoFactorAuthRepository.findByIdUser(1L)).thenReturn(Optional.empty());
        when(twoFactorAuthRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(recoveryCodeRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        TwoFactorActivationVO activation = service.enableTwoFactor(1L, TwoFactorMethod.EMAIL);

        assertEquals(TwoFactorMethod.EMAIL, activation.method());
        assertEquals(10, activation.recoveryCodes().size());
        assertNull(activation.secret());
        verify(twoFactorAuthRepository).save(any(TwoFactorAuthEntity.class));
    }

    @Test
    void enableTwoFactor_totp_retornaSegredoTextual() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(activeUser()));
        when(twoFactorAuthRepository.findByIdUser(1L)).thenReturn(Optional.empty());
        when(twoFactorAuthRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(recoveryCodeRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        TwoFactorActivationVO activation = service.enableTwoFactor(1L, TwoFactorMethod.TOTP);

        ArgumentCaptor<TwoFactorAuthEntity> captor = ArgumentCaptor.forClass(TwoFactorAuthEntity.class);
        verify(twoFactorAuthRepository).save(captor.capture());
        assertNotNull(captor.getValue().getSecret());
        assertEquals(captor.getValue().getSecret(), activation.secret());
        assertEquals(10, activation.recoveryCodes().size());
    }

    @Test
    void enableTwoFactor_substituiConfigAnterior() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(activeUser()));
        TwoFactorAuthEntity existing = new TwoFactorAuthEntity();
        existing.setIdUser(1L);
        when(twoFactorAuthRepository.findByIdUser(1L)).thenReturn(Optional.of(existing));
        when(twoFactorAuthRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(recoveryCodeRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        service.enableTwoFactor(1L, TwoFactorMethod.EMAIL);

        verify(twoFactorAuthRepository).delete(existing);
        verify(recoveryCodeRepository).deleteAllByIdUser(1L);
    }

    @Test
    void regenerateRecoveryCodes_invalidaAnterioresEGeraNovos() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(activeUser()));
        TwoFactorAuthEntity tfa = new TwoFactorAuthEntity();
        tfa.setIdUser(1L);
        tfa.setMethod(TwoFactorMethod.EMAIL);
        tfa.setActive(true);
        when(twoFactorAuthRepository.findByIdUser(1L)).thenReturn(Optional.of(tfa));
        when(recoveryCodeRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        List<String> codes = service.regenerateRecoveryCodes(1L);

        assertEquals(10, codes.size());
        verify(recoveryCodeRepository).deleteAllByIdUser(1L);
        verify(recoveryCodeRepository, times(10)).save(any(RecoveryCodeEntity.class));
    }

    @Test
    void disableTwoFactor_ativoComCodigoValido_removeTfaECodigos() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(activeUser()));
        TwoFactorAuthEntity tfa = new TwoFactorAuthEntity();
        tfa.setIdUser(1L);
        tfa.setMethod(TwoFactorMethod.EMAIL);
        tfa.setActive(true);
        UnlockCodeEntity challenge = new UnlockCodeEntity();
        challenge.setIdUser(1L);
        challenge.setCode("654321");
        challenge.setExpiresAt(LocalDateTime.now().plusMinutes(10));
        challenge.setUsed(false);
        when(twoFactorAuthRepository.findByIdUser(1L)).thenReturn(Optional.of(tfa));
        when(unlockCodeRepository.findTopByIdUserAndUsedFalseOrderByExpiresAtDesc(1L))
                .thenReturn(Optional.of(challenge));
        when(unlockCodeRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        service.disableTwoFactor(1L, "654321");

        verify(twoFactorAuthRepository).delete(tfa);
        verify(recoveryCodeRepository).deleteAllByIdUser(1L);
        assertTrue(challenge.isUsed());
    }

    @Test
    void disableTwoFactor_codigoInvalido_lancaExcecao() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(activeUser()));
        TwoFactorAuthEntity tfa = new TwoFactorAuthEntity();
        tfa.setIdUser(1L);
        tfa.setMethod(TwoFactorMethod.EMAIL);
        tfa.setActive(true);
        when(twoFactorAuthRepository.findByIdUser(1L)).thenReturn(Optional.of(tfa));
        when(unlockCodeRepository.findTopByIdUserAndUsedFalseOrderByExpiresAtDesc(1L)).thenReturn(Optional.empty());
        when(recoveryCodeRepository.findAllByIdUserAndUsedFalse(1L)).thenReturn(Collections.emptyList());

        ValidationException ex = assertThrows(ValidationException.class,
                () -> service.disableTwoFactor(1L, "000000"));
        assertEquals("user.error.invalidSecondFactorCode", ex.getMessage());
    }

    @Test
    void disableTwoFactor_naoAtivo_lancaExcecao() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(activeUser()));
        when(twoFactorAuthRepository.findByIdUser(1L)).thenReturn(Optional.empty());

        assertThrows(ValidationException.class, () -> service.disableTwoFactor(1L, "000000"));
    }

    @Test
    void validateTotpCode_codigoInvalido_retornaFalso() {
        TwoFactorAuthEntity tfa = new TwoFactorAuthEntity();
        tfa.setMethod(TwoFactorMethod.EMAIL);
        tfa.setActive(true);
        when(twoFactorAuthRepository.findByIdUser(1L)).thenReturn(Optional.of(tfa));
        when(unlockCodeRepository.findTopByIdUserAndUsedFalseOrderByExpiresAtDesc(1L)).thenReturn(Optional.empty());
        when(recoveryCodeRepository.findAllByIdUserAndUsedFalse(1L)).thenReturn(Collections.emptyList());

        assertFalse(service.validateTotpCode(1L, "CODIGOERRADO"));
    }

    @Test
    void validateTotpCode_codigoRecuperacaoValido_invalidaUso() {
        TwoFactorAuthEntity tfa = new TwoFactorAuthEntity();
        tfa.setMethod(TwoFactorMethod.TOTP);
        tfa.setSecret("AAAAAAAAAAAAAAAAAAAAAAAAAAA=");
        tfa.setActive(true);
        when(twoFactorAuthRepository.findByIdUser(1L)).thenReturn(Optional.of(tfa));

        RecoveryCodeEntity code = new RecoveryCodeEntity();
        code.setIdUser(1L);
        code.setCodeHash("ff664cc51251778d99d96f5948ae09bb440c6d0d45610c315b69b0a67d64daa8");
        code.setUsed(false);
        when(recoveryCodeRepository.findAllByIdUserAndUsedFalse(1L)).thenReturn(List.of(code));
        when(recoveryCodeRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        assertTrue(service.validateTotpCode(1L, "RECOVERY1"));
        assertTrue(code.isUsed());
    }

    @Test
    void validateTotpCode_tfaNaoHabilitado_lancaExcecao() {
        when(twoFactorAuthRepository.findByIdUser(1L)).thenReturn(Optional.empty());
        assertThrows(ValidationException.class, () -> service.validateTotpCode(1L, "123456"));
    }

    @Test
    void unlockAccount_codigoValido_removeBloqueioEInvalidaCodigo() {
        UserEntity user = activeUser();
        user.setStatus(UserStatus.LOCKED);
        AccountLockEntity lock = new AccountLockEntity();
        lock.setIdUser(1L);
        lock.setUnlockAt(LocalDateTime.now().plusMinutes(15));
        UnlockCodeEntity code = new UnlockCodeEntity();
        code.setIdUser(1L);
        code.setCode("654321");
        code.setExpiresAt(LocalDateTime.now().plusMinutes(15));
        code.setUsed(false);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(accountLockRepository.findTopByIdUserOrderByLockedAtDesc(1L)).thenReturn(Optional.of(lock));
        when(unlockCodeRepository.findTopByIdUserAndUsedFalseOrderByExpiresAtDesc(1L)).thenReturn(Optional.of(code));
        when(unlockCodeRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        assertDoesNotThrow(() -> service.unlockAccount(1L, "654321"));

        assertTrue(code.isUsed());
        assertEquals(UserStatus.ACTIVE, user.getStatus());
        verify(accountLockRepository).deleteAllByIdUser(1L);
    }

    @Test
    void unlockAccount_codigoNaoEncontrado_lancaExcecao() {
        UserEntity user = activeUser();
        user.setStatus(UserStatus.LOCKED);
        AccountLockEntity lock = new AccountLockEntity();
        lock.setIdUser(1L);
        lock.setUnlockAt(LocalDateTime.now().plusMinutes(15));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(accountLockRepository.findTopByIdUserOrderByLockedAtDesc(1L)).thenReturn(Optional.of(lock));
        when(unlockCodeRepository.findTopByIdUserAndUsedFalseOrderByExpiresAtDesc(1L)).thenReturn(Optional.empty());

        ValidationException ex = assertThrows(ValidationException.class,
                () -> service.unlockAccount(1L, "654321"));
        assertEquals("user.error.unlockCodeNotFound", ex.getMessage());
    }

    @Test
    void unlockAccount_codigoInvalido_lancaExcecao() {
        UserEntity user = activeUser();
        user.setStatus(UserStatus.LOCKED);
        AccountLockEntity lock = new AccountLockEntity();
        lock.setIdUser(1L);
        lock.setUnlockAt(LocalDateTime.now().plusMinutes(15));
        UnlockCodeEntity code = new UnlockCodeEntity();
        code.setIdUser(1L);
        code.setCode("654321");
        code.setExpiresAt(LocalDateTime.now().plusMinutes(15));
        code.setUsed(false);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(accountLockRepository.findTopByIdUserOrderByLockedAtDesc(1L)).thenReturn(Optional.of(lock));
        when(unlockCodeRepository.findTopByIdUserAndUsedFalseOrderByExpiresAtDesc(1L)).thenReturn(Optional.of(code));

        ValidationException ex = assertThrows(ValidationException.class,
                () -> service.unlockAccount(1L, "111111"));
        assertEquals("user.error.unlockCodeInvalid", ex.getMessage());
    }

    private UserEntity activeUser() {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setUsername("jogador1");
        user.setEmail("jogador@email.com");
        user.setPasswordHash("e8d3af75ae4bc94a8632b75fa79d56e600e9efce2b4f1dcdecd4713d29400a47");
        user.setEmailConfirmed(true);
        user.setStatus(UserStatus.ACTIVE);
        user.setCreatedAt(LocalDateTime.now());
        return user;
    }

    private LoginAttemptEntity failedAttempt() {
        LoginAttemptEntity attempt = new LoginAttemptEntity();
        attempt.setSuccess(false);
        attempt.setAttemptTime(LocalDateTime.now());
        return attempt;
    }

    private LoginAttemptEntity successfulAttempt() {
        LoginAttemptEntity attempt = new LoginAttemptEntity();
        attempt.setSuccess(true);
        attempt.setAttemptTime(LocalDateTime.now());
        return attempt;
    }
}
