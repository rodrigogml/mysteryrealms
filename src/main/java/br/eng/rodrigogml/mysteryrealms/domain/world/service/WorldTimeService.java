package br.eng.rodrigogml.mysteryrealms.domain.world.service;

import br.eng.rodrigogml.mysteryrealms.domain.world.model.DayPhase;
import br.eng.rodrigogml.mysteryrealms.domain.world.model.Season;
import br.eng.rodrigogml.mysteryrealms.domain.world.model.WorldConfig;

/**
 * Serviço de tempo do mundo — RF-MN-12 e RF-MN-13.
 *
 * A unidade interna de tempo é sempre o minuto inteiro ({@code tempo_total_min}).
 * Conversões para formatos legíveis são derivadas dos parâmetros do mundo.
 */
public final class WorldTimeService {

    private WorldTimeService() {}

    // ── RF-MN-13: Avanço de tempo ────────────────────────────────────────────

    /**
     * Avança o tempo global pelo número de minutos indicado — RF-MN-13.
     *
     * @param tempoAtualMin   tempo atual em minutos
     * @param minutosAvancar  minutos a avançar (>= 0)
     * @return novo tempo total em minutos
     */
    public static long advanceTime(long tempoAtualMin, long minutosAvancar) {
        if (minutosAvancar < 0) throw new IllegalArgumentException("minutosAvancar deve ser >= 0");
        return tempoAtualMin + minutosAvancar;
    }

    // ── RF-MN-12: Conversões de tempo ────────────────────────────────────────

    /**
     * Retorna o minuto do dia atual (0-based, dentro de {@code minutosPorDia}).
     */
    public static int minuteOfDay(long tempoTotalMin, WorldConfig config) {
        return (int) (tempoTotalMin % config.minutosPorDia());
    }

    /**
     * Retorna o número do dia atual (1-based).
     */
    public static long dayOfYear(long tempoTotalMin, WorldConfig config) {
        long totalDias = tempoTotalMin / config.minutosPorDia();
        return (totalDias % config.diasPorAno()) + 1;
    }

    /**
     * Retorna o ano atual (1-based).
     */
    public static long year(long tempoTotalMin, WorldConfig config) {
        long totalDias = tempoTotalMin / config.minutosPorDia();
        return (totalDias / config.diasPorAno()) + 1;
    }

    /**
     * Retorna a hora do dia (0-based).
     */
    public static int hourOfDay(long tempoTotalMin, WorldConfig config) {
        return minuteOfDay(tempoTotalMin, config) / config.minutosPorHora();
    }

    /**
     * Retorna o minuto dentro da hora atual (0-based).
     */
    public static int minuteOfHour(long tempoTotalMin, WorldConfig config) {
        return minuteOfDay(tempoTotalMin, config) % config.minutosPorHora();
    }

    /**
     * Formata o tempo em formato legível: {@code AnoX DiaY HH:MM}.
     */
    public static String formatTime(long tempoTotalMin, WorldConfig config) {
        long ano = year(tempoTotalMin, config);
        long dia = dayOfYear(tempoTotalMin, config);
        int hora = hourOfDay(tempoTotalMin, config);
        int min = minuteOfHour(tempoTotalMin, config);
        return String.format("Ano%d D%d-%02d:%02d", ano, dia, hora, min);
    }

    /**
     * Retorna a fase do dia correspondente ao tempo atual, ou {@code null} se não houver fase configurada.
     */
    public static DayPhase currentDayPhase(long tempoTotalMin, WorldConfig config) {
        int minDia = minuteOfDay(tempoTotalMin, config);
        for (DayPhase fase : config.fasesDia()) {
            if (minDia >= fase.inicioMinDia() && minDia <= fase.fimMinDia()) {
                return fase;
            }
        }
        return null;
    }

    /**
     * Retorna a estação correspondente ao dia do ano atual, ou {@code null} se não houver.
     */
    public static Season currentSeason(long tempoTotalMin, WorldConfig config) {
        long dia = dayOfYear(tempoTotalMin, config);
        for (Season estacao : config.estacoes()) {
            if (dia >= estacao.diaInicio() && dia <= estacao.diaFim()) {
                return estacao;
            }
        }
        return null;
    }
}
