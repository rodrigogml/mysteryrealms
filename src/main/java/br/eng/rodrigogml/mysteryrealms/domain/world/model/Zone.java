package br.eng.rodrigogml.mysteryrealms.domain.world.model;

import br.eng.rodrigogml.mysteryrealms.domain.world.enums.TipoLocalizacao;

/**
 * Zona navegável — RF-MN-02.
 *
 * ID deve usar prefixo {@code zona_} e ser único globalmente.
 * {@code tipo_navegavel} é sempre {@link TipoLocalizacao#ZONA}.
 */
public record Zone(
        String idZona,
        String nome,
        String idLocalidade,
        double coordX,
        double coordY,
        boolean acessivel,
        /** Texto de ambientação (opcional). */
        String descricao) {

    public static final TipoLocalizacao TIPO_NAVEGAVEL = TipoLocalizacao.ZONA;

    public Zone {
        if (idZona == null || idZona.isBlank())
            throw new IllegalArgumentException("idZona não pode ser vazio");
        if (!idZona.startsWith("zona_"))
            throw new IllegalArgumentException("idZona deve começar com 'zona_': " + idZona);
        if (nome == null || nome.isBlank())
            throw new IllegalArgumentException("nome não pode ser vazio");
        if (idLocalidade == null || idLocalidade.isBlank())
            throw new IllegalArgumentException("idLocalidade não pode ser vazio");
    }
}
