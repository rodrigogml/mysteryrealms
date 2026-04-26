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

- Validação por simulação do refinamento quantitativo de Raças e Classes (matriz por classe individual já documentada; falta calibração).
- Consolidação numérica da Progressão do Jogo em andamento (A001, A002 e A003 concluídas em simulação baseline; falta validação empírica para confirmar decisão final).

### Pendente prioritário

1. Validar empiricamente A001/A002/A003 para confirmar (ou revisar) a decisão da rodada inicial.
2. Validar por simulação os protocolos consolidados em `mecanicaDoJogo/validacao/protocolosDeTesteEMedicao.md` (taxa de sucesso, desvio e tempo para marco).
3. Validação por simulação da matriz quantitativa de Raças e Classes (classe individual + deslocamento racial).
4. Validação por simulação da matriz de Tipos de Armas (calibragem F1–F4 e sensibilidade de crítico/custo).

### Próxima ação recomendada (para agente sem contexto)

Executar o **pacote de coleta empírica de A001** (janela mínima, composição-alvo e checklist de fechamento) e preencher o template de comparação simulação vs telemetria; depois repetir para A002/A003.

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

### Regra de granularidade mínima (antifragmentação)

Para evitar ciclos excessivamente pequenos, cada novo incremento deve cumprir **pelo menos 1 bloco fechado** entre as opções abaixo:

1. migração completa de uma seção do legado para a documentação ativa (com limpeza correspondente no legado);
2. consolidação de um protocolo inteiro (definição + métricas + critério de fechamento);
3. fechamento de um artefato operacional com handoff completo (índice, status e próximos passos).

Incrementos que apenas ajustem frases isoladas sem fechar bloco não devem ser considerados conclusão de ciclo.

### Início rápido (sem contexto)

1. Ler `AGENTS.md` e `docs/guide.md`.
2. Executar o próximo item de “Pendente prioritário”.
3. Atualizar este arquivo com handoff ao finalizar.

---

## Registro de iterações (handoff obrigatório)

> Formato: adicionar sempre no topo (mais recente primeiro).

- **Iteração:** 032
  **Concluído:** Estruturação do pacote operacional de execução empírica de `A001` em `rodadaInicialCadencia.md` (parâmetros mínimos de coleta, tolerância de composição e checklist de fechamento), reduzindo ambiguidade da primeira coleta real.
  **Próxima ação recomendada:** Rodar a coleta empírica de `A001` com no mínimo 10 sessões válidas e publicar os deltas absolutos/relativos frente ao baseline.
  **Riscos/Bloqueios:** Sem dados reais coletados, o pacote permanece apenas preparatório e não reduz o risco quantitativo da decisão final.

- **Iteração:** 031
  **Concluído:** Formalização do protocolo operacional de validação empírica em `rodadaInicialCadencia.md`, com template único para comparar simulação baseline vs telemetria real (A001/A002/A003) e regra explícita de decisão pós-coleta (`Aprovado`/`Ajuste leve`/`Reprovado`).
  **Próxima ação recomendada:** Executar a coleta empírica de `A001` (combate-dominante) e preencher o template de comparação com deltas absolutos/relativos.
  **Riscos/Bloqueios:** A ausência de telemetria real mantém a decisão da rodada em estado provisório, mesmo com protocolo de comparação fechado.

- **Iteração:** 030
  **Concluído:** Preenchimento da amostra `A003` (narrativo-investigativo) em `rodadaInicialCadencia.md`, fechando cobertura simulada dos três cenários e permitindo decisão provisória de rodada (aprovado em baseline).
  **Próxima ação recomendada:** Executar validação empírica das três amostras e confirmar/revisar a decisão final da rodada inicial.
  **Riscos/Bloqueios:** Sem telemetria empírica, a aprovação atual permanece provisória e sujeita a ajuste.
  **Revisão sistêmica:** Executada nesta iteração (coerência entre índice, guia, status e páginas de progressão/validação); sem conflitos críticos de estrutura, com pendência principal na validação empírica da cadência.

- **Iteração:** 029
  **Concluído:** Preenchimento da amostra `A002` (híbrido equilibrado) em `rodadaInicialCadencia.md`, com métricas de cadência simulada, log incremental e premissas explícitas da simulação.
  **Próxima ação recomendada:** Validar empiricamente A001/A002 e preencher A003 para fechar a rodada inicial com os três cenários.
  **Riscos/Bloqueios:** Sem validação empírica, os valores de A001/A002 seguem como referência provisória e podem divergir da telemetria real.

- **Iteração:** 028
  **Concluído:** A amostra `A001` (cenário combate-dominante) foi preenchida em `rodadaInicialCadencia.md` com valores de simulação baseline, incluindo métricas de cadência e desvio entre classes, além de premissas explícitas da simulação.
  **Próxima ação recomendada:** Validar empiricamente os valores da `A001` e iniciar `A002` (híbrido equilibrado) com o mesmo formato de coleta.
  **Riscos/Bloqueios:** Como a A001 atual é simulada, existe risco de diferença frente à telemetria real de campanha.

- **Iteração:** 027
  **Concluído:** Migração em bloco dos protocolos legados de **Conscientização**, **Sono** e **Testes Adicionais Pós-Dano** para `docs/mecanicaDoJogo/validacao/protocolosDeTesteEMedicao.md`, com remoção completa de `docs/legado/testesDeJogo.md` (arquivo esgotado) e atualização de referências do índice para `testesDeJogo.wiki`.
  **Próxima ação recomendada:** Concluir amostra `A001` e iniciar validação simulada dos protocolos consolidados (taxa de sucesso, desvio e tempo para marco).
  **Riscos/Bloqueios:** A consolidação documental está avançada, mas sem rodada simulada completa ainda há risco de discrepância entre baseline teórica e comportamento esperado.

- **Iteração:** 026
  **Concluído:** Migração em bloco dos protocolos legados de **Acerto**, **Dano** e **Fuga** para `docs/mecanicaDoJogo/validacao/protocolosDeTesteEMedicao.md`, com normalização de pipeline de resolução e critérios de saída. O conteúdo correspondente foi removido de `docs/legado/testesDeJogo.md`.
  **Próxima ação recomendada:** Concluir amostra `A001` e migrar os blocos remanescentes do legado (`Conscientização`, `Sono` e `Testes Adicionais`) para fechar o eixo de validação.
  **Riscos/Bloqueios:** Enquanto o legado remanescente não for migrado, ainda pode haver divergência de regra em cenários específicos de recuperação e pós-dano.

- **Iteração:** 025
  **Concluído:** Migração em bloco dos protocolos legados de **Percepção** e **Iniciativa** para `docs/mecanicaDoJogo/validacao/protocolosDeTesteEMedicao.md`, com normalização de fórmulas, modos de resolução, critérios de saída e integração com rodada de cadência. O conteúdo equivalente foi removido de `docs/legado/testesDeJogo.md`.
  **Próxima ação recomendada:** Concluir amostra `A001` e migrar os próximos blocos legados (`Teste de Acerto`, `Teste de Dano` e `Teste de Fuga`) para o protocolo ativo.
  **Riscos/Bloqueios:** Enquanto Acerto/Dano/Fuga permanecerem no legado, ainda existe risco de dupla interpretação entre documentos.
  **Revisão sistêmica:** Executada nesta iteração (coerência entre índice, guia, status e páginas ativas de validação/progressão); sem conflitos críticos identificados, com pendência principal na migração completa dos protocolos remanescentes.

- **Iteração:** 024
  **Concluído:** Migração em bloco do modelo base de testes do legado para `docs/mecanicaDoJogo/validacao/protocolosDeTesteEMedicao.md`, com normalização operacional (CD, vantagem/desvantagem, métricas e critérios de fechamento). O bloco equivalente foi removido de `docs/legado/testesDeJogo.md` e substituído por referência para a documentação ativa.
  **Próxima ação recomendada:** Concluir amostra `A001` da rodada inicial e migrar os próximos blocos do legado (`Teste de Percepção` e `Teste de Iniciativa`) para o protocolo ativo.
  **Riscos/Bloqueios:** Persistência de conteúdo relevante no legado ainda não migrado pode gerar divergência de interpretação entre fontes.

- **Iteração:** 023
  **Concluído:** Estruturação da primeira amostra (`A001`) na página da rodada inicial, com campos obrigatórios e regra explícita de fechamento para o cenário `combate-dominante`.
  **Próxima ação recomendada:** Preencher numericamente a amostra `A001` e, após concluir, replicar o mesmo formato para os próximos cenários.
  **Riscos/Bloqueios:** Sem fechamento de `A001`, a rodada segue sem baseline quantitativa mínima para validar cadência.

- **Iteração:** 022
  **Concluído:** Rodada inicial atualizada com status `Em coleta` para o cenário `combate-dominante` e inclusão de log incremental de execução em `rodadaInicialCadencia.md`.
  **Próxima ação recomendada:** Registrar a primeira amostra numérica completa do cenário `combate-dominante` e consolidar métricas mínimas (`sessoesParaF2`, `sessoesF2ParaF3`).
  **Riscos/Bloqueios:** Enquanto os valores permanecerem pendentes, a rodada não produz sinal útil de calibração para Progressão.

- **Iteração:** 021
  **Concluído:** Refinamento operacional da `rodadaInicialCadencia.md` com plano de coleta real por etapas, critérios de conclusão por cenário e regras de encerramento da rodada para evitar fechamento sem evidência completa.
  **Próxima ação recomendada:** Executar a coleta dos 3 cenários (um por vez), eliminando campos pendentes e registrando decisão final da rodada.
  **Riscos/Bloqueios:** Enquanto os cenários não forem preenchidos com dados reais, a validação de cadência continua sem evidência quantitativa para calibragem.

- **Iteração:** 020
  **Concluído:** Rodada inicial de cadência marcada como iniciada em `rodadaInicialCadencia.md`, com metadados preenchidos (data, responsável, versão de referência e parâmetros de curva), checklist parcialmente concluído e observações de abertura para execução real.
  **Próxima ação recomendada:** Concluir a coleta dos 3 cenários com dados numéricos e publicar decisão formal da rodada (`Aprovado`, `Ajuste leve` ou `Reprovado`).
  **Riscos/Bloqueios:** A planilha oficial de XP ainda não está congelada/versionada no repositório, o que bloqueia rastreabilidade total da rodada.
  **Revisão sistêmica:** Executada nesta iteração (coerência entre `docs/index.md`, `docs/guide.md`, `docs/statusDoDesenvolvimento.md` e páginas de Progressão/Validação); sem conflitos críticos de terminologia, com pendência principal na evidência quantitativa da simulação.

- **Iteração:** 019
  **Concluído:** Criação da página `docs/mecanicaDoJogo/progressao/rodadaInicialCadencia.md` para registrar a execução real da rodada inicial (metadados, checklist, resultados por cenário, anomalias e decisão), alinhada ao protocolo de validação já documentado.
  **Próxima ação recomendada:** Preencher a página da rodada com dados reais dos três cenários e publicar decisão (`Aprovado`, `Ajuste leve` ou `Reprovado`) com justificativa.
  **Riscos/Bloqueios:** Sem preenchimento real, o projeto permanece sem evidência quantitativa para calibragem da cadência de progressão.

- **Iteração:** 018
  **Concluído:** Criação da página `docs/mecanicaDoJogo/progressao/validacaoDeCadencia.md` com procedimento operacional da rodada inicial, template de registro de resultados e regras de decisão para aprovação/ajuste/reprovação.
  **Próxima ação recomendada:** Executar a rodada inicial com dados reais nos 3 cenários e preencher o template para obter decisão formal de cadência.
  **Riscos/Bloqueios:** Sem execução real do template, o processo segue sem evidência quantitativa para calibragem da progressão.

- **Iteração:** 017
  **Concluído:** Inclusão do protocolo de validação de cadência em `progressaoDoJogo.md`, com cenários mínimos de simulação, métricas de alerta e critério de aprovação para validar ritmo de progressão entre classes.
  **Próxima ação recomendada:** Rodar a primeira simulação de cadência com os três cenários documentados e registrar resultados comparativos por classe/faixa.
  **Riscos/Bloqueios:** Ainda não há resultado simulado; risco de os percentuais da planilha XP não refletirem a diversidade de estilos de campanha.

- **Iteração:** 016
  **Concluído:** Definição do fechamento da planilha oficial de XP por cenário em `progressaoDoJogo.md`, incluindo estrutura mínima de colunas, baseline percentual por tipo de cenário/faixa e regras de governança para versionamento e exceções.
  **Próxima ação recomendada:** Executar validação de cadência (simulada) entre recompensas de XP por cenário e marcos por classe para verificar ritmo de progressão em F2/F3/F4.
  **Riscos/Bloqueios:** Sem rodada de simulação, percentuais de recompensa podem comprimir ou alongar demais o tempo entre marcos, especialmente em campanhas com foco em diálogo/exploração.

- **Iteração:** 015
  **Concluído:** Consolidação da Progressão com mapeamento baseline de XP por marco (níveis 10, 20–24 e 40–41) e critérios de exceção para conteúdo sazonal, preservando marcos estruturais e regra de reversão. Índice atualizado com as novas âncoras.
  **Próxima ação recomendada:** Fechar a planilha oficial de XP por cenário de recompensa (combate, exploração, diálogo, quest) e iniciar validação por simulação das matrizes quantitativas já definidas.
  **Riscos/Bloqueios:** Sem planilha unificada e simulação, o baseline de XP pode divergir da cadência real de progressão percebida em jogo.
  **Revisão sistêmica:** Executada nesta iteração (coerência entre `docs/index.md`, `docs/guide.md`, `docs/statusDoDesenvolvimento.md` e páginas mecânicas de Progressão/Raças e Classes); não foram encontrados conflitos críticos de terminologia, apenas pendência de validação quantitativa cruzada.

- **Iteração:** 014
  **Concluído:** Continuidade da consolidação numérica de **Progressão do Jogo** com tabela de marcos por classe individual (níveis exatos de especialização/maestria), regra de deslocamento por versatilidade e seção de vínculo operacional entre marcos e curva de XP.
  **Próxima ação recomendada:** Integrar os marcos com a planilha de XP por classe/cenário de recompensa (combate, exploração, diálogo, quest) e fechar critérios de exceção para conteúdo sazonal.
  **Riscos/Bloqueios:** Sem validação por simulação, os intervalos de classe (20–24 e 40–41) podem gerar percepção de desigualdade de ritmo entre arquétipos; exige checagem quantitativa antes de congelar requisito.

- **Iteração:** 013
  **Concluído:** Consolidação numérica parcial de **Progressão do Jogo** em `docs/mecanicaDoJogo/progressao/progressaoDoJogo.md`, convertendo os tetos por faixa para valores oficiais (`origem`, `equipamento`, `mitigação`) e formalizando custo de versatilidade + limite de maestrias simultâneas com regras de aplicação e conflito.
  **Próxima ação recomendada:** Fechar a tabela de marcos por classe individual (nível exato de desbloqueio de especialização/maestria) e vincular os marcos à curva de XP para rastreabilidade de requisito.
  **Riscos/Bloqueios:** Os novos valores podem tensionar classes híbridas e builds defensivas em F3/F4; sem simulação, há risco de excedente condicional frequente e aumento de complexidade operacional.

- **Iteração:** 012
  **Concluído:** Refinamento quantitativo inicial de **Raças e Classes** em `docs/mecanicaDoJogo/personagens/racasEClasses.md`, com matriz por classe individual (12 classes), custo de versatilidade e ajuste de penalidade de item alinhados às baselines de `Tipos de Armas` e `Itens de Mão`. Também foi adicionada matriz de deslocamento racial sobre eficiência por tipo (sem quebrar proficiência) e atualizados os links/âncoras no índice.
  **Próxima ação recomendada:** Executar a **Consolidação numérica da Progressão do Jogo**, formalizando tetos numéricos por faixa (F1–F4), custo explícito de versatilidade e marcos de classe para especialização/maestria compatíveis com a nova matriz de classes.
  **Riscos/Bloqueios:** Os valores da matriz por classe/raca ainda são baseline documental e podem gerar rebalanço em cadeia após validação por simulação; risco maior no eixo híbrido (`foco+arma`) em F3/F4.

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
