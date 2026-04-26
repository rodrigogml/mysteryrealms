package br.eng.rodrigogml.mysteryrealms.domain.social.model;

import br.eng.rodrigogml.mysteryrealms.domain.social.enums.SpeechStyle;

/**
 * Opção dentro de um nó de diálogo — RF-SS-01.
 */
public record DialogueOption(
        String opcaoId,
        SpeechStyle estiloFala,
        String textoOpcao,
        /** Presente quando há risco ou oposição — RF-SS-01. */
        SocialTest testeSocial,
        /** Efeitos quando não há teste ou teste passa — RF-SS-01. */
        DialogueEffects efeitosSucesso,
        /** Obrigatório quando {@code testeSocial} estiver presente — RF-SS-01. */
        DialogueEffects efeitosFalha) {

    public DialogueOption {
        if (opcaoId == null || opcaoId.isBlank())
            throw new IllegalArgumentException("opcaoId não pode ser vazio");
        if (estiloFala == null)
            throw new IllegalArgumentException("estiloFala não pode ser nulo");
        if (textoOpcao == null || textoOpcao.isBlank())
            throw new IllegalArgumentException("textoOpcao não pode ser vazio");
        if (efeitosSucesso == null)
            throw new IllegalArgumentException("efeitosSucesso não pode ser nulo");
        if (testeSocial != null && efeitosFalha == null)
            throw new IllegalArgumentException("efeitosFalha é obrigatório quando testeSocial estiver presente");
    }
}
