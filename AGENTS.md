# Instruções para Agentes (escopo: repositório)

## Leitura obrigatória (ordem)

1. `guideline.md` (regras gerais do projeto)
2. `docs/guide.md` (fase atual e objetivo macro)
3. `docs/statusDoDesenvolvimento.md` (avanço, pendências e próxima direção)
4. `docs/index.md` (mapa de navegação e referências cruzadas)

---

## Objetivo do agente nesta fase

Nesta etapa, o agente deve **evoluir documentação mecânica**, não implementar código do jogo.

Prioridades:
- reduzir ambiguidades;
- transformar conteúdo narrativo em regras operacionais;
- manter consistência de nomenclatura e links.

---

## Fluxo rápido para um agente sem contexto

1. Ler os 4 arquivos da seção “Leitura obrigatória”.
2. Em `docs/statusDoDesenvolvimento.md`, escolher o próximo tópico em “Pendente prioritário”.
3. Consultar `docs/legado/` apenas como fonte histórica.
4. Criar/atualizar página em `docs/mecanica-do-jogo/` (camelCase sem acentos).
5. Atualizar `docs/index.md` com links e âncoras da nova página.
6. Atualizar `docs/statusDoDesenvolvimento.md` (concluído/em andamento/pendente).
7. Registrar handoff no `docs/statusDoDesenvolvimento.md` com:
   - o que foi concluído nesta iteração;
   - próximo passo recomendado para o próximo agente;
   - sinalização de risco/bloqueio, se houver.

Se houver dúvida sobre por onde começar, executar exatamente o item 2 usando a seção
“Pendente prioritário” e a “Próxima ação recomendada” do status.

---

## Regras obrigatórias

1. Não adicionar `docs/guide.md` ao índice (`docs/index.md`) sem solicitação explícita.
2. Não alterar `docs/legado/` sem solicitação explícita.
3. Manter nomes de páginas em camelCase sem acentos.
4. Registrar critérios de “Pronto para Requisito” nas páginas mecânicas.
5. Evitar detalhes de implementação técnica nesta fase.
6. Ao fim de cada iteração, deixar instruções explícitas para continuidade do próximo agente.

---

## Definição mínima de entrega por tópico

Um tópico só pode ser considerado pronto quando tiver:
- definição operacional objetiva;
- regras/fórmulas (quando aplicável);
- limites/casos de borda;
- integração com outros sistemas já documentados;
- seção de pendências para futura formalização de requisitos.

---

## Revisão sistêmica periódica (obrigatória)

A cada **5 iterações** (ou antes, se houver mudança estrutural), o agente da vez deve:

1. Revisar coerência global entre `docs/index.md`, `docs/guide.md`, `docs/statusDoDesenvolvimento.md` e páginas mecânicas.
2. Verificar conflitos de fórmula/terminologia entre páginas.
3. Revalidar prioridades e fase atual no status.
4. Registrar no handoff que a revisão sistêmica foi executada.

---

## Prompt recomendado para acionar um novo agente

Use um prompt explícito, por exemplo:

> “Leia `AGENTS.md`, `guideline.md`, `docs/guide.md` e `docs/statusDoDesenvolvimento.md`.  
> Continue o loop de desenvolvimento executando **1 incremento pequeno** do próximo tópico prioritário.  
> Atualize `docs/index.md` e `docs/statusDoDesenvolvimento.md` com handoff (concluído, próximo passo e riscos).  
> Não implemente código do jogo nesta fase.”

Evite prompts curtos como apenas “continue o workflow”, pois podem perder contexto de escopo e entrega esperada.
