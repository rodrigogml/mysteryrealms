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

> A tabela define baseline da fase para tags. A calibração numérica por classe individual está definida nas matrizes abaixo.

### Matriz por Classe Individual (baseline quantitativo)

Escala desta fase (alinhada a `tiposDeArmas` e `itensDeMao`):
- afinidade por tipo: `+2` forte, `+1` funcional, `0` neutro, `-1` ineficiente, `-2` restrito;
- custo de versatilidade: atraso de marco (`+0` sem atraso, `+1` atraso de 1 marco da classe);
- ajuste de penalidade de item: valor aditivo aplicado sobre penalidades base de `itensDeMao`.

| Classe | C.C. Leve | C.C. Pesada | Haste | Distância | Foco Arcano | Itens de Mão preferenciais | custo de versatilidade | ajuste de penalidade de item |
|---|---:|---:|---:|---:|---:|---|---:|---:|
| Guerreiro | +1 | +2 | +2 | 0 | -2 | `arma+escudo`, `arma(2M)` | +0 | -1 em `arma+escudo` |
| Caçador | +1 | 0 | +1 | +2 | -1 | `arma(distância)+ferramenta` | +0 | -1 em troca para `distância` |
| Duelista | +2 | -1 | 0 | +1 | -1 | `arma(1M)+arma(1M)` | +1 (se usar foco) | -1 na penalidade de dupla empunhadura |
| Mago | 0 | -2 | -1 | 0 | +2 | `foco+arma(1M)` situacional | +0 | -1 em `foco+arma` (somente F3+) |
| Alquimista | +1 | -1 | 0 | +1 | +1 | `ferramenta+foco` | +0 | -1 em uso de `ferramenta` |
| Conjurador Elemental | 0 | -2 | 0 | 0 | +2 | `foco(2M)` ou `foco+reliquia` | +1 (se usar distância) | -1 em custo de ativação de foco |
| Bardo | +1 | -1 | 0 | 0 | +2 | `instrumento+foco` | +0 | -1 em ações de suporte com instrumento |
| Clerigo | +1 | 0 | +1 | -1 | +2 | `arma(1M)+escudo` ou `foco+escudo` | +0 | -1 em bloqueio com escudo |
| Sabio | 0 | -1 | 0 | 0 | +2 | `foco+ferramenta` | +0 | -1 em testes utilitários com ferramenta |
| Ladrao | +2 | -2 | -1 | +1 | 0 | `arma(1M)+ferramenta` | +0 | -1 em penalidade de item sem proficiência leve |
| Assassino | +2 | -2 | 0 | +1 | 0 | `arma(1M)+arma(1M)` | +0 | -1 em penalidade de execução discreta |
| Ilusionista | +1 | -2 | -1 | 0 | +2 | `foco+arma(1M)` leve | +1 (se usar pesada) | -1 em penalidade de foco sob furtividade |

Regras de uso da matriz:
1. A classe define o valor base por tipo na criação; raça apenas desloca esse valor dentro do intervalo `[-2, +2]`.
2. O campo `ajuste de penalidade de item` só reduz penalidades já existentes; nunca cria bônus líquido acima do baseline de `itensDeMao`.
3. Quando a classe assumir configuração fora de assinatura (coluna `custo de versatilidade`), aplicar atraso de 1 marco da classe no próximo desbloqueio de especialização.
4. Nenhuma classe pode iniciar F1 com mais de dois tipos em `+2`.

### Matriz de deslocamento racial sobre a classe

| Raça | deslocamento positivo | deslocamento negativo | limite por criação |
|---|---|---|---|
| Humano | +1 em 1 tipo à escolha | nenhum obrigatório | não pode elevar tipo já em `+2` |
| Elfo | +1 em `C.C. Leve` ou `Distância` | -1 em `C.C. Pesada` | aplica 1 positivo + 1 negativo |
| Meio-elfo | +1 em `Foco Arcano` ou `Distância` | nenhum obrigatório | apenas 1 deslocamento |
| Anão | +1 em `C.C. Pesada` ou `Haste` | -1 em `Distância` | aplica 1 positivo + 1 negativo |
| Meio-orc | +1 em `C.C. Pesada` | -1 em `Foco Arcano` | fixo |
| Tiefling | +1 em `Foco Arcano` | -1 em `C.C. Pesada` | fixo |
| Draconato | +1 em `C.C. Pesada` **ou** `Foco Arcano` | -1 em `C.C. Leve` | escolhe 1 linhagem |
| Halfling | +1 em `C.C. Leve` ou `Distância` | -1 em `C.C. Pesada` e `Haste` | aplica 1 positivo + 1 negativo |

Regras de deslocamento racial:
1. O deslocamento racial acontece após aplicar a classe e antes de validar teto da faixa em `progressaoDoJogo`.
2. Se deslocamento positivo e negativo incidirem no mesmo tipo por combinação rara, prevalece o valor mais restritivo (mínimo).
3. O deslocamento não altera proficiência; altera apenas eficiência (`afinidade`) e custo de adaptação.

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
2. Validar por simulação a lista final de proficiências iniciais por classe individual (já descrita em baseline).
3. Definir protocolo fechado para desbloqueio de especialização e maestria por tipo de arma.
4. Integrar este documento com a página de **Progressão do Jogo** (gatilhos exatos por nível/faixa).
5. Validar coerência final com catálogo mínimo de builds de referência (combate, magia, suporte e furtividade).
