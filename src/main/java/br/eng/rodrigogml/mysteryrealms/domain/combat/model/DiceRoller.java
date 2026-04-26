package br.eng.rodrigogml.mysteryrealms.domain.combat.model;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Interface funcional para rolagem de dados.
 * Permite substituição por implementação determinística nos testes.
 */
@FunctionalInterface
public interface DiceRoller {

    /**
     * Rola um dado de {@code sides} faces.
     *
     * @param sides número de faces do dado (>= 1)
     * @return valor entre 1 e {@code sides} (inclusivo)
     */
    int roll(int sides);

    default int d20() {
        return roll(20);
    }

    default int d6() {
        return roll(6);
    }

    default int d8() {
        return roll(8);
    }

    /**
     * Rolagem padrão com aleatoriedade real.
     */
    static DiceRoller standard() {
        return sides -> ThreadLocalRandom.current().nextInt(1, sides + 1);
    }

    /**
     * Rolagem determinística: retorna sempre {@code value} (clamped a sides).
     * Útil para testes.
     */
    static DiceRoller fixed(int value) {
        return sides -> Math.min(value, sides);
    }
}
