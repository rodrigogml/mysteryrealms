package br.eng.rodrigogml.mysteryrealms.domain.social.model;

import br.eng.rodrigogml.mysteryrealms.domain.social.enums.TipoMarcador;

/**
 * Marcador de progresso social — RF-SS-09.
 *
 * Convenção de ID: {@code mk_<dominio>_<descricaoCamelCase>}.
 */
public class Marcador {

    private final String id;
    private final TipoMarcador tipo;
    /** Boolean para SINALIZADOR; Integer para ESTAGIO e CONTADOR. */
    private Object valor;

    /**
     * Cria um marcador.
     *
     * @param id           identificador único com prefixo {@code mk_}
     * @param tipo         tipo do marcador
     * @param valorInicial valor inicial compatível com o tipo
     * @throws IllegalArgumentException se id, tipo ou valor forem inválidos
     */
    public Marcador(String id, TipoMarcador tipo, Object valorInicial) {
        if (id == null || id.isBlank())
            throw new IllegalArgumentException("id do marcador não pode ser vazio");
        if (!id.startsWith("mk_"))
            throw new IllegalArgumentException("id do marcador deve começar com 'mk_': " + id);
        if (tipo == null)
            throw new IllegalArgumentException("tipo do marcador não pode ser nulo");
        validarValor(tipo, valorInicial);

        this.id = id;
        this.tipo = tipo;
        this.valor = valorInicial;
    }

    /**
     * Cria um marcador {@link TipoMarcador#SINALIZADOR} ativo (true).
     *
     * @param id identificador único com prefixo {@code mk_}
     * @return novo marcador ativo
     */
    public static Marcador sinalizador(String id) {
        return new Marcador(id, TipoMarcador.SINALIZADOR, Boolean.TRUE);
    }

    /**
     * Cria um marcador {@link TipoMarcador#SINALIZADOR} inativo (false).
     *
     * @param id identificador único com prefixo {@code mk_}
     * @return novo marcador inativo
     */
    public static Marcador sinalizadorInativo(String id) {
        return new Marcador(id, TipoMarcador.SINALIZADOR, Boolean.FALSE);
    }

    /**
     * Cria um marcador {@link TipoMarcador#ESTAGIO} com valor inicial 0.
     *
     * @param id identificador único com prefixo {@code mk_}
     * @return novo marcador de estágio
     */
    public static Marcador estagio(String id) {
        return new Marcador(id, TipoMarcador.ESTAGIO, 0);
    }

    /**
     * Cria um marcador {@link TipoMarcador#CONTADOR} com valor inicial 0.
     *
     * @param id identificador único com prefixo {@code mk_}
     * @return novo marcador contador
     */
    public static Marcador contador(String id) {
        return new Marcador(id, TipoMarcador.CONTADOR, 0);
    }

    public String getId() { return id; }
    public TipoMarcador getTipo() { return tipo; }
    public Object getValor() { return valor; }

    /**
     * Retorna {@code true} se este marcador {@link TipoMarcador#SINALIZADOR} estiver ativo.
     *
     * @return estado do sinalizador
     * @throws IllegalStateException se o tipo não for SINALIZADOR
     */
    public boolean sinalizadorAtivo() {
        if (tipo != TipoMarcador.SINALIZADOR)
            throw new IllegalStateException("Marcador não é do tipo SINALIZADOR");
        return Boolean.TRUE.equals(valor);
    }

    /**
     * Retorna o valor inteiro de marcadores {@link TipoMarcador#ESTAGIO} ou {@link TipoMarcador#CONTADOR}.
     *
     * @return valor inteiro atual
     * @throws IllegalStateException se o tipo for SINALIZADOR
     */
    public int valorInteiro() {
        if (tipo == TipoMarcador.SINALIZADOR)
            throw new IllegalStateException("Marcador SINALIZADOR não tem valor inteiro");
        return (Integer) valor;
    }

    /**
     * Ativa/desativa um marcador {@link TipoMarcador#SINALIZADOR}.
     *
     * @param ativo novo estado
     * @throws IllegalStateException se o tipo não for SINALIZADOR
     */
    public void definirSinalizador(boolean ativo) {
        if (tipo != TipoMarcador.SINALIZADOR)
            throw new IllegalStateException("definirSinalizador só é válido para tipo SINALIZADOR");
        this.valor = ativo;
    }

    /**
     * Define o estágio de um marcador {@link TipoMarcador#ESTAGIO}.
     *
     * @param estagio novo valor de estágio
     * @throws IllegalStateException se o tipo não for ESTAGIO
     */
    public void definirEstagio(int estagio) {
        if (tipo != TipoMarcador.ESTAGIO)
            throw new IllegalStateException("definirEstagio só é válido para tipo ESTAGIO");
        this.valor = estagio;
    }

    /**
     * Incrementa um marcador numérico ({@link TipoMarcador#ESTAGIO} ou {@link TipoMarcador#CONTADOR}).
     *
     * @param delta valor a adicionar (pode ser negativo)
     * @throws IllegalStateException se o tipo for SINALIZADOR
     */
    public void incrementar(int delta) {
        if (tipo == TipoMarcador.SINALIZADOR)
            throw new IllegalStateException("incrementar não é válido para tipo SINALIZADOR");
        this.valor = (Integer) this.valor + delta;
    }

    private static void validarValor(TipoMarcador tipo, Object valor) {
        if (valor == null) throw new IllegalArgumentException("valor do marcador não pode ser nulo");
        switch (tipo) {
            case SINALIZADOR:
                if (!(valor instanceof Boolean))
                    throw new IllegalArgumentException("SINALIZADOR requer valor Boolean");
                break;
            case ESTAGIO:
            case CONTADOR:
                if (!(valor instanceof Integer))
                    throw new IllegalArgumentException("ESTAGIO/CONTADOR requerem valor Integer");
                break;
        }
    }
}
