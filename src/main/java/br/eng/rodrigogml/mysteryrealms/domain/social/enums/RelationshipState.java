package br.eng.rodrigogml.mysteryrealms.domain.social.enums;

public enum RelationshipState {
    HOSTILE(RelationshipRange.HOSTILE),
    NEUTRAL(RelationshipRange.NEUTRAL),
    FAVORABLE(RelationshipRange.FAVORABLE),
    ALLY(RelationshipRange.ALLY),
    MORTAL_ENEMY(RelationshipRange.MORTAL_ENEMY);

    private final RelationshipRange range;

    RelationshipState(RelationshipRange range) {
        this.range = range;
    }

    public RelationshipRange range() {
        return range;
    }

    public static RelationshipState fromScore(int value) {
        return switch (RelationshipRange.of(value)) {
            case MORTAL_ENEMY -> MORTAL_ENEMY;
            case HOSTILE -> HOSTILE;
            case NEUTRAL -> NEUTRAL;
            case FAVORABLE -> FAVORABLE;
            case ALLY -> ALLY;
        };
    }
}
