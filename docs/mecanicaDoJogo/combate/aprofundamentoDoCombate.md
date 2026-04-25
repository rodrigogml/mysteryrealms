# Aprofundamento do Combate

Este documento define o que ainda falta para encerrar o tópico de combate em nível de requisitos funcionais.

## O que ainda vale melhorar neste tópico

### 1) Janela temporal de reações
- Definir com precisão **quando** reações podem acontecer dentro do turno:
  - antes do teste de acerto;
  - após acerto e antes do dano;
  - após dano aplicado.
- Definir prioridade quando houver múltiplas reações válidas simultâneas.

### 2) Catálogo formal de estados
- Converter os estados base em tabela canônica com colunas obrigatórias:
  - ID técnico;
  - categoria;
  - duração;
  - empilhamento;
  - removibilidade;
  - janela de processamento.
- Definir regras de interação entre controle e deslocamento (ex.: Atordoado + Empurrado).

### 3) Mitigação e anti-invulnerabilidade
- Separar limites de mitigação por origem:
  - defesa (evita acerto),
  - bloqueio (reduz dano bruto),
  - resistência (reduz dano por tipo).
- Definir teto por camada e teto total por turno para evitar combinações dominantes.

### 4) Instrumentação para balanceamento
- Definir métricas mínimas que precisam ser registradas por combate:
  - rodadas totais;
  - dano total por participante;
  - consumo de recursos;
  - taxa de aplicação de aflições;
  - taxa de vitória por faixa de poder.
- Definir volume mínimo de amostra para validar ajustes (ex.: 100 combates por faixa).

## Critério de “pronto” para encerrar Combate

O tópico de combate pode ser considerado pronto para virar requisito quando:

1. Todas as fórmulas tiverem ordem de resolução fechada e casos-limite documentados.
2. Todos os estados base tiverem ID, empilhamento e remoção definidos.
3. Houver matriz mínima de cenários de validação manual.
4. Metas de balanceamento estiverem vinculadas a métricas observáveis.

## Recomendação de sequência

- **Passo 1 (rápido):** concluir os 4 pontos acima no próprio tópico de combate.
- **Passo 2 (em paralelo):** iniciar migração de novos tópicos legados mais acoplados ao combate.

### Próximos tópicos recomendados (ordem)
1. **Tipos de Resistências** (`docs/legado/tiposDeResistencias.wiki`)
2. **Modificadores** (`docs/legado/modificadores.wiki`)
3. **Moral** (`docs/legado/moral.wiki`)
4. **Recuperação** (`docs/legado/recuperacao.md`)

Essa ordem reduz retrabalho, porque esses temas influenciam diretamente as decisões já tomadas em combate.
