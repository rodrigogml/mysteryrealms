package br.eng.rodrigogml.mysteryrealms.application.world.repository;

import br.eng.rodrigogml.mysteryrealms.application.world.entity.WorldMarkerEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Repositório Spring Data JPA para {@link WorldMarkerEntity}.
 *
 * @author ?
 * @since 28-04-2026
 */
public interface WorldMarkerRepository extends JpaRepository<WorldMarkerEntity, Long> {

    /**
     * Busca um marcador em uma instância de mundo.
     *
     * @param idWorldInstance o ID da instância de mundo
     * @param markerId        o ID do marcador
     * @return o marcador encontrado, ou vazio
     */
    Optional<WorldMarkerEntity> findByIdWorldInstanceAndMarkerId(Long idWorldInstance, String markerId);

    /**
     * Remove todos os marcadores de uma instância de mundo.
     *
     * @param idWorldInstance o ID da instância de mundo
     */
    @Transactional
    void deleteAllByIdWorldInstance(Long idWorldInstance);
}
