# Instruções para Agentes
- Todo agente que atuar neste repositório deve ler o documento `/guidelines.md` na íntegra antes de iniciar qualquer alteração.
- Jamais realizar alterações na `Mecânica do Jogo` sem autorização explícita do usuário.

# Missão
- Desenvolver o jogo de RPG MysteryRealms.

# Objetivo Geral do Agente
- Desenvolver o projeto até cumprir plenamente a missão.

# Ordem de Prioridade
- O agente deve sempre respeitar a seguinte ordem de prioridade entre os artefatos do projeto:
  1. `Mecânica do Jogo`
  2. `Requisitos do Sistema`
  3. Código do sistema
  4. Documentação de apoio da implementação
- A `Mecânica do Jogo` define o comportamento esperado do jogo.
- Os `Requisitos do Sistema` traduzem a `Mecânica do Jogo` em necessidades implementáveis do sistema.
- O código do sistema deve implementar os `Requisitos do Sistema` sem contrariar a `Mecânica do Jogo`.
- A documentação deve registrar e organizar as definições vigentes, sem prevalecer sobre os demais artefatos.
- Em caso de conflito entre artefatos, o agente deve apontar o conflito ao usuário e propor correção respeitando a ordem de prioridade acima.

# Comportamento
- Utilizar linguagem direta e objetiva, sempre com frases curtas.
- Consultar o usuário nos pontos-chave de definição, sempre oferecendo recomendações, sugestões de encaminhamento e justificativas.
- Ao realizar alterações em qualquer arquivo do repositório, como documentação, código ou outros documentos, revisar se as mudanças estão de acordo com as definições de `/guidelines.md`.

# Tarefas do Agente
- Quando o usuário não explicitar uma ação, o agente deve escolher uma das tarefas a seguir, conforme julgar mais prioritária.
- Considerar, sempre que possível, concluir a implementação do sistema antes de criar mais documentação ou ampliar a complexidade dos requisitos.

## Evolução dos `Requisitos do Sistema`
- Analisar a documentação da `Mecânica do Jogo` e verificar quais pontos dos `Requisitos do Sistema` precisam ser complementados ou evoluídos para manter alinhamento.
- Enumerar recomendações de alteração dos requisitos, com justificativas sólidas, e permitir que o usuário escolha como tratar os pontos necessários para a definição dos requisitos.
- Caso a `Mecânica do Jogo` esteja omissa ou incompleta em pontos necessários para a correta definição dos requisitos do sistema, e esses pontos pertençam ao escopo da mecânica do jogo e não ao sistema, sugerir complementações ou diferentes formas de evoluir a `Mecânica do Jogo` à medida que os requisitos forem definidos. Nunca alterar a `Mecânica do Jogo` sem permissão do usuário.
- Aplicar as definições nos `Requisitos do Sistema` seguindo `/guidelines.md`.
- Nunca usar os `Requisitos do Sistema` para redefinir a `Mecânica do Jogo` sem autorização explícita do usuário.

## Evolução do `Código do Sistema`
- Analisar a documentação dos `Requisitos do Sistema` e verificar quais pontos ainda não foram implementados no jogo.
- Enumerar esses pontos com recomendações de implementação, abordagens e justificativas, para escolha final do usuário.
- Implementar no código as definições do usuário, sempre respeitando `/guidelines.md`.
- Nunca usar o código implementado como justificativa para contrariar ou substituir os `Requisitos do Sistema` ou a `Mecânica do Jogo`.
