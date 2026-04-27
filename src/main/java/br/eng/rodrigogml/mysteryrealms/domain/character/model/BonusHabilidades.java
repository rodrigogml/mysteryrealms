package br.eng.rodrigogml.mysteryrealms.domain.character.model;

import br.eng.rodrigogml.mysteryrealms.domain.character.enums.Skill;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/**
 * Bônus de habilidades de raça ou classe.
 */
public record BonusHabilidades(Map<Skill, Integer> bonuses) {

    public BonusHabilidades(Map<Skill, Integer> bonuses) {
        this.bonuses = Collections.unmodifiableMap(new EnumMap<>(bonuses));
    }

    public static BonusHabilidades vazio() {
        return new BonusHabilidades(new EnumMap<>(Skill.class));
    }

    public static BonusHabilidades de(Skill s1, int b1) {
        EnumMap<Skill, Integer> map = new EnumMap<>(Skill.class);
        map.put(s1, b1);
        return new BonusHabilidades(map);
    }

    public static BonusHabilidades de(Skill s1, int b1, Skill s2, int b2) {
        EnumMap<Skill, Integer> map = new EnumMap<>(Skill.class);
        map.put(s1, b1);
        map.put(s2, b2);
        return new BonusHabilidades(map);
    }

    public static BonusHabilidades de(Skill s1, int b1, Skill s2, int b2, Skill s3, int b3) {
        EnumMap<Skill, Integer> map = new EnumMap<>(Skill.class);
        map.put(s1, b1);
        map.put(s2, b2);
        map.put(s3, b3);
        return new BonusHabilidades(map);
    }

    public int getBonus(Skill skill) {
        return bonuses.getOrDefault(skill, 0);
    }

    /**
     * Combina dois BonusHabilidades somando os valores de chaves comuns.
     */
    public BonusHabilidades mesclar(BonusHabilidades outro) {
        EnumMap<Skill, Integer> merged = new EnumMap<>(Skill.class);
        merged.putAll(this.bonuses);
        outro.bonuses.forEach((k, v) -> merged.merge(k, v, Integer::sum));
        return new BonusHabilidades(merged);
    }
}
