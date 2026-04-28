package br.eng.rodrigogml.mysteryrealms.application.character.repository;

import br.eng.rodrigogml.mysteryrealms.application.character.entity.CharacterLocalityReputationEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Repositório Spring Data JPA para {@link CharacterLocalityReputationEntity}.
 *
 * @author ?
 * @since 28-04-2026
 */
public interface CharacterLocalityReputationRepository extends JpaRepository<CharacterLocalityReputationEntity, Long> {

    /**
     * Remove todas as reputações em localidades do personagem informado.
     *
     * @param idCharacter o ID do personagem
     */
    @Transactional
    void deleteAllByIdCharacter(Long idCharacter);
}
