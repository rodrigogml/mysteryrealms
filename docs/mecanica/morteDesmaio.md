# Morte e Desmaio

O fluxo canônico de jogador usa colapso/desmaio em vez de morte definitiva imediata.

## Estado crítico por Pontos de Vida

Quando `pontos_vida <= 0`, aplicar `estado_critico` e executar Teste de Conscientização.

- **Sucesso:** personagem retorna com `1` ponto de vida, `fadiga_atual` ajustada para `90% de fadiga_max` e estado `estado_critico_recente` por 10 minutos.
- **Falha:** entra em `desmaio` imediatamente.

## Gatilhos adicionais de desmaio fisiológico

- `fadiga_atual >= 120% de fadiga_max`.
- `sede_pct >= 100%`.
- `fome_pct >= 100%`.

Se múltiplos gatilhos ocorrerem no mesmo tick, registrar todos os motivos e abrir um único estado `desmaio`.

## Regras de desmaio

- Personagem inconsciente e incapaz de agir/reagir.
- Sem dano passivo sistêmico adicional durante desmaio (exceto regras explícitas de evento).
- Recuperação em modo equivalente a dormir com `fator_qualidade_sono = 0,5`.

## Critérios de despertar

Para sair de `desmaio`, todos devem ser verdadeiros:

- `pontos_vida >= 90% do máximo`.
- `fadiga_atual <= 10% de fadiga_max`.
- `fadiga_min <= 10% de fadiga_max`.

Ao despertar:

- Aplicar clamp: `fome_pct <= 95%` e `sede_pct <= 95%`.
- Aplicar modificadores:
  - `fadiga_elevada` por 24h.
  - `mal_estar_temporario` por 24h ou até completar 4h de sono contínuo.
  - `recuperacao_reduzida` por 24h.

## Precedência

- `desmaio` tem precedência máxima sobre ação, combate e diálogo.
- Durante desmaio, estados contínuos não letais continuam atualizando sem devolver controle ao jogador.
- Ao despertar, recalcular primeiro `fadiga/fome/sede/moral`, depois devolver controle.
