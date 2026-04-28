package br.eng.rodrigogml.mysteryrealms.application.user.repository;

import br.eng.rodrigogml.mysteryrealms.application.user.entity.UnlockCodeEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Repositório Spring Data JPA para {@link UnlockCodeEntity}.
 *
 * @author ?
 * @since 28-04-2026
 */
public interface UnlockCodeRepository extends JpaRepository<UnlockCodeEntity, Long> {

    /**
     * Busca o código de desbloqueio mais recente não utilizado de um usuário.
     *
     * @param idUser o ID do usuário
     * @return o código mais recente não utilizado, ou vazio
     */
    Optional<UnlockCodeEntity> findTopByIdUserAndUsedFalseOrderByExpiresAtDesc(Long idUser);

    /**
     * Remove todos os códigos de desbloqueio do usuário informado.
     *
     * @param idUser o ID do usuário
     */
    @Transactional
    void deleteAllByIdUser(Long idUser);
}
