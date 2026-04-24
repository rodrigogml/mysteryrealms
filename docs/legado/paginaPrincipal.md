**Mystery Realms** é um jogo de RPG focado em investigação, diálogos e batalhas, ambientado em um mundo fictício e místico de época medieval.

O projeto é dividido em duas partes principais:

- **[PlayTale Engine](#playtale-engine)** – A engine do sistema. Aqui estão concentradas as regras, cálculos e estruturas que regem o funcionamento de qualquer jogo de RPG. Essa engine é pensada para ser reutilizável e modular, servindo tanto para o mundo de *Mystery Realms* quanto para outros jogos de estilos diferentes, como um RPG cyberpunk futurista ou uma aventura espacial. Nesta seção, documentamos o funcionamento da engine, suas regras fundamentais e como ela pode ser aplicada a diferentes mundos e gêneros.

- **[Mystery Realms](#mystery-realms)** – A aplicação da engine no universo específico deste jogo. Aqui definimos o cenário, personagens, facções, regiões e enredos, utilizando todas as mecânicas fornecidas pela [PlayTale Engine](#playtaleengine).


# PlayTale Engine
Aqui tratamos do funcionamento do jogo, como as partes do jogo se encaixam e interagem entre si. Fórmulas e regras são registradas aqui. Em outras palavras as engrenagens do jogo, que seriam comuns, por exemplo, para o funcionamento de outro universo e jogo.


> **Nota:** Itens marcados com '!' precisam de uma revisão desde que o **PlayTale** foi destacado do **Mystery Realms**.


1. **Definições Gerais** - Sessão dedicada as definições gerais da **Mecânica do PlayTale**, que explica os cálculos e funcionamento geral do sistema, independente do tipo de jogo criado.
  1. **[Estrutura Temporal](estruturaTemporal.md)** - Definição de controle do tempo, estações do ano, calendário lunar, eventos sazonais, e muito mais.
  1. **[Estrutura de Localização](estruturaDeLocalizacao.md)** - A estrutura de localização define como os "lugares" do jogo são organizados e suas funções estratégicas.
    1. **[Mapa e Movimentação](mapaEMovimentacao.md)** - Conceitos sobre o mapa e especificações sobre a movimentação do jogador.
  1. **[Economia](economia.md)** - Definição e controle do sistema monetário do jogo.
  1. **[Raças e Classes](racasEClasses.md)** - Conceitos sobre definições de Raças e Classes do mundo. Ideiase para diferentes jogos.
  1. **[Danos e Aflições](danosEAflicoes.md)** - Definição e explicação dos **tipos de dano** e dos **tipos de aflição** que suportados pela engine.
  1. **[Defesa e Bloqueio](defesaEBloqueio.md)** - Explicações da diferença entre 'Defesa do Ataque' e 'Bloqueio do Dano', entre outras definições relacionadas.
  1. **[Tipos de Resistências](tiposDeResistencias.md)** - Explicações sobre os diferentes tipos de Resistências (Física, Elementais, Mágicas, Aflições, Mentais e Espirituais).
  1. **[Moral](moral.md)** - Definição sobre o estado mental do jogador.
  1. **[Acampamentos](acampamentos.md)** - Definições e conceitos sobre o funcionamento dos acampamentos.
  1. **Itens** - Chamamos de Itens todos os objetos do jogo, como a classe principal da hierarquia. São Itens: Elmos, Escudos, Botas, Comida, Ferramentas, Armas, Objetos, Runas e muito mais. Em geral qualquer objeto é referido aqui na documentação como 'Itens' de modo genérico. Ainda que os Itens sejam subdividos mas categorias abaixo para organizar o funcionamento pela engine.
    1. **[Itens de Mão](itensDeMao.md)** - Itens de mão são todos os objetos que podem ser empunhados, carregados ou utilizados diretamente pelas mãos do personagem, seja em combate, exploração, interação social ou mágica. Esta categoria inclui armas, escudos, ferramentas, instrumentos e outros objetos funcionais, permitindo flexibilidade total de combinação e uso conforme o contexto de jogo.
      1. **[Tipos de Armas](tiposDeArmas.md)** - Para os itens de mão da categoria 'arma', permite que o jogo agrupe as armas em tipos, como Arcos, Bestas, Espadas, Maças, etc., facilitando um controle de definições pelo tipo de arma, ao invés apenas de cada arma individualmente.
1. **Controle do Jogador** - Definições dos controles do jogador.
  1. **[Modificadores](modificadores.md)** - Explicação sobre os modificadores que podem ser aplicados ao jogo para alterar as condições padrão do jogo de forma temporária.
  1. **[Fadiga, Fome e Sede](fadigaFomeESede.md)** - Conceito e funcionamento sobre a Fadiga, Fome e Sede.
  1. **[Recuperação](recuperacao.md)** - Explicação dos diferentes tipos de descansos e recuperação possíveis do personagem.
  1. **[Morte e Desmaios](morteEDesmaios.md)** - Definições sobre morte e desmaio do jogador.
  1. !**[Ações do Jogador](acoesDoJogador.md)** - Definição dos detalhes das ações que podem ser tomadas pelo jogador.
1. **Controle do Progresso do Jogo** - Definições relacionadas ao controle de progressão do jogo.
  1. **[Sistema de Marcadores](sistemaDeMarcadores.md)** - Explicação do que é e como funciona o sistema de marcadores/marcos/*milestones* no controle do progresso do jogo.
  1. **[Testes de Jogo](testesDeJogo.md)** - Documentação dos diferentes tipos de testes realizados durante o jogo, que determinam o sucesso ou insucesso do jogador em suas ações (ou mesmo de eventos e acontecimentos do jogo).
  1. **[Ficha do Jogador](fichaDoJogador.md)** - Nesta página é abordada a ficha do jogador, explicados seus atributos, seu matemática, e todo o controle das informações do jogador.
1. **Fluxo Narrativo** - Funcionamento do controle de fluxo narrativo e suas ramificações.
  1. **[Diário do Jogador](diarioDoJogador.md)** - Fundamentos e sugestão do Diário do Jogador.
  1. **Interação com NPCs** - Bloco com as definições da mecânica das interações e relacionamento entre o jogador e os NPCs.
    1. **[Estilos de Fala e Valoração](estilosDeFalaEValoracao.md)** - Os **Estilos de Fala** definem o tom, abordagem e linguagem utilizados pelo jogador ao interagir com NPCs, assim como os estilos preferenciais e tolerados por cada NPC. Eles servem como base para o sistema de **Tolerância de Personalidade**, afetando diretamente as chances de sucesso de uma fala, bem como sua influência no relacionamento com o NPC.
    1. **[Estrutura de Diálogo](estruturaDeDialogo.md)** - Definições de como criar, organizar e documentar os fluxos de diálogo.
1. **Modelos de Fichas** - Sessão com os modelos de fichas para criação e controle de diversos elementos do Jogo.
  1. **[Modelo de Ficha de NPC](modeloDeFichaDeNpc.md)** - Modelo de ficha para NPC define as informações que precisam ser definidas para cada NPC para um boa imersão de personalização do NPC.
  1. **[Modelo de Ficha de Personalidade](modeloDeFichaDePersonalidade.md)** - Modelo de ficha utilizada para guiar a criação de personalidades do jogo.
  1. **[Modelo de Ficha de Diálogo](modeloDeFichaDeDialogo.md)** - Modelo de ficha utilizada para guiar a criação de um diálogo.
  1. **[Modelo de Ficha de Tipo de Arma](modeloDeFichaDeTipoDeArma.md)** - Modelo de ficha utilizada para gruiar a crição dos tipos de armas do jogo.
  1. **[Modelo de Ficha de Itens de Mão](modeloDeFichaDeItensDeMao.md)** - Modelo de ficha utilizada como base na criação de qualquer uma das fichas a seguir dos itens de mão.
    1. **[Modelo de Ficha de Armas](modeloDeFichaDeArmas.md)** - Modelo de ficha utilizada para guiar a criação do Item de Mão do Subtipo 'arma' para o jogo.
    1. **[Modelo de Ficha de Escudos](modeloDeFichaDeEscudos.md)** - Modelo de ficha utilizada para guiar a criação do Item de Mão do Subtipo 'escudo' para o jogo.
  1. **Modelos de Fichas de Lugares**
    1. **[Modelo de Ficha de Zonas](modeloDeFichaDeZonas.md)** - Modelo de ficha utilizada para gruiar a criação de uma Zona.
    1. **[Modelo de Ficha de Ambientes](modeloDeFichaDeAmbientes.md)** - Modelo de ficha utilizada para gruiar a criação de um Ambiente.
<hr>
1. **[Ciclo de Batalha](cicloDeBatalha.md)** - Aqui é documentado e explicado os passos e funcionamento das batalhas do jogo.
1. **[Progressão do Jogo](progressaoDoJogo.md)** - Métricas e regras sobre como o jogo progride, o jogador evolui, etc..

# Story Telling Mystery Realms
O mundo de **Mystery Realms** inclui criaturas mágicas, feitiçaria, alquimia, forças elementais e tecnologias esquecidas fazem parte da realidade. Embora tais forças existam, não é um mundo mágico e de ilusões. Um ambiente parecido como o criado nos mundo de D&D.

1. **Modelos de Documentação:**
  1. **![Fichas de Criação](fichasDeCriacao.md)** - Modelos de Fichas de Criação a serem seguidos para criar "tudo" em Mystery Realms. Ex: Ficha de NPCs, Lugares, Personalidades, Itens, etc..
  1. : *As fichas de criação estão sendo migradas para a estrutura de fichas modelo do PlayTale, e saindo do Mystery Realms.*
1. **Controle e Definições do Jogo**
  1. **[Marcadores do Jogo](marcadoresDoJogo.md)** - Marcadores utilizados entro do jogo de Mystery Realms.
1. **Criação do Mundo Mystery Realms:**
  1. **[Raças](racas.md)** - Definições das Raçcas e seus atributos.
  1. **[Classes](classes.md)** - Classes dos aventureiros.
  1. **Lugares** - Lugares é a expressão utilizada para todo e qualquer tipo de lugar físico do jogo. No entando cada lugar representa um escopo diferente de agrupamento e regras.
    1. **[Thámyros](thamyros.md)** - Primeiro Mundo (Realm) do Jogo Definição do plano / planeta de localização.
      1. **[Terras Centrais](terrasCentrais.md)** - Continente Central de Thámyros
        1. **[Reino de Althar](reinoDeAlthar.md)** - Nação conquistada e regida pela linhagem de Althar.
          1. **[Floresta de Arvalem](florestaDeArvalem.md)** - Região de Floresta do Reino de Althar.
            1. **[Langur](langur.md)** - Uma das cidades mais icônicas das Terras Centrais, dividida em três anéis principais que representam conhecimento, poder e comércio.
              1. **[Taverna Asa Quebrada](tavernaAsaQuebrada.md)** - Estabelecimento discreto e denso em atmosfera, ponto de encontros sigilosos e vigilância de seu misterioso proprietário.
                1. **[Salão da Asa Quebrada](salaoDaAsaQuebrada.md)** - Ambiente principal da taverna, usado para socialização tensa, vigilância e interações discretas entre NPCs de múltiplos interesses.
              1. **[Biblioteca Varnak](bibliotecaVarnak.md)** - Torre arcana viva onde o conhecimento se molda ao visitante. Contém seções mutáveis e guardiões mágicos.
                1. **[Sala de Recepção](salaDeRecepcao.md)** - Área de entrada onde [Lirianne Morth](lirianneMorth.md) avalia as intenções dos visitantes e decide seu acesso às áreas superiores da biblioteca.
              1. **[Forja dos Ecos](forjaDosEcos.md)** - Forja ancestral onde itens são imbuídos com identidade, operada por [Thramur](thramurAnkuhl.md) e sua aprendiz rúnica.
                1. **[Sala de Fundição](salaDeFundicao.md)** - Centro produtivo da forja, onde itens mágicos são criados, pedidos são aceitos e as propriedades rúnicas são manipuladas.
              1. **[Arsenal Três Lâminas](arsenalTresLaminas.md)** - Loja especializada em itens recuperados de ruínas e campos de batalha, respeitada por sua neutralidade comercial.
                1. **[Salão da Loja](salaoDaLoja.md)** - Área de exibição e negociação, com armas e armaduras à mostra, onde [Silma](silmaVerridan.md) e [Vareth](varethDolmar.md) conduzem suas atividades.
              1. **[Praça das Vozes](pracaDasVozes.md)** - Coração público e comercial de Langur, repleto de vozes, comércio, rumores e manifestações diversas.
                1. **[Círculo dos Três Ecos](circuloDosTresEcos.md)** - Fonte sagrada ao centro da praça, marco simbólico da união entre povo, magia e força. Ponto de partida do jogo.
              1. **[Casa de Custódia](casaDeCustodia.md)** - Instituição bancária e mágica onde são guardados itens, contratos e segredos dos poderosos.
                1. **[Salão de Atendimento](salaoDeAtendimento.md)** - Área formal e vigiada onde ocorrem interações bancárias e decisões contratuais monitoradas magicamente.
              1. **[Mercado da Pedra Branca](mercadoDaPedraBranca.md)** - Principal centro de suprimentos e artigos básicos de Langur, sempre ativo e cercado de boatos e ofertas.
  1. **[Conexões de Mystery Realms](conexoesDeMysteryRealms.md)** - Mapa de navegação do Mystery Realms (conexões das Zonas e Ambientes) .
  1. **Personagens** - Ficha de Personagens (NPCs, Personalidades e outros) existentes no jogo.
    1. **NPCs** - Lista de NPCs do Jogo.
      1. **[Dargan Harth](darganHarth.md)** - é um ex-soldado veterano do exército de [Langur](langur.md), que perdeu a perna direita ao conter sozinho um ataque de bestas da névoa. Atualmente é o dono da [Taverna Asa Quebrada](langurTavernaAsaQuebrada.md), reconhecida como um refúgio seguro para forasteiros e negócios discretos​.
      1. **[Elvarin do Silêncio](elvarinDoSilencio.md)** - é um sábio andarilho, membro da [Ordem dos Caminhantes Silenciosos](faccoesOrdemDosCaminhantesSilenciosos.md), que atua como guia filosófico e conselheiro na [Praça das Vozes](langurPracaDasVozes.md). Com fala metafórica e olhar contemplativo, representa uma âncora narrativa para o jogador logo no início da jornada em [Mystery Realms](roteiroInicial.md).
      1. **[Garrun Ferres](garrunFerres.md)** - é um ex-batedor de caravanas que perdeu parte da perna em uma emboscada e agora é dono do [Estábulo Ferradura Partida](langurEstabuloFerraduraPartida.md). É referência em montarias e conhece como ninguém as rotas e os perigos das estradas que cercam [Langur](langur.md)​.
      1. **[Kaela Runaveia](kaelaRunaveia.md)** - é uma jovem aprendiz de ferreiro rúnico acolhida por [Thramur Ankuhl](thramurAnkuhl.md) na [Forja dos Ecos](langurForjaDosEcos.md). Apesar de sem formação mágica formal, possui uma conexão misteriosa com runas, que parecem reconhecê-la​.
      1. **[Lirianne Morth](lirianneMorth.md)** - é a enigmática Alta Guardiã da [Biblioteca Varnak](langurBibliotecaVarnak.md), conhecida por sua disciplina inabalável, memória perfeita e habilidade em detectar mentiras. Vive reclusa na biblioteca, onde rumores indicam que nasceu e nunca saiu​.
      1. **[Madalin Crestamar](madalinCrestamar.md)** é a coordenadora do [Mercado da Pedra Branca](langurMercadoDaPedraBranca.md), uma figura respeitada e temida no comércio de Langur. Herdou o cargo do pai e controla tudo o que entra e sai do mercado com mão firme, olhos atentos e senso apurado de justiça e preço justo​.
      1. **[Oskar Brünn](oskarBrunn.md)** - é o severo tesoureiro-chefe da [Casa de Custódia](langurCasaDeCustodia.md) de Langur, respeitado por sua integridade inflexível e por ter criado o sistema de vedação rúnica mais seguro do continente. Ex-criptógrafo de guerra, atua como guardião arcano-financeiro da cidade​.
      1. **[Silma Verridan](silmaVerridan.md)** é uma ex-caçadora de artefatos que quase morreu em uma expedição e agora trabalha no [Arsenal Três Lâminas](langurArsenalTresLaminas.md) como avaliadora de relíquias, sendo temida por sua precisão, franqueza e integridade inflexível​.
      1. **[Thramur Ankuhl](thramurAnkuhl.md)** - é um mestre ferreiro anão da [Forja dos Ecos](langurForjaDosEcos.md), último herdeiro da Tradição de Dhurrak. Sobrevivente de uma tragédia rúnica, forja armas que reagem à alma do portador e despreza magos e clientes vaidosos​.
      1. **[Vareth Dolmar](varethDolmar.md)** - é um ex-mercenário que fundou o [Arsenal Três Lâminas](langurArsenalTresLaminas.md) em Langur, onde negocia armas e artefatos de zonas de guerra com lábia afiada e um olhar preciso para o valor oculto dos itens​.
    1. **Personalidades** - Personalidades é como são chamados os personagens ou nomes de pessoas que aparecem no jogo mas não são NPCs. Por exemplo, um reu antigo, um Lord que deu nome a uma cidade ou ponte, etc., alguma personalidade que é citada ou tem história no jogo, mas não é possível interagir  no jogo.
      1. **[Archom Varnak](archomVarnak.md)** - foi um lendário arcanista e fundador da [Biblioteca Varnak](langurBibliotecaVarnak.md), desaparecido há séculos durante experimentos arcanos. Muitos acreditam que sua consciência permanece fundida à torre, influenciando os encantamentos vivos do local​.
  1. **[Facções](faccoes.md)** - Detalhes sobre as Facções (Grupos, Guildas, Odens, Tributos, Seitas, etc.) existentes no jogo.
1. **Itens de Mão**
  1. **[Armas](armas.md)** - Definições das Armas do jogo. Desde especificações de tipos e atributos gerais até a ficha individual de cada arma.
    1. **Adagas** - Lista de Armas do tipo Adagas criadas para o jogo.
      1. **[Adaga Comum](adagaComum.md)** - Uma lâmina simples, afiada e discreta, típica de aventureiros e ladrões.
  1. **Escudos**
    1. **[Escudo de Carvalho](escudoDeCarvalho.md)**
1. **Roteiros de Mystery Realms** - Registro dos Roteiros de Mystery Reals.
  1. **[Modelo de Criação do Roteiro](modeloDeCriacaoDoRoteiro.md)** - Define as diretrizes do jogo para a criação do Roteiro de forma a montar um padrão visual e fotografia contante para todo o jogo.
  1. **[Roteiro Inicial](roteiroInicial.md)** - Roteiro inicial base do jogo, contando a história base do jogo, com diálogos dos NPCs e outras narrações.
1. **Diálogos Gerais** - Relação de Diálogos Gerais que o jogador pode ter com NPCs de forma geral do jogo, para explorar os locais, melhorar relacionamento, *small talk* e outros. Não são diálogos relacionados a quests.
  1. **[Diálogos: Elvarin do Silêncio](dialogosElvarinDoSilencio.md)** - Diálogos com o Elvarin do Silêncio.

# Implementação do Código
Sessão focada na documentação e definições da implementação do código fonte da Engine e do Mystery Realms.

- **[Padrão Geral do Código Fonte](padraoGeralDoCodigoFonte.md)** - Definições gerais sobre o código fonte que devem ser seguidos em todo o desenvolvimento. Leitura obrigatória antes do desenvolvimento de qualquer código.
- **[API do Servidor](apiDoServidor.md)** - Página com a documentação da implementação da API do Servidor do Jogo para acesso da interface ou integração com o jogo.

# Mecânica Geral do Jogo
- O gameplay se baseia em vencer quests, mistérios e aventuras que são descobertas ao se movimentar pelos ambientes do jogo, conversar com os personagens e descobrir pistas. Para conquistar o quest poderão haver múltiplos caminhos a serem tomados de acordo com o conjunto de habilidades do personagem. Uma mistura de RPGs de mesa com o jogo de mesa 'Interpol' e 'Scotland Yard', com dedução e estratégia.
- O jogo terá ação que será baseada em duelos em turnos (típicos de D&D), e que permitem a evolução do personagem por XP. Como por exemplo a dinâmica de duelos do jogo RPG de Console: Lord (Legend of the red Dragon).
- As áreas do jogo serão classificados como:
  - **Pacificado:** Áreas em que o jogador não é atacado de surpresa por nenhum outro player, monstro, etc.;
  - **Selvagem:** Áreas em que habitam monstros selvagens que podem atacar o jogador, ou mesmo outros NPCs como ladrões e bandidos;
  - **Hostil:** Área em que pode ser atacado ou intimidado por NPCs, Monstros e outros jogadores."
  - : Nas áres Selvagem e Hotís, o jogador poderá batalhar e ganhar XP, moedas e diversos itens.

# Mundo de **Mystery Realms**
Sessão dedicada as informações que compõe o mundo **Mystery Realms**. O ideal é que toda a informação sobre o mundo (cidade, ruínas, locais, etc.) citado em qualquer parte do jeto seja reigstrada e catalogada nesta seção para que não haja durante a evolução da história do jogo informações contraditórias.

## Criaturas e Monstros

### Introdução

As criaturas e monstros de Mystery Realms compõem a vida selvagem, os perigos mágicos e os inimigos ocultos que habitam o continente. Elas podem ser encontradas em áreas selvagens, ruínas, cidades corrompidas, mas também podem ter função narrativa, como guardiões ou provas vivas de antigas magias.

Nem todas são hostis. Algumas podem ser neutras, fugir ao ver o jogador ou até mesmo oferecer interação com certas habilidades ou níveis de conhecimento. Outras são entidades únicas — chefes, protetores, ou mesmo criaturas raríssimas ligadas a relíquias e quests específicas.

A criação e o equilíbrio de criaturas seguem uma lógica padronizada, explicada a seguir.

### Escalonamento de Atributos

Para manter o desafio dinâmico, as criaturas possuem atributos que escalam com seu nível, garantindo que batalhas se mantenham equilibradas independentemente da progressão do jogador.

A fórmula para calcular qualquer atributo de uma criatura é:

<center><math>A_{\text{final}} = \left\lfloor A_{\text{base}} \times (1 + N \times F) \right\rfloor</math></center>

**Onde:**

<math>A_{\text{final}}</math>: Atributo final da criatura

<math>A_{\text{base}}</math>: Valor base do atributo da criatura (definido por tipo)

<math>N</math>: Nível da criatura

<math>F</math>: Fator de crescimento (**F = 0,2**)

> **Nota:** Com esse fator, o atributo da criatura dobra a cada 5 níveis.

#### Nível e Dificuldade

As criaturas podem variar seu nível dependendo do local em que são encontradas:
- **Áreas fáceis:** até 2 níveis abaixo do jogador
- **Áreas comuns:** mesmo nível do jogador
- **Áreas perigosas:** até 3 níveis acima do jogador
- **Zonas de elite:** 4 ou mais níveis acima, com chance de habilidades especiais, efeitos mágicos e loot raro

### Tipos de Criaturas

Cada tipo de criatura possui um bloco fixo de atributos base e uma ficha própria que define seus comportamentos, resistências, vulnerabilidades e áreas típicas de ocorrência.

#### Fera Selvagem

**Descrição:** Criaturas comuns da fauna local, geralmente irracionais, mas perigosas. Inclui lobos, javalis, felinos e bestas nativas da região. Costumam agir por instinto, caçar em grupos ou emboscar viajantes.

**Atributos Base:**

Força: 4

Destreza: 3

Constituição: 4

Intelecto: 1

Percepção: 3

Carisma: 1

Vontade: 2

**Habilidades Secundárias:**

Furtividade: +2

Sobrevivência: +1

**Comportamento:**

Hostis em áreas selvagens, mas podem evitar confronto em áreas pacificadas

Costumam atacar presas solitárias ou feridas

Fogem ao sofrerem muito dano, a menos que estejam protegendo ninhadas ou territórios

**Resistências:**

Resistência natural a venenos leves e doenças comuns

#### Aberração

Pendente

#### Morto-Vivo

Pendente

#### Humanoide Hostil

Pendente

#### Besta Mística

Pendente

#### Guardião Elemental

Pendente

#### Criatura Demoníaca

Pendente

#### Criatura Lendária

Pendente

### Criaturas Comuns

#### Tatu-Pedra Selvagem

**Nome:** Tatu-Pedra Selvagem

**Categoria:** Fera Selvagem

**Aparência:** Criatura de médio porte com corpo alongado e coberto por placas duras de aspecto rochoso, cinza-escuro, que refletem a luz de forma irregular. Possui garras largas adaptadas à escavação e um focinho curto com bigodes sensíveis. Seus olhos pequenos, âmbar, brilham levemente no escuro. Quando enrolado, torna-se praticamente indistinguível de uma pedra entre musgos ou entulhos da floresta.

**Comportamento e Estilo de Combate:** É territorial e solitário, patrulha pequenas áreas onde cava tocas e busca alimento. Costuma evitar conflitos, mas torna-se agressivo se ameaçado ou acuado. Em combate, investe com força bruta usando seu corpo enrolado, tentando atordoar ou afastar o inimigo. Se muito ferido, se enrola e resiste passivamente até o perigo passar. Costuma surgir de tocas ou pedras em emboscadas curtas.

**História ou Origem (opcional):** Originário de áreas montanhosas ao norte do continente, foi adaptando-se às florestas densas próximas a Langur. Sua carapaça mineral é resultado de alimentação contínua de substâncias subterrâneas, como sais rúnicos e pedras ricas em magia residual. Alguns alquimistas acreditam que sua origem esteja ligada à contaminação mágica de uma antiga mina.

**Habilidades Especiais:**
- **Enrolar-se (passiva):** Ao atingir 50% de vida ou menos, tem 50% de chance de se enrolar. Reduz o dano físico recebido em 75% por 2 turnos. Recarga: 3 turnos após desfazer-se.
- **Investida de Pedra (ativa):** Ataca rolando em alta velocidade. Causa dano físico moderado com chance de empurrar o inimigo para trás e atordoá-lo por 1 turno. Recarga: 3 turnos.
- **Camuflagem Mineral (passiva):** Se estiver imóvel por 1 turno, torna-se difícil de detectar em terrenos rochosos ou florestais (-30% chance de ser notado por testes de Percepção).

**Resistências e Fraquezas:**
- **Resistências:** +50% resistência a dano físico cortante, imune a veneno
- **Fraquezas:** Vulnerável a magia do tipo trovejante e ataques perfurantes

**Loot:**
- **Fragmentos de Carapaça de Pedra (comum):** Usado em crafting de armaduras ou escudos reforçados
- **Garra Calcificada (incomum):** Componente alquímico para poções de resistência
- **Coração Mineralizado (raro):** Material mágico com propriedades de camuflagem e dureza, usado em artefatos

**Modificadores de Atributo:**

{| class="wikitable"
! Atributo !! Modificador
|-
| Força || +2
|-
| Destreza || +1
|-
| Constituição || +2
|-
| Intelecto || -2
|-
| Percepção || +1
|-
| Carisma || -3
|-
| Vontade || 0
|}

**Imagens:**
<gallery mode="packed" widths="300" heights="300">
File:Tatu-Pedra Selvagem.webp|Tatu-Pedra em posição ofensiva
</gallery>

### Criaturas Especiais

Pendente

### Criação e Geração de Criaturas no Mapa

Pendente



## Outras Informações e Sabedorias

Nesta sessão registramos informações e biografias sobre outras informações menores do jogo que não entram nos tópicos anterioes.

# Mecânica do Jogo

Sessão destinada a documentação sobreo o funcinoamento do jogo: tabelas, cálculos, regras, etc..

## Sistema de Encontro e Duelo: Etapas Lógicas
### 1. Entrada em Área Selvagem
Verifica-se o tipo de área: Selvagem.

Define-se a probabilidade de encontro com criatura hostil. Exemplo:

30% a 50% por movimento/passo relevante (definível por região).

Pode ser ajustada por hora do dia, clima, rotas seguras, etc.

### 2. Teste de Encontro
Rola-se um teste de encontro (ex: 1d100 < chance) para ver se um combate ocorre.

Se SIM → segue para geração de criatura.

### 3. Geração da Criatura
Sorteia uma criatura compatível com a área (ex: Tatu-Pedra Selvagem, no caso da Floresta de Langur).

Define-se o nível da criatura com base em:

Nível do jogador.

Dificuldade da área (ex: fácil: -2 a +0 níveis; normal: -1 a +2; difícil: 0 a +3).

Sorte pode influenciar para gerar um "evento raro" (ex: criatura mais forte).

### 4. Teste de Percepção Mútua (Iniciativa Narrativa)

Este teste define **como o encontro começa**: quem percebe quem primeiro, se há chance de **evitar o combate**, iniciar com **vantagem tática**, ou ser **surpreendido**.

Passo 1: Verificar Condição do Jogador

Antes de qualquer rolagem, o sistema deve verificar se o jogador está:

- Em **modo furtivo** (escondido, se movendo em silêncio).
- Em **modo aberto** (andando normalmente, correndo, carregando tocha, etc).

Essas condições afetam as rolagens seguintes (com bônus ou penalidades).

Passo 2: Jogador realiza rolagem de Furtividade (se aplicável)

Se estiver tentando evitar ser notado:

*Furtividade  Destreza + bônus racial/class + modificadores + 1d20*

Penalidades possíveis:
- Armadura barulhenta
- Excesso de carga
- Condições como fome, sede ou fadiga

Se o jogador não estiver tentando se esconder, esse passo é ignorado.

Passo 3: Criatura realiza rolagem de Percepção

*Percepção  Atributo base da criatura + modificador da categoria + 1d20*

Pode receber bônus naturais (olfato aguçado, visão noturna, etc).
Se superar a furtividade do jogador, a criatura **detecta sua presença**.

Passo 4: Jogador realiza rolagem de Percepção

*Percepção  Atributo + bônus racial/class + modificadores + 1d20*

Permite detectar a criatura antes de ser surpreendido.
Penalidades por ambiente (escuridão, neblina, ruído externo, etc).

**Passo 5:** Comparação dos Resultados


{| class"wikitable"
! Situação !! Resultado
|-
| Jogador percebe a criatura **antes** dela perceber o jogador || Jogador pode evitar o combate, preparar ataque surpresa, fugir ou tentar se aproximar.
|-
| Ambos se percebem ao mesmo tempo (empate ou rolagens próximas) || Encontro direto. Seguir para rolagem de iniciativa.
|-
| Criatura percebe o jogador, mas o jogador **não percebe a criatura** || Criatura realiza **ataque surpresa** (ganha um turno gratuito).
|-
| Nenhum dos dois percebe o outro (raro) || Nada acontece. Ambos seguem sem se notar.
|}

**Passo 6:** Decisão Narrativa

Com base no resultado da comparação:

- Jogador pode escolher entre **lutar, fugir ou observar** (caso tenha detectado a criatura).
- Se a criatura tiver comportamento **agressivo, predatório ou territorial**, ela atacará imediatamente após detectar o jogador.

### 5. Rolagem de Iniciativa
Baseado geralmente na Destreza + rolagem (ex: 1d20 + modificador).

Define a ordem dos turnos de ataque e ação.

### 6. Combate
Turnos alternados com as seguintes etapas por personagem:

Verificação de condições (efeitos ativos, venenos, buffs).

Escolha de ação (ataque, defender, usar item, fugir, habilidade).

Teste de ataque e defesa:

Ataque físico → baseado em Força/Destreza + arma.

Defesa → pode ser passiva (Constituição ou armadura).

Magias usam outros atributos e regras.

Cálculo de dano.

Aplicação de efeitos (envenenamento, perda de moral, etc.)

Verificação de morte/retirada.

### 7. Fim do Combate
Define-se:

XP recebido com base no nível da criatura.

Loot obtido (conforme ficha da criatura).

Condições persistentes (ferimentos, efeitos negativos).

Atualização de moral, sede, energia, etc.

Observações Importantes:
Jogador pode usar itens ou habilidades específicas que influenciam cada uma dessas fases.

Em áreas mais complexas, criaturas podem agir em grupo, ou emboscar o jogador.
