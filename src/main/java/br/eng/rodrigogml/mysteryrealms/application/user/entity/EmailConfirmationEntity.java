package br.eng.rodrigogml.mysteryrealms.application.user.entity;

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
 * Entidade JPA que representa um token de confirmação de e-mail.
 *
 * @author ?
 * @since 28-04-2026
 */
@Entity
@Table(name = "emailConfirmation")
public class EmailConfirmationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long idUser;

    @Column(nullable = false, length = 512)
    private String token;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private EmailConfirmationType type;

    @Column(nullable = true, length = 255)
    private String newEmail;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Long getIdUser() { return idUser; }

    public void setIdUser(Long idUser) { this.idUser = idUser; }

    public String getToken() { return token; }

    public void setToken(String token) { this.token = token; }

    public EmailConfirmationType getType() { return type; }

    public void setType(EmailConfirmationType type) { this.type = type; }

    public String getNewEmail() { return newEmail; }

    public void setNewEmail(String newEmail) { this.newEmail = newEmail; }

    public LocalDateTime getExpiresAt() { return expiresAt; }

    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
}
