package br.eng.rodrigogml.mysteryrealms.domain.physiology.model;

/**
 * Estado fisiológico dinâmico do personagem durante uma sessão de jogo.
 * Utilizado pelo {@link br.eng.rodrigogml.mysteryrealms.domain.physiology.service.ServicoFisiologico}
 * para calcular ticks e transições de estado — RF-EF-*.
 *
 * Os campos são mutáveis para refletir a natureza dinâmica do estado durante o jogo.
 */
public class PhysiologicalState {

    private double fadigaMin;
    private double fadigaAtual;
    private double fadigaMax;
    private double sedePct;
    private double fomePct;
    private int moral;
    private double pontosVida;
    private double pontosVidaMax;

    public PhysiologicalState(
            double fadigaMin,
            double fadigaAtual,
            double fadigaMax,
            double sedePct,
            double fomePct,
            int moral,
            double pontosVida,
            double pontosVidaMax) {
        this.fadigaMin = fadigaMin;
        this.fadigaAtual = fadigaAtual;
        this.fadigaMax = fadigaMax;
        this.sedePct = sedePct;
        this.fomePct = fomePct;
        this.moral = moral;
        this.pontosVida = pontosVida;
        this.pontosVidaMax = pontosVidaMax;
    }

    /** Cria um estado inicial "zerado" a partir dos atributos base. */
    public static PhysiologicalState initial(double fadigaMax, double pontosVidaMax) {
        return new PhysiologicalState(0.0, 0.0, fadigaMax, 0.0, 0.0, 75, pontosVidaMax, pontosVidaMax);
    }

    public double getFadigaMin() { return fadigaMin; }
    public void setFadigaMin(double fadigaMin) { this.fadigaMin = fadigaMin; }

    public double getFadigaAtual() { return fadigaAtual; }
    public void setFadigaAtual(double fadigaAtual) { this.fadigaAtual = fadigaAtual; }

    public double getFadigaMax() { return fadigaMax; }
    public void setFadigaMax(double fadigaMax) { this.fadigaMax = fadigaMax; }

    public double getSedePct() { return sedePct; }
    public void setSedePct(double sedePct) { this.sedePct = Math.min(100.0, Math.max(0.0, sedePct)); }

    public double getFomePct() { return fomePct; }
    public void setFomePct(double fomePct) { this.fomePct = Math.min(100.0, Math.max(0.0, fomePct)); }

    public int getMoral() { return moral; }
    public void setMoral(int moral) { this.moral = Math.min(100, Math.max(0, moral)); }

    public double getPontosVida() { return pontosVida; }
    public void setPontosVida(double pontosVida) { this.pontosVida = pontosVida; }

    public double getPontosVidaMax() { return pontosVidaMax; }
    public void setPontosVidaMax(double pontosVidaMax) { this.pontosVidaMax = pontosVidaMax; }
}
