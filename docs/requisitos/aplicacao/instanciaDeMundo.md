# Requisitos de Aplicação — Instância de Mundo

Requisitos funcionais relacionados ao ciclo de vida e ao isolamento da instância de mundo vinculada a cada personagem no MysteryRealms.

---

## RF-IM-01 — Uma instância de mundo por personagem

- Cada personagem possui exatamente uma instância de mundo exclusiva.
- A instância de mundo registra o estado completo daquele mundo para aquele personagem, incluindo:
  - Estado de missões (iniciadas, concluídas, falhas, disponíveis).
  - Estado de NPCs (vivos, mortos, relação com o personagem, diálogos já realizados).
  - Estado de localidades (descobertas, bloqueadas, alteradas por evento).
  - Marcadores ativos (ver `sistemaSocial.md`).
  - Posição atual do personagem no mapa.
  - Estrutura temporal do mundo (dia/hora corrente na linha de tempo do mundo).

---

## RF-IM-02 — Estado inicial do mundo

- Ao criar um personagem, a instância de mundo é inicializada com o estado canônico inicial do mundo de Mystery Realms.
- Todos os NPCs, missões e localidades partem de seus valores de início de jogo.
- O estado inicial não pode ser influenciado por instâncias de outros personagens do mesmo usuário.

---

## RF-IM-03 — Persistência do estado do mundo

- Ao iniciar uma sessão de jogo, a instância de mundo do personagem selecionado deve ser carregada integralmente na memória do servidor de jogo.
- O estado em memória é a fonte de verdade durante a sessão ativa; todas as leituras de estado do mundo durante a sessão devem ocorrer a partir dessa representação em memória.
- A cada ação do usuário que altere o estado do mundo (movimento, combate, diálogo, conclusão de missão, morte de NPC, descoberta de localidade, coleta de item, passagem de tempo etc.), o estado em memória deve ser persistido automaticamente no banco de dados, sem intervenção do usuário.
- A persistência pode ser realizada de forma parcial e incremental, gravando apenas os fragmentos do estado que foram modificados pela ação, de forma a minimizar a carga sobre o ORM e o banco de dados.
- O detalhamento da estratégia de persistência incremental (identificação de fragmentos modificados, granularidade de commit, estratégias de cache, latência aceitável etc.) deve ser especificado nos **Requisitos de Persistência** (a elaborar).
- O sistema não deve depender de save manual para preservar o estado do mundo.
- Em caso de falha de persistência, o sistema deve:
  - Registrar o erro internamente.
  - Tentar reprocessar a gravação automaticamente.
  - Notificar o administrador do sistema em caso de falha persistente.

---

## RF-IM-04 — Isolamento entre instâncias

- Nenhuma alteração no mundo de um personagem deve afetar o mundo de outro personagem, mesmo que ambos pertençam ao mesmo usuário.
- O isolamento se aplica integralmente, incluindo durante sessões co-op (ver `multijogador.md`).

---

## RF-IM-05 — Consulta do estado do mundo

- O sistema deve permitir que a lógica do jogo consulte o estado atual da instância de mundo do personagem ativo para:
  - Determinar disponibilidade de missões e diálogos.
  - Renderizar estado de localidades e NPCs.
  - Aplicar consequências de eventos anteriores.
