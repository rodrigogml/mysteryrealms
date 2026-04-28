package br.eng.rodrigogml.mysteryrealms.application.world.repository;

import br.eng.rodrigogml.mysteryrealms.application.world.entity.WorldLocationStateEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Repositório Spring Data JPA para {@link WorldLocationStateEntity}.
 *
 * @author ?
 * @since 28-04-2026
 */
public interface WorldLocationStateRepository extends JpaRepository<WorldLocationStateEntity, Long> {

    /**
     * Busca o estado de uma localidade em uma instância de mundo.
     *
     * @param idWorldInstance o ID da instância de mundo
     * @param locationId      o ID da localidade
     * @return o estado encontrado, ou vazio
     */
    Optional<WorldLocationStateEntity> findByIdWorldInstanceAndLocationId(Long idWorldInstance, String locationId);

    /**
     * Remove todos os estados de localidades de uma instância de mundo.
     *
     * @param idWorldInstance o ID da instância de mundo
     */
    @Transactional
    void deleteAllByIdWorldInstance(Long idWorldInstance);
}
