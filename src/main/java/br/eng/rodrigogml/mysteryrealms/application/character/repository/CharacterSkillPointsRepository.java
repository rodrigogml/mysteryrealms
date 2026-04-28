package br.eng.rodrigogml.mysteryrealms.application.character.repository;

import br.eng.rodrigogml.mysteryrealms.application.character.entity.CharacterSkillPointsEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Repositório Spring Data JPA para {@link CharacterSkillPointsEntity}.
 *
 * @author ?
 * @since 28-04-2026
 */
public interface CharacterSkillPointsRepository extends JpaRepository<CharacterSkillPointsEntity, Long> {

    /**
     * Remove todos os pontos de habilidade do personagem informado.
     *
     * @param idCharacter o ID do personagem
     */
    @Transactional
    void deleteAllByIdCharacter(Long idCharacter);
}
