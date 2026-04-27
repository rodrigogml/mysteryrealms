package br.eng.rodrigogml.mysteryrealms.domain.economy.model;

/**
 * Valor monetário com Moeda Primária (MP) e Moeda Secundária (MS) — RF-EI-01.
 *
 * Taxa fixa: 1 MP = 100 MS.
 * Formato canônico: {@code MP$MS} (ex.: {@code 10$25}, {@code 10$}, {@code $25}).
 */
public record ValorMonetario(long mp, long ms) {

    public ValorMonetario {
        if (mp < 0) throw new IllegalArgumentException("mp deve ser >= 0, recebido: " + mp);
        if (ms < 0) throw new IllegalArgumentException("ms deve ser >= 0, recebido: " + ms);
    }

    public static ValorMonetario deMp(long mp) {
        return new ValorMonetario(mp, 0);
    }

    public static ValorMonetario deMs(long ms) {
        return new ValorMonetario(0, ms);
    }

    public static ValorMonetario de(long mp, long ms) {
        return new ValorMonetario(mp, ms);
    }

    public static ValorMonetario zero() {
        return new ValorMonetario(0, 0);
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
    public double pesoKg() {
        return ((mp * 6L) + (ms * 5L)) / 1000.0;
    }

    /** Valor total equivalente em MS — RF-EI-01: 1 MP = 100 MS. */
    public long totalEmMs() {
        return mp * 100L + ms;
    }

    /**
     * Converte para formato normalizado: minimiza MP, mantém resto como MS.
     * Útil após operações que podem gerar > 99 MS.
     */
    public ValorMonetario normalizado() {
        long total = totalEmMs();
        return new ValorMonetario(total / 100, total % 100);
    }
}
