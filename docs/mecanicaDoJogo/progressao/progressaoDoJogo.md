# Progressão do Jogo

## Objetivo Mecânico

Definir a progressão de personagem como um sistema de **curva de poder previsível**, com marcos operacionais por faixa, controle de empilhamento de bônus e protocolo de desbloqueio de proficiência/especialização/maestria.

Este documento conecta criação de personagem (Raças e Classes), desempenho em combate e evolução contínua sem teto fixo de nível.

---

## Definição Operacional

- Progressão é regida por dois eixos:
  1. **Eixo de Nível** (evolução contínua por XP);
  2. **Eixo de Faixa de Poder** (pacotes de desbloqueio mecânico por intervalos de nível).
- O nível sobe por XP acumulado total e nunca reduz.
- Faixa de poder define os limites de acúmulo de bônus, acessos de build e complexidade esperada de combate.
- Toda evolução deve respeitar o orçamento de poder e métricas de balanceamento já definidas no sistema de combate.

---

## Curva de XP

### Fórmula Canônica

Para manter compatibilidade com o legado, o custo de XP do próximo nível segue:

`xpParaProximoNivel(n) = A * (ln(n + B))^C`

Onde:
- `n` = nível atual;
- `A` = escala base da curva;
- `B` = deslocamento inicial;
- `C` = curvatura do crescimento.

XP acumulado até o nível `n`:

`xpAcumulado(n) = Σ(i=1..n) A * (ln(i + B))^C`

### Parâmetros baseline da fase

- `A = 50`
- `B = 10`
- `C = 10`

> Esses parâmetros permanecem como baseline documental até o ciclo de calibragem quantitativa.

---

## Faixas de Poder e Marcos

| Faixa | Intervalo de nível | Papel sistêmico | Desbloqueios mínimos |
|---|---|---|---|
| F1 | 1–9 | Fundamentos | proficiências iniciais de classe, 1 trilha tática principal |
| F2 | 10–19 | Especialização | especialização em 1 família de arma/item, redução parcial de penalidades |
| F3 | 20–39 | Sinergia avançada | 2ª especialização situacional, acesso a combinações táticas compostas |
| F4 | 40+ | Maestria e otimização | maestria em 1 eixo principal, mitigação avançada de custo de execução |

Regras de transição:
1. Mudança de faixa ocorre imediatamente ao alcançar o primeiro nível do novo intervalo.
2. Desbloqueios de faixa só ativam se pré-requisitos de Classe forem atendidos.
3. Benefícios de faixa não podem quebrar limite de mitigação sustentada do sistema de combate.

---

## Teto de Acúmulo por Faixa

Para controlar explosão de poder, aplicar teto operacional por origem de bônus:

| Faixa | Teto de bônus de origem (Raça + Classe) | Teto de bônus de equipamento | Teto de mitigação efetiva* |
|---|---|---|---|
| F1 | baixo | baixo | até 50% |
| F2 | médio | baixo-médio | até 60% |
| F3 | médio-alto | médio | até 70% |
| F4 | alto | médio-alto | até 80% |

\* Mitigação efetiva = bloqueio + resistência aplicada no resultado final.

Regras:
- Quando o teto de uma fonte é atingido, novos bônus da mesma fonte viram bônus condicionais/situacionais.
- Teto de mitigação segue o limite sistêmico já definido em metas de balanceamento.

---

## Protocolo de Proficiência, Especialização e Maestria

### Estados de domínio

1. **Proficiência**: uso funcional sem penalidade-base do tipo/subtipo.
2. **Especialização**: bônus situacional e desbloqueio de interação avançada.
3. **Maestria**: redução de custo de execução e confiabilidade tática ampliada.

### Regras de desbloqueio

- Proficiência inicial vem da Classe (com exceções explicitadas em Raça).
- Especialização exige:
  - nível mínimo da faixa correspondente;
  - uso recorrente do eixo (arma/item) ou marco de classe equivalente;
  - ausência de incompatibilidade mecânica explícita.
- Maestria exige:
  - acesso à F4;
  - especialização prévia no mesmo eixo;
  - custo de oportunidade (limite de quantas maestrias simultâneas por personagem).

### Regras de conflito

- Não acumular especialização e maestria de eixos mutuamente exclusivos no mesmo turno de resolução.
- Em conflito entre domínio do personagem e restrição de equipamento, a restrição prevalece.

---

## Integração com Outros Sistemas

### Raças e Classes

- Faixas de progressão definem quando bônus de origem deixam de ser lineares e passam a ser condicionais.
- Progressão é a referência oficial para teto de soma entre origem (`Raça`) e função (`Classe`).

### Tipos de Armas

- Cada tipo de arma deve mapear em que faixa libera especialização e maestria.
- Tipos com alto impacto tático devem exigir marcos mais tardios para otimização plena.

### Itens de Mão

- Combinações avançadas (ex.: dupla empunhadura otimizada, foco + arma sem penalidade) dependem de marcos de faixa.
- Troca, ativação e passivos de itens devem respeitar estágio de domínio no eixo correspondente.

### Fórmulas de Combate e Metas de Balanceamento

- Progressão injeta parâmetros; não altera ordem canônica das fórmulas.
- Ajustes de faixa não podem violar metas-alvo de duração de combate, TTK e mitigação.

---

## Limites e Casos de Borda

- **Level infinito:** permitido, porém novos níveis além da última faixa não criam novos estados de domínio sem revisão sistêmica.
- **Power spike por combinação extrema:** quando exceder teto por faixa, converte excedente em bônus condicional com gatilho restritivo.
- **Respec de build:** exige recálculo de marcos já desbloqueados e nunca pode manter benefícios incompatíveis simultâneos.
- **Personagem híbrido multi-eixo:** deve pagar custo de versatilidade (atraso de 1 marco em ao menos um eixo principal).
- **Conteúdo sazonal/evento:** bônus temporários não alteram faixa, apenas estado transitório.

---

## Pronto para Requisito

Para este tópico ser considerado pronto para requisito funcional, ainda falta:

1. Definir valores numéricos oficiais de teto por faixa (em vez de faixas qualitativas baixo/médio/alto).
2. Fechar tabela de marcos por classe individual (nível exato de cada desbloqueio).
3. Especificar custo formal de versatilidade e limite de maestrias simultâneas.
4. Integrar planilha de curva XP com cenários de recompensa (combate, exploração, diálogo e quest).
5. Validar coerência final com os refinamentos quantitativos de Tipos de Armas, Itens de Mão e Raças/Classes.
