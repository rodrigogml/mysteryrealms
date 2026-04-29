package br.eng.rodrigogml.mysteryrealms.domain.world.service;

import br.eng.rodrigogml.mysteryrealms.domain.world.model.Connection;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Validações de consistência hierárquica do mundo — RF-MN-06.
 *
 * Todos os métodos são estáticos e stateless.
 */
public final class HierarchyValidationService {

    private static final Pattern CANONICAL_ID_PATTERN = Pattern.compile("^[a-z0-9_]+$");
    private static final Pattern NAVIGABLE_ID_PATTERN = Pattern.compile("^(zona|amb)_[a-z0-9_]+$");

    private HierarchyValidationService() {}

    // ── RF-MN-06: validações de consistência hierárquica ─────────────────────

    /**
     * Verifica se um ID começa com o prefixo esperado — RF-MN-01, RF-MN-06.
     */
    public static boolean hasValidPrefix(String id, String expectedPrefix) {
        if (id == null || expectedPrefix == null) return false;
        return id.startsWith(expectedPrefix) && CANONICAL_ID_PATTERN.matcher(id).matches();
    }

    /**
     * Verifica se um ID de Zone usa o prefixo canônico {@code zona_} — RF-MN-01.
     */
    public static boolean validateZonePrefix(String zoneId) {
        return hasValidPrefix(zoneId, "zona_");
    }

    /**
     * Verifica se um ID de Ambiente usa o prefixo canônico {@code amb_} — RF-MN-01.
     */
    public static boolean validateEnvironmentPrefix(String ambId) {
        return hasValidPrefix(ambId, "amb_");
    }

    /**
     * Verifica se os IDs de zonas e ambientes são únicos globalmente — RF-MN-06.
     *
     * Não deve haver nenhum ID duplicado entre as duas listas combinadas.
     *
     * @param zonaIds lista de IDs de Zone
     * @param ambIds  lista de IDs de Ambiente
     * @return {@code true} se todos os IDs forem distintos
     */
    public static boolean idsAreGloballyUnique(List<String> zonaIds, List<String> ambIds) {
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
    public static boolean hasValidCoordinates(double coordX, double coordY) {
        return Double.isFinite(coordX) && Double.isFinite(coordY);
    }

    /**
     * Verifica se o ID navegável pertence ao formato canônico de Zona/Ambiente — RF-MN-01, RF-MN-06.
     */
    public static boolean isValidNavigableId(String id) {
        if (id == null) return false;
        return NAVIGABLE_ID_PATTERN.matcher(id).matches();
    }

    /**
     * Verifica se todos os destinos e a origin de uma Conexão existem no conjunto de nós conhecidos — RF-MN-06.
     *
     * @param conn         conexão a validar
     * @param knownNodeIds conjunto de IDs de nós (zonas e ambientes) existentes
     * @return {@code true} se origin e todos os destinos forem encontrados
     */
    public static boolean connectionDestinationsExist(Connection conn, Set<String> knownNodeIds) {
        if (!isValidNavigableId(conn.originId()) || !knownNodeIds.contains(conn.originId())) return false;
        for (String destId : conn.prioritizedDestinations()) {
            if (!isValidNavigableId(destId) || !knownNodeIds.contains(destId)) return false;
        }
        return true;
    }

    /**
     * Verifica se origem e ao menos um destino elegível da conexão estão acessíveis — RF-MN-06.
     *
     * @param conn conexão a validar
     * @param accessibleNodes mapa de id → acessibilidade dos nós conhecidos
     * @return {@code true} se a origem e pelo menos um destino estiverem acessíveis
     */
    public static boolean connectionHasAccessibleEndpoint(Connection conn, Map<String, Boolean> accessibleNodes) {
        if (!Boolean.TRUE.equals(accessibleNodes.get(conn.originId()))) {
            return false;
        }
        for (String destId : conn.prioritizedDestinations()) {
            if (Boolean.TRUE.equals(accessibleNodes.get(destId))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Verifica se o type navegável declarado é coerente com o type esperado — RF-MN-06.
     *
     * @param tipoNavegavel  value declarado no campo {@code tipo_navegavel}
     * @param tipoEsperado   value esperado (ex.: "zona", "ambiente")
     * @return {@code true} se os valores forem iguais (comparação case-insensitive)
     */
    public static boolean isNavigableTypeCoherent(String tipoNavegavel, String tipoEsperado) {
        if (tipoNavegavel == null || tipoEsperado == null) return false;
        return tipoNavegavel.equalsIgnoreCase(tipoEsperado);
    }
}
