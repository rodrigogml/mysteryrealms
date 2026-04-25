# Itens de Mão

## Objetivo Mecânico

Definir o sistema operacional de **Itens de Mão** para validar o que cada personagem pode empunhar por turno, quais combinações são permitidas e quais efeitos sistêmicos cada configuração ativa.

Este tópico transforma o legado em regras objetivas e integra diretamente combate, tipos de arma, defesa e progressão.

---

## Definição Operacional

- **Item de Mão** é qualquer item equipado em um dos slots `maoPrincipal` e/ou `maoSecundaria`.
- Cada personagem possui **2 slots de mão simultâneos**.
- Todo item de mão deve declarar:
  1. `subtipo`;
  2. `maosNecessarias` (`1` ou `2`);
  3. `categoriaDeUsoPrimaria`.
- Itens de `2` mãos ocupam ambos os slots enquanto estiverem ativos.
- Nenhum efeito de item de mão é aplicado se os requisitos mínimos de uso não forem atendidos.

---

## Estrutura Canônica de um Item de Mão

Cada item de mão deve possuir, no mínimo:

1. **Subtipo** (`arma`, `escudo`, `focoMagico`, `reliquia`, `lanterna`, `ferramenta`, `instrumento`, `consumivelRapido`, `especial`).
2. **Mãos Necessárias** (`1` ou `2`).
3. **Categoria de Uso Primária** (`ataque`, `defesa`, `suporte`, `iluminacao`, `manipulacao`, `interacaoNarrativa`).
4. **Categoria de Uso Secundária** (opcional, uma única).
5. **Requisitos de Uso** (atributos, proficiência, classe/raça quando aplicável).
6. **Tags Mecânicas** (ex.: `leve`, `pesado`, `canalizador`, `fragil`, `rapido`).
7. **Regra de Ativação** (passivo em empunhadura, ação principal, reação, consumo).

> Se faltar qualquer campo obrigatório (1 a 7), o item não está pronto para requisito.

---

## Subtipos e Efeitos Mecânicos Base

### Arma

- Primário: `ataque`.
- Deve referenciar exatamente 1 tipo em `Tipos de Armas`.
- Efeito base: habilita ações ofensivas e usa parâmetros de acerto/dano do tipo.

### Escudo

- Primário: `defesa`.
- Efeito base: concede bloqueio passivo e pode habilitar reação defensiva (se declarado).

### Foco Mágico

- Primário: `suporte`.
- Efeito base: habilita canalização para habilidades mágicas e modifica custo/precisão conforme habilidade.

### Ferramenta

- Primário: `manipulacao`.
- Efeito base: habilita testes de perícia contextual; sem ação ofensiva por padrão.

### Lanterna

- Primário: `iluminacao`.
- Efeito base: altera condição de visibilidade em exploração/combate noturno.

### Relíquia / Instrumento / Especial

- Primário: definido por ficha.
- Efeito base: deve declarar gatilho explícito e limite de uso por cena/combate quando aplicável.

---

## Regras de Combinação e Conflito

### Matriz de Empunhadura

| Configuração | Válida | Observação |
|---|---|---|
| `arma(1M)` + `vazio` | Sim | configuração básica de ataque. |
| `arma(1M)` + `escudo(1M)` | Sim | ativa bloqueio do escudo e mantém ataque principal. |
| `arma(1M)` + `focoMagico(1M)` | Condicional | exige que habilidade permita arma + foco. |
| `arma(1M)` + `arma(1M)` | Condicional | exige proficiência de dupla empunhadura. |
| `arma(2M)` + qualquer item | Não | item de 2 mãos bloqueia a segunda mão. |
| `escudo(1M)` + `escudo(1M)` | Não | bloqueado por regra anti-duplicidade defensiva. |
| `ferramenta(1M)` + `escudo(1M)` | Condicional | permitido fora de combate; em combate depende de ação compatível. |

### Regras Gerais

1. **Regra do bloqueio de slot:** itens de `2` mãos sempre prevalecem e removem item ativo da outra mão.
2. **Regra anti-duplicidade:** não é permitido acumular dois itens com mesmo efeito passivo principal, salvo exceção explícita.
3. **Regra de compatibilidade de ação:** combinação válida em slot pode ser inválida para uma ação específica.
4. **Regra de requisito dominante:** se um item na combinação não cumprir requisito, apenas os efeitos desse item são suprimidos.

---

## Troca de Itens e Economia de Ações

- Cada turno permite **1 troca simples** na etapa de **Ação de Pré-Turno**.
- **Troca simples:** sacar, guardar ou substituir **1 item de 1 mão**.
- Itens de `2` mãos consomem a troca completa (equipar ou desequipar).
- Segunda troca no mesmo turno só ocorre por habilidade/modificador que conceda exceção.
- Troca interrompida por estado incapacitante falha e não gera efeito parcial.

---

## Matriz Quantitativa de Penalidades e Exceções

Escala desta fase:
- penalidades em `acerto`, `dano` e `bloqueio` são aditivas;
- custo adicional de troca é expresso em fração de ação (`0.5`, `1.0`);
- valores são baseline para calibração posterior por simulação.

### Penalidades de combinação

| Situação | Penalidade de acerto | Penalidade de dano | Penalidade de bloqueio | Observação |
|---|---:|---:|---:|---|
| Dupla empunhadura sem proficiência | -3 | -1 | 0 | aplica na arma secundária e em reações ofensivas derivadas |
| Dupla empunhadura com proficiência | -1 | 0 | 0 | mantém custo de execução tática |
| Arma (1M) + Foco sem habilidade compatível | -2 | -1 | 0 | foco permanece passivo, sem canalização ativa |
| Uso de item sem proficiência de subtipo | -2 | -2 | -1 | efeitos avançados do item ficam bloqueados |
| Mão secundária lesionada com item ativo | -2 | -1 | -2 | apenas ações de baixa complexidade são permitidas |

### Exceções por faixa de progressão

| Faixa | Exceção liberada | Impacto |
|---|---|---|
| F1 | nenhuma exceção estrutural | mantém custo integral das combinações |
| F2 | redução de 1 ponto na penalidade de dupla empunhadura | abre builds de combate ágil |
| F3 | permite `arma(1M)+foco(1M)` sem penalidade de dano quando houver especialização | fortalece híbridos de combate/magia |
| F4 | segunda troca condicional por turno (1x por combate) | aumenta flexibilidade sem remover custo base |

Regra de teto:
- nenhuma exceção de item de mão pode reduzir simultaneamente penalidade e custo a ponto de anular trade-off de configuração.

---

## Builds de Referência por Faixa

| Build | Configuração | Faixa mínima | Vantagem principal | Trade-off principal |
|---|---|---|---|---|
| Sentinela | `arma(1M)+escudo(1M)` | F1 | estabilidade defensiva | menor pico de dano |
| Duelista Ágil | `arma(1M)+arma(1M)` | F2 | volume ofensivo por turno | penalidade de precisão residual |
| Arcano Tático | `arma(1M)+foco(1M)` | F3 | pressão híbrida com utilidade | dependência de especialização |
| Especialista de Campo | `ferramenta(1M)+lanterna(1M)` | F1 | controle situacional e exploração | baixa resposta ofensiva direta |

Critério de uso de build de referência:
1. Build não substitui ficha de item; funciona como cenário de validação sistêmica.
2. Cada build deve ser testado contra metas de duração de combate e consumo de recursos.

---

## Integração com Outros Sistemas

### Tipos de Armas

- O subtipo `arma` depende de `tiposDeArmas` para parâmetros táticos e afinidades.

### Ciclo de Combate

- Troca de item ocorre no pré-turno e respeita limite de ações por turno.

### Fórmulas de Combate e Defesa/Bloqueio

- Itens de mão não substituem fórmulas; apenas injetam parâmetros (acerto, dano, bloqueio, canalização).

### Raças e Classes

- Afinidades e proficiências devem ser aplicadas por subtipo/tag, não por item individual, para evitar explosão de regras.

### Progressão do Jogo

- Marcos de progressão destravam combinações (ex.: dupla empunhadura) e mitigam penalidades de subtipos avançados.

---

## Limites e Casos de Borda

- **Mão lesionada/inutilizada:** personagem opera com 1 slot ativo até remoção do estado.
- **Item improvisado de mão:** usa subtipo `especial` com penalidade base e sem afinidade por padrão.
- **Combinação narrativa exótica:** só é válida quando convertida para subtipo + tags + regra de ativação.
- **Empunhadura fora de combate:** pode permitir combinações bloqueadas em combate, desde que sem ganho mecânico de ataque/defesa.

---

## Pronto para Requisito

Para este tópico ser considerado pronto para requisito funcional, ainda falta:

1. Validar em simulação a matriz quantitativa de penalidades/exceções por faixa (F1–F4).
2. Definir catálogo mínimo de escudos, focos e ferramentas por faixa de progressão.
3. Especificar protocolo de interação entre `consumivelRapido` e troca simples no mesmo turno.
4. Converter builds de referência em cenários de teste formal com métricas de sucesso/falha.

