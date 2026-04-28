package br.eng.rodrigogml.mysteryrealms.application.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

/**
 * Entidade JPA que representa uma tentativa de login de usuário.
 *
 * @author ?
 * @since 28-04-2026
 */
@Entity
@Table(name = "loginAttempt")
public class LoginAttemptEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long idUser;

    @Column(nullable = false, length = 45)
    private String ipAddress;

    @Column(nullable = false)
    private LocalDateTime attemptTime;

    @Column(nullable = false)
    private boolean success;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Long getIdUser() { return idUser; }

    public void setIdUser(Long idUser) { this.idUser = idUser; }

    public String getIpAddress() { return ipAddress; }

    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }

    public LocalDateTime getAttemptTime() { return attemptTime; }

    public void setAttemptTime(LocalDateTime attemptTime) { this.attemptTime = attemptTime; }

    public boolean isSuccess() { return success; }

    public void setSuccess(boolean success) { this.success = success; }
}
