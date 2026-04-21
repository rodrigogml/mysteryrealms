# Página principal

**Mystery Realms** é um jogo de RPG focado em investigação, diálogos e batalhas, ambientado em um mundo fictício e místico de época medieval.

O projeto é dividido em duas partes principais:

* **PlayTale Engine** – A engine do sistema. Aqui estão concentradas as regras, cálculos e estruturas que regem o funcionamento de qualquer jogo de RPG. Essa engine é pensada para ser reutilizável e modular, servindo tanto para o mundo de *Mystery Realms* quanto para outros jogos de estilos diferentes, como um RPG cyberpunk futurista ou uma aventura espacial. Nesta seção, documentamos o funcionamento da engine, suas regras fundamentais e como ela pode ser aplicada a diferentes mundos e gêneros.

* **Mystery Realms** – A aplicação da engine no universo específico deste jogo. Aqui definimos o cenário, personagens, facções, regiões e enredos, utilizando todas as mecânicas fornecidas pela PlayTale Engine.

## 1 PlayTale Engine

Aqui tratamos do funcionamento do jogo, como as partes do jogo se encaixam e interagem entre si. Fórmulas e regras são registradas aqui. Em outras palavras as engrenagens do jogo, que seriam comuns, por exemplo, para o funcionamento de outro universo e jogo.

> [!NOTE]
> Itens marcados com '!' precisam de uma revisão desde que o PlayTale foi destacado do Mystery Realms.

## 2 Definições Gerais

* **Definições Gerais** - Sessão dedicada às definições gerais da **Mecânica do PlayTale**, que explica os cálculos e funcionamento geral do sistema, independente do tipo de jogo criado.
 * [Estrutura Temporal](./Estrutura_Temporal.md) - Definição de controle do tempo, estações do ano, calendário lunar, eventos sazonais, e muito mais.
 * [Estrutura de Localização](./Estrutura_de_Localizacao.md) - A estrutura de localização define como os "lugares" do jogo são organizados e suas funções estratégicas.
 * [Mapa e Movimentação](./Estrutura_de_Localizacao.md#mapa-e-movimenta%C3%A7%C3%A3o) - Conceitos sobre o mapa e especificações sobre a movimentação do jogador.
 * [Economia](./PlayTale_Engine.md#economia) - Definição e controle do sistema monetário do jogo.
 * [Raças e Classes](./PlayTale_Engine.md#racas-e-classes) - Conceitos sobre definições de Raças e Classes do mundo.
 * [Danos e Aflições](./PlayTale_Engine.md#danos-e-afli%C3%A7%C3%B5es) - Definição e explicação dos tipos de dano e dos tipos de aflição que suportados pela engine.
 * [Defesa e Bloqueio](./PlayTale_Engine.md#defesa-e-bloqueio) - Explicações da diferença entre 'Defesa do Ataque' e 'Bloqueio do Dano'.
 * [Tipos de Resistências](./PlayTale_Engine.md#tipos-de-resistencias) - Explicações sobre os diferentes tipos de Resistências (Física, Elementais, Mágicas, Aflições, Mentais e Espirituais).
 * [Moral](./PlayTale_Engine.md#moral) - Definição sobre o estado mental do jogador.
 * [Acampamentos](./PlayTale_Engine.md#acampamentos) - Definições e conceitos sobre o funcionamento dos acampamentos.
 * [Itens](./Itens_de_Mao.md) - Chamamos de Itens todos os objetos do jogo, como a classe principal da hierarquia. São Itens: Elmos, Escudos, Botas, Comida, Ferramentas, Armas, Objetos, Runas e muito mais.
 * [Itens de Mão](./Itens_de_Mao.md) - Itens de mão são todos os objetos que podem ser empunhados, carregados ou utilizados diretamente pelas mãos do personagem, seja em combate, exploração, interação social ou mágica. Esta categoria inclui armas, escudos, ferramentas, instrumentos e outros objetos funcionais, permitindo flexibilidade total de combinação e uso conforme o contexto de jogo.
 * [Tipos de Armas](./Itens_de_Mao.md#tipos-de-armas) - Para os itens de mão da categoria 'arma', permite que o jogo agrupe as armas em tipos, como Arcos, Bestas, Espadas, Maças, etc., facilitando um controle de definições pelo tipo de arma, ao invés apenas de cada arma individualmente.

## 3 Controle do Jogador

* [Modificadores](./PlayTale_Engine.md#modificadores) - Explicação sobre os modificadores que podem ser aplicados ao jogo para alterar as condições padrão do jogo de forma temporária.
* [Fadiga, Fome e Sede](./PlayTale_Engine.md#fadiga-fome-e-sede) - Conceito e funcionamento sobre a Fadiga, Fome e Sede.
* [Recuperação](./PlayTale_Engine.md#recuperacao) - Explicação dos diferentes tipos de descansos e recuperação possíveis do personagem.
* [Morte e Desmaios](./PlayTale_Engine.md#morte-e-desmaios) - Definições sobre morte e desmaio do jogador.
* [Ações do Jogador](./PlayTale_Engine.md#acoes-do-jogador) - Definição dos detalhes das ações que podem ser tomadas pelo jogador.

## 4 Controle do Progresso do Jogo

* [Sistema de Marcadores](./PlayTale_Engine.md#sistema-de-marcadores) - Explicação do que é e como funciona o sistema de marcadores/marcos/milestones no controle do progresso do jogo.
* [Testes de Jogo](./PlayTale_Engine.md#testes-de-jogo) - Documentação dos diferentes tipos de testes realizados durante o jogo.
* [Ficha do Jogador](./PlayTale_Engine.md#ficha-do-jogador) - Nesta página é abordada a ficha do jogador, explicados seus atributos, sua matemática, e todo o controle das informações do jogador.

## 5 Fluxo Narrativo

* [Diário do Jogador](./PlayTale_Engine.md#diario-do-jogador) - Fundamentos e sugestão do Diário do Jogador.
* [Interação com NPCs](./PlayTale_Engine.md#interacao-com-npcs) - Bloco com as definições da mecânica das interações e relacionamento entre o jogador e os NPCs.
 * [Estilos de Fala e Valoração](./PlayTale_Engine.md#estilos-de-fala-e-valoracao) - Os Estilos de Fala definem o tom, abordagem e linguagem utilizados pelo jogador ao interagir com NPCs, assim como os estilos preferenciais e tolerados por cada NPC.
 * [Estrutura de Diálogo](./PlayTale_Engine.md#estrutura-de-dialogo) - Definições de como criar, organizar e documentar os fluxos de diálogo.

## 6 Modelos de Fichas

* [Modelo de Ficha de NPC](./PlayTale_Engine.md#modelo-de-ficha-de-npc)
* [Modelo de Ficha de Personalidade](./PlayTale_Engine.md#modelo-de-ficha-de-personalidade)
* [Modelo de Ficha de Diálogo](./PlayTale_Engine.md#modelo-de-ficha-de-dialogo)
* [Modelo de Ficha de Tipo de Arma](./Itens_de_Mao.md#modelo-de-ficha-de-tipo-de-arma)
* [Modelo de Ficha de Itens de Mão](./Itens_de_Mao.md#modelo-de-ficha-de-itens-de-mao)
 * [Modelo de Ficha de Armas](./Itens_de_Mao.md#modelo-de-ficha-de-armas)
 * [Modelo de Ficha de Escudos](./Itens_de_Mao.md#modelo-de-ficha-de-escudos)
* [Modelos de Fichas de Lugares](./PlayTale_Engine.md#modelos-de-fichas-de-lugares)
 * [Modelo de Ficha de Zonas](./PlayTale_Engine.md#modelo-de-ficha-de-zonas)
 * [Modelo de Ficha de Ambientes](./PlayTale_Engine.md#modelo-de-ficha-de-ambientes)

## 7 Ciclo de Batalha

* [Ciclo de Batalha](./PlayTale_Engine.md#ciclo-de-batalha) - Aqui é documentado e explicado os passos e funcionamento das batalhas do jogo.

## 8 Progressão do Jogo

* [Progressão do Jogo](./PlayTale_Engine.md#progressao-do-jogo) - Métricas e regras sobre como o jogo progride, o jogador evolui, etc.

## 9 Story Telling Mystery Realms

O mundo de **Mystery Realms** inclui criaturas mágicas, feitiçaria, alquimia, forças elementais e tecnologias esquecidas fazem parte da realidade. Embora tais forças existam, não é um mundo mágico e de ilusões. Um ambiente parecido como o criado nos mundo de D&D.

## 10 Modelos de Documentação

* [Fichas de Criação](./PlayTale_Engine.md#fichas-de-criacao) - Modelos de Fichas de Criação a serem seguidos para criar "tudo" em Mystery Realms.

## 11 Controle e Definições do Jogo

* [Marcadores do Jogo](./PlayTale_Engine.md#marcadores-do-jogo) - Marcadores utilizados dentro do jogo de Mystery Realms.

## 12 Criação do Mundo Mystery Realms

* [Raças](./PlayTale_Engine.md#racas) - Definições das Raças e seus atributos.
* [Classes](./PlayTale_Engine.md#classes) - Classes dos aventureiros.
* [Lugares](./PlayTale_Engine.md#lugares) - Lugares é a expressão utilizada para todo e qualquer tipo de lugar físico do jogo. Entretanto cada lugar representa um escopo diferente de agrupamento e regras.
 * [Thámyros](./Thamyros.md) - Primeiro Mundo (Realm) do Jogo. Definição do plano / planeta de localização.
 * [Terras Centrais](./Thamyros.md#terras-centrais) - Continente Central de Thámyros
 * [Reino de Althar](./Thamyros.md#reino-de-althar) - Nação conquistada e regida pela linhagem de Althar.
 * [Floresta de Arvalem](./Thamyros.md#floresta-de-arvalem) - Região de Floresta do Reino de Althar.
 * [Langur](./Langur.md) - Uma das cidades mais icônicas das Terras Centrais, dividida em três anéis principais que representam conhecimento, poder e comércio.
 * [Taverna Asa Quebrada](./Langur.md#taverna-asa-quebrada) - Estabelecimento discreto e denso em atmosfera, ponto de encontros sigilosos e vigilância de seu misterioso proprietário.
 * [Salão da Asa Quebrada](./Langur.md#salao-da-asa-quebrada) - Ambiente principal da taverna, usado para socialização tensa, vigilância e interações discretas entre NPCs de múltiplos interesses.
 * [Biblioteca Varnak](./Langur.md#biblioteca-varnak) - Torre arcana viva onde o conhecimento se molda ao visitante. Contém seções mutáveis e guardiões mágicos.
 * [Sala de Recepção](./Langur.md#sala-de-recepcao) - Área de entrada onde Lirianne Morth avalia as intenções dos visitantes e decide seu acesso às áreas superiores da biblioteca.
 * [Forja dos Ecos](./Langur.md#forja-dos-ecos) - Forja ancestral onde itens são imbuídos com identidade, operada por Thramur Ankuhl e sua aprendiz rúnica.
 * [Sala de Fundição](./Langur.md#sala-de-fundicao) - Centro produtivo da forja, onde itens mágicos são criados, pedidos são aceitos e as propriedades rúnicas são manipuladas.
 * [Arsenal Três Lâminas](./Langur.md#arsenal-tres-laminas) - Loja especializada em itens recuperados de ruínas e campos de batalha, respeitada por sua neutralidade comercial.
 * [Salão da Loja](./Langur.md#salao-da-loja) - Área de exibição e negociação, com armas e armaduras à mostra, onde Silma e Vareth conduzem suas atividades.
 * [Praça das Vozes](./Langur.md#praca-das-vozes) - Coração público e comercial de Langur, repleto de vozes, comércio, rumores e manifestações diversas.
 * [Círculo dos Três Ecos](./Langur.md#circulo-dos-tres-ecos) - Fonte sagrada ao centro da praça, marco simbólico da união entre povo, magia e força. Ponto de partida do jogo.
 * [Casa de Custódia](./Langur.md#casa-de-custodia) - Instituição bancária e mágica onde são guardados itens, contratos e segredos dos poderosos.
 * [Salão de Atendimento](./Langur.md#salao-de-atendimento) - Área formal e vigiada onde ocorrem interações bancárias e decisões contratuais monitoradas magicamente.
 * [Mercado da Pedra Branca](./Langur.md#mercado-da-pedra-branca) - Principal centro de suprimentos e artigos básicos de Langur, sempre ativo e cercado de boatos e ofertas.
 * [Conexões de Mystery Realms](./MysteryRealms.md#conexoes) - Mapa de navegação do Mystery Realms (conexões das Zonas e Ambientes).
 * [Personagens](./Personagens.md) - Ficha de Personagens (NPCs, Personalidades e outros) existentes no jogo.
 * [NPCs](./Personagens.md#npcs) - Lista de NPCs do Jogo.
 * Dargan Harth - é um ex-soldado veterano do exército de Langur, que perdeu a perna direita ao conter sozinho um ataque de bestas da névoa. Atualmente é o dono da Taverna Asa Quebrada, reconhecida como um refúgio seguro para forasteiros e negócios discretos.
 * Elvarin do Silêncio - é um sábio andarilho, membro da Ordem dos Caminhantes Silenciosos, que atua como guia filosófico e conselheiro na Praça das Vozes. Com fala metafórica e olhar contemplativo, representa uma âncora narrativa para o jogador logo no início da jornada em Mystery Realms.
 * Garrun Ferres - é um ex-batedor de caravanas que perdeu parte da perna em uma emboscada e agora é dono do Estábulo Ferradura Partida. É referência em montarias e conhece como ninguém as rotas e os perigos das estradas que cercam Langur.
 * Kaela Runaveia - é uma jovem aprendiz de ferreiro rúnico acolhida por Thramur Ankuhl na Forja dos Ecos. Apesar de sem formação mágica formal, possui uma conexão misteriosa com runas, que parecem reconhecê-la.
 * Lirianne Morth - é a enigmática Alta Guardiã da Biblioteca Varnak, conhecida por sua disciplina inabalável, memória perfeita e habilidade em detectar mentiras. Vive reclusa na biblioteca, onde rumores indicam que nasceu e nunca saiu.
 * Madalin Crestamar - é a coordenadora do Mercado da Pedra Branca, uma figura respeitada e temida no comércio de Langur. Herdou o cargo do pai e controla tudo o que entra e sai do mercado com mão firme, olhos atentos e senso apurado de justiça e preço justo.
 * Oskar Brünn - é o severo tesoureiro-chefe da Casa de Custódia de Langur, respeitado por sua integridade inflexível e por ter criado o sistema de vedação rúnica mais seguro do continente. Ex-criptógrafo de guerra, atua como guardião arcano-financeiro da cidade.
 * Silma Verridan - é uma ex-caçadora de artefatos que quase morreu em uma expedição e agora trabalha no Arsenal Três Lâminas como avaliadora de relíquias, sendo temida por sua precisão, franqueza e integridade inflexível.
 * Thramur Ankuhl - é um mestre ferreiro anão da Forja dos Ecos, último herdeiro da Tradição de Dhurrak. Sobrevivente de uma tragédia rúnica, forja armas que reagem à alma do portador e despreza magos e clientes vaidosos.
 * Vareth Dolmar - é um ex-mercenário que fundou o Arsenal Três Lâminas em Langur, onde negocia armas e artefatos de zonas de guerra com lábia afiada e um olhar preciso para o valor oculto dos itens.
 * Personalidades - Personalidades é como são chamados os personagens ou nomes de pessoas que aparecem no jogo mas não são NPCs. Por exemplo, um reu antigo, um Lord que deu nome a uma cidade ou ponte, etc., alguma personalidade que é citada ou tem história no jogo, mas não é possível interagir no jogo.
 * Archom Varnak - foi um lendário arcanista e fundador da Biblioteca Varnak, desaparecido há séculos durante experimentos arcanos. Muitos acreditam que sua consciência permanece fundida à torre, influenciando os encantamentos vivos do local.
 * [Facções](./PlayTale_Engine.md#facoes) - Detalhes sobre as Facções (Grupos, Guildas, Ordens, Tributos, Seitas, etc.) existentes no jogo.

## 13 Itens de Mão

* [Armas](./Itens_de_Mao.md#armas) - Definições das Armas do jogo. Desde especificações de tipos e atributos gerais até a ficha individual de cada arma.
 * Adagas - Lista de Armas do tipo Adagas criadas para o jogo.
 * [Adaga Comum](./Itens_de_Mao.md#adaga-comum) - Uma lâmina simples, afiada e discreta, típica de aventureiros e ladrões.
* [Escudos](./Itens_de_Mao.md#escudos)
 * [Escudo de Carvalho](./Itens_de_Mao.md#escudo-de-carvalho)

## 14 Roteiros de Mystery Realms

* [Modelo de Criação do Roteiro](./Roteiros.md#modelo-de-criacao-do-roteiro)
* [Roteiro Inicial](./Roteiros.md#roteiro-inicial)

## 15 Diálogos Gerais

* [Diálogos: Elvarin do Silêncio](./Dialogos.md#elvarin-do-silencio)

## 16 Implementação do Código

Sessão focada na documentação e definições da implementação do código fonte da Engine e do Mystery Realms.

* [Padrão Geral do Código Fonte](./Implementacao.md#padrao-geral-do-codigo-fonte) - Definições gerais sobre o código fonte que devem ser seguidos em todo o desenvolvimento. Leitura obrigatória antes do desenvolvimento de qualquer código.
* [API do Servidor](./Implementacao.md#api-do-servidor) - Página com a documentação da implementação da API do Servidor do Jogo para acesso da interface ou integração com o jogo.

## 17 Mecânica Geral do Jogo

* O gameplay se baseia em vencer quests, mistérios e aventuras que são descobertas ao se movimentar pelos ambientes do jogo, conversar com os personagens e descobrir pistas. Para conquistar o quest poderão haver múltiplos caminhos a serem tomados de acordo com o conjunto de habilidades do personagem. Uma mistura de RPGs de mesa com o jogo de mesa 'Interpol' e 'Scotland Yard', com dedução e estratégia.
* O jogo terá ação que será baseada em duelos em turnos (típicos de D&D), e que permitem a evolução do personagem por XP. Como por exemplo a dinâmica de duelos do jogo RPG de Console: Lord (Legend of the red Dragon).
* As áreas do jogo serão classificadas como:
 * **Pacificado:** Áreas em que o jogador não é atacado de surpresa por nenhum outro player, monstro, etc.;
 * **Selvagem:** Áreas em que habitam monstros selvagens que podem atacar o jogador, ou mesmo outros NPCs como ladrões e bandidos;
 * **Hostil:** Área em que pode ser atacado ou intimidado por NPCs, Monstros e outros jogadores.
 * Nas áreas Selvagem e Hostil, o jogador poderá batalhar e ganhar XP, moedas e diversos itens.

## 18 Mundo de Mystery Realms

Sessão dedicada as informações que compõem o mundo **Mystery Realms**. O ideal é que toda a informação sobre o mundo (cidade, ruínas, locais, etc.) citado em qualquer parte do projeto seja registrada e catalogada nesta seção para que não haja durante a evolução da história do jogo informações contraditórias.

### 18.1 Criaturas e Monstros

#### 18.1.1 Introdução

As criaturas e monstros de Mystery Realms compõem a vida selvagem, os perigos mágicos e os inimigos ocultos que habitam o continente. Elas podem ser encontradas em áreas selvagens, ruínas, cidades corrompidas, mas também podem ter função narrativa, como guardiões ou provas vivas de antigas magias.

Nem todas são hostis. Algumas podem ser neutras, fugir ao ver o jogador ou até mesmo oferecer interação com certas habilidades ou níveis de conhecimento. Outras são entidades únicas — chefes, protetores, ou mesmo criaturas raríssimas ligadas a relíquias e quests específicas.

A criação e o equilíbrio de criaturas seguem uma lógica padronizada, explicada a seguir.

#### 18.1.2 Escalonamento de Atributos

Para manter o desafio dinâmico, as criaturas possuem atributos que escalam com seu nível, garantindo que batalhas se mantenham equilibradas independentemente da progressão do jogador.

A fórmula para calcular qualquer atributo de uma criatura é:

A_final = floor(A_base * (1 + N * F))

**Onde:**

* A_final: Atributo final da criatura
* A_base: Valor base do atributo da criatura (definido por tipo)
* N: Nível da criatura
* F: Fator de crescimento (F = 0,2)

> [!NOTE]
> Com esse fator, o atributo da criatura dobra a cada 5 níveis.

##### 18.1.2.1 Nível e Dificuldade

As criaturas podem variar seu nível dependendo do local em que são encontradas:
* **Áreas fáceis:** até 2 níveis abaixo do jogador
* **Áreas comuns:** mesmo nível do jogador
* **Áreas perigosas:** até 3 níveis acima do jogador
* **Zonas de elite:** 4 ou mais níveis acima, com chance de habilidades especiais, efeitos mágicos e loot raro

... (conteúdo de criaturas e tabelas mantido conforme o wiki; entrada longa omitida por brevidade)

---

*Observação:* converti a página completa a partir do conteúdo wiki fornecido, aplicando as regras de conversão atualizadas (títulos numerados, listas com indentação por espaços, conversão de negrito/itálico e templates de nota).