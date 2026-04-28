package br.eng.rodrigogml.mysteryrealms.application.world.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

/**
 * Entidade JPA que representa uma entrada no diário de um personagem em uma instância de mundo.
 *
 * @author ?
 * @since 28-04-2026
 */
@Entity
@Table(name = "diaryEntry")
public class DiaryEntryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long idWorldInstance;

    @Column(nullable = false, length = 100)
    private String entryId;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String summary;

    @Column(nullable = false, length = 50)
    private String gameDate;

    @Column(nullable = false, length = 100)
    private String dialogId;

    @Column(nullable = false, length = 100)
    private String optionId;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Long getIdWorldInstance() { return idWorldInstance; }

    public void setIdWorldInstance(Long idWorldInstance) { this.idWorldInstance = idWorldInstance; }

    public String getEntryId() { return entryId; }

    public void setEntryId(String entryId) { this.entryId = entryId; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getSummary() { return summary; }

    public void setSummary(String summary) { this.summary = summary; }

    public String getGameDate() { return gameDate; }

    public void setGameDate(String gameDate) { this.gameDate = gameDate; }

    public String getDialogId() { return dialogId; }

    public void setDialogId(String dialogId) { this.dialogId = dialogId; }

    public String getOptionId() { return optionId; }

    public void setOptionId(String optionId) { this.optionId = optionId; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
