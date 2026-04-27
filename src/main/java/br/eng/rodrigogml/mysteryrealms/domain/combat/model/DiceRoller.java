package br.eng.rodrigogml.mysteryrealms.domain.combat.model;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Interface funcional para rolagem de dados.
 * Permite substituição por implementação determinística nos testes.
 */
@FunctionalInterface
public interface DiceRoller {

    /**
     * Rola um dado de {@code faces} faces.
     *
     * @param faces número de faces do dado (>= 1)
     * @return value entre 1 e {@code faces} (inclusivo)
     */
    int roll(int faces);

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
        return faces -> ThreadLocalRandom.current().nextInt(1, faces + 1);
    }

    /**
     * Rolagem determinística: retorna sempre {@code value} (clamped a faces).
     * Útil para testes.
     */
    static DiceRoller fixed(int value) {
        return faces -> Math.min(value, faces);
    }
}
