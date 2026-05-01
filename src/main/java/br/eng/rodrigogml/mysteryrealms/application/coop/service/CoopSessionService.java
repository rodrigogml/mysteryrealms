package br.eng.rodrigogml.mysteryrealms.application.coop.service;

import br.eng.rodrigogml.mysteryrealms.application.character.entity.CharacterEntity;
import br.eng.rodrigogml.mysteryrealms.application.character.repository.CharacterRepository;
import br.eng.rodrigogml.mysteryrealms.application.coop.entity.CoopParticipantEntity;
import br.eng.rodrigogml.mysteryrealms.application.coop.entity.CoopSessionEntity;
import br.eng.rodrigogml.mysteryrealms.application.coop.entity.CoopSessionStatus;
import br.eng.rodrigogml.mysteryrealms.application.coop.repository.CoopParticipantRepository;
import br.eng.rodrigogml.mysteryrealms.application.coop.repository.CoopSessionRepository;
import br.eng.rodrigogml.mysteryrealms.application.world.entity.WorldInstanceEntity;
import br.eng.rodrigogml.mysteryrealms.application.world.repository.WorldInstanceRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import br.eng.rodrigogml.mysteryrealms.common.exception.DomainException;
import br.eng.rodrigogml.mysteryrealms.common.exception.ValidationException;

/**
 * Serviço de aplicação responsável pelo gerenciamento de sessões cooperativas:
 * criação, entrada, saída, encerramento, listagem e regras de convidado (RF-MJ-03 a RF-MJ-05).
 *
 * @author ?
 * @since 28-04-2026
 */
@Service
@Transactional
public class CoopSessionService {

    private final CoopSessionRepository sessionRepository;
    private final CoopParticipantRepository participantRepository;
    private final CharacterRepository characterRepository;
    private final WorldInstanceRepository worldInstanceRepository;

    /**
     * Cria o serviço com as dependências necessárias.
     *
     * @param sessionRepository       repositório de sessões cooperativas
     * @param participantRepository   repositório de participantes
     * @param characterRepository     repositório de personagens
     * @param worldInstanceRepository repositório de instâncias de mundo
     */
    public CoopSessionService(CoopSessionRepository sessionRepository,
            CoopParticipantRepository participantRepository,
            CharacterRepository characterRepository,
            WorldInstanceRepository worldInstanceRepository) {
        this.sessionRepository = sessionRepository;
        this.participantRepository = participantRepository;
        this.characterRepository = characterRepository;
        this.worldInstanceRepository = worldInstanceRepository;
    }

    /**
     * Cria uma nova sessão cooperativa e adiciona o personagem anfitrião como primeiro participante.
     *
     * @param hostCharacterId o ID do personagem anfitrião
     * @param worldInstanceId o ID da instância de mundo compartilhada
     * @param maxPlayers      o número máximo de jogadores (2 a 4)
     * @return a sessão cooperativa criada
     * @throws IllegalArgumentException se maxPlayers estiver fora do intervalo permitido
     */
    public CoopSessionEntity createSession(Long hostCharacterId, Long worldInstanceId, int maxPlayers) {
        requirePositiveId(hostCharacterId, "coop.error.invalidHostCharacterId");
        requirePositiveId(worldInstanceId, "coop.error.invalidWorldInstanceId");
        if (maxPlayers < 2 || maxPlayers > 4) {
            throw new ValidationException("coop.error.invalidMaxPlayers");
        }
        requireHostWorld(hostCharacterId, worldInstanceId);

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
        requirePositiveId(sessionId, "coop.error.invalidSessionId");
        requirePositiveId(characterId, "coop.error.invalidCharacterId");

        CoopSessionEntity session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ValidationException("coop.error.sessionNotFound"));

        if (session.getStatus() != CoopSessionStatus.ACTIVE) {
            throw new ValidationException("coop.error.sessionNotActive");
        }

        long count = participantRepository.countByIdCoopSessionAndLeftAtIsNull(sessionId);
        if (count >= session.getMaxPlayers()) {
            throw new ValidationException("coop.error.playerLimitReached");
        }

        if (participantRepository.findByIdCoopSessionAndIdCharacterAndLeftAtIsNull(sessionId, characterId).isPresent()) {
            throw new ValidationException("coop.error.alreadyInSession");
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
        requirePositiveId(sessionId, "coop.error.invalidSessionId");
        requirePositiveId(characterId, "coop.error.invalidCharacterId");

        CoopSessionEntity session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ValidationException("coop.error.sessionNotFound"));
        requireActiveSession(session);

        CoopParticipantEntity participant = participantRepository
                .findByIdCoopSessionAndIdCharacterAndLeftAtIsNull(sessionId, characterId)
                .orElseThrow(() -> new ValidationException("coop.error.participantNotFound"));

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
        requirePositiveId(sessionId, "coop.error.invalidSessionId");
        requirePositiveId(hostCharacterId, "coop.error.invalidHostCharacterId");

        CoopSessionEntity session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ValidationException("coop.error.sessionNotFound"));
        requireActiveSession(session);

        if (!session.getIdHostCharacter().equals(hostCharacterId)) {
            throw new ValidationException("coop.error.notHost");
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
        requirePositiveId(sessionId, "coop.error.invalidSessionId");

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
        requirePositiveId(characterId, "coop.error.invalidCharacterId");

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

    /**
     * Retorna o {@link CharacterEntity} de um participante ativo da sessão, confirmando que o
     * convidado entra no mundo do anfitrião com o <em>seu próprio personagem</em> — RF-MJ-03.
     *
     * @param sessionId   o ID da sessão co-op ativa
     * @param characterId o ID do personagem do convidado
     * @return o personagem do convidado
     * @throws IllegalArgumentException se a sessão não existir, não estiver ativa, o participante
     *                                  não estiver na sessão ou o personagem não for encontrado
     */
    public CharacterEntity getParticipantCharacter(Long sessionId, Long characterId) {
        requirePositiveId(sessionId, "coop.error.invalidSessionId");
        requirePositiveId(characterId, "coop.error.invalidCharacterId");

        CoopSessionEntity session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ValidationException("coop.error.sessionNotFound"));

        requireActiveSession(session);

        participantRepository.findByIdCoopSessionAndIdCharacterAndLeftAtIsNull(sessionId, characterId)
                .orElseThrow(() -> new ValidationException("coop.error.participantNotFound"));

        return characterRepository.findById(characterId)
                .orElseThrow(() -> new ValidationException("character.error.notFound"));
    }

    /**
     * Persiste os dados pessoais do personagem convidado após alterações ocorridas durante a
     * sessão co-op — RF-MJ-04.
     *
     * <p>Os dados pessoais (PV, fadiga, fome, sede, morale, XP, nível, moedas, versão de
     * balanceamento) são de propriedade do convidado e devem ser salvos no
     * {@link CharacterEntity} do convidado, independentemente de qual mundo foi jogado.</p>
     *
     * @param sessionId           o ID da sessão co-op ativa
     * @param guestCharacterId    o ID do personagem convidado (não pode ser o anfitrião)
     * @param updatedCharacter    o personagem com os dados atualizados a persistir
     * @throws IllegalArgumentException se a sessão não existir, o personagem for o anfitrião
     *                                  ou o participante não estiver ativo na sessão
     */
    public void syncGuestPersonalData(Long sessionId, Long guestCharacterId,
            CharacterEntity updatedCharacter) {
        requirePositiveId(sessionId, "coop.error.invalidSessionId");
        requirePositiveId(guestCharacterId, "coop.error.invalidCharacterId");
        requireCharacterSnapshot(guestCharacterId, updatedCharacter);

        CoopSessionEntity session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ValidationException("coop.error.sessionNotFound"));
        requireActiveSession(session);

        if (session.getIdHostCharacter().equals(guestCharacterId)) {
            throw new ValidationException("coop.error.hostCannotBeGuest");
        }

        participantRepository.findByIdCoopSessionAndIdCharacterAndLeftAtIsNull(sessionId, guestCharacterId)
                .orElseThrow(() -> new ValidationException("coop.error.participantNotFound"));

        characterRepository.save(updatedCharacter);
    }

    /**
     * Retorna a instância de mundo <em>própria</em> do personagem convidado — RF-MJ-05.
     *
     * <p>O mundo do convidado é completamente isolado do mundo da sessão: NPCs, missões,
     * localidades e marcadores do mundo do anfitrião não se transferem para o mundo do
     * convidado.</p>
     *
     * @param sessionId        o ID da sessão co-op ativa
     * @param guestCharacterId o ID do personagem convidado
     * @return a instância de mundo própria do convidado
     * @throws IllegalArgumentException se a sessão não existir, o participante não estiver ativo
     *                                  ou a instância de mundo do convidado não existir
     */
    public WorldInstanceEntity getGuestOwnWorldInstance(Long sessionId, Long guestCharacterId) {
        requirePositiveId(sessionId, "coop.error.invalidSessionId");
        requirePositiveId(guestCharacterId, "coop.error.invalidCharacterId");

        CoopSessionEntity session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ValidationException("coop.error.sessionNotFound"));

        requireActiveSession(session);

        participantRepository.findByIdCoopSessionAndIdCharacterAndLeftAtIsNull(sessionId, guestCharacterId)
                .orElseThrow(() -> new ValidationException("coop.error.participantNotFound"));

        WorldInstanceEntity guestWorld = worldInstanceRepository.findByIdCharacter(guestCharacterId)
                .orElseThrow(() -> new ValidationException("world.error.instanceNotFound"));

        // Garante que a instância do convidado é diferente da instância da sessão (mundo do anfitrião)
        if (guestWorld.getId().equals(session.getIdWorldInstance())) {
            throw new DomainException("world.error.guestWorldConflict");
        }

        return guestWorld;
    }

    /**
     * Registra a desconexão inesperada de um jogador convidado — RF-MJ-07.
     *
     * <p>Os dados pessoais do convidado são salvos automaticamente no estado mais recente
     * conhecido antes de marcar a saída da sessão. A sessão do anfitrião continua normalmente
     * sem o jogador desconectado.</p>
     *
     * @param sessionId          o ID da sessão co-op
     * @param guestCharacterId   o ID do personagem convidado (não pode ser o anfitrião)
     * @param lastKnownCharacter o estado mais recente do personagem antes da desconexão
     * @throws IllegalArgumentException se a sessão não existir, o personagem for o anfitrião
     *                                  ou o participante não estiver ativo na sessão
     */
    public void disconnectGuest(Long sessionId, Long guestCharacterId,
            CharacterEntity lastKnownCharacter) {
        requirePositiveId(sessionId, "coop.error.invalidSessionId");
        requirePositiveId(guestCharacterId, "coop.error.invalidCharacterId");
        requireCharacterSnapshot(guestCharacterId, lastKnownCharacter);

        CoopSessionEntity session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ValidationException("coop.error.sessionNotFound"));
        requireActiveSession(session);

        if (session.getIdHostCharacter().equals(guestCharacterId)) {
            throw new ValidationException("coop.error.hostCannotBeGuest");
        }

        CoopParticipantEntity participant = participantRepository
                .findByIdCoopSessionAndIdCharacterAndLeftAtIsNull(sessionId, guestCharacterId)
                .orElseThrow(() -> new ValidationException("coop.error.participantNotFound"));

        // Persiste os dados pessoais do convidado no ponto da última ação sincronizada
        characterRepository.save(lastKnownCharacter);

        participant.setLeftAt(LocalDateTime.now());
        participantRepository.save(participant);
    }

    // ── Helpers privados ─────────────────────────────────────────────────────

    /**
     * Valida que a sessão está ativa.
     *
     * @param session a sessão a validar
     * @throws IllegalArgumentException se a sessão não estiver ativa
     */
    private void requireActiveSession(CoopSessionEntity session) {
        if (session.getStatus() != CoopSessionStatus.ACTIVE) {
            throw new ValidationException("coop.error.sessionNotActive");
        }
    }

    private void requireHostWorld(Long hostCharacterId, Long worldInstanceId) {
        WorldInstanceEntity worldInstance = worldInstanceRepository.findById(worldInstanceId)
                .orElseThrow(() -> new ValidationException("world.error.instanceNotFound"));
        if (!hostCharacterId.equals(worldInstance.getIdCharacter())) {
            throw new ValidationException("coop.error.hostWorldMismatch");
        }
    }

    private void requireCharacterSnapshot(Long expectedCharacterId, CharacterEntity snapshot) {
        if (snapshot == null || !expectedCharacterId.equals(snapshot.getId())) {
            throw new ValidationException("coop.error.invalidCharacterSnapshot");
        }
    }

    private void requirePositiveId(Long id, String message) {
        if (id == null || id <= 0L) {
            throw new ValidationException(message);
        }
    }
}
