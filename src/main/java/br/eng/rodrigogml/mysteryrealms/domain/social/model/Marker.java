package br.eng.rodrigogml.mysteryrealms.domain.social.model;

import br.eng.rodrigogml.mysteryrealms.domain.social.enums.MarkerType;

import java.util.regex.Pattern;

/**
 * Marcador de progresso social, conforme RF-SS-09.
 *
 * A convencao de ID e {@code mk_<dominio>_<descricaoCamelCase>}.
 *
 * @author ?
 * @since 29-04-2026
 */
public class Marker {

    private static final Pattern MARKER_ID_PATTERN = Pattern.compile("mk_[a-z][A-Za-z0-9]*_[a-z][A-Za-z0-9]*");

    private final String id;
    private final MarkerType type;
    /** Boolean para FLAG; Integer para STAGE e COUNTER. */
    private Object value;

    /**
     * Cria um marcador.
     *
     * @param id identificador unico no formato {@code mk_<dominio>_<descricaoCamelCase>}
     * @param type tipo do marcador
     * @param initialValue valor inicial compativel com o tipo
     * @throws IllegalArgumentException se id, type ou value forem invalidos
     */
    public Marker(String id, MarkerType type, Object initialValue) {
        if (id == null || id.isBlank())
            throw new IllegalArgumentException("id do marcador nao pode ser vazio");
        if (!MARKER_ID_PATTERN.matcher(id).matches())
            throw new IllegalArgumentException("id do marcador deve seguir mk_<dominio>_<descricaoCamelCase>: " + id);
        if (type == null)
            throw new IllegalArgumentException("tipo do marcador nao pode ser nulo");
        validateValue(type, initialValue);

        this.id = id;
        this.type = type;
        this.value = initialValue;
    }

    /**
     * Cria um marcador {@link MarkerType#FLAG} ativo.
     *
     * @param id identificador unico no formato {@code mk_<dominio>_<descricaoCamelCase>}
     * @return novo marcador ativo
     */
    public static Marker flag(String id) {
        return new Marker(id, MarkerType.FLAG, Boolean.TRUE);
    }

    /**
     * Cria um marcador {@link MarkerType#FLAG} inativo.
     *
     * @param id identificador unico no formato {@code mk_<dominio>_<descricaoCamelCase>}
     * @return novo marcador inativo
     */
    public static Marker inactiveFlag(String id) {
        return new Marker(id, MarkerType.FLAG, Boolean.FALSE);
    }

    /**
     * Cria um marcador {@link MarkerType#STAGE} com valor inicial 0.
     *
     * @param id identificador unico no formato {@code mk_<dominio>_<descricaoCamelCase>}
     * @return novo marcador de estagio
     */
    public static Marker stage(String id) {
        return new Marker(id, MarkerType.STAGE, 0);
    }

    /**
     * Cria um marcador {@link MarkerType#COUNTER} com valor inicial 0.
     *
     * @param id identificador unico no formato {@code mk_<dominio>_<descricaoCamelCase>}
     * @return novo marcador contador
     */
    public static Marker counter(String id) {
        return new Marker(id, MarkerType.COUNTER, 0);
    }

    public String getId() { return id; }
    public MarkerType getType() { return type; }
    public Object getValue() { return value; }

    /**
     * Retorna {@code true} se este marcador {@link MarkerType#FLAG} estiver ativo.
     *
     * @return estado do flag
     * @throws IllegalStateException se o tipo nao for FLAG
     */
    public boolean isFlagActive() {
        if (type != MarkerType.FLAG)
            throw new IllegalStateException("Marcador nao e do tipo FLAG");
        return Boolean.TRUE.equals(value);
    }

    /**
     * Retorna o valor inteiro de marcadores {@link MarkerType#STAGE} ou {@link MarkerType#COUNTER}.
     *
     * @return valor inteiro atual
     * @throws IllegalStateException se o tipo for FLAG
     */
    public int intValue() {
        if (type == MarkerType.FLAG)
            throw new IllegalStateException("Marcador FLAG nao tem valor inteiro");
        return (Integer) value;
    }

    /**
     * Ativa ou desativa um marcador {@link MarkerType#FLAG}.
     *
     * @param active novo estado
     * @throws IllegalStateException se o tipo nao for FLAG
     */
    public void setFlag(boolean active) {
        if (type != MarkerType.FLAG)
            throw new IllegalStateException("setFlag so e valido para tipo FLAG");
        this.value = active;
    }

    /**
     * Define o estagio de um marcador {@link MarkerType#STAGE}.
     *
     * @param stage novo valor de estagio
     * @throws IllegalArgumentException se stage for negativo
     * @throws IllegalStateException se o tipo nao for STAGE
     */
    public void setStage(int stage) {
        if (type != MarkerType.STAGE)
            throw new IllegalStateException("setStage so e valido para tipo STAGE");
        if (stage < 0)
            throw new IllegalArgumentException("estagio do marcador nao pode ser negativo");
        this.value = stage;
    }

    /**
     * Incrementa um marcador numerico ({@link MarkerType#STAGE} ou {@link MarkerType#COUNTER}).
     *
     * @param delta valor a adicionar
     * @throws IllegalArgumentException se o resultado ficar negativo
     * @throws IllegalStateException se o tipo for FLAG
     */
    public void increment(int delta) {
        if (type == MarkerType.FLAG)
            throw new IllegalStateException("increment nao e valido para tipo FLAG");
        int newValue = (Integer) this.value + delta;
        if (newValue < 0)
            throw new IllegalArgumentException("valor numerico do marcador nao pode ser negativo");
        this.value = newValue;
    }

    /**
     * Decrementa um marcador numerico ({@link MarkerType#STAGE} ou {@link MarkerType#COUNTER}).
     *
     * @param amount valor nao negativo a subtrair
     * @throws IllegalArgumentException se amount for negativo ou o resultado ficar negativo
     * @throws IllegalStateException se o tipo for FLAG
     */
    public void decrement(int amount) {
        if (amount < 0)
            throw new IllegalArgumentException("decremento do marcador nao pode ser negativo");
        increment(-amount);
    }

    /**
     * Valida se o marcador atende a uma pre-condicao simples de dialogo.
     *
     * @param expectedValue valor esperado para o marcador
     * @return {@code true} quando o valor atual equivale ao valor esperado
     */
    public boolean matchesExpectedValue(Object expectedValue) {
        return value.equals(expectedValue);
    }

    private static void validateValue(MarkerType type, Object value) {
        if (value == null)
            throw new IllegalArgumentException("valor do marcador nao pode ser nulo");
        switch (type) {
            case FLAG:
                if (!(value instanceof Boolean))
                    throw new IllegalArgumentException("FLAG requer valor Boolean");
                break;
            case STAGE:
            case COUNTER:
                if (!(value instanceof Integer))
                    throw new IllegalArgumentException("STAGE/COUNTER requerem valor Integer");
                if ((Integer) value < 0)
                    throw new IllegalArgumentException("STAGE/COUNTER requerem valor nao negativo");
                break;
        }
    }
}
