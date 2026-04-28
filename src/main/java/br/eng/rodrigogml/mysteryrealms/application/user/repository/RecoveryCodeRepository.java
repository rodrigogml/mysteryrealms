package br.eng.rodrigogml.mysteryrealms.application.user.repository;

import br.eng.rodrigogml.mysteryrealms.application.user.entity.RecoveryCodeEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Repositório Spring Data JPA para {@link RecoveryCodeEntity}.
 *
 * @author ?
 * @since 28-04-2026
 */
public interface RecoveryCodeRepository extends JpaRepository<RecoveryCodeEntity, Long> {

    /**
     * Busca todos os códigos de recuperação não utilizados de um usuário.
     *
     * @param idUser o ID do usuário
     * @return lista de códigos não utilizados
     */
    List<RecoveryCodeEntity> findAllByIdUserAndUsedFalse(Long idUser);

    /**
     * Remove todos os códigos de recuperação do usuário informado.
     *
     * @param idUser o ID do usuário
     */
    @Transactional
    void deleteAllByIdUser(Long idUser);
}
