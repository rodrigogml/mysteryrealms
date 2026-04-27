package br.eng.rodrigogml.mysteryrealms.domain.social.model;

import java.util.List;

/**
 * Resultado de um ciclo social — RF-SS-02.
 */
public record ResultadoCicloSocial(
        boolean sucesso,
        EntradaDiario entradaDiario,
        List<Marcador> marcadoresAlterados,
        Integer novoRelacionamentoNpc,
        Integer novaReputacao) {

    public ResultadoCicloSocial {
        marcadoresAlterados = marcadoresAlterados != null ? List.copyOf(marcadoresAlterados) : List.of();
    }
}
