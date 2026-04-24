Os tipos de armas representam uma forma lógica e conceitual de agrupar armas com características semelhantes dentro da PlayTale Engine. Esta classificação **não é fixa** e **não representa um conjunto universal de categorias**, mas sim um agrupamento criado pelo desenvolvedor do jogo com base em similaridades mecânicas e funcionais.

A categorização em tipos é especialmente útil por vários motivos técnicos e organizacionais no sistema:

# Finalidade da Classificação em Tipos

- **Afinidade com Classes e Raças**: permite aplicar bônus ou penalidades de forma ampla a todos os itens de um determinado tipo, facilitando o balanceamento e diferenciação entre personagens.
- **Simplicidade nas Tabelas de Bônus**: ao aplicar modificadores por tipo, evita-se a necessidade de definir valores para cada arma individualmente.
- **Consistência em Atributos-Chave**: armas do mesmo tipo costumam compartilhar atributos primários, padrões de dano ou comportamento em combate.
- **Compatibilidade entre Ambientações**: o mesmo tipo pode representar armas diferentes conforme o cenário. Ex: uma “arma de corte leve” pode ser uma adaga em um jogo medieval ou um bisturi de combate em um jogo sci-fi.

# Como Criar Tipos de Armas no Sistema

O desenvolvedor pode criar seus próprios tipos de armas conforme a ambientação do jogo. Para isso, recomenda-se considerar os seguintes pontos:

- **Função da Arma:** ataque leve, pesado, à distância, mágico, suporte, etc.
- **Estilo de Uso:** furtivo, de força bruta, de precisão, de área, etc.
- **Atributo Principal Usado:** Força, Destreza, Intelecto, etc.
- **Características Táticas:** uso com uma ou duas mãos, alcance, velocidade de ataque, chance crítica.
- **Tecnologia ou Magia Associada:** comum em jogos futuristas ou arcanos.


Utilize o [Modelo de Ficha de Tipo de Arma](modeloDeFichaDeTipoDeArma.md) existente para auxilar na criação de Tipos de Armas de sucesso!

# Exemplo de Tipos de Armas Criados por um Jogo

Estes são exemplos ilustrativos de como um sistema pode organizar seus tipos — não são obrigatórios:

- Corpo a corpo leve (ex: adagas, facas)
- Corpo a corpo pesado (ex: machados, martelos)
- À distância curta (ex: arcos curtos, pistolas)
- À distância longa (ex: rifles, arcos longos)
- Foco mágico (ex: cajados, grimórios)
- Arma de energia (ex: laser, raio pulsante)
- Arma improvisada (ex: pedaços de madeira, ferramentas)
- Arma simbiótica (ex: criaturas vivas que funcionam como armas)

# Aplicações Técnicas nos Sistemas da Engine

- A ficha da arma deve conter um campo para registrar o seu **Tipo de Arma**.
- As tabelas de compatibilidade de [Raças e Classes](racasEClasses.md) com armas devem utilizar esses tipos como chave de referência para conceder bônus, penalidades ou restrições.
- Sistemas derivados como [Ciclo de Batalha](cicloDeBatalha.md), [Modificadores](modificadores.md) e [Testes de Jogo](testesDeJogo.md) podem utilizar o tipo da arma como fator em testes específicos.

# Observações Finais

A engine não impõe uma taxonomia obrigatória. O conceito de "tipo" deve ser ajustado conforme o estilo de jogo, ambientação e intenção do desenvolvedor. É essencial, no entanto, que os tipos definidos sejam usados de forma consistente em todas as seções relacionadas para garantir coerência nos cálculos, testes e interações.
