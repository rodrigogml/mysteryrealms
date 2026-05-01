# Roadmap do Projeto MysteryRealms

## Objetivo
Converter os objetivos funcionais e técnicos atuais do projeto em milestones curtas (2 a 4 semanas), com escopo explícito, critério de pronto, riscos, dependências e indicadores de progresso.

## Backlog agrupado por épico

### Épico A — Segurança e Gestão de Usuários
**Objetivo do épico:** estabilizar os fluxos de autenticação, sessão, recuperação e segurança de conta.

| ID | Tarefa | Prioridade | Dependências |
|---|---|---|---|
| A1 | Cobrir expiração, renovação e invalidação de sessão (login/logout/timeout) | Must | RF-UA-02 |
| A2 | Consolidar bloqueio por tentativas, desbloqueio automático e por código | Must | A1, RF-UA-02 |
| A3 | Cobrir recuperação de senha com invalidação de sessões ativas | Must | A1, RF-UA-03 |
| A4 | Validar cenários de falha de envio de e-mail (OTP, recuperação, confirmação) | Should | A1, infraestrutura de e-mail |
| A5 | Fortalecer fluxo de 2FA (OTP/TOTP + códigos de recuperação) com testes de borda | Should | A2, RF-UA-06 |

### Épico B — Personagens e Progressão
**Objetivo do épico:** garantir determinismo e consistência dos cálculos centrais de evolução.

| ID | Tarefa | Prioridade | Dependências |
|---|---|---|---|
| B1 | Validar cálculo de atributos derivados e bônus por classe/raça/estado | Must | RF-FP-02, RF-FP-06 |
| B2 | Cobrir curva de XP, arredondamentos e marcos de nível | Must | B1, RF-PP-02 a RF-PP-07 |
| B3 | Garantir integridade transacional em criação/atualização de personagem | Should | B1, RF-PE-02 |
| B4 | Cobrir cenários de consistência entre inventário, reputação e ficha | Could | B3, RF-FP-05, RF-FP-09 |

### Épico C — Mundo, Instância e Navegação
**Objetivo do épico:** assegurar isolamento da progressão por personagem e robustez da navegação.

| ID | Tarefa | Prioridade | Dependências |
|---|---|---|---|
| C1 | Validar bootstrap completo da instância (quests, marcadores, NPCs, localização) | Must | RF-IM-01 a RF-IM-03 |
| C2 | Garantir isolamento entre instâncias de personagens diferentes | Must | C1, RF-IM-04 |
| C3 | Cobrir navegação com validação hierárquica e resolução de destino | Should | C1, RF-MN-06, RF-MN-08 |
| C4 | Cobrir avanço temporal e atualização fisiológica por deslocamento | Should | C3, RF-MN-10 a RF-MN-13 |

### Épico D — Combate, Modificadores e Fisiologia
**Objetivo do épico:** tornar o pipeline de combate previsível, auditável e aderente aos requisitos.

| ID | Tarefa | Prioridade | Dependências |
|---|---|---|---|
| D1 | Garantir pipeline completo: acerto → bloqueio → resistência → aflição | Must | RF-CT-07 a RF-CT-11 |
| D2 | Cobrir empilhamento de modificadores e custo de ação por contexto | Must | D1, RF-MAR-04 a RF-MAR-09 |
| D3 | Cobrir precedência fisiológica no tick e efeitos combinados | Should | D2, RF-EF-05, RF-EF-06, RF-EF-16 |
| D4 | Validar cenários de fuga, ação preparada e iniciativa com regressão | Could | D1, RF-CT-04, RF-CT-12, RF-CT-13 |

### Épico E — Sistema Social, Diário e Economia
**Objetivo do épico:** fechar consistência entre impactos narrativos, reputação e recursos do jogador.

| ID | Tarefa | Prioridade | Dependências |
|---|---|---|---|
| E1 | Garantir persistência consistente de relacionamento/reputação/marcadores | Must | RF-SS-06 a RF-SS-09 |
| E2 | Cobrir ciclo social ponta a ponta com escrita no diário | Should | E1, RF-SS-02, RF-SS-08 |
| E3 | Cobrir precificação e arredondamento monetário com casos extremos | Should | RF-EI-01, RF-EI-02 |
| E4 | Cobrir validação de combinação de equipamentos em mãos | Could | E3, RF-EI-05 a RF-EI-08 |

## Dependências entre tarefas (visão consolidada)

1. **Base de segurança antes de coop e mundo compartilhado:** A1/A2 desbloqueiam confiança de sessão para fluxos de personagem e multijogador.
2. **Determinismo de personagem antes de balanceamento avançado:** B1/B2 devem preceder D1/D2 para evitar combate calibrado sobre atributos instáveis.
3. **Instância sólida antes de exploração profunda:** C1/C2 devem preceder C3/C4 para impedir navegação sobre estado inconsistente.
4. **Pipeline de combate antes de cenários sociais/econômicos integrados:** D1/D2 antecedem E2/E3 quando houver efeitos cruzados por combate.
5. **Persistência social antes de expansão de conteúdo narrativo:** E1 antecede E2 para garantir rastreabilidade.

## Milestones (2 a 4 semanas)

## Milestone 1 (Semanas 1–2) — Fundação de Segurança e Progressão
**Escopo fechado**
- A1, A2, A3
- B1, B2

**Critério de pronto**
- Fluxos de autenticação e recuperação cobertos com testes de sucesso, falha e borda.
- Cálculos de atributos e progressão determinísticos para mesma entrada.
- Sem regressão nos testes atuais do módulo de usuário e personagem.

**Riscos principais**
- Ambiguidade em regras de sessão ativa vs. token renovável.
- Acoplamento entre serviços de usuário e persistência elevando esforço real.

**Indicadores de progresso**
- `% testes críticos de usuário passando` (meta: 100%).
- `% cenários de progressão por marco de nível cobertos` (meta: 100%).
- `Defeitos críticos abertos de segurança` (meta: 0 ao fim).

## Milestone 2 (Semanas 3–4) — Estado de Mundo e Navegação Confiáveis
**Escopo fechado**
- C1, C2, C3
- A4 (em paralelo, se infraestrutura permitir)

**Critério de pronto**
- Instância criada de forma íntegra para novo personagem.
- Isolamento comprovado entre personagens distintos.
- Navegação bloqueia destinos inválidos e resolve destinos válidos corretamente.

**Riscos principais**
- Dados de seed/modelagem incompletos para estados de mundo complexos.
- Dependência indireta de regras sociais/combate em certos fluxos de movimentação.

**Indicadores de progresso**
- `% testes de isolamento de instância passando` (meta: 100%).
- `Taxa de falha em criação de instância em testes` (meta: 0%).
- `% cenários de navegação hierárquica cobertos` (meta: >= 90%).

## Milestone 3 (Semanas 5–8) — Núcleo de Combate Determinístico
**Escopo fechado**
- D1, D2, D3
- C4

**Critério de pronto**
- Pipeline de combate executa em ordem canônica com saídas reproduzíveis.
- Modificadores e custos de ação respeitam regras de empilhamento e contexto.
- Atualização fisiológica por tick e por deslocamento sem inconsistências.

**Riscos principais**
- Explosão combinatória de cenários de combate.
- Divergência entre regras documentadas e implementação legada.

**Indicadores de progresso**
- `% cenários de pipeline de combate com assertiva completa` (meta: >= 95%).
- `Nº de flakies em testes determinísticos` (meta: 0).
- `% regras de precedência fisiológica cobertas` (meta: 100%).

## Milestone 4 (Semanas 9–10) — Coerência Social e Econômica
**Escopo fechado**
- E1, E2, E3, E4
- B3 (hardening transacional final)

**Critério de pronto**
- Impactos sociais persistidos com rastreabilidade por decisão.
- Diário registra eventos sociais relevantes de forma auditável.
- Regras de economia e equipamento bloqueiam estados inválidos.

**Riscos principais**
- Inconsistência entre entidades de impacto social.
- Lacunas de casos extremos em conversão monetária e equipamento dual.

**Indicadores de progresso**
- `% decisões de diálogo com impacto persistido corretamente` (meta: >= 95%).
- `% regras de equipamento em mãos cobertas` (meta: 100%).
- `Incidentes de inconsistência narrativa/econômica` (meta: 0 críticos).

## Observações de priorização
- **Must** compõe o caminho crítico para estabilidade do produto.
- **Should** entra no mesmo milestone quando não compromete o caminho crítico.
- **Could** é executado após cumprimento dos critérios de pronto de cada milestone.
