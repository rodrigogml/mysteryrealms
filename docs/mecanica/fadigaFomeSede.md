# Fadiga, Fome e Sede

Este documento define os gatilhos numéricos e efeitos canônicos dos sistemas fisiológicos de desgaste.

## Variáveis canônicas

| Exibição | Chave técnica | Regra |
|---|---|---|
| Fadiga Atual | `fadiga_atual` | Cresce por ações e passagem de tempo. |
| Fadiga Máxima | `fadiga_max` | `(constituicao + vontade) × 100`. |
| Fadiga Mínima | `fadiga_min` | Piso dinâmico da fadiga enquanto acordado. |
| Fome | `fome_pct` | Escala percentual de `0` a `100`. |
| Sede | `sede_pct` | Escala percentual de `0` a `100`. |

## Acúmulo base

- `fadiga_min += fadiga_max / 1440` por minuto acordado.
- `fadiga_atual` nunca pode ficar abaixo de `fadiga_min`.
- `sede_pct += 100 / (48 × 60)` por minuto (`≈ 0,0347`), antes de multiplicadores.
- `fome_pct += 100 / (7 × 24 × 60)` por minuto (`≈ 0,0099`), antes de multiplicadores.

## Faixas e efeitos por sistema

### Fadiga

| Faixa | Gatilho | Estado (`id`) | Efeitos |
|---|---:|---|---|
| Normal | `0% a <100% de fadiga_max` | — | Sem penalidade automática. |
| Exaustão | `>=100% e <120% de fadiga_max` | `exaustao` | `-2 forca`, `-2 destreza`, `-20% velocidade`, `+15% custo de fadiga por ação`. |
| Colapso | `>=120% de fadiga_max` | `desmaio_fadiga` | Desmaio imediato. |

### Sede

| Faixa | Gatilho | Estado (`id`) | Efeitos |
|---|---:|---|---|
| Normal | `0% a 24%` | — | Sem penalidade. |
| Sede | `25% a 64%` | `sede` | `-1 constituicao`, `-10% recuperação de fadiga`, `+10% custo de fadiga por ação`. |
| Sede Agravada | `65% a 99%` | `sede_agravada` | `-2 constituicao`, `-1 vontade`, `-25% recuperação de fadiga`, `+25% custo de fadiga por ação`. |
| Colapso | `100%` | `desmaio_sede` | Desmaio imediato. |

### Fome

| Faixa | Gatilho | Estado (`id`) | Efeitos |
|---|---:|---|---|
| Normal | `0% a 42%` | — | Sem penalidade. |
| Fome | `43% a 84%` | `fome` | `-1 forca`, `-1 intelecto`, `-5% chance de crítico`, `+10% custo de fadiga por ação`. |
| Fome Agravada | `85% a 99%` | `fome_agravada` | `-2 constituicao`, `-2 vontade`, `-20% velocidade`, `+20% custo de fadiga por ação`. |
| Colapso | `100%` | `desmaio_fome` | Desmaio imediato. |

## Interação entre sistemas

### Regras de empilhamento

1. Penalidades de atributo de fontes diferentes acumulam.
2. Penalidades de velocidade acumulam até `-60%`.
3. Modificadores de custo de fadiga por ação acumulam até `+100%`.
4. Penalidade de recuperação de fadiga acumulada é limitada a `-80%`.
5. Colapso (`desmaio_fadiga`/`desmaio_sede`/`desmaio_fome`) interrompe ação atual.

### Interações adicionais

- `sede_agravada + exaustao`: adicional `-1 destreza` e `-10% precisão`.
- `fome_agravada + exaustao`: adicional `-1 forca` e `-10% bloqueio`.
- `fome_agravada + sede_agravada`: `-15` de moral no instante de entrada da combinação.

## Tabela de precedência de estados

| Prioridade (1 = maior) | Categoria | Exemplos | Regra |
|---:|---|---|---|
| 1 | Colapso/Inconsciência | `desmaio_fadiga`, `desmaio_sede`, `desmaio_fome` | Interrompe ação e transfere para `morteDesmaio.md`. |
| 2 | Estado crítico de vida | `pv_zerado`, `estado_critico` | Resolve conscientização antes de custos contínuos. |
| 3 | Estados fisiológicos graves | `exaustao`, `sede_agravada`, `fome_agravada` | Aplicar efeitos e limites de empilhamento. |
| 4 | Aflições debilitantes | `medo`, `paralisia`, `doenca_magica`, etc. | Aplicar após fisiológicos graves. |
| 5 | Estados fisiológicos moderados | `sede`, `fome` | Aplicar por último na camada fisiológica. |

### Exemplo de resolução

Se ocorrer `exaustao + sede_agravada + aflicao` no mesmo tick:
1. Aplicar prioridade 3 (`exaustao` + `sede_agravada`).
2. Aplicar prioridade 4 (aflição).
3. Se qualquer limiar de colapso for atingido após o recálculo, aplicar prioridade 1 (`desmaio`).
