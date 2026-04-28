package br.eng.rodrigogml.mysteryrealms.application.coop.service;

import br.eng.rodrigogml.mysteryrealms.application.coop.entity.CoopParticipantEntity;
import br.eng.rodrigogml.mysteryrealms.application.coop.entity.CoopSessionEntity;
import br.eng.rodrigogml.mysteryrealms.application.coop.entity.CoopSessionStatus;
import br.eng.rodrigogml.mysteryrealms.application.coop.repository.CoopParticipantRepository;
import br.eng.rodrigogml.mysteryrealms.application.coop.repository.CoopSessionRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Serviço de aplicação responsável pelo gerenciamento de sessões cooperativas:
 * criação, entrada, saída, encerramento e listagem.
 *
 * @author ?
 * @since 28-04-2026
 */
public class CoopSessionService {

    private final CoopSessionRepository sessionRepository;
    private final CoopParticipantRepository participantRepository;

    /**
     * Cria o serviço com as dependências necessárias.
     *
     * @param sessionRepository     repositório de sessões cooperativas
     * @param participantRepository repositório de participantes
     */
    public CoopSessionService(CoopSessionRepository sessionRepository,
            CoopParticipantRepository participantRepository) {
        this.sessionRepository = sessionRepository;
        this.participantRepository = participantRepository;
    }

    /**
     * Cria uma nova sessão cooperativa e adiciona o personagem anfitrião como primeiro participante.
     *
     * @param hostCharacterId o ID do personagem anfitrião
     * @param worldInstanceId o ID da instância de mundo compartilhada
     * @param maxPlayers      o número máximo de jogadores (2 a 8)
     * @return a sessão cooperativa criada
     * @throws IllegalArgumentException se maxPlayers estiver fora do intervalo permitido
     */
    public CoopSessionEntity createSession(Long hostCharacterId, Long worldInstanceId, int maxPlayers) {
        if (maxPlayers < 2 || maxPlayers > 8) {
            throw new IllegalArgumentException("coop.error.invalidMaxPlayers");
        }

        CoopSessionEntity session = new CoopSessionEntity();
        session.setIdHostCharacter(hostCharacterId);
        session.setIdWorldInstance(worldInstanceId);
        session.setMaxPlayers(maxPlayers);
        session.setStatus(CoopSessionStatus.ACTIVE);
        session.setCreatedAt(LocalDateTime.now());
        session = sessionRepository.save(session);

        CoopParticipantEntity host = new CoopParticipantEntity();
        host.setIdCoopSession(session.getId());
        host.setIdCharacter(hostCharacterId);
        host.setJoinedAt(LocalDateTime.now());
        participantRepository.save(host);

        return session;
    }

    /**
     * Adiciona um personagem como participante de uma sessão cooperativa ativa.
     *
     * @param sessionId   o ID da sessão
     * @param characterId o ID do personagem que entra
     * @throws IllegalArgumentException se a sessão não existir, não estiver ativa, estiver cheia
     *                                  ou o personagem já for participante ativo
     */
    public void joinSession(Long sessionId, Long characterId) {
        CoopSessionEntity session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("coop.error.sessionNotFound"));

        if (session.getStatus() != CoopSessionStatus.ACTIVE) {
            throw new IllegalArgumentException("coop.error.sessionNotActive");
        }

        long count = participantRepository.countByIdCoopSessionAndLeftAtIsNull(sessionId);
        if (count >= session.getMaxPlayers()) {
            throw new IllegalArgumentException("coop.error.sessionFull");
        }

        if (participantRepository.findByIdCoopSessionAndIdCharacterAndLeftAtIsNull(sessionId, characterId).isPresent()) {
            throw new IllegalArgumentException("coop.error.alreadyParticipant");
        }

        CoopParticipantEntity participant = new CoopParticipantEntity();
        participant.setIdCoopSession(sessionId);
        participant.setIdCharacter(characterId);
        participant.setJoinedAt(LocalDateTime.now());
        participantRepository.save(participant);
    }

    /**
     * Remove um participante de uma sessão cooperativa (soft-delete via leftAt).
     * Caso o personagem seja o anfitrião, a sessão é encerrada.
     *
     * @param sessionId   o ID da sessão
     * @param characterId o ID do personagem que sai
     * @throws IllegalArgumentException se a sessão ou o participante não existirem como ativos
     */
    public void leaveSession(Long sessionId, Long characterId) {
        CoopSessionEntity session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("coop.error.sessionNotFound"));

        CoopParticipantEntity participant = participantRepository
                .findByIdCoopSessionAndIdCharacterAndLeftAtIsNull(sessionId, characterId)
                .orElseThrow(() -> new IllegalArgumentException("coop.error.notParticipant"));

        participant.setLeftAt(LocalDateTime.now());
        participantRepository.save(participant);

        // Se o anfitrião saiu, encerra a sessão
        if (session.getIdHostCharacter().equals(characterId)) {
            session.setStatus(CoopSessionStatus.CLOSED);
            sessionRepository.save(session);
        }
    }

    /**
     * Encerra uma sessão cooperativa, registrando a saída de todos os participantes ativos.
     *
     * @param sessionId       o ID da sessão
     * @param hostCharacterId o ID do personagem anfitrião que está encerrando a sessão
     * @throws IllegalArgumentException se a sessão não existir ou o personagem não for o anfitrião
     */
    public void closeSession(Long sessionId, Long hostCharacterId) {
        CoopSessionEntity session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("coop.error.sessionNotFound"));

        if (!session.getIdHostCharacter().equals(hostCharacterId)) {
            throw new IllegalArgumentException("coop.error.notHost");
        }

        LocalDateTime now = LocalDateTime.now();
        List<CoopParticipantEntity> active = participantRepository
                .findAllByIdCoopSessionAndLeftAtIsNull(sessionId);
        for (CoopParticipantEntity p : active) {
            p.setLeftAt(now);
        }
        participantRepository.saveAll(active);

        session.setStatus(CoopSessionStatus.CLOSED);
        sessionRepository.save(session);
    }

    /**
     * Lista os participantes ativos de uma sessão cooperativa.
     *
     * @param sessionId o ID da sessão
     * @return lista de participantes ativos
     */
    public List<CoopParticipantEntity> listParticipants(Long sessionId) {
        return participantRepository.findAllByIdCoopSessionAndLeftAtIsNull(sessionId);
    }

    /**
     * Lista todas as sessões cooperativas ativas.
     *
     * @return lista de sessões ativas
     */
    public List<CoopSessionEntity> listActiveSessions() {
        return sessionRepository.findAllByStatus(CoopSessionStatus.ACTIVE);
    }

    /**
     * Verifica se um personagem está participando de alguma sessão cooperativa ativa.
     *
     * @param characterId o ID do personagem
     * @return {@code true} se o personagem estiver em alguma sessão ativa
     */
    public boolean isParticipating(Long characterId) {
        List<CoopParticipantEntity> participations = participantRepository.findAllByIdCharacter(characterId);
        for (CoopParticipantEntity p : participations) {
            if (p.getLeftAt() != null) {
                continue;
            }
            CoopSessionEntity session = sessionRepository.findById(p.getIdCoopSession()).orElse(null);
            if (session != null && session.getStatus() == CoopSessionStatus.ACTIVE) {
                return true;
            }
        }
        return false;
    }
}

