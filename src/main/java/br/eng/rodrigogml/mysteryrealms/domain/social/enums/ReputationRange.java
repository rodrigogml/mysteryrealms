package br.eng.rodrigogml.mysteryrealms.domain.social.enums;

public enum ReputationRange {
    INFAMOUS(Integer.MIN_VALUE, -60, 2),
    DISLIKED(-59, -20, 1),
    NEUTRAL(-19, 19, 0),
    RESPECTED(20, 59, 1),
    REVERED(60, Integer.MAX_VALUE, 2);

    private final int min;
    private final int max;
    private final int impactBonus;

    ReputationRange(int min, int max, int impactBonus) {
        this.min = min;
        this.max = max;
        this.impactBonus = impactBonus;
    }

    public int impactBonus() {
        return impactBonus;
    }

    public static ReputationRange of(int value) {
        for (ReputationRange range : values()) {
            if (value >= range.min && value <= range.max) {
                return range;
            }
        }
        return NEUTRAL;
    }
}
