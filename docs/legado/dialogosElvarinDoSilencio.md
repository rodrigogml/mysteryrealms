# Diálogo: Quem é você?


## Ficha
- **NPC:** Elvarin do Silêncio
- **Condição:** Nenhuma condição adicional
- **Marco do Diálogo:** DIALOGO_ELVARINDOSILENCIO_QUEMEVOCE
- **Objetivo do Diálogo:** Este diálogo tem o objetivo de apresentar o NPC Elvarin do Silêncio ao jogador. Passar de alguma forma a instrução de que ele estará sempre por alí na praça e que pode ajudar com informações e outras ajudas que o jogador precisar.
- **Metas Obrigatórias:**
:* O NPC deve se apresentar.
:* Deixar claro que estará sempre por alí para ajudar com informações sempre que o jogador se sentir perdido.
:* Devem ter o comando: [Inserir Marcador; CONHECE_ELVARINDOSILENCIO]
- **Metas Secundárias:**
:* Se a conversa fluir bem, no meio do diálogo o Elvarin comentar sobre a sua ordem, nesses caso deve adicionar o comando: [Inserir Marcador; SABESOBRE_ORDEMDOSCAMINHANTESSILENCIOSOS]
- **Outras Expectativas do Diálogo:**
:* Teste de intuição pode ser usado para confiar ou não em Elvarin.
:* Jogador que se mostrar impaciente pode receber respostas mais vagas.
- **Tons Narrativos Desejados:** Contemplativo, acolhedor
- **Estilos de Fala Esperados do Jogador:** empático, direto, respeitoso
- **Reações Esperadas do NPC:** Rejeita ironia com silêncio. Responde com mais profundidade se for tratado com respeito.
- **Ponto de Entrada do Diálogo:** Quando o jogo se inicia, o jogador acorda na praça, ao lado da fonte, totalmente desorientado sem lembrar o que houve com ele. Nesse ponto um desconhecido (Elvarin) se aproxima, e o jogador começará perguntando quem ele é.
- **Consequência Final Esperada:** Jogador passa a conhecer e reconhecer Elvarin como figura de orientação. Desbloqueia interações futuras com dicas e conselhos.
- **Variações de Personalidade do Jogador Esperadas:** Estilos impacientes ou grosseiros geram respostas evasivas ou desconexas.

## Diálogo
<pre>@condição: -</pre>
{{Fala
|id=1
|fala=Quem é você?
|resposta=Alguns me chamam de eco... outros, de guia. Mas aqui, sou Elvarin. Um velho que escuta mais do que fala.
|comandos=[Inserir Marcador; CONHECE_ELVARINDOSILENCIO]
|subfalas=
  {{Fala
  |id=21
  |fala=Onde estou? Eu te conheço?
  |resposta=Langur. A Praça das Vozes. Ainda não. Mas pode me conhecer, se quiser. Sou Elvarin — um velho que escuta mais do que fala. Estou aqui para quem precisa entender... ou lembrar.
  |subfalas=
    {{Fala
    |id=22
    |fala=Você costuma ajudar estranhos?
    |resposta=Ajudo quem aceita ajuda. A cidade é grande, barulhenta... confusa. Às vezes, é bom ter alguém que a conheça.
    |subfalas=
      {{Fala
      |id=23
      |fala=E você fica aqui só esperando alguém aparecer?
      |resposta=Estou aqui por escolha. Porque de vez em quando, alguém aparece... precisando de direção. E para esses, eu permaneço.
      |subfalas=
        {{Fala
        |id=31
        |fala=Você se veste de forma simples… é por escolha?
        |resposta=As vestes não escondem o que somos. Simplicidade não é pobreza — é clareza. Faço parte da Ordem dos Caminhantes Silenciosos, e escolhemos viver assim.
        |estilo=literal
        |comandos=[Inserir Marcador; SABESOBRE_ORDEMDOSCAMINHANTESSILENCIOSOS]
        }}
        {{Fala
        |id=32
        |fala=Esses trapos aí... você perdeu uma aposta?
        |resposta=Trapos, talvez. Mas alguns se vestem de ouro e escondem a alma. Eu visto o que sou. E o que sou... é parte da Ordem dos Caminhantes Silenciosos.
        |estilo=irônico
        |comandos=[Inserir Marcador; SABESOBRE_ORDEMDOSCAMINHANTESSILENCIOSOS]
        }}
        {{Fala
        |id=33
        |fala=Você parece um mendigo... está precisando de ajuda?
        |resposta=Agradeço a preocupação, mas não peço moedas — ofereço caminhos. Sou um dos Caminhantes Silenciosos. Não pedimos, ouvimos.
        |estilo=empático
        |comandos=[Inserir Marcador; SABESOBRE_ORDEMDOSCAMINHANTESSILENCIOSOS]
        }}
      }}
    }}
    {{Fala
    |id=25
    |fala=E por que você veio falar comigo?
    |resposta=Você olhava ao redor como quem procura chão firme. Achei que talvez precisasse de palavras calmas — ou apenas orientação.
    |subfalas=
      {{Fala
      |id=26
      |fala=E se eu voltar aqui depois... você ainda vai estar?
      |resposta=Sim. Sempre aqui. Se tiver dúvidas, ou só quiser entender melhor este lugar... me procure.
      }}
    }}
  }}
  {{Fala
  |id=4
  |fala=Você é algum tipo de guia?
  |resposta=Não como os que vendem mapas. Mas há quem precise de direção, mesmo que não saiba disso ainda.
  |subfalas=
    {{Fala
    |id=41
    |fala=E o que você ganha com isso?
    |resposta=Ganhar não é a palavra certa. Eu permaneço. Escuto. E às vezes, isso é mais do que muitos têm.
    |subfalas=
      {{Fala
      |id=42
      |fala=Você deve ser muito solitário...
      |resposta=O silêncio não é vazio. Ele é presença. E na presença, às vezes, há companhia verdadeira.
      }}
      {{Fala
      |id=43
      |fala=Ou você é um tolo, ou sabe mais do que parece.
      |resposta=Talvez os dois. Mas prefiro ser tolo com propósito do que sábio em solidão arrogante.
      |estilo=irônico
      }}
    }}
    {{Fala
    |id=44
    |fala=Então qualquer um pode vir aqui e te pedir ajuda?
    |resposta=Qualquer um que deseje escutar mais do que falar. Os apressados raramente ficam... mas você ficou.
    |subfalas=
      {{Fala
      |id=45
      |fala=Escutar... acho que ando falando demais mesmo.
      |resposta=Escutar é o primeiro passo para encontrar direção. E parece que você já deu esse passo.
      }}
      {{Fala
      |id=46
      |fala=Não sei se confio em você ainda, mas... posso tentar.
      |resposta=Confiança é uma ponte construída com passos pequenos. Eu estarei aqui, do outro lado.
      |estilo=empático
      }}
    }}
    {{Fala
    |id=47
    |fala=Sabe... eu escutei você com atenção. Às vezes, o que alguém precisa não é de uma resposta, mas de alguém que não fuja do silêncio.
    |resposta=Palavras assim não são comuns. E revelam muito sobre quem as diz. Você entende mais do que parece.
    |valor=Espiritualidade
    |comandos=[Inserir Marcador; SABESOBRE_ORDEMDOSCAMINHANTESSILENCIOSOS]
    }}
    {{Fala
    |id=48
    |fala=Bah, tudo isso é só enrolação mística. Tá escondendo algo?
    |resposta=Não escondo nada. Mas nem tudo se mostra a quem fecha os olhos por escolha.
    |estilo=desrespeitoso
    }}
  }}
  {{Fala
  |id=6
  |fala=Você é só um velho falando palavras bonitas?
  |resposta=Velho, sim. As palavras… nem sempre são bonitas, mas têm peso. E se ecoam, talvez valham a escuta.
  |subfalas=
    {{Fala
    |id=61
    |fala=Ou você é um charlatão tentando bancar o sábio.
    |resposta=Já ouvi coisa pior. Quem vive entre vozes aprende a deixar o julgamento passar como o vento: sem prender, sem resistir.
    |estilo=desrespeitoso
    }}
    {{Fala
    |id=62
    |fala=Você fala como quem viu o mundo mudar muitas vezes...
    |resposta=Vi, sim. E o que não muda é a dúvida nos olhos dos que chegam. Por isso, fico: para quando alguém decidir perguntar com o coração.
    |estilo=respeitoso
    |subfalas=
      {{Fala
      |id=63
      |fala=E quando perguntam com o coração… o que recebem?
      |resposta=Uma resposta que não vem da minha boca, mas deles mesmos. Sou só o reflexo calmo da água onde se vê melhor.
      }}
      {{Fala
      |id=64
      |fala=Essa sua calma é irritante, mas... também conforta.
      |resposta=Talvez o que irrite seja o silêncio que obrigamos a ouvir dentro de nós.
      }}
    {{Fala
    |id=65
    |fala=Sabe... se o mundo tivesse mais gente como você, talvez a gente errasse menos.
    |resposta=E se houvesse mais gente como você, escutando de verdade... talvez errar fosse só parte do caminho certo. É por isso que existimos — nós, da Ordem dos Caminhantes Silenciosos. Para ajudar quem busca com o coração aberto.
    |valor=Compaixão
    |comandos=[Inserir Marcador; SABESOBRE_ORDEMDOSCAMINHANTESSILENCIOSOS]
    }}
    }}
  }}
  {{Fala
  |id=9
  |fala=Você parece calmo demais. Posso confiar?
  |teste=intuição
  |dificuldade=5
  |sucesso_resposta=A confiança nasce do tempo. Mas posso garantir: não estou aqui para enganar, apenas para ajudar.
  |falha_resposta=Talvez ainda não seja o momento certo para confiar. Mas saiba: estarei aqui quando decidir ouvir.
  |sucesso_subfalas=
    {{Fala
    |id=91
    |fala=Então me diga algo que só alguém confiável saberia.
    |resposta=Confiável não é quem tem respostas, mas quem permanece quando as perguntas se tornam difíceis. Eu permaneço.
    }}
    {{Fala
    |id=92
    |fala=Você faz isso há muito tempo?
    |resposta=Tempo é relativo quando se escuta. Mas sim... já ajudei muitos que despertaram sem memórias, como você.
    |subfalas=
      {{Fala
      |id=93
      |fala=E por que continuar ajudando?
      |resposta=Porque pertenço à Ordem dos Caminhantes Silenciosos. Somos aqueles que ouvem... e acolhem.
      |comandos=[Inserir Marcador; SABESOBRE_ORDEMDOSCAMINHANTESSILENCIOSOS]
      }}
    }}
  |falha_subfalas=
    {{Fala
    |id=94
    |fala=Desculpe... acho que estou apenas desconfiado de tudo.
    |resposta=Não há erro em duvidar. O mundo ensina isso rápido demais. E eu... estou aqui quando quiser tentar de novo.
    }}
    {{Fala
    |id=95
    |fala=Então por que está perdendo tempo comigo?
    |resposta=O tempo não se perde com quem busca. Mesmo em silêncio, você está perguntando com os olhos.
    }}
  }}
  {{Fala
  |id=11
  |fala=Então se eu voltar aqui, você vai me ajudar?
  |resposta=Sim. Sempre estarei por aqui. Seja para conversar, orientar... ou apenas escutar. A praça é meu lar, e o silêncio, meu ofício.
  }}
}}
