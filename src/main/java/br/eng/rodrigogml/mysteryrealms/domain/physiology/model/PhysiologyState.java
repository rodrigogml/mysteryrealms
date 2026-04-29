package br.eng.rodrigogml.mysteryrealms.domain.physiology.model;

/**
 * Estado fisiológico dinâmico do personagem durante uma sessão de jogo.
 * Utilizado pelo {@link br.eng.rodrigogml.mysteryrealms.domain.physiology.service.PhysiologyService}
 * para calcular ticks e transições de estado — RF-EF-*.
 *
 * Os campos são mutáveis para refletir a natureza dinâmica do estado durante o jogo.
 */
public class PhysiologyState {

    private double minFatigue;
    private double currentFatigue;
    private double maxFatigue;
    private double thirstPct;
    private double hungerPct;
    private int morale;
    private double healthPoints;
    private double maxHealthPoints;
    private boolean severeHungerThirstCombinationActive;

    public PhysiologyState(
            double minFatigue,
            double currentFatigue,
            double maxFatigue,
            double thirstPct,
            double hungerPct,
            int morale,
            double healthPoints,
            double maxHealthPoints,
            boolean severeHungerThirstCombinationActive) {
        this.minFatigue = minFatigue;
        this.currentFatigue = currentFatigue;
        this.maxFatigue = maxFatigue;
        this.thirstPct = thirstPct;
        this.hungerPct = hungerPct;
        this.morale = morale;
        this.healthPoints = healthPoints;
        this.maxHealthPoints = maxHealthPoints;
        this.severeHungerThirstCombinationActive = severeHungerThirstCombinationActive;
    }

    /** Cria um estado initial "zerado" a partir dos attributes base. */
    public static PhysiologyState initial(double maxFatigue, double maxHealthPoints) {
        return new PhysiologyState(0.0, 0.0, maxFatigue, 0.0, 0.0, 75, maxHealthPoints, maxHealthPoints, false);
    }

    public double getMinFatigue() { return minFatigue; }
    public void setMinFatigue(double minFatigue) { this.minFatigue = minFatigue; }

    public double getCurrentFatigue() { return currentFatigue; }
    public void setCurrentFatigue(double currentFatigue) { this.currentFatigue = currentFatigue; }

    public double getMaxFatigue() { return maxFatigue; }
    public void setMaxFatigue(double maxFatigue) { this.maxFatigue = maxFatigue; }

    public double getThirstPct() { return thirstPct; }
    public void setThirstPct(double thirstPct) { this.thirstPct = Math.min(100.0, Math.max(0.0, thirstPct)); }

    public double getHungerPct() { return hungerPct; }
    public void setHungerPct(double hungerPct) { this.hungerPct = Math.min(100.0, Math.max(0.0, hungerPct)); }

    public int getMorale() { return morale; }
    public void setMorale(int morale) { this.morale = Math.min(100, Math.max(0, morale)); }

    public double getHealthPoints() { return healthPoints; }
    public void setHealthPoints(double healthPoints) { this.healthPoints = healthPoints; }

    public double getMaxHealthPoints() { return maxHealthPoints; }
    public void setMaxHealthPoints(double maxHealthPoints) { this.maxHealthPoints = maxHealthPoints; }

    public boolean isSevereHungerThirstCombinationActive() { return severeHungerThirstCombinationActive; }
    public void setSevereHungerThirstCombinationActive(boolean severeHungerThirstCombinationActive) {
        this.severeHungerThirstCombinationActive = severeHungerThirstCombinationActive;
    }
}
