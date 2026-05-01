# Arquitetura do Repositório

## Visão geral

O repositório está organizado como uma aplicação Spring Boot + Vaadin em camadas, com separação clara entre regras de negócio puras (`domain`) e orquestração/persistência (`application`).

## Diagrama textual dos componentes

```text
[UI Vaadin]
  br...ui.MainView
  br...ui.EmailConfirmationView
           |
           v
[Application Services]
  user.UserService
  character.CharacterService
  world.WorldInstanceService
  coop.CoopSessionService
           |
           +---------------------+
           |                     |
           v                     v
[Domain Services/Models]   [Infrastructure Persistence]
  domain.character.*        application.*.entity (JPA)
  domain.combat.*           application.*.repository (Spring Data)
  domain.world.*                    |
  domain.social.*                   v
  domain.physiology.*         [MySQL/H2 via JPA]
  domain.economy.*
  domain.modifier.*
```

## Camadas e responsabilidades por pasta/módulo

### Interface
- `src/main/java/br/eng/rodrigogml/mysteryrealms/ui`: telas Vaadin e interação inicial do usuário (`MainView`, `EmailConfirmationView`).

### Aplicação
- `src/main/java/.../application/user`: cadastro/autenticação, confirmação de e-mail, 2FA, sessão, recuperação de senha.
- `src/main/java/.../application/character`: ciclo de vida de personagem, vínculo com usuário, inventário e reputações persistidas.
- `src/main/java/.../application/world`: estado persistente da instância de mundo (tempo, localização, quest, NPC, diário, marcadores).
- `src/main/java/.../application/coop`: sessões cooperativas (lobby, progresso, pausa, encerramento, participantes).
- `src/main/java/.../application/*/entity`: entidades JPA de armazenamento.
- `src/main/java/.../application/*/repository`: portas de persistência via Spring Data JPA.

### Domínio
- `src/main/java/.../domain/character`: regras de atributos e progressão.
- `src/main/java/.../domain/combat`: fórmulas de combate/testes.
- `src/main/java/.../domain/world`: navegação, hierarquia do mundo e tempo.
- `src/main/java/.../domain/social`: ciclo social, diálogo, relacionamento/reputação.
- `src/main/java/.../domain/physiology`: estados fisiológicos e resolução.
- `src/main/java/.../domain/economy`: monetização, preços e itens de mão.
- `src/main/java/.../domain/modifier`: modificadores, custo de ações, empilhamento.

### Infraestrutura e bootstrap
- `src/main/java/.../Starter.java`: bootstrap Spring Boot.
- `src/main/resources`: configurações e recursos de execução.
- `src/test/java`: testes unitários de domínio, testes de aplicação e teste de integração de fluxos críticos.

## Bibliotecas externas principais

- **Spring Boot 4.0.0**: framework base da aplicação.
- **Spring Data JPA**: persistência ORM das entidades de aplicação.
- **Spring Mail**: envio de e-mails transacionais.
- **Vaadin 25**: camada de interface web.
- **MySQL Connector/J**: driver de execução.
- **H2**: banco em memória para testes.
- **Spring Boot Test**: suporte a testes unitários e de integração.

## Fluxos principais identificados

1. **Onboarding de usuário → personagem → mundo inicial**
   - `UserService.register(...)` cria usuário pendente.
   - `CharacterService.createCharacter(...)` cria personagem com baseline de atributos.
   - `WorldInstanceService.createWorldInstance(...)` cria instância individual de mundo do personagem.

2. **Sessão cooperativa**
   - Host cria sessão por `CoopSessionService.createSession(...)` com a própria instância de mundo.
   - Convidados entram por `joinSession(...)` sob validações de status/capacidade.
   - Sessão transita entre `LOBBY`, `IN_PROGRESS`, `PAUSED`, `CLOSED`.

3. **Ação de combate com persistência de estado**
   - Regras de cálculo em `domain.combat.CombatService`.
   - Resultado aplicado em `CharacterEntity` e persistido via `CharacterRepository`.

4. **Fluxos de segurança de conta**
   - Tentativas de login, lock temporário, código de desbloqueio.
   - Ativação/verificação de 2FA por e-mail OTP/TOTP.
   - Recuperação de senha por token com expiração.

## Dependências críticas e riscos de manutenção

- **Acoplamento entre serviços de aplicação**: `UserService` depende de `CharacterService` e vários repositórios; mudanças de regra podem gerar regressões transversais.
- **Alta densidade de responsabilidade** em `UserService`: autenticação, cadastro, segurança e lifecycle ficam concentrados no mesmo serviço.
- **Entidades com muitos relacionamentos indiretos** (`CharacterEntity`, `WorldInstanceEntity` e agregados de diário/reputação) elevam risco de inconsistência em exclusões/atualizações.
- **Enum e contratos de estado distribuídos** (ex.: status de sessão, marker types, quest states) exigem sincronização entre domínio, aplicação e banco.
- **Dependência de tempo real (`LocalDateTime.now()`)** dificulta previsibilidade de testes de expiração sem abstração de relógio.

## Hotspots (arquivos sensíveis a mudanças)

1. `src/main/java/br/eng/rodrigogml/mysteryrealms/application/user/service/UserService.java`
   - Núcleo de autenticação, sessão, segurança e integração com e-mail/2FA.
2. `src/main/java/br/eng/rodrigogml/mysteryrealms/application/character/service/CharacterService.java`
   - Orquestra criação/remoção de personagem e integrações cruzadas (mundo/co-op/diário).
3. `src/main/java/br/eng/rodrigogml/mysteryrealms/application/world/service/WorldInstanceService.java`
   - Consolida persistência do estado global do mundo por personagem.
4. `src/main/java/br/eng/rodrigogml/mysteryrealms/application/coop/service/CoopSessionService.java`
   - Regras de transição de estado multiplayer e consistência de participantes.
5. `src/main/java/br/eng/rodrigogml/mysteryrealms/domain/combat/service/CombatService.java`
   - Fórmulas críticas de resolução de dano e balanceamento.
6. `src/test/java/br/eng/rodrigogml/mysteryrealms/application/IntegrationCriticalFlowsTest.java`
   - Teste integrador que mapeia os fluxos críticos entre módulos.
