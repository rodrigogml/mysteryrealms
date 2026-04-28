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
 * Entidade JPA que representa o estado de uma quest em uma instância de mundo.
 *
 * @author ?
 * @since 28-04-2026
 */
@Entity
@Table(name = "worldQuestState")
public class WorldQuestStateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long idWorldInstance;

    @Column(nullable = false, length = 100)
    private String questId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private QuestState state;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Long getIdWorldInstance() { return idWorldInstance; }

    public void setIdWorldInstance(Long idWorldInstance) { this.idWorldInstance = idWorldInstance; }

    public String getQuestId() { return questId; }

    public void setQuestId(String questId) { this.questId = questId; }

    public QuestState getState() { return state; }

    public void setState(QuestState state) { this.state = state; }
}
