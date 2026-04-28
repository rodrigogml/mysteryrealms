package br.eng.rodrigogml.mysteryrealms.application.world.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidade JPA que representa o impacto de uma entrada de diário em um relacionamento com NPC.
 *
 * @author ?
 * @since 28-04-2026
 */
@Entity
@Table(name = "diaryImpactRelationship")
public class DiaryImpactRelationshipEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long idDiaryEntry;

    @Column(nullable = false, length = 100)
    private String targetId;

    @Column(nullable = false)
    private int delta;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Long getIdDiaryEntry() { return idDiaryEntry; }

    public void setIdDiaryEntry(Long idDiaryEntry) { this.idDiaryEntry = idDiaryEntry; }

    public String getTargetId() { return targetId; }

    public void setTargetId(String targetId) { this.targetId = targetId; }

    public int getDelta() { return delta; }

    public void setDelta(int delta) { this.delta = delta; }
}
