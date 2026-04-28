package br.eng.rodrigogml.mysteryrealms.application.coop.repository;

import br.eng.rodrigogml.mysteryrealms.application.coop.entity.CoopParticipantEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório Spring Data JPA para {@link CoopParticipantEntity}.
 *
 * @author ?
 * @since 28-04-2026
 */
public interface CoopParticipantRepository extends JpaRepository<CoopParticipantEntity, Long> {

    /**
     * Lista os participantes ativos (sem data de saída) de uma sessão co-op.
     *
     * @param idCoopSession o ID da sessão co-op
     * @return lista de participantes ativos
     */
    List<CoopParticipantEntity> findAllByIdCoopSessionAndLeftAtIsNull(Long idCoopSession);

    /**
     * Lista todos os participantes de uma sessão co-op (incluindo os que saíram).
     *
     * @param idCoopSession o ID da sessão co-op
     * @return lista de participantes
     */
    List<CoopParticipantEntity> findAllByIdCoopSession(Long idCoopSession);

    /**
     * Lista todas as participações de um personagem (em qualquer sessão).
     *
     * @param idCharacter o ID do personagem
     * @return lista de participações
     */
    List<CoopParticipantEntity> findAllByIdCharacter(Long idCharacter);

    /**
     * Busca o participante ativo de uma sessão co-op pelo personagem.
     *
     * @param idCoopSession o ID da sessão co-op
     * @param idCharacter   o ID do personagem
     * @return o participante encontrado, ou vazio
     */
    Optional<CoopParticipantEntity> findByIdCoopSessionAndIdCharacterAndLeftAtIsNull(Long idCoopSession, Long idCharacter);

    /**
     * Conta os participantes ativos de uma sessão co-op.
     *
     * @param idCoopSession o ID da sessão co-op
     * @return quantidade de participantes ativos
     */
    long countByIdCoopSessionAndLeftAtIsNull(Long idCoopSession);
}
