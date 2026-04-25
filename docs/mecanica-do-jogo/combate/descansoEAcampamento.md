# Descanso e Acampamento

## Objetivo Mecânico

Definir o sistema de **acampamento** como núcleo de recuperação fora de combate, equilibrando risco, tempo e ganho de recursos.

Este documento converte o tópico legado de acampamentos em regras operacionais compatíveis com Moral, Recursos Vitais e Ciclo de Encontro.

---

## Premissas de Sistema

- Acampamento é uma atividade de exploração com custo temporal.
- Descanso seguro é a principal fonte de recuperação profunda.
- Quanto maior o conforto/segurança, maior o ganho e menor o risco de interrupção.

---

## Estrutura do Acampamento

Cada acampamento possui um **Nível de Qualidade (NQ)** de 0 a 100.

<math>\text{NQ} = \text{clamp}(\text{BaseTerreno} + \text{Itens} + \text{Perícia} + \text{Condições}, 0, 100)</math>

Onde:
- `BaseTerreno`: adequação do local (abrigo natural, exposição, altitude etc.);
- `Itens`: bônus de barraca/fogueira/ferramentas;
- `Perícia`: resultado de preparação do grupo;
- `Condições`: clima, ameaças próximas e penalidades ambientais.

---

## Fases do Acampamento

1. **Escolha de local**
   - Define `BaseTerreno` e risco inicial de encontro.

2. **Montagem**
   - Consome tempo e Fadiga leve/moderada.
   - Gera `NQ` final.

3. **Período de descanso**
   - Recuperação por hora baseada em `NQ`.
   - Testes periódicos de interrupção (guarda/percepção).

4. **Desmontagem/saída**
   - Aplica custo de tempo final.
   - Consolida ganhos e estados remanescentes.

---

## Recuperação por Hora de Descanso

### Fadiga

<math>\Delta\text{Fadiga}_{hora} = -\text{Fadiga}_{\max}\times(0{,}04 + 0{,}0006\times\text{NQ})</math>

Referência:
- NQ 0 → 4,0% de `Fadiga_max` por hora.
- NQ 100 → 10,0% de `Fadiga_max` por hora.

### Moral

<math>\Delta\text{Moral}_{hora} = 1 + \lfloor\text{NQ}/25\rfloor</math>

Referência:
- NQ 0–24: +1/h.
- NQ 25–49: +2/h.
- NQ 50–74: +3/h.
- NQ 75–100: +4/h.

### Recuperação de Fome/Sede

- Descanso **não** reduz automaticamente Fome/Sede.
- A redução ocorre pelo consumo planejado durante o acampamento.
- Sem suprimentos, Fome/Sede continuam acumulando normalmente.

---

## Risco de Interrupção Noturna

Por bloco de 2 horas de descanso, realizar teste de interrupção:

<math>P(\text{interrupção}) = \text{clamp}(P_{base} - 0{,}25\times\text{NQ} + A_{ameaça} - B_{guarda}, 2, 85)</math>

Parâmetros:
- `P_base`: risco do bioma/zona;
- `A_ameaça`: atividade hostil local;
- `B_guarda`: mitigação por guarda ativa.

Se houver interrupção:
- encerra o bloco de descanso atual;
- aplica penalidade de Moral (`-2` a `-8`, conforme severidade);
- inicia encontro conforme ciclo de combate.

---

## Papéis de Grupo (opcional por design)

Para grupos, os papéis podem conceder bônus específicos:
- **Guarda:** reduz chance de interrupção.
- **Batedor:** melhora escolha de local (`BaseTerreno`).
- **Cozinheiro/Alquimista:** amplifica recuperação de Fome/Sede/Moral por consumo.

> Se jogo solo, esses bônus podem vir de talento/perícia única do personagem.

---

## Regras de Conflito e Limites

- No máximo **1 acampamento completo** por janela de sono (anti-exploit).
- Bônus de múltiplos itens iguais têm retorno decrescente.
- Interrupção cancela ganhos daquele bloco, mas preserva ganhos já concluídos.

---

## Critérios de "Pronto para Requisito"

- Fechar tabela de `P_base` por bioma e faixa de perigo.
- Definir catálogo oficial de itens de acampamento e bônus.
- Definir custo temporal de montagem/desmontagem por tipo de terreno.
- Integrar formalmente com sistema de encontros e passagem de tempo.
