package br.eng.rodrigogml.mysteryrealms.application.world.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidade JPA que representa o estado de uma localidade em uma instância de mundo.
 *
 * @author ?
 * @since 28-04-2026
 */
@Entity
@Table(name = "worldLocationState")
public class WorldLocationStateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long idWorldInstance;

    @Column(nullable = false, length = 100)
    private String locationId;

    @Column(nullable = false)
    private boolean discovered;

    @Column(nullable = false)
    private boolean accessible;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Long getIdWorldInstance() { return idWorldInstance; }

    public void setIdWorldInstance(Long idWorldInstance) { this.idWorldInstance = idWorldInstance; }

    public String getLocationId() { return locationId; }

    public void setLocationId(String locationId) { this.locationId = locationId; }

    public boolean isDiscovered() { return discovered; }

    public void setDiscovered(boolean discovered) { this.discovered = discovered; }

    public boolean isAccessible() { return accessible; }

    public void setAccessible(boolean accessible) { this.accessible = accessible; }
}
