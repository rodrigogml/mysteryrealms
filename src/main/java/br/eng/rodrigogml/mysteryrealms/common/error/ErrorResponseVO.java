package br.eng.rodrigogml.mysteryrealms.common.error;

/**
 * Resposta de erro padronizada para endpoints, comandos e serviços.
 *
 * @param code código rastreável de erro
 * @param messageKey chave i18n da mensagem
 * @param traceId identificador único do incidente
 *
 * @author ?
 * @since 01-05-2026
 */
public record ErrorResponseVO(String code, String messageKey, String traceId) {
}
