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

- O estado da instância de mundo deve ser persistido automaticamente a cada ação relevante que altere o mundo (conclusão de missão, morte de NPC, descoberta de localidade etc.).
- O sistema não deve depender de save manual para preservar o estado do mundo.

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
