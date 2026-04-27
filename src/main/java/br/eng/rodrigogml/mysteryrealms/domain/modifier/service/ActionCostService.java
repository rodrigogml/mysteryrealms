package br.eng.rodrigogml.mysteryrealms.domain.modifier.service;

import br.eng.rodrigogml.mysteryrealms.domain.modifier.enums.ActionClass;
import br.eng.rodrigogml.mysteryrealms.domain.modifier.model.ActionCost;

/**
 * Serviço de custo de ações do jogador — RF-MAR-07, RF-MAR-08, RF-MAR-09.
 *
 * Todos os métodos são estáticos e stateless.
 * Os modificadores são aplicados na ordem obrigatória: clima → carga → estado fisiológico.
 */
public final class ActionCostService {

    private ActionCostService() {}

    // ── RF-MAR-08: custos base por classe de ação ─────────────────────────────

    /**
     * Retorna o custo base de uma ação, conforme a tabela RF-MAR-08.
     * Retorna uma nova instância a cada chamada para garantir mutabilidade segura.
     */
    public static ActionCost baseCost(ActionClass actionClass) {
        return switch (actionClass) {
            case PASSIVA      -> new ActionCost(5,   2.0,  0.05,  0.15);
            case MODERADA     -> new ActionCost(10,  8.0,  0.20,  0.60);
            case EXIGENTE     -> new ActionCost(15, 20.0,  0.50,  1.20);
            case RECUPERATIVA -> new ActionCost(20, -12.0, -0.40, -0.80);
        };
    }

    // ── RF-MAR-09: modificadores de clima ────────────────────────────────────

    /**
     * Aplica modificadores de clima ao custo da ação — RF-MAR-09.
     *
     * <ul>
     *   <li>{@code "HEAT"} — calor forte/seco: +20% custo_fadiga, +35% delta_sede_pct</li>
     *   <li>{@code "COLD"} — frio intenso: +15% custo_fadiga</li>
     *   <li>{@code "RAIN"} — chuva/vento forte: +10% tempo_gasto_min (MODERADA/EXIGENTE)</li>
     * </ul>
     *
     * @param cost        custo a modificar (mutado diretamente)
     * @param actionClass classe da ação (necessária para regra de RAIN)
     * @param condition   condição climática: "HEAT", "COLD" ou "RAIN"
     */
    public static void applyWeatherModifier(ActionCost cost, ActionClass actionClass, String condition) {
        if (condition == null) return;
        switch (condition.toUpperCase()) {
            case "HEAT" -> {
                cost.setCustoFadiga(cost.getCustoFadiga() * 1.20);
                cost.setDeltaSedePct(cost.getDeltaSedePct() * 1.35);
            }
            case "COLD" -> {
                cost.setCustoFadiga(cost.getCustoFadiga() * 1.15);
            }
            case "RAIN" -> {
                if (actionClass == ActionClass.MODERADA || actionClass == ActionClass.EXIGENTE) {
                    cost.setTempoGastoMin((int) Math.ceil(cost.getTempoGastoMin() * 1.10));
                }
            }
            default -> { /* condição desconhecida: sem ajuste */ }
        }
    }

    // ── RF-MAR-09: modificadores de carga ────────────────────────────────────

    /**
     * Aplica modificadores de carga ao custo da ação — RF-MAR-09.
     *
     * @param cost        custo a modificar (mutado diretamente)
     * @param actionClass classe da ação (EXIGENTE bloqueada acima de 100% de carga)
     * @param cargaRatio  razão carga_atual / capacidade_carga (0.0 = vazio, 1.0 = cheio, >1.0 = sobrecarga)
     * @throws IllegalStateException se actionClass=EXIGENTE e cargaRatio>1.0 (deslocamento bloqueado)
     */
    public static void applyLoadModifier(ActionCost cost, ActionClass actionClass, double cargaRatio) {
        if (cargaRatio > 1.0) {
            if (actionClass == ActionClass.EXIGENTE) {
                throw new IllegalStateException(
                        "Ação EXIGENTE bloqueada: carga acima de 100% da capacidade (ratio=" + cargaRatio + ")");
            }
            cost.setCustoFadiga(cost.getCustoFadiga() * 1.50);
        } else if (cargaRatio > 0.80) {
            cost.setCustoFadiga(cost.getCustoFadiga() * 1.25);
            cost.setTempoGastoMin((int) Math.ceil(cost.getTempoGastoMin() * 1.10));
        } else if (cargaRatio > 0.50) {
            if (actionClass == ActionClass.MODERADA || actionClass == ActionClass.EXIGENTE) {
                cost.setCustoFadiga(cost.getCustoFadiga() * 1.10);
            }
        }
        // <= 0.50: sem ajuste
    }

    // ── RF-MAR-09: modificadores de estado fisiológico ───────────────────────

    /**
     * Aplica modificadores de estado fisiológico ao custo da ação — RF-MAR-09.
     *
     * @param cost             custo a modificar (mutado diretamente)
     * @param isExaustao       estado {@code exaustao} ativo
     * @param isSede           estado {@code sede} ativo
     * @param isSedeAgravada   estado {@code sede_agravada} ativo
     * @param isFome           estado {@code fome} ativo
     * @param isFomeAgravada   estado {@code fome_agravada} ativo
     * @param isDesmaio        personagem em desmaio
     * @param isEstadoCritico  personagem em estado_critico
     * @throws IllegalStateException se isDesmaio ou isEstadoCritico for verdadeiro
     */
    public static void applyPhysiologicalModifier(
            ActionCost cost,
            boolean isExaustao,
            boolean isSede,
            boolean isSedeAgravada,
            boolean isFome,
            boolean isFomeAgravada,
            boolean isDesmaio,
            boolean isEstadoCritico) {

        if (isDesmaio || isEstadoCritico) {
            throw new IllegalStateException(
                    "Ação cancelada: personagem em estado de desmaio ou estado crítico");
        }

        double multiplicadorFadiga = 1.0;
        if (isExaustao)       multiplicadorFadiga += 0.15;
        if (isSede)           multiplicadorFadiga += 0.10;
        if (isSedeAgravada)   multiplicadorFadiga += 0.25;
        if (isFome)           multiplicadorFadiga += 0.10;
        if (isFomeAgravada)   multiplicadorFadiga += 0.20;

        cost.setCustoFadiga(cost.getCustoFadiga() * multiplicadorFadiga);

        if (isSedeAgravada) {
            cost.setDeltaSedePct(cost.getDeltaSedePct() + cost.getDeltaSedePct() * 0.15);
        }
        if (isFomeAgravada) {
            cost.setDeltaFomePct(cost.getDeltaFomePct() + cost.getDeltaFomePct() * 0.10);
        }

        // Clamp final — RF-MAR-09
        cost.setDeltaFomePct(Math.max(-100.0, Math.min(100.0, cost.getDeltaFomePct())));
        cost.setDeltaSedePct(Math.max(-100.0, Math.min(100.0, cost.getDeltaSedePct())));
    }

    // ── Método consolidado ────────────────────────────────────────────────────

    /**
     * Calcula o custo completo de uma ação aplicando todos os modificadores na ordem
     * obrigatória: clima → carga → estado fisiológico — RF-MAR-09.
     *
     * @param actionClass      classe da ação
     * @param weatherCondition condição climática ("HEAT", "COLD", "RAIN" ou null)
     * @param cargaRatio       razão de carga [0.0, ∞)
     * @param isExaustao       estado exaustao ativo
     * @param isSede           estado sede ativo
     * @param isSedeAgravada   estado sede_agravada ativo
     * @param isFome           estado fome ativo
     * @param isFomeAgravada   estado fome_agravada ativo
     * @param isDesmaio        em desmaio
     * @param isEstadoCritico  estado crítico ativo
     * @return novo {@link ActionCost} com todos os modificadores aplicados
     */
    public static ActionCost calculateCost(
            ActionClass actionClass,
            String weatherCondition,
            double cargaRatio,
            boolean isExaustao,
            boolean isSede,
            boolean isSedeAgravada,
            boolean isFome,
            boolean isFomeAgravada,
            boolean isDesmaio,
            boolean isEstadoCritico) {

        ActionCost cost = baseCost(actionClass);
        applyWeatherModifier(cost, actionClass, weatherCondition);
        applyLoadModifier(cost, actionClass, cargaRatio);
        applyPhysiologicalModifier(cost, isExaustao, isSede, isSedeAgravada,
                isFome, isFomeAgravada, isDesmaio, isEstadoCritico);
        return cost;
    }
}
