package br.eng.rodrigogml.mysteryrealms.domain.character.model;

import br.eng.rodrigogml.mysteryrealms.domain.character.enums.Habilidade;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/**
 * Bônus de habilidades de raça ou classe.
 */
public record BonusHabilidades(Map<Habilidade, Integer> bonuses) {

    public BonusHabilidades(Map<Habilidade, Integer> bonuses) {
        this.bonuses = Collections.unmodifiableMap(new EnumMap<>(bonuses));
    }

    public static BonusHabilidades vazio() {
        return new BonusHabilidades(new EnumMap<>(Habilidade.class));
    }

    public static BonusHabilidades de(Habilidade s1, int b1) {
        EnumMap<Habilidade, Integer> map = new EnumMap<>(Habilidade.class);
        map.put(s1, b1);
        return new BonusHabilidades(map);
    }

    public static BonusHabilidades de(Habilidade s1, int b1, Habilidade s2, int b2) {
        EnumMap<Habilidade, Integer> map = new EnumMap<>(Habilidade.class);
        map.put(s1, b1);
        map.put(s2, b2);
        return new BonusHabilidades(map);
    }

    public static BonusHabilidades de(Habilidade s1, int b1, Habilidade s2, int b2, Habilidade s3, int b3) {
        EnumMap<Habilidade, Integer> map = new EnumMap<>(Habilidade.class);
        map.put(s1, b1);
        map.put(s2, b2);
        map.put(s3, b3);
        return new BonusHabilidades(map);
    }

    public int getBonus(Habilidade skill) {
        return bonuses.getOrDefault(skill, 0);
    }

    /**
     * Combina dois BonusHabilidades somando os valores de chaves comuns.
     */
    public BonusHabilidades mesclar(BonusHabilidades outro) {
        EnumMap<Habilidade, Integer> merged = new EnumMap<>(Habilidade.class);
        merged.putAll(this.bonuses);
        outro.bonuses.forEach((k, v) -> merged.merge(k, v, Integer::sum));
        return new BonusHabilidades(merged);
    }
}
