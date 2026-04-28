package br.eng.rodrigogml.mysteryrealms.application.user.repository;

import br.eng.rodrigogml.mysteryrealms.application.user.entity.PasswordResetEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Repositório Spring Data JPA para {@link PasswordResetEntity}.
 *
 * @author ?
 * @since 28-04-2026
 */
public interface PasswordResetRepository extends JpaRepository<PasswordResetEntity, Long> {

    /**
     * Busca um token de redefinição de senha não utilizado.
     *
     * @param token o token
     * @return o registro encontrado, ou vazio
     */
    Optional<PasswordResetEntity> findByTokenAndUsedFalse(String token);

    /**
     * Remove todos os tokens de redefinição do usuário informado.
     *
     * @param idUser o ID do usuário
     */
    @Transactional
    void deleteAllByIdUser(Long idUser);
}
