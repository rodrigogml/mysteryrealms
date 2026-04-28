package br.eng.rodrigogml.mysteryrealms.application.world.repository;

import br.eng.rodrigogml.mysteryrealms.application.world.entity.DiaryImpactMarkerEntity;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositório Spring Data JPA para {@link DiaryImpactMarkerEntity}.
 *
 * @author ?
 * @since 28-04-2026
 */
public interface DiaryImpactMarkerRepository extends JpaRepository<DiaryImpactMarkerEntity, Long> {
}
