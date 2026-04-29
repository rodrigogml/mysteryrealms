package br.eng.rodrigogml.mysteryrealms.domain.modifier.model;

import br.eng.rodrigogml.mysteryrealms.domain.modifier.enums.ModifierOrigin;
import br.eng.rodrigogml.mysteryrealms.domain.modifier.enums.StackingRule;
import java.util.regex.Pattern;

/**
 * Estrutura de um modificador — RF-MAR-04.
 *
 * O ID deve ser slug sem acento em snake_case.
 * Origens seguem a ordem canônica de priority em {@link ModifierOrigin} — RF-MAR-05.
 */
public record Modifier(
        String id,
        String displayName,
        String trigger,
        ModifierEffect effect,
        ModifierDuration duration,
        StackingRule stackingRule,
        ModifierOrigin origin) {

    private static final Pattern ID_PATTERN = Pattern.compile("^[a-z0-9]+(?:_[a-z0-9]+)*$");

    public Modifier {
        if (id == null || id.isBlank())
            throw new IllegalArgumentException("id do modificador não pode ser vazio");
        if (!ID_PATTERN.matcher(id).matches())
            throw new IllegalArgumentException("id deve ser snake_case sem acentos: " + id);
        if (displayName == null || displayName.isBlank())
            throw new IllegalArgumentException("nomeExibicao não pode ser vazio");
        if (trigger == null || trigger.isBlank())
            throw new IllegalArgumentException("gatilho não pode ser vazio");
        if (effect == null)
            throw new IllegalArgumentException("efeito não pode ser nulo");
        if (duration == null)
            throw new IllegalArgumentException("duracao não pode ser nula");
        if (stackingRule == null)
            throw new IllegalArgumentException("regraEmpilhamento não pode ser nula");
        if (origin == null)
            throw new IllegalArgumentException("origem não pode ser nula");
    }
}
