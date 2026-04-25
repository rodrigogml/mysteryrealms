# Recuperação do Personagem

## Objetivo Mecânico

Definir um protocolo único de recuperação de **PV**, **Fadiga**, **Fadiga Mínima**, **Moral**, **Fome** e **Sede** para atividades fora de combate.

Este documento consolida descanso, sono e recuperação por itens/eventos em regras operacionais.

---

## Modos de Recuperação

1. **Descanso curto** (baixo risco, ganho moderado)
2. **Dormir** (alto ganho, requer janela de tempo e segurança)
3. **Consumo de itens/ações** (ganho imediato sob custo de recurso)
4. **Eventos narrativos** (recuperação excepcional, controlada por design)

---

## Descanso Curto

Recuperação base por minuto:

<math>\Delta\text{Fadiga}_{min} = -0{,}10\%\times\text{Fadiga}_{max}\times\text{FatorAtividade}</math>

Regras:
- `FatorAtividade` entre 0 e 1.
- Descanso curto não recupera `FadigaMinima`.
- Descanso curto não recupera PV por padrão.
- Se houver atividade leve (ex.: conversa, leitura, meditação), usar `FatorAtividade` reduzido.

---

## Dormir

Recuperação base por minuto:

- <math>\Delta\text{Fadiga}_{min} = -0{,}21\%\times\text{Fadiga}_{max}\times\text{FatorSono}</math>
- <math>\Delta\text{FadigaMinima}_{min} = -0{,}21\%\times\text{Fadiga}_{max}\times\text{FatorSono}</math>
- <math>\Delta\text{PV}_{min} = (\text{CON}/120)\times\text{FatorSono}</math>

Regras operacionais:
- Mínimo de **1h ininterrupta** para contar como sono.
- Interrupção antes de 1h converte o período em descanso curto.
- Remoção de modificadores de sono exige janela longa (baseline: 4h ininterruptas + condição de PV estável).

---

## Fator de Qualidade do Sono

Parâmetros:
- `C` (Conforto): 0..100
- `R` (Risco ambiente): 0..100
- `S` (Segurança aplicada): -50..100

Fórmula:

<math>\text{FatorSono} = 0{,}5 + \frac{3\times C + (100 - Max(0; R-S))}{800}</math>

Saída alvo:
- Mínimo prático: `0,5`
- Máximo: `1,0`

---

## Interrupção do Sono

Por bloco de 1h:
1. Executar teste de sono/interrupção.
2. Se sucesso: computar 1h de recuperação.
3. Se falha: interromper sono e disparar evento de risco.

Regra de tempo parcial em falha:
- Rolar minuto da interrupção no bloco e aplicar recuperação proporcional.
- Se nenhuma hora completa foi concluída, tratar recuperação acumulada como descanso curto.

---

## Recuperação por Itens e Ações

- Itens podem recuperar PV, Fadiga, Fome e Sede.
- `FadigaMinima` não deve ser recuperada por item de forma permanente.
- É permitido buff temporário em `FadigaMinima` com duração e efeito reversível.

Regras de segurança:
- Definir cooldown/custo para evitar cadeia infinita de consumo.
- Priorizar limites por categoria (poção, alimento, tônico etc.).

---

## Recuperação Especial (Narrativa)

Eventos de história podem restaurar parcial ou totalmente o personagem.

Diretriz:
- Uso excepcional e rastreável.
- Registrar origem e impacto para não quebrar metas de balanceamento.

---

## Critérios de "Pronto para Requisito"

- Fechar tabela oficial de `FatorAtividade` por tipo de ação.
- Fechar teste de sono (entrada, dificuldade, consequências).
- Definir limites finais de itens de recuperação por janela de tempo.
- Integrar com `recursosVitaisERecuperacao.md` e `descansoEAcampamento.md` sem sobreposição de regras.
