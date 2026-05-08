package br.eng.rodrigogml.mysteryrealms.ui;

import br.eng.rodrigogml.mysteryrealms.application.game.dto.GameActionDTO;
import br.eng.rodrigogml.mysteryrealms.application.game.dto.GameSnapshotDTO;
import br.eng.rodrigogml.mysteryrealms.application.game.service.GameSessionService;
import br.eng.rodrigogml.mysteryrealms.common.error.ErrorMapperService;
import br.eng.rodrigogml.mysteryrealms.common.error.ErrorResponseVO;
import br.eng.rodrigogml.mysteryrealms.common.exception.ValidationException;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Paragraph;
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

import java.util.List;
import java.util.Locale;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameViewTest {

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

    @Mock private GameSessionService gameSessionService;
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
    void render_comPersonagemSelecionado_carregaSnapshotSomenteLeituraEExibeEstadoInicial() {
        session.setAttribute(UiSessionAttributes.AUTH_TOKEN, "tok");
        session.setAttribute(UiSessionAttributes.SELECTED_CHARACTER_ID, 10L);
        when(gameSessionService.loadSnapshotForSelectedCharacter("tok", 10L)).thenReturn(snapshot());

        GameView view = new GameView(gameSessionService, messages(), errorMapperService);

        assertEquals("Current location: zona_langur_praca_das_vozes", paragraph(view, "Current location").getText());
        assertEquals("Current time: Day 1, 1:15", paragraph(view, "Current time").getText());
        assertFalse(button(view, "Explore").isEnabled());
        verify(gameSessionService).loadSnapshotForSelectedCharacter("tok", 10L);
        verify(gameSessionService, never()).loadSnapshot("tok", 10L);
        verifyNoMoreInteractions(gameSessionService);
    }


    @Test
    void render_semPersonagemSelecionado_redirecionaParaInicioSemCarregarSnapshot() {
        session.setAttribute(UiSessionAttributes.AUTH_TOKEN, "tok");

        new GameView(gameSessionService, messages(), errorMapperService);

        assertEquals("", ui.navigatedRoute);
        verifyNoMoreInteractions(gameSessionService);
    }

    @Test
    void render_quandoPersonagemPertenceAOutroUsuario_limpaSelecaoERedirecionaParaInicio() {
        assertGameLoadFailureClearsSelectionAndNavigatesHome("character.error.notOwned");
    }

    @Test
    void render_quandoInstanciaMundoAusente_limpaSelecaoERedirecionaParaInicio() {
        assertGameLoadFailureClearsSelectionAndNavigatesHome("world.error.instanceNotFound");
    }

    @Test
    void render_quandoLocalidadeInicialAusente_limpaSelecaoERedirecionaParaInicio() {
        assertGameLoadFailureClearsSelectionAndNavigatesHome("world.error.currentLocationStateNotFound");
    }

    @Test
    void render_quandoLocalidadeInicialVazia_limpaSelecaoERedirecionaParaInicio() {
        assertGameLoadFailureClearsSelectionAndNavigatesHome("world.error.invalidLocationId");
    }

    @Test
    void render_repetidoNaRotaGame_naoUsaFluxoMutavelDeSelecao() {
        session.setAttribute(UiSessionAttributes.AUTH_TOKEN, "tok");
        session.setAttribute(UiSessionAttributes.SELECTED_CHARACTER_ID, 10L);
        when(gameSessionService.loadSnapshotForSelectedCharacter("tok", 10L)).thenReturn(snapshot());

        new GameView(gameSessionService, messages(), errorMapperService);
        new GameView(gameSessionService, messages(), errorMapperService);

        verify(gameSessionService, times(2)).loadSnapshotForSelectedCharacter("tok", 10L);
        verify(gameSessionService, never()).loadSnapshot("tok", 10L);
        verifyNoMoreInteractions(gameSessionService);
    }


    private void assertGameLoadFailureClearsSelectionAndNavigatesHome(String messageKey) {
        session.setAttribute(UiSessionAttributes.AUTH_TOKEN, "tok");
        session.setAttribute(UiSessionAttributes.SELECTED_CHARACTER_ID, 10L);
        ValidationException exception = new ValidationException(messageKey);
        when(gameSessionService.loadSnapshotForSelectedCharacter("tok", 10L)).thenThrow(exception);
        when(errorMapperService.map(any(RuntimeException.class)))
                .thenReturn(new ErrorResponseVO("VALIDATION", messageKey, "trace-game"));

        new GameView(gameSessionService, messages(), errorMapperService);

        assertNull(session.getAttribute(UiSessionAttributes.SELECTED_CHARACTER_ID));
        assertEquals("", ui.navigatedRoute);
        verify(gameSessionService).loadSnapshotForSelectedCharacter("tok", 10L);
        verify(errorMapperService).map(exception);
    }

    private GameSnapshotDTO snapshot() {
        GameSnapshotDTO snapshot = new GameSnapshotDTO();
        snapshot.setCharacterId(10L);
        snapshot.setWorldInstanceId(20L);
        snapshot.setCharacterName("Arlen");
        snapshot.setCurrentLocationId("zona_langur_praca_das_vozes");
        snapshot.setCurrentTimeMin(75L);
        snapshot.setActions(List.of(blockedAction("explore", "ui.game.actionExplore")));
        return snapshot;
    }

    private GameActionDTO blockedAction(String actionId, String labelMessageKey) {
        GameActionDTO action = new GameActionDTO();
        action.setActionId(actionId);
        action.setLabelMessageKey(labelMessageKey);
        action.setAvailable(false);
        action.setBlockedReasonMessageKey("ui.game.contentUnavailable");
        return action;
    }

    private StaticMessageSource messages() {
        StaticMessageSource messages = new StaticMessageSource();
        messages.addMessage("ui.game.title", Locale.ENGLISH, "Adventure");
        messages.addMessage("ui.game.characterName", Locale.ENGLISH, "Character: {0}");
        messages.addMessage("ui.game.currentLocation", Locale.ENGLISH, "Current location: {0}");
        messages.addMessage("ui.game.currentTime", Locale.ENGLISH, "Current time: {0}");
        messages.addMessage("ui.game.locationUnknown", Locale.ENGLISH, "Unknown location");
        messages.addMessage("ui.game.emptyWorldState", Locale.ENGLISH, "World has no content");
        messages.addMessage("ui.game.contentUnavailable", Locale.ENGLISH, "No content");
        messages.addMessage("ui.game.actionExplore", Locale.ENGLISH, "Explore");
        messages.addMessage("ui.game.actionTravel", Locale.ENGLISH, "Travel");
        messages.addMessage("ui.game.actionRest", Locale.ENGLISH, "Rest");
        messages.addMessage("ui.game.backToCharacters", Locale.ENGLISH, "Back");
        messages.addMessage("ui.game.noSelectedCharacter", Locale.ENGLISH, "Select a character");
        messages.addMessage("ui.game.timeFormat", Locale.ENGLISH, "Day {0}, {1}:{2,number,00}");
        messages.addMessage("character.error.notOwned", Locale.ENGLISH, "Character belongs to another user");
        messages.addMessage("world.error.instanceNotFound", Locale.ENGLISH, "World instance missing");
        messages.addMessage("world.error.currentLocationStateNotFound", Locale.ENGLISH, "Initial location missing");
        messages.addMessage("world.error.invalidLocationId", Locale.ENGLISH, "Initial location is empty");
        return messages;
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
        return Stream.concat(Stream.of(component), component.getChildren().flatMap(this::descendants));
    }
}
