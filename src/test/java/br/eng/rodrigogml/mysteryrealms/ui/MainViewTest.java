package br.eng.rodrigogml.mysteryrealms.ui;

import br.eng.rodrigogml.mysteryrealms.application.character.service.CharacterService;
import br.eng.rodrigogml.mysteryrealms.application.user.entity.SessionEntity;
import br.eng.rodrigogml.mysteryrealms.application.user.entity.TwoFactorMethod;
import br.eng.rodrigogml.mysteryrealms.application.user.service.LoginResultVO;
import br.eng.rodrigogml.mysteryrealms.application.user.service.UserService;
import br.eng.rodrigogml.mysteryrealms.common.error.ErrorMapperService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.function.DeploymentConfiguration;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinSession;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.support.StaticMessageSource;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MainViewTest {

    private static class TestVaadinSession extends VaadinSession {
        private final ReentrantLock lock = new ReentrantLock();

        TestVaadinSession(VaadinService service) {
            super(service);
        }

        @Override
        public Lock getLockInstance() {
            return lock;
        }

        @Override
        public void lock() {
            lock.lock();
        }

        @Override
        public void unlock() {
            lock.unlock();
        }
    }

    @Mock private UserService userService;
    @Mock private CharacterService characterService;
    @Mock private ErrorMapperService errorMapperService;

    private VaadinSession session;
    private UI ui;

    @BeforeEach
    void setUp() {
        VaadinService service = mock(VaadinService.class);
        DeploymentConfiguration configuration = mock(DeploymentConfiguration.class);
        when(configuration.isProductionMode()).thenReturn(true);
        when(service.getDeploymentConfiguration()).thenReturn(configuration);
        session = new TestVaadinSession(service);
        session.lock();
        VaadinSession.setCurrent(session);
        ui = new UI();
        ui.getInternals().setSession(session);
        UI.setCurrent(ui);
    }

    @AfterEach
    void tearDown() {
        session.unlock();
        UI.setCurrent(null);
        VaadinSession.setCurrent(null);
    }

    @Test
    void login_comSegundoFator_confirmaCodigoEAutenticaSessao() throws Exception {
        LoginResultVO secondFactor = LoginResultVO.secondFactorRequired(1L, TwoFactorMethod.EMAIL, "123456");
        SessionEntity authenticated = new SessionEntity();
        authenticated.setIdUser(1L);
        authenticated.setToken("tok-2fa");
        when(userService.completeTwoFactorLogin(1L, "123456")).thenReturn(authenticated);
        when(characterService.listCharacterSummaries(1L)).thenReturn(Collections.emptyList());

        MainView view = new MainView(userService, characterService, messages(), errorMapperService);

        Dialog dialog = view.openSecondFactorDialog(secondFactor, "jogador@email.com", "Senha123!");
        textField(dialog, "Authentication code").setValue("123456");
        button(dialog, "Confirm").click();

        assertEquals("tok-2fa", session.getAttribute(sessionTokenAttribute()));
        verify(userService).completeTwoFactorLogin(1L, "123456");
    }


    @Test
    void login_comSegundoFator_codigoInvalido_marcaCampoComErro() {
        LoginResultVO secondFactor = LoginResultVO.secondFactorRequired(1L, TwoFactorMethod.EMAIL, "123456");
        when(userService.completeTwoFactorLogin(1L, "000000"))
                .thenThrow(new RuntimeException("invalid second factor"));

        MainView view = new MainView(userService, characterService, messages(), errorMapperService);

        Dialog dialog = view.openSecondFactorDialog(secondFactor, "jogador@email.com", "Senha123!");
        TextField code = textField(dialog, "Authentication code");
        code.setValue("000000");
        button(dialog, "Confirm").click();

        assertTrue(code.isInvalid());
        assertEquals("Invalid code", code.getErrorMessage());
    }

    @Test
    void login_comSegundoFator_emailPermiteReenvioDeCodigo() {
        LoginResultVO secondFactor = LoginResultVO.secondFactorRequired(1L, TwoFactorMethod.EMAIL, "123456");
        LoginResultVO resent = LoginResultVO.secondFactorRequired(1L, TwoFactorMethod.EMAIL, "654321");
        when(userService.login("jogador@email.com", "Senha123!", "0.0.0.0")).thenReturn(resent);

        MainView view = new MainView(userService, characterService, messages(), errorMapperService);

        Dialog dialog = view.openSecondFactorDialog(secondFactor, "jogador@email.com", "Senha123!");
        Button resend = button(dialog, "Resend code");

        assertTrue(resend.isVisible());
        resend.click();

        verify(userService).login("jogador@email.com", "Senha123!", "0.0.0.0");
    }

    @Test
    void login_comSegundoFatorTotp_naoExibeReenvio() {
        LoginResultVO secondFactor = LoginResultVO.secondFactorRequired(1L, TwoFactorMethod.TOTP, null);

        MainView view = new MainView(userService, characterService, messages(), errorMapperService);

        Dialog dialog = view.openSecondFactorDialog(secondFactor, "jogador@email.com", "Senha123!");

        assertFalse(button(dialog, "Resend code").isVisible());
    }

    private StaticMessageSource messages() {
        StaticMessageSource messages = new StaticMessageSource();
        messages.addMessage("ui.app.title", Locale.ENGLISH, "Mystery Realms");
        messages.addMessage("ui.access.subtitle", Locale.ENGLISH, "Access");
        messages.addMessage("ui.access.registerTab", Locale.ENGLISH, "Register");
        messages.addMessage("ui.access.loginTab", Locale.ENGLISH, "Login");
        messages.addMessage("ui.register.title", Locale.ENGLISH, "Register");
        messages.addMessage("ui.register.username", Locale.ENGLISH, "Username");
        messages.addMessage("ui.register.email", Locale.ENGLISH, "Register email");
        messages.addMessage("ui.register.password", Locale.ENGLISH, "Register password");
        messages.addMessage("ui.register.submit", Locale.ENGLISH, "Create account");
        messages.addMessage("ui.login.title", Locale.ENGLISH, "Login");
        messages.addMessage("ui.login.email", Locale.ENGLISH, "Email");
        messages.addMessage("ui.login.password", Locale.ENGLISH, "Password");
        messages.addMessage("ui.login.submit", Locale.ENGLISH, "Login");
        messages.addMessage("ui.login.secondFactorRequired", Locale.ENGLISH, "Enter code");
        messages.addMessage("ui.login.secondFactorTitle", Locale.ENGLISH, "Two-factor authentication");
        messages.addMessage("ui.login.secondFactorCode", Locale.ENGLISH, "Authentication code");
        messages.addMessage("ui.login.secondFactorConfirm", Locale.ENGLISH, "Confirm");
        messages.addMessage("ui.login.secondFactorInvalidCode", Locale.ENGLISH, "Invalid code");
        messages.addMessage("ui.login.secondFactorResend", Locale.ENGLISH, "Resend code");
        messages.addMessage("ui.login.secondFactorResent", Locale.ENGLISH, "Code resent");
        messages.addMessage("ui.logout.submit", Locale.ENGLISH, "Logout");
        messages.addMessage("ui.game.ready", Locale.ENGLISH, "Authenticated user {0}.");
        messages.addMessage("ui.character.title", Locale.ENGLISH, "Characters");
        messages.addMessage("ui.character.createAndSelect", Locale.ENGLISH, "Create");
        messages.addMessage("ui.character.filterName", Locale.ENGLISH, "Filter");
        messages.addMessage("ui.character.sortBy", Locale.ENGLISH, "Sort");
        messages.addMessage("ui.character.emptyState", Locale.ENGLISH, "No characters");
        return messages;
    }

    private TextField textField(Component root, String label) {
        return descendants(root)
                .filter(TextField.class::isInstance)
                .map(TextField.class::cast)
                .filter(field -> label.equals(field.getLabel()))
                .findFirst()
                .orElseThrow();
    }

    private Button button(Component root, String text) {
        return descendants(root)
                .filter(Button.class::isInstance)
                .map(Button.class::cast)
                .filter(button -> text.equals(button.getText()))
                .findFirst()
                .orElseThrow();
    }

    private Stream<Component> descendants(Component component) {
        Stream<Component> children = component.getChildren().flatMap(this::descendants);
        if (component instanceof Dialog dialog) {
            children = Stream.concat(children, dialog.getFooter().getElement().getChildren()
                    .map(Element::getComponent)
                    .flatMap(Optional::stream)
                    .flatMap(this::descendants));
        }
        return Stream.concat(Stream.of(component), children);
    }

    private String sessionTokenAttribute() throws Exception {
        Field field = MainView.class.getDeclaredField("SESSION_TOKEN_ATTRIBUTE");
        field.setAccessible(true);
        return (String) field.get(null);
    }
}
