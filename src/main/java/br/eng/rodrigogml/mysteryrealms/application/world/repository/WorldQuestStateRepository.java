package br.eng.rodrigogml.mysteryrealms.application.world.repository;

import br.eng.rodrigogml.mysteryrealms.application.world.entity.WorldQuestStateEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Repositório Spring Data JPA para {@link WorldQuestStateEntity}.
 *
 * @author ?
 * @since 28-04-2026
 */
public interface WorldQuestStateRepository extends JpaRepository<WorldQuestStateEntity, Long> {

    /**
     * Busca o estado de uma quest em uma instância de mundo.
     *
     * @param idWorldInstance o ID da instância de mundo
     * @param questId         o ID da quest
     * @return o estado encontrado, ou vazio
     */
    Optional<WorldQuestStateEntity> findByIdWorldInstanceAndQuestId(Long idWorldInstance, String questId);

    /**
     * Remove todos os estados de quests de uma instância de mundo.
     *
     * @param idWorldInstance o ID da instância de mundo
     */
    @Transactional
    void deleteAllByIdWorldInstance(Long idWorldInstance);
}
