# Requisitos — Ficha do Personagem

Requisitos funcionais do sistema para modelagem, armazenamento e cálculo da ficha do personagem jogador.

Referência canônica: `docs/mecanica/definicaoDePersonagem.md`, `docs/mecanica/glossario.md`.

---

## RF-FP-01 — Identidade do personagem

O sistema deve armazenar os seguintes campos de identidade do personagem:

| Campo | Tipo | Obrigatório | Regra |
|---|---|---|---|
| `nome` | string | Sim | Não vazio. |
| `sobrenome` | string | Sim | Não vazio. |
| `genero` | enum | Sim | Valores: `MASCULINO`, `FEMININO`, `OUTRO`. |
| `raca` | enum | Sim | Conforme RF-FP-07. |
| `classe` | enum | Sim | Conforme RF-FP-08. |
| `idadeInicial` | inteiro | Sim | Regra oficial: entre `12` e `120`, inclusive. |

---

## RF-FP-02 — Atributos principais

O sistema deve armazenar e gerenciar os 7 atributos principais do personagem, conforme chaves canônicas:

| Exibição | Chave técnica |
|---|---|
| Força | `forca` |
| Destreza | `destreza` |
| Constituição | `constituicao` |
| Intelecto | `intelecto` |
| Percepção | `percepcao` |
| Carisma | `carisma` |
| Vontade | `vontade` |

Regras:
- Os valores iniciais são definidos pela combinação Raça + Classe (RF-FP-07 e RF-FP-08).
- Não há distribuição livre de pontos na criação do personagem.
- Cada atributo é um inteiro `>= 1`.

---

## RF-FP-03 — Habilidades canônicas

O sistema deve armazenar e calcular as habilidades associadas ao personagem:

| Exibição | Chave técnica | Atributo base |
|---|---|---|
| Persuasão | `persuasao` | `carisma` |
| Intimidação | `intimidacao` | `carisma` |
| Enganação | `enganacao` | `carisma` |
| Conhecimento (Arcano) | `conhecimento_arcano` | `intelecto` |
| Conhecimento (História) | `conhecimento_historia` | `intelecto` |
| Conhecimento (Relíquias) | `conhecimento_reliquias` | `intelecto` |
| Herbalismo | `herbalismo` | `intelecto` |
| Alquimia | `alquimia` | `intelecto` |
| Furtividade | `furtividade` | `destreza` |
| Sobrevivência | `sobrevivencia` | `percepcao` |
| Manuseio de Armas | `manuseio_armas` | `forca` ou `destreza` |
| Uso de Magia | `uso_magia` | `vontade` ou `intelecto` |

Regras:
- Quando houver habilidade específica para a ação, usar a habilidade.
- Quando não houver, usar o atributo principal coerente com a ação.
- O valor final de habilidade segue a fórmula de progressão definida em `docs/mecanica/progressaoDoJogo.md`.

---

## RF-FP-04 — Estado do personagem

O sistema deve armazenar e gerenciar o estado dinâmico do personagem:

| Campo | Chave técnica | Tipo | Regra |
|---|---|---|---|
| XP Acumulado | `xp_acumulado` | inteiro | `>= 0`. |
| Nível Atual | `nivel_atual` | inteiro | `>= 1`. |
| Versão de Balanceamento | `balance_version` | string | Ex.: `BAL-1.0.0`. |
| Pontos de Vida | `pontos_vida` | decimal | Intervalo `[0, pontos_vida_max]`. |
| Fadiga Atual | `fadiga_atual` | decimal | `>= fadiga_min` e `<= 120% de fadiga_max` antes do colapso. |
| Fadiga Mínima | `fadiga_min` | decimal | Cresce com o tempo acordado; detalhe em RF-EF-01. |
| Fadiga Máxima | `fadiga_max` | decimal | Calculada conforme RF-FP-06. |
| Fome | `fome_pct` | decimal | Intervalo `[0, 100]`. |
| Sede | `sede_pct` | decimal | Intervalo `[0, 100]`. |
| Moral | `moral` | inteiro | Intervalo `[0, 100]`. |

Referências de regras canônicas de estado:
- Fadiga, fome e sede: `docs/mecanica/fadigaFomeSede.md`.
- Recuperação: `docs/mecanica/recuperacao.md`.
- Morte e desmaio: `docs/mecanica/morteDesmaio.md`.
- Moral: `docs/mecanica/moral.md`.

---

## RF-FP-05 — Inventário e recursos

O sistema deve armazenar e gerenciar o inventário do personagem:

| Campo | Tipo | Regra |
|---|---|---|
| `qtd_moeda_primaria` | inteiro | `>= 0`. Moeda Primária (MP). |
| `qtd_moeda_secundaria` | inteiro | `>= 0`. Moeda Secundária (MS). |
| `itens_equipados` | lista de itens | Máximo de 2 mãos ocupáveis; regras em RF-EI-05. |
| `itens_mochila` | lista de itens | Sem limite fixo além da carga máxima. |
| `montaria` | referência a ficha de montaria | Opcional; impacta capacidade de carga. |

---

## RF-FP-06 — Atributos derivados (ficha técnica)

### RF-FP-06.1 — Pontos de Vida Máximo

```
pontos_vida_max = constituicao × 10
```

### RF-FP-06.2 — Fadiga Máxima

```
fadiga_max = (constituicao + vontade) × 100
```

### RF-FP-06.3 — Peso do personagem

```
peso_kg = peso_base_raca_genero × (1 + ((constituicao - 3) × 0,05))
```

Onde `peso_base_raca_genero` é definido pela ficha de raça e gênero.

### RF-FP-06.4 — Capacidade de Carga

- **Carga Máxima (sem montaria):** `forca × 10` (kg).
- **Carga Máxima (montado):** `(carga_max_montaria - peso_kg_jogador) + modificadores_montaria`.
- **Carga Crítica:** `capacidade_carga_atual × 1,5`.
- **Carga Atual:** `peso_equipados_kg + peso_mochila_kg + peso_moedas_kg`.
  - `peso_moedas_kg = ((qtd_moeda_primaria × 6) + (qtd_moeda_secundaria × 5)) / 1000`.

### RF-FP-06.5 — Precisão

```
precisao = 1d20 + destreza + bonus_item_precisao + modificadores
```

### RF-FP-06.6 — Dano (final)

```
dano_final = max(0, floor(
  (dado_arma + mod_atributo + bonus_raca_classe + bonus_item_dano + bonus_plano)
  × (1 + soma_pct_positivo - soma_pct_negativo)
) + soma_flat_positivo - soma_flat_negativo)
```

**Ordem obrigatória de aplicação:**
1. Calcular base: `dado_arma + mod_atributo + bonus_raca_classe + bonus_item_dano + bonus_plano`.
2. Aplicar modificadores percentuais.
3. Aplicar `floor` no resultado.
4. Aplicar modificadores flat.
5. Truncar no mínimo em `0`.

### RF-FP-06.7 — Defesa (final)

```
defesa_final = max(0, floor(
  (base_esquiva + bonus_armadura + bonus_escudo + bonus_item_defesa + bonus_plano)
  × (1 + soma_pct_positivo - soma_pct_negativo)
) + soma_flat_positivo - soma_flat_negativo)
```

**Ordem obrigatória de aplicação:** mesma de RF-FP-06.6.

### RF-FP-06.8 — Bloqueio (final)

```
bloqueio_final = max(0, floor(
  (base_bloqueio + bonus_equip_bloqueio + bonus_item_bloqueio + bonus_plano)
  × (1 + soma_pct_positivo - soma_pct_negativo)
) + soma_flat_positivo - soma_flat_negativo)
```

**Ordem obrigatória de aplicação:** mesma de RF-FP-06.6.

---

## RF-FP-07 — Raças jogáveis

O sistema deve suportar as seguintes raças:

- Humano
- Elfo
- Meio-elfo
- Anão
- Meio-orc
- Tiefling
- Draconato
- Halfling

Cada raça deve definir:
- Distribuição-base dos 7 atributos principais.
- Bônus em habilidades específicas.
- Resistências e traços raciais.
- Tabela de peso base por gênero (campo `peso_base_raca_genero` usado em RF-FP-06.3).

---

## RF-FP-08 — Classes jogáveis

O sistema deve suportar as seguintes classes:

| Grupo | Classes |
|---|---|
| Combate | Guerreiro, Caçador, Duelista |
| Magia | Mago, Alquimista, Conjurador Elemental |
| Social / Suporte | Bardo, Clérigo, Sábio |
| Furtivo / Manipulação | Ladrão, Assassino, Ilusionista |

Cada classe deve definir:
- Bônus iniciais de atributos.
- Bônus em habilidades específicas.
- Afinidades com tipos de armas (para cálculo de bônus de proficiência).

---

## RF-FP-09 — Relacionamento e reputação

O sistema deve armazenar:

| Campo | Tipo | Regra |
|---|---|---|
| Relacionamento com NPC | mapa `npc_id → inteiro` | Escala `-100` a `100`. |
| Reputação por localidade | mapa `loc_id → inteiro` | Escala sem limite fixo; aplicada no fator de mercado. |
| Reputação por facção | mapa `faccao_id → inteiro` | Escala sem limite fixo. |

Faixas canônicas de relacionamento com NPC:

| Faixa | Intervalo |
|---|---|
| Inimigo Mortal | `-100` a `-61` |
| Hostil | `-60` a `-21` |
| Neutra | `-20` a `20` |
| Favorável | `21` a `60` |
| Aliado | `61` a `100` |

---

## RF-FP-10 — Consistência de cálculo

- Todos os cálculos de atributos derivados (RF-FP-06) devem usar aritmética de ponto flutuante internamente e `floor` (piso) onde especificado, garantindo mesmo resultado em qualquer ambiente de execução.
- A ficha do personagem deve ser recalculada sempre que qualquer atributo base, item equipado ou modificador ativo mudar.
