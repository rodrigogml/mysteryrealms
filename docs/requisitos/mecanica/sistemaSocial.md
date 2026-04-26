# Requisitos — Sistema Social

Requisitos funcionais do sistema para diálogos, testes sociais, estilos de fala, relacionamento, reputação, diário e marcadores.

Referências canônicas: `docs/mecanica/estruturaDeDialogo.md`, `docs/mecanica/estilosDeFalaEValoracao.md`, `docs/mecanica/diarioDoJogador.md`, `docs/mecanica/sistemaDeMarcadores.md`.

---

## RF-SS-01 — Estrutura de nó de diálogo

Cada nó de diálogo deve armazenar:

| Campo | Tipo | Obrigatório | Regra |
|---|---|---|---|
| `dialogoId` | string | Sim | Único; prefixo `dlg_`. |
| `npcId` | string | Sim | ID de NPC, grupo ou facção. |
| `texto` | string | Sim | Texto exibível ao jogador. |
| `opcoes` | lista | Sim | Pelo menos 1 opção. |

Cada opção deve armazenar:

| Campo | Tipo | Obrigatório | Regra |
|---|---|---|---|
| `opcaoId` | string | Sim | Único dentro do nó. |
| `estiloFalaId` | string | Sim | Um dos 5 estilos canônicos (RF-SS-03). |
| `textoOpcao` | string | Sim | Texto exibível ao jogador. |
| `testeSocial` | objeto | Não | Presente quando há risco ou oposição. |
| `efeitosSucesso` | objeto | Sim | Efeitos quando não há teste ou teste passa. |
| `efeitosFalha` | objeto | Não | Obrigatório quando `testeSocial` estiver presente. |

---

## RF-SS-02 — Ciclo social obrigatório

Todo diálogo com impacto mecânico deve seguir o ciclo:

```
Escolha de fala → Teste social (quando aplicável)
→ Alteração de relacionamento/reputação → Registro no diário e em marcadores
```

- O nó sempre deve terminar com atualização de estado social e persistência (diário/marcadores).
- Efeitos não devem ficar "soltos" fora de `efeitosSucesso`/`efeitosFalha`.
- Se não houver teste social, aplicar diretamente os efeitos e registrar no passo de persistência.

---

## RF-SS-03 — Estilos de fala canônicos

| Chave técnica | Descrição |
|---|---|
| `fala_diplomatica` | Respeito, negociação, conciliação. |
| `fala_intimidadora` | Ameaça, imposição, domínio. |
| `fala_empatica` | Acolhimento, escuta, conexão emocional. |
| `fala_pragmatica` | Objetividade, troca direta, foco em resultado. |
| `fala_ironica` | Sarcasmo, provocação, ambiguidade. |

Regras:
- IDs de estilo devem usar prefixo `fala_`.
- Não misturar múltiplos estilos em uma mesma opção, salvo justificativa explícita de design.

---

## RF-SS-04 — Valoração de estilo por NPC/facção

Cada NPC ou facção deve declarar a valoração para cada estilo de fala:

| Valoração | Efeito no teste social | Efeito no relacionamento |
|---|---|---|
| `gosta` | `+2` no resultado final do teste | Priorizar delta positivo mais alto em sucesso |
| `tolera` | `+0` | Sem ajuste |
| `rejeita` | `-2` no resultado final do teste | Priorizar delta negativo mais alto em falha |

---

## RF-SS-05 — Teste social

Quando `testeSocial` estiver presente na opção, o sistema deve:

1. Obter `atributo` e/ou `habilidade` declarados no nó.
2. Calcular `resultado_teste = valor_base + 1d20 + modificadores` (RF-CT-01).
3. Aplicar ajuste de estilo de fala (RF-SS-04): `+2` (gosta), `0` (tolera), `-2` (rejeita).
4. Comparar com `dificuldade` declarada (CD fixa) ou resultado do NPC (teste oposto):
   - `resultado_teste >= cd` → aplicar `efeitosSucesso`.
   - `resultado_teste < cd` → aplicar `efeitosFalha`.

---

## RF-SS-06 — Relacionamento com NPC

- Escala: inteiro de `-100` a `100`.
- Alterações são declaradas como `delta` (ex.: `+2`, `-1`).
- O sistema deve aplicar o delta e manter o valor no intervalo `[-100, 100]`.
- Faixas canônicas:

| Faixa | Intervalo |
|---|---|
| Inimigo Mortal | `-100` a `-61` |
| Hostil | `-60` a `-21` |
| Neutra | `-20` a `20` |
| Favorável | `21` a `60` |
| Aliado | `61` a `100` |

---

## RF-SS-07 — Reputação por localidade/facção

- Alterações são declaradas como `delta`.
- O sistema deve aplicar o delta ao registro de reputação do alvo (`loc_id` ou `faccao_id`).
- A reputação impacta o fator de mercado em transações econômicas (RF-EI-02).

---

## RF-SS-08 — Diário do jogador

O sistema deve criar uma entrada de diário quando ao menos um dos eventos ocorrer:
- Mudança de relacionamento com NPC.
- Mudança de reputação em localidade/facção.
- Descoberta narrativa relevante via diálogo.
- Ativação/desativação de marcador de progresso social.

### Estrutura mínima de entrada

| Campo | Tipo | Obrigatório | Regra |
|---|---|---|---|
| `entradaId` | string | Sim | Único; prefixo `diary_`. |
| `titulo` | string | Sim | Máximo 8 palavras. |
| `resumo` | string | Sim | 1 a 2 frases, em passado, foco em consequência. |
| `dataJogo` | string | Sim | Formato `D<n>-HH:MM`. |
| `origem.dialogoId` | string | Sim | ID do nó de diálogo que gerou a entrada. |
| `origem.opcaoId` | string | Sim | ID da opção escolhida. |
| `impactos` | objeto | Sim | Deltas de relacionamento, reputação e marcadores aplicados. |

---

## RF-SS-09 — Sistema de marcadores

### Tipos de marcador

| Tipo | Dado | Uso |
|---|---|---|
| `flag` | boolean | Fato ocorreu / não ocorreu. |
| `stage` | inteiro | Estágio de progresso (ex.: `0..3`). |
| `counter` | inteiro | Contagem de recorrência. |

### Convenção de IDs

```
mk_<dominio>_<descricaoCamelCase>
```

- Prefixo fixo: `mk_`.
- Domínio identifica NPC, facção, localidade ou quest.
- Descrição em `camelCase`, sem acentos e sem espaços.

### Operações obrigatórias

- Inserir/ativar marcador.
- Remover/desativar marcador.
- Incrementar/decrementar marcador numérico.
- Validar pré-condição de diálogo com base em marcador.

### Regras de consistência

- Um marcador deve ter **uma única responsabilidade**.
- Não duplicar semântica de estado com sinônimos.
- Toda regra de diálogo condicionada por marcador deve citar o ID completo.
- Alterações de marcador que afetam progressão devem gerar entrada no diário (RF-SS-08).
