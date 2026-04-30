package br.eng.rodrigogml.mysteryrealms.ui;

import br.eng.rodrigogml.mysteryrealms.application.user.service.UserService;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * View responsável por consumir o token de confirmação de e-mail enviado ao usuário.
 *
 * @author ?
 * @since 30-04-2026
 */
@PageTitle("Mystery Realms")
@Route("confirm-email/:token")
public class EmailConfirmationView extends VerticalLayout implements BeforeEnterObserver {

    private final UserService userService;
    private final MessageSource messageSource;

    /**
     * Cria a tela de confirmação de e-mail.
     *
     * @param userService serviço de usuários
     * @param messageSource mensagens internacionalizadas
     */
    public EmailConfirmationView(UserService userService, MessageSource messageSource) {
        this.userService = userService;
        this.messageSource = messageSource;
        setWidthFull();
        setMaxWidth("560px");
        setPadding(true);
        getStyle().set("margin", "0 auto");
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        removeAll();
        String token = event.getRouteParameters().get("token").orElse(null);
        if (token == null || token.isBlank()) {
            render(message("ui.confirm.titleError"), message("user.error.tokenNotFound"));
            return;
        }

        try {
            userService.confirmEmail(token);
            render(message("ui.confirm.titleSuccess"), message("ui.confirm.success"));
        } catch (IllegalArgumentException ex) {
            render(message("ui.confirm.titleError"), message(ex.getMessage()));
        }
    }

    private void render(String title, String text) {
        add(new H1(title), new Paragraph(text));
    }

    private String message(String key, Object... args) {
        return messageSource.getMessage(key, args, key, LocaleContextHolder.getLocale());
    }
}
