package br.eng.rodrigogml.mysteryrealms.domain.character.service;

/**
 * Serviço de progressão do personagem: fórmulas de XP, nível, PEA e PP.
 *
 * Versão de balanceamento ativa: BAL-1.0.0 (RF-PP-08).
 *
 * Fórmulas conforme RF-PP-02 e RF-PP-03:
 * xp_nivel(n) = ceil(A × pow(ln(n + B), C))
 * xp_total(n) = soma de xp_nivel(i), para i de 1 até n
 *
 * Parâmetros BAL-1.0.0: A=50, B=10, C=10.
 *
 * @author ?
 * @since 28-04-2026
 */
public final class ProgressionService {

    public static final String BALANCE_VERSION = "BAL-1.0.0";

    private static final double XP_A = 50.0;
    private static final double XP_B = 10.0;
    private static final double XP_C = 10.0;

    private ProgressionService() {}

    /**
     * Snapshot de progressão aplicado em um level-up específico.
     *
     * @param level nível atingido
     * @param attributePointGranted indica ganho de ponto de atributo
     * @param talentGranted indica ganho de talento
     * @param resourceGranted indica ganho de recurso
     */
    public record ProgressionMilestone(int level, boolean attributePointGranted, boolean talentGranted, boolean resourceGranted) {}

    /**
     * Resultado consolidado da aplicação de progressão com base no XP acumulado.
     *
     * @param targetLevel nível final atingido
     * @param levelsGained quantidade de níveis ganhos
     * @param attributePointsGranted total de pontos de atributo concedidos
     * @param talentsGranted total de talentos concedidos
     * @param resourcesGranted total de recursos concedidos
     */
    public record LevelUpResult(int targetLevel, int levelsGained, int attributePointsGranted, int talentsGranted, int resourcesGranted) {}

    /**
     * XP incremental necessário para avançar do nível {@code n} para o nível {@code n+1}.
     *
     * @param n nível atual
     * @return XP necessário para o próximo nível
     */
    public static long xpForLevel(int n) {
        if (n < 1) throw new IllegalArgumentException("Nível deve ser >= 1, recebido: " + n);
        return (long) Math.ceil(XP_A * Math.pow(Math.log(n + XP_B), XP_C));
    }

    /**
     * XP total acumulado necessário para atingir o estado de ter completado {@code n} níveis.
     *
     * @param n nível de referência
     * @return soma acumulada do XP por nível
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
     * Calcula o nível correspondente ao XP acumulado informado.
     * Permite múltiplas subidas em sequência conforme RF-PP-04.
     *
     * @param xpAccumulated XP acumulado do personagem
     * @return nível resultante para o XP informado
     */
    public static int levelFromAccumulatedXp(long xpAccumulated) {
        if (xpAccumulated < 0) throw new IllegalArgumentException("XP acumulado deve ser >= 0");

        int level = 1;
        long nextLevelThreshold = totalXpForLevel(2);
        while (xpAccumulated >= nextLevelThreshold) {
            level++;
            nextLevelThreshold += xpForLevel(level + 1);
        }
        return level;
    }

    /**
     * Calcula quantos níveis o personagem deve ganhar para o XP acumulado atual.
     *
     * @param xpAccumulated XP acumulado do personagem
     * @param currentLevel nível atual persistido
     * @return quantidade de níveis a avançar
     */
    public static int levelsGained(long xpAccumulated, int currentLevel) {
        if (currentLevel < 1) throw new IllegalArgumentException("Nível atual deve ser >= 1");
        return Math.max(0, levelFromAccumulatedXp(xpAccumulated) - currentLevel);
    }

    public static ProgressionMilestone milestoneForLevel(int level) {
        if (level < 2) {
            return new ProgressionMilestone(level, false, false, false);
        }
        return new ProgressionMilestone(level, peaOnLevelUp(level) > 0, talentOnLevelUp(level) > 0, resourceOnLevelUp(level) > 0);
    }

    public static int talentOnLevelUp(int newLevel) {
        if (newLevel < 1) return 0;
        return newLevel % 4 == 0 ? 1 : 0;
    }

    public static int resourceOnLevelUp(int newLevel) {
        if (newLevel < 1) return 0;
        return newLevel % 3 == 0 ? 1 : 0;
    }

    public static LevelUpResult evaluateLevelUp(long xpAccumulated, int currentLevel) {
        if (currentLevel < 1) throw new IllegalArgumentException("Nível atual deve ser >= 1");
        int targetLevel = levelFromAccumulatedXp(xpAccumulated);
        if (targetLevel <= currentLevel) {
            return new LevelUpResult(currentLevel, 0, 0, 0, 0);
        }

        int attributePoints = 0;
        int talents = 0;
        int resources = 0;
        for (int level = currentLevel + 1; level <= targetLevel; level++) {
            attributePoints += peaOnLevelUp(level);
            talents += talentOnLevelUp(level);
            resources += resourceOnLevelUp(level);
        }
        return new LevelUpResult(targetLevel, targetLevel - currentLevel, attributePoints, talents, resources);
    }

    /**
     * Verifica se o personagem deve subir de nível.
     *
     * @param xpAccumulated XP acumulado do personagem
     * @param currentLevel nível atual persistido
     * @return {@code true} quando há pelo menos uma subida pendente
     */
    public static boolean shouldLevelUp(long xpAccumulated, int currentLevel) {
        return levelsGained(xpAccumulated, currentLevel) > 0;
    }

    /**
     * Total de Pontos de Evolução de Atributo (PEA) acumulados ao atingir o nível {@code level}.
     *
     * @param level nível de referência
     * @return total acumulado de PEA
     */
    public static int totalPeaAtLevel(int level) {
        if (level < 2) return 0;
        if (level <= 10) return level - 1;
        if (level <= 30) return 9 + (level - 10) / 2;
        return 19 + (level - 30) / 3;
    }

    /**
     * PEA recebido ao subir para {@code newLevel}.
     *
     * @param newLevel novo nível atingido
     * @return PEA concedido nessa subida
     */
    public static int peaOnLevelUp(int newLevel) {
        if (newLevel < 2) return 0;
        return totalPeaAtLevel(newLevel) - totalPeaAtLevel(newLevel - 1);
    }

    /**
     * Total de Pontos de Proficiência (PP) acumulados ao atingir o nível {@code level}.
     *
     * @param level nível de referência
     * @return total acumulado de PP
     */
    public static int totalPpAtLevel(int level) {
        if (level < 1) throw new IllegalArgumentException("Nível deve ser >= 1");
        return (level + 1) / 2;
    }

    /**
     * PP recebido ao subir para {@code newLevel}.
     *
     * @param newLevel novo nível atingido
     * @return PP concedido nessa subida
     */
    public static int ppOnLevelUp(int newLevel) {
        if (newLevel < 1) return 0;
        return newLevel % 2 == 1 ? 1 : 0;
    }

    /**
     * Bônus de proficiência de faixa para o nível informado.
     *
     * @param level nível atual
     * @return bônus de proficiência da faixa
     */
    public static int proficiencyBonus(int level) {
        if (level >= 50) return 3;
        if (level >= 25) return 2;
        if (level >= 10) return 1;
        return 0;
    }

    /**
     * Valor final de habilidade.
     *
     * @param atributoBase atributo base da habilidade
     * @param ppDaHabilidade pontos de proficiência investidos
     * @param level nível atual do personagem
     * @param modificadores modificadores adicionais
     * @return valor final da habilidade
     */
    public static int finalSkillValue(int atributoBase, int ppDaHabilidade, int level, int modificadores) {
        return atributoBase + ppDaHabilidade + proficiencyBonus(level) + modificadores;
    }

    /**
     * Limite suave de atributo por nível.
     *
     * @param level nível atual
     * @return valor máximo suave do atributo
     */
    public static int softCapAttribute(int level) {
        return 10 + level / 5;
    }

    /**
     * Verifica se o traço utilitário de classe/racial avançado está desbloqueado.
     *
     * @param level nível atual do personagem
     * @return {@code true} se o traço estiver desbloqueado
     */
    public static boolean isUtilityTraitUnlocked(int level) {
        return level >= 5;
    }

    /**
     * Verifica se o slot de habilidade ativa está desbloqueado para o nível informado.
     *
     * @param level nível atual do personagem
     * @param slotIndex índice do slot com base 1
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
     * Verifica se a habilidade de assinatura está desbloqueada.
     *
     * @param level nível atual do personagem
     * @return {@code true} se a habilidade estiver desbloqueada
     */
    public static boolean isSignatureSkillUnlocked(int level) {
        return level >= 12;
    }

    /**
     * Verifica se a especialização avançada está desbloqueada.
     *
     * @param level nível atual do personagem
     * @return {@code true} se a especialização estiver desbloqueada
     */
    public static boolean isAdvancedSpecializationUnlocked(int level) {
        return level >= 20;
    }

    /**
     * Verifica se os ciclos de maestria repetíveis estão ativos.
     *
     * @param level nível atual do personagem
     * @return {@code true} se o personagem já entrou na faixa de maestria
     */
    public static boolean isMasteryCycleActive(int level) {
        return level >= 30;
    }
}
