package br.eng.rodrigogml.mysteryrealms.domain.physiology.service;

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
}
