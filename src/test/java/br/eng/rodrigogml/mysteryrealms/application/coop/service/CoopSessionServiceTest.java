package br.eng.rodrigogml.mysteryrealms.application.coop.service;

import br.eng.rodrigogml.mysteryrealms.application.coop.entity.CoopParticipantEntity;
import br.eng.rodrigogml.mysteryrealms.application.coop.entity.CoopSessionEntity;
import br.eng.rodrigogml.mysteryrealms.application.coop.entity.CoopSessionStatus;
import br.eng.rodrigogml.mysteryrealms.application.coop.repository.CoopParticipantRepository;
import br.eng.rodrigogml.mysteryrealms.application.coop.repository.CoopSessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Testes do serviço de sessões cooperativas — RF-MJ-01 a RF-MJ-07.
 *
 * @author ?
 * @since 28-04-2026
 */
@ExtendWith(MockitoExtension.class)
class CoopSessionServiceTest {

    @Mock private CoopSessionRepository sessionRepository;
    @Mock private CoopParticipantRepository participantRepository;

    private CoopSessionService service;

    @BeforeEach
    void setUp() {
        service = new CoopSessionService(sessionRepository, participantRepository);
    }

    // ── RF-MJ-01: Modo solo / listagem ───────────────────────────────────────

    @Test
    void isParticipating_semSessaoAtiva_retornaFalso() {
        when(participantRepository.findAllByIdCharacter(1L)).thenReturn(List.of());
        assertFalse(service.isParticipating(1L));
    }

    @Test
    void isParticipating_participanteEmSessaoAtiva_retornaVerdadeiro() {
        CoopParticipantEntity p = new CoopParticipantEntity();
        p.setIdCoopSession(10L);
        p.setIdCharacter(1L);
        p.setJoinedAt(LocalDateTime.now());
        when(participantRepository.findAllByIdCharacter(1L)).thenReturn(List.of(p));

        CoopSessionEntity session = activeSession(10L, 1L);
        when(sessionRepository.findById(10L)).thenReturn(Optional.of(session));

        assertTrue(service.isParticipating(1L));
    }

    @Test
    void listActiveSessions_retornaApenasAtivas() {
        CoopSessionEntity s = activeSession(1L, 1L);
        when(sessionRepository.findAllByStatus(CoopSessionStatus.ACTIVE)).thenReturn(List.of(s));

        List<CoopSessionEntity> result = service.listActiveSessions();

        assertEquals(1, result.size());
        assertEquals(CoopSessionStatus.ACTIVE, result.get(0).getStatus());
    }

    // ── RF-MJ-02: Convite de jogadores (criação de sessão) ───────────────────

    @Test
    void createSession_parametrosValidos_criaSessaoComAnfitriao() {
        CoopSessionEntity saved = activeSession(1L, 10L);
        saved.setId(1L);
        when(sessionRepository.save(any())).thenReturn(saved);
        when(participantRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        CoopSessionEntity result = service.createSession(10L, 5L, 4);

        assertEquals(CoopSessionStatus.ACTIVE, result.getStatus());
        verify(participantRepository).save(argThat(p -> p.getIdCharacter().equals(10L)));
    }

    @Test
    void createSession_maxPlayersMenorQue2_lancaExcecao() {
        assertThrows(IllegalArgumentException.class, () -> service.createSession(1L, 1L, 1));
    }

    @Test
    void createSession_maxPlayersMaiorQue4_lancaExcecao() {
        assertThrows(IllegalArgumentException.class, () -> service.createSession(1L, 1L, 5));
    }

    @Test
    void createSession_maxPlayersIgual4_permitido() {
        CoopSessionEntity saved = activeSession(1L, 10L);
        when(sessionRepository.save(any())).thenReturn(saved);
        when(participantRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        assertDoesNotThrow(() -> service.createSession(10L, 5L, 4));
    }

    @Test
    void joinSession_salaComVaga_adicionaParticipante() {
        CoopSessionEntity session = activeSession(1L, 1L);
        session.setMaxPlayers(4);
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(participantRepository.countByIdCoopSessionAndLeftAtIsNull(1L)).thenReturn(1L);
        when(participantRepository.findByIdCoopSessionAndIdCharacterAndLeftAtIsNull(1L, 2L)).thenReturn(Optional.empty());
        when(participantRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        service.joinSession(1L, 2L);

        verify(participantRepository).save(argThat(p -> p.getIdCharacter().equals(2L)));
    }

    @Test
    void joinSession_salaCheiaComMaxPlayers4_lancaExcecao() {
        CoopSessionEntity session = activeSession(1L, 1L);
        session.setMaxPlayers(4);
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(participantRepository.countByIdCoopSessionAndLeftAtIsNull(1L)).thenReturn(4L);

        assertThrows(IllegalArgumentException.class, () -> service.joinSession(1L, 5L));
    }

    @Test
    void joinSession_sessaoNaoAtiva_lancaExcecao() {
        CoopSessionEntity session = new CoopSessionEntity();
        session.setStatus(CoopSessionStatus.CLOSED);
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

        assertThrows(IllegalArgumentException.class, () -> service.joinSession(1L, 2L));
    }

    @Test
    void joinSession_jaEstaParticipando_lancaExcecao() {
        CoopSessionEntity session = activeSession(1L, 1L);
        session.setMaxPlayers(4);
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(participantRepository.countByIdCoopSessionAndLeftAtIsNull(1L)).thenReturn(1L);
        CoopParticipantEntity existing = new CoopParticipantEntity();
        when(participantRepository.findByIdCoopSessionAndIdCharacterAndLeftAtIsNull(1L, 2L)).thenReturn(Optional.of(existing));

        assertThrows(IllegalArgumentException.class, () -> service.joinSession(1L, 2L));
    }

    // ── RF-MJ-02: Saída e encerramento ───────────────────────────────────────

    @Test
    void leaveSession_participanteConvidado_marcaLeftAt() {
        CoopSessionEntity session = activeSession(1L, 1L);
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

        CoopParticipantEntity participant = new CoopParticipantEntity();
        participant.setIdCoopSession(1L);
        participant.setIdCharacter(2L);
        participant.setJoinedAt(LocalDateTime.now());
        when(participantRepository.findByIdCoopSessionAndIdCharacterAndLeftAtIsNull(1L, 2L))
                .thenReturn(Optional.of(participant));
        when(participantRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        service.leaveSession(1L, 2L);

        assertNotNull(participant.getLeftAt());
        // Sessão não encerrada pois o convidado saiu, não o anfitrião
        verify(sessionRepository, never()).save(any());
    }

    @Test
    void leaveSession_anfitriao_encerraSessao() {
        CoopSessionEntity session = activeSession(1L, 1L);
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

        CoopParticipantEntity hostParticipant = new CoopParticipantEntity();
        hostParticipant.setIdCharacter(1L);
        hostParticipant.setJoinedAt(LocalDateTime.now());
        when(participantRepository.findByIdCoopSessionAndIdCharacterAndLeftAtIsNull(1L, 1L))
                .thenReturn(Optional.of(hostParticipant));
        when(participantRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(sessionRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        service.leaveSession(1L, 1L);

        verify(sessionRepository).save(argThat(s -> s.getStatus() == CoopSessionStatus.CLOSED));
    }

    @Test
    void closeSession_somentePeloAnfitriao_encerraSessao() {
        CoopSessionEntity session = activeSession(1L, 1L);
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(participantRepository.findAllByIdCoopSessionAndLeftAtIsNull(1L)).thenReturn(List.of());
        when(sessionRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        service.closeSession(1L, 1L);

        verify(sessionRepository).save(argThat(s -> s.getStatus() == CoopSessionStatus.CLOSED));
    }

    @Test
    void closeSession_naoAnfitriao_lancaExcecao() {
        CoopSessionEntity session = activeSession(1L, 1L);
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

        assertThrows(IllegalArgumentException.class, () -> service.closeSession(1L, 99L));
    }

    // ── RF-MJ-06: Limite de 4 jogadores ──────────────────────────────────────

    @Test
    void createSession_maxPlayers2_permitido() {
        CoopSessionEntity saved = activeSession(1L, 10L);
        when(sessionRepository.save(any())).thenReturn(saved);
        when(participantRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        assertDoesNotThrow(() -> service.createSession(10L, 5L, 2));
    }

    @Test
    void createSession_maxPlayers3_permitido() {
        CoopSessionEntity saved = activeSession(1L, 10L);
        when(sessionRepository.save(any())).thenReturn(saved);
        when(participantRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        assertDoesNotThrow(() -> service.createSession(10L, 5L, 3));
    }

    // ── RF-MJ-07: Desconexão ─────────────────────────────────────────────────

    @Test
    void listParticipants_retornaApenasAtivos() {
        CoopParticipantEntity p = new CoopParticipantEntity();
        p.setIdCharacter(2L);
        when(participantRepository.findAllByIdCoopSessionAndLeftAtIsNull(1L)).thenReturn(List.of(p));

        List<CoopParticipantEntity> result = service.listParticipants(1L);

        assertEquals(1, result.size());
        assertNull(result.get(0).getLeftAt());
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private CoopSessionEntity activeSession(Long id, Long hostCharacterId) {
        CoopSessionEntity s = new CoopSessionEntity();
        s.setId(id);
        s.setIdHostCharacter(hostCharacterId);
        s.setIdWorldInstance(99L);
        s.setMaxPlayers(4);
        s.setStatus(CoopSessionStatus.ACTIVE);
        s.setCreatedAt(LocalDateTime.now());
        return s;
    }
}
