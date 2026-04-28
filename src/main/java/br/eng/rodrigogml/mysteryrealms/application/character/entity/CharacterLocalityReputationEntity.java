package br.eng.rodrigogml.mysteryrealms.application.character.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidade JPA que representa a reputação de um personagem em uma localidade.
 *
 * @author ?
 * @since 28-04-2026
 */
@Entity
@Table(name = "characterLocalityReputation")
public class CharacterLocalityReputationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long idCharacter;

    @Column(nullable = false, length = 100)
    private String localityId;

    @Column(nullable = false)
    private int value;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Long getIdCharacter() { return idCharacter; }

    public void setIdCharacter(Long idCharacter) { this.idCharacter = idCharacter; }

    public String getLocalityId() { return localityId; }

    public void setLocalityId(String localityId) { this.localityId = localityId; }

    public int getValue() { return value; }

    public void setValue(int value) { this.value = value; }
}
