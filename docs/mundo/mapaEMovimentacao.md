# Mapa e Movimentação

Este documento padroniza o cálculo de deslocamento entre Zonas/Ambientes, incluindo tempo consumido, interrupções e impacto fisiológico.

## Sistema de coordenadas

- Malha cartesiana global no formato `[x;y]`.
- Cada unidade inteira em `x` ou `y` equivale a **10 km**.
- Distância base entre dois pontos:

```text
distancia_base_km = sqrt((x2 - x1)^2 + (y2 - y1)^2) * 10
```

## Distância ajustada por rota

Cada conexão aplica penalidade (`penalidade_rota_pct`) para representar curvas, relevo, desvios e obstáculos.

```text
distancia_ajustada_km = distancia_base_km * (1 + penalidade_rota_pct/100)
```

## Origem, destino e fallback

Uma conexão deve possuir:

- `origem_id` (Zona/Ambiente atual).
- `destinos_priorizados` (lista ordenada).

Processo:

1. Tentar o primeiro destino da lista.
2. Se não estiver acessível, tentar o próximo.
3. Se nenhum destino estiver acessível, a ação de mover falha sem deslocamento.

## Interrupções durante deslocamento

A progressão ocorre em blocos de 1 km até concluir a distância restante.

Para cada 1 km:

1. Rolar teste com `chance_interrupcao_km_pct`.
2. Se não houver interrupção: avança 1 km.
3. Se houver interrupção:
   - sortear ponto do evento dentro do km corrente (`1d100` => fração);
   - avançar só a fração percorrida;
   - resolver evento;
   - jogador decide continuar ou abortar.

Se distância restante `< 1 km`, ajustar chance proporcionalmente:

```text
chance_ajustada_pct = chance_interrupcao_km_pct * distancia_restante_km
```

## Relação direta com tempo e fisiologia

Cada deslocamento concluído (ou parcial, em caso de interrupção) deve:

1. Consumir tempo proporcional ao trecho percorrido.
2. Aplicar desgaste fisiológico proporcional ao tempo gasto.

### Atualizações obrigatórias por trecho

- `tempo_total_min += minutos_trecho`
- `fadiga_atual += custo_fadiga_trecho`
- `sede_pct += delta_sede_trecho`
- `fome_pct += delta_fome_trecho`

Se `fadiga`, `sede` ou `fome` mudarem de faixa, recalcular velocidade efetiva para os próximos trechos.

## Exemplo concreto: Langur (origem/destino, penalidade, interrupções)

### Cenário

- Origem: `zona_langur_praca_das_vozes` (`[0.8500; 0.2000]`)
- Destino principal: `zona_langur_biblioteca_varnak` (`[0.0000; 0.4500]`)
- Destino fallback: `zona_langur_anel_central_portao_leste`
- Penalidade da conexão: `8%`
- Chance de interrupção por km: `6%`
- Velocidade efetiva inicial: `4,0 km/h`

### Cálculo de distância

```text
distancia_base = sqrt((0.0000-0.8500)^2 + (0.4500-0.2000)^2) * 10
               = sqrt(0.7225 + 0.0625) * 10
               = sqrt(0.7850) * 10
               ≈ 8,86 km

distancia_ajustada = 8,86 * 1,08 ≈ 9,57 km
```

### Linha de execução (com interrupção)

1. O jogador avança `3 km` sem eventos.
2. No teste do 4º km ocorre interrupção (ex.: emboscada arcana).
3. `1d100 = 40` → interrupção após `0,4 km` desse trecho.
4. Distância já percorrida: `3,4 km`.
5. Distância restante: `9,57 - 3,4 = 6,17 km`.

Tempo até a interrupção:

```text
minutos = (3,4 / 4,0) * 60 = 51 min
```

Aplicação fisiológica no ponto da interrupção (exemplo):

- `fadiga_atual +14`
- `sede_pct +1,8`
- `fome_pct +0,5`

Após resolver o evento, o jogador continua. Em seguida, entra em faixa de sede que reduz velocidade para `3,6 km/h`. O restante da rota passa a consumir mais tempo por km, elevando também o desgaste.

## Checklist de implementação

- [ ] Validar contrato de `Conexão` antes de iniciar deslocamento.
- [ ] Resolver destino por prioridade (`destinos_priorizados`).
- [ ] Processar interrupções por trecho percorrido.
- [ ] Atualizar tempo e fisiologia em cada trecho.
- [ ] Recalcular velocidade quando estado fisiológico mudar.
