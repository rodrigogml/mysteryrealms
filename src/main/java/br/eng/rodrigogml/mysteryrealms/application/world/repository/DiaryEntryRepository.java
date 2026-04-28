package br.eng.rodrigogml.mysteryrealms.application.world.repository;

import br.eng.rodrigogml.mysteryrealms.application.world.entity.DiaryEntryEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Repositório Spring Data JPA para {@link DiaryEntryEntity}.
 *
 * @author ?
 * @since 28-04-2026
 */
public interface DiaryEntryRepository extends JpaRepository<DiaryEntryEntity, Long> {

    /**
     * Lista todas as entradas de diário de uma instância de mundo.
     *
     * @param idWorldInstance o ID da instância de mundo
     * @return lista de entradas registradas
     */
    List<DiaryEntryEntity> findAllByIdWorldInstance(Long idWorldInstance);

    /**
     * Remove todas as entradas de diário de uma instância de mundo.
     *
     * @param idWorldInstance o ID da instância de mundo
     */
    @Transactional
    void deleteAllByIdWorldInstance(Long idWorldInstance);
}
