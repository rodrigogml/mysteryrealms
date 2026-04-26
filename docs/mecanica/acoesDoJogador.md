# Ações do Jogador

Este documento padroniza o catálogo de ações jogáveis, os custos fisiológicos base e as regras de ajuste por contexto.

## Catálogo de ações

As ações são classificadas em quatro grupos canônicos:

- **Passivas**: baixo esforço e baixo risco mecânico (observação, conversa breve, organização de inventário parado).
- **Moderadas**: esforço controlado e uso regular de recursos (movimento padrão, coleta simples, interação tática leve).
- **Exigentes**: alto esforço físico/mental e maior desgaste (corrida prolongada, escalada difícil, combate intenso).
- **Recuperativas**: foco em recuperação parcial de estado (descanso curto, hidratação, refeição leve, respiração guiada).

## Tabela padrão de custos e impacto fisiológico

A tabela abaixo é a referência base para cálculo por ação antes de modificadores contextuais.

| Classe da ação | `tempo_gasto_min` | `custo_fadiga` | Impacto em `fome_pct` | Impacto em `sede_pct` |
|---|---:|---:|---:|---:|
| Passiva | 5 | +2 | +0,05 | +0,15 |
| Moderada | 10 | +8 | +0,20 | +0,60 |
| Exigente | 15 | +20 | +0,50 | +1,20 |
| Recuperativa | 20 | -12 | -0,40 a +0,10* | -0,80 a +0,10* |

\* Em ações recuperativas, o delta exato de `fome_pct` e `sede_pct` depende do tipo da recuperação (ex.: refeição reduz `fome_pct`, hidratação reduz `sede_pct`, repouso sem consumo pode manter ou elevar levemente por passagem de tempo).

## Regras de ajuste por contexto

Aplicar os ajustes na ordem abaixo para evitar dupla contagem.

1. **Base da ação** (tabela padrão).
2. **Clima**.
3. **Carga**.
4. **Estado crítico/fisiológico**.
5. **Equipamentos e bônus situacionais**.
6. **Clamp final e atualização de estado**.

### 1) Clima

- **Calor forte / ambiente seco**: `+20%` em `custo_fadiga` e `+35%` no impacto de `sede_pct`.
- **Frio intenso**: `+15%` em `custo_fadiga`; em ações passivas ao relento, `+10%` no impacto de `fome_pct`.
- **Chuva/vento forte**: `+10%` em `tempo_gasto_min` para ações moderadas/exigentes de deslocamento.

### 2) Carga

Considerar razão `carga_atual / capacidade_carga`:

- **até 50%**: sem ajuste.
- **>50% e <=80%**: `+10%` em `custo_fadiga` para ações moderadas/exigentes.
- **>80% e <=100%**: `+25%` em `custo_fadiga`, `+10%` em `tempo_gasto_min`.
- **>100%**: ação de deslocamento exigente é bloqueada; demais ações sofrem `+50%` em `custo_fadiga`.

### 3) Estado crítico e estado fisiológico

Aplicar multiplicadores conforme estado ativo no momento da ação:

- `exaustao`: `+15%` em `custo_fadiga`.
- `sede`: `+10%` em `custo_fadiga`.
- `sede_agravada`: `+25%` em `custo_fadiga` e `+15%` no impacto de `sede_pct`.
- `fome`: `+10%` em `custo_fadiga`.
- `fome_agravada`: `+20%` em `custo_fadiga` e `+10%` no impacto de `fome_pct`.
- `desmaio_*` ou `estado_critico` impeditivo: ação cancelada e ciclo transferido para resolução de colapso.

### 4) Equipamentos

- Equipamentos com ergonomia/mobilidade: reduzem `custo_fadiga` em `5% a 20%` conforme qualidade.
- Armaduras pesadas sem proficiência: `+10%` em `custo_fadiga` para ações moderadas/exigentes.
- Itens de suporte (cantil, ração, estimulante): podem reduzir o impacto de `fome_pct`/`sede_pct` no tick da ação recuperativa, sem ignorar limites fisiológicos.

### 5) Clamp final

Após todos os modificadores:

- Respeitar limites de empilhamento fisiológico já definidos para custos por ação.
- `fome_pct` e `sede_pct` devem permanecer no intervalo `[0, 100]`.
- Recalcular faixas de estado fisiológico imediatamente após atualizar os valores.

## Integrações obrigatórias

- Estados e faixas fisiológicas: consultar `docs/mecanica/fadigaFomeSede.md`.
- Regras de repouso/sono e recuperação: consultar `docs/mecanica/recuperacao.md`.
- Deslocamento e consumo por tempo de viagem: consultar `docs/mundo/mapaEMovimentacao.md`.
- Janela de ação por turno/reação em combate: consultar `docs/mecanica/cicloDeBatalha.md`.

## Exemplo rápido de aplicação

Ação exigente de deslocamento sob calor forte, com carga em 85%, personagem em `sede`:

- Base exigente: `tempo_gasto_min=15`, `custo_fadiga=20`, `sede_pct=+1,20`.
- Clima quente: `custo_fadiga=24`, `sede_pct=+1,62`.
- Carga 85%: `custo_fadiga=30`, `tempo_gasto_min=16,5`.
- Estado `sede`: `custo_fadiga=33`.

Resultado final do tick da ação: `tempo_gasto_min≈17`, `custo_fadiga=33`, `sede_pct=+1,62` (antes de outros efeitos externos).
