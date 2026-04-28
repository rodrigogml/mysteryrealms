package br.eng.rodrigogml.mysteryrealms.application.world.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidade JPA que representa um marcador de progresso em uma instância de mundo.
 *
 * @author ?
 * @since 28-04-2026
 */
@Entity
@Table(name = "worldMarker")
public class WorldMarkerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long idWorldInstance;

    @Column(nullable = false, length = 100)
    private String markerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private MarkerType markerType;

    @Column(nullable = true)
    private Boolean flagValue;

    @Column(nullable = true)
    private Integer intValue;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Long getIdWorldInstance() { return idWorldInstance; }

    public void setIdWorldInstance(Long idWorldInstance) { this.idWorldInstance = idWorldInstance; }

    public String getMarkerId() { return markerId; }

    public void setMarkerId(String markerId) { this.markerId = markerId; }

    public MarkerType getMarkerType() { return markerType; }

    public void setMarkerType(MarkerType markerType) { this.markerType = markerType; }

    public Boolean getFlagValue() { return flagValue; }

    public void setFlagValue(Boolean flagValue) { this.flagValue = flagValue; }

    public Integer getIntValue() { return intValue; }

    public void setIntValue(Integer intValue) { this.intValue = intValue; }
}
