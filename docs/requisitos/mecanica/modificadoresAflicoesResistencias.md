# Requisitos — Modificadores, Aflições e Resistências

Requisitos funcionais do sistema para modificadores temporários, empilhamento, tipos de dano, aflições, resistências e ações do jogador.

Referências canônicas: `docs/mecanica/modificadores.md`, `docs/mecanica/danosAflicoesResistencias.md`, `docs/mecanica/acoesDoJogador.md`.

---

## RF-MAR-01 — Tipos de dano canônicos

| Exibição | Chave técnica |
|---|---|
| Corte | `corte` |
| Perfuração | `perfuracao` |
| Esmagamento | `esmagamento` |
| Fogo | `fogo` |
| Gelo | `gelo` |
| Raio | `raio` |
| Ácido | `acido` |
| Mágico Puro | `magia_pura` |
| Sangramento | `sangramento` |
| Veneno (Letal) | `veneno_letal` |

Compatibilidade legada: `Eletricidade` deve ser mapeado para `raio`.

---

## RF-MAR-02 — Tipos de aflição canônicos

| Exibição | Chave técnica |
|---|---|
| Psíquica | `psiquica` |
| Espiritual | `espiritual` |
| Medo | `medo` |
| Paralisia | `paralisia` |
| Cegueira | `cegueira` |
| Surdez / Mudez | `surdez_mudez` |
| Fadiga | `fadiga` |
| Doença Mágica | `doenca_magica` |
| Alucinação / Ilusão Persistente | `alucinacao_ilusao_persistente` |
| Sono / Torpor | `sono_torpor` |

---

## RF-MAR-03 — Tipos de resistência canônicos

| Exibição | Chave técnica |
|---|---|
| Corte | `corte` |
| Perfuração | `perfuracao` |
| Esmagamento | `esmagamento` |
| Fogo | `fogo` |
| Gelo | `gelo` |
| Raio | `raio` |
| Ácido | `acido` |
| Mágico Puro | `magia_pura` |
| Encantamento | `encantamento` |
| Veneno | `veneno` |
| Doença | `doenca` |
| Sangramento | `sangramento` |
| Fadiga | `fadiga` |
| Dor | `dor` |
| Som | `som` |
| Confusão | `confusao` |
| Ilusão | `ilusao` |
| Controle Mental | `controle_mental` |
| Corrupção Espiritual | `corrupcao_espiritual` |

---

## RF-MAR-04 — Estrutura de um modificador

Cada modificador deve armazenar:

| Campo | Tipo | Obrigatório | Regra |
|---|---|---|---|
| `id` | string | Sim | Slug sem acento, `snake_case`. |
| `nome_exibicao` | string | Sim | Nome legível com acento. |
| `gatilho` | string | Sim | Condição de aplicação. |
| `efeito` | objeto | Sim | Descrição mecânica objetiva. |
| `duracao` | objeto | Sim | Em turnos, minutos ou permanente. |
| `regra_empilhamento` | string | Sim | Acumula, substitui ou invalida. |
| `origem` | enum | Sim | Conforme RF-MAR-05. |

---

## RF-MAR-05 — Prioridade de origem de modificadores

Ordem canônica (maior para menor):

1. Estado crítico de combate
2. Habilidade ativa
3. Equipamento
4. Classe
5. Raça
6. Efeito temporário genérico

---

## RF-MAR-06 — Regras de empilhamento

- Modificadores de mesma origem e mesmo tipo acumulam, salvo exceção explícita.
- Se houver conflito excludente no mesmo nível de prioridade:
  - Aplica-se o de maior magnitude absoluta.
  - Empate de magnitude: vence menor duração restante.
  - Novo empate: vence o efeito mais recente.
- Modificadores de origens diferentes sempre acumulam entre si.

---

## RF-MAR-07 — Catálogo de classes de ações do jogador

O sistema deve classificar as ações em quatro categorias canônicas:

| Categoria | Exemplos |
|---|---|
| **Passiva** | Observação, conversa breve, organização de inventário parado. |
| **Moderada** | Movimento padrão, coleta simples, interação tática leve. |
| **Exigente** | Corrida prolongada, escalada difícil, combate intenso. |
| **Recuperativa** | Descanso curto, hidratação, refeição leve, respiração guiada. |

---

## RF-MAR-08 — Custos base por classe de ação

| Classe | `tempo_gasto_min` | `custo_fadiga` | Impacto `fome_pct` | Impacto `sede_pct` |
|---|---:|---:|---:|---:|
| Passiva | 5 | +2 | +0,05 | +0,15 |
| Moderada | 10 | +8 | +0,20 | +0,60 |
| Exigente | 15 | +20 | +0,50 | +1,20 |
| Recuperativa | 20 | -12 | -0,40 a +0,10 * | -0,80 a +0,10 * |

\* O delta exato de `fome_pct`/`sede_pct` em ações recuperativas depende do tipo (refeição, hidratação, repouso).

---

## RF-MAR-09 — Regras de ajuste de ações por contexto

Aplicar os modificadores na ordem abaixo, sem dupla contagem:

### 1) Base da ação (RF-MAR-08)

### 2) Clima

| Condição | Ajuste |
|---|---|
| Calor forte / ambiente seco | `+20%` em `custo_fadiga`; `+35%` no impacto de `sede_pct`. |
| Frio intenso | `+15%` em `custo_fadiga`; ações passivas ao relento: `+10%` em `fome_pct`. |
| Chuva/vento forte | `+10%` em `tempo_gasto_min` para ações moderadas/exigentes de deslocamento. |

### 3) Carga

| Razão `carga_atual / capacidade_carga` | Ajuste |
|---|---|
| até 50% | Sem ajuste. |
| >50% e <=80% | `+10%` em `custo_fadiga` para moderadas/exigentes. |
| >80% e <=100% | `+25%` em `custo_fadiga`; `+10%` em `tempo_gasto_min`. |
| >100% | Deslocamento exigente bloqueado; demais ações: `+50%` em `custo_fadiga`. |

### 4) Estado fisiológico

| Estado ativo | Ajuste |
|---|---|
| `exaustao` | `+15%` em `custo_fadiga`. |
| `sede` | `+10%` em `custo_fadiga`. |
| `sede_agravada` | `+25%` em `custo_fadiga`; `+15%` em `sede_pct`. |
| `fome` | `+10%` em `custo_fadiga`. |
| `fome_agravada` | `+20%` em `custo_fadiga`; `+10%` em `fome_pct`. |
| `desmaio_*` ou `estado_critico` | Ação cancelada; ciclo transferido para resolução de colapso (RF-EF-10). |

### 5) Equipamentos

| Condição | Ajuste |
|---|---|
| Equipamentos com ergonomia/mobilidade | Reduz `custo_fadiga` em `5% a 20%` conforme qualidade. |
| Armadura pesada sem proficiência | `+10%` em `custo_fadiga` para moderadas/exigentes. |
| Itens de suporte (cantil, ração, estimulante) | Podem reduzir `fome_pct`/`sede_pct` na ação recuperativa, sem ignorar limites fisiológicos. |

### 6) Clamp final

- Respeitar limites de empilhamento fisiológico de RF-EF-05.
- `fome_pct` e `sede_pct` devem permanecer no intervalo `[0, 100]`.
- Recalcular faixas de estado fisiológico imediatamente após atualizar os valores.
