# Sistema de Marcadores

O sistema de **Marcadores** da engine **PlayTale** é o mecanismo central de controle de progresso narrativo, condições lógicas e desbloqueio de conteúdo. Cada marcador representa um ponto de estado binário (presente ou ausente) na jornada do jogador, permitindo que o jogo reaja de forma dinâmica e consistente às ações e escolhas feitas.

## Conceito de Marcadores

Marcadores são registros internos aplicados à ficha do jogador que indicam que algo aconteceu, foi descoberto, conquistado ou decidido. Eles não possuem valor numérico — sua presença ou ausência é o que define as condições avaliadas pela engine.

### Um marcador pode representar:
- Uma informação aprendida.
- Um evento testemunhado.
- Um item importante obtido.
- Um teste superado ou falhado.
- Um trecho de missão concluído.
- Uma conquista significativa.
- A finalização de uma linha narrativa.
- O desbloqueio de uma área especial.
- A ativação de uma badge ou troféu.

## Utilização dos Marcadores

Marcadores são utilizados por praticamente todos os sistemas de lógica narrativa da engine:

1. **Desbloqueio e ramificação de [diálogos](estruturaDeDialogo.md).**
1. **Controle de avanço em missões e linhas narrativas.**
1. **Geração de entradas no [Diário do Jogador](diarioDoJogador.md).**
1. **Mudança de comportamento ou falas de NPCs.**
1. **Controle de acesso a zonas, ambientes ou interações.**
1. **Registro de descobertas, eventos e segredos.**
1. **Verificações de continuidade ao trocar de região, salvar ou carregar.**
1. **Concessão de badges, conquistas ou efeitos especiais.**
1. **Condicional de scripts, magias ou eventos especiais.**

## Nomeação de Marcadores

A nomenclatura dos marcadores deve seguir o formato **UPPER_SNAKE_CASE**, com nomes descritivos e claros. A padronização facilita sua busca e interpretação durante o design e integração.

### Prefixos de Marcadores
Marcadores são utilizados para registrar acontecimentos, descobertas, relações ou estados no progresso do jogador. Para facilitar a organização, manutenção e leitura dos eventos, todos os marcadores seguem uma estrutura com **prefixos padronizados**. Esses prefixos indicam o tipo e contexto do marcador.


## Prefixos Utilizados
- **CONHECE_**  - Prefixo utilizado para indicar que o jogador **conheceu ou foi apresentado a um personagem, criatura ou entidade específica**.
:* Exemplo: <code>CONHECE_ELVARINDOSILENCIO</code> — o jogador conheceu Elvarin do Silêncio.

- **SABESOBRE_** - Prefixo utilizado quando o jogador **descobre, toma ciência ou aprende sobre uma organização, evento, local ou informação relevante**.
:* Exemplo: <code>SABESOBRE_ORDEMDOSCAMINHANTESSILENCIOSOS</code> — o jogador agora sabe da existência e natureza da Ordem dos Caminhantes Silenciosos.

## Registro de Marcadores

Marcadores podem ser registrados de diversas formas:
- Por comandos em diálogos:
<pre>[Inserir Marcador; NOME_DO_MARCADOR]</pre>
- Por scripts de evento, interação com objetos, ou conclusão de tarefas.
- Automaticamente, ao cumprir certos gatilhos narrativos.

Marcadores são, por padrão, **permanentes** — só devem ser removidos por decisão narrativa específica ou efeito mágico singular.

## Consulta de Marcadores

Durante verificações lógicas, são utilizadas funções de leitura booleana:

<pre>hasModifier("NOME_DO_MARCADOR")</pre>

Versões múltiplas:
<pre>hasAnyModifier("A", "B")</pre>
<pre>hasAllModifiers("A", "B", "C")</pre>

## Boas Práticas

- Use nomes claros e específicos.
- Prefira granularidade a genéricos.
- Nunca reutilize marcadores para finalidades diferentes.
- Documente a função narrativa dos marcadores importantes.

## Relação com outros sistemas

- O [Diário_do_Jogador](diarioDoJogador.md) pode ser alimentado automaticamente por marcadores com certos prefixos.
- [Estrutura de Diálogo](estruturaDeDialogo.md) depende diretamente de marcadores para condicionar falas e caminhos.
- Troféus, conquistas e desbloqueios visuais também utilizam marcadores como chave de ativação.

> **Nota:** Embora o termo "marco" ou "milestone" possa ser utilizado informalmente no design, toda lógica da engine trata esses elementos como **Marcadores**. O uso consistente desse termo facilita a padronização e integração entre os sistemas narrativos.
