package br.eng.rodrigogml.mysteryrealms.application.world.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidade JPA que representa o impacto de uma entrada de diário em um marcador de mundo.
 *
 * @author ?
 * @since 28-04-2026
 */
@Entity
@Table(name = "diaryImpactMarker")
public class DiaryImpactMarkerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long idDiaryEntry;

    @Column(nullable = false, length = 100)
    private String markerId;

    @Column(nullable = false, length = 50)
    private String operation;

    @Column(nullable = true)
    private Integer value;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Long getIdDiaryEntry() { return idDiaryEntry; }

    public void setIdDiaryEntry(Long idDiaryEntry) { this.idDiaryEntry = idDiaryEntry; }

    public String getMarkerId() { return markerId; }

    public void setMarkerId(String markerId) { this.markerId = markerId; }

    public String getOperation() { return operation; }

    public void setOperation(String operation) { this.operation = operation; }

    public Integer getValue() { return value; }

    public void setValue(Integer value) { this.value = value; }
}
