package br.eng.rodrigogml.mysteryrealms.application.character.controller;

import br.eng.rodrigogml.mysteryrealms.application.character.dto.CharacterCreationDTO;
import br.eng.rodrigogml.mysteryrealms.application.character.dto.CharacterDeletionDTO;
import br.eng.rodrigogml.mysteryrealms.application.character.dto.CharacterRenameDTO;
import br.eng.rodrigogml.mysteryrealms.application.character.dto.CharacterSummaryDTO;
import br.eng.rodrigogml.mysteryrealms.application.character.entity.CharacterEntity;
import br.eng.rodrigogml.mysteryrealms.application.character.service.CharacterService;
import br.eng.rodrigogml.mysteryrealms.application.user.session.AuthenticatedUserContext;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller REST do fluxo pós-login de personagens.
 *
 * @author ?
 * @since 02-05-2026
 */
@RestController
@RequestMapping("/api/characters")
public class CharacterController {

    private final CharacterService characterService;
    private final AuthenticatedUserContext authenticatedUserContext;

    public CharacterController(CharacterService characterService, AuthenticatedUserContext authenticatedUserContext) {
        this.characterService = characterService;
        this.authenticatedUserContext = authenticatedUserContext;
    }

    @GetMapping
    public List<CharacterSummaryDTO> list() {
        return characterService.listCharacterSummaries(authenticatedUserId());
    }

    @PostMapping
    @org.springframework.web.bind.annotation.ResponseStatus(HttpStatus.CREATED)
    public CharacterEntity createAndSelect(@Valid @RequestBody CharacterCreationDTO creation) {
        return characterService.createAndSelectCharacter(authenticatedUserId(), creation);
    }

    @PostMapping("/{characterId}/select")
    public CharacterEntity select(@PathVariable Long characterId) {
        return characterService.selectCharacter(authenticatedUserId(), characterId);
    }

    @PatchMapping("/{characterId}/name")
    public void rename(@PathVariable Long characterId, @Valid @RequestBody CharacterRenameDTO rename) {
        characterService.renameCharacter(authenticatedUserId(), characterId, rename);
    }

    @DeleteMapping("/{characterId}")
    @org.springframework.web.bind.annotation.ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long characterId, @Valid @RequestBody CharacterDeletionDTO deletion) {
        characterService.deleteCharacter(authenticatedUserId(), characterId, deletion);
    }

    private Long authenticatedUserId() {
        return authenticatedUserContext.authenticatedUserId();
    }
}
