Os **Estilos de Fala** definem o tom, abordagem e linguagem utilizados pelo jogador ao interagir com NPCs, assim como os estilos preferenciais e tolerados por cada NPC. Eles servem como base para o sistema de **Tolerância de Personalidade**, afetando diretamente as chances de sucesso de uma fala, bem como sua influência no relacionamento com o NPC.

Cada estilo possui:
- Uma **descrição clara**;
- Uma **categoria de impacto** (positivo, neutro, negativo);
- Uma lista de **estilos opostos**, que são usados para calcular dissonância ou rejeição.

# Tabela de Estilos

{| class="wikitable"
! **Estilo** || **Descrição** || **Categoria** || **Estilos Opostos**
|-
| **Educado** || Usa gentileza, boas maneiras e cortesia. || Positivo || grosseiro
|-
| **Grosseiro** || Fala áspera, ofensiva ou rude. || Negativo || educado
|-
| **Direto** || Vai ao ponto, sem rodeios ou floreios. || Neutro || enigmático, formal
|-
| **Enigmático** || Usa ambiguidade, simbolismo ou misticismo. || Neutro || direto
|-
| **Formal** || Fala estruturada, respeitosa, com compostura social. || Positivo || informal, grosseiro
|-
| **Informal** || Descontraída, casual, uso de gírias. || Neutro || formal
|-
| **Irônico** || Zombaria sutil, sarcasmo ou humor ácido. || Negativo || literal, respeitoso
|-
| **Literal** || Objetiva, direta, sem duplo sentido. || Neutro || irônico, enigmático
|-
| **Empático** || Demonstra empatia, escuta e consideração. || Positivo || intimidativo
|-
| **Intimidativo** || Impõe pressão, ameaça ou dominação. || Negativo || empático
|-
| **Afetuoso** || Usa calor emocional, carinho, afabilidade. || Positivo || frio
|-
| **Frio** || Distante, impessoal, racional. || Neutro || afetuoso
|-
| **Lógico** || Argumentativo, analítico, racional. || Neutro || emocional
|-
| **Emocional** || Fala baseada em sentimentos, impulsos ou paixões. || Neutro || lógico
|-
| **Respeitoso** || Demonstra reverência, deferência ou honra. || Positivo || desrespeitoso, irônico
|-
| **Desrespeitoso** || Rompe etiqueta, insulta ou debocha abertamente. || Negativo || respeitoso
|}


## Categoria dos Estilos de Fala

Cada estilo de fala listado na tabela acima possui uma **categoria** descritiva associada: **positivo**, **neutro** ou **negativo**. Esta classificação não tem impacto direto no funcionamento mecânico do sistema, mas serve como orientação de design e organização.


A categorização dos estilos tem duas funções principais:
- **Orientação narrativa**: Auxilia criadores a compreender o tom social e emocional associado a cada estilo. Por exemplo:
:* Estilos classificados como **positivos** são, em geral, socialmente bem vistos e associados a empatia ou respeito.
:* Estilos **negativos** representam posturas mais agressivas, sarcásticas ou ofensivas.
:* Estilos **neutros** são contextuais e dependem mais do NPC do que de uma norma social.
- **Organização visual**: Facilita a leitura e utilização da tabela de estilos, especialmente em contextos de edição de diálogos ou criação de fichas de NPCs.


> **Nota:** A categoria **não altera rolagens, testes de tolerância ou resultados de afinidade**.

# Uso no Sistema de Relacionamento
- Quando uma fala do jogador utiliza um **estilo** que está entre os preferidos ou tolerados do NPC, pode gerar **ganho de relacionamento**.
- Quando utiliza um **estilo oposto**, há chance de **rejeição**, levando à perda de relacionamento.

# Sistema de Valoração
Além dos **Estilos de Fala**, os NPCs também possuem **valores pessoais**, que representam ideias, princípios e atitudes que eles admiram ou defendem. Esses valores formam o **Sistema de Valoração**.

Falas que expressam ou representam esses valores podem gerar afinidade com o NPC, **mesmo que o estilo da fala não seja o preferido**. Isso permite ao jogador conquistar NPCs por meio de **conteúdo moral ou filosófico**, não apenas pelo tom.

## Tabela de Valoração
- **Honra** — Respeita a verdade, promessas e conduta justa.
- **Liberdade** — Valoriza a escolha individual, odeia opressão.
- **Conhecimento** — Preza por sabedoria, descoberta e estudo.
- **Lealdade** — Admira fidelidade a amigos, clãs ou causas.
- **Espiritualidade** — Acredita no valor da fé e do sagrado.
- **Força** — Respeita coragem, poder e determinação.
- **Compaixão** — Empatia e cuidado com o outro.
- **Pragmatismo** — Valoriza resultados, não aparências.
- **Tradição** — Defende costumes, hierarquias e cultura.
- **Desobediência** — Aprecia rebeldia contra autoridades.
- **Autossuficiência** — Valoriza independência e autonomia.

# Diferença entre Estilo de Fala e Valoração
- O **Estilo de Fala** diz respeito **à forma** como algo é dito — se o tom é educado, rude, formal, etc.
- A **Valoração** considera **o conteúdo da mensagem**, o princípio que ela representa (honra, compaixão, força, etc.).

Um NPC pode detestar grosseria (estilo), mas ainda respeitar a mensagem se ela defender algo em que ele acredita (valor), como a liberdade ou a verdade.

Essa diferença permite que NPCs tenham reações mais humanas, como admirar a intenção apesar do tom, ou simpatizar com a forma, mesmo que discorde do conteúdo.

# Reações Espontâneas
Alguns NPCs podem gerar falas automáticas baseadas na recepção do estilo ou valor:
- "Gosto de quem vai direto ao ponto."
- "Formal demais pra mim... parece um nobre sem espada."
- "Você fala como um bruto, mas defende a verdade. Respeito isso."

# Sistema de Reação a Falas

Este sistema define como os NPCs reagem às falas do jogador com base em dois componentes opcionais atribuídos a cada fala:

- **Estilo de Fala** – Representa o **tom ou forma** com que a fala é expressa. Ex: direto, grosseiro, educado.
- **Valoração** – Representa o **conteúdo ou valor moral** da fala. Ex: honra, compaixão, liberdade.

Cada fala pode conter no máximo **1 estilo** e **1 valor**. Ambos são opcionais. Se nenhum for indicado, a fala será tratada como **neutra**, sem bônus ou penalidade.

## Rolagem de Tolerância

Se a fala contiver um **estilo de fala**, realiza-se uma rolagem de **4d25** (quatro dados de 25 lados) para verificar se o NPC tolera esse estilo.

- O valor da rolagem é comparado com a **Tolerância do NPC** ao estilo usado (valor entre 0 e 100).
- O NPC passa no teste se o valor de 4d25 for **menor ou igual** à sua tolerância.

{| class="wikitable"
! Resultado da Rolagem || Efeito || Impacto no Relacionamento
|-
| ≤ Tolerância || Fala aceita || Nenhuma penalidade
|-
| > Tolerância || Fala mal recebida || −1 ponto de relacionamento
|}

Caso a fala não tenha estilo, **nenhuma rolagem é feita** — o impacto da fala será avaliado apenas pela valoração ou por outros sistemas.

## Avaliação de Valoração

Se a fala expressar um **valor** listado como apreciado na ficha do NPC, o jogador ganha **+1 ponto de relacionamento** automaticamente, **independentemente da tolerância ao estilo** ou do sucesso/falha da rolagem.

Se o valor não for relevante ao NPC, nenhuma modificação ocorre.
Se for contrário aos princípios do NPC, pode influenciar a narrativa, mas **não gera penalidade mecânica automática**.

## Reações Narrativas

A depender do sucesso ou falha na tolerância e da presença de valoração, o NPC pode emitir uma **frase de reação espontânea**, escolhida da sua ficha, como forma de reforçar personalidade e imersão narrativa.

## Compatibilidade com Testes de Diálogo

Este sistema complementa, mas não substitui, os testes convencionais de diálogo (como Persuasão, Enganação, Intimidação).

- O sistema de Estilo e Valoração ajusta o **relacionamento** com o NPC.
- Os **testes de diálogo** definem o sucesso prático de ações sociais (convencer, enganar, etc.).
- Ambos podem ser usados em conjunto na mesma fala.

> **Nota:** Este sistema permite que uma fala seja julgada em três dimensões: **forma** (estilo), **conteúdo** (valor) e **intenção/resultados** (teste de diálogo). Essa separação aumenta a profundidade e reatividade dos diálogos.
