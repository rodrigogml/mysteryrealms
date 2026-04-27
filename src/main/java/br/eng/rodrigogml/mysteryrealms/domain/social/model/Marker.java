package br.eng.rodrigogml.mysteryrealms.domain.social.model;

import br.eng.rodrigogml.mysteryrealms.domain.social.enums.MarkerType;

/**
 * Marker de progresso social — RF-SS-09.
 *
 * Convenção de ID: {@code mk_<dominio>_<descricaoCamelCase>}.
 */
public class Marker {

    private final String id;
    private final MarkerType type;
    /** Boolean para FLAG; Integer para STAGE e COUNTER. */
    private Object value;

    /**
     * Cria um marcador.
     *
     * @param id           identificador único com prefixo {@code mk_}
     * @param type         type do marcador
     * @param valorInicial value initial compatível com o type
     * @throws IllegalArgumentException se id, type ou value forem inválidos
     */
    public Marker(String id, MarkerType type, Object valorInicial) {
        if (id == null || id.isBlank())
            throw new IllegalArgumentException("id do marcador não pode ser vazio");
        if (!id.startsWith("mk_"))
            throw new IllegalArgumentException("id do marcador deve começar com 'mk_': " + id);
        if (type == null)
            throw new IllegalArgumentException("tipo do marcador não pode ser nulo");
        validarValor(type, valorInicial);

        this.id = id;
        this.type = type;
        this.value = valorInicial;
    }

    /**
     * Cria um marcador {@link MarkerType#FLAG} ativo (true).
     *
     * @param id identificador único com prefixo {@code mk_}
     * @return novo marcador ativo
     */
    public static Marker flag(String id) {
        return new Marker(id, MarkerType.FLAG, Boolean.TRUE);
    }

    /**
     * Cria um marcador {@link MarkerType#FLAG} inativo (false).
     *
     * @param id identificador único com prefixo {@code mk_}
     * @return novo marcador inativo
     */
    public static Marker inactiveFlag(String id) {
        return new Marker(id, MarkerType.FLAG, Boolean.FALSE);
    }

    /**
     * Cria um marcador {@link MarkerType#STAGE} com value initial 0.
     *
     * @param id identificador único com prefixo {@code mk_}
     * @return novo marcador de estágio
     */
    public static Marker stage(String id) {
        return new Marker(id, MarkerType.STAGE, 0);
    }

    /**
     * Cria um marcador {@link MarkerType#COUNTER} com value initial 0.
     *
     * @param id identificador único com prefixo {@code mk_}
     * @return novo marcador counter
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
     * @throws IllegalStateException se o type não for FLAG
     */
    public boolean isFlagActive() {
        if (type != MarkerType.FLAG)
            throw new IllegalStateException("Marcador não é do tipo SINALIZADOR");
        return Boolean.TRUE.equals(value);
    }

    /**
     * Retorna o value inteiro de marcadores {@link MarkerType#STAGE} ou {@link MarkerType#COUNTER}.
     *
     * @return value inteiro atual
     * @throws IllegalStateException se o type for FLAG
     */
    public int intValue() {
        if (type == MarkerType.FLAG)
            throw new IllegalStateException("Marcador SINALIZADOR não tem valor inteiro");
        return (Integer) value;
    }

    /**
     * Ativa/desativa um marcador {@link MarkerType#FLAG}.
     *
     * @param ativo novo estado
     * @throws IllegalStateException se o type não for FLAG
     */
    public void setFlag(boolean ativo) {
        if (type != MarkerType.FLAG)
            throw new IllegalStateException("definirSinalizador só é válido para tipo SINALIZADOR");
        this.value = ativo;
    }

    /**
     * Define o estágio de um marcador {@link MarkerType#STAGE}.
     *
     * @param stage novo value de estágio
     * @throws IllegalStateException se o type não for STAGE
     */
    public void setStage(int stage) {
        if (type != MarkerType.STAGE)
            throw new IllegalStateException("definirEstagio só é válido para tipo ESTAGIO");
        this.value = stage;
    }

    /**
     * Incrementa um marcador numérico ({@link MarkerType#STAGE} ou {@link MarkerType#COUNTER}).
     *
     * @param delta value a adicionar (pode ser negativo)
     * @throws IllegalStateException se o type for FLAG
     */
    public void increment(int delta) {
        if (type == MarkerType.FLAG)
            throw new IllegalStateException("incrementar não é válido para tipo SINALIZADOR");
        this.value = (Integer) this.value + delta;
    }

    private static void validarValor(MarkerType type, Object value) {
        if (value == null) throw new IllegalArgumentException("valor do marcador não pode ser nulo");
        switch (type) {
            case FLAG:
                if (!(value instanceof Boolean))
                    throw new IllegalArgumentException("SINALIZADOR requer valor Boolean");
                break;
            case STAGE:
            case COUNTER:
                if (!(value instanceof Integer))
                    throw new IllegalArgumentException("ESTAGIO/CONTADOR requerem valor Integer");
                break;
        }
    }
}
