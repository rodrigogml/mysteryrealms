package br.eng.rodrigogml.mysteryrealms.domain.economy.model;

/**
 * Valor monetário com Moeda Primária (MP) e Moeda Secundária (MS) — RF-EI-01.
 *
 * Taxa fixa: 1 MP = 100 MS.
 * Formato canônico: {@code MP$MS} (ex.: {@code 10$25}, {@code 10$}, {@code $25}).
 */
public record MonetaryValue(long mp, long ms) {

    public MonetaryValue {
        if (mp < 0) throw new IllegalArgumentException("mp deve ser >= 0, recebido: " + mp);
        if (ms < 0) throw new IllegalArgumentException("ms deve ser >= 0, recebido: " + ms);
    }

    public static MonetaryValue ofMp(long mp) {
        return new MonetaryValue(mp, 0);
    }

    public static MonetaryValue ofMs(long ms) {
        return new MonetaryValue(0, ms);
    }

    public static MonetaryValue of(long mp, long ms) {
        return new MonetaryValue(mp, ms);
    }

    public static MonetaryValue zero() {
        return new MonetaryValue(0, 0);
    }

    /** Formato canônico: {@code MP$MS} — RF-EI-01. */
    public String format() {
        if (mp > 0 && ms > 0) return mp + "$" + ms;
        if (mp > 0) return mp + "$";
        return "$" + ms;
    }

    /**
     * Peso total das moedas em kg — RF-EI-01.
     * {@code peso_moedas_kg = ((mp × 6) + (ms × 5)) / 1000}
     */
    public double weightKg() {
        return ((mp * 6L) + (ms * 5L)) / 1000.0;
    }

    /** Valor total equivalente em MS — RF-EI-01: 1 MP = 100 MS. */
    public long toTotalMs() {
        return mp * 100L + ms;
    }

    /**
     * Converte para formato normalizado: minimiza MP, mantém resto como MS.
     * Útil após operações que podem gerar > 99 MS.
     */
    public MonetaryValue normalized() {
        long total = toTotalMs();
        return new MonetaryValue(total / 100, total % 100);
    }
}
