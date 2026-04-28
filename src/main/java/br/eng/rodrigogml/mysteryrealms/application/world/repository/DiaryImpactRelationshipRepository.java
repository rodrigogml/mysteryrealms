package br.eng.rodrigogml.mysteryrealms.application.world.repository;

import br.eng.rodrigogml.mysteryrealms.application.world.entity.DiaryImpactRelationshipEntity;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositório Spring Data JPA para {@link DiaryImpactRelationshipEntity}.
 *
 * @author ?
 * @since 28-04-2026
 */
public interface DiaryImpactRelationshipRepository extends JpaRepository<DiaryImpactRelationshipEntity, Long> {
}
