package br.eng.rodrigogml.mysteryrealms.domain.world.service;

import br.eng.rodrigogml.mysteryrealms.domain.world.model.Connection;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Serviço de navegação no mundo — RF-MN-07 a RF-MN-11.
 *
 * Todos os métodos são estáticos e stateless.
 */
public final class NavigationService {

    private NavigationService() {}

    // ── RF-MN-07: Sistema de coordenadas ────────────────────────────────────

    /**
     * Distância base em km entre dois points cartesianos — RF-MN-07.
     * {@code distancia_base_km = sqrt((x2-x1)^2 + (y2-y1)^2) × 10}
     */
    public static double baseDistanceKm(double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        return Math.sqrt(dx * dx + dy * dy) * 10.0;
    }

    /**
     * Distância ajustada por rota em km — RF-MN-07.
     * {@code distancia_ajustada_km = distancia_base_km × (1 + routePenaltyPct / 100)}
     *
     * @param distBaseKm       distância base em km
     * @param routePenaltyPct penalidade da rota em % (intervalo [0, 15])
     */
    public static double adjustedDistanceKm(double distBaseKm, double routePenaltyPct) {
        return distBaseKm * (1.0 + routePenaltyPct / 100.0);
    }

    // ── RF-MN-08: Resolução de destino ──────────────────────────────────────

    /**
     * Resolve o primeiro destino acessível da conexão — RF-MN-08.
     *
     * Itera {@code destinos_priorizados} em ordem; retorna o primeiro que estiver
     * acessível (mapa {@code accessibleNodes}).
     * Se nenhum estiver acessível, retorna {@link Optional#empty()}.
     *
     * @param connection    conexão com lista priorizada de destinos
     * @param accessibleNodes mapa de id → acessibilidade dos nós conhecidos
     */
    public static Optional<String> resolveDestination(
            Connection connection, Map<String, Boolean> accessibleNodes) {
        for (String destId : connection.prioritizedDestinations()) {
            Boolean accessible = accessibleNodes.get(destId);
            if (Boolean.TRUE.equals(accessible)) {
                return Optional.of(destId);
            }
        }
        return Optional.empty();
    }

    // ── RF-MN-09: Interrupções durante deslocamento ─────────────────────────

    /**
     * Calcula a chance de interrupção para um segmento de deslocamento — RF-MN-09.
     *
     * Para segmentos menores que 1 km:
     * {@code chance_ajustada_pct = chance_interrupcao_km_pct × distancia_restante_km}
     *
     * Para segmentos >= 1 km, usa a chance por km diretamente.
     *
     * @param chanceKmPct       chance de interrupção por km (%)
     * @param distanciaKm       tamanho do segmento em km
     * @return chance de interrupção para o segmento (%)
     */
    public static double interruptionChancePerSegment(double chanceKmPct, double distanciaKm) {
        if (distanciaKm < 1.0) {
            return chanceKmPct * distanciaKm;
        }
        return chanceKmPct;
    }

    // ── RF-MN-10: Consumo de tempo por deslocamento ─────────────────────────

    /**
     * Calcula o tempo de deslocamento em minutes — RF-MN-10.
     * {@code minutes = ceil((distancia_ajustada_km / velocidade_kmh) × minutos_por_hora)}
     *
     * @param distAjustadaKm    distância ajustada em km
     * @param velocidadeKmh     velocidade efetiva em km/h
     * @param minutesPerHour    minutes por hora do mundo
     * @return tempo de deslocamento em minutes (>= 1)
     */
    public static long travelTimeMinutes(
            double distAjustadaKm, double velocidadeKmh, int minutesPerHour) {
        if (velocidadeKmh <= 0) throw new IllegalArgumentException("velocidadeKmh deve ser > 0");
        double horas = distAjustadaKm / velocidadeKmh;
        return Math.max(1L, (long) Math.ceil(horas * minutesPerHour));
    }

    // ── RF-MN-11: Atualização fisiológica por trecho ────────────────────────

    /**
     * Calcula o custo de fadiga para um trecho de deslocamento.
     * O custo base é proporcional ao tempo do trecho, escalado pelo fator de ação.
     *
     * @param custoPorMinuto custo de fadiga por minuto de ação
     * @param minutosTrecho  duração do trecho em minutes
     * @return custo total de fadiga para o trecho
     */
    public static double fatigueCostPerSegment(double custoPorMinuto, long minutosTrecho) {
        return custoPorMinuto * minutosTrecho;
    }
}
