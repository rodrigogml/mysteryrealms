package br.eng.rodrigogml.mysteryrealms.domain.social.model;

import br.eng.rodrigogml.mysteryrealms.domain.social.enums.EstiloDiscurso;

/**
 * Opção dentro de um nó de diálogo — RF-SS-01.
 */
public record OpcaoDialogo(
        String opcaoId,
        EstiloDiscurso estiloFala,
        String textoOpcao,
        /** Presente quando há risco ou oposição — RF-SS-01. */
        TesteSocial testeSocial,
        /** Efeitos quando não há teste ou teste passa — RF-SS-01. */
        EfeitosDialogo efeitosSucesso,
        /** Obrigatório quando {@code testeSocial} estiver presente — RF-SS-01. */
        EfeitosDialogo efeitosFalha) {

    public OpcaoDialogo {
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
