package br.eng.rodrigogml.mysteryrealms.domain.social.service;

import br.eng.rodrigogml.mysteryrealms.domain.social.enums.RelationshipBand;
import br.eng.rodrigogml.mysteryrealms.domain.social.enums.SpeechStyleValuation;

/**
 * Serviço do sistema social — RF-SS-02 a RF-SS-07.
 *
 * Implementa relacionamentos, reputação, testes sociais e ajustes de estilo de fala.
 * Stateless: todos os métodos são estáticos.
 */
public final class SocialService {

    private SocialService() {}

    // ── RF-SS-05: Teste social ────────────────────────────────────────────────

    /**
     * Aplica o ajuste de estilo de fala ao resultado do teste — RF-SS-04 / RF-SS-05.
     *
     * @param resultadoTeste resultado base do teste (valor_base + 1d20 + modificadores)
     * @param valoracao      valoração do NPC para o estilo de fala escolhido
     * @return resultado ajustado (+2, 0 ou -2)
     */
    public static int ajustarPorEstiloFala(int resultadoTeste, SpeechStyleValuation valoracao) {
        if (valoracao == null) return resultadoTeste;
        return resultadoTeste + valoracao.getAjusteTeste();
    }

    // ── RF-SS-06: Relacionamento com NPC ─────────────────────────────────────

    /**
     * Aplica delta de relacionamento com NPC, clampado em [-100, 100] — RF-SS-06.
     *
     * @param valorAtual valor atual do relacionamento
     * @param delta      incremento (pode ser negativo)
     * @return novo valor clampado
     */
    public static int aplicarDeltaRelacionamento(int valorAtual, int delta) {
        return Math.max(-100, Math.min(100, valorAtual + delta));
    }

    /**
     * Retorna a faixa canônica de relacionamento — RF-SS-06.
     */
    public static RelationshipBand faixaRelacionamento(int valor) {
        return RelationshipBand.of(valor);
    }

    // ── RF-SS-07: Reputação por localidade/facção ─────────────────────────────

    /**
     * Aplica delta de reputação (sem limite fixo de clamp) — RF-SS-07.
     *
     * @param valorAtual valor atual de reputação
     * @param delta      incremento (pode ser negativo)
     * @return novo valor
     */
    public static int aplicarDeltaReputacao(int valorAtual, int delta) {
        return valorAtual + delta;
    }
}
