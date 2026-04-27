package br.eng.rodrigogml.mysteryrealms.domain.physiology.service;

import br.eng.rodrigogml.mysteryrealms.domain.physiology.enums.CombinedPhysiologyEffect;
import br.eng.rodrigogml.mysteryrealms.domain.physiology.enums.ConsciousnessTestResult;
import br.eng.rodrigogml.mysteryrealms.domain.physiology.model.PhysiologyState;

/**
 * Serviço de estado fisiológico do personagem — RF-EF-01 a RF-EF-16.
 *
 * Todos os métodos são estáticos e stateless para facilitar testes e reutilização.
 */
public final class PhysiologyService {

    // ── Constantes fisiológicas ───────────────────────────────────────────────

    /** Minutos por dia completo de vigília até atingir fadiga_max (RF-EF-01). */
    private static final double MINUTOS_DIA = 1440.0;

    /** Minutos até sede_pct chegar a 100 (48 horas) — RF-EF-01. */
    private static final double MINUTOS_SEDE_TOTAL = 48.0 * 60.0;

    /** Minutos até fome_pct chegar a 100 (7 dias) — RF-EF-01. */
    private static final double MINUTOS_FOME_TOTAL = 7.0 * 24.0 * 60.0;

    /** Limiar de colapso de fadiga: 120% de fadiga_max — RF-EF-02. */
    public static final double FATIGUE_COLLAPSE_THRESHOLD = 1.2;

    /** Recuperação de fadiga por minuto em descanso: 0,10% de fadiga_max — RF-EF-07. */
    private static final double RECUPERACAO_DESCANSO_PCT = 0.001;

    /** Recuperação de fadiga por minuto em sono: 0,21% de fadiga_max — RF-EF-08. */
    private static final double RECUPERACAO_SONO_PCT = 0.0021;

    private PhysiologyService() {}

    // ── RF-EF-01: tick de minuto ──────────────────────────────────────────────

    /**
     * Aplica os efeitos de um minuto de jogo enquanto o personagem está acordado.
     * Muta o estado diretamente.
     */
    public static void applyMinuteTick(PhysiologyState state) {
        double newFadigaMin = state.getMinFatigue() + state.getMaxFatigue() / MINUTOS_DIA;
        double newFadigaAtual = Math.max(state.getCurrentFatigue(), newFadigaMin);
        double newSede = Math.min(100.0, state.getThirstPct() + 100.0 / MINUTOS_SEDE_TOTAL);
        double newFome = Math.min(100.0, state.getHungerPct() + 100.0 / MINUTOS_FOME_TOTAL);

        state.setMinFatigue(newFadigaMin);
        state.setCurrentFatigue(newFadigaAtual);
        state.setThirstPct(newSede);
        state.setHungerPct(newFome);
    }

    // ── RF-EF-02: faixas de fadiga ────────────────────────────────────────────

    public enum EstadoFadiga {
        NORMAL,
        EXAUSTAO,
        COLAPSO_FADIGA
    }

    /** Retorna o estado de fadiga atual do personagem — RF-EF-02. */
    public static EstadoFadiga fatigueState(PhysiologyState s) {
        double ratio = s.getCurrentFatigue() / s.getMaxFatigue();
        if (ratio >= FATIGUE_COLLAPSE_THRESHOLD) return EstadoFadiga.COLAPSO_FADIGA;
        if (ratio >= 1.0) return EstadoFadiga.EXAUSTAO;
        return EstadoFadiga.NORMAL;
    }

    // ── RF-EF-03: faixas de sede ──────────────────────────────────────────────

    public enum EstadoSede {
        NORMAL,
        SEDE,
        SEDE_AGRAVADA,
        COLAPSO_SEDE
    }

    /** Retorna o estado de sede atual do personagem — RF-EF-03. */
    public static EstadoSede thirstState(PhysiologyState s) {
        double pct = s.getThirstPct();
        if (pct >= 100.0) return EstadoSede.COLAPSO_SEDE;
        if (pct >= 65.0)  return EstadoSede.SEDE_AGRAVADA;
        if (pct >= 25.0)  return EstadoSede.SEDE;
        return EstadoSede.NORMAL;
    }

    // ── RF-EF-04: faixas de fome ──────────────────────────────────────────────

    public enum EstadoFome {
        NORMAL,
        FOME,
        FOME_AGRAVADA,
        COLAPSO_FOME
    }

    /** Retorna o estado de fome atual do personagem — RF-EF-04. */
    public static EstadoFome hungerState(PhysiologyState s) {
        double pct = s.getHungerPct();
        if (pct >= 100.0) return EstadoFome.COLAPSO_FOME;
        if (pct >= 85.0)  return EstadoFome.FOME_AGRAVADA;
        if (pct >= 43.0)  return EstadoFome.FOME;
        return EstadoFome.NORMAL;
    }

    // ── RF-EF-12: faixas de morale ─────────────────────────────────────────────

    public enum EstadoMoral {
        COLAPSO_EMOCIONAL,
        MORAL_BAIXA,
        MORAL_ESTAVEL,
        MORAL_ALTA
    }

    /** Retorna o estado de morale atual do personagem — RF-EF-12. */
    public static EstadoMoral moraleState(PhysiologyState s) {
        int m = s.getMorale();
        if (m <= 20) return EstadoMoral.COLAPSO_EMOCIONAL;
        if (m <= 50) return EstadoMoral.MORAL_BAIXA;
        if (m <= 80) return EstadoMoral.MORAL_ESTAVEL;
        return EstadoMoral.MORAL_ALTA;
    }

    // ── RF-EF-07: recuperação por descanso ───────────────────────────────────

    /**
     * Aplica recuperação de fadiga por um minuto de descanso.
     * Não recupera fadiga_min nem points de vida.
     */
    public static void applyRestTick(PhysiologyState state, double fatorAtividade) {
        double recovery = RECUPERACAO_DESCANSO_PCT * state.getMaxFatigue() * fatorAtividade;
        double newFadiga = Math.max(state.getMinFatigue(), state.getCurrentFatigue() - recovery);
        state.setCurrentFatigue(newFadiga);
    }

    // ── RF-EF-08: recuperação por sono ───────────────────────────────────────

    /**
     * Calcula o fator de qualidade do sono — RF-EF-08.
     * fator = clamp(0,5 + (3 × C + (100 - max(0, R - S))) / 800, 0,5, 1,0)
     *
     * @param c conforto (0–100)
     * @param r ruído    (0–100)
     * @param s segurança (0–100)
     */
    public static double sleepQualityFactor(double c, double r, double s) {
        double raw = 0.5 + (3.0 * c + (100.0 - Math.max(0.0, r - s))) / 800.0;
        return Math.max(0.5, Math.min(1.0, raw));
    }

    /**
     * Aplica recuperação por um minuto de sono — RF-EF-08.
     */
    public static void applySleepTick(PhysiologyState state, double sleepQualityFactor) {
        double recoveryFadiga = RECUPERACAO_SONO_PCT * state.getMaxFatigue() * sleepQualityFactor;
        double recoveryFadigaMin = RECUPERACAO_SONO_PCT * state.getMaxFatigue() * sleepQualityFactor;
        double recoveryPv = (state.getMaxHealthPoints() > 0)
                ? (/* constitution embutida na formula RF-EF-08 */ hpRecoveryPerMinute(state, sleepQualityFactor))
                : 0.0;

        state.setMinFatigue(Math.max(0.0, state.getMinFatigue() - recoveryFadigaMin));
        state.setCurrentFatigue(Math.max(state.getMinFatigue(), state.getCurrentFatigue() - recoveryFadiga));
        state.setHealthPoints(Math.min(state.getMaxHealthPoints(), state.getHealthPoints() + recoveryPv));
    }

    /**
     * Recuperação de PV por minuto de sono: (constitution / 120) × fator — RF-EF-08.
     * Como constitution = maxHealthPoints / 10 (RF-FP-06.1), substituímos diretamente.
     */
    public static double hpRecoveryPerMinute(PhysiologyState state, double sleepQualityFactor) {
        double constitution = state.getMaxHealthPoints() / 10.0;
        return (constitution / 120.0) * sleepQualityFactor;
    }

    // ── RF-EF-10: critérios de despertar ─────────────────────────────────────

    /**
     * Verifica se o personagem pode sair do estado de desmaio — RF-EF-10.
     */
    public static boolean canWakeFromFaint(PhysiologyState state) {
        return state.getHealthPoints() >= 0.9 * state.getMaxHealthPoints()
                && state.getCurrentFatigue() <= 0.1 * state.getMaxFatigue()
                && state.getMinFatigue() <= 0.1 * state.getMaxFatigue();
    }

    // ── RF-EF-13: deltas de morale ─────────────────────────────────────────────

    /** Aplica delta de morale por testemunhar queda de aliado (-8) — RF-EF-13. */
    public static void applyMoraleDeltaAllyDown(PhysiologyState state) {
        state.setMorale(state.getMorale() - 8);
    }

    /** Aplica delta de morale por vitória em combate significativo (+6) — RF-EF-13. */
    public static void applyMoraleDeltaCombatVictory(PhysiologyState state) {
        state.setMorale(state.getMorale() + 6);
    }

    /** Aplica delta de morale por sono contínuo >= 4h com fator >= 0.75 (+10) — RF-EF-13/15. */
    public static void applyMoraleDeltaGoodSleep(PhysiologyState state) {
        state.setMorale(state.getMorale() + 10);
    }

    /** Aplica delta de morale por entrada em desmaio (-12) — RF-EF-13. */
    public static void applyMoraleDeltaFaint(PhysiologyState state) {
        state.setMorale(state.getMorale() - 12);
    }

    // ── RF-EF-05: interações entre estados fisiológicos ───────────────────────

    /**
     * Penalidade máxima de velocidade acumulada — RF-EF-05.
     */
    public static final double MAX_SPEED_PENALTY_PCT = 0.60;

    /**
     * Bônus máximo acumulado de custo de fadiga por ação — RF-EF-05.
     */
    public static final double MAX_FATIGUE_COST_BONUS_PCT = 1.00;

    /**
     * Penalidade máxima de recuperação de fadiga — RF-EF-05.
     */
    public static final double MAX_FATIGUE_RECOVERY_PENALTY_PCT = 0.80;

    /**
     * Avalia o effect combinado dos estados fisiológicos graves — RF-EF-05.
     *
     * Quando {@code fome_agravada + sede_agravada} ocorrem simultaneamente,
     * aplica -15 de morale diretamente no state.
     * As outras combinações (que impactam attributes/precisão/bloqueio) retornam
     * o enum correspondente para serem processadas pela camada que gerencia attributes.
     *
     * @return o effect combinado detectado
     */
    public static CombinedPhysiologyEffect combinedEffect(PhysiologyState state) {
        boolean isExaustao      = fatigueState(state) == EstadoFadiga.EXAUSTAO;
        boolean isSedeAgravada  = thirstState(state)  == EstadoSede.SEDE_AGRAVADA;
        boolean isFomeAgravada  = hungerState(state)  == EstadoFome.FOME_AGRAVADA;

        if (isFomeAgravada && isSedeAgravada) {
            applyMoraleDeltaHungerThirst(state);
            return CombinedPhysiologyEffect.SEVERE_HUNGER_THIRST;
        }
        if (isSedeAgravada && isExaustao) {
            return CombinedPhysiologyEffect.THIRST_EXHAUSTION;
        }
        if (isFomeAgravada && isExaustao) {
            return CombinedPhysiologyEffect.HUNGER_EXHAUSTION;
        }
        return CombinedPhysiologyEffect.NONE;
    }

    /** Aplica -15 de morale pela combinação fome_agravada + sede_agravada — RF-EF-05. */
    public static void applyMoraleDeltaHungerThirst(PhysiologyState state) {
        state.setMorale(state.getMorale() - 15);
    }

    // ── RF-EF-09: recuperação por itens ──────────────────────────────────────

    /**
     * Aplica recuperação por uso de itens — RF-EF-09.
     *
     * <ul>
     *   <li>fadiga: reduz {@code currentFatigue} mas nunca abaixo de {@code minFatigue}; nunca reduz {@code minFatigue}.</li>
     *   <li>pv: clamp [0, maxHealthPoints].</li>
     *   <li>fome/sede: clamp [0, 100].</li>
     *   <li>morale: clamp [0, 100].</li>
     * </ul>
     *
     * @param state       estado a modificar
     * @param pvDelta     variação de points de vida (positivo = recuperação)
     * @param fadigaDelta variação de fadiga_atual (negativo = recuperação)
     * @param fomeDelta   variação de fome_pct (negativo = recuperação)
     * @param sedeDelta   variação de sede_pct (negativo = recuperação)
     * @param moralDelta  variação de morale
     */
    public static void applyItemRecovery(
            PhysiologyState state,
            double pvDelta,
            double fadigaDelta,
            double fomeDelta,
            double sedeDelta,
            int moralDelta) {

        // PV
        state.setHealthPoints(Math.max(0.0, Math.min(state.getMaxHealthPoints(), state.getHealthPoints() + pvDelta)));

        // Fadiga: aplica delta mas não desce abaixo de minFatigue; não reduz minFatigue
        if (fadigaDelta != 0) {
            double novaFadiga = state.getCurrentFatigue() + fadigaDelta;
            state.setCurrentFatigue(Math.max(state.getMinFatigue(), novaFadiga));
        }

        // Fome e Sede (os setters do PhysiologyState já fazem clamp [0, 100])
        state.setHungerPct(state.getHungerPct() + fomeDelta);
        state.setThirstPct(state.getThirstPct() + sedeDelta);

        // Moral
        state.setMorale(state.getMorale() + moralDelta);
    }

    // ── RF-EF-11: estado crítico por PV ──────────────────────────────────────

    /**
     * Verifica se o personagem está em estado crítico por PV (pontos_vida <= 0) — RF-EF-11.
     */
    public static boolean isHealthCritical(PhysiologyState state) {
        return state.getHealthPoints() <= 0;
    }

    /**
     * Executa o Teste de Conscientização quando PV chega a zero — RF-EF-11.
     *
     * <ul>
     *   <li>Sucesso: PV seta para 1, {@code currentFatigue} seta para 90% de {@code maxFatigue}.</li>
     *   <li>Falha: state não é modificado — o caller deve disparar o desmaio.</li>
     * </ul>
     *
     * @param state      estado do personagem
     * @param testPassed resultado externo do teste (dado já rolado pelo caller)
     * @return {@link ConsciousnessTestResult#SUCCESS} ou {@link ConsciousnessTestResult#FAINTED}
     */
    public static ConsciousnessTestResult consciousnessTest(PhysiologyState state, boolean testPassed) {
        if (testPassed) {
            state.setHealthPoints(1.0);
            state.setCurrentFatigue(0.90 * state.getMaxFatigue());
            return ConsciousnessTestResult.SUCCESS;
        }
        return ConsciousnessTestResult.FAINTED;
    }

    // ── RF-EF-14: morale interage com custo de fadiga ──────────────────────────

    /**
     * Retorna o multiplicador de custo de fadiga baseado no estado de morale — RF-EF-14.
     *
     * <ul>
     *   <li>moral_colapso (morale <= 20): 1.10 (+10%)</li>
     *   <li>moral_alta (morale > 80): 0.90 (-10%)</li>
     *   <li>outros: 1.0</li>
     * </ul>
     */
    public static double fatigueCostMultiplierByMorale(PhysiologyState state) {
        int m = state.getMorale();
        if (m <= 20) return 1.10;
        if (m > 80)  return 0.90;
        return 1.0;
    }

    // ── RF-EF-15: morale por repouso ───────────────────────────────────────────

    /**
     * Aplica +5 de morale por descanso seguro de 2h sem interrupção — RF-EF-15.
     */
    public static void applyMoraleDeltaQuietRest(PhysiologyState state) {
        state.setMorale(state.getMorale() + 5);
    }

    /**
     * Aplica -8 de morale por interrupção hostil do sono — RF-EF-15.
     */
    public static void applyMoraleDeltaInterruptedSleep(PhysiologyState state) {
        state.setMorale(state.getMorale() - 8);
    }
}
