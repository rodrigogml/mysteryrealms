package br.eng.rodrigogml.mysteryrealms.application.coop.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

/**
 * Entidade JPA que representa um participante de uma sessão co-op.
 *
 * @author ?
 * @since 28-04-2026
 */
@Entity
@Table(name = "coopParticipant")
public class CoopParticipantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long idCoopSession;

    @Column(nullable = false)
    private Long idCharacter;

    @Column(nullable = false)
    private LocalDateTime joinedAt;

    @Column(nullable = true)
    private LocalDateTime leftAt;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Long getIdCoopSession() { return idCoopSession; }

    public void setIdCoopSession(Long idCoopSession) { this.idCoopSession = idCoopSession; }

    public Long getIdCharacter() { return idCharacter; }

    public void setIdCharacter(Long idCharacter) { this.idCharacter = idCharacter; }

    public LocalDateTime getJoinedAt() { return joinedAt; }

    public void setJoinedAt(LocalDateTime joinedAt) { this.joinedAt = joinedAt; }

    public LocalDateTime getLeftAt() { return leftAt; }

    public void setLeftAt(LocalDateTime leftAt) { this.leftAt = leftAt; }
}
