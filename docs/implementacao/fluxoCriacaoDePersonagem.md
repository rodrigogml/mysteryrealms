# Fluxo de Criação e Gestão de Personagens (Pós-Login)

## Objetivo
Definir o fluxo ponta a ponta da tela de personagens após login, cobrindo: listagem, seleção, criação, renomeação e exclusão; além de mapear lacunas de requisitos e definições de mundo necessárias para implementação segura.

## Escopo
- Entrada: usuário autenticado com sessão válida.
- Saída: personagem selecionado para jogar **ou** personagem criado e selecionado.
- Fora de escopo (neste documento): gameplay em mundo, combate e progressão pós-criação.

## Fluxo principal (visão funcional)

### 1. Entrada na tela de personagens
1. Após autenticação bem-sucedida, o sistema redireciona para a tela de personagens.
2. O sistema carrega os personagens do usuário ordenados por `ultimoAcesso` desc e, em empate, por `dataCriacao` desc.
3. A tela exibe para cada personagem: nome, raça, classe, nível e último acesso.
4. A tela exibe ações: **Selecionar**, **Criar novo**, **Renomear**, **Excluir**.

### 2. Seleção de personagem existente
1. Usuário clica em **Selecionar**.
2. Sistema valida posse do personagem e status válido.
3. Sistema marca `ultimoAcesso`.
4. Sistema carrega a instância de mundo vinculada ao personagem (RF-IM-01/02/03).
5. Usuário entra na sessão de jogo.

### 3. Criação de personagem (wizard)
1. Usuário clica em **Criar novo**.
2. Sistema verifica limite de 50 personagens por usuário (RF-PE-01).
3. Wizard sugerido:
   - Etapa A: Identidade (nome, sobrenome, gênero, idade inicial).
   - Etapa B: Escolha de raça.
   - Etapa C: Escolha de classe.
   - Etapa D: Revisão (atributos iniciais derivados de raça+classe).
   - Etapa E: Confirmação e criação.
4. Ao confirmar:
   - Valida unicidade de nome por usuário.
   - Inicializa ficha base (RF-FP-01..08).
   - Cria instância de mundo vinculada (RF-IM-01/02).
   - Registra timestamps de criação/último acesso.
5. Sistema retorna para a lista com o novo personagem selecionado (opção padrão) ou permite seleção manual.

### 4. Renomeação
1. Usuário clica em **Renomear**.
2. Sistema valida formato e unicidade por usuário.
3. Persiste alteração e atualiza lista.

### 5. Exclusão
1. Usuário clica em **Excluir**.
2. Sistema exige confirmação forte (texto de confirmação com nome do personagem).
3. Ao confirmar, remove personagem e instância de mundo associada (RF-PE-05 + RF-IM-04).
4. Atualiza lista; se era o último personagem, exibe CTA para criação.

## Requisitos faltantes ou ambíguos

### A. Lacunas em requisitos de aplicação
1. **Estado de personagem excluído**: não está definido se haverá soft delete ou hard delete técnico (apesar do requisito funcional indicar exclusão permanente).
2. **Ordenação oficial da lista**: requisito atual pede exibição de último acesso, mas não formaliza ordenação.
3. **Comportamento com lista vazia**: falta regra explícita para primeiro acesso sem personagens.
4. **Bloqueio de ações concorrentes**: falta requisito para prevenir duplo clique/dupla criação.
5. **Padrão de navegação pós-criação**: auto-seleciona ou permanece na tela?

### B. Lacunas em ficha e criação
1. RF-PE-02 cita apenas nome/raça/classe, mas RF-FP-01 exige `sobrenome`, `genero`, `idadeInicial`; falta alinhar requisitos.
2. Não há contrato explícito de quais campos são editáveis somente na criação vs pós-criação.
3. Não há requisitos de validação de nome (tamanho, charset, palavras bloqueadas).

### C. Lacunas de definições do mundo
1. Não existe, em `/docs/mundo/`, catálogo versionado de raças/classes com metadados de exibição (lore curto, tags, afinidades).
2. Não existe definição de **origem inicial no mundo** por raça/classe (spawn inicial, narrativa de entrada).
3. Não existe regra explícita de **seed de conteúdo inicial** da instância (itens iniciais, diário inicial, reputações iniciais por facção/localidade).

## Plano de implementação (tarefas)

## Épico FC-01 — Tela de gestão de personagens pós-login
- **FC-01.1 (P0)**: Definir contrato API de listagem/seleção/renomeação/exclusão.
- **FC-01.2 (P0)**: Implementar listagem ordenada e estado vazio.
- **FC-01.3 (P0)**: Implementar seleção com atualização de `ultimoAcesso` em transação.
- **FC-01.4 (P1)**: Implementar confirmação forte de exclusão.

## Épico FC-02 — Wizard de criação de personagem
- **FC-02.1 (P0)**: Definir payload de criação alinhado a RF-FP-01 (nome, sobrenome, gênero, idadeInicial, raça, classe).
- **FC-02.2 (P0)**: Validar unicidade de nome por usuário e limite de 50 personagens.
- **FC-02.3 (P0)**: Orquestrar criação atômica de personagem + instância de mundo.
- **FC-02.4 (P1)**: Implementar etapa de revisão com prévia de atributos iniciais.

## Épico FC-03 — Fechamento de lacunas de requisitos
- **FC-03.1 (P0)**: Atualizar `docs/requisitos/aplicacao/personagens.md` com ordenação, estado vazio e pós-criação.
- **FC-03.2 (P0)**: Alinhar RF-PE-02 com RF-FP-01 em campos obrigatórios da criação.
- **FC-03.3 (P1)**: Formalizar regras de validação de nomes e política de renomeação.
- **FC-03.4 (P1)**: Definir idempotência e controle de concorrência no endpoint de criação.

## Épico FC-04 — Definições de mundo para suportar criação
- **FC-04.1 (P0)**: Criar documento de catálogo de raças/classes em `/docs/mundo/` com metadados funcionais de criação.
- **FC-04.2 (P0)**: Definir ponto de origem inicial por combinação válida (ou regra geral parametrizável).
- **FC-04.3 (P1)**: Definir seed inicial de inventário/diário/reputação para bootstrap de instância.

## Critérios de pronto (DoD)
- Fluxo pós-login cobre 5 operações: listar, selecionar, criar, renomear, excluir.
- Criação ocorre em transação única entre ficha + instância de mundo.
- Requisitos de aplicação e ficha sem conflito para campos obrigatórios.
- Definições mínimas de mundo publicadas para remover ambiguidade de bootstrap.
