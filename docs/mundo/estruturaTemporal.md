# Estrutura Temporal

Este documento define o contrato temporal canônico para o mundo, em integração direta com movimentação, estados fisiológicos e eventos narrativos.

## Princípios

- A unidade interna de tempo é sempre **minuto inteiro** (`tempo_total_min`).
- O tempo **só avança por ação** (movimentação, combate, interação, descanso, viagem narrativa).
- Conversões para formato legível (`ano`, `dia`, `hora`, `minuto`) são derivadas dos parâmetros do mundo.

## Parâmetros obrigatórios por Mundo

| Campo | Tipo | Obrigatório | Regra |
|---|---|---|---|
| `id_mundo` | string | Sim | Identificador único estável. |
| `minutos_por_hora` | inteiro | Sim | `>= 1`. |
| `horas_por_dia` | inteiro | Sim | `>= 1`. |
| `dias_por_ano` | inteiro | Sim | `>= 1`. |
| `fases_dia` | lista | Sim | Lista ordenada sem sobreposição. |
| `estacoes` | lista | Sim | Deve cobrir o ano completo sem lacunas. |
| `tempo_inicial_min` | inteiro | Sim | Valor inicial do contador absoluto (normalmente `0`). |

### Contrato mínimo de `fases_dia`

Cada fase deve conter:

| Campo | Tipo | Obrigatório | Regra |
|---|---|---|---|
| `id_fase` | string | Sim | Ex.: `amanhecer`, `dia`, `anoitecer`, `noite`. |
| `inicio_min_dia` | inteiro | Sim | `>= 0` e `< minutos_por_dia`. |
| `fim_min_dia` | inteiro | Sim | `>= inicio_min_dia` e `< minutos_por_dia`. |

### Contrato mínimo de `estacoes`

| Campo | Tipo | Obrigatório | Regra |
|---|---|---|---|
| `id_estacao` | string | Sim | Identificador estável. |
| `dia_inicio` | inteiro | Sim | `1 <= dia_inicio <= dias_por_ano`. |
| `dia_fim` | inteiro | Sim | `dia_fim >= dia_inicio`. |

## Consumo temporal por movimentação

Movimentação em conexão consome tempo diretamente e deve atualizar o relógio global.

1. Calcular distância ajustada da conexão (`distancia_km_ajustada`).
2. Calcular velocidade efetiva (`velocidade_kmh_efetiva`).
3. Converter em minutos:

```text
minutos_mov = ceil((distancia_km_ajustada / velocidade_kmh_efetiva) * minutos_por_hora)
```

4. Aplicar `tempo_total_min += minutos_mov`.
5. Disparar atualizações fisiológicas no mesmo tick (fadiga/sede/fome).

## Sincronização com fisiologia

A cada avanço temporal:

- `fadiga_min` e `fadiga_atual` são recalculadas conforme regras de desgaste.
- `sede_pct` e `fome_pct` sobem proporcionalmente ao tempo decorrido.
- Mudança de faixa fisiológica pode alterar velocidade e, por consequência, tempo de viagem subsequente.

> Referência mecânica: `docs/mecanica/fadigaFomeSede.md`.

## Exemplo (Langur)

- Mundo padrão: 60 min/hora, 24 h/dia.
- Trajeto: **Langur / Praça das Vozes** → **Langur / Biblioteca Varnak**.
- Distância ajustada: `4,7 km`.
- Velocidade efetiva do grupo: `3,6 km/h` (já com penalidade por fadiga leve).

```text
minutos_mov = ceil((4,7 / 3,6) * 60) = ceil(78,33) = 79 min
```

Resultado:

- Avanço temporal: `+79 min`.
- Aplicação de desgaste fisiológico por 79 minutos.
- Revalidação de estados antes de permitir a próxima conexão.
