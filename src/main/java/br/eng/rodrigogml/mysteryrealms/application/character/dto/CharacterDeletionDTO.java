package br.eng.rodrigogml.mysteryrealms.application.character.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para confirmação forte de exclusão de personagem.
 *
 * @author ?
 * @since 02-05-2026
 */
public class CharacterDeletionDTO {

    @NotBlank
    @Size(max = 100)
    private String confirmationText;

    public String getConfirmationText() {
        return confirmationText;
    }

    public void setConfirmationText(String confirmationText) {
        this.confirmationText = confirmationText;
    }
}
