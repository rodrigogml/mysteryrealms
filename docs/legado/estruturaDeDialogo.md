# Introdução Geral

O sistema de **Diálogos** da engine **PlayTale** é uma ferramenta central para criar interações narrativas ricas entre o jogador e personagens não-jogadores (NPCs). Ele permite a construção de conversas ramificadas, influenciadas por atributos, decisões anteriores, condições do mundo e sistemas internos do jogo.

Mais do que apenas exibir falas, os diálogos desempenham um papel fundamental na mecânica do jogo:
- Disparam eventos e atualizam marcos que o jogador alcançou e que alteram o estado do mundo.
- Ativam gatilhos narrativos, desbloqueiam áreas ou avanços de missão.
- Revelam informações que alimentam sistemas como o [Diário do Jogador](diarioDoJogador.md).
- Realizam testes baseados em atributos e permitem múltiplos caminhos de resolução.

## Funções do Sistema de Diálogo

O sistema é capaz de atender a diferentes finalidades narrativas e funcionais:
- **Diálogos Narrativos**: para contextualização, ambientação e aprofundamento de personagens.
- **Diálogos Investigativos**: para coleta de pistas, verificação de contradições e confrontos.
- **Diálogos de Progressão**: para desbloquear missões, entregar itens ou registrar marcos da história.
- **Diálogos Condicionais**: com respostas e opções variáveis, baseadas em atributos, inventário ou estado do jogo.
- **Diálogos Estratégicos**: com possibilidades de persuasão, intimidação, barganha, entre outros usos de habilidades sociais.

## Fluxo de Criação e Integração

O fluxo de trabalho do sistema de diálogos envolve geralmente duas funções principais:
- **Autores de Diálogo**: responsáveis pela escrita das falas, estrutura narrativa e organização lógica dos blocos.
- **Integradores Técnicos**: encarregados de aplicar marcadores, vincular testes, configurar variáveis e assegurar que o diálogo esteja conectado ao restante dos sistemas do jogo.

Essa divisão permite que equipes criativas e técnicas colaborem com clareza, garantindo coesão narrativa e funcionalidade mecânica.



# Estrutura de Diálogos

A estrutura de **Diálogo** da engine **PlayTale** define interações conversacionais entre o jogador e personagens não-jogadores (NPCs), com ramificações narrativas, testes de atributo e efeitos mecânicos. Esta estrutura foi adaptada para utilizar um modelo visual e legível baseado em templates MediaWiki.

Essa estrutura permite uma leitura clara, aninhamento visual de falas, múltiplas variações de estilo e testes, e é especialmente útil durante o design narrativo ou análise por ferramentas de IA.


## O que é uma Fala?
Uma **Fala** representa uma linha de diálogo iniciada pelo jogador e seguida de uma resposta do NPC. Ela pode ter consequências, ramificações e efeitos no mundo de jogo. Falas podem ser simples ou conter variações com estilos diferentes, testes de atributo, comandos e falas subsequentes.


## O que é um Diálogo?
Um **Diálogo** é o conjunto completo de falas encadeadas que ocorrem em uma situação específica com um NPC. Cada **Diálogo** deve ser documentado como um tópico próprio de nível 1 (usando "="), com um nome que represente o tema tratado. Exemplo:
<pre>= Diálogo: Algo Incomum na Floresta =</pre>

Esse bloco conterá todas as falas, suas variações e ramificações.


## Estilos de Fala

Os **estilos de fala** representam a abordagem escolhida pelo jogador ao interagir com um NPC — podendo ser direta, persuasiva, agressiva, mentirosa, entre outras. Eles influenciam o tom da fala, o comportamento do NPC, e determinam o tipo de teste aplicado (quando houver).

Estilos não são apenas variações cosméticas, mas afetam a mecânica do diálogo, desbloqueiam respostas diferentes, e podem alterar o relacionamento com personagens.

Para uma descrição completa dos estilos disponíveis, suas implicações mecânicas e exemplos práticos, consulte a página [Estilos de Fala e Valoração](estilosDeFalaEValoracao.md).

## Testes de Fala
Algumas falas exigem testes de atributos para definir se o jogador será bem-sucedido. Os testes são definidos por:
- **Atributo testado:** (ex: persuasão, intuição, enganar, percepção, carisma)
- **Dificuldade:** valor-alvo a ser igualado ou superado

O estilo e o teste são conceitos diferentes:
- O **estilo** define a forma da abordagem.
- O **teste** define se será necessário um sucesso num teste para desbloquear a resposta positiva.


Exemplo: uma fala **persuasiva** pode conter um teste de **persuasão**, enquanto uma fala **mentirosa** pode conter um teste de **enganar**.


## Comandos da Fala

Os **comandos da fala** são efeitos executados após a resposta do NPC. Eles permitem que o diálogo influencie o mundo de jogo, registre descobertas, altere relacionamentos ou desencadeie eventos narrativos.


Esses comandos devem ser escritos entre colchetes com seus parâmetros separados por ';'. Para indicar múltiplos comandos eles são separados por vírgulas. Exemplo:
<pre>
[Nome do Comando; Argumento 1; Argumento 2; ...], [Nome do Comando; Argumento 1; Argumento 2; ...], ...
</pre>

Vários comandos podem ser aplicados a uma única resposta, separados por vírgulas:

<pre>
[Inserir Marcador; DESCOBRIU_SOBRE_ORB], [Mudar Relacionamento; 1]
</pre>

Abaixo estão os comandos atualmente disponíveis e sua função:

- **[Inserir Marco; NOME_DO_MARCO]**
  Insere um marco no sistema de jogo para registrar que determinada informação foi adquirida ou evento ocorreu.
  Exemplo:
  <pre>[Inserir Marco; VIU_MAPA_SECRETO]</pre>

- **[Adicionar Entrada ao Diário; TEXTO_DA_ENTRADA]**
  Adiciona uma nova entrada ao [Diário do Jogador](diarioDoJogador.md), visível para consulta futura.
  Exemplo:
  <pre>[Adicionar Entrada ao Diário; O velho mencionou um local escondido sob as ruínas do norte.]</pre>

- **[Entregar Item; ID_DO_ITEM]**
  O NPC entrega um item ao jogador, geralmente como consequência de uma fala bem-sucedida ou como parte de uma negociação.
  Exemplo:
  <pre>[Entregar Item; CHAVE_ANTIGA]</pre>

- **[Mudar Relacionamento; VALOR]**
  Modifica o nível de relacionamento entre o jogador e o NPC.
  * Valores positivos (ex: `1`, `2`) melhoram a relação.
  * Valores negativos (ex: `-1`, `-3`) deterioram a relação.
  * Um valor de `5` representa confiança máxima; `-5` pode representar hostilidade.
  Exemplo:
  <pre>[Mudar Relacionamento; -2]</pre>

- **[Teleportar jogador; ID_DO_DESTINO]**
  Move o jogador instantaneamente para outro local. Pode representar uma expulsão, um transporte ou um efeito mágico.
  Exemplo:
  <pre>[Teleportar jogador; TEMPLO_ANTIGO_ENTRADA]</pre>

Esses comandos são processados automaticamente após a fala correspondente. Em falas com testes, os comandos são separados para os casos de **sucesso** e **falha**, permitindo efeitos distintos dependendo do resultado.



# Criação do Diálogo
Para criar um diálogo:
- comece completando o [Modelo de Ficha de Diálogo](modeloDeFichaDeDialogo.md), ela dará os primeiros passos de como o diálogo será e objetivos que precisam ser atingidos;
- crie a condição
- após a ficha criada, defina uma ou mais trilhas que atingem tantos os objetivos principais, quando os objetivos secundários, com todos os testes necessários;
- crie trilhas adicionais para melhor imersão, com mais desafios, testes e modificações de relacionamento com o NPC, falas com valores, etc..
- adicione trilhas em que os objetivos secundários não são atingidos.
- valide se todas as trilhas tem o objetivo principal completo, se os caminhos de falas são coerentes e se não há uma grande variação dentro de cada trilha do humor.


# Documentação do Diálogo

Os diálogos são documentados utilizando a predefinição <code><nowiki>`{{Fala}}`</nowiki></code>. Cada bloco representa uma fala do jogador, uma resposta do NPC e as possíveis consequências lógicas e narrativas dessa interação.

A predefinição é utilizada tanto para falas simples quanto para falas com testes de habilidade, variações, estilos de fala, valoração moral e desdobramentos. Todas as estruturas devem seguir regras rígidas de indentação e consistência.

## Estrutura Geral do Bloco

Existem dois tipos principais de fala:
- **Falas Simples:** sem teste de atributo
- **Falas com Teste:** exigem verificação baseada em um atributo do jogador

### Campos Comuns
Todos os blocos de fala aceitam os seguintes campos:
- <code>id</code>: Identificador único da fala. Deve ser único dentro do mesmo diálogo.
- <code>fala</code>: A fala do jogador.
- <code>resposta</code>: A resposta do NPC (apenas em falas simples).
- <code>comandos</code>: Lista de comandos a serem executados após a fala (opcional). Ex: <code>[Inserir Marcador; SABE_ALGO]</code>
- <code>subfalas</code>: Lista de falas-filhas (opcional), para ramificação narrativa.
<code>estilo</code>: Define o tom ou intenção emocional da fala. Ver lista completa na [Tabela de Estilos](estilosDeFalaEValoracaoTabelaDeEstilos.md). Este campo é opcional e, quando omitido, indica uma fala com tom **neutro**. O atributo <code>estilo</code> deve ser usado quando houver variações de uma mesma fala com tons distintos — por exemplo, uma versão empática, outra irônica e uma neutra da mesma pergunta. Nesses casos, a opção neutra (sem o campo <code>estilo</code>) deve sempre estar presente como alternativa. Exceções são permitidas: em trilhas narrativas específicas onde a ausência do neutro intensifica o drama ou a tensão da escolha, podem ser apresentadas apenas opções com estilo definido.
- <code>valor</code>: Valor moral representado pela fala. Ver tabela em [Tabela de Valoração](estilosDeFalaEValoracaoTabelaDeValoracao.md). Opcional. O valor só deve ser definido uma vez por trilha do diálogo, e somente quando a frase tem um sentido que demostra um alto valor de caráter. O valor deve ser compatível com as definições da Tabela de Valoração.

### Campos para Falas com Teste
Quando uma fala inclui <code>teste</code>, os campos abaixo substituem <code>resposta</code>:

- <code>teste</code>: Nome da habilidade testada (ex: intuição, persuasão).
- <code>dificuldade</code>: Valor mínimo necessário para o sucesso.
- <code>sucesso_resposta</code>: Resposta em caso de sucesso.
- <code>falha_resposta</code>: Resposta em caso de falha.
- <code>sucesso_comandos</code>, <code>falha_comandos</code>: Comandos disparados conforme o resultado (opcional).
- <code>sucesso_subfalas</code>, <code>falha_subfalas</code>: Ramificações específicas conforme o resultado (opcional).

> **Nota:** Nunca usar o campo <code>resposta</code> em falas com teste. Também não usar <code>comandos</code> nesse caso.

## Regras de Indentação
A formatação segue regras rígidas:
- Cada novo nível de fala deve ter **1 espaço adicional** de indentação.
- Aberturas <code>`{{</code> e fechamentos <code>}}`</code> devem estar alinhados verticalmente.
- Campos (como <code>|fala=</code>, <code>|resposta=</code>) devem ser indentados com 1 nível.
- Se o conteúdo do campo ocupar uma nova linha (ex: texto longo), deve ter mais 1 nível de indentação.

## Exemplo Completo
O exemplo abaixo cobre todas as variações:

<pre>
{{Fala
|id=1
|fala=Qual é o seu papel neste lugar?
|resposta=Sou aquele que cuida das passagens. Mas só revela o caminho quem está pronto para vê-lo.
|subfalas=
 {{Fala
 |id=2
 |fala=Então você é uma espécie de guardião?
 |resposta=Essa é uma interpretação possível. Mas há quem me veja como guia, outros como obstáculo.
 |subfalas=
  {{Fala
  |id=3
  |fala=E qual você prefere ser?
  |resposta=Prefiro ser aquele que respeita a escolha de quem passa.
  }}
  {{Fala
  |id=4
  |fala=Você não respondeu. Está escondendo algo?
  |resposta=Algumas respostas só existem quando o outro está pronto para escutá-las.
  |estilo=desrespeitoso
  }}
 }}
 {{Fala
 |id=5
 |fala=E se eu quiser passar mesmo assim?
 |teste=persuasão
 |dificuldade=4
 |sucesso_resposta=Essa é uma resposta de sucesso. Você parece decidido. Pode passar, mas leve sabedoria contigo.
 |falha_resposta=Essa é uma resposta de falha. Ainda não vejo firmeza nos seus olhos. Reflita e tente novamente.
 |sucesso_comandos=[Inserir Marcador; PASSAGEM_LIBERADA]
 |falha_comandos=[Inserir Marcador; TENTOU_PASSAGEM]
 |sucesso_subfalas=
  {{Fala
  |id=6
  |fala=Obrigado. Prometo que não vou desperdiçar isso.
  |resposta=Palavras firmes sustentam passos verdadeiros. Boa jornada.
  |valor=Honra
  }}
 |falha_subfalas=
  {{Fala
  |id=7
  |fala=Você está me testando de propósito, não está?
  |resposta=Testes são reflexos. Não são meus, são seus. Tente de novo quando souber o que busca.
  }}
  {{Fala
  |id=8
  |fala=Então vou encontrar outro caminho.
  |resposta=Caminhos existem. Mas nem todos levam aonde você precisa ir.
  |estilo=frio
  }}
 }}
}}
</pre>

## Resumo de Regras Importantes

- Nunca misture <code>resposta</code> com campos de teste (<code>sucesso_resposta</code>, etc.).
- Nunca use <code>comandos</code> em falas com teste. Use <code>sucesso_comandos</code> e <code>falha_comandos</code>.
- O campo <code>valor</code> deve representar um princípio ético claro, e só deve ser usado em falas com esse propósito.
- O campo <code>estilo</code> é opcional e só deve ser usado quando a intenção do tom for relevante.
- Todas as IDs devem ser únicas dentro do mesmo diálogo.
- Não deixe linhas em branco dentro dos blocos <code>Fala</code>.

# Condições de Ativação de Diálogo
**Diálogos** são restritos para aparecerem apenas quando certas condições forem atendidas, permitindo controle contextual e narrativo sobre o que está disponível ao jogador em determinado momento do jogo.

Essas condições são escritas no formato de expressões booleanas simples, similares à linguagem de programação, utilizando funções e operadores lógicos. O objetivo é oferecer uma forma legível, poderosa e extensível de controlar a ativação de blocos de diálogo.


## Como declarar uma condição de ativação
Uma condição deve ser colocada logo no início do bloco de diálogo, utilizando a seguinte anotação:

<pre>
# Diálogo: Guardião da Trilha
@condição: hasMilestone("OUVIU_SOBRE_TRILHA") AND !hasMilestone("TRILHA_INVESTIGADA")
</pre>

Essa condição significa que o diálogo só estará disponível se o jogador tiver o marco **OUVIU_SOBRE_TRILHA** e ainda não tiver o marco **TRILHA_INVESTIGADA**.


## Sintaxe de Condições
As expressões devem utilizar os seguintes operadores lógicos:
- **AND**: e
- **OR**: ou - no sentido de qualquer uma das condições
- **XOR**: ou exclusivo - no sentido de 'ou uma ou outra, não as duas'
- **!**: negação (NOT) - inverte o true para false e vice-versa. Ou leia-se apenas "não [condição da função]"

As funções retornam valores booleanos e podem ser combinadas livremente com os operadores acima.


## Funções Disponíveis

As funções abaixo podem ser utilizadas nas expressões booleanas para definir as condições de ativação dos diálogos. Todas retornam valores booleanos e podem ser combinadas com operadores lógicos.
<Center>
{| class="wikitable" style="width: 90%;"
! style="width: 20%;" | Função
! Descrição e Parâmetros

|-
| **hasAllMilestones** |
- **marco1, marco2, ...:** Identificadores dos marcos a serem verificados.
- **Retorna:** true se o jogador possuir **todos** os marcos listados.

|-
| **hasAnyMilestone** |
- **marco1, marco2, ...:** Identificadores dos marcos a serem verificados.
- **Retorna:** true se o jogador possuir **pelo menos um** dos marcos listados.

|-
| **hasItem**
|
- **idDoItem:** Identificador do item a ser verificado na ficha do jogador.
- **Retorna:** true se o jogador possuir o item.

|-
| **hasMilestone** |
- **idDoMarco:** Identificador do marco a ser verificado.
- **Retorna:** true se o jogador possuir o marco.

|-
| **isNPCListening**
|
- **nomeDoNPC:** Nome do NPC que deve estar presente e ouvindo o jogador. Obrigatório o nome completo do NPC, conforme consta em sua ficha.
- **Retorna:** true se o NPC estiver no ambiente e ouvindo.

|-
| **isRaining**
|
- **(sem parâmetros)**
- **Retorna:** true se o clima atual do ambiente for de chuva.

|-
| **isUnderRain**
|
- **(sem parâmetros)**
- **Retorna:** true se o jogador estiver fisicamente exposto à chuva (sem cobertura).

|}
</center>

## Exemplos de Condição
<pre>
@condição: hasMilestone("OUVIU_GRITO") && isUnderRain()
</pre>

<pre>
@condição: hasAnyMilestone("VIU_SANGUE", "ACHOU_COLAR") && isNPCListening("Caçador")
</pre>

<pre>
@condição: !hasMilestone("TESTEMUNHA_SILENCIADA") && hasItem("CHAVE_DO_PORTAO")
</pre>


## Condições Automáticas

Algumas condições já são consideradas por padrão em todos os diálogos e não devem ser escritas na condição do diálogo. São elas:
- Todo diálogo só será exibido para o jogador se ele ainda não tiver acontecido. Para isso o sistema verifica automaticamente se ele **não tem o 'Marco do Diálogo**';
- Todo diálogo só é exibido quando o jogador está próximo ou em conversa com o NPC presente na ficha do diálogo, assim não é necessário condiconar se o NPC está ouvindo ou presente.

# Exemplos
A seguir exemplos de diálogos para referência.


## Diálogo de Exemplo: Algo Incomum na Floresta

<pre>@condição: isNPCListening("Lenhador") && hasAnyMilestone("OUVIU_SOBRE_FANTASMAS", "VIU_SOMBRA_NA_FLORESTA", "LEU_LORE_ESPIRITOS")</pre>

{{Fala |id=1 |fala=Você já viu algo incomum na floresta?
 |resposta=Depende do que você chama de incomum. Essa floresta é cheia de segredos...
 |subfalas=
  {{Fala |id=2 |fala=Comento que senti algo me observando entre as árvores. |estilo=Intuição |valor=Espiritualidade |teste=Intuição |dificuldade=12
   |sucesso_resposta=Você também sentiu isso, hein? Não é só paranoia...
   |sucesso_comandos=[Inserir Marcador; SENTIU_PRESENCA_SOMBRIA], [Mudar Relacionamento; 1]
   |sucesso_subfalas=
    {{Fala |id=4 |fala=Acha que é um espírito ou uma fera?
     |resposta=Espírito, fera... ou pior. Às vezes, o que te observa não tem forma.
    }}
   |falha_resposta=Hah! Isso é o que dá dormir pouco ou comer cogumelo errado.
   |falha_comandos=[Mudar Relacionamento; -1]
   |falha_subfalas=
    {{Fala |id=5 |fala=Peço que leve isso a sério. |estilo=Persuasivo |valor=Honra |teste=Persuasão |dificuldade=14
     |sucesso_resposta=Tudo bem... talvez eu esteja negando algo que também temo.
     |sucesso_comandos=[Inserir Marcador; CONVENCEU_SOBRE_PRESENCA]
     |sucesso_subfalas=
      {{Fala |id=6 |fala=Poderia me mostrar onde sentiu isso?
       |resposta=Venha ao entardecer. Eu te mostro. Mas esteja preparado.
      }}
     |falha_resposta=Não vou perder meu tempo com delírios.
     |falha_comandos=[Mudar Relacionamento; -2]
    }}
  }}
  {{Fala |id=7 |fala=Minto dizendo que ouvi rumores semelhantes. |estilo=Mentiroso |valor=Conhecimento |teste=Enganar |dificuldade=15
   |sucesso_resposta=Você ouviu isso onde? Há um velho conto sobre o Guardião das Sombras.
   |sucesso_comandos=[Inserir Marcador; DESCOBRIU_LENDA_DA_SOMBRA], [Adicionar Entrada ao Diário; O velho mencionou um tal 'Guardião das Sombras', que habita a floresta.]
   |sucesso_subfalas=
    {{Fala |id=8 |fala=O que você sabe sobre esse guardião?
     |resposta=Dizem que protege algo... ou alguém. Mas odeia ser perturbado.
    }}
   |falha_resposta=Mentiras não caem bem por aqui, forasteiro.
   |falha_comandos=[Mudar Relacionamento; -3]
  }}
  {{Fala |id=9 |fala=Falo diretamente sobre o que vi. |estilo=Direto
   |resposta=Você é do tipo que vai direto ao ponto, hein? Gosto disso. Mas isso pode ser perigoso por aqui.
   |comandos=[Mudar Relacionamento; 1]
  }}
}}
{{Fala |id=10 |fala=E sobre os viajantes desaparecidos, você sabe de algo? |estilo=Persuasivo |valor=Compaixão |teste=Persuasão |dificuldade=13
 |sucesso_resposta=Ouvi que sumiram perto da trilha do penhasco. Mas ninguém confirma.
 |sucesso_comandos=[Inserir Marcador; DESCOBRIU_TRILHA], [Mudar Relacionamento; 1]
 |sucesso_subfalas=
  {{Fala |id=12 |fala=Você conhece bem essa trilha?
   |resposta=Conheço sim. Já andei muito por lá, mas faz tempo que evito aquela área.
   |subfalas=
    {{Fala |id=13 |fala=Pergunto com naturalidade.
     |resposta=Bem demais... mas evito ela há anos.
     |comandos=[Inserir Marcador; EVITA_TRILHA]
     |subfalas=
      {{Fala |id=14 |fala=Por quê?
       |resposta=Algo mudou por lá. O som... o cheiro... até os pássaros evitam aquele caminho.
      }}
    }}
    {{Fala |id=15 |fala=Insisto de forma persuasiva. |estilo=Persuasivo |valor=Pragmatismo |teste=Intuição |dificuldade=12
     |sucesso_resposta=Tá bom... vi pegadas lá. Grandes. Demais pra serem humanas.
     |sucesso_comandos=[Inserir Marcador; PEGADAS_MISTERIOSAS]
     |sucesso_subfalas=
      {{Fala |id=16 |fala=Você seguiu as pegadas?
       |resposta=Por duas curvas... depois desapareciam na pedra.
      }}
     |falha_resposta=Não tenho certeza... talvez tenha sido só impressão.
     |falha_comandos=[Mudar Relacionamento; -1]
    }}
  }}
 |falha_resposta=Isso não é da sua conta.
 |falha_comandos=[Mudar Relacionamento; -1]
 |falha_subfalas=
  {{Fala |id=17 |fala=Não precisa ser rude, só quero ajudar.
   |resposta=Tá certo... desculpe. É que falar disso ainda me abala.
   |subfalas=
    {{Fala |id=18 |fala=Digo de forma amistosa. |estilo=Amistoso |valor=Compaixão
     |resposta=Desculpe... é que falar disso me incomoda. Tive um amigo que sumiu por lá.
     |comandos=[Mudar Relacionamento; 1]
     |subfalas=
      {{Fala |id=19 |fala=Você pode me contar mais sobre ele?
       |resposta=Chamava-se Arel. Era caçador... e bom demais para simplesmente desaparecer.
      }}
    }}
    {{Fala |id=20 |fala=Reforço minha intenção diretamente. |estilo=Direto
     |resposta=Já disse, não vou falar disso.
     |comandos=[Mudar Relacionamento; -2]
    }}
  }}
}}
