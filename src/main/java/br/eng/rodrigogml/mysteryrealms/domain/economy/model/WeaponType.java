package br.eng.rodrigogml.mysteryrealms.domain.economy.model;

import java.util.Collections;
import java.util.List;

/**
 * Tipo de arma do sistema — RF-EI-06.
 *
 * <p>Define a classificação de armas. Cada tipo declara os campos obrigatórios
 * listados no requisito, além da lista opcional de compatibilidades por classe/raça.
 *
 * <p>Instâncias são imutáveis após a construção.
 *
 * @author ?
 * @since 28-04-2026
 */
public class WeaponType {

    /** Identificador único do tipo (nome_tipo). */
    private final String typeId;

    /** Função da arma (ex.: leve, pesada, distância, foco mágico). */
    private final String function;

    /** Atributo primário canônico usado em testes de ataque (ex.: {@code forca}, {@code destreza}). */
    private final String primaryAttribute;

    /** Número de mãos comum para o uso deste tipo: {@code 1} ou {@code 2}. */
    private final int commonHands;

    /** Alcance padrão (ex.: curto, médio, longo). */
    private final String defaultRange;

    /** Perfil de crítico padrão do tipo. */
    private final CriticalProfile criticalProfile;

    /**
     * Lista de classes/raças com afinidade natural com este tipo de arma.
     * Pode ser vazia; nunca {@code null}.
     */
    private final List<String> compatibilities;

    /**
     * Cria um novo tipo de arma com todos os campos obrigatórios e lista de compatibilidades.
     *
     * @param typeId          identificador único; não pode ser vazio
     * @param function        função da arma; não pode ser vazio
     * @param primaryAttribute atributo primário canônico; não pode ser vazio
     * @param commonHands     mãos comuns ({@code 1} ou {@code 2})
     * @param defaultRange    alcance padrão; não pode ser vazio
     * @param criticalProfile perfil de crítico; não pode ser {@code null}
     * @param compatibilities lista de compatibilidades; pode ser {@code null} (tratada como vazia)
     * @throws IllegalArgumentException em caso de parâmetros inválidos
     */
    public WeaponType(
            String typeId,
            String function,
            String primaryAttribute,
            int commonHands,
            String defaultRange,
            CriticalProfile criticalProfile,
            List<String> compatibilities) {

        if (typeId == null || typeId.isBlank())
            throw new IllegalArgumentException("typeId não pode ser vazio");
        if (function == null || function.isBlank())
            throw new IllegalArgumentException("function não pode ser vazio");
        if (primaryAttribute == null || primaryAttribute.isBlank())
            throw new IllegalArgumentException("primaryAttribute não pode ser vazio");
        if (commonHands != 1 && commonHands != 2)
            throw new IllegalArgumentException("commonHands deve ser 1 ou 2, recebido: " + commonHands);
        if (defaultRange == null || defaultRange.isBlank())
            throw new IllegalArgumentException("defaultRange não pode ser vazio");
        if (criticalProfile == null)
            throw new IllegalArgumentException("criticalProfile não pode ser nulo");

        this.typeId = typeId;
        this.function = function;
        this.primaryAttribute = primaryAttribute;
        this.commonHands = commonHands;
        this.defaultRange = defaultRange;
        this.criticalProfile = criticalProfile;
        this.compatibilities = (compatibilities == null)
                ? Collections.emptyList()
                : List.copyOf(compatibilities);
    }

    /**
     * Cria um novo tipo de arma sem lista de compatibilidades.
     *
     * @param typeId          identificador único
     * @param function        função da arma
     * @param primaryAttribute atributo primário canônico
     * @param commonHands     mãos comuns ({@code 1} ou {@code 2})
     * @param defaultRange    alcance padrão
     * @param criticalProfile perfil de crítico
     */
    public WeaponType(
            String typeId,
            String function,
            String primaryAttribute,
            int commonHands,
            String defaultRange,
            CriticalProfile criticalProfile) {
        this(typeId, function, primaryAttribute, commonHands, defaultRange, criticalProfile, null);
    }

    public String getTypeId() { return typeId; }
    public String getFunction() { return function; }
    public String getPrimaryAttribute() { return primaryAttribute; }
    public int getCommonHands() { return commonHands; }
    public String getDefaultRange() { return defaultRange; }
    public CriticalProfile getCriticalProfile() { return criticalProfile; }

    /**
     * Retorna a lista de compatibilidades (classes/raças com afinidade natural).
     * Nunca retorna {@code null}; pode ser vazia.
     *
     * @return lista imutável de compatibilidades
     */
    public List<String> getCompatibilities() { return compatibilities; }
}
