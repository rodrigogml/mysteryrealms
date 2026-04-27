package br.eng.rodrigogml.mysteryrealms.domain.character.service;

/**
 * Cálculo dos atributos derivados do personagem — RF-FP-06.
 *
 * Todos os métodos recebem os valores de atributo como parâmetros primitivos para
 * manter o serviço stateless e facilitar os testes.
 */
public final class ServicoAtributosPersonagem {

    /** Limite máximo de resistência para personagens jogadores — RF-CT-10. */
    public static final double RESISTENCIA_MAXIMA_JOGADOR = 0.80;

    private ServicoAtributosPersonagem() {}

    // ── RF-FP-06.1 ────────────────────────────────────────────────────────────

    /**
     * Pontos de Vida Máximo: constituicao × 10.
     */
    public static double pvMaximo(double constituicao) {
        return constituicao * 10.0;
    }

    // ── RF-FP-06.2 ────────────────────────────────────────────────────────────

    /**
     * Fadiga Máxima: (constituicao + vontade) × 100.
     */
    public static double fadigaMaxima(double constituicao, double vontade) {
        return (constituicao + vontade) * 100.0;
    }

    // ── RF-FP-06.3 ────────────────────────────────────────────────────────────

    /**
     * Peso do personagem em kg.
     * peso_kg = pesoBaseRacaGenero × (1 + ((constituicao - 3) × 0,05))
     */
    public static double pesoPersonagem(double pesoBaseRacaGenero, double constituicao) {
        return pesoBaseRacaGenero * (1.0 + (constituicao - 3.0) * 0.05);
    }

    // ── RF-FP-06.4 ────────────────────────────────────────────────────────────

    /**
     * Carga Máxima sem montaria: forca × 10 (kg).
     */
    public static double capacidadeMaximaCarga(double forca) {
        return forca * 10.0;
    }

    /**
     * Carga Crítica: capacidadeCargas × 1,5.
     */
    public static double capacidadeCriticaCarga(double capacidadeMaximaCarga) {
        return capacidadeMaximaCarga * 1.5;
    }

    /**
     * Peso das moedas em kg.
     * peso_moedas_kg = ((qtdMoedaPrimaria × 6) + (qtdMoedaSecundaria × 5)) / 1000
     */
    public static double pesoMoedas(long qtdMoedaPrimaria, long qtdMoedaSecundaria) {
        return ((qtdMoedaPrimaria * 6L) + (qtdMoedaSecundaria * 5L)) / 1000.0;
    }

    /**
     * Carga atual: pesoEquipadosKg + pesomBolsaKg + pesomMoedasKg.
     */
    public static double cargaAtual(double pesoEquipadosKg, double pesoBolsaKg, double pesoMoedasKg) {
        return pesoEquipadosKg + pesoBolsaKg + pesoMoedasKg;
    }

    // ── RF-FP-06.5 ────────────────────────────────────────────────────────────

    /**
     * Bônus estático de precisão (a parte sem o dado):
     * precisao = destreza + bonusItemPrecisao + modificadores.
     * O dado (1d20) é adicionado pelo motor de combate em tempo de execução.
     */
    public static int bonusPrecisao(int destreza, int bonusItemPrecisao, int modificadores) {
        return destreza + bonusItemPrecisao + modificadores;
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
    public static int calcularDanoFinal(
            int dadoArma,
            int modAtributo,
            int bonusRacaClasse,
            int bonusItemDano,
            int bonusPlano,
            double somaPctPositivo,
            double somaPctNegativo,
            int somaFlatPositivo,
            int somaFlatNegativo) {

        double base = dadoArma + modAtributo + bonusRacaClasse + bonusItemDano + bonusPlano;
        double withPct = base * (1.0 + somaPctPositivo - somaPctNegativo);
        int withFloor = (int) Math.floor(withPct);
        int withFlat = withFloor + somaFlatPositivo - somaFlatNegativo;
        return Math.max(0, withFlat);
    }

    // ── RF-FP-06.7 ────────────────────────────────────────────────────────────

    /**
     * Defesa final — RF-FP-06.7.
     * Mesma ordem de aplicação de RF-FP-06.6.
     */
    public static int calcularDefesaFinal(
            int baseEsquiva,
            int bonusArmadura,
            int bonusEscudo,
            int bonusItemDefesa,
            int bonusPlano,
            double somaPctPositivo,
            double somaPctNegativo,
            int somaFlatPositivo,
            int somaFlatNegativo) {

        double base = baseEsquiva + bonusArmadura + bonusEscudo + bonusItemDefesa + bonusPlano;
        double withPct = base * (1.0 + somaPctPositivo - somaPctNegativo);
        int withFloor = (int) Math.floor(withPct);
        int withFlat = withFloor + somaFlatPositivo - somaFlatNegativo;
        return Math.max(0, withFlat);
    }

    // ── RF-FP-06.8 ────────────────────────────────────────────────────────────

    /**
     * Bloqueio final — RF-FP-06.8.
     * Mesma ordem de aplicação de RF-FP-06.6.
     */
    public static int calcularBloqueioFinal(
            int baseBloqueio,
            int bonusEquipBloqueio,
            int bonusItemBloqueio,
            int bonusPlano,
            double somaPctPositivo,
            double somaPctNegativo,
            int somaFlatPositivo,
            int somaFlatNegativo) {

        double base = baseBloqueio + bonusEquipBloqueio + bonusItemBloqueio + bonusPlano;
        double withPct = base * (1.0 + somaPctPositivo - somaPctNegativo);
        int withFloor = (int) Math.floor(withPct);
        int withFlat = withFloor + somaFlatPositivo - somaFlatNegativo;
        return Math.max(0, withFlat);
    }
}
