package br.eng.rodrigogml.mysteryrealms.ui;

import br.eng.rodrigogml.mysteryrealms.application.character.dto.CharacterCreationDTO;
import br.eng.rodrigogml.mysteryrealms.application.character.dto.CharacterSelectionDTO;
import br.eng.rodrigogml.mysteryrealms.application.character.service.CharacterService;
import br.eng.rodrigogml.mysteryrealms.application.game.dto.GameActionDTO;
import br.eng.rodrigogml.mysteryrealms.application.game.dto.GameSnapshotDTO;
import br.eng.rodrigogml.mysteryrealms.application.game.service.GameSessionService;
import br.eng.rodrigogml.mysteryrealms.application.user.entity.SessionEntity;
import br.eng.rodrigogml.mysteryrealms.application.user.entity.TwoFactorMethod;
import br.eng.rodrigogml.mysteryrealms.application.user.service.LoginResultVO;
import br.eng.rodrigogml.mysteryrealms.application.user.service.UserService;
import br.eng.rodrigogml.mysteryrealms.common.error.ErrorMapperService;
import br.eng.rodrigogml.mysteryrealms.common.error.ErrorResponseVO;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.textfield.IntegerField;
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
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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

    private static class RecordingUI extends UI {
        private String navigatedRoute;

        @Override
        public void navigate(String location) {
            this.navigatedRoute = location;
        }
    }

    private VaadinSession session;
    private RecordingUI ui;

    @BeforeEach
    void setUp() {
        VaadinService service = mock(VaadinService.class);
        DeploymentConfiguration configuration = mock(DeploymentConfiguration.class);
        when(configuration.isProductionMode()).thenReturn(true);
        when(service.getDeploymentConfiguration()).thenReturn(configuration);
        session = new TestVaadinSession(service);
        session.lock();
        VaadinSession.setCurrent(session);
        ui = new RecordingUI();
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
    void criacaoPersonagem_autenticado_armazenaSelecaoENavegaParaGame() {
        SessionEntity authenticated = authenticatedSession();
        session.setAttribute(UiSessionAttributes.AUTH_TOKEN, authenticated.getToken());
        when(userService.validateSession(authenticated.getToken())).thenReturn(authenticated);
        when(characterService.listCharacterSummaries(1L)).thenReturn(Collections.emptyList());
        when(characterService.createAndSelectCharacterForGame(eq(1L), any(CharacterCreationDTO.class)))
                .thenReturn(selection(10L, 20L, "zona_langur_praca_das_vozes"));

        MainView view = new MainView(userService, characterService, messages(), errorMapperService);

        Dialog wizard = view.openCreateWizardDialog(1L);
        textField(wizard, "Name").setValue("Arlen");
        textField(wizard, "Surname").setValue("Vale");
        integerField(wizard, "Initial age").setValue(24);
        button(wizard, "Next").click();
        button(wizard, "Confirm creation").click();

        assertEquals(10L, session.getAttribute(UiSessionAttributes.SELECTED_CHARACTER_ID));
        assertEquals("game", ui.navigatedRoute);

        GameSessionService gameSessionService = mock(GameSessionService.class);
        when(gameSessionService.loadSnapshotForSelectedCharacter("tok-auth", 10L)).thenReturn(snapshot());
        GameView gameView = new GameView(gameSessionService, messages(), errorMapperService);

        assertEquals("Current location: zona_langur_praca_das_vozes",
                paragraph(gameView, "Current location").getText());
        verify(characterService).createAndSelectCharacterForGame(eq(1L), any(CharacterCreationDTO.class));
        verify(gameSessionService).loadSnapshotForSelectedCharacter("tok-auth", 10L);
    }

    @Test
    void criacaoPersonagem_quandoInstanciaMundoAusente_naoArmazenaSelecaoNemNavega() {
        SessionEntity authenticated = authenticatedSession();
        session.setAttribute(UiSessionAttributes.AUTH_TOKEN, authenticated.getToken());
        when(userService.validateSession(authenticated.getToken())).thenReturn(authenticated);
        when(characterService.listCharacterSummaries(1L)).thenReturn(Collections.emptyList());
        when(characterService.createAndSelectCharacterForGame(eq(1L), any(CharacterCreationDTO.class)))
                .thenReturn(selection(10L, null, "zona_langur_praca_das_vozes"));
        when(errorMapperService.map(any(RuntimeException.class)))
                .thenReturn(new ErrorResponseVO("VALIDATION", "character.error.worldInstanceNotFound", "trace-world"));

        MainView view = new MainView(userService, characterService, messages(), errorMapperService);

        confirmarCriacaoValida(view);

        assertEquals(null, session.getAttribute(UiSessionAttributes.SELECTED_CHARACTER_ID));
        assertEquals(null, ui.navigatedRoute);
    }

    @Test
    void criacaoPersonagem_quandoLocalidadeInicialAusente_naoArmazenaSelecaoNemNavega() {
        SessionEntity authenticated = authenticatedSession();
        session.setAttribute(UiSessionAttributes.AUTH_TOKEN, authenticated.getToken());
        when(userService.validateSession(authenticated.getToken())).thenReturn(authenticated);
        when(characterService.listCharacterSummaries(1L)).thenReturn(Collections.emptyList());
        when(characterService.createAndSelectCharacterForGame(eq(1L), any(CharacterCreationDTO.class)))
                .thenReturn(selection(10L, 20L, null));
        when(errorMapperService.map(any(RuntimeException.class)))
                .thenReturn(new ErrorResponseVO("VALIDATION", "world.error.currentLocationStateNotFound", "trace-location"));

        MainView view = new MainView(userService, characterService, messages(), errorMapperService);

        confirmarCriacaoValida(view);

        assertEquals(null, session.getAttribute(UiSessionAttributes.SELECTED_CHARACTER_ID));
        assertEquals(null, ui.navigatedRoute);
    }

    @Test
    void criacaoPersonagem_quandoLocalidadeInicialVazia_naoArmazenaSelecaoNemNavega() {
        SessionEntity authenticated = authenticatedSession();
        session.setAttribute(UiSessionAttributes.AUTH_TOKEN, authenticated.getToken());
        when(userService.validateSession(authenticated.getToken())).thenReturn(authenticated);
        when(characterService.listCharacterSummaries(1L)).thenReturn(Collections.emptyList());
        when(characterService.createAndSelectCharacterForGame(eq(1L), any(CharacterCreationDTO.class)))
                .thenReturn(selection(10L, 20L, "   "));
        when(errorMapperService.map(any(RuntimeException.class)))
                .thenReturn(new ErrorResponseVO("VALIDATION", "world.error.currentLocationStateNotFound", "trace-location"));

        MainView view = new MainView(userService, characterService, messages(), errorMapperService);

        confirmarCriacaoValida(view);

        assertEquals(null, session.getAttribute(UiSessionAttributes.SELECTED_CHARACTER_ID));
        assertEquals(null, ui.navigatedRoute);
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
        messages.addMessage("ui.game.title", Locale.ENGLISH, "Adventure");
        messages.addMessage("ui.game.characterName", Locale.ENGLISH, "Character: {0}");
        messages.addMessage("ui.game.currentLocation", Locale.ENGLISH, "Current location: {0}");
        messages.addMessage("ui.game.currentTime", Locale.ENGLISH, "Current time: {0}");
        messages.addMessage("ui.game.locationUnknown", Locale.ENGLISH, "Unknown location");
        messages.addMessage("ui.game.contentUnavailable", Locale.ENGLISH, "No content");
        messages.addMessage("ui.game.actionExplore", Locale.ENGLISH, "Explore");
        messages.addMessage("ui.game.backToCharacters", Locale.ENGLISH, "Back to characters");
        messages.addMessage("ui.game.timeFormat", Locale.ENGLISH, "Day {0}, {1}:{2,number,00}");
        messages.addMessage("ui.character.title", Locale.ENGLISH, "Characters");
        messages.addMessage("ui.character.createAndSelect", Locale.ENGLISH, "Create");
        messages.addMessage("ui.character.filterName", Locale.ENGLISH, "Filter");
        messages.addMessage("ui.character.sortBy", Locale.ENGLISH, "Sort");
        messages.addMessage("ui.character.emptyState", Locale.ENGLISH, "No characters");
        messages.addMessage("ui.character.wizardTitle", Locale.ENGLISH, "New character");
        messages.addMessage("ui.character.name", Locale.ENGLISH, "Name");
        messages.addMessage("ui.character.surname", Locale.ENGLISH, "Surname");
        messages.addMessage("ui.character.gender", Locale.ENGLISH, "Gender");
        messages.addMessage("ui.character.initialAge", Locale.ENGLISH, "Initial age");
        messages.addMessage("ui.character.race", Locale.ENGLISH, "Race");
        messages.addMessage("ui.character.class", Locale.ENGLISH, "Class");
        messages.addMessage("ui.character.cancel", Locale.ENGLISH, "Cancel");
        messages.addMessage("ui.character.wizardNext", Locale.ENGLISH, "Next");
        messages.addMessage("ui.character.wizardBack", Locale.ENGLISH, "Back");
        messages.addMessage("ui.character.wizardConfirm", Locale.ENGLISH, "Confirm creation");
        messages.addMessage("ui.character.wizardStepIdentity", Locale.ENGLISH, "Identity");
        messages.addMessage("ui.character.wizardStepArchetype", Locale.ENGLISH, "Archetype");
        messages.addMessage("ui.character.wizardAttrPreview", Locale.ENGLISH, "Attributes {0}/{1}/{2}/{3}/{4}/{5}/{6}");
        messages.addMessage("ui.character.wizardReview", Locale.ENGLISH, "Review {0} {1} {2} {3} {4} {5}");
        messages.addMessage("ui.character.validation.required", Locale.ENGLISH, "Required");
        messages.addMessage("ui.character.validation.initialAgeRange", Locale.ENGLISH, "Age must be between 12 and 120");
        messages.addMessage("ui.character.wizardFillRequired", Locale.ENGLISH, "Fill required fields");
        messages.addMessage("ui.character.created", Locale.ENGLISH, "Character created");
        messages.addMessage("character.error.worldInstanceNotFound", Locale.ENGLISH, "World instance missing");
        messages.addMessage("world.error.currentLocationStateNotFound", Locale.ENGLISH, "Initial location missing");
        return messages;
    }



    private GameSnapshotDTO snapshot() {
        GameSnapshotDTO snapshot = new GameSnapshotDTO();
        snapshot.setCharacterId(10L);
        snapshot.setWorldInstanceId(20L);
        snapshot.setCharacterName("Arlen");
        snapshot.setCurrentLocationId("zona_langur_praca_das_vozes");
        snapshot.setCurrentTimeMin(75L);
        snapshot.setActions(List.of(blockedAction()));
        return snapshot;
    }

    private GameActionDTO blockedAction() {
        GameActionDTO action = new GameActionDTO();
        action.setActionId("explore");
        action.setLabelMessageKey("ui.game.actionExplore");
        action.setAvailable(false);
        action.setBlockedReasonMessageKey("ui.game.contentUnavailable");
        return action;
    }

    private void confirmarCriacaoValida(MainView view) {
        Dialog wizard = view.openCreateWizardDialog(1L);
        textField(wizard, "Name").setValue("Arlen");
        textField(wizard, "Surname").setValue("Vale");
        integerField(wizard, "Initial age").setValue(24);
        button(wizard, "Next").click();
        button(wizard, "Confirm creation").click();
    }

    private SessionEntity authenticatedSession() {
        SessionEntity authenticated = new SessionEntity();
        authenticated.setIdUser(1L);
        authenticated.setToken("tok-auth");
        return authenticated;
    }

    private CharacterSelectionDTO selection(Long characterId, Long worldInstanceId, String currentLocationId) {
        CharacterSelectionDTO selection = new CharacterSelectionDTO();
        selection.setCharacterId(characterId);
        selection.setWorldInstanceId(worldInstanceId);
        selection.setCurrentLocationId(currentLocationId);
        selection.setName("Arlen");
        selection.setCurrentLevel(1);
        selection.setCurrentTimeMin(0L);
        return selection;
    }

    private IntegerField integerField(Component root, String label) {
        return descendants(root)
                .filter(IntegerField.class::isInstance)
                .map(IntegerField.class::cast)
                .filter(field -> label.equals(field.getLabel()))
                .findFirst()
                .orElseThrow();
    }

    private TextField textField(Component root, String label) {
        return descendants(root)
                .filter(TextField.class::isInstance)
                .map(TextField.class::cast)
                .filter(field -> label.equals(field.getLabel()))
                .findFirst()
                .orElseThrow();
    }


    private Paragraph paragraph(Component root, String textPrefix) {
        return descendants(root)
                .filter(Paragraph.class::isInstance)
                .map(Paragraph.class::cast)
                .filter(paragraph -> paragraph.getText().startsWith(textPrefix))
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
