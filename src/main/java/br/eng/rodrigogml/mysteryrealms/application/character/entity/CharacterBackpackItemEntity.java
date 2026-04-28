package br.eng.rodrigogml.mysteryrealms.application.character.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidade JPA que representa um item na mochila de um personagem.
 *
 * @author ?
 * @since 28-04-2026
 */
@Entity
@Table(name = "characterBackpackItem")
public class CharacterBackpackItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long idCharacter;

    @Column(nullable = false)
    private Long idItem;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Long getIdCharacter() { return idCharacter; }

    public void setIdCharacter(Long idCharacter) { this.idCharacter = idCharacter; }

    public Long getIdItem() { return idItem; }

    public void setIdItem(Long idItem) { this.idItem = idItem; }
}
