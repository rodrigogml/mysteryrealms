# Fórmulas de Combate

## Convenções

- `⌊x⌋`: arredondamento para baixo.
- `clamp(min, x, max)`: limita valor ao intervalo.
- Ordem de resolução: **Acerto → Dano Base → Bloqueio → Resistência → Dano Final**.

## Iniciativa

`Iniciativa = 1d20 + Destreza + Percepção + Modificadores`

## Acerto vs Defesa

`AtaqueTotal = 1d20 + Precisão + Modificadores`

Se `AtaqueTotal >= DefesaAlvo`, o golpe acerta.

## Dano Base

`DanoBase = DadoDaArmaOuMagia + AtributoEscalado + BônusFixos`

## Redução por Bloqueio

`DanoApósBloqueio = max(0, DanoBase × (1 - BloqueioTotal/100))`

## Redução por Resistência

`ResistênciaEfetiva = clamp(-100, ResistênciaTipo, 90)`

`DanoFinal = max(0, ⌊DanoApósBloqueio × (1 - ResistênciaEfetiva/100)⌋)`

## Aplicação de Aflição

`ChanceAplicação = ChanceBase + BônusAplicador - ResistênciaAflição`

Aplica se `1d100 <= clamp(0, ChanceAplicação, 100)`.

## Exemplo Numérico

- DanoBase = 40
- BloqueioTotal = 25%
- ResistênciaTipo = 20%

`DanoApósBloqueio = 40 × 0,75 = 30`

`DanoFinal = ⌊30 × 0,8⌋ = 24`
