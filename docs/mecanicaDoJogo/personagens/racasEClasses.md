# Raças e Classes

## Objetivo Mecânico

Definir o recorte operacional de **Raças e Classes** como camadas de regra que modulam atributos, proficiências e afinidades de equipamento, sem depender de descrição narrativa.

Este documento consolida o legado em um modelo único para criação de personagem e integração com combate, equipamentos e progressão.

---

## Definição Operacional

- **Raça**: camada de origem mecânica com foco em baseline fisiológico (`atributosBase`, `resistenciaInata`, `limitesFisicos`).
- **Classe**: camada de função mecânica com foco em treinamento (`bonusDeFuncao`, `proficiências`, `assinaturaTatica`).
- O pacote inicial do personagem é a soma de:  
  `perfilInicial = baselineGlobal + modificadoresDeRaca + modificadoresDeClasse`.
- Raça e Classe nunca substituem as fórmulas canônicas de combate; apenas injetam parâmetros nelas.

---

## Estrutura Canônica

### Ficha mínima de Raça

Cada raça deve declarar:

1. `modificadoresDeAtributo` (vetor por atributo primário);
2. `resistenciaInata` (quando existir, com limite e gatilho);
3. `limiteFisico` (carga, deslocamento ou tolerância);
4. `afinidadesDeEquipamento` por `tag` e por `tipoDeArma`;
5. `penalidadesOuRestricoes` mecânicas explícitas.

### Ficha mínima de Classe

Cada classe deve declarar:

1. `bonusDeAtributoFuncional`;
2. `proficiênciasIniciais` (tipos de arma, subtipos de item de mão, defesa);
3. `assinaturaTatica` (pressão, controle, suporte, execução, utilidade);
4. `regrasDeEscalonamento` por faixa de progressão;
5. `incompatibilidades` e exceções (quando aplicável).

> Se Raça ou Classe não preencher os 5 campos da sua ficha mínima, o conteúdo não está pronto para requisito.

---

## Regras de Combinação Raça + Classe

1. **Regra de soma controlada:** bônus de atributo de Raça e Classe se acumulam, respeitando teto global por faixa (definido em Progressão do Jogo).
2. **Regra de proficiência dominante:** quando Raça e Classe concedem a mesma proficiência, não há duplicação; o ganho excedente vira `especialização` na mesma família.
3. **Regra de restrição primeiro:** restrições explícitas sempre são aplicadas antes de bônus condicionais.
4. **Regra anti-explosão de tabela:** afinidades devem ser expressas por `tag`/`tipo`, nunca por item individual.
5. **Regra de precedência temporal:** em criação, aplica primeiro Raça, depois Classe; em progressão, aplica primeiro marcos de Classe e depois ajustes situacionais.

---

## Matriz Operacional de Proficiências e Afinidades

### Grupos de Classe

| Grupo de Classe | Tipos de Arma com proficiência inicial | Itens de Mão preferenciais | Assinatura tática |
|---|---|---|---|
| Combate (`Guerreiro`, `Caçador`, `Duelista`) | `corpoACorpoLeve`, `corpoACorpoPesada`, `hasteAlcanceEstendido`, `distanciaProjetil` (varia por classe) | `arma`, `escudo` | pressão, duelismo, controle de linha |
| Magia (`Mago`, `Alquimista`, `ConjuradorElemental`) | `focoArcano` e arma secundária situacional | `focoMagico`, `reliquia`, `ferramenta` | controle, explosão situacional, suporte arcano |
| Suporte (`Bardo`, `Clerigo`, `Sabio`) | `corpoACorpoLeve` e `focoArcano` situacional | `instrumento`, `focoMagico`, `escudo` leve | suporte, proteção, utilidade |
| Furtividade (`Ladrao`, `Assassino`, `Ilusionista`) | `corpoACorpoLeve`, `distanciaProjetil` leve | `arma` leve, `ferramenta`, `consumivelRapido` | execução, infiltração, manipulação |

### Afinidade racial por tag de equipamento

| Raça | Tags com afinidade positiva (baseline) | Tags com restrição mecânica (baseline) |
|---|---|---|
| Humano | `adaptavel` (escolhe 1 tag para bônus situacional) | sem restrição fixa |
| Elfo | `leve`, `discreta`, `canalizadora` | `pesada` (penalidade de mobilidade) |
| Meio-elfo | `canalizadora`, `precisao` | sem restrição fixa |
| Anão | `pesada`, `impacto`, `escudo` | `discreta` (penalidade de eficiência) |
| Meio-orc | `impacto`, `pesada` | `canalizadora` (custo aumentado) |
| Tiefling | `canalizadora`, `arcana` | `escudo` pesado (penalidade situacional) |
| Draconato | `impacto` ou `canalizadora` (escolha de linhagem) | `discreta` |
| Halfling | `leve`, `discreta`, `rapido` | `pesada`, `alcance` de haste pesada |

> A tabela define baseline da fase. Valores numéricos de bônus/penalidade ficam para o refinamento quantitativo.

---

## Integração com Outros Sistemas

### Tipos de Armas

- Proficiências de Classe devem apontar para `tipo de arma`, não para itens individuais.
- Afinidades raciais alteram desempenho por `tag`, preservando taxonomia canônica.

### Itens de Mão

- Requisitos de uso (`classe`, `raça`, `proficiência`) são avaliados por slot ativo e por ação.
- Regras de dupla empunhadura e foco + arma dependem de destravos de Classe.

### Fórmulas de Combate

- Bônus de Raça/Classe entram como modificadores de entrada em acerto, dano, bloqueio ou resistência.
- Nenhuma exceção local pode quebrar ordem de resolução definida em `formulasDeCombate`.

### Progressão do Jogo

- Progressão define quando proficiências viram especializações e quando penalidades são mitigadas.
- Faixas de poder também definem teto para empilhamento de bônus de origem e função.

---

## Limites e Casos de Borda

- **Sem proficiência de classe no tipo equipado:** aplica penalidade de uso e bloqueia passivas avançadas do item.
- **Conflito Raça vs Classe:** quando uma concede bônus e outra impõe restrição na mesma tag, prevalece a restrição e o bônus vira condicional.
- **Mudança de Classe (respec narrativo):** só é válida com protocolo de migração que preserve teto de poder por faixa.
- **Raça híbrida futura:** deve herdar no máximo 1 pacote majoritário e 1 pacote minoritário para evitar dupla vantagem estrutural.
- **Classe híbrida futura:** deve declarar custo explícito de versatilidade (ex.: atraso de especialização).

---

## Pronto para Requisito

Para este tópico ser considerado pronto para requisito funcional, ainda falta:

1. Converter afinidades por tag em matriz numérica oficial (`bônus`, `penalidade`, `custo extra`) por faixa.
2. Formalizar lista final de proficiências iniciais por classe individual (não apenas por grupo).
3. Definir protocolo fechado para desbloqueio de especialização e maestria por tipo de arma.
4. Integrar este documento com a página de **Progressão do Jogo** (gatilhos exatos por nível/faixa).
5. Validar coerência final com catálogo mínimo de builds de referência (combate, magia, suporte e furtividade).
