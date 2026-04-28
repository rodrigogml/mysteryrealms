package br.eng.rodrigogml.mysteryrealms.application.character.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidade JPA que representa a reputação de um personagem em uma facção.
 *
 * @author ?
 * @since 28-04-2026
 */
@Entity
@Table(name = "characterFactionReputation")
public class CharacterFactionReputationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long idCharacter;

    @Column(nullable = false, length = 100)
    private String factionId;

    @Column(nullable = false)
    private int value;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Long getIdCharacter() { return idCharacter; }

    public void setIdCharacter(Long idCharacter) { this.idCharacter = idCharacter; }

    public String getFactionId() { return factionId; }

    public void setFactionId(String factionId) { this.factionId = factionId; }

    public int getValue() { return value; }

    public void setValue(int value) { this.value = value; }
}
