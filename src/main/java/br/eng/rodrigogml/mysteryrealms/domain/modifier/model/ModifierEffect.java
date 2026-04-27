package br.eng.rodrigogml.mysteryrealms.domain.modifier.model;

/**
 * Efeito mecânico de um modificador — RF-MAR-04.
 *
 * Descreve o effect de forma objetiva. A interpretação concreta do effect
 * é responsabilidade do sistema que aplica o modificador.
 */
public record ModifierEffect(String description) {

    public ModifierEffect {
        if (description == null || description.isBlank())
            throw new IllegalArgumentException("descricao do efeito não pode ser vazia");
    }
}
