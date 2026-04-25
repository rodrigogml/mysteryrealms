# Guideline do Projeto

## Leitura obrigatória antes de contribuir

1. `guideline.md` (este documento)
2. `AGENTS.md` (regras operacionais do repositório)
3. `docs/guide.md` (fase atual e objetivo macro)
4. `docs/statusDoDesenvolvimento.md` (estado de avanço, pendências e direção)

---

## Objetivo do projeto

- Objetivo final: implementar o jogo.
- Fase atual: consolidar e normalizar a mecânica em documentação operacional.
- Regra central desta fase: **não implementar código de jogo ainda**; focar em definições claras para requisitos futuros.

---

## Regras de documentação

- Manter `docs/index.md` como índice único de navegação.
- Não incluir `docs/guide.md` no índice, salvo solicitação explícita.
- Nomear novas páginas de documentação em **camelCase sem acentos**.
- Sempre atualizar referências cruzadas quando renomear arquivos.
- Priorizar páginas curtas, objetivas e com critérios de “pronto para requisito”.
- Ao final de cada iteração, atualizar `docs/statusDoDesenvolvimento.md` com handoff para o próximo agente.
- A cada 5 iterações, executar revisão sistêmica de coerência documental e registrar no status.

---

## Regras de código e mudanças técnicas

- Evitar alterações de implementação fora do escopo solicitado.
- Em mudanças de código (quando a fase permitir), preferir alterações pequenas e rastreáveis.
- Não introduzir dependências sem justificativa explícita.
- Registrar comandos de validação executados.

---

## Regras de colaboração

- Preservar consistência terminológica (mecânica unificada, sem separar módulos legados).
- Tratar `docs/legado/` como referência histórica; não reescrever sem pedido explícito.
- Em caso de ambiguidades mecânicas, documentar premissas e pendências em vez de supor implementação.
