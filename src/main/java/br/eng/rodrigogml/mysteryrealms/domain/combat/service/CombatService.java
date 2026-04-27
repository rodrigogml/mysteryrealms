package br.eng.rodrigogml.mysteryrealms.domain.combat.service;

import br.eng.rodrigogml.mysteryrealms.domain.character.service.CharacterAttributeService;
import br.eng.rodrigogml.mysteryrealms.domain.combat.model.DiceRoller;

/**
 * Serviço de resolução de combate — RF-CT-01 a RF-CT-13.
 *
 * Implementa o pipeline de resolução (RF-CT-07) e as fórmulas de teste, acerto,
 * bloqueio, resistência e aflição.
 */
public final class CombatService {

    private CombatService() {}

    // ── RF-CT-01: fórmula de teste ────────────────────────────────────────────

    /**
     * Resultado do teste: valorBase + 1d20 + modificadores — RF-CT-01.
     */
    public static int testResult(int valorBase, int modificadores, DiceRoller dice) {
        return valorBase + dice.d20() + modificadores;
    }

    // ── RF-CT-02: critério de sucesso por CD ─────────────────────────────────

    /**
     * Verifica sucesso contra CD fixa — RF-CT-02.
     */
    public static boolean isSuccess(int resultado, int cd) {
        return resultado >= cd;
    }

    // ── RF-CT-03: vantagem e desvantagem ─────────────────────────────────────

    /**
     * Rolagem com vantagem: rola 2d20, mantém o maior — RF-CT-03.
     */
    public static int rollWithAdvantage(DiceRoller dice) {
        return Math.max(dice.d20(), dice.d20());
    }

    /**
     * Rolagem com desvantagem: rola 2d20, mantém o menor — RF-CT-03.
     */
    public static int rollWithDisadvantage(DiceRoller dice) {
        return Math.min(dice.d20(), dice.d20());
    }

    // ── RF-CT-04: iniciativa ─────────────────────────────────────────────────

    /**
     * Rolagem de iniciativa: 1d20 + destreza + percepcao — RF-CT-04.
     */
    public static int rollInitiative(int destreza, int percepcao, DiceRoller dice) {
        return dice.d20() + destreza + percepcao;
    }

    // ── RF-CT-08: teste de acerto vs defesaFinal ─────────────────────────────

    /**
     * Teste de acerto: 1d20 + precisaoFinal — RF-CT-08.
     */
    public static int rollAttack(int precisaoFinal, DiceRoller dice) {
        return dice.d20() + precisaoFinal;
    }

    /**
     * Verifica se o ataque acerta: testeAcerto >= defesaFinal — RF-CT-08.
     */
    public static boolean isHit(int testeAcerto, int defesaFinal) {
        return testeAcerto >= defesaFinal;
    }

    // ── RF-CT-09: bloqueio ───────────────────────────────────────────────────

    /**
     * Dano após bloqueio — RF-CT-09.
     * dano_pos_bloqueio = max(0, floor(danoBruto × (1 - bloqueioPctFinal)))
     * onde bloqueioPctFinal = bloqueioFinal / 100, clampado em [0, 1].
     */
    public static int damageAfterBlock(int danoBruto, int bloqueioFinal) {
        double bloqueioPct = Math.max(0.0, Math.min(1.0, bloqueioFinal / 100.0));
        return Math.max(0, (int) Math.floor(danoBruto * (1.0 - bloqueioPct)));
    }

    // ── RF-CT-10: resistência ────────────────────────────────────────────────

    /**
     * Dano após resistência por tipo — RF-CT-10.
     * dano_pos_resistencia = max(0, floor(danoPosBloquio × (1 - resistenciaTipo)))
     * Para jogadores, resistenciaTipo é limitado a 0,80 (RF-CT-10).
     *
     * @param danoPosBloquio     dano após bloqueio
     * @param resistenciaTipo    fração de resistência [0,0 a 1,0] (já aplicado o limite do jogador externamente)
     */
    public static int damageAfterResistance(int danoPosBloquio, double resistenciaTipo) {
        double resistClamp = Math.max(0.0, Math.min(1.0, resistenciaTipo));
        return Math.max(0, (int) Math.floor(danoPosBloquio * (1.0 - resistClamp)));
    }

    /**
     * Aplica o limite máximo de resistência para personagens jogadores — RF-CT-10.
     */
    public static double clampPlayerResistance(double resistenciaTipo) {
        return Math.min(resistenciaTipo, CharacterAttributeService.MAX_RESISTANCE_PLAYER);
    }

    // ── RF-CT-11: aflições ───────────────────────────────────────────────────

    /**
     * Calcula chance final de aplicação de aflição — RF-CT-11.
     * chance_final = max(minChance, chanceBase × (1 - resistenciaAflicao))
     */
    public static double afflictionChance(double chanceBase, double resistenciaAflicao, double minChance) {
        double resistClamp = Math.max(0.0, Math.min(1.0, resistenciaAflicao));
        return Math.max(minChance, chanceBase * (1.0 - resistClamp));
    }

    /**
     * Calcula duração final de aflição — RF-CT-11.
     * duracao_final = max(1, floor(duracaoBase × (1 - resistenciaAflicao)))
     */
    public static int afflictionDuration(int duracaoBase, double resistenciaAflicao) {
        double resistClamp = Math.max(0.0, Math.min(1.0, resistenciaAflicao));
        return Math.max(1, (int) Math.floor(duracaoBase * (1.0 - resistClamp)));
    }

    /**
     * Calcula intensidade final de aflição — RF-CT-11.
     * intensidade_final = floor(intensidadeBase × (1 - resistenciaAflicao))
     */
    public static int afflictionIntensity(int intensidadeBase, double resistenciaAflicao) {
        double resistClamp = Math.max(0.0, Math.min(1.0, resistenciaAflicao));
        return (int) Math.floor(intensidadeBase * (1.0 - resistClamp));
    }

    // ── RF-CT-07: pipeline de resolução (etapas 3–7) ─────────────────────────

    /**
     * Resultado final de dano após bloqueio e resistência (etapas 3–5 do pipeline) — RF-CT-07.
     *
     * @param danoBruto          dano bruto calculado pelas fórmulas de RF-FP-06.6
     * @param bloqueioFinal      valor final de bloqueio
     * @param resistenciaTipo    resistência por tipo de dano [0,0 a 1,0] (já limitada externamente para jogadores)
     */
    public static int resolveDamage(int danoBruto, int bloqueioFinal, double resistenciaTipo) {
        int posBloquio = damageAfterBlock(danoBruto, bloqueioFinal);
        return damageAfterResistance(posBloquio, resistenciaTipo);
    }

    // ── RF-CT-05: teste de percepção ─────────────────────────────────────────

    /**
     * Rolagem de percepção: percepcao + 1d20 — RF-CT-05.
     */
    public static int rollPerception(int percepcao, DiceRoller dice) {
        return percepcao + dice.d20();
    }

    /**
     * Verifica se o observador detecta o alvo oculto — RF-CT-05.
     * Empate: prevalece o oculto (retorna {@code false}).
     *
     * @param perceptionRoll  resultado do teste de percepção do observador
     * @param furtividadeRoll resultado do teste de furtividade do oculto
     * @return {@code true} se o observador detectar; {@code false} em empate ou falha
     */
    public static boolean detectsTarget(int perceptionRoll, int furtividadeRoll) {
        return perceptionRoll > furtividadeRoll;
    }

    /**
     * Verifica se o observador detecta um alvo fixo com CD de ambiente — RF-CT-05.
     *
     * @param perceptionRoll resultado do teste de percepção
     * @param cdAmbiente     CD fixa do ambiente
     * @return {@code true} se percepção >= CD
     */
    public static boolean detectsVsCd(int perceptionRoll, int cdAmbiente) {
        return perceptionRoll >= cdAmbiente;
    }

    // ── RF-CT-12: ação preparada ──────────────────────────────────────────────

    /**
     * Verifica se uma ação preparada foi cancelada por alguma das condições definidas em RF-CT-12.
     *
     * @param isParalysed              personagem afetado por paralisia
     * @param isSleepTorpor            personagem afetado por sono/torpor
     * @param isBlindedWithVisualTarget personagem cego e a ação exige alvo visual
     * @param forcedDisplacementOver3m personagem sofreu deslocamento forçado > 3m
     * @param triggerBecameInvalid     gatilho declarado tornou-se inválido
     * @param nextTurnStarted          início do próximo turno ocorreu sem o gatilho
     * @return {@code true} se a ação preparada foi cancelada
     */
    public static boolean isPreparedActionCancelled(
            boolean isParalysed,
            boolean isSleepTorpor,
            boolean isBlindedWithVisualTarget,
            boolean forcedDisplacementOver3m,
            boolean triggerBecameInvalid,
            boolean nextTurnStarted) {

        return isParalysed
                || isSleepTorpor
                || isBlindedWithVisualTarget
                || forcedDisplacementOver3m
                || triggerBecameInvalid
                || nextTurnStarted;
    }
}
