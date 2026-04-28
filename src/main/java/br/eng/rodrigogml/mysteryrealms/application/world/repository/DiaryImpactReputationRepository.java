package br.eng.rodrigogml.mysteryrealms.application.world.repository;

import br.eng.rodrigogml.mysteryrealms.application.world.entity.DiaryImpactReputationEntity;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositório Spring Data JPA para {@link DiaryImpactReputationEntity}.
 *
 * @author ?
 * @since 28-04-2026
 */
public interface DiaryImpactReputationRepository extends JpaRepository<DiaryImpactReputationEntity, Long> {
}
