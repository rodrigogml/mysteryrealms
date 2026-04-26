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
|---|---:|---:|---:|
| F1 | +4 | +2 | 50% |
| F2 | +6 | +4 | 60% |
| F3 | +8 | +6 | 70% |
| F4 | +10 | +8 | 80% |

\* Mitigação efetiva = bloqueio + resistência aplicada no resultado final.

Normalização de unidade para os tetos:
- `bônus de origem`: soma de modificadores permanentes de raça+classe no eixo ativo (acerto, dano, bloqueio **ou** resistência).
- `bônus de equipamento`: soma de modificadores permanentes da configuração equipada no mesmo eixo.
- efeitos temporários continuam permitidos, mas qualquer excedente acima do teto vira bônus condicional com gatilho explícito.

Regras:
1. Quando o teto de uma fonte é atingido, novos bônus da mesma fonte viram bônus condicionais/situacionais.
2. Teto de mitigação segue o limite sistêmico já definido em metas de balanceamento.
3. Em F1/F2, o personagem não pode converter excedente em mitigação; em F3/F4, pode converter no máximo `+1` por turno em efeito condicional.

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

## Tabela de Marcos por Classe Individual

A tabela abaixo fixa **níveis exatos de desbloqueio** por classe para reduzir ambiguidade na transição entre F1–F4.

| Classe | Especialização 1 (F2) | Especialização 2 situacional (F3) | Maestria principal (F4) | Trava de versatilidade |
|---|---:|---:|---:|---|
| Guerreiro | Nível 10 | Nível 22 | Nível 40 | se ativar eixo secundário, próximo marco atrasa +1 nível |
| Caçador | Nível 10 | Nível 21 | Nível 40 | mesmo protocolo de atraso +1 |
| Duelista | Nível 10 | Nível 20 | Nível 40 | se combinar foco recorrente, atraso +1 no próximo marco |
| Mago | Nível 10 | Nível 24 | Nível 41 | se usar eixo físico recorrente, atraso +1 |
| Alquimista | Nível 10 | Nível 23 | Nível 41 | sem atraso extra para `ferramenta+foco`; demais eixos +1 |
| Conjurador Elemental | Nível 10 | Nível 24 | Nível 41 | eixo de distância recorrente gera atraso +1 |
| Bardo | Nível 10 | Nível 23 | Nível 41 | atraso +1 quando ativar 2º eixo ofensivo recorrente |
| Clerigo | Nível 10 | Nível 22 | Nível 40 | atraso +1 fora do eixo `suporte+defesa` |
| Sabio | Nível 11 | Nível 24 | Nível 41 | atraso +1 em qualquer eixo ofensivo recorrente |
| Ladrao | Nível 10 | Nível 20 | Nível 40 | atraso +1 ao entrar em eixo arcano recorrente |
| Assassino | Nível 10 | Nível 20 | Nível 40 | atraso +1 fora de `execução+furtividade` |
| Ilusionista | Nível 10 | Nível 23 | Nível 41 | atraso +1 ao ativar eixo pesado recorrente |

Regras de leitura:
1. O nível do marco já considera a faixa mínima (não existem marcos antecipados antes de F2/F3/F4).
2. O atraso de versatilidade desloca **apenas o próximo marco pendente**; não reescreve marcos já adquiridos.
3. Se duas regras de atraso incidirem juntas, aplicar somente a de maior impacto (máximo `+2 níveis`).

### Vínculo dos marcos com a Curva de XP

Para manter rastreabilidade entre nível e progressão real, cada marco deve apontar para o XP acumulado correspondente.

| Marco | Nível de referência | Função no sistema |
|---|---:|---|
| Entrada de Especialização 1 | 10 | libera mitigação parcial de penalidades e 1 eixo especializado |
| Entrada de Especialização 2 | 20–24 (classe-dependente) | libera sinergia composta e 2º eixo situacional |
| Entrada de Maestria principal | 40–41 (classe-dependente) | libera otimização de custo mantendo limite estrutural de maestria |

Protocolo operacional:
1. O requisito funcional deve armazenar `nivelMarco` e `xpAcumuladoMarco` no mesmo artefato de classe.
2. Quando houver recalibragem de curva (`A`, `B`, `C`), os níveis de marco ficam estáveis e apenas os pontos de XP são atualizados.
3. Em classe híbrida com atraso de versatilidade, atualizar primeiro `nivelMarco` e depois recalcular `xpAcumuladoMarco`.


### Mapeamento baseline de XP por marco

Valores de referência calculados com os parâmetros atuais da curva (`A=50`, `B=10`, `C=10`) e arredondados para inteiro.

| Marco | Nível | XP acumulado de referência |
|---|---:|---:|
| Especialização 1 | 10 | 13.662.257 |
| Especialização 2 (faixa baixa) | 20 | 79.351.706 |
| Especialização 2 (faixa alta) | 24 | 131.776.685 |
| Maestria principal (faixa baixa) | 40 | 580.787.524 |
| Maestria principal (faixa alta) | 41 | 624.935.673 |

Regra operacional de uso:
1. Os valores acima são baseline documental para rastreabilidade; a planilha oficial deve guardar o valor não arredondado.
2. Classes com marco em 21–23 usam interpolação direta pelo nível da tabela de marcos por classe.
3. Se o atraso por versatilidade mover o nível do marco, recalcular o XP no mesmo ciclo de atualização de classe.

### Critérios de exceção para conteúdo sazonal

Exceções sazonais não podem alterar a estrutura canônica de marcos (`Especialização 1`, `Especialização 2`, `Maestria`).

| Tipo de exceção sazonal | Permitido | Restrição obrigatória |
|---|---|---|
| Bônus temporário de XP | Sim | não pode antecipar desbloqueio de marco estrutural |
| Janela extra de especialização situacional | Sim | duração máxima de 1 arco sazonal e sem empilhar com maestria principal |
| Redução temporária de custo de versatilidade | Condicional | limite de 1 eixo e reversão automática ao final do evento |
| Marco sazonal exclusivo | Não | proibido criar novo estado de domínio fora de F1–F4 |

Regras de governança:
1. Toda exceção sazonal deve declarar data de expiração e regra de reversão explícita.
2. Nenhuma exceção pode modificar permanentemente `nivelMarco` base da classe.
3. Em conflito entre regra sazonal e teto por faixa, prevalece sempre o teto por faixa.

---

### Custo formal de versatilidade e limite de maestrias

| Regra | Valor canônico (baseline) | Observação operacional |
|---|---|---|
| Custo de versatilidade por eixo secundário ativo | atraso de `1 marco de classe` | aplica ao próximo desbloqueio de especialização no eixo principal |
| Custo adicional para 2º eixo secundário ativo | atraso adicional de `1 marco` (total 2) | não cumulativo além de 2 eixos secundários |
| Limite de maestrias simultâneas em F4 | `1` maestria principal + `1` maestria situacional | a maestria situacional não pode estar ativa no mesmo turno da principal |
| Reconfiguração de maestria | cooldown narrativo de `1 ciclo de descanso completo` | impede troca oportunista por encontro |

Regras de aplicação:
1. O custo de versatilidade é aplicado no momento em que a build passa a operar com eixo secundário de forma recorrente (não em uso pontual).
2. Se a classe já possui custo de versatilidade próprio em `Raças e Classes`, prevalece o maior custo entre os dois documentos.
3. O limite de maestrias simultâneas é estrutural e não pode ser ampliado por item consumível/evento temporário nesta fase.
4. Em respec, toda maestria situacional deve ser removida antes de redistribuir especializações.

---

### Fechamento da planilha oficial de XP por cenário

Para concluir a consolidação, a planilha oficial deve registrar recompensa por tipo de atividade com rastreabilidade por faixa.

#### Estrutura mínima da planilha

| Coluna | Descrição | Regra |
|---|---|---|
| `cenarioId` | identificador do evento de ganho de XP | obrigatório e único por evento |
| `tipoDeCenario` | `combate`, `exploracao`, `dialogo`, `quest` | usar taxonomia fixa |
| `faixaAlvo` | F1, F2, F3, F4 | deve refletir nível médio do desafio |
| `xpBaseEvento` | XP bruto do evento | calculado por referência de faixa |
| `multiplicadorDeRisco` | ajuste por risco real | intervalo recomendado `0.8` a `1.4` |
| `xpFinalEvento` | XP concedido após ajustes | `xpBaseEvento * multiplicadorDeRisco` |
| `deltaParaProximoMarco` | distância ao próximo marco de classe | obrigatório para auditoria de cadência |

#### Baseline de recompensa por cenário e faixa

| Tipo de cenário | F1 | F2 | F3 | F4 |
|---|---:|---:|---:|---:|
| Combate padrão | 1.5% do `xpParaProximoNivel` | 1.2% | 1.0% | 0.8% |
| Exploração relevante | 1.2% | 1.0% | 0.8% | 0.7% |
| Diálogo com impacto sistêmico | 1.0% | 0.9% | 0.7% | 0.6% |
| Quest principal | 8.0% | 7.0% | 6.0% | 5.0% |
| Quest secundária | 3.0% | 2.5% | 2.0% | 1.5% |

Regras de fechamento:
1. O total médio de XP por sessão deve manter tempo de progressão coerente com os marcos de classe já definidos.
2. O mesmo conteúdo não pode premiar simultaneamente duas fontes de XP sem justificativa (ex.: combate + quest).
3. Qualquer exceção sazonal deve ser registrada em aba separada e não pode alterar a aba canônica.
4. Alterações de percentual exigem registro de versão e motivo de balanceamento.

### Protocolo de validação de cadência (simulação)

Antes de congelar requisito funcional, executar simulação de cadência para verificar se o ganho de XP por cenário sustenta os marcos por classe.

#### Cenários mínimos de simulação

| Cenário | Composição de sessão | Objetivo |
|---|---|---|
| Combate-dominante | 70% combate, 20% quest, 10% exploração/diálogo | validar ritmo de classes de pressão/execução |
| Híbrido equilibrado | 35% combate, 30% quest, 20% exploração, 15% diálogo | baseline geral de campanha |
| Narrativo-investigativo | 15% combate, 35% diálogo, 30% exploração, 20% quest | validar classes de suporte/utilidade |

#### Métricas de validação

| Métrica | Definição | Faixa de alerta |
|---|---|---|
| `sessoesParaF2` | sessões médias para atingir nível 10 | alerta se < 4 ou > 12 |
| `sessoesF2ParaF3` | sessões médias do nível 10 ao 20 | alerta se < 8 ou > 24 |
| `sessoesF3ParaF4` | sessões médias do nível 20 ao 40 | alerta se < 16 ou > 48 |
| `desvioEntreClasses` | diferença percentual de ritmo entre classes no mesmo cenário | alerta se > 20% |

#### Critério de aprovação da cadência

1. Pelo menos 2 dos 3 cenários devem ficar dentro das faixas de alerta para todas as métricas principais.
2. Nenhuma classe pode ultrapassar `desvioEntreClasses` acima de 20% em dois cenários simultaneamente.
3. Se houver reprovação, ajustar primeiro percentuais da planilha de XP; só depois revisar marcos de classe.
4. Toda rodada de simulação deve registrar versão da planilha, parâmetros de curva (`A`, `B`, `C`) e timestamp.

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

1. Validar coerência final com os refinamentos quantitativos de Tipos de Armas, Itens de Mão e Raças/Classes.
2. Executar rodada inicial da validação de cadência com os 3 cenários definidos e publicar resultados.
3. Executar simulação de sensibilidade para os novos tetos numéricos, marcos por classe e custos de versatilidade.
