# Requisitos de Aplicação — Personagens

Requisitos funcionais relacionados à criação, gerenciamento e seleção de personagens por um usuário na plataforma MysteryRealms.

---

## RF-PE-01 — Múltiplos personagens por usuário

- Um usuário pode criar e manter múltiplos personagens simultaneamente.
- O limite máximo de personagens por usuário é **50**.
- Cada personagem é independente dos demais: atributos, inventário, moedas, XP e progressão não são compartilhados.

---

## RF-PE-02 — Criação de personagem

- O sistema deve permitir ao usuário criar um novo personagem informando:
  - Nome do personagem (obrigatório, único por usuário).
  - Raça (seleção a partir das raças jogáveis definidas em `fichaDoPersonagem.md`).
  - Classe (seleção a partir das classes jogáveis definidas em `fichaDoPersonagem.md`).
- Ao criar o personagem, o sistema deve:
  - Inicializar a ficha do personagem com os valores base da raça e classe selecionadas.
  - Criar uma nova instância de mundo vinculada a esse personagem (ver `instanciaDeMundo.md`).

---

## RF-PE-03 — Listagem e seleção de personagem

- A tela inicial pós-login deve exibir a lista de personagens do usuário com:
  - Nome, raça, classe e nível.
  - Data do último acesso com aquele personagem.
- O usuário deve selecionar um personagem para iniciar ou retomar uma sessão de jogo.

---

## RF-PE-04 — Renomeação de personagem

- O usuário deve poder renomear um personagem existente.
- O novo nome deve ser único dentro dos personagens do mesmo usuário.

---

## RF-PE-05 — Exclusão de personagem

- O usuário deve poder excluir um personagem.
- A exclusão deve remover permanentemente a ficha do personagem e a sua instância de mundo associada.
- O sistema deve exigir confirmação explícita antes de executar a exclusão.
