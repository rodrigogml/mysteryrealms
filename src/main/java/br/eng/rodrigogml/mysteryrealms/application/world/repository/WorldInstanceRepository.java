package br.eng.rodrigogml.mysteryrealms.application.world.repository;

import br.eng.rodrigogml.mysteryrealms.application.world.entity.WorldInstanceEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Repositório Spring Data JPA para {@link WorldInstanceEntity}.
 *
 * @author ?
 * @since 28-04-2026
 */
public interface WorldInstanceRepository extends JpaRepository<WorldInstanceEntity, Long> {

    /**
     * Busca a instância de mundo associada a um personagem.
     *
     * @param idCharacter o ID do personagem
     * @return a instância encontrada, ou vazio
     */
    Optional<WorldInstanceEntity> findByIdCharacter(Long idCharacter);

    /**
     * Remove a instância de mundo associada a um personagem.
     *
     * @param idCharacter o ID do personagem
     */
    @Transactional
    void deleteByIdCharacter(Long idCharacter);
}
