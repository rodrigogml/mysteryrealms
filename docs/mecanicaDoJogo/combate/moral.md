# Moral

## Objetivo Mecânico

A **Moral** representa estabilidade emocional e prontidão mental durante exploração, combate e cenas sociais.

Nesta mecânica unificada, Moral é um **recurso de estado** (0 a 100) que altera desempenho, risco de colapso e resistência a efeitos mentais.

---

## Escala e Faixas de Estado

- **0–20 (Colapso):** alto risco de pânico/fuga; bloqueio de ações complexas sob pressão.
- **21–50 (Baixa):** penalidades moderadas em testes mentais e sociais.
- **51–80 (Estável):** sem bônus/penalidades de base.
- **81–100 (Elevada):** bônus moderados em testes de Vontade e ações inspiradoras.

> Convenção: efeitos exatos por faixa devem ser sempre aplicados por modificadores explícitos (ex.: `MORAL_BAIXA`, `MORAL_ELEVADA`).

---

## Regra de Atualização de Moral

A Moral muda por **eventos relevantes** (combate, narrativa, descanso, condições físicas).

**Fórmula canônica por evento:**

<math>\text{Moral}_{\text{nova}} = \text{clamp}(\text{Moral}_{\text{atual}} + \Delta\text{Moral}_{\text{evento}}, 0, 100)</math>

Onde:
- `clamp(x,0,100)` limita o resultado ao intervalo [0,100];
- `ΔMoral_evento` é definido pela tabela de eventos abaixo.

### Tabela base de variação por evento

- Derrota relevante: **-8**
- Aliado incapacitado/morto em cena: **-10**
- Vitória contra ameaça superior: **+6**
- Cumprimento de objetivo significativo: **+8**
- Descanso completo seguro: **+10**
- Falha crítica em cena de alto impacto: **-6**

> Os valores são baseline de design e poderão ser recalibrados na fase de formalização de requisitos.

---

## Interações com outros sistemas

1. **Combate**
   - Moral baixa amplia vulnerabilidade a efeitos de Medo/Pânico.
   - Moral elevada melhora consistência de ações de suporte/inspiração.

2. **Aflições e Estados**
   - Estados mentais reduzem Moral ao aplicar e/ou por turno.
   - Remoção de estados mentais pode conceder recuperação parcial de Moral.

3. **Recursos vitais**
   - Fadiga, Fome e Sede em estágios críticos geram penalidade periódica de Moral.

---

## Regras de Segurança e Antiexploração

- Moral não pode oscilar infinitamente no mesmo evento (aplicar no máximo 1 ajuste por gatilho único).
- Ganhos de Moral por repetição de evento trivial devem sofrer redução progressiva.
- Em combate, a recuperação instantânea de Moral é limitada a **1 evento positivo por rodada**.

---

## Critérios de "Pronto para Requisito"

- Tabela completa de eventos com `ΔMoral` por categoria de conteúdo.
- Lista fechada de modificadores acionados por faixa de Moral.
- Definição final dos testes impactados por Moral (combate, social, magia).
- Regras de UI/telemetria (quando alertar mudança de faixa ao jogador).
