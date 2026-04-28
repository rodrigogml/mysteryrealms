package br.eng.rodrigogml.mysteryrealms.application.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidade JPA que representa um código de recuperação de autenticação de dois fatores.
 *
 * @author ?
 * @since 28-04-2026
 */
@Entity
@Table(name = "recoveryCode")
public class RecoveryCodeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long idUser;

    @Column(nullable = false, length = 255)
    private String codeHash;

    @Column(nullable = false)
    private boolean used;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Long getIdUser() { return idUser; }

    public void setIdUser(Long idUser) { this.idUser = idUser; }

    public String getCodeHash() { return codeHash; }

    public void setCodeHash(String codeHash) { this.codeHash = codeHash; }

    public boolean isUsed() { return used; }

    public void setUsed(boolean used) { this.used = used; }
}
