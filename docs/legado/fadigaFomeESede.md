# Fadiga

A **Fadiga** representa o desgaste físico do personagem ao longo do tempo. A maior parte das ações executadas durante a jornada (como batalhas, caminhadas, corridas, trabalhos, etc.) aumentam a **Fadiga**. Algumas poucas atividades consideradas como **descanso** (como sentar, conversar parado ou cavalgar) podem ajudar a reduzi-la.


> **Nota:** Para detalhes sobre o acúmulo de Fadiga por tipo de ação, veja [Custo de Fadiga por Ação do Jogador](acoesDoJogadorCustoDeFadigaPorAcaoDoJogador.md).


Ao atingir o limite de **Fadiga Máxima**, o personagem sofre perda de desempenho nas atividades, entrando em **estado de exaustão**, e eventualmente pode **desmaiar**.

O ritmo de acúmulo da **Fadiga** pode aumentar de acordo com [Modificadores](modificadores.md) aplicados. Por exemplo:
- Esforço físico intenso;
- Clima extremo (frio ou calor);
- Efeitos de Aflições;
- Maldições;
- Etc.


## Fadiga Máxima
A **Fadiga Máxima** define o valor máximo de **Fadiga** que o jogador pode acumular antes de entrar em exaustão, definida por:

<math>\text{Fadiga}_{\text{máx}} = (\text{CON} + \text{VON}) \times 100</math>


Ao atingir esse valor, o jogador entra em **exaustão**, sofrendo penalidades severas como perda de força, lentidão e diminuição de reflexos.


## Fadiga Mínima
A **Fadiga Mínima** é o limite inferior da **Fadiga**. Representa o cansaço natural acumulado ao longo do tempo acordado. Aumenta automaticamente com o tempo e só pode ser reduzida ao **dormir**, ou se o personagem **desmaiar**.

Como o valor atual de Fadiga nunca pode ser inferior à Fadiga Mínima, quanto mais tempo o personagem permanecer acordado, mais rapidamente se esgotará, forçando a necessidade de dormir.

<math>\text{Fadiga}_{\text{mín}} += \frac{1}{1440} \text{por minuto acordado}</math>

Em 24 horas acordado (1440 minutos), a Fadiga Mínima atinge 100% da Fadiga Máxima, provocando exaustão e risco de desmaio.


## Exaustão
Quando a **Fadiga** ultrapassa a **Fadiga Máxima**, o personagem entra em **estado de exaustão**, podendo acumular até **120%** da **Fadiga Máxima**. Neste estágio, sofre penalidades drásticas e risco de colapso.

<math>\text{Exaustão} = \text{Fadiga}_{\text{máx}} \times 1{,}2</math>


Quando em Exaustão é atribuído automaticamente o modificador [Exaustão](modificadoresExaustao.md).

## Desmaio
Ao atingir **120% da Fadiga Máxima**, o personagem desmaia imediatamente por colapso físico e mental.

Para mais informações, veja [Desmaio](morteEDesmaiosDesmaio.md).

# Sede
A **Sede** representa a necessidade de hidratação. Acumula-se automaticamente ao longo do tempo, independentemente das ações do personagem. Só pode ser reduzida com o consumo de água ou líquidos apropriados.


## Cálculo de Acúmulo de Sede
**Fórmula:**

<math>\text{Sede}_{\text{por minuto}} = \frac{100}{48 \times 60} \times \text{Fator Ambiental} \approx 0{,}0347\%</math>


## Estágios de Sede

{| class="wikitable"
! Nível de Sede !! Intervalo (%) !! Efeitos
|-
| Nenhuma || 0% – 24% || Nenhum efeito negativo.
|-
| [SEDE](modificadoresSede.md) || 25% – 64% || Conforme Definições do Modificador.
|-
| [SEDE AGRAVADA](modificadoresSedeAgravada.md) || 65% – 99% || Conforme Definições do Modificador.
|-
| Colapso || 100% || Desmaio imediato.
|}

## Aceleradores de Sede
- Ambientes de **alta temperatura** ou **baixa umidade**
- Atividades intensas como correr, lutar ou carregar peso
- Aflições ou efeitos mágicos que intensificam desidratação

Estes fatores aumentam temporariamente o valor de acúmulo por minuto.


# Fome
A **Fome** simula a necessidade de nutrição. Acumula-se gradualmente ao longo do tempo, e só pode ser reduzida com ingestão de alimentos.


## Cálculo de Acúmulo de Fome
**Fórmula:**

<math>\text{Fome}_{\text{por minuto}} = \frac{100}{7 \times 24 \times 60} \times \text{Fatores Ambientais} \approx 0{,}0099\%</math>


## Estágios de Fome

{| class="wikitable"
! Nível de Fome !! Intervalo (%) !! Efeitos
|-
| Nenhuma || 0% – 42% || Nenhum efeito negativo.
|-
| [FOME](modificadoresFome.md) || 43% – 84% || -1 Força, -1 Intelecto, -5% chance de crítico, testes mentais mais difíceis.
|-
| [FOME AGRAVADA](modificadoresFomeAgravada.md) || 85% – 99% || Todos os efeitos anteriores, -2 Constituição, -2 Vontade, +20% consumo de Fadiga, -20% velocidade de movimento.
|-
| Colapso || 100% || Desmaio imediato.
|}

## Aceleradores de Fome

- Esforço físico prolongado
- Frio extremo
- Aflições, venenos ou maldições que aumentam a demanda alimentar

Esses fatores aumentam o valor de acúmulo de Fome por minuto.
