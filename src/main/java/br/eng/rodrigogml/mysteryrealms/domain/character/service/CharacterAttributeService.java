package br.eng.rodrigogml.mysteryrealms.domain.character.service;

/**
 * Cálculo dos attributes derivados do personagem — RF-FP-06.
 *
 * Todos os métodos recebem os valores de atributo como parâmetros primitivos para
 * manter o serviço stateless e facilitar os testes.
 */
public final class CharacterAttributeService {

    /** Limite máximo de resistência para personagens jogadores — RF-CT-10. */
    public static final double MAX_PLAYER_RESISTANCE = 0.80;

    private CharacterAttributeService() {}

    // ── RF-FP-06.1 ────────────────────────────────────────────────────────────

    /**
     * Pontos de Vida Máximo: constitution × 10.
     */
    public static double maxHealthPoints(double constitution) {
        return constitution * 10.0;
    }

    // ── RF-FP-06.2 ────────────────────────────────────────────────────────────

    /**
     * Fadiga Máxima: (constitution + willpower) × 100.
     */
    public static double maxFatigue(double constitution, double willpower) {
        return (constitution + willpower) * 100.0;
    }

    // ── RF-FP-06.3 ────────────────────────────────────────────────────────────

    /**
     * Peso do personagem em kg.
     * peso_kg = pesoBaseRacaGenero × (1 + ((constitution - 3) × 0,05))
     */
    public static double characterWeight(double pesoBaseRacaGenero, double constitution) {
        return pesoBaseRacaGenero * (1.0 + (constitution - 3.0) * 0.05);
    }

    // ── RF-FP-06.4 ────────────────────────────────────────────────────────────

    /**
     * Carga Máxima sem montaria: strength × 10 (kg).
     */
    public static double maxLoadCapacity(double strength) {
        return strength * 10.0;
    }

    /**
     * Carga Crítica: capacidadeCargas × 1,5.
     */
    public static double criticalLoadCapacity(double maxLoadCapacity) {
        return maxLoadCapacity * 1.5;
    }

    /**
     * Peso das moedas em kg.
     * peso_moedas_kg = ((primaryCurrencyQty × 6) + (secondaryCurrencyQty × 5)) / 1000
     */
    public static double currencyWeight(long primaryCurrencyQty, long secondaryCurrencyQty) {
        return ((primaryCurrencyQty * 6L) + (secondaryCurrencyQty * 5L)) / 1000.0;
    }

    /**
     * Carga atual: pesoEquipadosKg + pesomBolsaKg + pesomMoedasKg.
     */
    public static double currentLoad(double pesoEquipadosKg, double pesoBolsaKg, double pesoMoedasKg) {
        return pesoEquipadosKg + pesoBolsaKg + pesoMoedasKg;
    }

    // ── RF-FP-06.5 ────────────────────────────────────────────────────────────

    /**
     * Bônus estático de precisão (a parte sem o dado):
     * precisao = dexterity + bonusItemPrecisao + modificadores.
     * O dado (1d20) é adicionado pelo motor de combate em tempo de execução.
     */
    public static int precisionBonus(int dexterity, int bonusItemPrecisao, int modificadores) {
        return dexterity + bonusItemPrecisao + modificadores;
    }

    // ── RF-FP-06.6 ────────────────────────────────────────────────────────────

    /**
     * Dano final — RF-FP-06.6.
     * Ordem obrigatória:
     *   1. base = dadoArma + modAtributo + bonusRacaClasse + bonusItemDano + bonusPlano
     *   2. × (1 + somaPctPositivo - somaPctNegativo)
     *   3. floor
     *   4. + somaFlatPositivo - somaFlatNegativo
     *   5. max(0, resultado)
     */
    public static int calculateFinalDamage(
            int dadoArma,
            int modAtributo,
            int bonusRacaClasse,
            int bonusItemDano,
            int bonusPlano,
            double somaPctPositivo,
            double somaPctNegativo,
            int somaFlatPositivo,
            int somaFlatNegativo) {

        return calculateCompositeFinalValue(
                dadoArma + modAtributo + bonusRacaClasse + bonusItemDano + bonusPlano,
                somaPctPositivo,
                somaPctNegativo,
                somaFlatPositivo,
                somaFlatNegativo);
    }

    // ── RF-FP-06.7 ────────────────────────────────────────────────────────────

    /**
     * Defesa final — RF-FP-06.7.
     * Mesma ordem de aplicação de RF-FP-06.6.
     */
    public static int calculateFinalDefense(
            int baseEsquiva,
            int bonusArmadura,
            int bonusEscudo,
            int bonusItemDefesa,
            int bonusPlano,
            double somaPctPositivo,
            double somaPctNegativo,
            int somaFlatPositivo,
            int somaFlatNegativo) {

        return calculateCompositeFinalValue(
                baseEsquiva + bonusArmadura + bonusEscudo + bonusItemDefesa + bonusPlano,
                somaPctPositivo,
                somaPctNegativo,
                somaFlatPositivo,
                somaFlatNegativo);
    }

    // ── RF-FP-06.8 ────────────────────────────────────────────────────────────

    /**
     * Bloqueio final — RF-FP-06.8.
     * Mesma ordem de aplicação de RF-FP-06.6.
     */
    public static int calculateFinalBlock(
            int baseBloqueio,
            int bonusEquipBloqueio,
            int bonusItemBloqueio,
            int bonusPlano,
            double somaPctPositivo,
            double somaPctNegativo,
            int somaFlatPositivo,
            int somaFlatNegativo) {

        return calculateCompositeFinalValue(
                baseBloqueio + bonusEquipBloqueio + bonusItemBloqueio + bonusPlano,
                somaPctPositivo,
                somaPctNegativo,
                somaFlatPositivo,
                somaFlatNegativo);
    }

    private static int calculateCompositeFinalValue(
            int base,
            double somaPctPositivo,
            double somaPctNegativo,
            int somaFlatPositivo,
            int somaFlatNegativo) {
        double withPct = base * (1.0 + somaPctPositivo - somaPctNegativo);
        int withFloor = (int) Math.floor(withPct);
        int withFlat = withFloor + somaFlatPositivo - somaFlatNegativo;
        return Math.max(0, withFlat);
    }
}
