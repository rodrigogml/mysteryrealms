package br.eng.rodrigogml.mysteryrealms.domain.world.model;

import br.eng.rodrigogml.mysteryrealms.domain.world.enums.TipoLocalizacao;

/**
 * Ambiente navegável — RF-MN-03.
 *
 * ID deve usar prefixo {@code amb_} e ser único globalmente.
 * {@code tipo_navegavel} é sempre {@link TipoLocalizacao#AMBIENTE}.
 */
public record AmbienteJogo(
        String idAmbiente,
        String nome,
        String idZona,
        double coordX,
        double coordY,
        boolean acessivel,
        /** Texto de ambientação (opcional). */
        String descricao) {

    public static final TipoLocalizacao TIPO_NAVEGAVEL = TipoLocalizacao.AMBIENTE;

    public AmbienteJogo {
        if (idAmbiente == null || idAmbiente.isBlank())
            throw new IllegalArgumentException("idAmbiente não pode ser vazio");
        if (!idAmbiente.startsWith("amb_"))
            throw new IllegalArgumentException("idAmbiente deve começar com 'amb_': " + idAmbiente);
        if (nome == null || nome.isBlank())
            throw new IllegalArgumentException("nome não pode ser vazio");
        if (idZona == null || idZona.isBlank())
            throw new IllegalArgumentException("idZona não pode ser vazio");
    }
}
