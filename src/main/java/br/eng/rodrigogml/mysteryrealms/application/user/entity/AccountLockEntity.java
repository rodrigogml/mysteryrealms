package br.eng.rodrigogml.mysteryrealms.application.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

/**
 * Entidade JPA que representa um bloqueio de conta de usuário.
 *
 * @author ?
 * @since 28-04-2026
 */
@Entity
@Table(name = "accountLock")
public class AccountLockEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long idUser;

    @Column(nullable = false)
    private LocalDateTime lockedAt;

    @Column(nullable = false)
    private LocalDateTime unlockAt;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Long getIdUser() { return idUser; }

    public void setIdUser(Long idUser) { this.idUser = idUser; }

    public LocalDateTime getLockedAt() { return lockedAt; }

    public void setLockedAt(LocalDateTime lockedAt) { this.lockedAt = lockedAt; }

    public LocalDateTime getUnlockAt() { return unlockAt; }

    public void setUnlockAt(LocalDateTime unlockAt) { this.unlockAt = unlockAt; }
}
