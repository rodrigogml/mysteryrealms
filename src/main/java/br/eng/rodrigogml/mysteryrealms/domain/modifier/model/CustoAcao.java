package br.eng.rodrigogml.mysteryrealms.domain.modifier.model;

/**
 * Custo de uma ação em tempo, fadiga, fome e sede — RF-MAR-08.
 *
 * Todos os deltas podem ser negativos (ex.: ação recuperativa reduz fadiga).
 * Os campos são mutáveis para permitir a aplicação incremental de modificadores.
 */
public class CustoAcao {

    /** Tempo gasto em minutos de jogo. */
    private int tempoGastoMin;

    /** Delta de fadiga_atual. Negativo = recuperação. */
    private double custoFadiga;

    /** Delta de fome_pct. Negativo = recuperação. */
    private double deltaFomePct;

    /** Delta de sede_pct. Negativo = recuperação. */
    private double deltaSedePct;

    public CustoAcao(int tempoGastoMin, double custoFadiga, double deltaFomePct, double deltaSedePct) {
        this.tempoGastoMin = tempoGastoMin;
        this.custoFadiga = custoFadiga;
        this.deltaFomePct = deltaFomePct;
        this.deltaSedePct = deltaSedePct;
    }

    /** Cria uma cópia independente deste custo. */
    public CustoAcao copy() {
        return new CustoAcao(tempoGastoMin, custoFadiga, deltaFomePct, deltaSedePct);
    }

    public int getTempoGastoMin() { return tempoGastoMin; }
    public void setTempoGastoMin(int tempoGastoMin) { this.tempoGastoMin = tempoGastoMin; }

    public double getCustoFadiga() { return custoFadiga; }
    public void setCustoFadiga(double custoFadiga) { this.custoFadiga = custoFadiga; }

    public double getDeltaFomePct() { return deltaFomePct; }
    public void setDeltaFomePct(double deltaFomePct) { this.deltaFomePct = deltaFomePct; }

    public double getDeltaSedePct() { return deltaSedePct; }
    public void setDeltaSedePct(double deltaSedePct) { this.deltaSedePct = deltaSedePct; }
}
