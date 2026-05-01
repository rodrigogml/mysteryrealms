# Observabilidade

## Objetivo
Padronizar logs, métricas e correlação para reduzir MTTR em produção e facilitar monitoramento de execução por request e por job.

## Convenções de correlação

### Request ID
- Header de entrada: `X-Request-Id`.
- Se não informado pelo cliente, o backend gera um UUID.
- O valor é devolvido no response header `X-Request-Id`.
- O valor também é publicado no MDC como `requestId`, aparecendo em todos os logs da thread da requisição.

### Job ID
- Para execuções assíncronas e jobs, usar `LogContext.withJobId("<id>")`.
- Quando `jobId` é vazio/nulo, é gerado UUID automaticamente.
- O MDC `jobId` precisa envolver toda a execução lógica do job.

Exemplo:

```java
try (AutoCloseable ignored = LogContext.withJobId("sync-diario-2026-05-01")) {
    // processamento do job
}
```

## Níveis de log

### Padrão
- `ERROR`: falhas não recuperáveis, exceções propagadas, inconsistência de estado.
- `WARN`: degradação controlada, fallback, tentativas excedidas.
- `INFO`: início/fim de fluxo relevante, eventos de negócio importantes.
- `DEBUG`: detalhes diagnósticos para troubleshooting técnico.
- `TRACE`: granularidade máxima (evitar em produção).

### Configuração aplicada
- `root`: `INFO`
- `br.eng.rodrigogml.mysteryrealms`: `INFO`
- `org.springframework`: `WARN`

### Formato de log
O padrão de console inclui:
- timestamp ISO-8601;
- nível;
- app;
- `requestId`;
- `jobId`;
- thread;
- logger;
- mensagem.

## Métricas padrão

### HTTP
- `mysteryrealms.request.latency` (Timer): latência por rota/status.
- `mysteryrealms.request.throughput` (Counter): volume por rota/status.
- `mysteryrealms.request.errors` (Counter): erros HTTP 5xx por rota/status.

Tags padrão:
- `route`: `<METHOD> <URI>`
- `status`: código HTTP
- `application`: `${spring.application.name}`

### Exposição
- Actuator habilitado para `health`, `info`, `prometheus`, `metrics`.
- Endpoint Prometheus: `/actuator/prometheus`.

## Dashboards esperados

### 1) API Overview
- Throughput total (req/s) por status class.
- Latência p50/p95/p99 por rota.
- Taxa de erro 5xx (global e por rota).
- Top 10 rotas com maior p95.

### 2) Error Drilldown
- Série temporal de `mysteryrealms.request.errors`.
- Tabela por rota/status com janela de 5 min e 1 h.
- Painel com logs filtrados por `requestId`.

### 3) Execução de Jobs
- Logs com agrupamento por `jobId`.
- Tempo médio de execução por tipo de job (a evoluir com métricas dedicadas).
- Contagem de falhas por job e última execução com erro.

## Alertas recomendados
- Erro 5xx > 2% por 5 min.
- p95 de latência > 1.5 s por 10 min em rotas críticas.
- Ausência de throughput em rotas de health-check ou queda abrupta > 80%.

## Próximos passos sugeridos
- Instrumentar serviços de domínio com timers específicos por caso de uso.
- Adicionar métricas de fila e jobs assíncronos.
- Padronizar eventos estruturados de auditoria (security/business).
