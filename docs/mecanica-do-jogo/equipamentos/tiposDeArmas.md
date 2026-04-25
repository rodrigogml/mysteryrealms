# Tipos de Armas

## Objetivo Mecânico

Definir uma **taxonomia operacional de tipos de armas** para padronizar regras de acerto, dano, alcance, custo de ação e integração com Classe/Raça sem depender de exceções por item individual.

Este documento transforma a referência legada em um modelo mecânico único para uso em combate e progressão.

---

## Definição Operacional

- **Tipo de Arma** é uma categoria mecânica (não narrativa) que agrupa armas com comportamento semelhante.
- Atributos de item (nome, raridade, lore, fabricante) **não** definem o tipo por si só.
- Cada arma deve apontar para **1 tipo primário**.
- Tipos existem para:
  - reduzir ambiguidade em bônus de Classe/Raça;
  - estabilizar balanceamento por papel tático;
  - permitir expansão de arsenal sem reabrir fórmulas base.

---

## Estrutura Canônica de um Tipo de Arma

Cada tipo deve declarar, no mínimo:

1. **Papel Tático Primário** (pressão, controle, execução, suporte ofensivo).
2. **Faixa de Alcance** (`adjacente`, `curto`, `medio`, `longo`).
3. **Perfil de Dano Base** (`baixo`, `medio`, `alto`) e variância esperada.
4. **Mãos Requeridas** (`1`, `2`, `versatil`).
5. **Atributo de Ataque Preferencial** (Força, Destreza, Intelecto etc.).
6. **Janela Crítica Padrão** (`faixa` e `multiplicador`).
7. **Tags Mecânicas** (ex.: `leve`, `pesada`, `discreta`, `perfurante`, `canalizadora`).

> Se um tipo não preencher os 7 campos, ele não está pronto para virar requisito.

---

## Catálogo Inicial (Baseline da Fase)

### 1) Corpo a Corpo Leve

- Papel: mobilidade e execução rápida.
- Alcance: `adjacente`.
- Dano: baixo a médio.
- Mãos: `1`.
- Atributo preferencial: Destreza.
- Crítico padrão: 19-20/x2.
- Tags comuns: `leve`, `discreta`.

### 2) Corpo a Corpo Pesada

- Papel: pressão e ruptura de defesa.
- Alcance: `adjacente`.
- Dano: médio a alto.
- Mãos: `2` (ou `versatil` em casos específicos).
- Atributo preferencial: Força.
- Crítico padrão: 20/x3.
- Tags comuns: `pesada`, `impacto`.

### 3) Haste/Alcance Estendido

- Papel: controle de espaço e punição de aproximação.
- Alcance: `adjacente` + extensão curta.
- Dano: médio.
- Mãos: `2`.
- Atributo preferencial: Força ou Destreza (definido pelo item).
- Crítico padrão: 20/x2.
- Tags comuns: `alcance`, `controle`.

### 4) Distância de Projétil

- Papel: pressão a média/longa distância.
- Alcance: `medio` a `longo`.
- Dano: médio.
- Mãos: `2` (padrão), com exceções de `1` para modelos leves.
- Atributo preferencial: Destreza.
- Crítico padrão: 20/x2.
- Tags comuns: `municao`, `precisao`.

### 5) Foco Arcano

- Papel: entrega de efeitos mágicos ofensivos.
- Alcance: variável por habilidade.
- Dano: variável por escala mágica.
- Mãos: `1` ou `2`.
- Atributo preferencial: Intelecto (ou atributo mágico do sistema).
- Crítico padrão: definido pela habilidade, não pelo foco.
- Tags comuns: `canalizadora`, `arcana`.

---

## Regras de Classificação e Conflito

1. **Regra do comportamento dominante:** tipo é decidido pelo padrão de uso mais frequente da arma, não por exceções ocasionais.
2. **Regra anti-duplicidade:** uma arma não recebe dois tipos primários ao mesmo tempo.
3. **Regra de variação local:** ambientações diferentes podem renomear exemplos de armas, mas o tipo mecânico deve permanecer equivalente.
4. **Regra de migração:** se uma arma mudar de tipo por balanceamento, ela deve ser revalidada em Classe/Raça, Itens de Mão e Progressão.

---

## Impacto Sistêmico

### Integração com Fórmulas de Combate

- Tipo de arma pode alterar:
  - modificador de acerto situacional;
  - faixa de dano base por categoria;
  - chance/multiplicador crítico padrão.
- Nenhum tipo substitui as fórmulas canônicas de `formulasDeCombate`; ele apenas injeta parâmetros.

### Integração com Modificadores e Estados

- Tags de tipo alimentam gatilhos de modificadores (ex.: bônus com `leve`, penalidade para `pesada` sob `exausto`).
- Estados que limitam mobilidade devem afetar mais tipos dependentes de reposicionamento.

### Integração com Itens de Mão

- `Itens de Mão` usará o tipo de arma como chave para validar empunhadura, combinação com escudo/foco e custo de troca em turno.

### Integração com Raças e Classes

- Afinidade é aplicada por tipo de arma (e não por arma individual) para evitar explosão de tabela.

### Integração com Progressão

- Marcos de progressão destravam proficiência e especialização por tipo, preservando curva de poder previsível.

---

## Limites e Casos de Borda

- **Arma improvisada:** usa tipo temporário `improvisada`, sempre com penalidade base e sem afinidade de classe/raca por padrão.
- **Arma híbrida:** deve escolher um modo ativo por ação; cada modo referencia um tipo diferente, mas nunca simultâneo.
- **Arma viva/simbiótica:** permitida como variação de tipo existente; só vira tipo novo se alterar pelo menos 2 eixos da estrutura canônica.
- **Sem proficiência no tipo:** aplica penalidade de uso e bloqueia efeitos avançados do tipo.

---

## Pronto para Requisito

Para este tópico ser considerado pronto para requisito funcional, ainda falta:

1. Consolidar tabela numérica oficial de parâmetros por tipo (acerto, dano, crítico, custo de ação).
2. Fechar matriz de afinidade Classe/Raça por tipo de arma.
3. Definir lista mínima de tipos obrigatórios por cenário (fantasia, sci-fi, híbrido).
4. Formalizar protocolo de criação de novos tipos sem quebrar baseline.
5. Vincular o tópico a `Itens de Mão` com exemplos completos de combinações válidas.

