# Modificadores

Modificadores representam efeitos temporários que alteram desempenho por bônus, penalidades ou combinação de ambos.

## Objetivo Mecânico

- Centralizar efeitos recorrentes em componentes reutilizáveis.
- Reduzir ambiguidade de balanceamento entre sistemas (combate, exploração e recuperação).
- Permitir regras previsíveis de aplicação, empilhamento e expiração.

## Modelo Canônico de Modificador

Cada modificador deve declarar:

- **ID técnico** (único).
- **Nome de exibição**.
- **Gatilho de aplicação** (ex.: fome >= 43%, habilidade, ambiente).
- **Tipo de efeito** (atributo, taxa, chance, bloqueio de ação, recuperação).
- **Magnitude** (valor absoluto, percentual, multiplicador).
- **Duração** (turnos, tempo contínuo, até condição de remoção).
- **Empilhamento** (não empilha, empilha intensidade, empilha duração).
- **Condição de remoção**.

## Regras de Resolução

1. Aplicar primeiro modificadores que alteram **taxas globais** (ex.: ganho de fadiga).
2. Aplicar depois modificadores de **atributo base**.
3. Em seguida, aplicar modificadores de **chance/precisão/efeito**.
4. Por fim, aplicar **limites mínimos e máximos** do sistema.
5. Se dois modificadores conflitarem, prevalece o de maior prioridade definida no próprio modificador.

## Catálogo Inicial Revisado

### Exaustão
- Gatilho: fadiga acima do limite máximo.
- Efeitos:
  - penalidade proporcional em Força;
  - redução de velocidade de movimento;
  - redução de precisão de ataque.

### Sede
- Gatilho: sede em faixa de alerta.
- Efeitos:
  - penalidade leve de precisão;
  - aumento de ganho de fadiga;
  - redução da eficácia de recuperação.

### Sede Agravada
- Gatilho: sede em faixa crítica.
- Efeitos:
  - herda efeitos de Sede;
  - penalidades adicionais de atributo;
  - risco de desorientação;
  - bloqueio parcial/total de recuperação por descanso.

### Fome
- Gatilho: fome em faixa de alerta.
- Efeitos:
  - penalidades moderadas em atributos;
  - redução de performance ofensiva.

### Fome Agravada
- Gatilho: fome em faixa crítica.
- Efeitos:
  - herda efeitos de Fome;
  - penalidades adicionais;
  - aumento de ganho de fadiga;
  - redução de mobilidade.

### Fadiga Elevada
- Efeito principal: multiplicador de ganho de fadiga.

### Mal-Estar Temporário
- Efeito principal: penalidade uniforme nos atributos principais.

### Recuperação Reduzida
- Efeito principal: multiplicador negativo em toda forma de cura/recuperação.

## Relação com Outros Sistemas

- Com **Aflições e Estados**: modificadores podem ser disparados por estados e também reforçar estados existentes.
- Com **Resistências**: resistências específicas podem reduzir chance/intensidade de modificadores de origem interna (ex.: veneno, fadiga, dor).
- Com **Metas de Balanceamento**: excesso de penalidades simultâneas deve acionar alerta de experiência frustrante.

## Pendentes para Requisitos

1. Definir faixas numéricas oficiais para cada gatilho (alerta/crítico).
2. Definir prioridade formal para conflitos entre modificadores equivalentes.
3. Definir teto de penalidade acumulada por atributo e por turno.
