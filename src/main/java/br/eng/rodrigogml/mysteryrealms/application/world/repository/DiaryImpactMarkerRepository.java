package br.eng.rodrigogml.mysteryrealms.application.world.repository;

import br.eng.rodrigogml.mysteryrealms.application.world.entity.DiaryImpactMarkerEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Repositório Spring Data JPA para {@link DiaryImpactMarkerEntity}.
 *
 * @author ?
 * @since 28-04-2026
 */
public interface DiaryImpactMarkerRepository extends JpaRepository<DiaryImpactMarkerEntity, Long> {

    /**
     * Remove todos os impactos de marcador vinculados a uma entrada de diário.
     *
     * @param idDiaryEntry o ID da entrada de diário
     */
    @Transactional
    void deleteAllByIdDiaryEntry(Long idDiaryEntry);
}
