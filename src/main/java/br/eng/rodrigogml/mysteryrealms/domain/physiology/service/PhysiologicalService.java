package br.eng.rodrigogml.mysteryrealms.domain.physiology.service;

import br.eng.rodrigogml.mysteryrealms.domain.physiology.enums.CombinedPhysiologicalEffect;
import br.eng.rodrigogml.mysteryrealms.domain.physiology.enums.ConsciousnessTestResult;
import br.eng.rodrigogml.mysteryrealms.domain.physiology.model.PhysiologicalState;

/**
 * Serviço de estado fisiológico do personagem — RF-EF-01 a RF-EF-16.
 *
 * Todos os métodos são estáticos e stateless para facilitar testes e reutilização.
 */
public final class PhysiologicalService {

    // ── Constantes fisiológicas ───────────────────────────────────────────────

    /** Minutos por dia completo de vigília até atingir fadiga_max (RF-EF-01). */
    private static final double MINUTOS_DIA = 1440.0;

    /** Minutos até sede_pct chegar a 100 (48 horas) — RF-EF-01. */
    private static final double MINUTOS_SEDE_TOTAL = 48.0 * 60.0;

    /** Minutos até fome_pct chegar a 100 (7 dias) — RF-EF-01. */
    private static final double MINUTOS_FOME_TOTAL = 7.0 * 24.0 * 60.0;

    /** Limiar de colapso de fadiga: 120% de fadiga_max — RF-EF-02. */
    public static final double LIMIAR_COLAPSO_FADIGA = 1.2;

    /** Recuperação de fadiga por minuto em descanso: 0,10% de fadiga_max — RF-EF-07. */
    private static final double RECUPERACAO_DESCANSO_PCT = 0.001;

    /** Recuperação de fadiga por minuto em sono: 0,21% de fadiga_max — RF-EF-08. */
    private static final double RECUPERACAO_SONO_PCT = 0.0021;

    private PhysiologicalService() {}

    // ── RF-EF-01: tick de minuto ──────────────────────────────────────────────

    /**
     * Aplica os efeitos de um minuto de jogo enquanto o personagem está acordado.
     * Muta o estado diretamente.
     */
    public static void applyMinuteTick(PhysiologicalState state) {
        double newFadigaMin = state.getFadigaMin() + state.getFadigaMax() / MINUTOS_DIA;
        double newFadigaAtual = Math.max(state.getFadigaAtual(), newFadigaMin);
        double newSede = Math.min(100.0, state.getSedePct() + 100.0 / MINUTOS_SEDE_TOTAL);
        double newFome = Math.min(100.0, state.getFomePct() + 100.0 / MINUTOS_FOME_TOTAL);

        state.setFadigaMin(newFadigaMin);
        state.setFadigaAtual(newFadigaAtual);
        state.setSedePct(newSede);
        state.setFomePct(newFome);
    }

    // ── RF-EF-02: faixas de fadiga ────────────────────────────────────────────

    public enum FatigueStateId {
        NORMAL,
        EXAUSTAO,
        COLAPSO_FADIGA
    }

    /** Retorna o estado de fadiga atual do personagem — RF-EF-02. */
    public static FatigueStateId fatigueState(PhysiologicalState s) {
        double ratio = s.getFadigaAtual() / s.getFadigaMax();
        if (ratio >= LIMIAR_COLAPSO_FADIGA) return FatigueStateId.COLAPSO_FADIGA;
        if (ratio >= 1.0) return FatigueStateId.EXAUSTAO;
        return FatigueStateId.NORMAL;
    }

    // ── RF-EF-03: faixas de sede ──────────────────────────────────────────────

    public enum ThirstStateId {
        NORMAL,
        SEDE,
        SEDE_AGRAVADA,
        COLAPSO_SEDE
    }

    /** Retorna o estado de sede atual do personagem — RF-EF-03. */
    public static ThirstStateId thirstState(PhysiologicalState s) {
        double pct = s.getSedePct();
        if (pct >= 100.0) return ThirstStateId.COLAPSO_SEDE;
        if (pct >= 65.0)  return ThirstStateId.SEDE_AGRAVADA;
        if (pct >= 25.0)  return ThirstStateId.SEDE;
        return ThirstStateId.NORMAL;
    }

    // ── RF-EF-04: faixas de fome ──────────────────────────────────────────────

    public enum HungerStateId {
        NORMAL,
        FOME,
        FOME_AGRAVADA,
        COLAPSO_FOME
    }

    /** Retorna o estado de fome atual do personagem — RF-EF-04. */
    public static HungerStateId hungerState(PhysiologicalState s) {
        double pct = s.getFomePct();
        if (pct >= 100.0) return HungerStateId.COLAPSO_FOME;
        if (pct >= 85.0)  return HungerStateId.FOME_AGRAVADA;
        if (pct >= 43.0)  return HungerStateId.FOME;
        return HungerStateId.NORMAL;
    }

    // ── RF-EF-12: faixas de moral ─────────────────────────────────────────────

    public enum MoralStateId {
        COLAPSO_EMOCIONAL,
        MORAL_BAIXA,
        MORAL_ESTAVEL,
        MORAL_ALTA
    }

    /** Retorna o estado de moral atual do personagem — RF-EF-12. */
    public static MoralStateId moralState(PhysiologicalState s) {
        int m = s.getMoral();
        if (m <= 20) return MoralStateId.COLAPSO_EMOCIONAL;
        if (m <= 50) return MoralStateId.MORAL_BAIXA;
        if (m <= 80) return MoralStateId.MORAL_ESTAVEL;
        return MoralStateId.MORAL_ALTA;
    }

    // ── RF-EF-07: recuperação por descanso ───────────────────────────────────

    /**
     * Aplica recuperação de fadiga por um minuto de descanso.
     * Não recupera fadiga_min nem pontos de vida.
     */
    public static void applyRestRecoveryTick(PhysiologicalState state, double fatorAtividade) {
        double recovery = RECUPERACAO_DESCANSO_PCT * state.getFadigaMax() * fatorAtividade;
        double newFadiga = Math.max(state.getFadigaMin(), state.getFadigaAtual() - recovery);
        state.setFadigaAtual(newFadiga);
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
    public static void applySleepRecoveryTick(PhysiologicalState state, double fatorQualidadeSono) {
        double recoveryFadiga = RECUPERACAO_SONO_PCT * state.getFadigaMax() * fatorQualidadeSono;
        double recoveryFadigaMin = RECUPERACAO_SONO_PCT * state.getFadigaMax() * fatorQualidadeSono;
        double recoveryPv = (state.getPontosVidaMax() > 0)
                ? (/* constituicao embutida na formula RF-EF-08 */ recoveryPvPerMinute(state, fatorQualidadeSono))
                : 0.0;

        state.setFadigaMin(Math.max(0.0, state.getFadigaMin() - recoveryFadigaMin));
        state.setFadigaAtual(Math.max(state.getFadigaMin(), state.getFadigaAtual() - recoveryFadiga));
        state.setPontosVida(Math.min(state.getPontosVidaMax(), state.getPontosVida() + recoveryPv));
    }

    /**
     * Recuperação de PV por minuto de sono: (constituicao / 120) × fator — RF-EF-08.
     * Como constituicao = pontosVidaMax / 10 (RF-FP-06.1), substituímos diretamente.
     */
    public static double recoveryPvPerMinute(PhysiologicalState state, double fatorQualidadeSono) {
        double constituicao = state.getPontosVidaMax() / 10.0;
        return (constituicao / 120.0) * fatorQualidadeSono;
    }

    // ── RF-EF-10: critérios de despertar ─────────────────────────────────────

    /**
     * Verifica se o personagem pode sair do estado de desmaio — RF-EF-10.
     */
    public static boolean canWakeFromFaint(PhysiologicalState state) {
        return state.getPontosVida() >= 0.9 * state.getPontosVidaMax()
                && state.getFadigaAtual() <= 0.1 * state.getFadigaMax()
                && state.getFadigaMin() <= 0.1 * state.getFadigaMax();
    }

    // ── RF-EF-13: deltas de moral ─────────────────────────────────────────────

    /** Aplica delta de moral por testemunhar queda de aliado (-8) — RF-EF-13. */
    public static void applyMoralDeltaAllyFallen(PhysiologicalState state) {
        state.setMoral(state.getMoral() - 8);
    }

    /** Aplica delta de moral por vitória em combate significativo (+6) — RF-EF-13. */
    public static void applyMoralDeltaCombatVictory(PhysiologicalState state) {
        state.setMoral(state.getMoral() + 6);
    }

    /** Aplica delta de moral por sono contínuo >= 4h com fator >= 0.75 (+10) — RF-EF-13/15. */
    public static void applyMoralDeltaGoodSleep(PhysiologicalState state) {
        state.setMoral(state.getMoral() + 10);
    }

    /** Aplica delta de moral por entrada em desmaio (-12) — RF-EF-13. */
    public static void applyMoralDeltaFainted(PhysiologicalState state) {
        state.setMoral(state.getMoral() - 12);
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
     * Avalia o efeito combinado dos estados fisiológicos graves — RF-EF-05.
     *
     * Quando {@code fome_agravada + sede_agravada} ocorrem simultaneamente,
     * aplica -15 de moral diretamente no state.
     * As outras combinações (que impactam atributos/precisão/bloqueio) retornam
     * o enum correspondente para serem processadas pela camada que gerencia atributos.
     *
     * @return o efeito combinado detectado
     */
    public static CombinedPhysiologicalEffect combinedEffect(PhysiologicalState state) {
        boolean isExaustao      = fatigueState(state) == FatigueStateId.EXAUSTAO;
        boolean isSedeAgravada  = thirstState(state)  == ThirstStateId.SEDE_AGRAVADA;
        boolean isFomeAgravada  = hungerState(state)  == HungerStateId.FOME_AGRAVADA;

        if (isFomeAgravada && isSedeAgravada) {
            applyMoralDeltaFomeSede(state);
            return CombinedPhysiologicalEffect.FOME_SEDE_AGRAVADA;
        }
        if (isSedeAgravada && isExaustao) {
            return CombinedPhysiologicalEffect.SEDE_EXAUSTAO;
        }
        if (isFomeAgravada && isExaustao) {
            return CombinedPhysiologicalEffect.FOME_EXAUSTAO;
        }
        return CombinedPhysiologicalEffect.NONE;
    }

    /** Aplica -15 de moral pela combinação fome_agravada + sede_agravada — RF-EF-05. */
    public static void applyMoralDeltaFomeSede(PhysiologicalState state) {
        state.setMoral(state.getMoral() - 15);
    }

    // ── RF-EF-09: recuperação por itens ──────────────────────────────────────

    /**
     * Aplica recuperação por uso de itens — RF-EF-09.
     *
     * <ul>
     *   <li>fadiga: reduz {@code fadigaAtual} mas nunca abaixo de {@code fadigaMin}; nunca reduz {@code fadigaMin}.</li>
     *   <li>pv: clamp [0, pontosVidaMax].</li>
     *   <li>fome/sede: clamp [0, 100].</li>
     *   <li>moral: clamp [0, 100].</li>
     * </ul>
     *
     * @param state       estado a modificar
     * @param pvDelta     variação de pontos de vida (positivo = recuperação)
     * @param fadigaDelta variação de fadiga_atual (negativo = recuperação)
     * @param fomeDelta   variação de fome_pct (negativo = recuperação)
     * @param sedeDelta   variação de sede_pct (negativo = recuperação)
     * @param moralDelta  variação de moral
     */
    public static void applyItemRecovery(
            PhysiologicalState state,
            double pvDelta,
            double fadigaDelta,
            double fomeDelta,
            double sedeDelta,
            int moralDelta) {

        // PV
        state.setPontosVida(Math.max(0.0, Math.min(state.getPontosVidaMax(), state.getPontosVida() + pvDelta)));

        // Fadiga: aplica delta mas não desce abaixo de fadigaMin; não reduz fadigaMin
        if (fadigaDelta != 0) {
            double novaFadiga = state.getFadigaAtual() + fadigaDelta;
            state.setFadigaAtual(Math.max(state.getFadigaMin(), novaFadiga));
        }

        // Fome e Sede (os setters do PhysiologicalState já fazem clamp [0, 100])
        state.setFomePct(state.getFomePct() + fomeDelta);
        state.setSedePct(state.getSedePct() + sedeDelta);

        // Moral
        state.setMoral(state.getMoral() + moralDelta);
    }

    // ── RF-EF-11: estado crítico por PV ──────────────────────────────────────

    /**
     * Verifica se o personagem está em estado crítico por PV (pontos_vida <= 0) — RF-EF-11.
     */
    public static boolean isPvCritical(PhysiologicalState state) {
        return state.getPontosVida() <= 0;
    }

    /**
     * Executa o Teste de Conscientização quando PV chega a zero — RF-EF-11.
     *
     * <ul>
     *   <li>Sucesso: PV seta para 1, {@code fadigaAtual} seta para 90% de {@code fadigaMax}.</li>
     *   <li>Falha: state não é modificado — o caller deve disparar o desmaio.</li>
     * </ul>
     *
     * @param state      estado do personagem
     * @param testPassed resultado externo do teste (dado já rolado pelo caller)
     * @return {@link ConsciousnessTestResult#SUCCESS} ou {@link ConsciousnessTestResult#FAINTED}
     */
    public static ConsciousnessTestResult consciousnessTest(PhysiologicalState state, boolean testPassed) {
        if (testPassed) {
            state.setPontosVida(1.0);
            state.setFadigaAtual(0.90 * state.getFadigaMax());
            return ConsciousnessTestResult.SUCCESS;
        }
        return ConsciousnessTestResult.FAINTED;
    }

    // ── RF-EF-14: moral interage com custo de fadiga ──────────────────────────

    /**
     * Retorna o multiplicador de custo de fadiga baseado no estado de moral — RF-EF-14.
     *
     * <ul>
     *   <li>moral_colapso (moral <= 20): 1.10 (+10%)</li>
     *   <li>moral_alta (moral > 80): 0.90 (-10%)</li>
     *   <li>outros: 1.0</li>
     * </ul>
     */
    public static double moralFatigueCostMultiplier(PhysiologicalState state) {
        int m = state.getMoral();
        if (m <= 20) return 1.10;
        if (m > 80)  return 0.90;
        return 1.0;
    }

    // ── RF-EF-15: moral por repouso ───────────────────────────────────────────

    /**
     * Aplica +5 de moral por descanso seguro de 2h sem interrupção — RF-EF-15.
     */
    public static void applyMoralDeltaQuietRest(PhysiologicalState state) {
        state.setMoral(state.getMoral() + 5);
    }

    /**
     * Aplica -8 de moral por interrupção hostil do sono — RF-EF-15.
     */
    public static void applyMoralDeltaInterruptedSleep(PhysiologicalState state) {
        state.setMoral(state.getMoral() - 8);
    }
}
