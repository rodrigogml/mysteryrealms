package br.eng.rodrigogml.mysteryrealms.application.character.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Entidade JPA que representa os pontos investidos em uma habilidade de um personagem.
 *
 * @author ?
 * @since 28-04-2026
 */
@Entity
@Table(name = "characterSkillPoints")
public class CharacterSkillPointsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long idCharacter;

    @Column(nullable = false, length = 50)
    private String skillKey;

    @Column(nullable = false)
    private int pointsInvested;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public Long getIdCharacter() { return idCharacter; }

    public void setIdCharacter(Long idCharacter) { this.idCharacter = idCharacter; }

    public String getSkillKey() { return skillKey; }

    public void setSkillKey(String skillKey) { this.skillKey = skillKey; }

    public int getPointsInvested() { return pointsInvested; }

    public void setPointsInvested(int pointsInvested) { this.pointsInvested = pointsInvested; }
}
