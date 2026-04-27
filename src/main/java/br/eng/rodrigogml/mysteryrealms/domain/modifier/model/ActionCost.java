package br.eng.rodrigogml.mysteryrealms.domain.modifier.model;

/**
 * Custo de uma ação em tempo, fadiga, fome e sede — RF-MAR-08.
 *
 * Todos os deltas podem ser negativos (ex.: ação recuperativa reduz fadiga).
 * Os campos são mutáveis para permitir a aplicação incremental de modificadores.
 */
public class ActionCost {

    /** Tempo gasto em minutes de jogo. */
    private int timeSpentMin;

    /** Delta de fadiga_atual. Negativo = recuperação. */
    private double fatigueCost;

    /** Delta de fome_pct. Negativo = recuperação. */
    private double hungerDeltaPct;

    /** Delta de sede_pct. Negativo = recuperação. */
    private double thirstDeltaPct;

    public ActionCost(int timeSpentMin, double fatigueCost, double hungerDeltaPct, double thirstDeltaPct) {
        this.timeSpentMin = timeSpentMin;
        this.fatigueCost = fatigueCost;
        this.hungerDeltaPct = hungerDeltaPct;
        this.thirstDeltaPct = thirstDeltaPct;
    }

    /** Cria uma cópia independente deste custo. */
    public ActionCost copy() {
        return new ActionCost(timeSpentMin, fatigueCost, hungerDeltaPct, thirstDeltaPct);
    }

    public int getTimeSpentMin() { return timeSpentMin; }
    public void setTimeSpentMin(int timeSpentMin) { this.timeSpentMin = timeSpentMin; }

    public double getFatigueCost() { return fatigueCost; }
    public void setFatigueCost(double fatigueCost) { this.fatigueCost = fatigueCost; }

    public double getHungerDeltaPct() { return hungerDeltaPct; }
    public void setHungerDeltaPct(double hungerDeltaPct) { this.hungerDeltaPct = hungerDeltaPct; }

    public double getThirstDeltaPct() { return thirstDeltaPct; }
    public void setThirstDeltaPct(double thirstDeltaPct) { this.thirstDeltaPct = thirstDeltaPct; }
}
