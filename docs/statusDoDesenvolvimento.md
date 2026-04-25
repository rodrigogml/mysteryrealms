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
> Regra de governança documental: referências nominais a "iteração" devem ficar **apenas neste arquivo de controle**; páginas mecânicas, índice e guias devem usar termos neutros (ex.: ciclo de trabalho).

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
- Tipos de Armas (taxonomia mecânica, impacto sistêmico e matriz quantitativa baseline por faixa).
- Itens de Mão (slots, categorias, combinação/troca e matriz quantitativa baseline de penalidades/exceções por faixa).
- Raças e Classes (recorte mecânico com proficiências e afinidades por tipo/tag de equipamento).
- Progressão do Jogo (curva de XP, faixas de poder, marcos e teto operacional de acúmulo).

### Em andamento

- Refinamento quantitativo do eixo personagens + progressão (classe individual e marcos finais ainda pendentes).
- Consolidação de tabelas de teto por faixa para fechar rastreabilidade de requisito funcional.

### Pendente prioritário

1. Refinamento quantitativo de Raças e Classes (bônus/penalidades por tag e proficiências por classe individual).
2. Consolidação numérica da Progressão do Jogo (tetos por faixa, custos de versatilidade e marcos por classe).
3. Validação por simulação da matriz de Tipos de Armas (calibragem F1–F4 e sensibilidade de crítico/custo).
4. Validação por simulação da matriz de Itens de Mão (penalidades/exceções e desempenho de builds de referência).

### Próxima ação recomendada (para agente sem contexto)

Iniciar por **Refinamento quantitativo de Raças e Classes**, detalhando a matriz por classe individual e alinhando com os custos já definidos em Tipos de Armas/Itens de Mão.

---

## Loop de desenvolvimento entre agentes (workflow contínuo)

Em cada iteração, o agente deve seguir este ciclo:

1. **Ler contexto mínimo obrigatório** (`AGENTS.md`, `guideline.md`, `docs/guide.md`, este status e `docs/index.md`).
2. **Escolher 1 incremento pequeno** alinhado ao pendente prioritário.
3. **Executar e integrar** (página mecânica + atualização de índice/links).
4. **Limpar legado revisado**: todo conteúdo migrado deve ser removido do arquivo `docs/legado/*.md` correspondente (preservando `*.wiki`); se o `*.md` ficar sem conteúdo remanescente, ele deve ser excluído.
5. **Validar coerência local** (termos, fórmulas, seções de pronto para requisito).
6. **Registrar handoff** (conclusão + próxima ação recomendada + riscos).

Objetivo do loop: garantir progresso incremental sem depender de memória externa ou conversa entre agentes.

### Início rápido (sem contexto)

1. Ler `AGENTS.md` e `docs/guide.md`.
2. Executar o próximo item de “Pendente prioritário”.
3. Atualizar este arquivo com handoff ao finalizar.

---

## Registro de iterações (handoff obrigatório)

> Formato: adicionar sempre no topo (mais recente primeiro).

- **Iteração:** 011
  **Concluído:** Revisão detalhada, por seção/tópico, dos arquivos legados de mecânica já migrados (`cicloDeBatalha.md`, `danosEAflicoes.md`, `defesaEBloqueio.md`, `tiposDeResistencias.md`, `modificadores.md`, `moral.md`, `racasEClasses.md`). Como todas as seções desses arquivos já constam na documentação mecânica atual, os `.md` foram removidos, mantendo os equivalentes `.wiki` como referência histórica. Também foram atualizados os links do índice e referências cruzadas para apontar para `.wiki`.
  **Próxima ação recomendada:** Repetir o mesmo processo de revisão seção-a-seção nos próximos legados de mecânica parcialmente migrados (prioridade: `armas.md`, `recuperacao.md`, `acampamentos.md`), removendo apenas blocos comprovadamente já refletidos na mecânica atual.
  **Riscos/Bloqueios:** Alguns legados têm granularidade mais narrativa que a documentação mecânica atual; em caso de dúvida, manter o bloco até consolidar equivalência operacional explícita.

- **Iteração:** 010
  **Concluído:** Regra do loop atualizada para limpeza obrigatória de `docs/legado/*.md` após migração, e aplicação imediata da regra com remoção dos arquivos legados `.md` já totalmente migrados nesta fase (`racas.md`, `classes.md`, `progressaoDoJogo.md`, `itensDeMao.md`, `tiposDeArmas.md`), mantendo os equivalentes `.wiki` como referência histórica.
  **Próxima ação recomendada:** Seguir com o refinamento quantitativo de **Raças e Classes** (classe individual), já considerando a nova regra de limpeza contínua do legado em cada ciclo.
  **Riscos/Bloqueios:** Mudanças de links para `.wiki` no legado exigem atenção em navegação histórica; validar periodicamente consistência dos caminhos no índice principal.

- **Iteração:** 009
  **Concluído:** Refinamento quantitativo de **Itens de Mão** em `docs/mecanicaDoJogo/equipamentos/itensDeMao.md`, com matriz baseline de penalidades/exceções por faixa e builds de referência para validação sistêmica.
  **Próxima ação recomendada:** Executar o refinamento quantitativo de **Raças e Classes** no nível de classe individual para fechar o elo entre afinidade, proficiência e custo tático.
  **Riscos/Bloqueios:** Valores ainda não foram calibrados por simulação; risco de ajuste em cadeia após validação cruzada com Tipos de Armas e Progressão.

- **Iteração:** 008
  **Concluído:** Refinamento quantitativo de **Tipos de Armas** em `docs/mecanicaDoJogo/equipamentos/tiposDeArmas.md`, incluindo matriz numérica baseline (acerto/dano/crítico/custo), afinidade por grupo de classe em F1/F2+ e regra de progressão para limitar salto de poder.
  **Próxima ação recomendada:** Executar o refinamento quantitativo de **Itens de Mão** para alinhar penalidades e combinações ao custo por tipo de arma e aos marcos de faixa.
  **Riscos/Bloqueios:** A calibração ainda não foi validada em simulação; valores atuais devem ser tratados como baseline sujeito a ajuste após testes de sensibilidade.

- **Iteração:** 007
  **Concluído:** Tópico **Progressão do Jogo** migrado para `docs/mecanicaDoJogo/progressao/progressaoDoJogo.md`, com curva de XP canônica, faixas de poder (F1–F4), marcos de desbloqueio, teto de acúmulo por faixa e protocolo de proficiência/especialização/maestria integrado a Raças/Classes e equipamentos.
  **Próxima ação recomendada:** Executar o **Refinamento quantitativo de Tipos de Armas** já ancorado por faixa de progressão, fechando parâmetros numéricos de acerto/dano/crítico/custo por tipo.
  **Riscos/Bloqueios:** Os tetos e marcos ainda estão em formato qualitativo em pontos-chave (baixo/médio/alto), exigindo conversão numérica antes da formalização final de requisitos.

- **Iteração:** 006
  **Concluído:** Tópico **Raças e Classes** migrado para `docs/mecanicaDoJogo/personagens/racasEClasses.md`, com definição operacional por camadas (origem/função), regras de combinação, matriz baseline de proficiências/afinidades e integração explícita com Tipos de Armas, Itens de Mão e Progressão.
  **Próxima ação recomendada:** Abrir o tópico **Progressão do Jogo** para formalizar faixas de poder, tetos de acúmulo e gatilhos de especialização conectados à matriz de Raças/Classes.
  **Riscos/Bloqueios:** As tabelas numéricas ainda não foram fechadas (afinidade por tag, penalidade por ausência de proficiência e custo de especialização), o que pode gerar rebalanço em cadeia nos tópicos de equipamentos.

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
