package br.eng.rodrigogml.mysteryrealms.application.character.repository;

import br.eng.rodrigogml.mysteryrealms.application.character.entity.CharacterBackpackItemEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Repositório Spring Data JPA para {@link CharacterBackpackItemEntity}.
 *
 * @author ?
 * @since 28-04-2026
 */
public interface CharacterBackpackItemRepository extends JpaRepository<CharacterBackpackItemEntity, Long> {

    /**
     * Remove todos os itens da mochila do personagem informado.
     *
     * @param idCharacter o ID do personagem
     */
    @Transactional
    void deleteAllByIdCharacter(Long idCharacter);
}
