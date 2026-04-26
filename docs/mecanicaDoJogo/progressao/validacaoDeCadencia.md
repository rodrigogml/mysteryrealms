# Validação de Cadência

## Objetivo Mecânico

Definir o procedimento operacional da **rodada inicial de validação de cadência** da progressão, usando o protocolo já estabelecido em `progressaoDoJogo.md`.

Este documento não calibra números finais; ele padroniza **como executar e registrar** a validação.

---

## Insumos Obrigatórios

1. Planilha oficial de XP por cenário (versão identificada).
2. Tabela de marcos por classe individual (F2/F3/F4).
3. Parâmetros de curva ativos (`A`, `B`, `C`).
4. Cenários de simulação definidos: `combate-dominante`, `hibrido-equilibrado`, `narrativo-investigativo`.

Sem os 4 insumos, a rodada não é válida para decisão de balanceamento.

---

## Procedimento da Rodada Inicial

### Passo 1 — Congelar baseline

- Registrar hash/versão da planilha de XP.
- Registrar versão da página de progressão e parâmetros de curva.
- Bloquear alterações durante a rodada.

### Passo 2 — Executar 3 cenários

Para cada cenário:

1. Simular progressão por classe até atingir F4 ou limite de sessões definido para teste.
2. Coletar métricas mínimas:
   - `sessoesParaF2`
   - `sessoesF2ParaF3`
   - `sessoesF3ParaF4`
   - `desvioEntreClasses`
3. Registrar anomalias (ex.: classe fora de faixa de alerta por 2 ciclos seguidos).

### Passo 3 — Classificar resultado

- **Aprovado**: critérios de aprovação atendidos em pelo menos 2 cenários.
- **Ajuste leve**: desvio pontual corrigível por percentual de XP por cenário.
- **Reprovado**: quebra estrutural de cadência exigindo revisão de marcos e/ou tetos.

### Passo 4 — Definir ação de continuidade

1. Se aprovado: abrir ciclo de validação cruzada com Raças/Classes, Tipos de Armas e Itens de Mão.
2. Se ajuste leve: versionar planilha, aplicar ajuste e repetir rodada curta.
3. Se reprovado: registrar bloqueio no status e abrir revisão sistêmica antecipada.

---

## Template de Registro dos Resultados

### Cabeçalho da execução

| Campo | Valor |
|---|---|
| Data da rodada | |
| Responsável | |
| Versão da planilha XP | |
| Parâmetros de curva (`A`, `B`, `C`) | |
| Escopo de classes avaliadas | |

### Resultado por cenário

| Cenário | `sessoesParaF2` | `sessoesF2ParaF3` | `sessoesF3ParaF4` | `desvioEntreClasses` | Status |
|---|---:|---:|---:|---:|---|
| Combate-dominante | | | | | |
| Híbrido equilibrado | | | | | |
| Narrativo-investigativo | | | | | |

### Anomalias e decisão

| Item | Registro |
|---|---|
| Classes fora de faixa | |
| Hipótese de causa | |
| Ação escolhida | |
| Próxima execução | |

---

## Integração com Outros Sistemas

- Resultado da cadência alimenta ajustes em `progressaoDoJogo.md`.
- Desvios por arquétipo devem ser validados contra `racasEClasses.md` antes de alterar marcos.
- Ajuste de ritmo por combate deve ser cruzado com metas de balanceamento e matrizes de equipamentos.

---

## Pronto para Requisito

Para este tópico ser considerado pronto para requisito funcional, ainda falta:

1. Registrar a primeira rodada completa com dados reais nos 3 cenários.
2. Publicar decisão de aprovação/ajuste/reprovação com justificativa.
3. Vincular os resultados ao status com plano de ação para o ciclo seguinte.
