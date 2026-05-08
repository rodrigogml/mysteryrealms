# Tela de Jogo Após Criação do Personagem

## Objetivo
Planejar o fluxo imediatamente posterior à criação ou seleção de personagem, conectando backend, sessão Vaadin e primeira versão da tela de jogo.

## Premissas vigentes
- O usuário já está autenticado e possui uma sessão válida.
- A criação de personagem deve gerar a ficha inicial e uma instância de mundo vinculada ao personagem.
- A seleção do personagem deve validar posse, atualizar último acesso e manter o `characterId` selecionado apenas na sessão da UI.
- A tela de jogo deve carregar a instância de mundo do personagem selecionado antes de exibir ações jogáveis.
- O primeiro incremento deve expor estado inicial e ações bloqueadas ou mínimas, sem inventar mecânicas novas.

## Fluxo esperado após criar o personagem
1. O usuário confirma o wizard de criação.
2. A UI envia os dados de criação para o backend de personagens.
3. O backend valida limite, unicidade, idade, raça e classe.
4. O backend cria a ficha do personagem.
5. O backend solicita ao serviço de instância de mundo a criação do mundo inicial.
6. O backend persiste personagem e instância de mundo de forma atômica.
7. O backend retorna um DTO de seleção com `characterId`, `worldInstanceId`, dados mínimos do personagem e localidade atual.
8. A UI grava `characterId` em `mysteryrealms.selectedCharacterId` na sessão Vaadin.
9. A UI navega para `/game`.
10. A tela de jogo valida a sessão, valida novamente a posse do personagem e carrega a instância de mundo.
11. A tela renderiza o estado inicial jogável.

## Fluxo esperado ao iniciar com personagem existente
1. O usuário clica em selecionar ou jogar na lista de personagens.
2. A UI chama o backend de seleção.
3. O backend valida posse do personagem.
4. O backend atualiza `lastAccessedAt`.
5. O backend localiza a instância de mundo vinculada.
6. O backend retorna o DTO de seleção.
7. A UI grava `characterId` na sessão Vaadin.
8. A UI navega para `/game`.
9. A tela de jogo executa o mesmo carregamento inicial usado após criação.

## Contrato mínimo de backend para a tela de jogo

### DTO de entrada da criação
Campos mínimos:
- `name`.
- `surname`.
- `gender`.
- `initialAge`.
- `race`.
- `characterClass`.

### DTO de seleção para jogo
Campos mínimos:
- `characterId`.
- `worldInstanceId`.
- `lastAccessedAt`.
- `characterName`.
- `race`.
- `characterClass`.
- `currentLevel`.
- `currentLocationId`.
- `currentTimeMin`.

### Snapshot inicial da tela de jogo
Campos mínimos para evoluir a `GameView` sem acoplar a UI às entidades JPA:
- Identificação do personagem: nome, raça, classe e nível.
- Estado de mundo: localidade atual, tempo atual e indicadores de bootstrap.
- Estado resumido do personagem: vida, recursos principais e estados fisiológicos quando disponíveis no backend.
- Ações disponíveis na localidade atual.
- Mensagens de bloqueio quando uma ação ainda não estiver implementada ou não estiver disponível no contexto atual.

## Composição sugerida da tela de jogo

### Cabeçalho
- Nome do jogo.
- Nome do personagem.
- Raça, classe e nível.
- Botão para voltar à lista de personagens.

### Painel de localidade
- Nome ou identificador da localidade atual.
- Descrição curta quando o catálogo de localidades estiver disponível.
- Tempo atual do mundo.
- Estado de descoberta e acesso da localidade.

### Painel de personagem
- Vida e recursos principais.
- Fadiga, fome, sede, moral e outros estados quando já persistidos.
- Indicadores de aflições ou modificadores ativos.

### Painel de ações
- Ações primárias: explorar, viajar e descansar.
- Ações contextuais futuras: dialogar, combater, coletar, comprar, abrir diário e inventário.
- Cada ação deve ser obtida a partir do estado carregado do backend, não codificada diretamente na UI.

### Painel de feedback
- Resultado da última ação.
- Mensagens narrativas curtas.
- Erros padronizados vindos do mapeador de erros.

## Sequência técnica sugerida

### Fase TJ-01 — Consolidar contrato de entrada no jogo
- Criar ou consolidar um DTO de seleção para jogo usado por criação e seleção.
- Evitar retorno direto de entidades JPA para a UI.
- Garantir que criação e seleção tenham o mesmo contrato de navegação.

### Fase TJ-02 — Criar snapshot backend da tela de jogo
- Criar serviço de aplicação para montar o snapshot inicial da tela.
- O serviço deve validar sessão, posse do personagem e integridade da instância de mundo.
- O snapshot deve ser a fonte da renderização inicial da tela.

### Fase TJ-03 — Evoluir a `GameView`
- Substituir leitura direta de `CharacterEntity` e `WorldInstanceEntity` por DTO de snapshot.
- Separar componentes visuais em métodos ou componentes menores: cabeçalho, localidade, personagem, ações e feedback.
- Manter ações desabilitadas até o backend fornecer comandos reais.

### Fase TJ-04 — Implementar comandos mínimos de jogo
- Definir contrato backend para ação de jogo com entrada `characterId`, `worldInstanceId`, tipo de ação e parâmetros.
- Implementar primeiro comando seguro: descanso ou exploração sem combate.
- Persistir alterações de mundo após cada ação que modifique estado.
- Retornar novo snapshot após cada comando.

### Fase TJ-05 — Testar fluxo ponta a ponta
- Cobrir criação seguida de entrada em `/game`.
- Cobrir seleção de personagem existente seguida de entrada em `/game`.
- Cobrir ausência de personagem selecionado.
- Cobrir tentativa de acesso a personagem de outro usuário.
- Cobrir instância de mundo ausente ou inconsistente.

## Lacunas antes de implementar ações reais
- Falta um contrato explícito para snapshot da tela de jogo.
- Falta catálogo de ações disponíveis por localidade.
- Falta exposição consolidada de estado fisiológico e recursos atuais no backend de aplicação.
- Falta definição de comandos mínimos de jogo que possam alterar tempo, diário, localidade ou estado do personagem.
- Falta separar visualmente a tela de jogo em componentes reaproveitáveis.

## Critérios de pronto da primeira versão jogável
- Criar personagem e entrar na tela de jogo sem passos manuais adicionais.
- Selecionar personagem existente e entrar na mesma tela de jogo.
- Exibir nome do personagem, raça, classe, nível, localidade atual e tempo do mundo.
- Bloquear ações sem backend implementado com mensagem clara.
- Não retornar entidade JPA diretamente para a UI em novos contratos.
- Ter testes automatizados cobrindo os fluxos de entrada na tela de jogo.
