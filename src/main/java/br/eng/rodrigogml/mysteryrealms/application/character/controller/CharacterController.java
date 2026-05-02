package br.eng.rodrigogml.mysteryrealms.application.character.controller;

import br.eng.rodrigogml.mysteryrealms.application.character.dto.CharacterCreationDTO;
import br.eng.rodrigogml.mysteryrealms.application.character.dto.CharacterDeletionDTO;
import br.eng.rodrigogml.mysteryrealms.application.character.dto.CharacterRenameDTO;
import br.eng.rodrigogml.mysteryrealms.application.character.dto.CharacterSummaryDTO;
import br.eng.rodrigogml.mysteryrealms.application.character.entity.CharacterEntity;
import br.eng.rodrigogml.mysteryrealms.application.character.service.CharacterService;
import br.eng.rodrigogml.mysteryrealms.domain.character.enums.CharacterClass;
import br.eng.rodrigogml.mysteryrealms.domain.character.enums.Race;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    public CharacterController(CharacterService characterService) {
        this.characterService = characterService;
    }

    @GetMapping
    public List<CharacterSummaryDTO> list(@RequestParam Long userId) {
        return characterService.listCharacterSummaries(userId);
    }

    @PostMapping
    @org.springframework.web.bind.annotation.ResponseStatus(HttpStatus.CREATED)
    public CharacterEntity createAndSelect(@RequestParam Long userId, @Valid @RequestBody CharacterCreationDTO creation) {
        return characterService.createAndSelectCharacter(userId, creation);
    }

    @PostMapping("/{characterId}/select")
    public CharacterEntity select(@RequestParam Long userId, @PathVariable Long characterId) {
        return characterService.selectCharacter(userId, characterId);
    }

    @PatchMapping("/{characterId}/name")
    public void rename(@RequestParam Long userId, @PathVariable Long characterId, @Valid @RequestBody CharacterRenameDTO rename) {
        characterService.renameCharacter(userId, characterId, rename);
    }

    @DeleteMapping("/{characterId}")
    @org.springframework.web.bind.annotation.ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestParam Long userId, @PathVariable Long characterId, @Valid @RequestBody CharacterDeletionDTO deletion) {
        characterService.deleteCharacter(userId, characterId, deletion);
    }

    @PostMapping("/legacy")
    public CharacterEntity createLegacy(@RequestParam Long userId, @RequestParam String name,
            @RequestParam Race race, @RequestParam CharacterClass characterClass) {
        return characterService.createCharacter(userId, name, race, characterClass);
    }
}
