package br.eng.rodrigogml.mysteryrealms.application.user.repository;

import br.eng.rodrigogml.mysteryrealms.application.user.entity.EmailConfirmationEntity;
import br.eng.rodrigogml.mysteryrealms.application.user.entity.EmailConfirmationType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repositório Spring Data JPA para {@link EmailConfirmationEntity}.
 *
 * @author ?
 * @since 28-04-2026
 */
public interface EmailConfirmationRepository extends JpaRepository<EmailConfirmationEntity, Long> {

    /**
     * Busca uma confirmação de e-mail pelo token.
     *
     * @param token o token de confirmação
     * @return a confirmação encontrada, ou vazio
     */
    Optional<EmailConfirmationEntity> findByToken(String token);

    /**
     * Busca confirmações de e-mail expiradas por tipo.
     *
     * @param type o tipo da confirmação
     * @param expiresAt a data/hora máxima de expiração
     * @return confirmações expiradas encontradas
     */
    List<EmailConfirmationEntity> findAllByTypeAndExpiresAtBefore(EmailConfirmationType type, LocalDateTime expiresAt);

    /**
     * Remove todas as confirmações de e-mail do usuário informado.
     *
     * @param idUser o ID do usuário
     */
    @Transactional
    void deleteAllByIdUser(Long idUser);
}
