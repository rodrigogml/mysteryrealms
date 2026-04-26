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

## Vantagem e desvantagem

- **Vantagem:** rola `2d20`, mantém o maior.
- **Desvantagem:** rola `2d20`, mantém o menor.

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
