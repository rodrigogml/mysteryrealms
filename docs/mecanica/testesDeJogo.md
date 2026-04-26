# Testes de Jogo

Este documento padroniza os testes mecânicos com base no glossário canônico (`docs/mecanica/glossario.md`).

## Fórmula do teste

Regra base:

`resultado_teste = valor_base + 1d20 + modificadores`

Onde:
- `valor_base` é a **habilidade canônica** (ex.: `persuasao`, `furtividade`) quando existir;
- caso não exista habilidade adequada, usa-se o **atributo canônico** (ex.: `forca`, `intelecto`, `percepcao`);
- `modificadores` reúne bônus/penalidades temporários.

## Uso de habilidade vs atributo

- Se houver habilidade específica para a ação, use habilidade.
- Se não houver, use o atributo principal coerente com a ação.

Exemplos canônicos:
- Convencer NPC: `persuasao`.
- Abrir fechadura sem habilidade específica cadastrada: `destreza`.
- Resolver enigma técnico: `intelecto`.

## CD (Classe de Dificuldade)

A CD representa o mínimo para sucesso e pode variar por:
- complexidade da tarefa;
- condições de ambiente;
- resistência do alvo;
- urgência da ação.

Resultado:
- `resultado_teste >= cd` → sucesso.
- `resultado_teste < cd` → falha.

### Tabela de referência de dificuldade

| Tier de progresso | Muito Fácil | Fácil | Médio | Difícil | Muito Difícil | Heroico |
|---|---:|---:|---:|---:|---:|---:|
| Inicial | 8 | 10 | 12 | 14 | 16 | 18 |
| Intermediário | 10 | 12 | 14 | 16 | 18 | 20 |
| Avançado | 12 | 14 | 16 | 18 | 20 | 22 |

> Use a tabela como ponto de partida. Ajuste em ±2 quando fatores situacionais forem relevantes (tempo, visibilidade, equipamento, pressão narrativa).

## Vantagem e desvantagem

- **Vantagem:** rola `2d20`, mantém o maior.
- **Desvantagem:** rola `2d20`, mantém o menor.

## Orientação de design

### Taxa-alvo de sucesso em ações centrais

Para ações centrais (o que o jogo espera que os personagens tentem com frequência), vise uma taxa de sucesso aproximada de:
- **Inicial:** 65%–75%
- **Intermediário:** 60%–70%
- **Avançado:** 55%–65%

Se a taxa estiver muito abaixo disso, a experiência tende a parecer punitiva; se estiver muito acima, o desafio pode perder tensão.

### Como vantagem/desvantagem altera expectativa

Sem alterar modificadores, vantagem/desvantagem desloca a chance de sucesso de forma relevante:
- **Vantagem:** melhora a probabilidade especialmente em CDs próximas ao valor médio do personagem.
- **Desvantagem:** reduz na mesma ordem de grandeza.

Regra prática para calibragem rápida:
- trate **vantagem** como algo próximo de **+3 a +5 efetivo**;
- trate **desvantagem** como algo próximo de **-3 a -5 efetivo**.

Para manter consistência, evite empilhar muitos bônus numéricos com vantagem/desvantagem no mesmo teste, salvo em momentos heroicos deliberados.

### Quando usar CD fixa versus teste oposto

Prefira **CD fixa** quando:
- o desafio vem do ambiente, objeto ou processo (escalar muro, decifrar mecanismo, atravessar terreno);
- você quer ritmo rápido e resolução previsível;
- não há agente ativo reagindo em tempo real.

Prefira **teste oposto** quando:
- há disputa direta entre dois agentes (enganar vs perceber, agarrar vs escapar);
- ambos estão ativamente influenciando o resultado;
- a cena ganha dramaticidade com variação de desempenho dos dois lados.

## Teste de Percepção

Usa atributo/habilidade de percepção canônica:
- Atributo: `percepcao`
- Habilidade oposta comum: `furtividade`

### Percepção vs Furtividade

- Observador: `percepcao + 1d20`
- Oculto: `furtividade + 1d20`
- Maior resultado detecta primeiro.

### Percepção vs dificuldade fixa do ambiente

- Observador: `percepcao + 1d20`
- Alvo: `cd_ambiente`

## Teste de Iniciativa

Fórmula canônica:

`iniciativa = 1d20 + destreza + percepcao`

Desempate:
1. maior `destreza`;
2. maior `percepcao`;
3. sorteio.
