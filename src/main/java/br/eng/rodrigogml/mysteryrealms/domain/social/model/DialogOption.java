package br.eng.rodrigogml.mysteryrealms.domain.social.model;

import br.eng.rodrigogml.mysteryrealms.domain.social.enums.DiscourseStyle;

/**
 * Opção dentro de um nó de diálogo — RF-SS-01.
 */
public record DialogOption(
        String optionId,
        DiscourseStyle talkStyle,
        String optionText,
        /** Presente quando há risco ou oposição — RF-SS-01. */
        SocialTest socialTest,
        /** Efeitos quando não há teste ou teste passa — RF-SS-01. */
        DialogEffects successEffects,
        /** Obrigatório quando {@code socialTest} estiver presente — RF-SS-01. */
        DialogEffects failureEffects) {

    public DialogOption {
        if (optionId == null || optionId.isBlank())
            throw new IllegalArgumentException("opcaoId não pode ser vazio");
        if (talkStyle == null)
            throw new IllegalArgumentException("estiloFala não pode ser nulo");
        if (optionText == null || optionText.isBlank())
            throw new IllegalArgumentException("textoOpcao não pode ser vazio");
        if (successEffects == null)
            throw new IllegalArgumentException("efeitosSucesso não pode ser nulo");
        if (socialTest != null && failureEffects == null)
            throw new IllegalArgumentException("efeitosFalha é obrigatório quando testeSocial estiver presente");
    }
}
