# Estilos de Fala e Valoração

## Objetivo

Padronizar como o jogo interpreta o tom da fala do jogador e converte isso em bônus/penalidades sociais.

## Estilos canônicos

- `fala_diplomatica`: respeito, negociação, conciliação.
- `fala_intimidadora`: ameaça, imposição, domínio.
- `fala_empatica`: acolhimento, escuta, conexão emocional.
- `fala_pragmatica`: objetividade, troca direta, foco em resultado.
- `fala_ironica`: sarcasmo, provocação, ambiguidade.

## Matriz de valoração social

Cada NPC/facção deve ter afinidade por estilo de fala:

- `gosta`: bônus no teste social e tendência positiva no relacionamento.
- `tolera`: sem ajuste mecânico relevante.
- `rejeita`: penalidade no teste social e tendência negativa no relacionamento.

Exemplo de valoração:

```yaml
npcId: npc_kaelaRunaveia
valoracaoEstilo:
  fala_diplomatica: gosta
  fala_empatica: gosta
  fala_pragmatica: tolera
  fala_ironica: rejeita
  fala_intimidadora: rejeita
```

## Aplicação no ciclo social

No passo **teste social**:

- `gosta`: +2 no valor final do teste.
- `tolera`: +0.
- `rejeita`: -2 no valor final do teste.

No passo **relacionamento/reputação**:

- sucesso com estilo `gosta`: priorizar delta positivo mais alto;
- falha com estilo `rejeita`: priorizar delta negativo mais alto.

## Convenções de escrita

- IDs de estilo devem usar prefixo `fala_`.
- Textos de opção devem refletir claramente o estilo selecionado.
- Não misturar múltiplos estilos na mesma opção, salvo justificativa explícita em design.

## Integrações

- Estrutura de diálogo: `./estruturaDeDialogo.md`
- Relacionamento e reputação: `./definicaoDePersonagem.md#relacionamento-e-reputação`
