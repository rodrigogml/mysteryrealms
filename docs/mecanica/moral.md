# Moral

Este documento define a Moral como recurso psicológico em escala de `0` a `100`.

## Faixas e efeitos

| Faixa | Gatilho | Estado (`id`) | Efeitos |
|---|---:|---|---|
| Colapso emocional | `0 a 20` | `moral_colapso` | `-4 vontade`, `-3 carisma`, `-25% testes sociais`, risco de pânico em combate. |
| Baixa moral | `21 a 50` | `moral_baixa` | `-2 vontade`, `-1 carisma`, `-10% testes mentais/sociais`. |
| Moral estável | `51 a 80` | `moral_estavel` | Sem modificadores. |
| Alta moral | `81 a 100` | `moral_alta` | `+2 vontade`, `+1 intimidação`, `+10% resistência a medo`. |

## Gatilhos de variação

### Redução

- Testemunhar queda de aliado: `-8`.
- Falha crítica narrativa relevante: `-5`.
- Entrada em `fome_agravada + sede_agravada`: `-15` instantâneo.
- Entrada em `desmaio`: `-12`.

### Aumento

- Vitória em combate significativo: `+6`.
- Descanso seguro por 2h: `+5`.
- Sono contínuo `>= 4h` com boa qualidade: `+10`.
- Marco narrativo pessoal: `+8`.

## Interação com fadiga, fome e sede

1. `moral_colapso` aumenta em `+10%` o custo de fadiga por ação.
2. `moral_alta` reduz em `-10%` o custo de fadiga por ação (não acumula com redutor superior da mesma categoria).
3. `fome_agravada` e `sede_agravada` podem forçar troca de faixa no mesmo tick.

## Ordem de aplicação no tick

1. Calcular estados fisiológicos (`fadiga`, `fome`, `sede`).
2. Aplicar deltas de moral disparados no tick.
3. Recalcular faixa de moral ativa e efeitos.
