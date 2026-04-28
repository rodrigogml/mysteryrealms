package br.eng.rodrigogml.mysteryrealms.application.character.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidade JPA que representa o relacionamento de um personagem com um NPC.
 *
 * @author ?
 * @since 28-04-2026
 */
@Entity
@Table(name = "characterNpcRelationship")
public class CharacterNpcRelationshipEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long idCharacter;

    @Column(nullable = false, length = 100)
    private String npcId;

    @Column(nullable = false)
    private int value;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Long getIdCharacter() { return idCharacter; }

    public void setIdCharacter(Long idCharacter) { this.idCharacter = idCharacter; }

    public String getNpcId() { return npcId; }

    public void setNpcId(String npcId) { this.npcId = npcId; }

    public int getValue() { return value; }

    public void setValue(int value) { this.value = value; }
}
