package br.eng.rodrigogml.mysteryrealms.domain.modifier.model;

import br.eng.rodrigogml.mysteryrealms.domain.modifier.enums.OrigemModificador;
import br.eng.rodrigogml.mysteryrealms.domain.modifier.enums.StackingRule;

/**
 * Estrutura de um modificador — RF-MAR-04.
 *
 * O ID deve ser slug sem acento em snake_case.
 * Origens seguem a ordem canônica de prioridade em {@link OrigemModificador} — RF-MAR-05.
 */
public record Modificador(
        String id,
        String nomeExibicao,
        String gatilho,
        EfeitoModificador efeito,
        ModifierDuration duracao,
        StackingRule regraEmpilhamento,
        OrigemModificador origem) {

    public Modificador {
        if (id == null || id.isBlank())
            throw new IllegalArgumentException("id do modificador não pode ser vazio");
        if (!id.equals(id.toLowerCase()) || id.contains(" "))
            throw new IllegalArgumentException("id deve ser snake_case sem acentos: " + id);
        if (nomeExibicao == null || nomeExibicao.isBlank())
            throw new IllegalArgumentException("nomeExibicao não pode ser vazio");
        if (gatilho == null || gatilho.isBlank())
            throw new IllegalArgumentException("gatilho não pode ser vazio");
        if (efeito == null)
            throw new IllegalArgumentException("efeito não pode ser nulo");
        if (duracao == null)
            throw new IllegalArgumentException("duracao não pode ser nula");
        if (regraEmpilhamento == null)
            throw new IllegalArgumentException("regraEmpilhamento não pode ser nula");
        if (origem == null)
            throw new IllegalArgumentException("origem não pode ser nula");
    }
}
