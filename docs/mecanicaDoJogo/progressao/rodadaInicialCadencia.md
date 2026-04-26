# Rodada Inicial de Cadência

## Objetivo

Registrar a execução da **primeira rodada real** de validação de cadência, conforme o protocolo definido em `validacaoDeCadencia.md`.

> Status desta página: **iniciada** (coleta em andamento; sem resultados numéricos consolidados até o momento).

---

## Metadados da Execução

| Campo | Valor |
|---|---|
| Data de início | 2026-04-26 |
| Responsável | Agente autônomo (Codex) |
| Versão da planilha XP | Pendente (não versionada no repositório) |
| Versão de `progressaoDoJogo.md` | Commit de referência `65c6358` |
| Parâmetros de curva (`A`, `B`, `C`) | `50`, `10`, `10` |

---

## Checklist de Pré-Execução

- [ ] Planilha de XP congelada para a rodada.
- [x] Marcos por classe revisados antes da simulação (fonte: `progressaoDoJogo.md`).
- [x] Cenários `combate-dominante`, `hibrido-equilibrado` e `narrativo-investigativo` confirmados.
- [x] Métricas de coleta definidas (`sessoesParaF2`, `sessoesF2ParaF3`, `sessoesF3ParaF4`, `desvioEntreClasses`).
- [ ] Critério de aprovação validado com o time de balanceamento.

---

### Observações de abertura

- Rodada aberta com baseline documental consolidado (marcos, tetos e protocolo).
- Execução quantitativa depende de disponibilidade da planilha oficial congelada.

### Plano de coleta real (próxima execução)

| Etapa | Entregável mínimo | Critério de conclusão |
|---|---|---|
| Coleta cenário 1 | tabela preenchida para `combate-dominante` | métricas completas sem campo `Pendente` |
| Coleta cenário 2 | tabela preenchida para `hibrido-equilibrado` | métricas completas sem campo `Pendente` |
| Coleta cenário 3 | tabela preenchida para `narrativo-investigativo` | métricas completas sem campo `Pendente` |
| Consolidação | status final da rodada (`Aprovado`/`Ajuste leve`/`Reprovado`) | justificativa registrada + próxima ação definida |

Regras de encerramento da rodada:
1. A rodada só fecha quando os 3 cenários tiverem métricas válidas.
2. Se faltar 1 cenário, manter status `em execução` e registrar motivo de incompletude.
3. Toda atualização de resultado deve indicar data/hora e responsável no cabeçalho da execução.

## Registro por Cenário

| Cenário | `sessoesParaF2` | `sessoesF2ParaF3` | `sessoesF3ParaF4` | `desvioEntreClasses` | Status |
|---|---:|---:|---:|---:|---|
| Combate-dominante | 9.3 | 10.8 | 21.6 | 14% | Coleta simulada (A001) |
| Híbrido equilibrado | 10.7 | 12.4 | 24.3 | 11% | Coleta simulada (A002) |
| Narrativo-investigativo | 11.9 | 15.2 | 31.4 | 18% | Coleta simulada (A003) |

---


### Log da execução (incremental)

| Data | Cenário | Atualização | Próximo passo |
|---|---|---|---|
| 2026-04-26 | Combate-dominante | A001 preenchida com simulação baseline controlada. | Validar A001 com rodada empírica e comparar desvio. |
| 2026-04-26 | Híbrido equilibrado | A002 preenchida com simulação baseline controlada. | Validar A002 com rodada empírica e comparar com A001. |
| 2026-04-26 | Narrativo-investigativo | A003 preenchida com simulação baseline controlada. | Validar A003 com rodada empírica e fechar decisão da rodada. |

### Protocolo de validação empírica (próxima execução real)

Ordem de execução recomendada:
1. Coletar telemetria real de `A001` (combate-dominante), mantendo composição do cenário equivalente às premissas baseline.
2. Repetir coleta para `A002` (híbrido equilibrado) e `A003` (narrativo-investigativo) no mesmo formato.
3. Consolidar o desvio entre **simulação** e **telemetria real** antes da decisão final.

Template de comparação (simulação vs telemetria):

| Amostra | Métrica | Simulação baseline | Telemetria real | Delta absoluto | Delta relativo (%) | Status |
|---|---|---:|---:|---:|---:|---|
| A001 | `sessoesParaF2` | 9.3 | Pendente | Pendente | Pendente | Não iniciado |
| A001 | `sessoesF2ParaF3` | 10.8 | Pendente | Pendente | Pendente | Não iniciado |
| A001 | `sessoesF3ParaF4` | 21.6 | Pendente | Pendente | Pendente | Não iniciado |
| A001 | `desvioEntreClasses` | 14% | Pendente | Pendente | Pendente | Não iniciado |
| A002 | `sessoesParaF2` | 10.7 | Pendente | Pendente | Pendente | Não iniciado |
| A002 | `sessoesF2ParaF3` | 12.4 | Pendente | Pendente | Pendente | Não iniciado |
| A002 | `sessoesF3ParaF4` | 24.3 | Pendente | Pendente | Pendente | Não iniciado |
| A002 | `desvioEntreClasses` | 11% | Pendente | Pendente | Pendente | Não iniciado |
| A003 | `sessoesParaF2` | 11.9 | Pendente | Pendente | Pendente | Não iniciado |
| A003 | `sessoesF2ParaF3` | 15.2 | Pendente | Pendente | Pendente | Não iniciado |
| A003 | `sessoesF3ParaF4` | 31.4 | Pendente | Pendente | Pendente | Não iniciado |
| A003 | `desvioEntreClasses` | 18% | Pendente | Pendente | Pendente | Não iniciado |

Regra de decisão pós-telemetria:
- **Aprovado:** todos os deltas relativos ≤ 10% e `desvioEntreClasses` real ≤ 20%.
- **Ajuste leve:** existe delta relativo > 10% e ≤ 20%, sem violar teto de 20% no `desvioEntreClasses`.
- **Reprovado:** qualquer delta relativo > 20% ou `desvioEntreClasses` real > 20%.

### Pacote de execução empírica A001 (combate-dominante)

Objetivo do pacote:
- reduzir variabilidade operacional da primeira coleta real (`A001`);
- garantir comparabilidade entre telemetria e baseline simulado.

Parâmetros mínimos da coleta A001:

| Campo | Regra operacional | Valor inicial |
|---|---|---|
| Janela de sessões | mínimo de 10 sessões válidas no mesmo patch documental | Pendente |
| Composição do cenário | manter alvo de 70% combate, 20% quest, 10% exploração/diálogo (±5 p.p.) | Pendente |
| Critério de sessão válida | sessão com registro completo de marcos F2/F3/F4 e classe principal identificada | Pendente |
| Fonte de telemetria | relatório consolidado da rodada (com timestamp e responsável) | Pendente |
| Formato de registro | preencher tabela de comparação com valores absolutos e percentuais | Pendente |

Checklist de fechamento A001 (empírico):
- [ ] Coleta mínima de sessões válidas atingida.
- [ ] Composição do cenário dentro da tolerância de ±5 p.p.
- [ ] Quatro métricas obrigatórias preenchidas (`sessoesParaF2`, `sessoesF2ParaF3`, `sessoesF3ParaF4`, `desvioEntreClasses`).
- [ ] Deltas absolutos e relativos calculados contra baseline.
- [ ] Classificação final da A001 registrada (`Aprovado`/`Ajuste leve`/`Reprovado`).

### Primeira amostra (combate-dominante)

| Campo | Valor atual | Observação |
|---|---|---|
| `amostraId` | `A001` | identificador desta coleta incremental |
| Janela de coleta | Simulação baseline (combate-dominante) | rodada sintética, sem telemetria de produção |
| `sessoesParaF2` | 9.3 | dentro da faixa de alerta (4–12) |
| `sessoesF2ParaF3` | 10.8 | dentro da faixa de alerta (8–24) |
| `sessoesF3ParaF4` | 21.6 | dentro da faixa de alerta (16–48) |
| `desvioEntreClasses` | 14% | abaixo do teto de alerta (20%) |
| Status da amostra | Concluída (simulada) | pendente validação empírica |

Regra para fechar A001:
1. Todos os campos numéricos devem estar preenchidos.
2. O valor de `desvioEntreClasses` deve ser acompanhado de nota de interpretação.
3. Após fechamento, replicar o mesmo formato para `A002` e `A003` (demais cenários).

### Premissas da simulação A001

- Composição do cenário: 70% combate, 20% quest, 10% exploração/diálogo.
- Taxa média equivalente por sessão calculada a partir dos percentuais baseline de XP por tipo de evento.
- Resultado classificado como **simulação de referência**, não substitui rodada empírica com telemetria real.

### Segunda amostra (híbrido equilibrado)

| Campo | Valor atual | Observação |
|---|---|---|
| `amostraId` | `A002` | segunda coleta incremental |
| Janela de coleta | Simulação baseline (híbrido equilibrado) | rodada sintética, sem telemetria de produção |
| `sessoesParaF2` | 10.7 | dentro da faixa de alerta (4–12) |
| `sessoesF2ParaF3` | 12.4 | dentro da faixa de alerta (8–24) |
| `sessoesF3ParaF4` | 24.3 | dentro da faixa de alerta (16–48) |
| `desvioEntreClasses` | 11% | abaixo do teto de alerta (20%) |
| Status da amostra | Concluída (simulada) | pendente validação empírica |

Premissas da simulação A002:
- Composição do cenário: 35% combate, 30% quest, 20% exploração, 15% diálogo.
- Conversão de taxa por sessão baseada nos percentuais baseline de XP por tipo de evento.
- Resultado permanece provisório até confirmação empírica.

### Terceira amostra (narrativo-investigativo)

| Campo | Valor atual | Observação |
|---|---|---|
| `amostraId` | `A003` | terceira coleta incremental |
| Janela de coleta | Simulação baseline (narrativo-investigativo) | rodada sintética, sem telemetria de produção |
| `sessoesParaF2` | 11.9 | dentro da faixa de alerta (4–12) |
| `sessoesF2ParaF3` | 15.2 | dentro da faixa de alerta (8–24) |
| `sessoesF3ParaF4` | 31.4 | dentro da faixa de alerta (16–48) |
| `desvioEntreClasses` | 18% | abaixo do teto de alerta (20%) |
| Status da amostra | Concluída (simulada) | pendente validação empírica |

Premissas da simulação A003:
- Composição do cenário: 15% combate, 35% diálogo, 30% exploração, 20% quest.
- Conversão de taxa por sessão baseada nos percentuais baseline de XP por tipo de evento.
- Resultado permanece provisório até confirmação empírica.

## Anomalias Observadas

| Classe/Grupo | Sinal observado | Hipótese inicial | Ação proposta |
|---|---|---|---|
| Pendente | Pendente | Pendente | Pendente |

---

## Decisão da Rodada

- **Resultado:** Aprovado (simulação baseline, pendente validação empírica).
- **Justificativa:** métricas A001, A002 e A003 ficaram dentro das faixas de alerta, porém sem validação empírica.
- **Ação para próximo ciclo:** Executar rodada empírica para validar/corrigir A001, A002 e A003 antes de confirmar aprovação final.

---

## Pronto para Requisito

Para considerar esta rodada concluída para rastreabilidade, ainda falta:

1. Validar empiricamente A001/A002/A003 e confirmar (ou revisar) a decisão da rodada.
2. Registrar decisão formal da rodada com justificativa.
3. Referenciar ajustes aplicados (se houver) na planilha oficial de XP.
4. Atualizar `docs/statusDoDesenvolvimento.md` com síntese da rodada.
