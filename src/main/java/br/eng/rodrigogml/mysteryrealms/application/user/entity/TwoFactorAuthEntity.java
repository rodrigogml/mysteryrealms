package br.eng.rodrigogml.mysteryrealms.application.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidade JPA que representa a configuração de autenticação de dois fatores de um usuário.
 *
 * @author ?
 * @since 28-04-2026
 */
@Entity
@Table(name = "twoFactorAuth")
public class TwoFactorAuthEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long idUser;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private TwoFactorMethod method;

    @Column(nullable = true, length = 255)
    private String secret;

    @Column(nullable = false)
    private boolean active;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Long getIdUser() { return idUser; }

    public void setIdUser(Long idUser) { this.idUser = idUser; }

    public TwoFactorMethod getMethod() { return method; }

    public void setMethod(TwoFactorMethod method) { this.method = method; }

    public String getSecret() { return secret; }

    public void setSecret(String secret) { this.secret = secret; }

    public boolean isActive() { return active; }

    public void setActive(boolean active) { this.active = active; }
}
