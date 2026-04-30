package br.eng.rodrigogml.mysteryrealms.ui;

import br.eng.rodrigogml.mysteryrealms.application.user.entity.SessionEntity;
import br.eng.rodrigogml.mysteryrealms.application.user.service.LoginResultVO;
import br.eng.rodrigogml.mysteryrealms.application.user.service.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * View inicial de acesso ao jogo, com cadastro, login e sessão autenticada.
 *
 * @author ?
 * @since 30-04-2026
 */
@PageTitle("Mystery Realms")
@Route("")
public class MainView extends VerticalLayout {

    private static final String SESSION_TOKEN_ATTRIBUTE = "mysteryrealms.authToken";

    private final UserService userService;
    private final MessageSource messageSource;

    /**
     * Cria a tela inicial do jogo.
     *
     * @param userService serviço de usuários
     * @param messageSource mensagens internacionalizadas
     */
    public MainView(UserService userService, MessageSource messageSource) {
        this.userService = userService;
        this.messageSource = messageSource;
        setSizeFull();
        setPadding(false);
        setSpacing(false);
        render();
    }

    private void render() {
        removeAll();
        String token = (String) VaadinSession.getCurrent().getAttribute(SESSION_TOKEN_ATTRIBUTE);
        if (token != null) {
            try {
                SessionEntity session = userService.validateSession(token);
                renderAuthenticated(session);
                return;
            } catch (IllegalArgumentException ex) {
                VaadinSession.getCurrent().setAttribute(SESSION_TOKEN_ATTRIBUTE, null);
            }
        }
        renderAccess();
    }

    private void renderAccess() {
        VerticalLayout shell = new VerticalLayout();
        shell.setWidthFull();
        shell.setMaxWidth("520px");
        shell.setPadding(true);
        shell.setSpacing(true);
        shell.getStyle().set("margin", "0 auto");

        H1 title = new H1(message("ui.app.title"));
        Paragraph subtitle = new Paragraph(message("ui.access.subtitle"));

        VerticalLayout registerForm = registerForm();
        VerticalLayout loginForm = loginForm();
        loginForm.setVisible(false);

        Tab registerTab = new Tab(message("ui.access.registerTab"));
        Tab loginTab = new Tab(message("ui.access.loginTab"));
        Tabs tabs = new Tabs(registerTab, loginTab);
        tabs.addSelectedChangeListener(event -> {
            boolean registerSelected = event.getSelectedTab() == registerTab;
            registerForm.setVisible(registerSelected);
            loginForm.setVisible(!registerSelected);
        });

        shell.add(title, subtitle, tabs, registerForm, loginForm);
        add(shell);
    }

    private VerticalLayout registerForm() {
        TextField username = new TextField(message("ui.register.username"));
        EmailField email = new EmailField(message("ui.register.email"));
        PasswordField password = new PasswordField(message("ui.register.password"));
        Button submit = new Button(message("ui.register.submit"));

        username.setWidthFull();
        email.setWidthFull();
        password.setWidthFull();
        submit.addClickListener(event -> {
            try {
                userService.register(username.getValue(), email.getValue(), password.getValue());
                username.clear();
                email.clear();
                password.clear();
                Notification.show(message("ui.register.success"));
            } catch (IllegalArgumentException ex) {
                Notification.show(message(ex.getMessage()));
            }
        });

        VerticalLayout form = new VerticalLayout(new H2(message("ui.register.title")), username, email, password, submit);
        form.setPadding(false);
        form.setWidthFull();
        return form;
    }

    private VerticalLayout loginForm() {
        EmailField email = new EmailField(message("ui.login.email"));
        PasswordField password = new PasswordField(message("ui.login.password"));
        Button submit = new Button(message("ui.login.submit"));

        email.setWidthFull();
        password.setWidthFull();
        submit.addClickListener(event -> {
            try {
                LoginResultVO result = userService.login(email.getValue(), password.getValue(), clientIpAddress());
                if (result.status() == LoginResultVO.Status.SECOND_FACTOR_REQUIRED) {
                    Notification.show(message("ui.login.secondFactorRequired"));
                    return;
                }
                VaadinSession.getCurrent().setAttribute(SESSION_TOKEN_ATTRIBUTE, result.session().getToken());
                renderAuthenticated(result.session());
            } catch (IllegalArgumentException ex) {
                Notification.show(message(ex.getMessage()));
            }
        });

        VerticalLayout form = new VerticalLayout(new H2(message("ui.login.title")), email, password, submit);
        form.setPadding(false);
        form.setWidthFull();
        return form;
    }

    private void renderAuthenticated(SessionEntity session) {
        removeAll();
        VerticalLayout shell = new VerticalLayout();
        shell.setWidthFull();
        shell.setMaxWidth("760px");
        shell.setPadding(true);
        shell.getStyle().set("margin", "0 auto");

        H1 title = new H1(message("ui.app.title"));
        Paragraph status = new Paragraph(message("ui.game.ready", session.getIdUser()));
        Button logout = new Button(message("ui.logout.submit"), event -> {
            String token = (String) VaadinSession.getCurrent().getAttribute(SESSION_TOKEN_ATTRIBUTE);
            if (token != null) {
                try {
                    userService.logout(token);
                } catch (IllegalArgumentException ex) {
                    Notification.show(message(ex.getMessage()));
                }
            }
            VaadinSession.getCurrent().setAttribute(SESSION_TOKEN_ATTRIBUTE, null);
            renderAccess();
        });

        shell.add(title, status, new HorizontalLayout(logout));
        add(shell);
    }

    private String clientIpAddress() {
        VaadinRequest request = VaadinRequest.getCurrent();
        if (request == null) {
            return "0.0.0.0";
        }
        return request.getRemoteAddr();
    }

    private String message(String key, Object... args) {
        return messageSource.getMessage(key, args, key, LocaleContextHolder.getLocale());
    }
}
