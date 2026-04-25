# Status do Desenvolvimento

## Objetivo deste documento

Este arquivo é a referência principal para acompanhar o **andamento do projeto**.

Ele consolida:
- fase atual;
- entregas concluídas;
- pendências priorizadas;
- critério de transição entre fases;
- riscos e decisões em aberto.

> Regra: sempre atualizar este arquivo ao concluir migrações relevantes de tópicos mecânicos.

Este documento também funciona como **canal de continuidade entre agentes autônomos**.

---

## Visão geral do projeto

- **Objetivo final:** implementação completa do jogo com base em requisitos claros e testáveis.
- **Fase atual:** consolidação da mecânica (sem implementação de código).
- **Próxima macrofase:** formalização de requisitos funcionais.

---

## Progresso da fase atual

### Concluído

- Estrutura de documentação unificada com índice único.
- Glossário mecânico base.
- Ciclo de combate.
- Fórmulas canônicas de combate.
- Resistências, modificadores, aflições e estados.
- Metas de balanceamento (baseline).
- Moral.
- Recursos vitais e recuperação.
- Descanso e acampamento.
- Tipos de Armas (taxonomia mecânica e impacto sistêmico).
- Itens de Mão (slots, categorias, regras de uso, combinação e troca por turno).

### Em andamento

- Refinamento de cobertura de tópicos legados fora do núcleo de combate.
- Integração do eixo de equipamentos (Tipos de Armas e Itens de Mão concluídos; refinamento quantitativo em andamento).

### Pendente prioritário

1. Raças e Classes (recorte mecânico separado da narrativa).
2. Progressão do Jogo (curva de poder e marcos por faixa).
3. Refinamento quantitativo de Tipos de Armas (matriz numérica e afinidades).
4. Refinamento quantitativo de Itens de Mão (penalidades, exceções e builds de referência).

### Próxima ação recomendada (para agente sem contexto)

Iniciar por **Raças e Classes** (recorte mecânico), aproveitando o eixo de equipamentos já consolidado para:
- definir proficiências e afinidades por tipo/subtipo de equipamento;
- mapear restrições mecânicas sem narrativa obrigatória;
- preparar a matriz de progressão por faixas de poder.

---

## Loop de desenvolvimento entre agentes (workflow contínuo)

Em cada iteração, o agente deve seguir este ciclo:

1. **Ler contexto mínimo obrigatório** (`AGENTS.md`, `guideline.md`, `docs/guide.md`, este status e `docs/index.md`).
2. **Escolher 1 incremento pequeno** alinhado ao pendente prioritário.
3. **Executar e integrar** (página mecânica + atualização de índice/links).
4. **Validar coerência local** (termos, fórmulas, seções de pronto para requisito).
5. **Registrar handoff** (conclusão + próxima ação recomendada + riscos).

Objetivo do loop: garantir progresso incremental sem depender de memória externa ou conversa entre agentes.

### Início rápido (sem contexto)

1. Ler `AGENTS.md` e `docs/guide.md`.
2. Executar o próximo item de “Pendente prioritário”.
3. Atualizar este arquivo com handoff ao finalizar.

---

## Registro de iterações (handoff obrigatório)

> Formato: adicionar sempre no topo (mais recente primeiro).

- **Iteração:** 005
  **Concluído:** Renomeação estrutural da pasta de mecânica revisada para `docs/mecanicaDoJogo/` (padrão camelCase) com atualização das referências cruzadas em índice, guia, status e instruções operacionais.
  **Próxima ação recomendada:** Retomar o pendente prioritário em **Raças e Classes** mantendo os novos caminhos em camelCase nas futuras páginas mecânicas.
  **Riscos/Bloqueios:** Revisão sistêmica executada nesta iteração (coerência global entre `docs/index.md`, `docs/guide.md`, `docs/statusDoDesenvolvimento.md` e páginas mecânicas); não foram identificados conflitos de terminologia/fórmula, mas links externos antigos para `docs/mecanica-do-jogo/` podem quebrar fora do repositório.

- **Iteração:** 004
  **Concluído:** Tópico **Itens de Mão** migrado para `docs/mecanicaDoJogo/equipamentos/itensDeMao.md`, com definição operacional de slots, subtipos, matriz de combinação, economia de ações para troca e integração com Tipos de Armas/Ciclo de Combate.
  **Próxima ação recomendada:** Iniciar **Raças e Classes** com foco em proficiências e afinidades mecânicas por subtipo/tag de equipamento.
  **Riscos/Bloqueios:** Tabelas numéricas de penalidade (dupla empunhadura e falta de proficiência) ainda estão pendentes e podem impactar balanceamento posterior.

- **Iteração:** 003
  **Concluído:** Revisão ampliada de acentuação em arquivos `.md` do repositório com validação de consistência textual; não foram encontrados novos blocos críticos de ortografia fora dos ajustes já aplicados.
  **Próxima ação recomendada:** Retomar o fluxo mecânico em **Itens de Mão**, aproveitando a base de Tipos de Armas e o índice atualizado.
  **Riscos/Bloqueios:** Há divergências pontuais em âncoras históricas do legado (slug sem acento), que devem ser preservadas para não quebrar links antigos.

- **Iteração:** 002
  **Concluído:** Revisão de acentuação textual aplicada na documentação mecânica ativa e no status/índice, mantendo a regra de nomes de arquivo sem acentos apenas para paths.
  **Próxima ação recomendada:** Seguir para migração de **Itens de Mão** com integração direta aos tipos de arma já formalizados.
  **Riscos/Bloqueios:** Ainda há páginas legadas extensas com estilo textual heterogêneo; revisar acentuação completa do legado pode exigir um ciclo dedicado sem avanço mecânico.

- **Iteração:** 001
  **Concluído:** Tópico **Tipos de Armas** migrado para `docs/mecanicaDoJogo/equipamentos/tiposDeArmas.md`, com taxonomia mecânica, regras de classificação, impacto sistêmico e seção de pronto para requisito.
  **Próxima ação recomendada:** Migrar **Itens de Mão** conectando slots, combinações válidas e regras de conflito com o tipo de arma.
  **Riscos/Bloqueios:** Ainda faltam tabelas numéricas oficiais (acerto/dano/crítico por tipo) e matriz de afinidade Classe/Raça para fechar requisito funcional.

- **Iteração:** 000
  **Concluído:** Estrutura inicial do loop definida.
  **Próxima ação recomendada:** Migrar tópico *Tipos de Armas* para mecânica revisada com seção de pronto para requisito.
  **Riscos/Bloqueios:** Nenhum no momento.

---

## Critério de "pronto" por tópico migrado

Um tópico só é considerado pronto para requisito quando tiver:

- definição operacional objetiva;
- regras/fórmulas e ordem de resolução (quando aplicável);
- casos-limite e limites mecânicos;
- integração com sistemas já definidos;
- seção de "Pronto para Requisito" com pendências explícitas.

---

## Critério para encerrar a fase atual

A fase de consolidação mecânica será considerada concluída quando:

1. Tópicos prioritários estiverem migrados com critério de pronto atendido.
2. Ambiguidades críticas entre páginas forem removidas.
3. Houver rastreabilidade clara para conversão em requisitos.

Ao atingir os 3 critérios, o agente deve atualizar este documento com status de **pronto para transição de fase**.

---

## Riscos e atenções

- Crescimento de regras sem normalização comum (risco de inconsistência).
- Conflitos entre fórmulas de páginas diferentes.
- Introdução de detalhes de implementação antes da fase correta.

Mitigação:
- manter atualização disciplinada do índice e deste status;
- revisar dependência entre páginas ao final de cada migração;
- registrar decisões mecânicas explicitamente.

Checkpoint global:
- a cada 5 iterações, executar revisão sistêmica do conjunto documental e registrar no handoff.

---

## Próximas fases (macro)

1. Formalização de requisitos.
2. Planejamento técnico.
3. Implementação incremental.
4. Validação e balanceamento contínuo.

Este arquivo não detalha execução técnica das fases futuras; apenas o estado e direção do desenvolvimento.
