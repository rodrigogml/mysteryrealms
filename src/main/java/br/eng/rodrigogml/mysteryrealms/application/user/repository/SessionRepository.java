package br.eng.rodrigogml.mysteryrealms.application.user.repository;

import br.eng.rodrigogml.mysteryrealms.application.user.entity.SessionEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Repositório Spring Data JPA para {@link SessionEntity}.
 *
 * @author ?
 * @since 28-04-2026
 */
public interface SessionRepository extends JpaRepository<SessionEntity, Long> {

    /**
     * Busca uma sessão pelo token.
     *
     * @param token o token da sessão
     * @return a sessão encontrada, ou vazio
     */
    Optional<SessionEntity> findByToken(String token);

    /**
     * Remove todas as sessões do usuário informado.
     *
     * @param idUser o ID do usuário
     */
    @Transactional
    void deleteAllByIdUser(Long idUser);
}
