package br.eng.rodrigogml.mysteryrealms.application.user.repository;

import br.eng.rodrigogml.mysteryrealms.application.user.entity.TwoFactorAuthEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Repositório Spring Data JPA para {@link TwoFactorAuthEntity}.
 *
 * @author ?
 * @since 28-04-2026
 */
public interface TwoFactorAuthRepository extends JpaRepository<TwoFactorAuthEntity, Long> {

    /**
     * Busca a configuração de dois fatores de um usuário.
     *
     * @param idUser o ID do usuário
     * @return a configuração encontrada, ou vazio
     */
    Optional<TwoFactorAuthEntity> findByIdUser(Long idUser);

    /**
     * Remove a configuração de dois fatores do usuário informado.
     *
     * @param idUser o ID do usuário
     */
    @Transactional
    void deleteAllByIdUser(Long idUser);
}
