# Requisitos — Progressão do Personagem

Requisitos funcionais do sistema para XP, níveis, evolução de atributos e desbloqueios.

Referência canônica: `docs/mecanica/progressaoDoJogo.md`.

---

## RF-PP-01 — Acúmulo de XP

O sistema deve:
- Armazenar o campo `xp_acumulado` (inteiro `>= 0`) na ficha do personagem.
- Somar XP ao campo `xp_acumulado` ao ocorrer qualquer dos eventos abaixo:
  - Combate encerrado com vitória.
  - Conclusão de objetivo/quest.
  - Descoberta ou exploração de local novo.
  - Interação relevante com NPC.
- O XP nunca deve ser decrementado por evento normal de jogo.

---

## RF-PP-02 — Fórmula de XP por nível

### XP necessário para avançar do nível `n` para `n+1`

```
xp_nivel(n) = ceil(A × pow(ln(n + B), C))
```

**Parâmetros ativos (versão `BAL-1.0.0`):**

| Parâmetro | Valor |
|---|---|
| `A` | 50 |
| `B` | 10 |
| `C` | 10 |

Onde:
- `n`: nível atual (`n >= 1`).
- `ln`: logaritmo natural.
- `ceil`: arredondamento para cima.

### XP total acumulado até o nível `n`

```
xp_total(n) = soma de xp_nivel(i), para i de 1 até n
```

Onde cada `xp_nivel(i)` já está arredondado com `ceil` antes de somar.

---

## RF-PP-03 — Regras de arredondamento e implementação

- Calcular `xp_nivel(n)` em ponto flutuante e aplicar `ceil` no resultado antes de armazenar ou comparar.
- `xp_total(n)` é a soma dos `xp_nivel(i)` já arredondados individualmente.
- A implementação deve garantir resultado determinístico (mesmo arredondamento em todos os ambientes de execução).

---

## RF-PP-04 — Subida de nível

O sistema deve verificar, após cada ganho de XP, se `xp_acumulado >= xp_total(nivel_atual + 1)`.
- Se sim: incrementar `nivel_atual` e disparar os efeitos de subida de nível abaixo.
- O processo deve repetir enquanto a condição for verdadeira (permitindo múltiplos níveis em sequência).

---

## RF-PP-05 — Pontos de Evolução de Atributo (PEA)

Ao subir de nível, o personagem recebe Pontos de Evolução de Atributo conforme a faixa:

| Faixa de nível | PEA recebidos |
|---|---|
| `2` a `10` | `+1 PEA` por nível |
| `11` a `30` | `+1 PEA` a cada 2 níveis |
| `31+` | `+1 PEA` a cada 3 níveis |

Regras de uso:
- Cada PEA aumenta `+1` em um atributo principal escolhido pelo jogador.
- Nenhum atributo pode receber mais de `+2` no mesmo evento de subida de nível.
- Limite suave por nível: `atributo_max_nivel = 10 + floor(nivel / 5)`.

---

## RF-PP-06 — Pontos de Proficiência (PP)

A cada nível ímpar, o jogador recebe `+1 Ponto de Proficiência (PP)` para distribuir em habilidades treináveis.

Bônus de proficiência adicional por faixa de nível:

| Faixa de nível | Bônus de proficiência |
|---|---|
| `1` a `9` | `+0` |
| `10` a `24` | `+1` |
| `25` a `49` | `+2` |
| `50+` | `+3` |

Fórmula de valor final de habilidade:

```
habilidade_final = atributo_base + pp_da_habilidade + bonus_proficiencia_faixa + modificadores
```

---

## RF-PP-07 — Desbloqueios por marco de nível

O sistema deve conceder os seguintes desbloqueios obrigatórios nos marcos de nível:

| Marco | Desbloqueio |
|---|---|
| Nível 3 | 1º slot de habilidade ativa de classe |
| Nível 5 | Traço utilitário de classe/racial avançado |
| Nível 8 | 2º slot de habilidade ativa |
| Nível 12 | Habilidade de assinatura (tier intermediário) |
| Nível 20 | Especialização avançada (ramificação de build) |
| Nível 30+ | Ciclos de maestria (repetíveis por patamar) |

Regra: cada classe/raça pode definir desbloqueios adicionais, mas nunca pode remover os marcos base acima.

---

## RF-PP-08 — Versionamento de balanceamento

### Formato

```
BAL-MAJOR.MINOR.PATCH
```

| Segmento | Quando usar |
|---|---|
| `MAJOR` | Mudança de fórmula ou parâmetros com quebra de compatibilidade |
| `MINOR` | Ajustes de curva/desbloqueio compatíveis com personagens existentes |
| `PATCH` | Correções sem impacto estrutural |

### Política de migração

- Cada personagem deve armazenar `balance_version` (versão aplicada no último recálculo).
- Ao alterar a versão de balanceamento:
  1. Manter `xp_acumulado` como fonte da verdade.
  2. Recalcular `nivel_atual` usando a nova curva.
  3. Registrar a migração no histórico interno.
- Alterações `MAJOR` exigem simulação prévia, plano de migração e comunicação de impacto ao jogador antes de serem aplicadas.
