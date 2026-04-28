package br.eng.rodrigogml.mysteryrealms.application.character.repository;

import br.eng.rodrigogml.mysteryrealms.application.character.entity.CharacterEquippedItemEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Repositório Spring Data JPA para {@link CharacterEquippedItemEntity}.
 *
 * @author ?
 * @since 28-04-2026
 */
public interface CharacterEquippedItemRepository extends JpaRepository<CharacterEquippedItemEntity, Long> {

    /**
     * Remove todos os itens equipados do personagem informado.
     *
     * @param idCharacter o ID do personagem
     */
    @Transactional
    void deleteAllByIdCharacter(Long idCharacter);
}
