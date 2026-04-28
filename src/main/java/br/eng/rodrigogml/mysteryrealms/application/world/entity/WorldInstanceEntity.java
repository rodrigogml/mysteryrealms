package br.eng.rodrigogml.mysteryrealms.application.world.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidade JPA que representa a instância de mundo associada a um personagem.
 *
 * @author ?
 * @since 28-04-2026
 */
@Entity
@Table(name = "worldInstance")
public class WorldInstanceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long idCharacter;

    @Column(nullable = false)
    private long currentTimeMin;

    @Column(nullable = true, length = 100)
    private String currentLocationId;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Long getIdCharacter() { return idCharacter; }

    public void setIdCharacter(Long idCharacter) { this.idCharacter = idCharacter; }

    public long getCurrentTimeMin() { return currentTimeMin; }

    public void setCurrentTimeMin(long currentTimeMin) { this.currentTimeMin = currentTimeMin; }

    public String getCurrentLocationId() { return currentLocationId; }

    public void setCurrentLocationId(String currentLocationId) { this.currentLocationId = currentLocationId; }
}
