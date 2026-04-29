package br.eng.rodrigogml.mysteryrealms.domain.social.model;

import java.util.List;
import java.util.HashSet;
import java.util.Set;

/**
 * Nó de diálogo — RF-SS-01.
 *
 * O {@code dialogId} deve usar o prefixo {@code dlg_}.
 * Deve conter pelo menos 1 opção.
 */
public record DialogNode(
        String dialogId,
        String npcId,
        String text,
        List<DialogOption> options) {

    public DialogNode {
        if (dialogId == null || dialogId.isBlank())
            throw new IllegalArgumentException("dialogoId não pode ser vazio");
        if (!dialogId.startsWith("dlg_"))
            throw new IllegalArgumentException("dialogoId deve começar com 'dlg_': " + dialogId);
        if (npcId == null || npcId.isBlank())
            throw new IllegalArgumentException("npcId não pode ser vazio");
        if (text == null || text.isBlank())
            throw new IllegalArgumentException("texto não pode ser vazio");
        if (options == null || options.isEmpty())
            throw new IllegalArgumentException("opcoes deve ter pelo menos 1 elemento");
        options = List.copyOf(options);
        Set<String> optionIds = new HashSet<>();
        for (DialogOption option : options) {
            if (!optionIds.add(option.optionId()))
                throw new IllegalArgumentException("opcaoId deve ser unico dentro do no: " + option.optionId());
        }
    }
}
