# Requisitos — Mundo e Navegação

Requisitos funcionais do sistema para hierarquia de localização, zonas, ambientes, conexões, mapa, movimentação e estrutura temporal.

Referências canônicas: `docs/mundo/estruturaDeLocalizacao.md`, `docs/mundo/estruturaTemporal.md`, `docs/mundo/mapaEMovimentacao.md`, `docs/mundo/modelosDeFicha.md`.

---

## RF-MN-01 — Hierarquia de localização

O sistema deve suportar a hierarquia espacial canônica:

```
Mundo → Continente → Domínio → Região → Localidade → Zona → Ambiente
```

- `Zona` e `Ambiente` são os únicos níveis navegáveis diretamente pelo jogador.
- Cada nível deve referenciar seu pai imediato.

### Regras de nomenclatura e IDs

- IDs devem usar apenas minúsculas, números e `_`.
- Prefixos obrigatórios por tipo:

| Tipo | Prefixo |
|---|---|
| Mundo | `mundo_` |
| Continente | `cont_` |
| Domínio | `dom_` |
| Região | `reg_` |
| Localidade | `loc_` |
| Zona | `zona_` |
| Ambiente | `amb_` |

---

## RF-MN-02 — Contrato mínimo: Zona

| Campo | Tipo | Obrigatório | Regra |
|---|---|---|---|
| `id_zona` | string | Sim | Único globalmente; prefixo `zona_`. |
| `nome` | string | Sim | Nome exibível não vazio. |
| `id_localidade` | string | Sim | Deve existir em `Localidade`. |
| `coord_x` | decimal | Sim | Coordenada cartesiana global. |
| `coord_y` | decimal | Sim | Coordenada cartesiana global. |
| `tipo_navegavel` | enum | Sim | Valor fixo `zona`. |
| `acessivel` | boolean | Sim | Define disponibilidade como origem/destino. |
| `descricao` | string | Não | Texto curto de ambientação. |

---

## RF-MN-03 — Contrato mínimo: Ambiente

| Campo | Tipo | Obrigatório | Regra |
|---|---|---|---|
| `id_ambiente` | string | Sim | Único globalmente; prefixo `amb_`. |
| `nome` | string | Sim | Nome exibível não vazio. |
| `id_zona` | string | Sim | Deve existir em `Zona`. |
| `coord_x` | decimal | Sim | Coordenada cartesiana global. |
| `coord_y` | decimal | Sim | Coordenada cartesiana global. |
| `tipo_navegavel` | enum | Sim | Valor fixo `ambiente`. |
| `acessivel` | boolean | Sim | Define disponibilidade como origem/destino. |
| `descricao` | string | Não | Texto curto de ambientação. |

---

## RF-MN-04 — Contrato mínimo: Conexão

| Campo | Tipo | Obrigatório | Regra |
|---|---|---|---|
| `id_conexao` | string | Sim | Único globalmente. |
| `origem_id` | string | Sim | ID de Zona ou Ambiente. |
| `destinos_priorizados` | lista<string> | Sim | Pelo menos 1 destino válido. |
| `bidirecional` | boolean | Sim | Se `true`, cria fluxo reverso automático. |
| `classificacao` | enum | Sim | `pacificado`, `hostil` ou `selvagem`. |
| `penalidade_rota_pct` | decimal | Sim | Intervalo `[0, 15]`. |
| `chance_interrupcao_km_pct` | decimal | Sim | Intervalo `[0, 100]`. |
| `tabela_interrupcoes_id` | string | Sim | Referência para sorteio de interrupções. |

---

## RF-MN-05 — Contrato mínimo: Cluster

| Campo | Tipo | Obrigatório | Regra |
|---|---|---|---|
| `id_cluster` | string | Sim | Único no escopo da localidade. |
| `nome` | string | Sim | Nome de referência operacional. |
| `id_localidade` | string | Sim | Localidade dona do cluster. |
| `pontos` | lista<string> | Sim | IDs de Zonas/Ambientes pertencentes. |
| `conexoes` | lista<string> | Sim | IDs de conexões do cluster. |
| `modo_navegacao` | enum | Sim | `livre` ou `guiada`. |

---

## RF-MN-06 — Validações de consistência hierárquica

1. Toda ficha deve referenciar um pai existente do tipo correto.
2. Não pode haver ciclo hierárquico (um nó não pode ser ancestral de si próprio).
3. O caminho completo de um Ambiente deve resolver até Mundo sem lacunas.
4. IDs de Zona e Ambiente devem ser únicos globalmente.
5. `coord_x` e `coord_y` de Zona e Ambiente devem usar a mesma malha global.
6. `tipo_navegavel` deve ser coerente com o tipo da ficha.
7. Todo `origem_id` e `destino` de uma Conexão deve existir e estar acessível.

---

## RF-MN-07 — Sistema de coordenadas

- Malha cartesiana global no formato `[x; y]`.
- Cada unidade inteira em `x` ou `y` equivale a **10 km**.

### Distância base

```
distancia_base_km = sqrt((x2 - x1)^2 + (y2 - y1)^2) * 10
```

### Distância ajustada por rota

```
distancia_ajustada_km = distancia_base_km * (1 + penalidade_rota_pct / 100)
```

---

## RF-MN-08 — Resolução de destino

1. Tentar o primeiro destino da lista `destinos_priorizados`.
2. Se não estiver acessível (`acessivel = false`), tentar o próximo.
3. Se nenhum destino estiver acessível, a ação de mover falha sem deslocamento.

---

## RF-MN-09 — Interrupções durante deslocamento

A progressão ocorre em blocos de 1 km até concluir a distância restante.

**Para cada 1 km:**
1. Rolar teste com `chance_interrupcao_km_pct`.
2. Se sem interrupção: avançar 1 km.
3. Se com interrupção:
   - Sortear ponto do evento dentro do km corrente (`1d100` → fração).
   - Avançar apenas a fração percorrida.
   - Resolver evento.
   - Jogador decide continuar ou abortar.

Se distância restante `< 1 km`:
```
chance_ajustada_pct = chance_interrupcao_km_pct * distancia_restante_km
```

---

## RF-MN-10 — Consumo de tempo por deslocamento

```
minutos_mov = ceil((distancia_ajustada_km / velocidade_kmh_efetiva) * minutos_por_hora)
tempo_total_min += minutos_mov
```

Após cada avanço temporal, disparar atualizações fisiológicas no mesmo tick (RF-EF-01).

---

## RF-MN-11 — Atualização fisiológica por trecho

A cada trecho de deslocamento concluído (ou parcial):

```
tempo_total_min  += minutos_trecho
fadiga_atual     += custo_fadiga_trecho
sede_pct         += delta_sede_trecho
fome_pct         += delta_fome_trecho
```

Se `fadiga`, `sede` ou `fome` mudarem de faixa durante o deslocamento, recalcular velocidade efetiva para os trechos seguintes.

---

## RF-MN-12 — Estrutura temporal do mundo

Cada mundo deve declarar:

| Campo | Tipo | Obrigatório | Regra |
|---|---|---|---|
| `id_mundo` | string | Sim | Identificador único estável. |
| `minutos_por_hora` | inteiro | Sim | `>= 1`. |
| `horas_por_dia` | inteiro | Sim | `>= 1`. |
| `dias_por_ano` | inteiro | Sim | `>= 1`. |
| `fases_dia` | lista | Sim | Ordenada, sem sobreposição, cobrindo o dia completo. |
| `estacoes` | lista | Sim | Deve cobrir o ano completo sem lacunas. |
| `tempo_inicial_min` | inteiro | Sim | Normalmente `0`. |

### Contrato de fase do dia

| Campo | Tipo | Obrigatório | Regra |
|---|---|---|---|
| `id_fase` | string | Sim | Ex.: `amanhecer`, `dia`, `anoitecer`, `noite`. |
| `inicio_min_dia` | inteiro | Sim | `>= 0` e `< minutos_por_dia`. |
| `fim_min_dia` | inteiro | Sim | `>= inicio_min_dia` e `< minutos_por_dia`. |

### Contrato de estação

| Campo | Tipo | Obrigatório | Regra |
|---|---|---|---|
| `id_estacao` | string | Sim | Identificador estável. |
| `dia_inicio` | inteiro | Sim | `1 <= dia_inicio <= dias_por_ano`. |
| `dia_fim` | inteiro | Sim | `dia_fim >= dia_inicio`. |

---

## RF-MN-13 — Princípio de avanço temporal

- A unidade interna de tempo é sempre **minuto inteiro** (`tempo_total_min`).
- O tempo **só avança por ação** (movimentação, combate, interação, descanso, viagem narrativa).
- Conversões para formato legível (ano, dia, hora, minuto) são derivadas dos parâmetros do mundo em RF-MN-12.
