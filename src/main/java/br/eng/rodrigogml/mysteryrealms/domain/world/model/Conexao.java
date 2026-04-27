package br.eng.rodrigogml.mysteryrealms.domain.world.model;

import br.eng.rodrigogml.mysteryrealms.domain.world.enums.ClassificacaoConexao;

import java.util.List;

/**
 * Conexão entre zonas/ambientes — RF-MN-04.
 *
 * {@code penaldadeRotaPct} ∈ [0, 15].
 * {@code chanceInterrupcaoKmPct} ∈ [0, 100].
 * Deve ter pelo menos 1 destino em {@code destinosPriorizados}.
 */
public record Conexao(
        String idConexao,
        String origemId,
        List<String> destinosPriorizados,
        boolean bidirecional,
        ClassificacaoConexao classificacao,
        double penaldadeRotaPct,
        double chanceInterrupcaoKmPct,
        String tabelaInterrupcoesId) {

    public Conexao {
        if (idConexao == null || idConexao.isBlank())
            throw new IllegalArgumentException("idConexao não pode ser vazio");
        if (origemId == null || origemId.isBlank())
            throw new IllegalArgumentException("origemId não pode ser vazio");
        if (destinosPriorizados == null || destinosPriorizados.isEmpty())
            throw new IllegalArgumentException("destinosPriorizados deve ter pelo menos 1 destino");
        if (classificacao == null)
            throw new IllegalArgumentException("classificacao não pode ser nula");
        if (penaldadeRotaPct < 0 || penaldadeRotaPct > 15)
            throw new IllegalArgumentException("penaldadeRotaPct deve estar em [0, 15]: " + penaldadeRotaPct);
        if (chanceInterrupcaoKmPct < 0 || chanceInterrupcaoKmPct > 100)
            throw new IllegalArgumentException("chanceInterrupcaoKmPct deve estar em [0, 100]: " + chanceInterrupcaoKmPct);
        if (tabelaInterrupcoesId == null || tabelaInterrupcoesId.isBlank())
            throw new IllegalArgumentException("tabelaInterrupcoesId não pode ser vazio");
        destinosPriorizados = List.copyOf(destinosPriorizados);
    }
}
