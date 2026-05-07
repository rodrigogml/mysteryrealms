package br.eng.rodrigogml.mysteryrealms.ui;

import br.eng.rodrigogml.mysteryrealms.application.character.dto.CharacterCreationDTO;
import br.eng.rodrigogml.mysteryrealms.application.character.dto.CharacterDeletionDTO;
import br.eng.rodrigogml.mysteryrealms.application.character.dto.CharacterRenameDTO;
import br.eng.rodrigogml.mysteryrealms.application.character.dto.CharacterSummaryDTO;
import br.eng.rodrigogml.mysteryrealms.application.character.service.CharacterService;
import br.eng.rodrigogml.mysteryrealms.application.user.entity.SessionEntity;
import br.eng.rodrigogml.mysteryrealms.domain.character.model.AttributeSet;
import br.eng.rodrigogml.mysteryrealms.domain.character.model.Character;
import br.eng.rodrigogml.mysteryrealms.application.user.service.LoginResultVO;
import br.eng.rodrigogml.mysteryrealms.application.user.entity.TwoFactorMethod;
import br.eng.rodrigogml.mysteryrealms.application.user.service.UserService;
import br.eng.rodrigogml.mysteryrealms.common.error.ErrorMapperService;
import br.eng.rodrigogml.mysteryrealms.common.error.ErrorResponseVO;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.html.UnorderedList;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
    private static final String CHARACTER_WIZARD_DRAFT_ATTRIBUTE = "mysteryrealms.characterWizardDraft";


    private static class CharacterWizardDraft {
        private String name;
        private String surname;
        private String gender;
        private Integer initialAge;
        private String race;
        private String characterClass;
    }

    private final UserService userService;
    private final CharacterService characterService;
    private final MessageSource messageSource;
    private final ErrorMapperService errorMapperService;

    /**
     * Cria a tela inicial do jogo.
     *
     * @param userService serviço de usuários
     * @param messageSource mensagens internacionalizadas
     */
    public MainView(UserService userService, CharacterService characterService, MessageSource messageSource, ErrorMapperService errorMapperService) {
        this.userService = userService;
        this.characterService = characterService;
        this.messageSource = messageSource;
        this.errorMapperService = errorMapperService;
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
            } catch (RuntimeException ex) {
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
            } catch (RuntimeException ex) {
                showStandardError(ex);
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
                    openSecondFactorDialog(result, email.getValue(), password.getValue());
                    return;
                }
                VaadinSession.getCurrent().setAttribute(SESSION_TOKEN_ATTRIBUTE, result.session().getToken());
                renderAuthenticated(result.session());
            } catch (RuntimeException ex) {
                showStandardError(ex);
            }
        });

        VerticalLayout form = new VerticalLayout(new H2(message("ui.login.title")), email, password, submit);
        form.setPadding(false);
        form.setWidthFull();
        return form;
    }

    Dialog openSecondFactorDialog(LoginResultVO initialResult, String email, String password) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle(message("ui.login.secondFactorTitle"));

        TextField code = new TextField(message("ui.login.secondFactorCode"));
        code.setWidthFull();
        code.setAutofocus(true);

        Paragraph hint = new Paragraph(message("ui.login.secondFactorRequired"));
        VerticalLayout content = new VerticalLayout(hint, code);
        content.setPadding(false);
        content.setWidthFull();

        LoginResultVO[] pendingResult = { initialResult };

        Button confirm = new Button(message("ui.login.secondFactorConfirm"), event -> {
            try {
                SessionEntity session = userService.completeTwoFactorLogin(pendingResult[0].userId(), code.getValue());
                VaadinSession.getCurrent().setAttribute(SESSION_TOKEN_ATTRIBUTE, session.getToken());
                dialog.close();
                renderAuthenticated(session);
            } catch (RuntimeException ex) {
                code.setInvalid(true);
                code.setErrorMessage(message("ui.login.secondFactorInvalidCode"));
                Notification.show(message("ui.login.secondFactorInvalidCode"));
            }
        });

        Button resend = new Button(message("ui.login.secondFactorResend"), event -> {
            try {
                LoginResultVO resentResult = userService.login(email, password, clientIpAddress());
                if (resentResult.status() == LoginResultVO.Status.SECOND_FACTOR_REQUIRED) {
                    pendingResult[0] = resentResult;
                    code.clear();
                    code.setInvalid(false);
                    Notification.show(message("ui.login.secondFactorResent"));
                }
            } catch (RuntimeException ex) {
                showStandardError(ex);
            }
        });
        resend.setVisible(initialResult.secondFactorMethod() == TwoFactorMethod.EMAIL);

        dialog.add(content);
        dialog.getFooter().add(resend, confirm);
        dialog.open();
        return dialog;
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
                } catch (RuntimeException ex) {
                    showStandardError(ex);
                }
            }
            VaadinSession.getCurrent().setAttribute(SESSION_TOKEN_ATTRIBUTE, null);
            renderAccess();
        });

        shell.add(title, status, new HorizontalLayout(logout), characterManagementSection(session.getIdUser()));
        add(shell);
    }

    private VerticalLayout characterManagementSection(Long userId) {
        VerticalLayout section = new VerticalLayout();
        section.setWidthFull();
        section.add(new H2(message("ui.character.title")));

        Button openWizard = new Button(message("ui.character.createAndSelect"), event -> openCreateWizardDialog(userId));
        HorizontalLayout createRow = new HorizontalLayout(openWizard);
        createRow.setWidthFull();

        TextField filterName = new TextField(message("ui.character.filterName"));
        Select<String> sortBy = new Select<>("LAST_ACCESS", "NAME", "LEVEL");
        sortBy.setLabel(message("ui.character.sortBy"));
        sortBy.setValue("LAST_ACCESS");

        VerticalLayout cards = new VerticalLayout();
        cards.setWidthFull();

        Runnable refreshCards = () -> {
            cards.removeAll();
            List<CharacterSummaryDTO> items = new ArrayList<>(characterService.listCharacterSummaries(userId));
            if (!isBlank(filterName.getValue())) {
                String term = filterName.getValue().trim().toLowerCase();
                items.removeIf(c -> c.getName() == null || !c.getName().toLowerCase().contains(term));
            }
            if ("NAME".equals(sortBy.getValue())) {
                items.sort(Comparator.comparing(CharacterSummaryDTO::getName, String.CASE_INSENSITIVE_ORDER));
            } else if ("LEVEL".equals(sortBy.getValue())) {
                items.sort(Comparator.comparing(CharacterSummaryDTO::getCurrentLevel).reversed());
            }
            if (items.isEmpty()) {
                cards.add(new Paragraph(message("ui.character.emptyState")));
            } else {
                for (CharacterSummaryDTO summary : items) {
                    cards.add(characterCard(userId, summary));
                }
            }
        };

        filterName.addValueChangeListener(e -> refreshCards.run());
        sortBy.addValueChangeListener(e -> refreshCards.run());
        refreshCards.run();

        section.add(createRow, new HorizontalLayout(filterName, sortBy), cards);
        return section;
    }


    private void openCreateWizardDialog(Long userId) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle(message("ui.character.wizardTitle"));

        CharacterWizardDraft storedDraft = (CharacterWizardDraft) VaadinSession.getCurrent().getAttribute(CHARACTER_WIZARD_DRAFT_ATTRIBUTE);
        final CharacterWizardDraft draft = storedDraft == null ? new CharacterWizardDraft() : storedDraft;
        if (storedDraft == null) {
            draft.gender = "OTHER";
            draft.initialAge = 20;
            draft.race = "HUMAN";
            draft.characterClass = "WARRIOR";
        }

        TextField name = new TextField(message("ui.character.name"));
        name.setRequiredIndicatorVisible(true);
        if (draft.name != null) { name.setValue(draft.name); }
        TextField surname = new TextField(message("ui.character.surname"));
        surname.setRequiredIndicatorVisible(true);
        if (draft.surname != null) { surname.setValue(draft.surname); }
        Select<String> gender = new Select<>("MALE", "FEMALE", "OTHER");
        gender.setLabel(message("ui.character.gender"));
        gender.setValue(draft.gender == null ? "OTHER" : draft.gender);
        IntegerField initialAge = new IntegerField(message("ui.character.initialAge"));
        initialAge.setRequiredIndicatorVisible(true);
        initialAge.setValue(draft.initialAge == null ? 20 : draft.initialAge);
        Select<String> race = new Select<>("HUMAN", "ELF", "DWARF");
        race.setLabel(message("ui.character.race"));
        race.setValue(draft.race == null ? "HUMAN" : draft.race);
        Select<String> characterClass = new Select<>("WARRIOR", "MAGE", "HUNTER", "CLERIC", "ROGUE");
        characterClass.setLabel(message("ui.character.class"));
        characterClass.setValue(draft.characterClass == null ? "WARRIOR" : draft.characterClass);

        Paragraph step = new Paragraph(message("ui.character.wizardStepIdentity"));
        VerticalLayout content = new VerticalLayout(step, name, surname, gender, initialAge);

        Button cancel = new Button(message("ui.character.cancel"), e -> dialog.close());
        Button next = new Button(message("ui.character.wizardNext"));
        Button back = new Button(message("ui.character.wizardBack"));
        back.setVisible(false);
        Button confirm = new Button(message("ui.character.wizardConfirm"));
        confirm.setVisible(false);
        confirm.setEnabled(false);

        Runnable saveDraft = () -> {
            draft.name = name.getValue();
            draft.surname = surname.getValue();
            draft.gender = gender.getValue();
            draft.initialAge = initialAge.getValue();
            draft.race = race.getValue();
            draft.characterClass = characterClass.getValue();
            VaadinSession.getCurrent().setAttribute(CHARACTER_WIZARD_DRAFT_ATTRIBUTE, draft);
        };
        Runnable refreshWizardState = () -> {
            boolean validIdentity = validateWizardIdentity(name, surname, initialAge);
            next.setEnabled(validIdentity);
            confirm.setEnabled(validIdentity);
        };

        name.addValueChangeListener(e -> { saveDraft.run(); refreshWizardState.run(); });
        surname.addValueChangeListener(e -> { saveDraft.run(); refreshWizardState.run(); });
        initialAge.addValueChangeListener(e -> { saveDraft.run(); refreshWizardState.run(); });
        gender.addValueChangeListener(e -> saveDraft.run());
        race.addValueChangeListener(e -> saveDraft.run());
        characterClass.addValueChangeListener(e -> saveDraft.run());
        saveDraft.run();
        refreshWizardState.run();

        next.addClickListener(e -> {
            if (!validateWizardIdentity(name, surname, initialAge)) {
                Notification.show(message("ui.character.wizardFillRequired"));
                return;
            }

            step.setText(message("ui.character.wizardStepArchetype"));
            content.removeAll();

            Character preview = new Character(name.getValue().trim(), surname.getValue().trim(),
                    br.eng.rodrigogml.mysteryrealms.domain.character.enums.Gender.valueOf(gender.getValue()),
                    br.eng.rodrigogml.mysteryrealms.domain.character.enums.Race.valueOf(race.getValue()),
                    br.eng.rodrigogml.mysteryrealms.domain.character.enums.CharacterClass.valueOf(characterClass.getValue()),
                    initialAge.getValue());
            AttributeSet attrs = preview.getAttributes();
            Paragraph attributesPreview = new Paragraph(message("ui.character.wizardAttrPreview",
                    attrs.strength(), attrs.dexterity(), attrs.constitution(), attrs.intellect(),
                    attrs.perception(), attrs.charisma(), attrs.willpower()));

            content.add(step, race, characterClass,
                    new Paragraph(message("ui.character.wizardReview", name.getValue(), surname.getValue(), gender.getValue(), initialAge.getValue(), race.getValue(), characterClass.getValue())),
                    attributesPreview);
            back.setVisible(true);
            next.setVisible(false);
            confirm.setVisible(true);
        });

        back.addClickListener(e -> {
            step.setText(message("ui.character.wizardStepIdentity"));
            content.removeAll();
            content.add(step, name, surname, gender, initialAge);
            back.setVisible(false);
            next.setVisible(true);
            confirm.setVisible(false);
        });

        confirm.addClickListener(e -> {
            try {
                CharacterCreationDTO dto = new CharacterCreationDTO();
                dto.setName(name.getValue());
                dto.setSurname(surname.getValue());
                dto.setGender(br.eng.rodrigogml.mysteryrealms.domain.character.enums.Gender.valueOf(gender.getValue()));
                dto.setInitialAge(initialAge.getValue());
                dto.setRace(br.eng.rodrigogml.mysteryrealms.domain.character.enums.Race.valueOf(race.getValue()));
                dto.setCharacterClass(br.eng.rodrigogml.mysteryrealms.domain.character.enums.CharacterClass.valueOf(characterClass.getValue()));
                characterService.createAndSelectCharacter(userId, dto);
                VaadinSession.getCurrent().setAttribute(CHARACTER_WIZARD_DRAFT_ATTRIBUTE, null);
                dialog.close();
                Notification.show(message("ui.character.created"));
                render();
            } catch (RuntimeException ex) {
                showStandardError(ex);
            }
        });

        dialog.add(content);
        dialog.getFooter().add(cancel, back, next, confirm);
        dialog.open();
    }

    private VerticalLayout characterCard(Long userId, CharacterSummaryDTO summary) {
        VerticalLayout card = new VerticalLayout();
        card.setPadding(true);
        card.setSpacing(true);
        card.setWidthFull();
        card.getStyle().set("border", "1px solid var(--lumo-contrast-20pct)");
        card.getStyle().set("border-radius", "8px");

        Paragraph title = new Paragraph(summary.getName() + " - " + summary.getRace() + " / "
                + summary.getCharacterClass() + " Lv." + summary.getCurrentLevel());

        Button select = new Button(message("ui.character.select"), e -> {
            try {
                characterService.selectCharacter(userId, summary.getId());
                Notification.show(message("ui.character.selected"));
                render();
            } catch (RuntimeException ex) {
                showStandardError(ex);
            }
        });
        Button play = new Button(message("ui.character.play"), e -> {
            try {
                characterService.selectCharacter(userId, summary.getId());
                Notification.show(message("ui.character.playing", summary.getName()));
            } catch (RuntimeException ex) {
                showStandardError(ex);
            }
        });
        Button rename = new Button(message("ui.character.rename"), e -> openRenameDialog(userId, summary));
        Button delete = new Button(message("ui.character.delete"), e -> openDeleteDialog(userId, summary));

        card.add(title, new HorizontalLayout(play, select, rename, delete));
        return card;
    }

    private void openRenameDialog(Long userId, CharacterSummaryDTO summary) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle(message("ui.character.renameDialogTitle", summary.getName()));
        TextField newName = new TextField(message("ui.character.renameNewName"));
        newName.setValue(summary.getName());

        Button cancel = new Button(message("ui.character.cancel"), e -> dialog.close());
        Button save = new Button(message("ui.character.save"), e -> {
            try {
                CharacterRenameDTO dto = new CharacterRenameDTO();
                dto.setNewName(newName.getValue());
                characterService.renameCharacter(userId, summary.getId(), dto);
                dialog.close();
                Notification.show(message("ui.character.renamed"));
                render();
            } catch (RuntimeException ex) {
                showStandardError(ex);
            }
        });

        dialog.add(new VerticalLayout(newName));
        dialog.getFooter().add(cancel, save);
        dialog.open();
    }

    private void openDeleteDialog(Long userId, CharacterSummaryDTO summary) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle(message("ui.character.deleteDialogTitle", summary.getName()));
        TextField confirmation = new TextField(message("ui.character.deleteConfirmLabel"));
        confirmation.setPlaceholder(summary.getName());

        Button cancel = new Button(message("ui.character.cancel"), e -> dialog.close());
        Button confirm = new Button(message("ui.character.confirmDelete"), e -> {
            try {
                CharacterDeletionDTO dto = new CharacterDeletionDTO();
                dto.setConfirmationText(confirmation.getValue());
                characterService.deleteCharacter(userId, summary.getId(), dto);
                dialog.close();
                Notification.show(message("ui.character.deleted"));
                render();
            } catch (RuntimeException ex) {
                showStandardError(ex);
            }
        });
        confirm.setEnabled(false);
        confirmation.addValueChangeListener(e -> confirm.setEnabled(summary.getName().equals(e.getValue())));

        dialog.add(new VerticalLayout(new Paragraph(message("ui.character.deleteHint", summary.getName())), confirmation));
        dialog.getFooter().add(cancel, confirm);
        dialog.open();
    }


    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private boolean validateWizardIdentity(TextField name, TextField surname, IntegerField initialAge) {
        boolean valid = true;

        boolean nameInvalid = isBlank(name.getValue());
        name.setInvalid(nameInvalid);
        name.setErrorMessage(message("ui.character.validation.required"));
        valid = valid && !nameInvalid;

        boolean surnameInvalid = isBlank(surname.getValue());
        surname.setInvalid(surnameInvalid);
        surname.setErrorMessage(message("ui.character.validation.required"));
        valid = valid && !surnameInvalid;

        Integer ageValue = initialAge.getValue();
        boolean ageInvalid = ageValue == null || ageValue < 12 || ageValue > 120;
        initialAge.setInvalid(ageInvalid);
        initialAge.setErrorMessage(message("ui.character.validation.initialAgeRange"));
        valid = valid && !ageInvalid;

        return valid;
    }

    private void showStandardError(RuntimeException exception) {
        ErrorResponseVO error = errorMapperService.map(exception);
        Notification.show(message(error.messageKey()) + " [" + error.code() + " | " + error.traceId() + "]");
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
