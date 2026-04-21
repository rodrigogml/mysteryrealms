# Página principal

**Mystery Realms** é um jogo de RPG focado em investigação, diálogos e batalhas, ambientado em um mundo fictício e místico de época medieval.

O projeto é dividido em duas partes principais:

* **PlayTale Engine** – A engine do sistema. Aqui estão concentradas as regras, cálculos e estruturas que regem o funcionamento de qualquer jogo de RPG. Esta engine é reutilizável e modular, servindo tanto para o mundo de *Mystery Realms* quanto para outros jogos. [PlayTale Engine](./PlayTale_Engine.md)

* **Mystery Realms** – A aplicação da engine no universo específico deste jogo. Aqui definimos o cenário, personagens, facções, regiões e enredos, utilizando todas as mecânicas fornecidas pela PlayTale Engine.

## 1 PlayTale Engine

Aqui tratamos do funcionamento do jogo, como as partes do jogo se encaixam e interagem entre si. Fórmulas e regras são registradas aqui. Em outras palavras as engrenagens do jogo, que seriam comuns, por exemplo, para o funcionamento de outro universo e jogo.

> **NOTE:** Itens marcados com '!' precisam de uma revisão desde que o PlayTale foi destacado do Mystery Realms.

## 2 Definições Gerais

* **Definições Gerais** - Sessão dedicada as definições gerais da **Mecânica do PlayTale**, que explica os cálculos e funcionamento geral do sistema, independente do tipo de jogo criado.
:* [Estrutura Temporal](./Estrutura_Temporal.md) - Definição de controle do tempo, estações do ano, calendário lunar, eventos sazonais, e muito mais.
:* [Estrutura de Localização](./Estrutura_de_Localizacao.md) - A estrutura de localização define como os "lugares" do jogo são organizados e suas funções estratégicas.
::* [Mapa e Movimentação](./Estrutura_de_Localizacao.md#mapa-e-movimenta%C3%A7%C3%A3o) - Conceitos sobre o mapa e especificações sobre a movimentação do jogador.
:* [Economia](./PlayTale_Engine.md#economia) - Definição e controle do sistema monetário do jogo.
:* [Raças e Classes](./PlayTale_Engine.md#ra%C3%A7as-e-classes) - Conceitos sobre definições de Raças e Classes do mundo.
:* [Danos e Aflições](./PlayTale_Engine.md#danos-e-afli%C3%A7%C3%B5es) - Definição e explicação dos tipos de dano e dos tipos de aflição que suportados pela engine.
:* [Defesa e Bloqueio](./PlayTale_Engine.md#defesa-e-bloqueio) - Explicações da diferença entre 'Defesa do Ataque' e 'Bloqueio do Dano'.
:* [Tipos de Resistências](./PlayTale_Engine.md#tipos-de-resist%C3%AAncias) - Explicações sobre os diferentes tipos de Resistências (Física, Elementais, Mágicas, Aflições, Mentais e Espirituais).
:* [Moral](./PlayTale_Engine.md#moral) - Definição sobre o estado mental do jogador.
:* [Acampamentos](./PlayTale_Engine.md#acampamentos) - Definições e conceitos sobre o funcionamento dos acampamentos.
:* [Itens](./Itens_de_Mao.md) - Chamamos de Itens todos os objetos do jogo, como a classe principal da hierarquia. São Itens: Elmos, Escudos, Botas, Comida, Ferramentas, Armas, Objetos, Runas e muito mais.
::* [Itens de Mão](./Itens_de_Mao.md) - Itens de mão são todos os objetos que podem ser empunhados, carregados ou utilizados diretamente pelas mãos do personagem, seja em combate, exploração, interação social ou mágica.
:::* [Tipos de Armas](./Itens_de_Mao.md#tipos-de-armas) - Para os itens de mão da categoria 'arma', permite que o jogo agrupe as armas em tipos, como Arcos, Bestas, Espadas, Maças, etc.

## 3 Controle do Jogador

* [Modificadores](./PlayTale_Engine.md#modificadores) - Explicação sobre os modificadores que podem ser aplicados ao jogo para alterar as condições padrão do jogo de forma temporária.
* [Fadiga, Fome e Sede](./PlayTale_Engine.md#fadiga-fome-e-sede) - Conceito e funcionamento sobre a Fadiga, Fome e Sede.
* [Recuperação](./PlayTale_Engine.md#recupera%C3%A7%C3%A3o) - Explicação dos diferentes tipos de descansos e recuperação possíveis do personagem.
* [Morte e Desmaios](./PlayTale_Engine.md#morte-e-desmaios) - Definições sobre morte e desmaio do jogador.
* [Ações do Jogador](./PlayTale_Engine.md#a%C3%A7%C3%B5es-do-jogador) - Definição dos detalhes das ações que podem ser tomadas pelo jogador.

## 4 Controle do Progresso do Jogo

* [Sistema de Marcadores](./PlayTale_Engine.md#sistema-de-marcadores) - Explicação do que é e como funciona o sistema de marcadores/marcos/milestones no controle do progresso do jogo.
* [Testes de Jogo](./PlayTale_Engine.md#testes-de-jogo) - Documentação dos diferentes tipos de testes realizados durante o jogo.
* [Ficha do Jogador](./PlayTale_Engine.md#ficha-do-jogador) - Nesta página é abordada a ficha do jogador, explicados seus atributos, sua matemática, e todo o controle das informações do jogador.

## 5 Fluxo Narrativo

* [Diário do Jogador](./PlayTale_Engine.md#di%C3%A1rio-do-jogador) - Fundamentos e sugestão do Diário do Jogador.
* [Interação com NPCs](./PlayTale_Engine.md#intera%C3%A7%C3%A3o-com-npcs) - Bloco com as definições da mecânica das interações e relacionamento entre o jogador e os NPCs.
::* [Estilos de Fala e Valoração](./PlayTale_Engine.md#estilos-de-fala-e-valora%C3%A7%C3%A3o) - Os Estilos de Fala definem o tom, abordagem e linguagem utilizados pelo jogador ao interagir com NPCs.
::* [Estrutura de Diálogo](./PlayTale_Engine.md#estrutura-de-di%C3%A1logo) - Definições de como criar, organizar e documentar os fluxos de diálogo.

## 6 Modelos de Fichas

* [Modelo de Ficha de NPC](./PlayTale_Engine.md#modelo-de-ficha-de-npc)
* [Modelo de Ficha de Personalidade](./PlayTale_Engine.md#modelo-de-ficha-de-personalidade)
* [Modelo de Ficha de Diálogo](./PlayTale_Engine.md#modelo-de-ficha-de-di%C3%A1logo)
* [Modelo de Ficha de Tipo de Arma](./Itens_de_Mao.md#modelo-de-ficha-de-tipo-de-arma)
* [Modelo de Ficha de Itens de Mão](./Itens_de_Mao.md#modelo-de-ficha-de-itens-de-m%C3%A3o)
::* [Modelo de Ficha de Armas](./Itens_de_Mao.md#modelo-de-ficha-de-armas)
::* [Modelo de Ficha de Escudos](./Itens_de_Mao.md#modelo-de-ficha-de-escudos)
* [Modelos de Fichas de Lugares](./PlayTale_Engine.md#modelos-de-fichas-de-lugares)
::* [Modelo de Ficha de Zonas](./PlayTale_Engine.md#modelo-de-ficha-de-zonas)
::* [Modelo de Ficha de Ambientes](./PlayTale_Engine.md#modelo-de-ficha-de-ambientes)

## Ciclo de Batalha

[Ciclo de Batalha](./PlayTale_Engine.md#ciclo-de-batalha) - Aqui é documentado e explicado os passos e funcionamento das batalhas do jogo.

## Progressão do Jogo

[Progressão do Jogo](./PlayTale_Engine.md#progress%C3%A3o-do-jogo) - Métricas e regras sobre como o jogo progride, o jogador evolui, etc.

## Story Telling Mystery Realms

O mundo de **Mystery Realms** inclui criaturas mágicas, feitiçaria, alquimia, forças elementais e tecnologias esquecidas fazem parte da realidade. Embora tais forças existam, não é um mundo mágico e de ilusões. Um ambiente parecido como o criado nos mundo de D&D.

## Modelos de Documentação

* [Fichas de Criação](./PlayTale_Engine.md#fichas-de-cria%C3%A7%C3%A3o) - Modelos de Fichas de Criação a serem seguidos para criar "tudo" em Mystery Realms.

## Controle e Definições do Jogo