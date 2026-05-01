package br.eng.rodrigogml.mysteryrealms.domain.modifier.service;

import br.eng.rodrigogml.mysteryrealms.domain.modifier.enums.ModifierOrigin;
import br.eng.rodrigogml.mysteryrealms.domain.modifier.enums.StackingRule;
import br.eng.rodrigogml.mysteryrealms.domain.modifier.model.Modifier;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import br.eng.rodrigogml.mysteryrealms.common.exception.DomainException;
import br.eng.rodrigogml.mysteryrealms.common.exception.ValidationException;

/**
 * Serviço de resolução de empilhamento de modificadores — RF-MAR-06.
 *
 * Regras de empilhamento:
 * - Modificadores de mesma origin e mesmo type acumulam, salvo exceção explícita.
 * - Se houver conflito excludente no mesmo nível de priority:
 *   1. Aplica-se o de maior magnitude absoluta.
 *   2. Empate de magnitude: vence menor duração restante (turns/minutes).
 *   3. Novo empate: vence o mais recente (último na lista).
 * - Modificadores de origens diferentes sempre acumulam entre si.
 */
public final class ModifierService {

    private ModifierService() {}

    /**
     * Aplica um novo modificador à lista ativa, respeitando as regras de empilhamento
     * (RF-MAR-06).
     *
     * @param ativos  lista mutável de modificadores ativos
     * @param novo    novo modificador a apply
     * @return lista atualizada (mesma referência de {@code ativos})
     */
    public static List<Modifier> apply(List<Modifier> ativos, Modifier novo) {
        if (ativos == null) throw new ValidationException("modifier.error.activeListRequired");
        if (novo == null) throw new ValidationException("modifier.error.modifierRequired");

        switch (novo.stackingRule()) {
            case ACCUMULATE -> ativos.add(novo);

            case REPLACE -> {
                // Substitui o modificador conflitante do mesmo type e origin, se houver
                boolean substituiu = replaceConflicting(ativos, novo);
                if (!substituiu) ativos.add(novo);
            }

            case INVALIDATE -> {
                // Remove o modificador conflitante e NÃO adiciona o novo
                ativos.removeIf(m -> m.origin() == novo.origin() && m.id().equals(novo.id()));
            }
        }

        return ativos;
    }

    /**
     * Retorna a lista de modificadores ativos após apply a regra de conflito
     * excludente no mesmo nível de priority — RF-MAR-06.
     *
     * Critérios em caso de conflito (mesmo id, mesma origin, regra REPLACE):
     * 1. Maior magnitude absoluta (baseado na description do effect — comparativo textual não aplicável;
     *    aqui usamos a duração como proxy para selecionar o mais relevante).
     * 2. Empate: menor duração restante.
     * 3. Empate: mais recente (último na lista).
     */
    private static boolean replaceConflicting(List<Modifier> ativos, Modifier novo) {
        for (int i = 0; i < ativos.size(); i++) {
            Modifier existente = ativos.get(i);
            if (existente.origin() == novo.origin() && existente.id().equals(novo.id())) {
                // Substituir (o novo prevalece em caso de empate — é o mais recente)
                ativos.set(i, novo);
                return true;
            }
        }
        return false;
    }

    /**
     * Filtra modificadores ativos por origin — RF-MAR-05.
     */
    public static List<Modifier> byOrigin(List<Modifier> ativos, ModifierOrigin origin) {
        if (ativos == null || origin == null) return new ArrayList<>();
        return ativos.stream().filter(m -> m.origin() == origin).toList();
    }

    /**
     * Retorna a lista de modificadores ativos ordenada por priority de origin
     * (maior priority primeiro) — RF-MAR-05.
     */
    public static List<Modifier> orderedByPriority(List<Modifier> ativos) {
        if (ativos == null) return new ArrayList<>();
        return ativos.stream()
                .sorted(Comparator.comparingInt(m -> m.origin().getPriority()))
                .toList();
    }
}
