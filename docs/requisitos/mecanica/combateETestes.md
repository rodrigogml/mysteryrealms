# Requisitos — Combate e Testes

Requisitos funcionais do sistema para testes de jogo, iniciativa, ciclo de batalha, pipeline de resolução, defesa e bloqueio.

Referências canônicas: `docs/mecanica/testesDeJogo.md`, `docs/mecanica/cicloDeBatalha.md`, `docs/mecanica/defesaEBloqueio.md`, `docs/mecanica/danosAflicoesResistencias.md`.

---

## RF-CT-01 — Fórmula de teste

```
resultado_teste = valor_base + 1d20 + modificadores
```

Onde:
- `valor_base`: habilidade canônica (ex.: `persuasao`, `furtividade`) quando existir; caso contrário, atributo canônico coerente com a ação (ex.: `forca`, `intelecto`).
- `modificadores`: bônus/penalidades temporários ativos.

---

## RF-CT-02 — Critério de sucesso por CD

```
resultado_teste >= cd  →  sucesso
resultado_teste <  cd  →  falha
```

### Tabela de referência de CD por tier

| Tier de progresso | Muito Fácil | Fácil | Médio | Difícil | Muito Difícil | Heroico |
|---|---:|---:|---:|---:|---:|---:|
| Inicial | 8 | 10 | 12 | 14 | 16 | 18 |
| Intermediário | 10 | 12 | 14 | 16 | 18 | 20 |
| Avançado | 12 | 14 | 16 | 18 | 20 | 22 |

Ajuste situacional permitido: `±2` quando fatores de tempo, visibilidade, equipamento ou pressão narrativa forem relevantes.

---

## RF-CT-03 — Vantagem e desvantagem

- **Vantagem:** rolar `2d20`, manter o maior.
- **Desvantagem:** rolar `2d20`, manter o menor.

---

## RF-CT-04 — Teste de iniciativa

```
iniciativa = 1d20 + destreza + percepcao
```

**Desempate:**
1. Maior `destreza`.
2. Maior `percepcao`.
3. Sorteio.

A ordem de iniciativa permanece fixa até o fim do combate, salvo efeito explícito que a altere.

---

## RF-CT-05 — Teste de percepção

- `percepcao + 1d20` vs `furtividade + 1d20` do oculto (teste oposto).
- `percepcao + 1d20` vs `cd_ambiente` (CD fixa do ambiente).
- Maior resultado detecta primeiro; em caso de empate, prevalece o oculto.

---

## RF-CT-06 — Ciclo de batalha

### Etapa 1 — Encontro

Combate inicia ao ocorrer contato hostil por narrativa, evento fixo ou encontro aleatório.

### Etapa 2 — Percepção inicial

Executar teste de percepção conforme RF-CT-05.
Quem vence pode ganhar vantagem tática (surpresa, reposicionamento, abertura de turno).

### Etapa 3 — Iniciativa

Todos os participantes rolam iniciativa (RF-CT-04).

### Etapa 4 — Rodadas e turnos

Cada participante possui por turno:

| Componente | Regra |
|---|---|
| **Ação de pré-turno** | Ação rápida; não substitui ação principal (trocar arma, consumir item simples, ajuste de posição curto). |
| **Ação de turno** | Ação principal da rodada; uma por turno (atacar, assumir postura defensiva, fugir, conjurar, apoiar, interagir com ambiente, intimidar, preparar ação). |
| **Reação** | 1 por rodada; disparada por gatilho válido fora do próprio turno; recuperada no início do próximo turno do participante. |

#### Gatilhos comuns de reação

- Ataque corpo a corpo entrando em alcance de ameaça.
- Inimigo saindo do alcance de ameaça sem usar regra de fuga.
- Conjuração hostil em alcance curto quando existir habilidade de interrupção.
- Gatilho explícito de habilidade/equipamento (contra-ataque, aparar, contramágica, etc.).

Se múltiplos gatilhos ocorrerem na mesma janela, o participante escolhe apenas 1 para consumir a reação disponível.

#### Movimento por turno

| Tipo | Distância | Custo |
|---|---|---|
| Movimento curto | até 3m | Gratuito; pode ser acoplado à ação de pré-turno. |
| Movimento padrão | até 9m | Consome ação de turno. |
| Movimento estendido | até 18m | Consome ação de turno e aplica `-2` em testes de acerto/defesa até o próximo turno. |

- Movimento estendido não pode ser combinado com ataque ou conjuração na mesma ação de turno, salvo efeito explícito.
- Conversão para grid: 1 quadrado = 1,5m.

### Etapa 5 — Resolução mecânica

Executar o pipeline de resolução (RF-CT-07) conforme a ação.

### Etapa 6 — Encerramento

O combate termina quando:
- Todos os inimigos são derrotados.
- Fuga bem-sucedida.
- Derrota do grupo/jogador.

Recompensas possíveis: XP, loot, progresso narrativo.

---

## RF-CT-07 — Pipeline de resolução de combate

Executar as etapas abaixo **na ordem** para cada evento ofensivo:

1. Validar ação e alvo.
2. Teste de acerto.
3. Mitigação ativa (bloqueio/defesa).
4. Cálculo de dano bruto.
5. Aplicação de resistência por tipo.
6. Aplicação de aflições e testes associados.
7. Atualização de estado (PV, fadiga, duração de modificadores).

**Regra de integração:** modificadores percentuais/flat já consolidados nas fórmulas finais de `definicaoDePersonagem.md` **não** devem ser reaplicados nas etapas 5 a 7.

---

## RF-CT-08 — Defesa: teste de acerto vs DefesaFinal

```
teste_acerto = 1d20 + precisao_final
```

- `teste_acerto >= defesa_final` → acerto confirmado; seguir para RF-CT-09.
- `teste_acerto < defesa_final` → ataque erra; nenhum dano ou aflição é aplicado.

---

## RF-CT-09 — Bloqueio: mitigação de dano após acerto

```
dano_pos_bloqueio = max(0, floor(dano_bruto × (1 - bloqueio_pct_final)))
```

Onde:
- `bloqueio_pct_final = bloqueio_final / 100`, com clamp em `[0, 1]`.
- Efeitos que "substituem bloqueio" têm precedência sobre efeitos aditivos.

---

## RF-CT-10 — Resistência: mitigação por tipo de dano

```
dano_pos_resistencia = max(0, floor(dano_pos_bloqueio × (1 - resistencia_tipo)))
```

Onde `resistencia_tipo` corresponde ao tipo de dano do evento (chave canônica do glossário).

Limites:
- Resistência de jogador normalmente limitada a `80%`.
- Criaturas podem atingir `100%` (imunidade).

---

## RF-CT-11 — Aflições: aplicação após resistência

Quando o ataque possui aflição associada:

```
chance_final     = max(min_chance, chance_base_aplicacao × (1 - resistencia_aflicao))
duracao_final    = max(1, floor(duracao_base × (1 - resistencia_aflicao)))
intensidade_final = floor(intensidade_base × (1 - resistencia_aflicao))
```

- `min_chance` evita chance nula quando não há imunidade explícita.
- Para imunidade explícita: `resistencia_aflicao = 1` e `chance_final = 0`.

#### Exceções canônicas

- **Medo (`medo`):** `resistencia_aflicao` reduz principalmente `chance_final` e `duracao_final`; `intensidade_final` pode permanecer fixa por tier.
- **Sangramento (`sangramento`):** `intensidade_final` é tratada como dano por turno; `resistencia_aflicao` reduz esse dano por turno.

---

## RF-CT-12 — Ação preparada

- `Preparar ação` consome a ação de turno atual.
- Cria uma condição de disparo e uma ação futura.
- No disparo válido, a ação preparada acontece como reação e consome a reação da rodada.

A ação preparada é cancelada se qualquer condição abaixo ocorrer antes do disparo:
1. Personagem afetado por `paralisia`, `sono_torpor` ou `cegueira` (quando a ação exigir alvo visual).
2. Personagem sofrer deslocamento forçado maior que 3m.
3. O gatilho declarado tornar-se inválido (alvo cai, sai de alcance ou perde linha de efeito).
4. O início do próximo turno do personagem ocorrer sem o gatilho ser atendido.

Se cancelada, o custo da ação de turno não é reembolsado.

---

## RF-CT-13 — Fuga (teste oposto)

`Fugir` usa teste oposto:
- **Quem foge:** `atletismo` (forçar passagem) **ou** `acrobacia` (evasão), à escolha de quem foge.
- **Quem impede:** `atletismo` (bloqueio físico) **ou** `percepcao` (antecipação), à escolha de quem impede.

**Procedimento:**
1. Quem foge declara direção/rota.
2. Resolve-se o teste oposto.
3. **Sucesso:** deslocamento de até 9m para fora da zona de ameaça; aplica estado `em_fuga` até o próximo turno.
4. **Falha:** sem deslocamento; ação de turno consumida; oponente realiza 1 ataque de oportunidade imediato (sem custo de reação, por ser efeito da regra de falha de fuga).

`Fugir` sempre consome a ação de turno; a ação de pré-turno ainda pode ser usada normalmente.
