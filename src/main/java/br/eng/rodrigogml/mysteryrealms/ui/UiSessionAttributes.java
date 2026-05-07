package br.eng.rodrigogml.mysteryrealms.ui;

import com.vaadin.flow.server.VaadinSession;

/**
 * Centraliza os nomes de atributos armazenados na {@link VaadinSession}
 * pelas views Vaadin do jogo.
 *
 * @author ?
 * @since 07-05-2026
 */
final class UiSessionAttributes {

    static final String AUTH_TOKEN = "mysteryrealms.authToken";
    static final String CHARACTER_WIZARD_DRAFT = "mysteryrealms.characterWizardDraft";
    static final String SELECTED_CHARACTER_ID = "mysteryrealms.selectedCharacterId";

    private UiSessionAttributes() {
    }
}
