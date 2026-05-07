package br.eng.rodrigogml.mysteryrealms.ui;

import br.eng.rodrigogml.mysteryrealms.application.character.entity.CharacterEntity;
import br.eng.rodrigogml.mysteryrealms.application.character.service.CharacterService;
import br.eng.rodrigogml.mysteryrealms.application.user.entity.SessionEntity;
import br.eng.rodrigogml.mysteryrealms.application.user.service.UserService;
import br.eng.rodrigogml.mysteryrealms.application.world.entity.WorldInstanceEntity;
import br.eng.rodrigogml.mysteryrealms.application.world.service.WorldInstanceService;
import br.eng.rodrigogml.mysteryrealms.common.error.ErrorMapperService;
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

import java.util.Locale;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
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

    @Mock private UserService userService;
    @Mock private CharacterService characterService;
    @Mock private WorldInstanceService worldInstanceService;
    @Mock private ErrorMapperService errorMapperService;

    private VaadinSession session;

    @BeforeEach
    void setUp() {
        VaadinService service = mock(VaadinService.class);
        DeploymentConfiguration configuration = mock(DeploymentConfiguration.class);
        when(configuration.isProductionMode()).thenReturn(true);
        when(service.getDeploymentConfiguration()).thenReturn(configuration);
        session = new TestVaadinSession(service);
        session.lock();
        VaadinSession.setCurrent(session);
        UI ui = new UI();
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
    void render_comPersonagemSelecionado_carregaMundoEExibeEstadoInicial() {
        session.setAttribute(UiSessionAttributes.AUTH_TOKEN, "tok");
        session.setAttribute(UiSessionAttributes.SELECTED_CHARACTER_ID, 10L);
        SessionEntity authenticated = new SessionEntity();
        authenticated.setIdUser(1L);
        when(userService.validateSession("tok")).thenReturn(authenticated);
        CharacterEntity character = new CharacterEntity();
        character.setId(10L);
        character.setIdUser(1L);
        character.setName("Arlen");
        when(characterService.selectCharacter(1L, 10L)).thenReturn(character);
        WorldInstanceEntity worldInstance = new WorldInstanceEntity();
        worldInstance.setIdCharacter(10L);
        worldInstance.setCurrentTimeMin(75L);
        worldInstance.setCurrentLocationId(null);
        when(worldInstanceService.loadWorldInstance(10L)).thenReturn(worldInstance);

        GameView view = new GameView(userService, characterService, worldInstanceService, messages(), errorMapperService);

        assertEquals("Current location: Unknown location", paragraph(view, "Current location").getText());
        assertEquals("Current time: Day 1, 1:15", paragraph(view, "Current time").getText());
        assertFalse(button(view, "Explore").isEnabled());
        verify(userService).validateSession("tok");
        verify(characterService).selectCharacter(1L, 10L);
        verify(worldInstanceService).loadWorldInstance(10L);
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
