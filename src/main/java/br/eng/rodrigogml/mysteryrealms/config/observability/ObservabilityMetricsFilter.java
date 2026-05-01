package br.eng.rodrigogml.mysteryrealms.config.observability;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class ObservabilityMetricsFilter extends OncePerRequestFilter {

    private final MeterRegistry meterRegistry;

    public ObservabilityMetricsFilter(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        long startedAt = System.nanoTime();
        String route = request.getMethod() + " " + request.getRequestURI();

        try {
            filterChain.doFilter(request, response);
        } finally {
            long elapsedNanos = System.nanoTime() - startedAt;
            String status = String.valueOf(response.getStatus());

            Timer.builder("mysteryrealms.request.latency")
                    .description("Latência por requisição HTTP")
                    .tag("route", route)
                    .tag("status", status)
                    .register(meterRegistry)
                    .record(elapsedNanos, TimeUnit.NANOSECONDS);

            meterRegistry.counter(
                    "mysteryrealms.request.throughput",
                    "route", route,
                    "status", status
            ).increment();

            if (response.getStatus() >= 500) {
                meterRegistry.counter(
                        "mysteryrealms.request.errors",
                        "route", route,
                        "status", status
                ).increment();
            }
        }
    }
}
