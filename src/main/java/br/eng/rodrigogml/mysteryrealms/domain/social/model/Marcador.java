package br.eng.rodrigogml.mysteryrealms.domain.social.model;

/**
 * Marcador de progresso social — RF-SS-09.
 *
 * Convenção de ID: {@code mk_<dominio>_<descricaoCamelCase>}.
 */
public class Marcador {

    private final String id;
    private final br.eng.rodrigogml.mysteryrealms.domain.social.enums.TipoMarcador tipo;
    /** Boolean para SINALIZADOR; Integer para ESTAGIO e CONTADOR. */
    private Object valor;

    public Marcador(String id, br.eng.rodrigogml.mysteryrealms.domain.social.enums.TipoMarcador tipo, Object valorInicial) {
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

    /** Cria um marcador SINALIZADOR ativo (true). */
    public static Marcador sinalizador(String id) {
        return new Marcador(id, br.eng.rodrigogml.mysteryrealms.domain.social.enums.TipoMarcador.SINALIZADOR, Boolean.TRUE);
    }

    /** Cria um marcador SINALIZADOR inativo (false). */
    public static Marcador sinalizadorInativo(String id) {
        return new Marcador(id, br.eng.rodrigogml.mysteryrealms.domain.social.enums.TipoMarcador.SINALIZADOR, Boolean.FALSE);
    }

    /** Cria um marcador ESTAGIO com valor inicial 0. */
    public static Marcador estagio(String id) {
        return new Marcador(id, br.eng.rodrigogml.mysteryrealms.domain.social.enums.TipoMarcador.ESTAGIO, 0);
    }

    /** Cria um marcador CONTADOR com valor inicial 0. */
    public static Marcador contador(String id) {
        return new Marcador(id, br.eng.rodrigogml.mysteryrealms.domain.social.enums.TipoMarcador.CONTADOR, 0);
    }

    public String getId() { return id; }
    public br.eng.rodrigogml.mysteryrealms.domain.social.enums.TipoMarcador getTipo() { return tipo; }
    public Object getValor() { return valor; }

    public boolean sinalizadorAtivo() {
        if (tipo != br.eng.rodrigogml.mysteryrealms.domain.social.enums.TipoMarcador.SINALIZADOR)
            throw new IllegalStateException("Marcador não é do tipo SINALIZADOR");
        return Boolean.TRUE.equals(valor);
    }

    public int valorInteiro() {
        if (tipo == br.eng.rodrigogml.mysteryrealms.domain.social.enums.TipoMarcador.SINALIZADOR)
            throw new IllegalStateException("Marcador SINALIZADOR não tem valor inteiro");
        return (Integer) valor;
    }

    /** Ativa/desativa um marcador SINALIZADOR. */
    public void definirSinalizador(boolean ativo) {
        if (tipo != br.eng.rodrigogml.mysteryrealms.domain.social.enums.TipoMarcador.SINALIZADOR)
            throw new IllegalStateException("definirSinalizador só é válido para tipo SINALIZADOR");
        this.valor = ativo;
    }

    /** Define o estágio de um marcador ESTAGIO. */
    public void definirEstagio(int estagio) {
        if (tipo != br.eng.rodrigogml.mysteryrealms.domain.social.enums.TipoMarcador.ESTAGIO)
            throw new IllegalStateException("definirEstagio só é válido para tipo ESTAGIO");
        this.valor = estagio;
    }

    /** Incrementa um marcador numérico (ESTAGIO ou CONTADOR). */
    public void increment(int delta) {
        if (tipo == br.eng.rodrigogml.mysteryrealms.domain.social.enums.TipoMarcador.SINALIZADOR)
            throw new IllegalStateException("increment não é válido para tipo SINALIZADOR");
        this.valor = (Integer) this.valor + delta;
    }

    private static void validarValor(
            br.eng.rodrigogml.mysteryrealms.domain.social.enums.TipoMarcador tipo, Object valor) {
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
