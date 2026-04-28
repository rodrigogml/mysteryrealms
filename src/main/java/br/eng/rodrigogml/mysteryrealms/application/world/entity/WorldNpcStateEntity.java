package br.eng.rodrigogml.mysteryrealms.application.world.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidade JPA que representa o estado de um NPC em uma instância de mundo.
 *
 * @author ?
 * @since 28-04-2026
 */
@Entity
@Table(name = "worldNpcState")
public class WorldNpcStateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long idWorldInstance;

    @Column(nullable = false, length = 100)
    private String npcId;

    @Column(nullable = false)
    private boolean alive;

    @Column(nullable = true, length = 100)
    private String dialogState;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Long getIdWorldInstance() { return idWorldInstance; }

    public void setIdWorldInstance(Long idWorldInstance) { this.idWorldInstance = idWorldInstance; }

    public String getNpcId() { return npcId; }

    public void setNpcId(String npcId) { this.npcId = npcId; }

    public boolean isAlive() { return alive; }

    public void setAlive(boolean alive) { this.alive = alive; }

    public String getDialogState() { return dialogState; }

    public void setDialogState(String dialogState) { this.dialogState = dialogState; }
}
