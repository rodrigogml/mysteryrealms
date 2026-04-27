package br.eng.rodrigogml.mysteryrealms.domain.world.model;

import java.util.List;

/**
 * Configuração temporal e estrutural do mundo — RF-MN-12.
 *
 * {@code fasesDia} deve cobrir o dia completo sem sobreposição.
 * {@code estacoes} deve cobrir o ano completo sem lacunas.
 */
public record ConfiguracaoMundo(
        String idMundo,
        int minutosPorHora,
        int horasPorDia,
        int diasPorAno,
        List<FaseDia> fasesDia,
        List<Estacao> estacoes,
        int tempoInicialMin) {

    public ConfiguracaoMundo {
        if (idMundo == null || idMundo.isBlank())
            throw new IllegalArgumentException("idMundo não pode ser vazio");
        if (minutosPorHora < 1)
            throw new IllegalArgumentException("minutosPorHora deve ser >= 1");
        if (horasPorDia < 1)
            throw new IllegalArgumentException("horasPorDia deve ser >= 1");
        if (diasPorAno < 1)
            throw new IllegalArgumentException("diasPorAno deve ser >= 1");
        if (fasesDia == null || fasesDia.isEmpty())
            throw new IllegalArgumentException("fasesDia deve ter pelo menos 1 fase");
        if (estacoes == null || estacoes.isEmpty())
            throw new IllegalArgumentException("estacoes deve ter pelo menos 1 estação");
        if (tempoInicialMin < 0)
            throw new IllegalArgumentException("tempoInicialMin deve ser >= 0");
        fasesDia = List.copyOf(fasesDia);
        estacoes = List.copyOf(estacoes);
    }

    /** Total de minutos por dia. */
    public int minutosPorDia() {
        return minutosPorHora * horasPorDia;
    }

    /** Total de minutos por ano. */
    public long minutosPorAno() {
        return (long) minutosPorDia() * diasPorAno;
    }
}
