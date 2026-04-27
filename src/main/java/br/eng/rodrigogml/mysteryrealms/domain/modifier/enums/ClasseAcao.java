package br.eng.rodrigogml.mysteryrealms.domain.modifier.enums;

/**
 * Classificação de ações do jogador, conforme RF-MAR-07.
 *
 * Define as quatro categorias canônicas que determinam custos de tempo,
 * fadiga, fome e sede de uma ação.
 */
public enum ClasseAcao {

    /** Observação, conversa breve, organização de inventário parado. */
    PASSIVA,

    /** Movimento padrão, coleta simples, interação tática leve. */
    MODERADA,

    /** Corrida prolongada, escalada difícil, combate intenso. */
    EXIGENTE,

    /** Descanso curto, hidratação, refeição leve, respiração guiada. */
    RECUPERATIVA
}
