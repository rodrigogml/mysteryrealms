package br.eng.rodrigogml.mysteryrealms.application.character.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidade JPA que representa um item equipado em mãos por um personagem.
 *
 * @author ?
 * @since 28-04-2026
 */
@Entity
@Table(name = "characterEquippedItem")
public class CharacterEquippedItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long idCharacter;

    @Column(nullable = false)
    private Long idItem;

    @Column(nullable = false)
    private int slotOrder;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Long getIdCharacter() { return idCharacter; }

    public void setIdCharacter(Long idCharacter) { this.idCharacter = idCharacter; }

    public Long getIdItem() { return idItem; }

    public void setIdItem(Long idItem) { this.idItem = idItem; }

    public int getSlotOrder() { return slotOrder; }

    public void setSlotOrder(int slotOrder) { this.slotOrder = slotOrder; }
}
