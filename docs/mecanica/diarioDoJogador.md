# Diário do Jogador

## Objetivo

Registrar, de forma legível para o jogador e rastreável para o sistema, os eventos sociais relevantes.

## Quando registrar

Uma entrada de diário deve ser criada quando ocorrer ao menos um dos eventos:

- mudança de relacionamento com NPC;
- mudança de reputação em localidade/facção;
- descoberta narrativa relevante via diálogo;
- ativação/desativação de marcador de progresso social.

## Estrutura mínima de entrada

```yaml
entradaId: diary_kaela_primeiroContato_sucesso
titulo: "Primeiro contato com Kaela"
resumo: "A conversa foi cordial e abriu espaço para cooperação."
dataJogo: "D3-18:20"
origem:
  dialogoId: dlg_taverna_primeiroContato_001
  opcaoId: op_001
impactos:
  relacionamento:
    - alvoId: npc_kaelaRunaveia
      delta: +2
  reputacao:
    - alvoId: loc_salaoDaAsaQuebrada
      delta: +1
  marcadores:
    - mk_kaela_confiouPrimeiroContato
```

## Convenções de escrita

- IDs de diário devem usar prefixo `diary_`.
- `titulo` curto (até 8 palavras), `resumo` objetivo (1 a 2 frases).
- Escrever sempre em passado, com foco em consequência.
- Toda entrada deve referenciar `dialogoId` e `opcaoId` de origem.

## Integração obrigatória com o ciclo

No fluxo social padronizado, o diário representa parte do passo final:

`Escolha de fala → Teste social → Relação/Reputação → Diário/Marcadores`

Sem entrada de diário (ou justificativa de silêncio), o ciclo é considerado incompleto.
