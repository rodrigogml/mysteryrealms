package br.eng.rodrigogml.mysteryrealms.application.user.repository;

import br.eng.rodrigogml.mysteryrealms.application.user.entity.LoginAttemptEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositório Spring Data JPA para {@link LoginAttemptEntity}.
 *
 * @author ?
 * @since 28-04-2026
 */
public interface LoginAttemptRepository extends JpaRepository<LoginAttemptEntity, Long> {

    /**
     * Busca tentativas de login de um usuário após uma data/hora.
     *
     * @param idUser o ID do usuário
     * @param since  a data/hora mínima
     * @return lista de tentativas
     */
    List<LoginAttemptEntity> findByIdUserAndAttemptTimeAfter(Long idUser, LocalDateTime since);

    /**
     * Conta tentativas de login falhas de um usuário após uma data/hora.
     *
     * @param idUser  o ID do usuário
     * @param since   a data/hora mínima
     * @return quantidade de tentativas falhas
     */
    long countByIdUserAndSuccessFalseAndAttemptTimeAfter(Long idUser, LocalDateTime since);
}
