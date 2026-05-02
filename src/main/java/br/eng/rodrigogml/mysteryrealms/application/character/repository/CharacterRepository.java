package br.eng.rodrigogml.mysteryrealms.application.character.repository;

import br.eng.rodrigogml.mysteryrealms.application.character.entity.CharacterEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório Spring Data JPA para {@link CharacterEntity}.
 *
 * @author ?
 * @since 28-04-2026
 */
public interface CharacterRepository extends JpaRepository<CharacterEntity, Long> {

    /**
     * Lista todos os personagens de um usuário.
     *
     * @param idUser o ID do usuário
     * @return lista de personagens
     */
    List<CharacterEntity> findAllByIdUserOrderByLastAccessedAtDescCreatedAtDesc(Long idUser);

    /**
     * Lista todos os personagens de um usuário ordenados por último acesso e criação.
     *
     * @param idUser o ID do usuário
     * @return lista de personagens ordenada
     */
    List<CharacterEntity> findAllByIdUserOrderByLastAccessedAtDescCreatedAtDesc(Long idUser);

    /**
     * Conta os personagens de um usuário.
     *
     * @param idUser o ID do usuário
     * @return quantidade de personagens
     */
    long countByIdUser(Long idUser);

    /**
     * Verifica se um usuário já possui um personagem com determinado nome.
     *
     * @param idUser o ID do usuário
     * @param name   o nome do personagem
     * @return true se existir
     */
    boolean existsByIdUserAndName(Long idUser, String name);

    /**
     * Busca um personagem de um usuário pelo nome.
     *
     * @param idUser o ID do usuário
     * @param name   o nome do personagem
     * @return o personagem encontrado, ou vazio
     */
    Optional<CharacterEntity> findByIdUserAndName(Long idUser, String name);
}
