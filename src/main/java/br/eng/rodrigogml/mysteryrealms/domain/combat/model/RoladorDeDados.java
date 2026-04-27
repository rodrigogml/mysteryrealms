package br.eng.rodrigogml.mysteryrealms.domain.combat.model;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Interface funcional para rolagem de dados.
 * Permite substituição por implementação determinística nos testes.
 */
@FunctionalInterface
public interface RoladorDados {

    /**
     * Rola um dado de {@code faces} faces.
     *
     * @param faces número de faces do dado (>= 1)
     * @return valor entre 1 e {@code faces} (inclusivo)
     */
    int rolar(int faces);

    default int d20() {
        return rolar(20);
    }

    default int d6() {
        return rolar(6);
    }

    default int d8() {
        return rolar(8);
    }

    /**
     * Rolagem padrão com aleatoriedade real.
     */
    static RoladorDados padrao() {
        return faces -> ThreadLocalRandom.current().nextInt(1, faces + 1);
    }

    /**
     * Rolagem determinística: retorna sempre {@code valor} (clamped a faces).
     * Útil para testes.
     */
    static RoladorDados fixo(int valor) {
        return faces -> Math.min(valor, faces);
    }
}
