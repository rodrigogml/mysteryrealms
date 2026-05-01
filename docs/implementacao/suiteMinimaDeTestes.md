# Suíte Mínima de Testes dos Módulos Core

## Objetivo

Mapear as funcionalidades essenciais e definir uma suíte mínima de testes para reduzir risco de regressão nos fluxos críticos do sistema.

## Módulos Core e Funcionalidades Essenciais

- Usuários e segurança:
  - cadastro, confirmação de e-mail, login, recuperação de conta e 2FA.
- Personagens e progressão:
  - criação de personagem, atributos derivados e evolução por nível.
- Mundo/instância:
  - criação de instância por personagem e persistência de estado.
- Co-op/multijogador:
  - criação de sessão, entrada de convidados e regras de status.
- Regras de domínio (combate, social, fisiologia, economia, navegação):
  - cálculos determinísticos e validações de limites.

## Cobertura Atual da Suíte Mínima

### Testes unitários (regras de negócio principais)

- `UserServiceTest` para regras de autenticação e segurança.
- `CharacterServiceTest` para criação e validações de personagem.
- `WorldInstanceServiceTest` para consistência de estado de mundo.
- `CoopSessionServiceTest` para regras de sessão cooperativa.
- Testes de domínio em `domain/*` para combate, progressão, social, fisiologia, economia, modificadores e navegação.

### Testes de integração (fluxos ponta a ponta críticos)

- `IntegrationCriticalFlowsTest` com cenários:
  - usuário -> personagem -> instância inicial de mundo;
  - host + convidado em sessão co-op;
  - persistência de estado após ação de combate.

## Dados de teste reutilizáveis

- `TestDataFactory` como factory compartilhada para:
  - dados de cadastro (`RegistrationData`);
  - dados de criação de personagem (`CharacterCreationData`).
- `CombatTestFixture` como fixture especializada para cenários de combate.

## Meta inicial de cobertura

Meta inicial para os módulos core (fase de baseline):

- Cobertura de linhas: **60%** mínimo.
- Cobertura de branches: **45%** mínimo.
- Cobertura obrigatória dos fluxos críticos de aplicação: **100% dos cenários mapeados** em `IntegrationCriticalFlowsTest`.

## Próximos passos

- Consolidar relatório de cobertura por pacote (`application` e `domain`) em pipeline CI.
- Evoluir meta para 70%/55% após estabilização dos fluxos de co-op.
