# Página principal

**Mystery Realms** é um jogo de RPG focado em investigação, diálogos e batalhas, ambientado em um mundo fictício e místico de época medieval.

O projeto é dividido em duas partes principais:

* **PlayTale Engine** – A engine do sistema. Aqui estão concentradas as regras, cálculos e estruturas que regem o funcionamento de qualquer jogo de RPG. Essa engine é pensada para ser reutilizável e modular, servindo tanto para o mundo de *Mystery Realms* quanto para outros jogos de estilos diferentes, como um RPG cyberpunk futurista ou uma aventura espacial. Nesta seção, documentamos o funcionamento da engine, suas regras fundamentais e como ela pode ser aplicada a diferentes mundos e gêneros.

* **Mystery Realms** – A aplicação da engine no universo específico deste jogo. Aqui definimos o cenário, personagens, facções, regiões e enredos, utilizando todas as mecânicas fornecidas pela PlayTale Engine.

## 1 PlayTale Engine

Aqui tratamos do funcionamento do jogo, como as partes do jogo se encaixam e interagem entre si. Fórmulas e regras são registradas aqui. Em outras palavras as engrenagens do jogo, que seriam comuns, por exemplo, para o funcionamento de outro universo e jogo.

> [!NOTE]
> Itens marcados com '!' precisam de uma revisão desde que o PlayTale foi destacado do Mystery Realms.

* Definições Gerais - Sessão dedicada às definições gerais da *Mecânica do PlayTale*, que explica os cálculos e funcionamento geral do sistema, independente do tipo de jogo criado.
 * Estrutura Temporal - Definição de controle do tempo, estações do ano, calendário lunar, eventos sazonais, e muito mais. ([ver](./Estrutura_Temporal.md))
 * Estrutura de Localização - A estrutura de localização define como os "lugares" do jogo são organizados e suas funções estratégicas. ([ver](./Estrutura_de_Localizacao.md))
 * Mapa e Movimentação - Conceitos sobre o mapa e especificações sobre a movimentação do jogador. ([ver âncora](./Estrutura_de_Localizacao.md#mapa-e-movimentacao))
 * Economia - Definição e controle do sistema monetário do jogo. ([ver](./PlayTale_Engine.md#economia))
 * Raças e Classes - Conceitos sobre definições de Raças e Classes do mundo. ([ver](./PlayTale_Engine.md#racas-e-classes))
 * Danos e Aflições - Definição e explicação dos tipos de dano e dos tipos de aflição que suportados pela engine. ([ver](./PlayTale_Engine.md#danos-e-aflicoes))
 * Defesa e Bloqueio - Explicações da diferença entre 'Defesa do Ataque' e 'Bloqueio do Dano'. ([ver](./PlayTale_Engine.md#defesa-e-bloqueio))
 * Tipos de Resistências - Explicações sobre os diferentes tipos de Resistências (Física, Elementais, Mágicas, Aflições, Mentais e Espirituais). ([ver](./PlayTale_Engine.md#tipos-de-resistencias))
 * Moral - Definição sobre o estado mental do jogador. ([ver](./PlayTale_Engine.md#moral))
 * Acampamentos - Definições e conceitos sobre o funcionamento dos acampamentos. ([ver](./PlayTale_Engine.md#acampamentos))
 * Itens - Chamamos de Itens todos os objetos do jogo, como a classe principal da hierarquia. São Itens: Elmos, Escudos, Botas, Comida, Ferramentas, Armas, Objetos, Runas e muito mais. ([ver](./Itens_de_Mao.md))
 * Itens de Mão - Itens de mão são todos os objetos que podem ser empunhados, carregados ou utilizados diretamente pelas mãos do personagem, seja em combate, exploração, interação social ou mágica. ([ver](./Itens_de_Mao.md))
 * Tipos de Armas - Para os itens de mão da categoria 'arma', permite que o jogo agrupe as armas em tipos, como Arcos, Bestas, Espadas, Maças, etc. ([ver](./Itens_de_Mao.md#tipos-de-armas))

* Controle do Jogador - Definições dos controles do jogador.
 * Modificadores - Explicação sobre os modificadores que podem ser aplicados ao jogo para alterar as condições padrão do jogo de forma temporária. ([ver](./PlayTale_Engine.md#modificadores))
 * Fadiga, Fome e Sede - Conceito e funcionamento sobre a Fadiga, Fome e Sede. ([ver](./PlayTale_Engine.md#fadiga-fome-e-sede))
 * Recuperação - Explicação dos diferentes tipos de descansos e recuperação possíveis do personagem. ([ver](./PlayTale_Engine.md#recuperacao))
 * Morte e Desmaios - Definições sobre morte e desmaio do jogador. ([ver](./PlayTale_Engine.md#morte-e-desmaios))
 * Ações do Jogador - Definição dos detalhes das ações que podem ser tomadas pelo jogador. ([ver](./PlayTale_Engine.md#acoes-do-jogador))

* Controle do Progresso do Jogo - Definições relacionadas ao controle de progressão do jogo.
 * Sistema de Marcadores - Explicação do que é e como funciona o sistema de marcadores/marcos/milestones no controle do progresso do jogo. ([ver](./PlayTale_Engine.md#sistema-de-marcadores))
 * Testes de Jogo - Documentação dos diferentes tipos de testes realizados durante o jogo. ([ver](./PlayTale_Engine.md#testes-de-jogo))
 * Ficha do Jogador - Nesta página é abordada a ficha do jogador, explicados seus atributos, sua matemática, e todo o controle das informações do jogador. ([ver](./PlayTale_Engine.md#ficha-do-jogador))

* Fluxo Narrativo - Funcionamento do controle de fluxo narrativo e suas ramificações.
 * Diário do Jogador - Fundamentos e sugestão do Diário do Jogador. ([ver](./PlayTale_Engine.md#diario-do-jogador))
 * Interação com NPCs - Bloco com as definições da mecânica das interações e relacionamento entre o jogador e os NPCs. ([ver](./PlayTale_Engine.md#interacao-com-npcs))
 * Estilos de Fala e Valoração - Os Estilos de Fala definem o tom, abordagem e linguagem utilizados pelo jogador ao interagir com NPCs. ([ver](./PlayTale_Engine.md#estilos-de-fala-e-valoracao))
 * Estrutura de Diálogo - Definições de como criar, organizar e documentar os fluxos de diálogo. ([ver](./PlayTale_Engine.md#estrutura-de-dialogo))

* Modelos de Fichas - Sessão com os modelos de fichas para criação e controle de diversos elementos do Jogo.
 * Modelo de Ficha de NPC ([ver](./PlayTale_Engine.md#modelo-de-ficha-de-npc))
 * Modelo de Ficha de Personalidade ([ver](./PlayTale_Engine.md#modelo-de-ficha-de-personalidade))
 * Modelo de Ficha de Diálogo ([ver](./PlayTale_Engine.md#modelo-de-ficha-de-dialogo))
 * Modelo de Ficha de Tipo de Arma ([ver](./Itens_de_Mao.md#modelo-de-ficha-de-tipo-de-arma))
 * Modelo de Ficha de Itens de Mão ([ver](./Itens_de_Mao.md#modelo-de-ficha-de-itens-de-mao))
 * Modelo de Ficha de Armas ([ver](./Itens_de_Mao.md#modelo-de-ficha-de-armas))
 * Modelo de Ficha de Escudos ([ver](./Itens_de_Mao.md#modelo-de-ficha-de-escudos))
 * Modelos de Fichas de Lugares ([ver](./PlayTale_Engine.md#modelos-de-fichas-de-lugares))
 * Modelo de Ficha de Zonas ([ver](./PlayTale_Engine.md#modelo-de-ficha-de-zonas))
 * Modelo de Ficha de Ambientes ([ver](./PlayTale_Engine.md#modelo-de-ficha-de-ambientes))

* Ciclo de Batalha - Aqui é documentado e explicado os passos e funcionamento das batalhas do jogo. ([ver](./PlayTale_Engine.md#ciclo-de-batalha))

* Progressão do Jogo - Métricas e regras sobre como o jogo progride, o jogador evolui, etc. ([ver](./PlayTale_Engine.md#progressao-do-jogo))

* Story Telling Mystery Realms - O mundo de Mystery Realms inclui criaturas mágicas, feitiçaria, alquimia, forças elementais e tecnologias esquecidas. Embora tais forças existam, não é um mundo mágico e de ilusões. ([ver](./PlayTale_Engine.md))

* Modelos de Documentação
 * Fichas de Criação - Modelos de Fichas de Criação a serem seguidos para criar "tudo" em Mystery Realms. ([ver](./PlayTale_Engine.md#fichas-de-criacao))

* Controle e Definições do Jogo
 * Marcadores do Jogo - Marcadores utilizados dentro do jogo de Mystery Realms. ([ver](./PlayTale_Engine.md#marcadores-do-jogo))

* Criação do Mundo Mystery Realms
 * Raças - Definições das Raças e seus atributos. ([ver](./PlayTale_Engine.md#racas))
 * Classes - Classes dos aventureiros. ([ver](./PlayTale_Engine.md#classes))
 * Lugares - Lugares é a expressão utilizada para todo e qualquer tipo de lugar físico do jogo. Entretanto cada lugar representa um escopo diferente de agrupamento e regras. ([ver](./Thamyros.md))
 * Thámyros - Primeiro Mundo (Realm) do Jogo. Definição do plano / planeta de localização. ([ver](./Thamyros.md))
 * Terras Centrais - Continente Central de Thámyros ([ver](./Thamyros.md#terras-centrais))
 * Reino de Althar - Nação conquistada e regida pela linhagem de Althar. ([ver](./Thamyros.md#reino-de-althar))
 * Floresta de Arvalem - Região de Floresta do Reino de Althar. ([ver](./Thamyros.md#floresta-de-arvalem))
 * Langur - Uma das cidades mais icônicas das Terras Centrais, dividida em três anéis principais que representam conhecimento, poder e comércio. ([ver](./Langur.md))
 * Taverna Asa Quebrada - Estabelecimento discreto e denso em atmosfera, ponto de encontros sigilosos e vigilância de seu misterioso proprietário. ([ver](./Langur.md#taverna-asa-quebrada))
 * Salão da Asa Quebrada - Ambiente principal da taverna, usado para socialização tensa, vigilância e interações discretas entre NPCs de múltiplos interesses. ([ver](./Langur.md#salao-da-asa-quebrada))
 * Biblioteca Varnak - Torre arcana viva onde o conhecimento se molda ao visitante. Contém seções mutáveis e guardiões mágicos. ([ver](./Langur.md#biblioteca-varnak))
 * Sala de Recepção - Área de entrada onde Lirianne Morth avalia as intenções dos visitantes e decide seu acesso às áreas superiores da biblioteca. ([ver](./Langur.md#sala-de-recepcao))
 * Forja dos Ecos - Forja ancestral onde itens são imbuídos com identidade, operada por Thramur Ankuhl e sua aprendiz rúnica. ([ver](./Langur.md#forja-dos-ecos))
 * Sala de Fundição - Centro produtivo da forja, onde itens mágicos são criados, pedidos são aceitos e as propriedades rúnicas são manipuladas. ([ver](./Langur.md#sala-de-fundicao))
 * Arsenal Três Lâminas - Loja especializada em itens recuperados de ruínas e campos de batalha, respeitada por sua neutralidade comercial. ([ver](./Langur.md#arsenal-tres-laminas))
 * Salão da Loja - Área de exibição e negociação, com armas e armaduras à mostra, onde Silma e Vareth conduzem suas atividades. ([ver](./Langur.md#salao-da-loja))
 * Praça das Vozes - Coração público e comercial de Langur, repleto de vozes, comércio, rumores e manifestações diversas. ([ver](./Langur.md#praca-das-vozes))
 * Círculo dos Três Ecos - Fonte sagrada ao centro da praça, marco simbólico da união entre povo, magia e força. Ponto de partida do jogo. ([ver](./Langur.md#circulo-dos-tres-ecos))
 * Casa de Custódia - Instituição bancária e mágica onde são guardados itens, contratos e segredos dos poderosos. ([ver](./Langur.md#casa-de-custodia))
 * Salão de Atendimento - Área formal e vigiada onde ocorrem interações bancárias e decisões contratuais monitoradas magicamente. ([ver](./Langur.md#salao-de-atendimento))
 * Mercado da Pedra Branca - Principal centro de suprimentos e artigos básicos de Langur, sempre ativo e cercado de boatos e ofertas. ([ver](./Langur.md#mercado-da-pedra-branca))
 * Conexões de Mystery Realms - Mapa de navegação do Mystery Realms (conexões das Zonas e Ambientes). ([ver](./MysteryRealms.md#conexoes))
 * Personagens - Ficha de Personagens (NPCs, Personalidades e outros) existentes no jogo. ([ver](./Personagens.md))
 * NPCs - Lista de NPCs do Jogo. ([ver](./Personagens.md#npcs))
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
 * Facções - Detalhes sobre as Facções (Grupos, Guildas, Ordens, Tributos, Seitas, etc.) existentes no jogo. ([ver](./PlayTale_Engine.md#facoes))

## 18.1 Criaturas e Monstros

* Introdução
 * As criaturas e monstros de Mystery Realms compõem a vida selvagem, os perigos mágicos e os inimigos ocultos que habitam o continente. Elas podem ser encontradas em áreas selvagens, ruínas, cidades corrompidas, mas também podem ter função narrativa, como guardiões ou provas vivas de antigas magias.
 * Nem todas são hostis. Algumas podem ser neutras, fugir ao ver o jogador ou até mesmo oferecer interação com certas habilidades ou níveis de conhecimento. Outras são entidades únicas — chefes, protetores, ou mesmo criaturas raríssimas ligadas a relíquias e quests específicas.
* Escalonamento de Atributos
 * Para manter o desafio dinâmico, as criaturas possuem atributos que escalam com seu nível, garantindo que batalhas se mantenham equilibradas independentemente da progressão do jogador.
 * A fórmula para calcular qualquer atributo de uma criatura é:

 A_final = floor(A_base * (1 + N * F))

 * Onde:
 * A_final: Atributo final da criatura
 * A_base: Valor base do atributo da criatura (definido por tipo)
 * N: Nível da criatura
 * F: Fator de crescimento (F = 0,2)

 > [!NOTE]
 > Com esse fator, o atributo da criatura dobra a cada 5 níveis.

 * Nível e Dificuldade
 * As criaturas podem variar seu nível dependendo do local em que são encontradas:
 * **Áreas fáceis:** até 2 níveis abaixo do jogador
 * **Áreas comuns:** mesmo nível do jogador
 * **Áreas perigosas:** até 3 níveis acima do jogador
 * **Zonas de elite:** 4 ou mais níveis acima, com chance de habilidades especiais, efeitos mágicos e loot raro

(continuação do conteúdo detalhado das criaturas, fichas e exemplos mantido no repositório)

