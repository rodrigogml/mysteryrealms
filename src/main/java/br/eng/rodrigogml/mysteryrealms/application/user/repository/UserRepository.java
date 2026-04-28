package br.eng.rodrigogml.mysteryrealms.application.user.repository;

import br.eng.rodrigogml.mysteryrealms.application.user.entity.UserEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositório Spring Data JPA para {@link UserEntity}.
 *
 * @author ?
 * @since 28-04-2026
 */
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    /**
     * Busca um usuário pelo e-mail.
     *
     * @param email o e-mail do usuário
     * @return o usuário encontrado, ou vazio
     */
    Optional<UserEntity> findByEmail(String email);

    /**
     * Busca um usuário pelo nome de usuário.
     *
     * @param username o nome de usuário
     * @return o usuário encontrado, ou vazio
     */
    Optional<UserEntity> findByUsername(String username);

    /**
     * Verifica se existe um usuário com o nome de usuário informado.
     *
     * @param username o nome de usuário
     * @return true se existir
     */
    boolean existsByUsername(String username);

    /**
     * Verifica se existe um usuário com o e-mail informado.
     *
     * @param email o e-mail
     * @return true se existir
     */
    boolean existsByEmail(String email);
}
