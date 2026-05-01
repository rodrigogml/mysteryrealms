package br.eng.rodrigogml.mysteryrealms.config.observability;

import java.util.UUID;
import org.slf4j.MDC;

public final class LogContext {

    public static final String JOB_ID_KEY = "jobId";

    private LogContext() {
    }

    public static AutoCloseable withJobId(String jobId) {
        String effectiveJobId = (jobId == null || jobId.isBlank())
                ? UUID.randomUUID().toString()
                : jobId;

        MDC.put(JOB_ID_KEY, effectiveJobId);
        return () -> MDC.remove(JOB_ID_KEY);
    }
}
