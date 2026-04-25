# Resistências

Este documento unifica e operacionaliza o sistema de resistências para uso mecânico e futura formalização de requisitos.

## Papel das Resistências no Sistema

Resistências reduzem dano e/ou chance de aplicação de efeitos por tipo.

No fluxo de dano, resistências são aplicadas **após bloqueio** e antes do dano final, conforme a ordem canônica de combate.

## Limites Mecânicos

- **Personagens de jogador**: resistência efetiva por tipo com teto recomendado de 80%.
- **Criaturas/NPCs**: podem alcançar 100% (imunidade), desde que exista contrapeso de design (fraqueza, janela tática ou custo).
- **Vulnerabilidade**: valores negativos de resistência aumentam dano recebido.

## Taxonomia de Resistências

### Resistências Físicas
- Corte
- Perfuração
- Esmagamento

### Resistências Elementais
- Fogo
- Gelo
- Eletricidade
- Ácido
- Vento

### Resistências Mágicas
- Magia Pura
- Encantamentos

### Resistências Internas e de Aflição
- Veneno
- Doença
- Sangramento
- Fadiga
- Dor
- Som

### Resistências Mentais e Espirituais
- Medo
- Confusão
- Ilusão
- Controle Mental
- Corrupção Espiritual

## Regras Operacionais

1. Cada efeito ofensivo deve declarar explicitamente seu **tipo primário** de dano/aflição.
2. Por padrão, cada instância ofensiva usa **uma resistência principal**.
3. Exceções multi-tipo devem definir regra explícita de composição (ex.: maior resistência, média ponderada ou duas instâncias separadas).
4. Imunidade (100%) impede dano/efeito daquele tipo, mas não remove efeitos de outros tipos no mesmo turno.
5. Resistência contra aflição pode atuar em duas etapas:
   - reduzir chance de aplicação;
   - reduzir intensidade/duração após aplicada.

## Diretrizes de Balanceamento

- Evitar imunidades amplas sem contrajogo.
- Distribuir resistências por perfil de inimigo para incentivar variação tática.
- Revisar dados de combate quando um tipo de dano perder relevância sistêmica por resistência excessiva.

## Pendente para Requisitos

Antes da implementação, fechar os seguintes pontos:

1. Regra oficial para ataques multi-tipo.
2. Faixas por tier/nível para resistências de jogador e inimigos.
3. Tabela canônica de contrapesos para imunidades e vulnerabilidades.
