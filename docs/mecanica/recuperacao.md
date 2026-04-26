# Recuperação

Este documento define as regras canônicas de recuperação para `pontos_vida`, `fadiga_atual`, `fadiga_min`, `fome_pct`, `sede_pct` e moral.

## Modos de recuperação

### Descanso

Ativo em atividades de baixa demanda física (sentado, conversa parada, leitura, meditação leve, cavalgada passiva).

- Recuperação de fadiga por minuto: `-0,10% de fadiga_max × fator_atividade`.
- `fator_atividade` no intervalo `[0, 1]`.
- Não recupera `fadiga_min` nem `pontos_vida`.
- Enquanto acordado, `fadiga_min` continua subindo (regra de vigília).

### Dormir

Requer local para sentar/deitar e ao menos `60` minutos ininterruptos para contar como sono.

- Recuperação de fadiga por minuto: `-0,21% de fadiga_max × fator_qualidade_sono`.
- Recuperação de fadiga mínima por minuto: `-0,21% de fadiga_max × fator_qualidade_sono`.
- Recuperação de pontos de vida por minuto: `(constituicao / 120) × fator_qualidade_sono`.

#### Fator de qualidade do sono

- `fator_qualidade_sono = 0,5 + (3 × C + (100 - max(0, R - S))) / 800`
- Clamp final: `[0,5, 1,0]`.

### Itens e ações específicas

- Itens podem recuperar `pontos_vida`, fadiga, fome, sede e moral.
- Itens não reduzem `fadiga_min` de forma permanente.
- Boost temporário de `fadiga_min` é permitido somente com duração e reversão explícitas.

## Interação com estados críticos

1. Em `sede_agravada`, recuperação efetiva de fadiga recebe `-25%`.
2. Em `fome_agravada`, custo de fadiga por ação recebe `+20%`.
3. Em `exaustao`, qualquer ação de esforço no minuto cancela recuperação daquele minuto.
4. Em `desmaio`, usar exclusivamente `morteDesmaio.md`.

## Ordem de resolução no tick

`colapso/desmaio -> estado_critico (PV) -> estados fisiológicos graves -> aflições -> recuperação`

## Moral durante recuperação

- Descanso seguro sem interrupção por 2h: `+5` de moral.
- Sono contínuo `>= 4h` com `fator_qualidade_sono >= 0,75`: `+10` de moral.
- Interrupção hostil do sono: `-8` de moral no evento.
