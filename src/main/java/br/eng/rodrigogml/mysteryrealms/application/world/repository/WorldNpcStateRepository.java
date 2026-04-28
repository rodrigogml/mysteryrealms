package br.eng.rodrigogml.mysteryrealms.application.world.repository;

import br.eng.rodrigogml.mysteryrealms.application.world.entity.WorldNpcStateEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Repositório Spring Data JPA para {@link WorldNpcStateEntity}.
 *
 * @author ?
 * @since 28-04-2026
 */
public interface WorldNpcStateRepository extends JpaRepository<WorldNpcStateEntity, Long> {

    /**
     * Busca o estado de um NPC em uma instância de mundo.
     *
     * @param idWorldInstance o ID da instância de mundo
     * @param npcId           o ID do NPC
     * @return o estado encontrado, ou vazio
     */
    Optional<WorldNpcStateEntity> findByIdWorldInstanceAndNpcId(Long idWorldInstance, String npcId);

    /**
     * Remove todos os estados de NPCs de uma instância de mundo.
     *
     * @param idWorldInstance o ID da instância de mundo
     */
    @Transactional
    void deleteAllByIdWorldInstance(Long idWorldInstance);
}
