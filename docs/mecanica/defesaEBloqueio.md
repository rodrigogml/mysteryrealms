# Defesa e Bloqueio

Este documento define a separação canônica entre **Defesa** (evitar acerto) e **Bloqueio** (mitigar dano após acerto), além da ordem obrigatória de resolução no pipeline de combate.

## Definições formais

### Defesa (evita acerto)

**Defesa** é o limiar que determina se um ataque conecta ou falha.

- Se `TesteAcerto < DefesaFinal`, o ataque **erra** e a resolução ofensiva é encerrada para aquele alvo.
- Se `TesteAcerto >= DefesaFinal`, o ataque **acerta** e segue para mitigação.

Defesa não reduz dano; ela decide **acerto/falha**.

### Bloqueio (mitiga dano)

**Bloqueio** é a mitigação aplicada somente quando o acerto já foi confirmado.

- Bloqueio atua sobre o **dano bruto** antes das resistências.
- Bloqueio não transforma um acerto em erro; ele apenas reduz magnitude de dano.

## Ordem obrigatória de resolução

A sequência abaixo é mandatória para evitar dupla aplicação e inconsistências:

1. **Acerto** (validação de hit)
2. **Defesa** (`TesteAcerto` vs `DefesaFinal`)
3. **Bloqueio** (mitigação do dano bruto)
4. **Resistências** (mitigação por tipo)
5. **Aflições** (chance, duração e intensidade)

Regra operacional:
- Se falhar na etapa de defesa, as etapas 3 a 5 não são executadas para aquele evento ofensivo.

## Fórmulas canônicas

### 1) Teste de acerto contra Defesa

- `TesteAcerto = 1d20 + PrecisaoFinal`
- Critério de sucesso: `TesteAcerto >= DefesaFinal`

### 2) Mitigação por Bloqueio

Adota-se bloqueio percentual normalizado em `[0, 1]`:

- `DanoPosBloqueio = max(0, floor(DanoBruto × (1 - BloqueioPctFinal)))`

Observações:
- `BloqueioPctFinal = BloqueioFinal / 100`, com clamp em `[0, 1]` quando aplicável por regra de balanceamento.
- Efeitos que “substituem bloqueio” têm precedência sobre efeitos aditivos.

### 3) Mitigação por Resistência

Para o tipo de dano do evento:

- `DanoPosResistencia = max(0, floor(DanoPosBloqueio × (1 - ResistenciaTipo)))`

### 4) Aplicação de Aflições

Quando o ataque possui aflição associada, usar o modelo padrão:

- `ChanceFinal = max(min_chance, ChanceBaseAplicacao × (1 - ResistenciaAflicao))`
- `DuracaoFinal = max(1, floor(DuracaoBase × (1 - ResistenciaAflicao)))`
- `IntensidadeFinal = floor(IntensidadeBase × (1 - ResistenciaAflicao))`

## Exemplos curtos

### Exemplo A — ataque evitado na defesa

- `TesteAcerto = 17`
- `DefesaFinal = 22`

Como `17 < 22`, o ataque erra. **Nenhum dano** e **nenhuma aflição** são aplicados.

### Exemplo B — acerto com bloqueio e resistência

- `TesteAcerto = 25`, `DefesaFinal = 20` → acerto confirmado.
- `DanoBruto = 40`
- `BloqueioPctFinal = 0,25` → `DanoPosBloqueio = floor(40 × 0,75) = 30`
- `ResistenciaTipo = 0,20` → `DanoPosResistencia = floor(30 × 0,80) = 24`

**Dano final recebido: 24**.

## Integração com documentos canônicos

### Integração com `cicloDeBatalha.md`

- Este documento especializa o pipeline de resolução do combate e fixa a ordem em: acerto → defesa → bloqueio → resistências → aflições.
- Em caso de divergência de interpretação de mesa, prevalece esta ordem para eventos ofensivos direcionados.

### Integração com `escudos.md`

- Escudos contribuem em `BonusItemDefesa` e/ou `BonusItemBloqueio` conforme regra de equipamento ativo.
- Não acumular dois escudos ativos para somar bloqueio.

### Integração com `modificadores.md`

- Aplicar prioridade de origem e regras de empilhamento antes de consolidar `DefesaFinal` e `BloqueioFinal`.
- Evitar dupla aplicação: após calcular os valores finais, não repetir os mesmos modificadores nas etapas posteriores.

### Integração com `danosAflicoesResistencias.md`

- Tipos de dano, aflição e resistência devem usar exatamente as chaves canônicas do glossário.
- Resistências mitigam dano por tipo e também impactam aplicação de aflições, conforme fórmula própria.
