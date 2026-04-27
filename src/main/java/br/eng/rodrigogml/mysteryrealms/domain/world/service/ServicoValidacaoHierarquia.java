package br.eng.rodrigogml.mysteryrealms.domain.world.service;

import br.eng.rodrigogml.mysteryrealms.domain.world.model.Connection;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Validações de consistência hierárquica do mundo — RF-MN-06.
 *
 * Todos os métodos são estáticos e stateless.
 */
public final class ServicoValidacaoHierarquia {

    private ServicoValidacaoHierarquia() {}

    // ── RF-MN-06: validações de consistência hierárquica ─────────────────────

    /**
     * Verifica se um ID começa com o prefixo esperado — RF-MN-01, RF-MN-06.
     */
    public static boolean temPrefixoValido(String id, String expectedPrefix) {
        if (id == null || expectedPrefix == null) return false;
        return id.startsWith(expectedPrefix);
    }

    /**
     * Verifica se um ID de Zona usa o prefixo canônico {@code zona_} — RF-MN-01.
     */
    public static boolean validarPrefixoZona(String zoneId) {
        return temPrefixoValido(zoneId, "zona_");
    }

    /**
     * Verifica se um ID de Ambiente usa o prefixo canônico {@code amb_} — RF-MN-01.
     */
    public static boolean validarPrefixoAmbiente(String ambId) {
        return temPrefixoValido(ambId, "amb_");
    }

    /**
     * Verifica se os IDs de zonas e ambientes são únicos globalmente — RF-MN-06.
     *
     * Não deve haver nenhum ID duplicado entre as duas listas combinadas.
     *
     * @param zonaIds lista de IDs de Zona
     * @param ambIds  lista de IDs de Ambiente
     * @return {@code true} se todos os IDs forem distintos
     */
    public static boolean idsGlobalmenteUnicos(List<String> zonaIds, List<String> ambIds) {
        Set<String> seen = new HashSet<>();
        for (String id : zonaIds) {
            if (!seen.add(id)) return false;
        }
        for (String id : ambIds) {
            if (!seen.add(id)) return false;
        }
        return true;
    }

    /**
     * Verifica se as coordenadas são valores finitos válidos — RF-MN-06.
     */
    public static boolean temCoordenadasValidas(double coordX, double coordY) {
        return Double.isFinite(coordX) && Double.isFinite(coordY);
    }

    /**
     * Verifica se todos os destinos e a origem de uma Conexão existem no conjunto de nós conhecidos — RF-MN-06.
     *
     * @param conn         conexão a validar
     * @param knownNodeIds conjunto de IDs de nós (zonas e ambientes) existentes
     * @return {@code true} se origem e todos os destinos forem encontrados
     */
    public static boolean destinosConexaoExistem(Connection conn, Set<String> knownNodeIds) {
        if (!knownNodeIds.contains(conn.origemId())) return false;
        for (String destId : conn.destinosPriorizados()) {
            if (!knownNodeIds.contains(destId)) return false;
        }
        return true;
    }

    /**
     * Verifica se o tipo navegável declarado é coerente com o tipo esperado — RF-MN-06.
     *
     * @param tipoNavegavel  valor declarado no campo {@code tipo_navegavel}
     * @param expectedType   valor esperado (ex.: "zona", "ambiente")
     * @return {@code true} se os valores forem iguais (comparação case-insensitive)
     */
    public static boolean tipoNavegavelCoerente(String tipoNavegavel, String expectedType) {
        if (tipoNavegavel == null || expectedType == null) return false;
        return tipoNavegavel.equalsIgnoreCase(expectedType);
    }
}
