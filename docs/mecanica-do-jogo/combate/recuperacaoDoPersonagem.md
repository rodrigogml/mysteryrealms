# Recuperacao do Personagem

## Objetivo Mecanico

Definir um protocolo unico de recuperacao de **PV**, **Fadiga**, **Fadiga Minima**, **Moral**, **Fome** e **Sede** para atividades fora de combate.

Este documento consolida descanso, sono e recuperacao por itens/eventos em regras operacionais.

---

## Modos de Recuperacao

1. **Descanso curto** (baixo risco, ganho moderado)
2. **Dormir** (alto ganho, requer janela de tempo e seguranca)
3. **Consumo de itens/acoes** (ganho imediato sob custo de recurso)
4. **Eventos narrativos** (recuperacao excepcional, controlada por design)

---

## Descanso Curto

Recuperacao base por minuto:

<math>\Delta\text{Fadiga}_{min} = -0{,}10\%\times\text{Fadiga}_{max}\times\text{FatorAtividade}</math>

Regras:
- `FatorAtividade` entre 0 e 1.
- Descanso curto nao recupera `FadigaMinima`.
- Descanso curto nao recupera PV por padrao.
- Se houver atividade leve (ex.: conversa, leitura, meditacao), usar `FatorAtividade` reduzido.

---

## Dormir

Recuperacao base por minuto:

- <math>\Delta\text{Fadiga}_{min} = -0{,}21\%\times\text{Fadiga}_{max}\times\text{FatorSono}</math>
- <math>\Delta\text{FadigaMinima}_{min} = -0{,}21\%\times\text{Fadiga}_{max}\times\text{FatorSono}</math>
- <math>\Delta\text{PV}_{min} = (\text{CON}/120)\times\text{FatorSono}</math>

Regras operacionais:
- Minimo de **1h ininterrupta** para contar como sono.
- Interrupcao antes de 1h converte o periodo em descanso curto.
- Remocao de modificadores de sono exige janela longa (baseline: 4h ininterruptas + condicao de PV estavel).

---

## Fator de Qualidade do Sono

Parametros:
- `C` (Conforto): 0..100
- `R` (Risco ambiente): 0..100
- `S` (Seguranca aplicada): -50..100

Formula:

<math>\text{FatorSono} = 0{,}5 + \frac{3\times C + (100 - Max(0; R-S))}{800}</math>

Saida alvo:
- Minimo pratico: `0,5`
- Maximo: `1,0`

---

## Interrupcao do Sono

Por bloco de 1h:
1. Executar teste de sono/interrupcao.
2. Se sucesso: computar 1h de recuperacao.
3. Se falha: interromper sono e disparar evento de risco.

Regra de tempo parcial em falha:
- Rolar minuto da interrupcao no bloco e aplicar recuperacao proporcional.
- Se nenhuma hora completa foi concluida, tratar recuperacao acumulada como descanso curto.

---

## Recuperacao por Itens e Acoes

- Itens podem recuperar PV, Fadiga, Fome e Sede.
- `FadigaMinima` nao deve ser recuperada por item de forma permanente.
- E permitido buff temporario em `FadigaMinima` com duracao e efeito reversivel.

Regras de seguranca:
- Definir cooldown/custo para evitar cadeia infinita de consumo.
- Priorizar limites por categoria (pocao, alimento, tonico etc.).

---

## Recuperacao Especial (Narrativa)

Eventos de historia podem restaurar parcial ou totalmente o personagem.

Diretriz:
- Uso excepcional e rastreavel.
- Registrar origem e impacto para nao quebrar metas de balanceamento.

---

## Criterios de "Pronto para Requisito"

- Fechar tabela oficial de `FatorAtividade` por tipo de acao.
- Fechar teste de sono (entrada, dificuldade, consequencias).
- Definir limites finais de itens de recuperacao por janela de tempo.
- Integrar com `recursosVitaisERecuperacao.md` e `descansoEAcampamento.md` sem sobreposicao de regras.
