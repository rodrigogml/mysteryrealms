package br.eng.rodrigogml.mysteryrealms.domain.social.model;

import java.util.List;

/**
 * Nó de diálogo — RF-SS-01.
 *
 * O {@code dialogoId} deve usar o prefixo {@code dlg_}.
 * Deve conter pelo menos 1 opção.
 */
public record NoDialogo(
        String dialogoId,
        String npcId,
        String texto,
        List<OpcaoDialogo> opcoes) {

    public NoDialogo {
        if (dialogoId == null || dialogoId.isBlank())
            throw new IllegalArgumentException("dialogoId não pode ser vazio");
        if (!dialogoId.startsWith("dlg_"))
            throw new IllegalArgumentException("dialogoId deve começar com 'dlg_': " + dialogoId);
        if (npcId == null || npcId.isBlank())
            throw new IllegalArgumentException("npcId não pode ser vazio");
        if (texto == null || texto.isBlank())
            throw new IllegalArgumentException("texto não pode ser vazio");
        if (opcoes == null || opcoes.isEmpty())
            throw new IllegalArgumentException("opcoes deve ter pelo menos 1 elemento");
        opcoes = List.copyOf(opcoes);
    }
}
