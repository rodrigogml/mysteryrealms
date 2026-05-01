package br.eng.rodrigogml.mysteryrealms.domain.combat.service;

import br.eng.rodrigogml.mysteryrealms.domain.combat.enums.AfflictionType;
import br.eng.rodrigogml.mysteryrealms.domain.combat.enums.DamageType;
import br.eng.rodrigogml.mysteryrealms.domain.combat.enums.ResistanceType;

import java.util.EnumMap;
import java.util.Map;

/**
 * Fixture reutilizável para cenários de combate em testes.
 */
final class CombatTestFixture {

    private final Map<DamageType, Integer> resistanceByDamageType;
    private final Map<AfflictionType, Double> afflictionResistanceByType;

    private CombatTestFixture(
            Map<DamageType, Integer> resistanceByDamageType,
            Map<AfflictionType, Double> afflictionResistanceByType) {
        this.resistanceByDamageType = resistanceByDamageType;
        this.afflictionResistanceByType = afflictionResistanceByType;
    }

    static CombatTestFixture defaultFixture() {
        Map<DamageType, Integer> resistanceByDamageType = new EnumMap<>(DamageType.class);
        for (DamageType type : DamageType.values()) {
            resistanceByDamageType.put(type, 0);
        }

        Map<AfflictionType, Double> afflictionResistanceByType = new EnumMap<>(AfflictionType.class);
        for (AfflictionType type : AfflictionType.values()) {
            afflictionResistanceByType.put(type, 0.0);
        }

        return new CombatTestFixture(resistanceByDamageType, afflictionResistanceByType);
    }

    CombatTestFixture withResistance(DamageType damageType, double resistanceFraction) {
        resistanceByDamageType.put(damageType, (int) Math.round(resistanceFraction * 100));
        return this;
    }

    CombatTestFixture withAfflictionResistance(AfflictionType afflictionType, double resistanceFraction) {
        afflictionResistanceByType.put(afflictionType, resistanceFraction);
        return this;
    }

    int resistanceFor(DamageType damageType) {
        return resistanceByDamageType.getOrDefault(damageType, 0);
    }

    double afflictionResistanceFor(AfflictionType afflictionType) {
        return afflictionResistanceByType.getOrDefault(afflictionType, 0.0);
    }

    ResistanceType resistanceTypeFor(DamageType damageType) {
        return ResistanceType.valueOf(damageType.name());
    }
}
