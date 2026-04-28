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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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

    private UserService service;

    @BeforeEach
    void setUp() {
        service = new UserService(userRepository, sessionRepository, loginAttemptRepository,
                accountLockRepository, unlockCodeRepository, emailConfirmationRepository,
                passwordResetRepository, twoFactorAuthRepository, recoveryCodeRepository);
    }

    // ── RF-UA-01: Cadastro de usuário ────────────────────────────────────────

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
    }

    @Test
    void register_nomeUsuarioJaExiste_lancaExcecao() {
        when(userRepository.existsByUsername("jogador1")).thenReturn(true);
        assertThrows(IllegalArgumentException.class,
                () -> service.register("jogador1", "jogador@email.com", "Senha123!"));
    }

    @Test
    void register_emailJaExiste_lancaExcecao() {
        when(userRepository.existsByUsername("jogador1")).thenReturn(false);
        when(userRepository.existsByEmail("jogador@email.com")).thenReturn(true);
        assertThrows(IllegalArgumentException.class,
                () -> service.register("jogador1", "jogador@email.com", "Senha123!"));
    }

    @Test
    void register_nomeUsuarioMenorQue3_lancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> service.register("ab", "jogador@email.com", "Senha123!"));
    }

    @Test
    void register_nomeUsuarioComEspaco_lancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> service.register("jo gador", "jogador@email.com", "Senha123!"));
    }

    @Test
    void register_emailInvalido_lancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> service.register("jogador1", "emailinvalido", "Senha123!"));
    }

    @Test
    void register_senhaFraca_lancaExcecao() {
        assertThrows(IllegalArgumentException.class,
                () -> service.register("jogador1", "jogador@email.com", "senhafraca"));
    }

    @Test
    void register_senhaCurtaDemais_lancaExcecao() {
        assertThrows(IllegalArgumentException.class,
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
    void confirmEmail_tokenNaoEncontrado_lancaExcecao() {
        when(emailConfirmationRepository.findByToken("invalido")).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> service.confirmEmail("invalido"));
    }

    @Test
    void confirmEmail_tokenExpirado_lancaExcecao() {
        EmailConfirmationEntity conf = new EmailConfirmationEntity();
        conf.setIdUser(1L);
        conf.setToken("tok");
        conf.setExpiresAt(LocalDateTime.now().minusHours(1));
        when(emailConfirmationRepository.findByToken("tok")).thenReturn(Optional.of(conf));
        assertThrows(IllegalArgumentException.class, () -> service.confirmEmail("tok"));
    }

    // ── RF-UA-02: Autenticação ────────────────────────────────────────────────

    @Test
    void login_credenciaisValidas_retornaSessao() {
        UserEntity user = activeUser();
        when(userRepository.findByEmail("jogador@email.com")).thenReturn(Optional.of(user));
        when(accountLockRepository.findTopByIdUserOrderByLockedAtDesc(1L)).thenReturn(Optional.empty());
        when(loginAttemptRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        SessionEntity session = new SessionEntity();
        session.setToken("tok");
        when(sessionRepository.save(any())).thenReturn(session);

        SessionEntity result = service.login("jogador@email.com", "Senha123!", "127.0.0.1");

        assertNotNull(result.getToken());
    }

    @Test
    void login_senhaIncorreta_lancaExcecao() {
        UserEntity user = activeUser();
        when(userRepository.findByEmail("jogador@email.com")).thenReturn(Optional.of(user));
        when(accountLockRepository.findTopByIdUserOrderByLockedAtDesc(1L)).thenReturn(Optional.empty());
        when(loginAttemptRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(loginAttemptRepository.countByIdUserAndSuccessFalseAndAttemptTimeAfter(eq(1L), any())).thenReturn(0L);

        assertThrows(IllegalArgumentException.class,
                () -> service.login("jogador@email.com", "SenhaErrada1!", "127.0.0.1"));
    }

    @Test
    void login_contaBloqueada_lancaExcecao() {
        UserEntity user = activeUser();
        when(userRepository.findByEmail("jogador@email.com")).thenReturn(Optional.of(user));

        AccountLockEntity lock = new AccountLockEntity();
        lock.setUnlockAt(LocalDateTime.now().plusMinutes(20));
        when(accountLockRepository.findTopByIdUserOrderByLockedAtDesc(1L)).thenReturn(Optional.of(lock));

        assertThrows(IllegalArgumentException.class,
                () -> service.login("jogador@email.com", "Senha123!", "127.0.0.1"));
    }

    @Test
    void login_usuarioNaoAtivo_lancaExcecao() {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setStatus(UserStatus.PENDING_CONFIRMATION);
        when(userRepository.findByEmail("jogador@email.com")).thenReturn(Optional.of(user));

        assertThrows(IllegalArgumentException.class,
                () -> service.login("jogador@email.com", "Senha123!", "127.0.0.1"));
    }

    @Test
    void checkBruteForce_apos5Falhas_bloqueiaContaELancaExcecao() {
        UserEntity user = activeUser();
        when(userRepository.findByEmail("jogador@email.com")).thenReturn(Optional.of(user));
        when(accountLockRepository.findTopByIdUserOrderByLockedAtDesc(1L)).thenReturn(Optional.empty());
        when(loginAttemptRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(loginAttemptRepository.countByIdUserAndSuccessFalseAndAttemptTimeAfter(eq(1L), any())).thenReturn(5L);

        assertThrows(IllegalArgumentException.class,
                () -> service.login("jogador@email.com", "SenhaErrada1!", "127.0.0.1"));
        verify(accountLockRepository).save(any(AccountLockEntity.class));
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
        assertThrows(IllegalArgumentException.class, () -> service.logout("invalido"));
    }

    // ── RF-UA-03: Recuperação de senha ───────────────────────────────────────

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

        assertThrows(IllegalArgumentException.class, () -> service.resetPassword("tok", "NovaSenha1!"));
    }

    // ── RF-UA-04: Perfil do usuário ──────────────────────────────────────────

    @Test
    void changeUsername_nomeDisponivel_atualizaUsername() {
        UserEntity user = activeUser();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.existsByUsername("novoNome")).thenReturn(false);
        when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        service.changeUsername(1L, "novoNome");

        verify(userRepository).save(argThat(u -> "novoNome".equals(u.getUsername())));
    }

    @Test
    void changeUsername_nomeEmUso_lancaExcecao() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(activeUser()));
        when(userRepository.existsByUsername("novoNome")).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> service.changeUsername(1L, "novoNome"));
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

        assertThrows(IllegalArgumentException.class,
                () -> service.changePassword(1L, "SenhaErrada1!", "NovaSenha1!"));
    }

    @Test
    void requestEmailChange_emailDisponivel_salvaConfirmacao() {
        when(userRepository.existsByEmail("novo@email.com")).thenReturn(false);
        UserEntity user = activeUser();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
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
        assertThrows(IllegalArgumentException.class, () -> service.requestEmailChange(1L, "novo@email.com"));
    }

    // ── RF-UA-05: Exclusão de conta ──────────────────────────────────────────

    @Test
    void deleteAccount_usuarioExistente_removeTodasAsDependencias() {
        UserEntity user = activeUser();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(loginAttemptRepository.findByIdUserAndAttemptTimeAfter(eq(1L), any())).thenReturn(Collections.emptyList());

        service.deleteAccount(1L);

        verify(sessionRepository).deleteAllByIdUser(1L);
        verify(emailConfirmationRepository).deleteAllByIdUser(1L);
        verify(passwordResetRepository).deleteAllByIdUser(1L);
        verify(accountLockRepository).deleteAllByIdUser(1L);
        verify(twoFactorAuthRepository).deleteAllByIdUser(1L);
        verify(recoveryCodeRepository).deleteAllByIdUser(1L);
        verify(userRepository).delete(user);
    }

    @Test
    void deleteAccount_usuarioNaoEncontrado_lancaExcecao() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> service.deleteAccount(99L));
    }

    // ── RF-UA-06: Autenticação de dois fatores ───────────────────────────────

    @Test
    void enableTwoFactor_emailOtp_gera10CodigosDeRecuperacao() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(activeUser()));
        when(twoFactorAuthRepository.findByIdUser(1L)).thenReturn(Optional.empty());
        when(twoFactorAuthRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(recoveryCodeRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        List<String> codes = service.enableTwoFactor(1L, TwoFactorMethod.EMAIL);

        assertEquals(10, codes.size());
        verify(twoFactorAuthRepository).save(any(TwoFactorAuthEntity.class));
    }

    @Test
    void enableTwoFactor_totp_geraSegreto() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(activeUser()));
        when(twoFactorAuthRepository.findByIdUser(1L)).thenReturn(Optional.empty());
        when(twoFactorAuthRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(recoveryCodeRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        service.enableTwoFactor(1L, TwoFactorMethod.TOTP);

        ArgumentCaptor<TwoFactorAuthEntity> captor = ArgumentCaptor.forClass(TwoFactorAuthEntity.class);
        verify(twoFactorAuthRepository).save(captor.capture());
        assertNotNull(captor.getValue().getSecret());
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
    void disableTwoFactor_ativo_removeTfaECodigos() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(activeUser()));
        TwoFactorAuthEntity tfa = new TwoFactorAuthEntity();
        tfa.setIdUser(1L);
        when(twoFactorAuthRepository.findByIdUser(1L)).thenReturn(Optional.of(tfa));

        service.disableTwoFactor(1L);

        verify(twoFactorAuthRepository).delete(tfa);
        verify(recoveryCodeRepository).deleteAllByIdUser(1L);
    }

    @Test
    void disableTwoFactor_naoAtivo_lancaExcecao() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(activeUser()));
        when(twoFactorAuthRepository.findByIdUser(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> service.disableTwoFactor(1L));
    }

    @Test
    void validateTotpCode_codigoInvalido_retornaFalso() {
        TwoFactorAuthEntity tfa = new TwoFactorAuthEntity();
        tfa.setMethod(TwoFactorMethod.EMAIL);
        when(twoFactorAuthRepository.findByIdUser(1L)).thenReturn(Optional.of(tfa));
        when(recoveryCodeRepository.findAllByIdUserAndUsedFalse(1L)).thenReturn(Collections.emptyList());

        assertFalse(service.validateTotpCode(1L, "CODIGOERRADO"));
    }

    @Test
    void validateTotpCode_tfaNaoHabilitado_lancaExcecao() {
        when(twoFactorAuthRepository.findByIdUser(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> service.validateTotpCode(1L, "123456"));
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    /**
     * Cria um usuário ativo com senha "Senha123!" já hasheada (SHA-256).
     */
    private UserEntity activeUser() {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setUsername("jogador1");
        user.setEmail("jogador@email.com");
        // SHA-256 de "Senha123!"
        user.setPasswordHash("e8d3af75ae4bc94a8632b75fa79d56e600e9efce2b4f1dcdecd4713d29400a47");
        user.setEmailConfirmed(true);
        user.setStatus(UserStatus.ACTIVE);
        user.setCreatedAt(LocalDateTime.now());
        return user;
    }
}
