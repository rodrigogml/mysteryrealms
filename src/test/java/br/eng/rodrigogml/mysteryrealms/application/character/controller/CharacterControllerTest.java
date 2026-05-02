package br.eng.rodrigogml.mysteryrealms.application.character.controller;

import br.eng.rodrigogml.mysteryrealms.application.character.dto.CharacterDeletionDTO;
import br.eng.rodrigogml.mysteryrealms.application.character.dto.CharacterRenameDTO;
import br.eng.rodrigogml.mysteryrealms.application.character.dto.CharacterSummaryDTO;
import br.eng.rodrigogml.mysteryrealms.application.character.entity.CharacterEntity;
import br.eng.rodrigogml.mysteryrealms.application.character.service.CharacterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CharacterControllerTest {

    @Mock
    private CharacterService characterService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new CharacterController(characterService)).build();
    }

    @Test
    void list_retorna200() throws Exception {
        CharacterSummaryDTO summary = new CharacterSummaryDTO();
        summary.setId(1L);
        when(characterService.listCharacterSummaries(10L)).thenReturn(List.of(summary));

        mockMvc.perform(get("/api/characters").param("userId", "10"))
                .andExpect(status().isOk());

        verify(characterService).listCharacterSummaries(10L);
    }

    @Test
    void createAndSelect_retorna201() throws Exception {
        CharacterEntity entity = new CharacterEntity();
        entity.setId(99L);
        when(characterService.createAndSelectCharacter(any(), any())).thenReturn(entity);

        mockMvc.perform(post("/api/characters")
                        .param("userId", "10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Aragorn\",\"surname\":\"Elessar\",\"gender\":\"MALE\",\"initialAge\":35,\"race\":\"HUMAN\",\"characterClass\":\"WARRIOR\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    void rename_retorna200() throws Exception {
        mockMvc.perform(patch("/api/characters/1/name")
                        .param("userId", "10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"newName\":\"Bilbo\"}"))
                .andExpect(status().isOk());

        verify(characterService).renameCharacter(any(), any(), any(CharacterRenameDTO.class));
    }

    @Test
    void delete_retorna204() throws Exception {
        mockMvc.perform(delete("/api/characters/1")
                        .param("userId", "10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"confirmationText\":\"Sam\"}"))
                .andExpect(status().isNoContent());

        verify(characterService).deleteCharacter(any(), any(), any(CharacterDeletionDTO.class));
    }
}
