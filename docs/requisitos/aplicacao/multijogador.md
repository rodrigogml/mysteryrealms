# Requisitos de Aplicação — Multijogador

Requisitos funcionais relacionados ao modo de jogo multi-usuário cooperativo (co-op) no MysteryRealms.

---

## RF-MJ-01 — Modo solo

- Por padrão, ao iniciar uma sessão de jogo, o personagem entra no seu próprio mundo de forma individual (modo solo).
- Em modo solo, somente o dono do personagem está presente no mundo.

---

## RF-MJ-02 — Convite de jogadores (co-op)

- O dono de uma sessão (chamado de **anfitrião**) pode convidar outros usuários para se juntarem ao seu mundo em modo co-op.
- O convite deve ser enviado por nome de usuário do convidado.
- O convidado deve aceitar o convite para ingressar na sessão.
- O sistema deve permitir ao anfitrião remover jogadores convidados da sua sessão a qualquer momento.
- O anfitrião pode encerrar a sessão co-op, retornando todos os convidados ao estado de desconectado.

---

## RF-MJ-03 — Regras do jogador convidado

- O jogador convidado entra no mundo do anfitrião com o **seu próprio personagem** (inventário, atributos, XP, equipamentos).
- Dentro do mundo do anfitrião, o convidado está sujeito ao estado daquele mundo:
  - NPCs no estado definido pelo mundo do anfitrião.
  - Missões no estado definido pelo mundo do anfitrião.
  - Condições climáticas, temporais e de localidade do mundo do anfitrião.
- O convidado pode interagir com o mundo do anfitrião (combater NPCs, dialogar, explorar) conforme as regras normais do jogo.

---

## RF-MJ-04 — Persistência de dados pessoais do convidado

- Alterações nos dados pessoais do personagem convidado devem ser salvas no personagem do convidado, independentemente de onde ocorreu a sessão:
  - Inventário (itens adquiridos ou perdidos).
  - Equipamentos (itens equipados ou desequipados).
  - Moedas (ganhas ou gastas).
  - XP acumulado.
  - Atributos e progressão de nível.
  - Estado fisiológico (fadiga, fome, sede, PV).
- Esses dados são de propriedade do personagem do convidado e acompanham-no para qualquer sessão.

---

## RF-MJ-05 — Isolamento da progressão de mundo do convidado

- Evoluções ocorridas no mundo do anfitrião **não são transferidas** para o mundo do personagem do convidado.
- Ao retornar ao seu próprio mundo, o personagem do convidado encontrará:
  - As mesmas missões no estado em que estavam antes de entrar na sessão co-op.
  - Os mesmos NPCs no estado definido pelo seu próprio mundo.
  - A mesma posição de mapa (ou última posição salva no seu mundo).
- A única exceção são os dados pessoais do personagem (RF-MJ-04), que são preservados.

---

## RF-MJ-06 — Limite de jogadores por sessão

- O limite máximo de jogadores simultâneos em uma sessão co-op é: **a definir** (sugestão: 4 jogadores incluindo o anfitrião).

---

## RF-MJ-07 — Desconexão durante sessão co-op

- Em caso de desconexão inesperada de um jogador convidado, os dados pessoais do personagem devem ser salvos no ponto da última ação sincronizada.
- A sessão do anfitrião deve continuar normalmente sem o jogador desconectado.
- O jogador convidado pode solicitar reingressar na sessão do anfitrião, se a sessão ainda estiver ativa.
