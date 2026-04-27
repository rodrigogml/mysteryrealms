package br.eng.rodrigogml.mysteryrealms.domain.character.service;

/**
 * Serviço de progressão do personagem: fórmulas de XP, nível, PEA e PP.
 *
 * Versão de balanceamento ativa: BAL-1.0.0 (RF-PP-08).
 *
 * Fórmulas conforme RF-PP-02 e RF-PP-03:
 *   xp_nivel(n) = ceil(A × pow(ln(n + B), C))
 *   xp_total(n) = soma de xp_nivel(i), para i de 1 até n
 *
 * Parâmetros BAL-1.0.0: A=50, B=10, C=10.
 */
public final class ProgressionService {

    public static final String BALANCE_VERSION = "BAL-1.0.0";

    private static final double XP_A = 50.0;
    private static final double XP_B = 10.0;
    private static final double XP_C = 10.0;

    private ProgressionService() {}

    // ── XP ────────────────────────────────────────────────────────────────────

    /**
     * XP incremental necessário para avançar do nível {@code n} para o nível {@code n+1}.
     * Fórmula: ceil(A × pow(ln(n + B), C)) — RF-PP-02.
     */
    public static long xpForLevel(int n) {
        if (n < 1) throw new IllegalArgumentException("Nível deve ser >= 1, recebido: " + n);
        return (long) Math.ceil(XP_A * Math.pow(Math.log(n + XP_B), XP_C));
    }

    /**
     * XP total acumulado necessário para atingir o estado de ter completado {@code n} níveis.
     * xp_total(n) = soma de xp_nivel(i), para i de 1 até n — RF-PP-02.
     */
    public static long totalXpForLevel(int n) {
        if (n < 1) throw new IllegalArgumentException("Nível deve ser >= 1, recebido: " + n);
        long total = 0;
        for (int i = 1; i <= n; i++) {
            total += xpForLevel(i);
        }
        return total;
    }

    /**
     * Verifica se o personagem deve subir de nível.
     * Condição: xpAccumulated >= xp_total(currentLevel + 1) — RF-PP-04.
     */
    public static boolean shouldLevelUp(long xpAccumulated, int currentLevel) {
        if (currentLevel < 1) throw new IllegalArgumentException("Nível atual deve ser >= 1");
        return xpAccumulated >= totalXpForLevel(currentLevel + 1);
    }

    // ── PEA ───────────────────────────────────────────────────────────────────

    /**
     * Total de Pontos de Evolução de Atributo (PEA) acumulados ao atingir o nível {@code level}.
     * Faixas — RF-PP-05:
     *   2 a 10: +1 PEA por nível  →  9 PEA total no nível 10
     *   11 a 30: +1 PEA a cada 2 níveis
     *   31+: +1 PEA a cada 3 níveis
     */
    public static int totalPeaAtLevel(int level) {
        if (level < 2) return 0;
        if (level <= 10) return level - 1;
        if (level <= 30) return 9 + (level - 10) / 2;
        return 9 + 10 + (level - 30) / 3;
    }

    /**
     * PEA recebido ao subir para {@code newLevel} — RF-PP-05.
     */
    public static int peaOnLevelUp(int newLevel) {
        if (newLevel < 2) return 0;
        return totalPeaAtLevel(newLevel) - totalPeaAtLevel(newLevel - 1);
    }

    // ── PP ────────────────────────────────────────────────────────────────────

    /**
     * Total de Pontos de Proficiência (PP) acumulados ao atingir o nível {@code level}.
     * +1 PP a cada nível ímpar — RF-PP-06.
     */
    public static int totalPpAtLevel(int level) {
        if (level < 1) throw new IllegalArgumentException("Nível deve ser >= 1");
        return (level + 1) / 2;
    }

    /**
     * PP recebido ao subir para {@code newLevel} — RF-PP-06.
     */
    public static int ppOnLevelUp(int newLevel) {
        if (newLevel < 1) return 0;
        return newLevel % 2 == 1 ? 1 : 0;
    }

    /**
     * Bônus de proficiência de faixa para o nível informado — RF-PP-06.
     * Faixas: 1–9 → +0 | 10–24 → +1 | 25–49 → +2 | 50+ → +3.
     */
    public static int proficiencyBonus(int level) {
        if (level >= 50) return 3;
        if (level >= 25) return 2;
        if (level >= 10) return 1;
        return 0;
    }

    /**
     * Valor final de habilidade — RF-PP-06.
     * habilidade_final = atributo_base + pp_da_habilidade + bonus_proficiencia_faixa + modificadores
     */
    public static int finalSkillValue(int atributoBase, int ppDaHabilidade, int level, int modificadores) {
        return atributoBase + ppDaHabilidade + proficiencyBonus(level) + modificadores;
    }

    /**
     * Limite suave de atributo por nível: 10 + floor(level / 5) — RF-PP-05.
     */
    public static int softCapAttribute(int level) {
        return 10 + level / 5;
    }

    // ── RF-PP-07: marcos de nível ─────────────────────────────────────────────

    /**
     * Verifica se o slot de habilidade ativa está desbloqueado para o nível informado — RF-PP-07.
     * <ul>
     *   <li>Slot 1: nível >= 3</li>
     *   <li>Slot 2: nível >= 8</li>
     * </ul>
     *
     * @param level     nível atual do personagem
     * @param slotIndex índice do slot (1-based)
     * @return {@code true} se o slot estiver desbloqueado
     */
    public static boolean isSkillSlotUnlocked(int level, int slotIndex) {
        return switch (slotIndex) {
            case 1 -> level >= 3;
            case 2 -> level >= 8;
            default -> false;
        };
    }

    /**
     * Verifica se a habilidade de assinatura (tier intermediário) está desbloqueada — RF-PP-07.
     * Marco: nível 12.
     */
    public static boolean isSignatureSkillUnlocked(int level) {
        return level >= 12;
    }

    /**
     * Verifica se a especialização avançada (ramificação de build) está desbloqueada — RF-PP-07.
     * Marco: nível 20.
     */
    public static boolean isAdvancedSpecializationUnlocked(int level) {
        return level >= 20;
    }

    /**
     * Verifica se os ciclos de maestria repetíveis estão ativos — RF-PP-07.
     * Marco: nível 30+.
     */
    public static boolean isMasteryCycleActive(int level) {
        return level >= 30;
    }
}
