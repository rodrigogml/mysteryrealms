package br.eng.rodrigogml.mysteryrealms.domain.modifier.enums;

/**
 * Origens de modificadores, em ordem canônica de priority (1 = maior), conforme RF-MAR-05.
 */
public enum ModifierOrigin {

    CRITICAL_COMBAT_STATE(1),
    ACTIVE_SKILL(2),
    EQUIPMENT(3),
    CHARACTER_CLASS(4),
    RACE(5),
    GENERIC_TEMPORARY_EFFECT(6);

    private final int priority;

    ModifierOrigin(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }
}
