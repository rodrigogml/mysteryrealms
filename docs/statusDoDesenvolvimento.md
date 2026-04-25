# Status do Desenvolvimento

## Objetivo deste documento

Este arquivo e a referencia principal para acompanhar o **andamento do projeto**.

Ele consolida:
- fase atual;
- entregas concluidas;
- pendencias priorizadas;
- criterio de transicao entre fases;
- riscos e decisoes em aberto.

> Regra: sempre atualizar este arquivo ao concluir migracoes relevantes de topicos mecanicos.

Este documento tambem funciona como **canal de continuidade entre agentes autonomos**.

---

## Visao geral do projeto

- **Objetivo final:** implementacao completa do jogo com base em requisitos claros e testaveis.
- **Fase atual:** consolidacao da mecanica (sem implementacao de codigo).
- **Proxima macrofase:** formalizacao de requisitos funcionais.

---

## Progresso da fase atual

### Concluido

- Estrutura de documentacao unificada com indice unico.
- Glossario mecanico base.
- Ciclo de combate.
- Formulas canonicas de combate.
- Resistencias, modificadores, aflicoes e estados.
- Metas de balanceamento (baseline).
- Moral.
- Recursos vitais e recuperacao.
- Descanso e acampamento.

### Em andamento

- Refinamento de cobertura de topicos legados fora do nucleo de combate.

### Pendente prioritario

1. Tipos de Armas (taxonomia mecanica e impacto sistemico).
2. Itens de Mao (slots, categorias, regras de uso e efeitos).
3. Racas e Classes (recorte mecanico separado da narrativa).
4. Progressao do Jogo (curva de poder e marcos por faixa).

### Proxima acao recomendada (para agente sem contexto)

Iniciar por **Tipos de Armas**, pois esse topico desbloqueia consistencia para:
- Itens de Mao (regras de equipamento e uso);
- formulas de acerto/dano por categoria;
- progressao de poder por estilo de combate.

---

## Loop de desenvolvimento entre agentes (workflow continuo)

Em cada iteracao, o agente deve seguir este ciclo:

1. **Ler contexto minimo obrigatorio** (`AGENTS.md`, `guideline.md`, `docs/guide.md`, este status e `docs/index.md`).
2. **Escolher 1 incremento pequeno** alinhado ao pendente prioritario.
3. **Executar e integrar** (pagina mecanica + atualizacao de indice/links).
4. **Validar coerencia local** (termos, formulas, secoes de pronto para requisito).
5. **Registrar handoff** (conclusao + proxima acao recomendada + riscos).

Objetivo do loop: garantir progresso incremental sem depender de memoria externa ou conversa entre agentes.

### Inicio rapido (sem contexto)

1. Ler `AGENTS.md` e `docs/guide.md`.
2. Executar o próximo item de “Pendente prioritario”.
3. Atualizar este arquivo com handoff ao finalizar.

---

## Registro de iteracoes (handoff obrigatorio)

> Formato: adicionar sempre no topo (mais recente primeiro).

- **Iteracao:** 000  
  **Concluido:** Estrutura inicial do loop definida.  
  **Proxima acao recomendada:** Migrar topico *Tipos de Armas* para mecanica revisada com secao de pronto para requisito.  
  **Riscos/Bloqueios:** Nenhum no momento.

---

## Criterio de "pronto" por topico migrado

Um topico so e considerado pronto para requisito quando tiver:

- definicao operacional objetiva;
- regras/fomulas e ordem de resolucao (quando aplicavel);
- casos-limite e limites mecanicos;
- integracao com sistemas ja definidos;
- secao de "Pronto para Requisito" com pendencias explicitas.

---

## Criterio para encerrar a fase atual

A fase de consolidacao mecanica sera considerada concluida quando:

1. Topicos prioritarios estiverem migrados com criterio de pronto atendido.
2. Ambiguidades criticas entre paginas forem removidas.
3. Houver rastreabilidade clara para conversao em requisitos.

Ao atingir os 3 criterios, o agente deve atualizar este documento com status de **pronto para transicao de fase**.

---

## Riscos e atencoes

- Crescimento de regras sem normalizacao comum (risco de inconsistencia).
- Conflitos entre formulas de paginas diferentes.
- Introducao de detalhes de implementacao antes da fase correta.

Mitigacao:
- manter atualizacao disciplinada do indice e deste status;
- revisar dependencia entre paginas ao final de cada migracao;
- registrar decisoes mecanicas explicitamente.

Checkpoint global:
- a cada 5 iteracoes, executar revisao sistêmica do conjunto documental e registrar no handoff.

---

## Proximas fases (macro)

1. Formalizacao de requisitos.
2. Planejamento tecnico.
3. Implementacao incremental.
4. Validacao e balanceamento continuo.

Este arquivo nao detalha execucao tecnica das fases futuras; apenas o estado e direcao do desenvolvimento.
