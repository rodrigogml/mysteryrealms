# Requisitos — Estado Fisiológico

Requisitos funcionais do sistema para fadiga, fome, sede, recuperação, morte, desmaio e moral.

Referências canônicas: `docs/mecanica/fadigaFomeSede.md`, `docs/mecanica/recuperacao.md`, `docs/mecanica/morteDesmaio.md`, `docs/mecanica/moral.md`.

---

## RF-EF-01 — Acúmulo passivo por tempo (tick de minuto)

A cada minuto de jogo que o personagem estiver acordado, o sistema deve executar:

```
fadiga_min += fadiga_max / 1440
fadiga_atual = max(fadiga_atual, fadiga_min)
sede_pct   += 100 / (48 × 60)      ≈ 0,0347 por minuto
fome_pct   += 100 / (7 × 24 × 60)  ≈ 0,0099 por minuto
```

- `fadiga_atual` nunca pode ficar abaixo de `fadiga_min`.
- `sede_pct` e `fome_pct` são limitados ao intervalo `[0, 100]`.

---

## RF-EF-02 — Faixas e estados de fadiga

| Faixa | Gatilho | Estado (`id`) | Efeitos automáticos |
|---|---|---|---|
| Normal | `fadiga_atual < 100% de fadiga_max` | — | Nenhum. |
| Exaustão | `fadiga_atual >= 100% e < 120% de fadiga_max` | `exaustao` | `-2 forca`, `-2 destreza`, `-20% velocidade`, `+15% custo de fadiga por ação`. |
| Colapso | `fadiga_atual >= 120% de fadiga_max` | `desmaio_fadiga` | Desmaio imediato (RF-EF-10). |

---

## RF-EF-03 — Faixas e estados de sede

| Faixa | Gatilho | Estado (`id`) | Efeitos automáticos |
|---|---|---|---|
| Normal | `0% a 24%` | — | Nenhum. |
| Sede | `25% a 64%` | `sede` | `-1 constituicao`, `-10% recuperação de fadiga`, `+10% custo de fadiga por ação`. |
| Sede Agravada | `65% a 99%` | `sede_agravada` | `-2 constituicao`, `-1 vontade`, `-25% recuperação de fadiga`, `+25% custo de fadiga por ação`. |
| Colapso | `100%` | `desmaio_sede` | Desmaio imediato (RF-EF-10). |

---

## RF-EF-04 — Faixas e estados de fome

| Faixa | Gatilho | Estado (`id`) | Efeitos automáticos |
|---|---|---|---|
| Normal | `0% a 42%` | — | Nenhum. |
| Fome | `43% a 84%` | `fome` | `-1 forca`, `-1 intelecto`, `-5% chance de crítico`, `+10% custo de fadiga por ação`. |
| Fome Agravada | `85% a 99%` | `fome_agravada` | `-2 constituicao`, `-2 vontade`, `-20% velocidade`, `+20% custo de fadiga por ação`. |
| Colapso | `100%` | `desmaio_fome` | Desmaio imediato (RF-EF-10). |

---

## RF-EF-05 — Interações entre estados fisiológicos

O sistema deve aplicar os efeitos adicionais abaixo quando os estados coocorrerem:

| Combinação | Efeito adicional |
|---|---|
| `sede_agravada + exaustao` | `-1 destreza` e `-10% precisão` |
| `fome_agravada + exaustao` | `-1 forca` e `-10% bloqueio` |
| `fome_agravada + sede_agravada` | `-15` de moral no instante de entrada da combinação |

### Limites de empilhamento

- Penalidades de atributo de fontes diferentes: acumulam sem limite.
- Penalidade de velocidade: acumula até `-60%` total.
- Modificadores de custo de fadiga por ação: acumulam até `+100%` total.
- Penalidade de recuperação de fadiga: limitada a `-80%` total.

---

## RF-EF-06 — Tabela de precedência de estados (ordem de resolução no tick)

| Prioridade | Categoria | IDs | Regra de resolução |
|---|---|---|---|
| 1 | Colapso/Inconsciência | `desmaio_fadiga`, `desmaio_sede`, `desmaio_fome` | Transferir para RF-EF-10. |
| 2 | Estado crítico de vida | `pv_zerado`, `estado_critico` | Resolver antes de custos contínuos. |
| 3 | Estados fisiológicos graves | `exaustao`, `sede_agravada`, `fome_agravada` | Aplicar efeitos e limites. |
| 4 | Aflições debilitantes | `medo`, `paralisia`, `doenca_magica`, etc. | Após fisiológicos graves. |
| 5 | Estados fisiológicos moderados | `sede`, `fome` | Último na camada fisiológica. |

---

## RF-EF-07 — Recuperação por descanso

Ativo em atividades de baixa demanda (sentado, conversa parada, leitura, meditação leve, cavalgada passiva).

```
recuperacao_fadiga_por_min = 0,10% de fadiga_max × fator_atividade
```

- `fator_atividade`: intervalo `[0, 1]`.
- Não recupera `fadiga_min` nem `pontos_vida`.
- `fadiga_min` continua subindo enquanto o personagem estiver acordado.

---

## RF-EF-08 — Recuperação por sono

Requer local adequado e pelo menos `60` minutos ininterruptos para contar como sono válido.

```
recuperacao_fadiga_por_min      = 0,21% de fadiga_max × fator_qualidade_sono
recuperacao_fadiga_min_por_min  = 0,21% de fadiga_max × fator_qualidade_sono
recuperacao_pv_por_min          = (constituicao / 120) × fator_qualidade_sono
```

### Fórmula do fator de qualidade do sono

```
fator_qualidade_sono = clamp(0,5 + (3 × C + (100 - max(0, R - S))) / 800, 0,5, 1,0)
```

Onde `C`, `R` e `S` são parâmetros de conforto, ruído e segurança do local, definidos pelo contexto de mundo.

---

## RF-EF-09 — Recuperação por itens

- Itens podem recuperar `pontos_vida`, `fadiga_atual`, `fome_pct`, `sede_pct` e `moral`.
- Itens **não** reduzem `fadiga_min` de forma permanente.
- Boost temporário de `fadiga_min` é permitido apenas com duração e reversão explícitas na ficha do item.

---

## RF-EF-10 — Estado de desmaio

### Gatilhos de entrada em desmaio

O sistema deve entrar no estado `desmaio` quando qualquer um dos seguintes ocorrer:

- `pontos_vida <= 0` e o Teste de Conscientização falhar (RF-EF-11).
- `fadiga_atual >= 120% de fadiga_max`.
- `sede_pct >= 100%`.
- `fome_pct >= 100%`.

Se múltiplos gatilhos ocorrerem no mesmo tick, registrar todos os motivos e abrir um único estado `desmaio`.

### Comportamento durante desmaio

- Personagem inconsciente e incapaz de agir ou reagir.
- Nenhum dano passivo sistêmico adicional (exceto regras explícitas de evento).
- Recuperação automática em modo equivalente a dormir com `fator_qualidade_sono = 0,5`.
- Estados contínuos não letais continuam atualizando sem devolver controle ao jogador.

### Critérios de despertar

Para sair de `desmaio`, todos os seguintes devem ser verdadeiros simultaneamente:

- `pontos_vida >= 90% de pontos_vida_max`.
- `fadiga_atual <= 10% de fadiga_max`.
- `fadiga_min <= 10% de fadiga_max`.

### Efeitos ao despertar

1. Aplicar clamp: `fome_pct <= 95%` e `sede_pct <= 95%`.
2. Aplicar modificadores:
   - `fadiga_elevada` por 24h.
   - `mal_estar_temporario` por 24h ou até completar 4h de sono contínuo.
   - `recuperacao_reduzida` por 24h.
3. Recalcular estados de fadiga/fome/sede/moral antes de devolver controle ao jogador.

---

## RF-EF-11 — Estado crítico por Pontos de Vida

Quando `pontos_vida <= 0`:
1. Aplicar estado `estado_critico`.
2. Executar **Teste de Conscientização**:
   - **Sucesso:** personagem retorna com `pontos_vida = 1`, `fadiga_atual = 90% de fadiga_max` e estado `estado_critico_recente` por 10 minutos.
   - **Falha:** entrar em `desmaio` (RF-EF-10).

---

## RF-EF-12 — Moral: faixas e efeitos

| Faixa | Intervalo | Estado (`id`) | Efeitos automáticos |
|---|---|---|---|
| Colapso emocional | `0` a `20` | `moral_colapso` | `-4 vontade`, `-3 carisma`, `-25% testes sociais`, risco de pânico em combate. |
| Baixa moral | `21` a `50` | `moral_baixa` | `-2 vontade`, `-1 carisma`, `-10% testes mentais/sociais`. |
| Moral estável | `51` a `80` | `moral_estavel` | Sem modificadores. |
| Alta moral | `81` a `100` | `moral_alta` | `+2 vontade`, `+1 intimidacao`, `+10% resistência a medo`. |

---

## RF-EF-13 — Moral: gatilhos de variação

### Redução

| Evento | Delta |
|---|---|
| Testemunhar queda de aliado | `-8` |
| Falha crítica narrativa relevante | `-5` |
| Entrada em `fome_agravada + sede_agravada` | `-15` (instantâneo) |
| Entrada em `desmaio` | `-12` |

### Aumento

| Evento | Delta |
|---|---|
| Vitória em combate significativo | `+6` |
| Descanso seguro por 2h sem interrupção | `+5` |
| Sono contínuo `>= 4h` com `fator_qualidade_sono >= 0,75` | `+10` |
| Marco narrativo pessoal | `+8` |

---

## RF-EF-14 — Moral: interação com fisiologia

- `moral_colapso` aumenta em `+10%` o custo de fadiga por ação.
- `moral_alta` reduz em `-10%` o custo de fadiga por ação (não acumula com redutor superior da mesma categoria).
- `fome_agravada` e `sede_agravada` podem forçar troca de faixa de moral no mesmo tick.

---

## RF-EF-15 — Moral: recuperação por repouso

- Descanso seguro sem interrupção por 2h: `+5` de moral.
- Sono contínuo `>= 4h` com `fator_qualidade_sono >= 0,75`: `+10` de moral.
- Interrupção hostil do sono: `-8` de moral no evento.

---

## RF-EF-16 — Ordem de resolução do tick

O sistema deve processar o tick na seguinte ordem obrigatória:

```
colapso/desmaio → estado_critico (PV) → estados fisiológicos graves
→ aflições → recuperação → moral
```

1. Calcular estados fisiológicos (`fadiga`, `fome`, `sede`).
2. Aplicar deltas de moral disparados no tick.
3. Recalcular faixa de moral ativa e seus efeitos.
4. Se qualquer limiar de colapso for atingido após o recálculo, aplicar desmaio (prioridade 1).

---

## Regras explícitas de transição (implementação)

A camada de serviço mantém mapas explícitos de transição permitida por faixa:

- Fadiga: `NORMAL -> EXAUSTAO -> COLAPSO_FADIGA` (com retorno gradual).
- Sede: `NORMAL -> SEDE -> SEDE_AGRAVADA -> COLAPSO_SEDE` (com retorno gradual).
- Fome: `NORMAL -> FOME -> FOME_AGRAVADA -> COLAPSO_FOME` (com retorno gradual).
- Moral: `COLAPSO_EMOCIONAL <-> MORAL_BAIXA <-> MORAL_ESTAVEL <-> MORAL_ALTA`.

Saltos de faixa que pulam um estado intermediário no mesmo tick são considerados inválidos e devem ser bloqueados.

### Exemplos curtos (entrada → saída)

- `fadiga: NORMAL(99%) -> EXAUSTAO(100%)` → válido.
- `fadiga: NORMAL(99%) -> COLAPSO_FADIGA(120%)` → inválido (salto bloqueado).
- `sede: SEDE_AGRAVADA(80%) -> COLAPSO_SEDE(100%)` → válido e com prioridade `COLLAPSE_UNCONSCIOUSNESS`.
- `fome+sede: FOME/SEDE -> FOME_AGRAVADA/SEDE_AGRAVADA` → ação `APPLY_SEVERE_HUNGER_THIRST_MORALE_DELTA` com prioridade `SEVERE_PHYSIOLOGY_STATES`.
