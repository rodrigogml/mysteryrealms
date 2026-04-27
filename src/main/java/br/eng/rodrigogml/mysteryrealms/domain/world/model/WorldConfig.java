package br.eng.rodrigogml.mysteryrealms.domain.world.model;

import java.util.List;

/**
 * Configuração temporal e estrutural do mundo — RF-MN-12.
 *
 * {@code dayPhases} deve cobrir o dia completo sem sobreposição.
 * {@code seasons} deve cobrir o ano completo sem lacunas.
 */
public record WorldConfig(
        String worldId,
        int minutesPerHour,
        int hoursPerDay,
        int daysPerYear,
        List<DayPhase> dayPhases,
        List<Season> seasons,
        int initialTimeMin) {

    public WorldConfig {
        if (worldId == null || worldId.isBlank())
            throw new IllegalArgumentException("idMundo não pode ser vazio");
        if (minutesPerHour < 1)
            throw new IllegalArgumentException("minutosPorHora deve ser >= 1");
        if (hoursPerDay < 1)
            throw new IllegalArgumentException("horasPorDia deve ser >= 1");
        if (daysPerYear < 1)
            throw new IllegalArgumentException("diasPorAno deve ser >= 1");
        if (dayPhases == null || dayPhases.isEmpty())
            throw new IllegalArgumentException("fasesDia deve ter pelo menos 1 fase");
        if (seasons == null || seasons.isEmpty())
            throw new IllegalArgumentException("estacoes deve ter pelo menos 1 estação");
        if (initialTimeMin < 0)
            throw new IllegalArgumentException("tempoInicialMin deve ser >= 0");
        dayPhases = List.copyOf(dayPhases);
        seasons = List.copyOf(seasons);
    }

    /** Total de minutes por dia. */
    public int minutesPerDay() {
        return minutesPerHour * hoursPerDay;
    }

    /** Total de minutes por ano. */
    public long minutesPerYear() {
        return (long) minutesPerDay() * daysPerYear;
    }
}
