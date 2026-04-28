package br.eng.rodrigogml.mysteryrealms.application.user.repository;

import br.eng.rodrigogml.mysteryrealms.application.user.entity.AccountLockEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Repositório Spring Data JPA para {@link AccountLockEntity}.
 *
 * @author ?
 * @since 28-04-2026
 */
public interface AccountLockRepository extends JpaRepository<AccountLockEntity, Long> {

    /**
     * Busca o bloqueio mais recente de um usuário.
     *
     * @param idUser o ID do usuário
     * @return o bloqueio mais recente, ou vazio
     */
    Optional<AccountLockEntity> findTopByIdUserOrderByLockedAtDesc(Long idUser);

    /**
     * Remove todos os bloqueios do usuário informado.
     *
     * @param idUser o ID do usuário
     */
    @Transactional
    void deleteAllByIdUser(Long idUser);
}
