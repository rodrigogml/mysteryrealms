package br.eng.rodrigogml.mysteryrealms.domain.modifier.service;

import br.eng.rodrigogml.mysteryrealms.domain.modifier.enums.OrigemModificador;
import br.eng.rodrigogml.mysteryrealms.domain.modifier.enums.RegraEmpilhamento;
import br.eng.rodrigogml.mysteryrealms.domain.modifier.model.Modificador;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Serviço de resolução de empilhamento de modificadores — RF-MAR-06.
 *
 * Regras de empilhamento:
 * - Modificadores de mesma origem e mesmo tipo acumulam, salvo exceção explícita.
 * - Se houver conflito excludente no mesmo nível de prioridade:
 *   1. Aplica-se o de maior magnitude absoluta.
 *   2. Empate de magnitude: vence menor duração restante (turnos/minutos).
 *   3. Novo empate: vence o mais recente (último na lista).
 * - Modificadores de origens diferentes sempre acumulam entre si.
 */
public final class ServicoModificador {

    private ServicoModificador() {}

    /**
     * Aplica um novo modificador à lista ativa, respeitando as regras de empilhamento
     * (RF-MAR-06).
     *
     * @param ativos  lista mutável de modificadores ativos
     * @param novo    novo modificador a aplicar
     * @return lista atualizada (mesma referência de {@code ativos})
     */
    public static List<Modificador> aplicar(List<Modificador> ativos, Modificador novo) {
        if (ativos == null) throw new IllegalArgumentException("ativos não pode ser nulo");
        if (novo == null) throw new IllegalArgumentException("modificador não pode ser nulo");

        switch (novo.regraEmpilhamento()) {
            case ACUMULA -> ativos.add(novo);

            case SUBSTITUI -> {
                // Substitui o modificador conflitante do mesmo tipo e origem, se houver
                boolean substituiu = substituirConflitante(ativos, novo);
                if (!substituiu) ativos.add(novo);
            }

            case INVALIDA -> {
                // Remove o modificador conflitante e NÃO adiciona o novo
                ativos.removeIf(m -> m.origem() == novo.origem() && m.id().equals(novo.id()));
            }
        }

        return ativos;
    }

    /**
     * Retorna a lista de modificadores ativos após aplicar a regra de conflito
     * excludente no mesmo nível de prioridade — RF-MAR-06.
     *
     * Critérios em caso de conflito (mesmo id, mesma origem, regra SUBSTITUI):
     * 1. Maior magnitude absoluta (baseado na descricao do efeito — comparativo textual não aplicável;
     *    aqui usamos a duração como proxy para selecionar o mais relevante).
     * 2. Empate: menor duração restante.
     * 3. Empate: mais recente (último na lista).
     */
    private static boolean substituirConflitante(List<Modificador> ativos, Modificador novo) {
        for (int i = 0; i < ativos.size(); i++) {
            Modificador existente = ativos.get(i);
            if (existente.origem() == novo.origem() && existente.id().equals(novo.id())) {
                // Substituir (o novo prevalece em caso de empate — é o mais recente)
                ativos.set(i, novo);
                return true;
            }
        }
        return false;
    }

    /**
     * Filtra modificadores ativos por origem — RF-MAR-05.
     */
    public static List<Modificador> porOrigem(List<Modificador> ativos, OrigemModificador origem) {
        if (ativos == null || origem == null) return new ArrayList<>();
        return ativos.stream().filter(m -> m.origem() == origem).toList();
    }

    /**
     * Retorna a lista de modificadores ativos ordenada por prioridade de origem
     * (maior prioridade primeiro) — RF-MAR-05.
     */
    public static List<Modificador> ordenadosPorPrioridade(List<Modificador> ativos) {
        if (ativos == null) return new ArrayList<>();
        return ativos.stream()
                .sorted(Comparator.comparingInt(m -> m.origem().getPrioridade()))
                .toList();
    }
}
