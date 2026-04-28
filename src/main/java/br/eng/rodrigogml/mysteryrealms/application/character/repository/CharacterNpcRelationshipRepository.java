package br.eng.rodrigogml.mysteryrealms.application.character.repository;

import br.eng.rodrigogml.mysteryrealms.application.character.entity.CharacterNpcRelationshipEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Repositório Spring Data JPA para {@link CharacterNpcRelationshipEntity}.
 *
 * @author ?
 * @since 28-04-2026
 */
public interface CharacterNpcRelationshipRepository extends JpaRepository<CharacterNpcRelationshipEntity, Long> {

    /**
     * Remove todos os relacionamentos com NPCs do personagem informado.
     *
     * @param idCharacter o ID do personagem
     */
    @Transactional
    void deleteAllByIdCharacter(Long idCharacter);
}
