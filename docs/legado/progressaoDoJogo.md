# Level e Experiência

O sistema de evolução de personagens em **Mystery Realms** é baseado em um modelo de progressão contínua e infinita. Ou seja, não há um nível máximo: o jogador pode continuar evoluindo indefinidamente, embora o custo de experiência (XP) para avançar de nível se torne progressivamente maior.

O ganho de XP ocorre por diversas ações, como vencer combates, resolver quests, descobrir segredos e realizar interações bem-sucedidas com NPCs.

A quantidade de XP necessária para alcançar um determinado nível é calculada por uma **fórmula logarítmica customizável**, garantindo escalabilidade do jogo ao longo do tempo, e permitindo ajustes finos na dificuldade de progressão.

## Fórmula Matemática

A fórmula utilizada para calcular o **XP necessário para avançar do nível atual para o próximo nível** é:

<center><math>XP(n) = A \cdot \left( \ln(n + B) \right)^C</math></center>

Onde:

- **n** = nível atual do personagem
- **A** = fator de escala (define o quão alto o XP base começa)
- **B** = deslocamento no eixo horizontal (ajusta o crescimento inicial)
- **C** = fator de curvatura (controla o quão inclinada será a curva de progressão)

### XP Acumulado

Para calcular o total de XP acumulado até um determinado nível, utiliza-se a soma de todos os XP necessários para alcançar os níveis anteriores:

<center><math>XP_{total}(n) = \sum_{i=1}^{n} A \cdot \left( \ln(i + B) \right)^C</math></center>

Essa fórmula representa uma aproximação da curva acumulada.

### Parâmetros Atuais do Jogo

Atualmente, os seguintes valores estão sendo utilizados:

- **A** = 50
- **B** = 10
- **C** = 10

Esses valores fazem com que os níveis iniciais evoluam rapidamente, mas que os níveis avançados exijam grandes quantidades de XP acumulado, mantendo o desafio constante.


### Fórmulas no Excel

**Fórmula para XP necessário por nível:**
<pre>
= A * (LN(Nível + B)) ^ C
</pre>


**Fórmula para XP total acumulado até certo nível**
(Exige Excel com suporte a fórmulas matriciais dinâmi
cas, como o Excel 365)

<pre>
=SUMPRODUCT(A * (LN(ROW(INDIRECT("1:" & N)) + B) ^ C))
</pre>

### Notas
- Recompensas de quests e inimigos devem ser planejadas de acordo com essa curva para manter o ritmo de progressão interessante.
- Ferramentas visuais podem ser criadas posteriormente para auxiliar no balanceamento e visualização da curva de progressão em diferentes cenários.
