# Definição de Personagem

Este documento consolida, em formato Markdown, as definições legadas sobre personagem (jogador), atributos, ficha técnica e componentes diretamente relacionados.

Termos de exibição e chaves técnicas seguem o padrão canônico definido em `docs/mecanica/glossario.md`.

## Escopo da definição do personagem

Com base na documentação legada:
- A construção inicial do personagem é definida pela combinação de **Raça + Classe**.
- Não há distribuição livre de pontos iniciais de atributos.
- A evolução posterior ocorre pelo sistema de progressão.

## Identidade da ficha do jogador

Campos de identidade mapeados:
- Nome
- Sobrenome
- Gênero (Masculino, Feminino, Outros)
- Raça
- Classe
- Idade Inicial

## Atributos principais

Atributos-base do personagem:
- Força (`forca`)
- Destreza (`destreza`)
- Constituição (`constituicao`)
- Intelecto (`intelecto`)
- Percepção (`percepcao`)
- Carisma (`carisma`)
- Vontade (`vontade`)

### Papel dos atributos

- **Força:** potência física, carga, dano corporal.
- **Destreza:** agilidade, reflexos, precisão motora.
- **Constituição:** robustez, vida, resistência física.
- **Intelecto:** raciocínio, memória, conhecimento técnico/arcano.
- **Percepção:** observação, detecção de ameaças e detalhes.
- **Carisma:** presença social, persuasão, influência interpessoal.
- **Vontade:** foco mental, disciplina, resistência mental.

## Habilidades associadas

Habilidades listadas na base legada (padronizadas):
- Persuasão (`persuasao`) [Carisma / `carisma`]
- Intimidação (`intimidacao`) [Carisma / `carisma`]
- Enganação (`enganacao`) [Carisma / `carisma`]
- Conhecimento (Arcano, História, Relíquias) (`conhecimento_arcano`, `conhecimento_historia`, `conhecimento_reliquias`) [Intelecto / `intelecto`]
- Herbalismo / Alquimia (`herbalismo`, `alquimia`) [Intelecto / `intelecto`]
- Furtividade (`furtividade`) [Destreza / `destreza`]
- Sobrevivência (`sobrevivencia`) [Percepção / `percepcao`]
- Manuseio de Armas (`manuseio_armas`) [Força ou Destreza / `forca` ou `destreza`]
- Uso de Magia (`uso_magia`) [Vontade ou Intelecto / `vontade` ou `intelecto`]

Regra geral legada: quando houver habilidade específica, usar habilidade; se não houver, usar teste direto de atributo principal.

## Estado do personagem

Atributos de estado referenciados:
- XP Acumulado
- Pontos de Vida
- Fadiga
- Fome
- Sede
- Moral (escala 0 a 100)

Regras canônicas obrigatórias do estado do personagem:
- `docs/mecanica/fadigaFomeSede.md` (faixas, gatilhos, empilhamento e precedência)
- `docs/mecanica/recuperacao.md` (recuperação por descanso, sono e itens)
- `docs/mecanica/morteDesmaio.md` (estado crítico, colapso e despertar)
- `docs/mecanica/moral.md` (faixas emocionais e efeitos)

## Inventário e recursos

Campos previstos:
- Moedas
- Itens Equipados
- Itens na Mochila
- Montaria (com impacto em transporte e carga)

## Ficha técnica (atributos derivados)

### Atributos de carga

- **Capacidade de Carga Máxima (Jogador):**
  - Fórmula: `Força × 10` (kg)
- **Capacidade de Carga Máxima da Montaria:**
  - Valor vindo da ficha da montaria
- **Capacidade de Carga Atual:**
  - Montado: `(Carga Máxima da Montaria - Peso do Jogador) + Modificadores da Montaria`
  - Não montado: `(Carga Máxima Jogador + Carga Máxima Montaria) + Modificadores`
- **Carga Crítica Atual:**
  - Fórmula: `Capacidade de Carga Atual × 1,5`
- **Carga Atual:**
  - Soma dos pesos de equipados + mochila + moedas

### Peso do personagem

- **Peso (kg):**
  - Fórmula: `PesoBase(raça/gênero) × (1 + ((CON - 3) × 0,05))`

### Vida, fadiga e combate

- **Pontos de Vida Máximo:** `CON × 10`
- **Fadiga Máxima / Fadiga Mínima / Exaustão:** definidos no sistema de Fadiga, Fome e Sede
- **Precisão:** `1d20 + Destreza + Bônus de Precisão da Arma + Modificadores`
- **Dano Base:** `Dado da Arma + Modificador de Atributo + Bônus de Raça/Classe`

#### Dano (final)

1. **Fórmula matemática final**

   `DanoFinal = max(0, arred( (DadoArma + ModAtributo + BonusRacaClasse + BonusPlano) × (1 + SomaPctPositivo - SomaPctNegativo) ) + SomaFlatPositivo - SomaFlatNegativo )`

   **Ordem de aplicação de modificadores (obrigatória):**
   1) rolar/computar base (`DadoArma + ModAtributo + BonusRacaClasse + BonusPlano`),  
   2) aplicar modificadores percentuais,  
   3) aplicar modificadores flat (fixos),  
   4) truncar no mínimo em `0`.

2. **Variáveis obrigatórias e opcionais**
   - Obrigatórias: `DadoArma`, `ModAtributo`.
   - Opcionais: `BonusRacaClasse`, `BonusPlano`, `SomaPctPositivo`, `SomaPctNegativo`, `SomaFlatPositivo`, `SomaFlatNegativo`.

3. **Regra de arredondamento**
   - `arred(x)` = **piso** (`floor`), sempre arredondando para baixo após etapa percentual.

4. **Exemplo numérico completo**
   - `DadoArma=14`, `ModAtributo=6`, `BonusRacaClasse=2`, `BonusPlano=0` → base `22`.
   - `SomaPctPositivo=0,25`, `SomaPctNegativo=0,10` → multiplicador `1,15`.
   - Pós-percentual: `22 × 1,15 = 25,3` → piso = `25`.
   - `SomaFlatPositivo=4`, `SomaFlatNegativo=3` → `25 + 4 - 3 = 26`.
   - **DanoFinal = 26**.

5. **Regra de prioridade em conflito**
   - Prioridade de origem (maior para menor): **regra de estado crítico do combate > habilidade ativa > efeito de equipamento > traço de classe > traço racial > efeito temporário genérico**.
   - Modificadores da mesma origem e mesmo tipo **acumulam**.
   - Quando duas regras do mesmo nível se excluem, vence a de **maior magnitude absoluta**; empate: vence a de **menor duração restante**; novo empate: aplica-se a mais recente.

#### Defesa (final)

1. **Fórmula matemática final**

   `DefesaFinal = max(0, arred( (BaseEsquiva + BonusArmadura + BonusEscudo + BonusPlano) × (1 + SomaPctPositivo - SomaPctNegativo) ) + SomaFlatPositivo - SomaFlatNegativo )`

   **Ordem de aplicação de modificadores (obrigatória):**
   1) somar base (`BaseEsquiva + BonusArmadura + BonusEscudo + BonusPlano`),  
   2) aplicar percentuais,  
   3) aplicar flats,  
   4) truncar no mínimo em `0`.

2. **Variáveis obrigatórias e opcionais**
   - Obrigatórias: `BaseEsquiva` (normalmente derivada de Destreza).
   - Opcionais: `BonusArmadura`, `BonusEscudo`, `BonusPlano`, `SomaPctPositivo`, `SomaPctNegativo`, `SomaFlatPositivo`, `SomaFlatNegativo`.

3. **Regra de arredondamento**
   - `arred(x)` = **piso** (`floor`) após aplicação percentual.

4. **Exemplo numérico completo**
   - `BaseEsquiva=18`, `BonusArmadura=7`, `BonusEscudo=3`, `BonusPlano=0` → base `28`.
   - `SomaPctPositivo=0,20`, `SomaPctNegativo=0,05` → multiplicador `1,15`.
   - Pós-percentual: `28 × 1,15 = 32,2` → piso = `32`.
   - `SomaFlatPositivo=2`, `SomaFlatNegativo=4` → `32 + 2 - 4 = 30`.
   - **DefesaFinal = 30**.

5. **Regra de prioridade em conflito**
   - Mesma prioridade global de combate: **estado crítico > habilidade ativa > equipamento > classe > raça > temporário genérico**.
   - Apenas o **maior bônus de escudo** é considerado quando houver múltiplos escudos.
   - Penalidades de armadura e terreno **sempre acumulam** entre si.

#### Bloqueio (final)

1. **Fórmula matemática final**

   `BloqueioFinal = max(0, arred( (BaseBloqueio + BonusEquipBloqueio + BonusPlano) × (1 + SomaPctPositivo - SomaPctNegativo) ) + SomaFlatPositivo - SomaFlatNegativo )`

   **Ordem de aplicação de modificadores (obrigatória):**
   1) somar base (`BaseBloqueio + BonusEquipBloqueio + BonusPlano`),  
   2) aplicar percentuais,  
   3) aplicar flats,  
   4) truncar no mínimo em `0`.

2. **Variáveis obrigatórias e opcionais**
   - Obrigatórias: `BaseBloqueio`.
   - Opcionais: `BonusEquipBloqueio`, `BonusPlano`, `SomaPctPositivo`, `SomaPctNegativo`, `SomaFlatPositivo`, `SomaFlatNegativo`.

3. **Regra de arredondamento**
   - `arred(x)` = **piso** (`floor`) após aplicação percentual.

4. **Exemplo numérico completo**
   - `BaseBloqueio=12`, `BonusEquipBloqueio=6`, `BonusPlano=1` → base `19`.
   - `SomaPctPositivo=0,30`, `SomaPctNegativo=0,10` → multiplicador `1,20`.
   - Pós-percentual: `19 × 1,20 = 22,8` → piso = `22`.
   - `SomaFlatPositivo=3`, `SomaFlatNegativo=2` → `22 + 3 - 2 = 23`.
   - **BloqueioFinal = 23**.

5. **Regra de prioridade em conflito**
   - Mesma prioridade global de combate: **estado crítico > habilidade ativa > equipamento > classe > raça > temporário genérico**.
   - Efeitos com texto “substitui valor de bloqueio” têm precedência sobre efeitos que “adicionam”.
   - Se duas substituições competirem, aplica-se a de maior valor final calculado; empate: a de menor duração restante; novo empate: a mais recente.

## Raças jogáveis e características-base

Raças listadas:
- Humano
- Elfo
- Meio-elfo
- Anão
- Meio-orc
- Tiefling
- Draconato
- Halfling

Cada raça define:
- distribuição-base dos 7 atributos principais;
- bônus em habilidades específicas;
- resistências e traços raciais;
- tabela de peso por gênero.

## Classes e papel mecânico

Classes listadas:
- Combate: Guerreiro, Caçador, Duelista
- Magia: Mago, Alquimista, Conjurador Elemental
- Sociais/Suporte: Bardo, Clérigo, Sábio
- Furtivas/Manipulação: Ladrão, Assassino, Ilusionista

Cada classe define:
- bônus iniciais de atributos;
- bônus em habilidades específicas;
- estilo de jogo e papel funcional.

## Relacionamento e reputação

Sistema social da ficha contempla:
- Relacionamento com NPCs (escala -100 a 100)
- Faixas: Inimigo Mortal, Hostil, Neutra, Favorável, Aliado
- Reputação por localidade e por grupos/facções

## Referências legadas consultadas

- `docs/legado/fichaDoJogador.wiki`
- `docs/legado/racas.wiki`
- `docs/legado/classes.wiki`
- `docs/legado/testesDeJogo.wiki`
- `docs/legado/moral.wiki`
- `docs/legado/modificadores.wiki`
- `docs/legado/recuperacao.wiki`
- `docs/legado/morteEDesmaios.wiki`
