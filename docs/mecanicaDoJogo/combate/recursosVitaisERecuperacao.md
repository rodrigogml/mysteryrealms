# Recursos Vitais e Recuperação

## Objetivo Mecânico

Consolidar **Fadiga, Fome e Sede** como recursos contínuos de sobrevivência, com impacto direto em combate e exploração.

A proposta substitui descrições narrativas por regras operacionais, mantendo espaço para balanceamento futuro.

---

## Modelo Unificado

Todos os recursos vitais usam escala percentual de **0 a 100**:

- `0`: sem pressão do recurso.
- `100`: colapso daquele recurso.

Cada recurso possui:
- taxa de acúmulo por minuto (baseline);
- aceleradores contextuais;
- estágios com modificadores;
- regras de recuperação.

---

## Fadiga

### Limites

<math>\text{Fadiga}_{\max} = (\text{CON} + \text{VON}) \times 100</math>

- **Fadiga Atual**: valor dinâmico por ação/tempo.
- **Fadiga Mínima**: piso que sobe com tempo acordado.

### Crescimento da Fadiga Mínima

<math>\text{Fadiga}_{\min} += \frac{1}{1440}\times\text{Fadiga}_{\max}\;\text{por minuto acordado}</math>

Após 24h acordado, o piso alcança 100% de `Fadiga_max`.

### Exaustão e Colapso

- Exaustão inicia quando `Fadiga_Atual > Fadiga_max`.
- Limite de colapso por fadiga: `120%` de `Fadiga_max`.

<math>\text{LimiteColapsoFadiga} = 1{,}2 \times \text{Fadiga}_{\max}</math>

---

## Sede

### Acúmulo base por minuto

<math>\text{Sede}_{\text{min}} = \frac{100}{48\times60} \times \text{FatorAmbiental}</math>

### Estágios operacionais

- **0–24**: sem modificador.
- **25–64**: aplica modificador `SEDE`.
- **65–99**: aplica modificador `SEDE_AGRAVADA`.
- **100**: colapso por desidratação.

---

## Fome

### Acúmulo base por minuto

<math>\text{Fome}_{\text{min}} = \frac{100}{7\times24\times60} \times \text{FatorAmbiental}</math>

### Estágios operacionais

- **0–42**: sem modificador.
- **43–84**: aplica modificador `FOME`.
- **85–99**: aplica modificador `FOME_AGRAVADA`.
- **100**: colapso por inanição aguda.

---

## Ordem de Processamento por Tick (tempo)

Em cada tick temporal (por minuto de jogo):

1. Atualizar Fome/Sede por taxa e aceleradores.
2. Atualizar Fadiga Mínima (se acordado).
3. Aplicar custos de ação em Fadiga Atual.
4. Recalcular estágios e modificadores.
5. Verificar gatilhos de colapso.

> Se múltiplos colapsos ocorrerem no mesmo tick, registrar o primeiro gatilho e aplicar estado de incapacidade único.

---

## Recuperação

### Regras gerais

- **Dormir** reduz Fadiga Atual e Fadiga Mínima.
- **Hidratar** reduz Sede.
- **Alimentar** reduz Fome.
- Recuperação nunca reduz abaixo de 0.

### Taxas baseline de recuperação (provisórias)

- Dormir em condição segura: `-8% Fadiga_max` por hora.
- Hidratação adequada: `-20` pontos de Sede por consumo padrão.
- Refeição completa: `-25` pontos de Fome por consumo padrão.

> Valores são placeholders de balanceamento para validação de ritmo de sobrevivência.

---

## Integração com Moral e Combate

- Estágios agravados de Fome/Sede/Fadiga causam penalidade periódica de Moral.
- Em exaustão, o orçamento de poder por turno deve ser reduzido.
- Em colapso, personagem entra em incapacidade temporária e exige recuperação antes de retomar ações plenas.

---

## Critérios de "Pronto para Requisito"

- Definir duração exata de incapacidade por tipo de colapso.
- Fechar catálogo de aceleradores ambientais por bioma/clima.
- Vincular cada estágio a modificadores numéricos finalizados.
- Definir políticas de pilha quando Fome, Sede e Exaustão coexistirem.
