package br.eng.rodrigogml.mysteryrealms.domain.modifier.model;

/**
 * Efeito mecânico de um modificador — RF-MAR-04.
 *
 * Descreve o efeito de forma objetiva. A interpretação concreta do efeito
 * é responsabilidade do sistema que aplica o modificador.
 */
public record ModifierEffect(String descricao) {

    public ModifierEffect {
        if (descricao == null || descricao.isBlank())
            throw new IllegalArgumentException("descricao do efeito não pode ser vazia");
    }
}
