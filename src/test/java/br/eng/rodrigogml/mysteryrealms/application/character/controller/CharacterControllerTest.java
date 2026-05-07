package br.eng.rodrigogml.mysteryrealms.application.character.controller;

import br.eng.rodrigogml.mysteryrealms.application.character.dto.CharacterDeletionDTO;
import br.eng.rodrigogml.mysteryrealms.application.character.dto.CharacterRenameDTO;
import br.eng.rodrigogml.mysteryrealms.application.character.dto.CharacterSelectionDTO;
import br.eng.rodrigogml.mysteryrealms.application.character.dto.CharacterSummaryDTO;
import br.eng.rodrigogml.mysteryrealms.application.character.service.CharacterService;
import br.eng.rodrigogml.mysteryrealms.application.user.session.AuthenticatedUserContext;
import br.eng.rodrigogml.mysteryrealms.domain.character.enums.CharacterClass;
import br.eng.rodrigogml.mysteryrealms.domain.character.enums.Race;
import br.eng.rodrigogml.mysteryrealms.common.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CharacterControllerTest {

    private static final Long AUTHENTICATED_USER_ID = 10L;

    @Mock
    private CharacterService characterService;

    @Mock
    private AuthenticatedUserContext authenticatedUserContext;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new CharacterController(characterService, authenticatedUserContext))
                .setControllerAdvice(new ApiExceptionHandler())
                .build();
        when(authenticatedUserContext.authenticatedUserId()).thenReturn(AUTHENTICATED_USER_ID);
    }

    @Test
    void list_retorna200_usandoUsuarioAutenticadoSemUserIdNaRequest() throws Exception {
        CharacterSummaryDTO summary = new CharacterSummaryDTO();
        summary.setId(1L);
        when(characterService.listCharacterSummaries(AUTHENTICATED_USER_ID)).thenReturn(List.of(summary));

        mockMvc.perform(get("/api/characters"))
                .andExpect(status().isOk());

        verify(characterService).listCharacterSummaries(AUTHENTICATED_USER_ID);
    }

    @Test
    void createAndSelect_retorna201_comContratoDeSelecaoSemExporEntidade() throws Exception {
        CharacterSelectionDTO selection = selectionDTO(99L, 7L);
        when(characterService.createAndSelectCharacterForGame(eq(AUTHENTICATED_USER_ID), any())).thenReturn(selection);

        mockMvc.perform(post("/api/characters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Aragorn\",\"surname\":\"Elessar\",\"gender\":\"MALE\",\"initialAge\":35,\"race\":\"HUMAN\",\"characterClass\":\"WARRIOR\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.characterId").value(99L))
                .andExpect(jsonPath("$.worldInstanceId").value(7L))
                .andExpect(jsonPath("$.lastAccessedAt").exists())
                .andExpect(jsonPath("$.name").value("Aragorn"))
                .andExpect(jsonPath("$.race").value("HUMAN"))
                .andExpect(jsonPath("$.characterClass").value("WARRIOR"))
                .andExpect(jsonPath("$.currentLevel").value(1))
                .andExpect(jsonPath("$.currentLocationId").value("zona_langur_praca_das_vozes"))
                .andExpect(jsonPath("$.idUser").doesNotExist())
                .andExpect(jsonPath("$.strength").doesNotExist());

        verify(characterService).createAndSelectCharacterForGame(eq(AUTHENTICATED_USER_ID), any());
    }

    @Test
    void select_retorna200_comContratoDeSelecaoSemExporEntidade() throws Exception {
        CharacterSelectionDTO selection = selectionDTO(1L, 3L);
        when(characterService.selectCharacterForGame(AUTHENTICATED_USER_ID, 1L)).thenReturn(selection);

        mockMvc.perform(post("/api/characters/1/select"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.characterId").value(1L))
                .andExpect(jsonPath("$.worldInstanceId").value(3L))
                .andExpect(jsonPath("$.lastAccessedAt").exists())
                .andExpect(jsonPath("$.name").value("Aragorn"))
                .andExpect(jsonPath("$.race").value("HUMAN"))
                .andExpect(jsonPath("$.characterClass").value("WARRIOR"))
                .andExpect(jsonPath("$.currentLevel").value(1))
                .andExpect(jsonPath("$.currentLocationId").value("zona_langur_praca_das_vozes"))
                .andExpect(jsonPath("$.idUser").doesNotExist())
                .andExpect(jsonPath("$.strength").doesNotExist());

        verify(characterService).selectCharacterForGame(AUTHENTICATED_USER_ID, 1L);
    }

    @Test
    void rename_retorna200_usandoUsuarioAutenticadoSemUserIdNaRequest() throws Exception {
        mockMvc.perform(patch("/api/characters/1/name")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"newName\":\"Bilbo\"}"))
                .andExpect(status().isOk());

        verify(characterService).renameCharacter(eq(AUTHENTICATED_USER_ID), eq(1L), any(CharacterRenameDTO.class));
    }

    @Test
    void delete_retorna204_usandoUsuarioAutenticadoSemUserIdNaRequest() throws Exception {
        mockMvc.perform(delete("/api/characters/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"confirmationText\":\"Sam\"}"))
                .andExpect(status().isNoContent());

        verify(characterService).deleteCharacter(eq(AUTHENTICATED_USER_ID), eq(1L), any(CharacterDeletionDTO.class));
    }

    @Test
    void select_personagemDeOutroUsuario_retorna400() throws Exception {
        when(characterService.selectCharacterForGame(AUTHENTICATED_USER_ID, 99L))
                .thenThrow(new ValidationException("character.error.notOwned"));

        mockMvc.perform(post("/api/characters/99/select"))
                .andExpect(status().isBadRequest());

        verify(characterService).selectCharacterForGame(AUTHENTICATED_USER_ID, 99L);
    }

    @Test
    void rename_personagemDeOutroUsuario_retorna400() throws Exception {
        doThrow(new ValidationException("character.error.notOwned"))
                .when(characterService).renameCharacter(eq(AUTHENTICATED_USER_ID), eq(99L), any(CharacterRenameDTO.class));

        mockMvc.perform(patch("/api/characters/99/name")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"newName\":\"Bilbo\"}"))
                .andExpect(status().isBadRequest());

        verify(characterService).renameCharacter(eq(AUTHENTICATED_USER_ID), eq(99L), any(CharacterRenameDTO.class));
    }

    @Test
    void delete_personagemDeOutroUsuario_retorna400() throws Exception {
        doThrow(new ValidationException("character.error.notOwned"))
                .when(characterService).deleteCharacter(eq(AUTHENTICATED_USER_ID), eq(99L), any(CharacterDeletionDTO.class));

        mockMvc.perform(delete("/api/characters/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"confirmationText\":\"Sam\"}"))
                .andExpect(status().isBadRequest());

        verify(characterService).deleteCharacter(eq(AUTHENTICATED_USER_ID), eq(99L), any(CharacterDeletionDTO.class));
    }

    private CharacterSelectionDTO selectionDTO(Long characterId, Long worldInstanceId) {
        CharacterSelectionDTO selection = new CharacterSelectionDTO();
        selection.setCharacterId(characterId);
        selection.setWorldInstanceId(worldInstanceId);
        selection.setLastAccessedAt(LocalDateTime.of(2026, 5, 7, 12, 0));
        selection.setName("Aragorn");
        selection.setRace(Race.HUMAN);
        selection.setCharacterClass(CharacterClass.WARRIOR);
        selection.setCurrentLevel(1);
        selection.setCurrentLocationId("zona_langur_praca_das_vozes");
        return selection;
    }
}
