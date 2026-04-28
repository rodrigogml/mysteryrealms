package br.eng.rodrigogml.mysteryrealms.application.world.repository;

import br.eng.rodrigogml.mysteryrealms.application.world.entity.DiaryImpactRelationshipEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Repositório Spring Data JPA para {@link DiaryImpactRelationshipEntity}.
 *
 * @author ?
 * @since 28-04-2026
 */
public interface DiaryImpactRelationshipRepository extends JpaRepository<DiaryImpactRelationshipEntity, Long> {

    /**
     * Remove todos os impactos de relacionamento vinculados a uma entrada de diário.
     *
     * @param idDiaryEntry o ID da entrada de diário
     */
    @Transactional
    void deleteAllByIdDiaryEntry(Long idDiaryEntry);
}
