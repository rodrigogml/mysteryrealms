package br.eng.rodrigogml.mysteryrealms.domain.social.model;

/**
 * Marcador de progresso social — RF-SS-09.
 *
 * Convenção de ID: {@code mk_<dominio>_<descricaoCamelCase>}.
 */
public class Marker {

    private final String id;
    private final br.eng.rodrigogml.mysteryrealms.domain.social.enums.MarkerType tipo;
    /** Boolean para FLAG; Integer para STAGE e COUNTER. */
    private Object valor;

    public Marker(String id, br.eng.rodrigogml.mysteryrealms.domain.social.enums.MarkerType tipo, Object valorInicial) {
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

    /** Cria um marcador FLAG ativo (true). */
    public static Marker flag(String id) {
        return new Marker(id, br.eng.rodrigogml.mysteryrealms.domain.social.enums.MarkerType.FLAG, Boolean.TRUE);
    }

    /** Cria um marcador FLAG inativo (false). */
    public static Marker flagInativo(String id) {
        return new Marker(id, br.eng.rodrigogml.mysteryrealms.domain.social.enums.MarkerType.FLAG, Boolean.FALSE);
    }

    /** Cria um marcador STAGE com valor inicial 0. */
    public static Marker stage(String id) {
        return new Marker(id, br.eng.rodrigogml.mysteryrealms.domain.social.enums.MarkerType.STAGE, 0);
    }

    /** Cria um marcador COUNTER com valor inicial 0. */
    public static Marker counter(String id) {
        return new Marker(id, br.eng.rodrigogml.mysteryrealms.domain.social.enums.MarkerType.COUNTER, 0);
    }

    public String getId() { return id; }
    public br.eng.rodrigogml.mysteryrealms.domain.social.enums.MarkerType getTipo() { return tipo; }
    public Object getValor() { return valor; }

    public boolean isFlagAtivo() {
        if (tipo != br.eng.rodrigogml.mysteryrealms.domain.social.enums.MarkerType.FLAG)
            throw new IllegalStateException("Marcador não é do tipo FLAG");
        return Boolean.TRUE.equals(valor);
    }

    public int getValorInteiro() {
        if (tipo == br.eng.rodrigogml.mysteryrealms.domain.social.enums.MarkerType.FLAG)
            throw new IllegalStateException("Marcador FLAG não tem valor inteiro");
        return (Integer) valor;
    }

    /** Ativa/desativa um marcador FLAG. */
    public void setFlag(boolean ativo) {
        if (tipo != br.eng.rodrigogml.mysteryrealms.domain.social.enums.MarkerType.FLAG)
            throw new IllegalStateException("setFlag só é válido para tipo FLAG");
        this.valor = ativo;
    }

    /** Define o estágio de um marcador STAGE. */
    public void setStage(int estagio) {
        if (tipo != br.eng.rodrigogml.mysteryrealms.domain.social.enums.MarkerType.STAGE)
            throw new IllegalStateException("setStage só é válido para tipo STAGE");
        this.valor = estagio;
    }

    /** Incrementa um marcador numérico (STAGE ou COUNTER). */
    public void increment(int delta) {
        if (tipo == br.eng.rodrigogml.mysteryrealms.domain.social.enums.MarkerType.FLAG)
            throw new IllegalStateException("increment não é válido para tipo FLAG");
        this.valor = (Integer) this.valor + delta;
    }

    private static void validarValor(
            br.eng.rodrigogml.mysteryrealms.domain.social.enums.MarkerType tipo, Object valor) {
        if (valor == null) throw new IllegalArgumentException("valor do marcador não pode ser nulo");
        switch (tipo) {
            case FLAG:
                if (!(valor instanceof Boolean))
                    throw new IllegalArgumentException("FLAG requer valor Boolean");
                break;
            case STAGE:
            case COUNTER:
                if (!(valor instanceof Integer))
                    throw new IllegalArgumentException("STAGE/COUNTER requerem valor Integer");
                break;
        }
    }
}
