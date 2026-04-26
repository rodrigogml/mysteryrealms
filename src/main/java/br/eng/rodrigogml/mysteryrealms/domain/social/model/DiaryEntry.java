package br.eng.rodrigogml.mysteryrealms.domain.social.model;

/**
 * Entrada do diário do jogador — RF-SS-08.
 *
 * {@code entradaId} deve usar prefixo {@code diary_}.
 * {@code titulo} deve ter no máximo 8 palavras.
 * {@code dataJogo} segue o formato {@code D<n>-HH:MM}.
 */
public record DiaryEntry(
        String entradaId,
        String titulo,
        String resumo,
        String dataJogo,
        String origemDialogoId,
        String origemOpcaoId,
        DiaryImpact impactos) {

    public DiaryEntry {
        if (entradaId == null || entradaId.isBlank())
            throw new IllegalArgumentException("entradaId não pode ser vazio");
        if (!entradaId.startsWith("diary_"))
            throw new IllegalArgumentException("entradaId deve começar com 'diary_': " + entradaId);
        if (titulo == null || titulo.isBlank())
            throw new IllegalArgumentException("titulo não pode ser vazio");
        if (titulo.trim().split("\\s+").length > 8)
            throw new IllegalArgumentException("titulo deve ter no máximo 8 palavras: " + titulo);
        if (resumo == null || resumo.isBlank())
            throw new IllegalArgumentException("resumo não pode ser vazio");
        if (dataJogo == null || !dataJogo.matches("D\\d+-\\d{2}:\\d{2}"))
            throw new IllegalArgumentException("dataJogo deve seguir o formato D<n>-HH:MM: " + dataJogo);
        if (origemDialogoId == null || origemDialogoId.isBlank())
            throw new IllegalArgumentException("origemDialogoId não pode ser vazio");
        if (origemOpcaoId == null || origemOpcaoId.isBlank())
            throw new IllegalArgumentException("origemOpcaoId não pode ser vazio");
        if (impactos == null)
            throw new IllegalArgumentException("impactos não pode ser nulo");
    }
}
