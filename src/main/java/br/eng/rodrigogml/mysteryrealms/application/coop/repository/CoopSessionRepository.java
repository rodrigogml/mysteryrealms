package br.eng.rodrigogml.mysteryrealms.application.coop.repository;

import br.eng.rodrigogml.mysteryrealms.application.coop.entity.CoopSessionEntity;
import br.eng.rodrigogml.mysteryrealms.application.coop.entity.CoopSessionStatus;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositório Spring Data JPA para {@link CoopSessionEntity}.
 *
 * @author ?
 * @since 28-04-2026
 */
public interface CoopSessionRepository extends JpaRepository<CoopSessionEntity, Long> {

    /**
     * Busca todas as sessões com um determinado status.
     *
     * @param status o status desejado
     * @return lista de sessões
     */
    List<CoopSessionEntity> findAllByStatus(CoopSessionStatus status);
}
