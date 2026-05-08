package br.eng.rodrigogml.mysteryrealms.ui;

import br.eng.rodrigogml.mysteryrealms.application.game.dto.GameActionDTO;
import br.eng.rodrigogml.mysteryrealms.application.game.dto.GameSnapshotDTO;
import br.eng.rodrigogml.mysteryrealms.application.game.service.GameSessionService;
import br.eng.rodrigogml.mysteryrealms.common.error.ErrorMapperService;
import br.eng.rodrigogml.mysteryrealms.common.error.ErrorResponseVO;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * View principal de jogo para o personagem selecionado na sessão Vaadin.
 * Valida a sessão autenticada, confirma a posse do personagem e renderiza
 * o estado inicial da instância de mundo carregada.
 *
 * @author ?
 * @since 07-05-2026
 */
@PageTitle("Mystery Realms - Game")
@Route("game")
public class GameView extends VerticalLayout {

    private final GameSessionService gameSessionService;
    private final MessageSource messageSource;
    private final ErrorMapperService errorMapperService;

    /**
     * Cria a view de jogo com os serviços necessários para autenticação e carregamento de mundo.
     *
     * @param gameSessionService serviço de snapshot da sessão de jogo
     * @param messageSource mensagens internacionalizadas
     * @param errorMapperService serviço de mapeamento de erros
     */
    public GameView(GameSessionService gameSessionService, MessageSource messageSource,
            ErrorMapperService errorMapperService) {
        this.gameSessionService = gameSessionService;
        this.messageSource = messageSource;
        this.errorMapperService = errorMapperService;
        setSizeFull();
        setPadding(false);
        setSpacing(false);
        render();
    }

    private void render() {
        removeAll();
        VaadinSession session = VaadinSession.getCurrent();
        String token = (String) session.getAttribute(UiSessionAttributes.AUTH_TOKEN);
        if (token == null) {
            navigateToStart();
            return;
        }

        try {
            Long selectedCharacterId = (Long) session.getAttribute(UiSessionAttributes.SELECTED_CHARACTER_ID);
            if (selectedCharacterId == null) {
                Notification.show(message("ui.game.noSelectedCharacter"));
                navigateToStart();
                return;
            }
            GameSnapshotDTO snapshot = gameSessionService.loadSnapshot(token, selectedCharacterId);
            renderGameShell(snapshot);
        } catch (RuntimeException ex) {
            showStandardError(ex);
            session.setAttribute(UiSessionAttributes.SELECTED_CHARACTER_ID, null);
            navigateToStart();
        }
    }

    private void renderGameShell(GameSnapshotDTO snapshot) {
        VerticalLayout shell = new VerticalLayout();
        shell.setWidthFull();
        shell.setMaxWidth("900px");
        shell.setPadding(true);
        shell.setSpacing(true);
        shell.getStyle().set("margin", "0 auto");

        H1 title = new H1(message("ui.game.title"));
        H2 characterName = new H2(message("ui.game.characterName", snapshot.getCharacterName()));
        Paragraph location = new Paragraph(message("ui.game.currentLocation", locationLabel(snapshot)));
        Paragraph currentTime = new Paragraph(message("ui.game.currentTime", formatWorldTime(snapshot.getCurrentTimeMin())));
        Paragraph status = new Paragraph(contentStatus(snapshot));

        HorizontalLayout actions = new HorizontalLayout();
        snapshot.getActions().forEach(action -> actions.add(actionButton(action)));

        Button back = new Button(message("ui.game.backToCharacters"), event -> UI.getCurrent().navigate(""));
        shell.add(title, characterName, location, currentTime, status, actions, back);
        add(shell);
    }

    private Button actionButton(GameActionDTO snapshotAction) {
        Button action = new Button(message(snapshotAction.getLabelMessageKey()));
        action.setEnabled(snapshotAction.isAvailable());
        return action;
    }

    private String locationLabel(GameSnapshotDTO snapshot) {
        String currentLocationId = snapshot.getCurrentLocationId();
        if (currentLocationId == null || currentLocationId.isBlank()) {
            return message("ui.game.locationUnknown");
        }
        return currentLocationId;
    }

    private String contentStatus(GameSnapshotDTO snapshot) {
        return snapshot.getActions().stream()
                .filter(action -> !action.isAvailable())
                .map(GameActionDTO::getBlockedReasonMessageKey)
                .filter(messageKey -> messageKey != null && !messageKey.isBlank())
                .findFirst()
                .map(this::message)
                .orElseGet(() -> message("ui.game.contentUnavailable"));
    }

    private String formatWorldTime(long currentTimeMin) {
        long day = currentTimeMin / 1_440L + 1L;
        long minuteOfDay = currentTimeMin % 1_440L;
        long hour = minuteOfDay / 60L;
        long minute = minuteOfDay % 60L;
        return message("ui.game.timeFormat", day, hour, minute);
    }

    private void showStandardError(RuntimeException exception) {
        ErrorResponseVO error = errorMapperService.map(exception);
        Notification.show(message(error.messageKey()) + " [" + error.code() + " | " + error.traceId() + "]");
    }

    private void navigateToStart() {
        UI.getCurrent().navigate("");
    }

    private String message(String key, Object... args) {
        return messageSource.getMessage(key, args, key, LocaleContextHolder.getLocale());
    }
}
