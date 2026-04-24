Aqui ficam documentados os padrões de criação dos roteiros do jogo. O objetivo é documentar as regras e padrões para que todo o jogo seja criado de forma consistente, coerente e visual padronizado.

# Estrutura Narrativa Visual

Todas as cenas do jogo devem seguir o seguinte padrão de ilustração e narrativa:

- **Perspectiva:** Sempre em primeira pessoa, refletindo o ponto de vista do jogador.
- **Formato visual:** Imagens estáticas, como quadros ilustrados que representam momentos específicos da narrativa.
- **Avanço narrativo:** Cada nova fala ou evento relevante pode gerar uma nova cena ilustrada.
- **Ambientação:** Os cenários devem conter elementos ricos que contextualizem visualmente o ambiente, os NPCs e a atmosfera do momento.
- **Foco emocional:** A expressão dos personagens, iluminação e composição devem transmitir com clareza a emoção ou tensão da cena (mistério, calma, desconforto, etc).
- **Estilo de arte:** Usar o modelo definido no prompt padrão do projeto: arte digital em semi-realismo cinematográfico, com riqueza de detalhes e atmosfera imersiva.
- **Sem textos nas imagens:** Toda a comunicação narrativa — falas, pensamentos ou lembranças — deve ser exibida no sistema textual do jogo, nunca diretamente na imagem.

# Documentação do Roteiro e Cena

Esta seção define o padrão obrigatório para documentar roteiros narrativos no universo de *Mystery Realms*. Cada roteiro representa uma sequência de cenas interativas e ilustradas, e deve seguir a estrutura abaixo para garantir consistência com o sistema de narrativa e ilustração do jogo.

## Estrutura Obrigatória

Todo roteiro deve conter, obrigatoriamente, os seguintes tópicos e estrutura:

### Roteiro: Título do Roteiro
- Título no primeiro nível (<code>=</code>).
- Abaixo do título, inserir o ID único do roteiro no seguinte formato:
<code>ID do Roteiro: X</code>
- Onde **X** um número sequencial único de identificação do roteiro dentro da página.

- Em seguida, incluir uma explicação introdutória breve (1 a 3 parágrafos) contextualizando a cena ou narrativa do roteiro.

### Cenas
Tópico com o título <code>== Cenas ==</code> (nível 2).

Cada cena deve ser estruturada da seguinte forma:

Título da cena: <code>=== Cena X: Título Descritivo ===</code> (nível 3)
- **Descrição da cena:** Parágrafo em terceira pessoa descrevendo o que o jogador vê, sempre da perspectiva em primeira pessoa (como se fosse a visão do personagem).

Para cada bloco de fala, pensamento ou lembrança, utilizar a seguinte marcação:
:* **[Nome do Personagem]:** (para falas de NPCs)
:* **Pensando:** (para pensamentos internos do personagem jogador)
:* **Imaginando:** (para lembranças, suposições ou projeções mentais)
:* **Eu:** (para quando o jogador se expressa verbalmente)

<blockquote>
Texto da fala, pensamento ou lembrança.

Segunda linha do texto.
</blockquote>

- Sempre separar ideias ou frases com pausas relevantes em parágrafos dentro do blockquote.
- Os títulos das cenas devem ser significativos e temáticos (ex: "O Despertar", "A Proposta", "Liberdade").

### Ilustrações das Cenas
Tópico no padrão <code>== Ilustrações das Cenas ==</code> (nível 2).

Deve conter uma galeria wiki com as imagens correspondentes a cada cena. A estrutura da galeria é a seguinte:

<pre>
<gallery mode="packed" widths="250" heights="250">
Arquivo:prefixo_rXc1.png|Cena 1
Arquivo:prefixo_rXc2.png|Cena 2
...
</gallery>
</pre>

- O nome dos arquivos de imagem deve seguir o padrão:
  * **prefixo_rXcY.png**
  * Onde:
    * **prefixo** é um identificador definido pelo usuário (ex: "inicial", "praca", "intro").
    * **X** é o número do Roteiro (conforme o ID).
    * **Y** é o número da Cena correspondente.

### Observações e Continuidade
Tópico no formato <code>== Observações e Continuidade ==</code> (nível 2).

Espaço reservado para observações adicionais e ideias de desenvolvimento, como:

- Pontos narrativos de retorno ou reencontro com personagens.
- Evolução do relacionamento com NPCs apresentados no roteiro.
- Propostas para roteiros derivados, ramificações narrativas ou cenas alternativas.
- Ligações diretas com possíveis quests futuros.
