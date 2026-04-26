# Requisitos de Aplicação — Usuários

Requisitos funcionais relacionados ao cadastro, autenticação e gerenciamento de contas de usuário na plataforma MysteryRealms.

---

## RF-UA-01 — Cadastro de usuário

- O sistema deve permitir o cadastro de novos usuários mediante informação de:
  - Nome de usuário (único na plataforma, sem espaços, mínimo 3 caracteres).
  - Endereço de e-mail válido (único na plataforma).
  - Senha (mínimo 8 caracteres).
- O sistema deve validar o endereço de e-mail por meio de link de confirmação enviado ao endereço cadastrado antes de ativar a conta.
- Contas não confirmadas dentro de 48 horas devem ser removidas automaticamente.

---

## RF-UA-02 — Autenticação

- O sistema deve permitir o login com e-mail e senha.
- O sistema deve manter a sessão autenticada por meio de token com expiração configurável (padrão: 24 horas com renovação automática em atividade).
- O sistema deve encerrar a sessão ao expirar o token ou por solicitação explícita do usuário (logout).
- O sistema deve bloquear temporariamente o acesso após 5 tentativas de login com senha incorreta (tempo de bloqueio: a definir).

---

## RF-UA-03 — Recuperação de senha

- O sistema deve permitir a solicitação de redefinição de senha por meio do e-mail cadastrado.
- O link de redefinição deve expirar em 1 hora após o envio.
- O sistema deve invalidar todos os tokens de sessão ativos ao concluir a redefinição de senha.

---

## RF-UA-04 — Perfil do usuário

- Cada usuário possui um perfil com:
  - Nome de usuário (exibido publicamente).
  - E-mail (privado, visível apenas ao próprio usuário).
  - Data de criação da conta.
  - Lista de personagens criados.
- O usuário deve poder alterar o nome de usuário (sujeito à verificação de unicidade) e a senha.
- O usuário não deve poder alterar o e-mail sem verificação do novo endereço.

---

## RF-UA-05 — Exclusão de conta

- O usuário deve poder solicitar a exclusão permanente da sua conta.
- A exclusão deve remover todos os personagens e instâncias de mundo associados.
- O sistema deve exigir confirmação por e-mail antes de executar a exclusão.
