package br.eng.rodrigogml.mysteryrealms.domain.world.model;

import java.util.List;
import java.util.regex.Pattern;

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

    private static final Pattern CANONICAL_WORLD_ID_PATTERN = Pattern.compile("^mundo_[a-z0-9_]+$");

    public WorldConfig {
        if (worldId == null || worldId.isBlank())
            throw new IllegalArgumentException("idMundo não pode ser vazio");
        if (!CANONICAL_WORLD_ID_PATTERN.matcher(worldId).matches())
            throw new IllegalArgumentException("idMundo deve começar com 'mundo_' e usar apenas letras minúsculas, números e '_': " + worldId);
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
        validateDayPhases(dayPhases, minutesPerHour * hoursPerDay);
        validateSeasons(seasons, daysPerYear);
    }

    /** Total de minutes por dia. */
    public int minutesPerDay() {
        return minutesPerHour * hoursPerDay;
    }

    /** Total de minutes por ano. */
    public long minutesPerYear() {
        return (long) minutesPerDay() * daysPerYear;
    }

    private static void validateDayPhases(List<DayPhase> dayPhases, int minutesPerDay) {
        int expectedStart = 0;
        for (DayPhase dayPhase : dayPhases) {
            if (dayPhase.startMinOfDay() != expectedStart) {
                throw new IllegalArgumentException("fasesDia devem cobrir o dia sem lacunas e em ordem");
            }
            if (dayPhase.endMinOfDay() >= minutesPerDay) {
                throw new IllegalArgumentException("fimMinDia deve ser menor que o total de minutos do dia");
            }
            expectedStart = dayPhase.endMinOfDay() + 1;
        }
        if (expectedStart != minutesPerDay) {
            throw new IllegalArgumentException("fasesDia devem cobrir o dia completo");
        }
    }

    private static void validateSeasons(List<Season> seasons, int daysPerYear) {
        int expectedStart = 1;
        for (Season season : seasons) {
            if (season.startDay() != expectedStart) {
                throw new IllegalArgumentException("estacoes devem cobrir o ano sem lacunas e em ordem");
            }
            if (season.endDay() > daysPerYear) {
                throw new IllegalArgumentException("diaFim da estação deve estar dentro do ano configurado");
            }
            expectedStart = season.endDay() + 1;
        }
        if (expectedStart != daysPerYear + 1) {
            throw new IllegalArgumentException("estacoes devem cobrir o ano completo");
        }
    }
}
