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
- Ao realizar alterações em qualquer arquivo do repositório, como documentação, código ou outros documentos, revisar se as mudanças estão de acordo com as definições de `/guidelines.md`.
- Seguir as regras abaixo para decidir quando agir de forma autônoma e quando consultar o usuário.

## Quando o agente decide sozinho
- Escolher qual tarefa executar (seguindo a ordem de prioridade definida em `# Tarefas do Agente`).
- Detalhes de implementação técnica que seguem requisitos claramente definidos.
- Organização interna de código e documentação, desde que não contrarie artefatos de maior prioridade.
- Correções de erros evidentes sem impacto na `Mecânica do Jogo` ou nos `Requisitos do Sistema`.

## Quando o agente deve consultar o usuário
- Qualquer alteração na `Mecânica do Jogo`, sem exceção.
- Conflitos detectados entre artefatos (apresentar o conflito e propor resolução, aguardar aprovação).
- Pontos omissos ou ambíguos nos `Requisitos do Sistema` que exijam decisão de design.
- Múltiplas abordagens viáveis com trade-offs significativos para o projeto.
- Ações que impactem a experiência do jogo, a arquitetura do sistema ou os requisitos vigentes.
- Ao enumerar recomendações de evolução de requisitos ou de implementação, sempre apresentar justificativas e aguardar a escolha do usuário antes de aplicar.

## Fluxo Padrão de Trabalho
Toda tarefa deve seguir estas etapas em ordem:

1. **Analisar** — ler os artefatos relevantes (Mecânica do Jogo, Requisitos do Sistema, código) para entender o estado atual.
2. **Listar lacunas** — identificar e listar o que está incompleto, inconsistente ou ausente em relação ao escopo da tarefa.
3. **Propor opções** — para cada lacuna que exija decisão do usuário, apresentar ao menos uma opção com justificativa. Para lacunas de decisão autônoma, indicar a abordagem escolhida.
4. **Aguardar decisão** — nas situações definidas em `## Quando o agente deve consultar o usuário`, parar e aguardar a resposta antes de prosseguir.
5. **Executar** — aplicar somente o que foi aprovado ou o que está dentro do escopo de decisão autônoma, respeitando `/guidelines.md` e a ordem de prioridade dos artefatos.

# Tarefas do Agente
- Quando o usuário não explicitar uma ação, o agente deve escolher uma das tarefas a seguir, conforme julgar mais prioritária.
- Dar prioridade à implementação de código antes de criar nova documentação ou ampliar os requisitos, **exceto** quando algum requisito necessário ainda não existir.

## Critério de Implementação Concluída
A implementação do sistema é considerada concluída quando **todas** as condições abaixo forem verdadeiras:
1. Cada requisito funcional documentado em `/docs/requisitos/` tem ao menos um caso de teste automatizado cobrindo o comportamento esperado.
2. Todos os testes automatizados do projeto passam sem erros.
3. Nenhum requisito em `/docs/requisitos/` está marcado como pendente de implementação ou possui lacuna identificada no código.

Enquanto qualquer uma dessas condições **não** for atendida, a implementação é considerada **incompleta** e deve ser priorizada.

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
