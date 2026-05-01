# Backlog Técnico por Módulo

## Objetivo
Organizar épicos técnicos de curto e médio prazo para acelerar a evolução do sistema, com foco em previsibilidade de entrega, redução de risco e incremento de cobertura automatizada.

## Escala usada
- **Prioridade**: P0 (crítico), P1 (alto), P2 (médio).
- **Esforço estimado**: PP (1–2 dias), P (3–5 dias), M (1–2 semanas), G (3+ semanas).

## Módulo: Usuários e Segurança

### Épico 1 — Fortalecer trilha de autenticação e sessão
| Item | Problema | Impacto | Prioridade | Esforço | Dependências | Classes/arquivos iniciais |
|---|---|---|---|---|---|---|
| UA-01 | Regras de expiração/invalidação de sessão não estão explicitamente cobertas por testes de borda. | Risco de sessão indevida e regressão em login/logout. | P0 | M | Modelo de sessão e repositório de sessão estáveis. | `src/main/java/br/eng/rodrigogml/mysteryrealms/application/user/service/UserService.java`, `src/main/java/br/eng/rodrigogml/mysteryrealms/application/user/repository/SessionRepository.java`, `src/test/java/br/eng/rodrigogml/mysteryrealms/application/user/service/UserServiceTest.java` |
| UA-02 | Fluxos de bloqueio/desbloqueio de conta e tentativas de login podem divergir entre serviços e persistência. | Reduz segurança e gera suporte manual por falsos bloqueios. | P0 | M | Definição funcional consolidada em requisitos de usuários. | `src/main/java/br/eng/rodrigogml/mysteryrealms/application/user/repository/AccountLockRepository.java`, `src/main/java/br/eng/rodrigogml/mysteryrealms/application/user/repository/LoginAttemptRepository.java`, `src/main/java/br/eng/rodrigogml/mysteryrealms/application/user/entity/AccountLockEntity.java` |
| UA-03 | Cobertura de cenários de falha de entrega de e-mail (OTP/recuperação) é limitada. | Aumenta taxa de abandono em autenticação e recuperação de senha. | P1 | P | Infra de envio de e-mail e mensagens i18n. | `src/main/java/br/eng/rodrigogml/mysteryrealms/application/user/service/EmailService.java`, `src/test/java/br/eng/rodrigogml/mysteryrealms/application/user/service/EmailServiceTest.java`, `src/main/resources/messages.properties` |

**Critérios de aceite do épico**
- Testes automatizados cobrindo sucesso, falha e bordas de sessão, bloqueio e recuperação passam em pipeline.
- Comportamento esperado: sessão expirada é invalidada; tentativas excessivas bloqueiam conforme regra; desbloqueio restaura autenticação normal.
- Documentação atualizada com fluxos, exceções e matriz de erros em `/docs/requisitos/aplicacao/usuarios.md`.

## Módulo: Personagens e Progressão

### Épico 2 — Consistência de atributos derivados e progressão
| Item | Problema | Impacto | Prioridade | Esforço | Dependências | Classes/arquivos iniciais |
|---|---|---|---|---|---|---|
| PP-01 | Cálculo de atributos derivados e bônus pode apresentar divergência entre domínio e aplicação. | Inconsistência percebida pelo jogador e bugs de balanceamento. | P0 | M | Requisitos de ficha e progressão congelados. | `src/main/java/br/eng/rodrigogml/mysteryrealms/domain/character/service/CharacterAttributeService.java`, `src/main/java/br/eng/rodrigogml/mysteryrealms/domain/character/model/AttributeSet.java`, `src/test/java/br/eng/rodrigogml/mysteryrealms/domain/character/service/CharacterAttributeServiceTest.java` |
| PP-02 | Regras de nível/XP e concessão de pontos precisam de testes de regressão por marcos. | Evita quebra silenciosa de progressão em refactors. | P0 | P | Definição de curva de XP vigente. | `src/main/java/br/eng/rodrigogml/mysteryrealms/domain/character/service/ProgressionService.java`, `src/test/java/br/eng/rodrigogml/mysteryrealms/domain/character/service/ProgressionServiceTest.java` |
| PP-03 | Integração entre `CharacterService` e persistência de reputação/itens carece de cenários completos. | Pode gerar estado parcial do personagem. | P1 | M | Mapeamentos JPA das entidades de personagem. | `src/main/java/br/eng/rodrigogml/mysteryrealms/application/character/service/CharacterService.java`, `src/main/java/br/eng/rodrigogml/mysteryrealms/application/character/entity/CharacterEntity.java`, `src/test/java/br/eng/rodrigogml/mysteryrealms/application/character/service/CharacterServiceTest.java` |

**Critérios de aceite do épico**
- Testes unitários e de serviço validam marcos de XP, arredondamento e distribuição de pontos.
- Comportamento esperado: cálculos determinísticos para o mesmo estado de entrada e persistência transacional íntegra.
- Documentação sincronizada com exemplos numéricos e casos de borda em `/docs/requisitos/mecanica/fichaDoPersonagem.md` e `/docs/requisitos/mecanica/progressaoDoPersonagem.md`.

## Módulo: Instância de Mundo e Navegação

### Épico 3 — Robustez de estado de mundo por personagem
| Item | Problema | Impacto | Prioridade | Esforço | Dependências | Classes/arquivos iniciais |
|---|---|---|---|---|---|---|
| WM-01 | Criação e carga de instância de mundo podem não validar todos os estados relacionados (marcadores, quests, NPCs). | Risco de inconsistência entre sessões e perda de progresso percebida. | P0 | G | Contratos de estado de mundo definidos nos requisitos. | `src/main/java/br/eng/rodrigogml/mysteryrealms/application/world/service/WorldInstanceService.java`, `src/main/java/br/eng/rodrigogml/mysteryrealms/application/world/entity/WorldInstanceEntity.java`, `src/test/java/br/eng/rodrigogml/mysteryrealms/application/world/service/WorldInstanceServiceTest.java` |
| WM-02 | Validação hierárquica e navegação têm dependências implícitas pouco testadas em conjunto. | Erros de movimentação e travamentos de fluxo de exploração. | P1 | M | Coerência entre modelos de mundo e regras de navegação. | `src/main/java/br/eng/rodrigogml/mysteryrealms/domain/world/service/HierarchyValidationService.java`, `src/main/java/br/eng/rodrigogml/mysteryrealms/domain/world/service/NavigationService.java`, `src/test/java/br/eng/rodrigogml/mysteryrealms/domain/world/WorldNavigationTest.java` |

**Critérios de aceite do épico**
- Testes de integração validam bootstrap completo da instância de mundo e isolamento entre personagens.
- Comportamento esperado: navegação respeita conexões válidas, tempo avança corretamente e estados são persistidos sem contaminação cruzada.
- Documentação de fluxos de criação/consulta de instância atualizada em `/docs/requisitos/aplicacao/instanciaDeMundo.md` e `/docs/requisitos/mecanica/mundoENavegacao.md`.

## Módulo: Combate, Modificadores e Fisiologia

### Épico 4 — Pipeline determinístico de resolução de turno
| Item | Problema | Impacto | Prioridade | Esforço | Dependências | Classes/arquivos iniciais |
|---|---|---|---|---|---|---|
| CF-01 | Encadeamento de cálculo (acerto, bloqueio, resistência, aflição) é complexo e suscetível a regressão. | Impacta diretamente o núcleo de gameplay. | P0 | G | Definições canônicas de combate e tipos de dano/aflição. | `src/main/java/br/eng/rodrigogml/mysteryrealms/domain/combat/service/CombatService.java`, `src/main/java/br/eng/rodrigogml/mysteryrealms/domain/combat/model/DiceRoller.java`, `src/test/java/br/eng/rodrigogml/mysteryrealms/domain/combat/service/CombatServiceTest.java` |
| CF-02 | Interação entre modificadores e custo de ação carece de matriz de conflitos completa. | Ações podem custar incorretamente e quebrar estratégia do jogador. | P1 | M | Catálogo de ações e regras de empilhamento. | `src/main/java/br/eng/rodrigogml/mysteryrealms/domain/modifier/service/ModifierService.java`, `src/main/java/br/eng/rodrigogml/mysteryrealms/domain/modifier/service/ActionCostService.java`, `src/test/java/br/eng/rodrigogml/mysteryrealms/domain/modifier/ModifierTest.java` |
| CF-03 | Resolução fisiológica por tick e precedência de estados precisa de cenários de carga combinada. | Falhas podem causar desmaio/morte fora das regras esperadas. | P1 | M | Ordem de precedência de estados definida. | `src/main/java/br/eng/rodrigogml/mysteryrealms/domain/physiology/service/PhysiologyService.java`, `src/main/java/br/eng/rodrigogml/mysteryrealms/domain/physiology/model/PhysiologyState.java`, `src/test/java/br/eng/rodrigogml/mysteryrealms/domain/physiology/service/PhysiologyServiceTest.java` |

**Critérios de aceite do épico**
- Testes determinísticos com sementes controladas para cenários de combate e estados fisiológicos.
- Comportamento esperado: mesma entrada gera mesma saída; precedência de regras segue requisitos sem efeitos colaterais.
- Documentação técnica com tabela de ordem de execução e exemplos de turno atualizada em `/docs/requisitos/mecanica/combateETestes.md`, `/docs/requisitos/mecanica/modificadoresAflicoesResistencias.md` e `/docs/requisitos/mecanica/estadoFisiologico.md`.

## Módulo: Sistema Social e Diário

### Épico 5 — Integridade de impacto social e rastreabilidade narrativa
| Item | Problema | Impacto | Prioridade | Esforço | Dependências | Classes/arquivos iniciais |
|---|---|---|---|---|---|---|
| SS-01 | Atualizações de relacionamento, reputação e marcadores podem ficar inconsistentes entre memória e banco. | Narrativa perde coerência e reduz confiança do jogador. | P0 | M | Modelos de diário e impacto social estáveis. | `src/main/java/br/eng/rodrigogml/mysteryrealms/domain/social/service/SocialService.java`, `src/main/java/br/eng/rodrigogml/mysteryrealms/application/world/entity/DiaryImpactRelationshipEntity.java`, `src/main/java/br/eng/rodrigogml/mysteryrealms/application/world/entity/DiaryImpactReputationEntity.java` |
| SS-02 | Falta de testes de ponta a ponta do ciclo social + escrita de diário por decisão. | Dificulta evolução de conteúdo de diálogo com segurança. | P1 | M | Nó de diálogo e efeitos sociais mapeados. | `src/main/java/br/eng/rodrigogml/mysteryrealms/domain/social/model/DialogNode.java`, `src/main/java/br/eng/rodrigogml/mysteryrealms/domain/social/model/DiaryEntry.java`, `src/test/java/br/eng/rodrigogml/mysteryrealms/domain/social/SocialSystemTest.java` |

**Critérios de aceite do épico**
- Testes validam alteração de relacionamento/reputação e geração de diário para sucesso e falha social.
- Comportamento esperado: cada escolha de diálogo aplica impactos previstos e persiste histórico auditável.
- Documentação de integrações obrigatórias atualizada em `/docs/requisitos/mecanica/sistemaSocial.md`.

## Módulo: Economia e Inventário

### Épico 6 — Consistência de itens, preço e equipamento
| Item | Problema | Impacto | Prioridade | Esforço | Dependências | Classes/arquivos iniciais |
|---|---|---|---|---|---|---|
| EI-01 | Regras de precificação e arredondamento podem divergir entre domínio e persistência de item. | Economia do jogo fica instável e sujeita a exploração. | P1 | P | Tabela de moedas e regras de preço vigentes. | `src/main/java/br/eng/rodrigogml/mysteryrealms/domain/economy/service/PricingService.java`, `src/main/java/br/eng/rodrigogml/mysteryrealms/domain/economy/model/MonetaryValue.java`, `src/test/java/br/eng/rodrigogml/mysteryrealms/domain/economy/EconomyTest.java` |
| EI-02 | Validação de combinação de equipamentos em mãos pode não cobrir todos os tipos de arma/escudo. | Jogador pode equipar combinações inválidas. | P1 | M | Modelagem de `WeaponType` e `Shield` finalizada. | `src/main/java/br/eng/rodrigogml/mysteryrealms/domain/economy/model/EquippedHands.java`, `src/main/java/br/eng/rodrigogml/mysteryrealms/domain/economy/model/Weapon.java`, `src/main/java/br/eng/rodrigogml/mysteryrealms/domain/economy/model/Shield.java` |

**Critérios de aceite do épico**
- Testes validam arredondamento monetário, limites de preço e restrições de equipamento por categoria/subtipo.
- Comportamento esperado: transações preservam integridade monetária e bloqueiam combinações inválidas de itens de mão.
- Documentação de contratos de item e equipamento atualizada em `/docs/requisitos/mecanica/economiaEInventario.md`.

## Sequência sugerida de execução
1. P0 de segurança e sessão (UA-01, UA-02).
2. P0 de progressão e combate (PP-01, PP-02, CF-01).
3. P0 de instância de mundo e social (WM-01, SS-01).
4. P1 remanescentes por módulo, priorizando os de maior dependência cruzada.
