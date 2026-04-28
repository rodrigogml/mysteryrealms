package br.eng.rodrigogml.mysteryrealms.application.character.repository;

import br.eng.rodrigogml.mysteryrealms.application.character.entity.CharacterFactionReputationEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Repositório Spring Data JPA para {@link CharacterFactionReputationEntity}.
 *
 * @author ?
 * @since 28-04-2026
 */
public interface CharacterFactionReputationRepository extends JpaRepository<CharacterFactionReputationEntity, Long> {

    /**
     * Remove todas as reputações em facções do personagem informado.
     *
     * @param idCharacter o ID do personagem
     */
    @Transactional
    void deleteAllByIdCharacter(Long idCharacter);
}
