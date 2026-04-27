package br.eng.rodrigogml.mysteryrealms.domain.social.model;

/**
 * Entrada do diário do jogador — RF-SS-08.
 *
 * {@code entryId} deve usar prefixo {@code diary_}.
 * {@code title} deve ter no máximo 8 palavras.
 * {@code gameDate} segue o formato {@code D<n>-HH:MM}.
 */
public record DiaryEntry(
        String entryId,
        String title,
        String summary,
        String gameDate,
        String dialogOriginId,
        String optionOriginId,
        DiaryImpact impacts) {

    public DiaryEntry {
        if (entryId == null || entryId.isBlank())
            throw new IllegalArgumentException("entradaId não pode ser vazio");
        if (!entryId.startsWith("diary_"))
            throw new IllegalArgumentException("entradaId deve começar com 'diary_': " + entryId);
        if (title == null || title.isBlank())
            throw new IllegalArgumentException("titulo não pode ser vazio");
        if (title.trim().split("\\s+").length > 8)
            throw new IllegalArgumentException("titulo deve ter no máximo 8 palavras: " + title);
        if (summary == null || summary.isBlank())
            throw new IllegalArgumentException("resumo não pode ser vazio");
        if (gameDate == null || !gameDate.matches("D\\d+-\\d{2}:\\d{2}"))
            throw new IllegalArgumentException("dataJogo deve seguir o formato D<n>-HH:MM: " + gameDate);
        if (dialogOriginId == null || dialogOriginId.isBlank())
            throw new IllegalArgumentException("origemDialogoId não pode ser vazio");
        if (optionOriginId == null || optionOriginId.isBlank())
            throw new IllegalArgumentException("origemOpcaoId não pode ser vazio");
        if (impacts == null)
            throw new IllegalArgumentException("impactos não pode ser nulo");
    }
}
