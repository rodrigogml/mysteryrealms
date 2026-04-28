package br.eng.rodrigogml.mysteryrealms.application.coop.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

/**
 * Entidade JPA que representa uma sessão co-op entre jogadores.
 *
 * @author ?
 * @since 28-04-2026
 */
@Entity
@Table(name = "coopSession")
public class CoopSessionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long idWorldInstance;

    @Column(nullable = false)
    private Long idHostCharacter;

    @Column(nullable = false)
    private int maxPlayers;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private CoopSessionStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Long getIdWorldInstance() { return idWorldInstance; }

    public void setIdWorldInstance(Long idWorldInstance) { this.idWorldInstance = idWorldInstance; }

    public Long getIdHostCharacter() { return idHostCharacter; }

    public void setIdHostCharacter(Long idHostCharacter) { this.idHostCharacter = idHostCharacter; }

    public int getMaxPlayers() { return maxPlayers; }

    public void setMaxPlayers(int maxPlayers) { this.maxPlayers = maxPlayers; }

    public CoopSessionStatus getStatus() { return status; }

    public void setStatus(CoopSessionStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
