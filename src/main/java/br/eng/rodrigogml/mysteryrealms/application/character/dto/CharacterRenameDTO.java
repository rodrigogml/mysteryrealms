package br.eng.rodrigogml.mysteryrealms.application.character.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para solicitação de renomeação de personagem.
 *
 * @author ?
 * @since 02-05-2026
 */
public class CharacterRenameDTO {

    @NotBlank
    @Size(max = 100)
    private String newName;

    public String getNewName() {
        return newName;
    }

    public void setNewName(String newName) {
        this.newName = newName;
    }
}
