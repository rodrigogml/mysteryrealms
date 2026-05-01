# Requisitos de Aplicação — Usuários

Requisitos funcionais relacionados ao cadastro, autenticação e gerenciamento de contas de usuário na plataforma MysteryRealms.

---

## RF-UA-01 — Cadastro de usuário

- O sistema deve permitir o cadastro de novos usuários mediante informação de:
  - Nome de usuário (único na plataforma, sem espaços, mínimo 3 caracteres).
  - Endereço de e-mail válido (único na plataforma).
  - Senha (mínimo 8 caracteres, contendo ao menos uma letra maiúscula, uma minúscula e um caractere numérico ou especial).
- O sistema deve enviar um link de confirmação ao endereço de e-mail cadastrado imediatamente após o cadastro.
- A conta só é ativada após a confirmação do e-mail por meio do link recebido.
- O link de confirmação expira em **48 horas**. Após a expiração, a conta não confirmada é removida automaticamente.
- Enquanto não confirmada, a conta não pode ser usada para login.

---

## RF-UA-02 — Autenticação

- O sistema deve permitir o login com e-mail e senha.
- O sistema deve manter a sessão autenticada por meio de token com expiração de **24 horas**, renovável automaticamente enquanto o usuário estiver ativo.
- O sistema deve encerrar a sessão ao expirar o token ou por solicitação explícita do usuário (logout).

### Proteção contra força bruta

- O sistema deve registrar tentativas de login com senha incorreta por conta e por dispositivo/IP.
- Após **5 tentativas consecutivas com senha incorreta** para a mesma conta, o acesso deve ser bloqueado temporariamente.
- O desbloqueio pode ser feito de duas formas:
  1. **Automaticamente**, após um período de espera de **30 minutos**.
  2. **Imediatamente**, por meio de código numérico de 6 dígitos enviado ao e-mail cadastrado. O código expira em **15 minutos** e é de uso único.
- O sistema deve notificar o usuário por e-mail quando um bloqueio por tentativas for acionado, informando o endereço IP da tentativa.

### Mensagens/erros esperados (sessão e bloqueio)

- `user.error.sessionExpired`: sessão encontrada, porém com prazo de expiração ultrapassado; deve ser invalidada imediatamente.
- `user.error.tokenNotFound`: token de sessão inexistente ou já invalidado por logout.
- `user.error.passwordMismatch`: tentativa de login com senha inválida sem atingir limiar de bloqueio.
- `user.error.accountLocked`: conta bloqueada após atingir o limiar de 5 falhas consecutivas.
- `user.error.unlockCodeNotFound`: ausência de bloqueio ativo elegível para desbloqueio por código, ou ausência de código válido.
- `user.error.unlockCodeInvalid`: código de desbloqueio informado não confere com o último código ativo.
- `user.error.tokenExpired`: código de desbloqueio encontrado, porém expirado.

---

## RF-UA-03 — Recuperação de senha

- O sistema deve permitir a solicitação de redefinição de senha por meio do e-mail cadastrado, mesmo com a conta bloqueada por tentativas.
- O link de redefinição deve expirar em **1 hora** após o envio.
- O sistema deve invalidar todos os tokens de sessão ativos ao concluir a redefinição de senha.
- Ao utilizar o link de redefinição, qualquer bloqueio por tentativas de login deve ser removido automaticamente.

---

## RF-UA-04 — Perfil do usuário

- Cada usuário possui um perfil com:
  - Nome de usuário (exibido publicamente).
  - E-mail (privado, visível apenas ao próprio usuário).
  - Data de criação da conta.
  - Lista de personagens criados.
  - Status de 2FA (ativo/inativo e método configurado).
- O usuário deve poder alterar o nome de usuário (sujeito à verificação de unicidade) e a senha.
- A alteração de senha exige a confirmação da senha atual.
- O usuário não deve poder alterar o e-mail sem verificação do novo endereço:
  - Um link de confirmação é enviado ao novo e-mail.
  - Enquanto não confirmado, o e-mail atual permanece ativo.
  - O link expira em **24 horas**.

---

## RF-UA-05 — Exclusão de conta

- O usuário deve poder solicitar a exclusão permanente da sua conta.
- A exclusão deve remover permanentemente todos os personagens e instâncias de mundo associados.
- O sistema deve exigir confirmação por e-mail antes de executar a exclusão (link de confirmação com expiração de **24 horas**).
- Contas com 2FA ativo devem exigir validação do segundo fator antes de permitir a solicitação de exclusão.

---

## RF-UA-06 — Autenticação de dois fatores (2FA)

- O sistema deve oferecer autenticação de dois fatores como recurso opcional, habilitável pelo usuário nas configurações da conta.
- Quando habilitado, o segundo fator é exigido **em todo login**, após a verificação de e-mail e senha.
- O sistema deve suportar dois métodos de segundo fator:

### Método 1: E-mail (OTP)

- Um código numérico de **6 dígitos** é enviado ao e-mail cadastrado após a validação da senha.
- O código expira em **10 minutos** e é de uso único.
- O sistema deve invalidar códigos anteriores ao gerar um novo código para o mesmo usuário.

### Método 2: App autenticador (TOTP)

- O sistema deve suportar o padrão **TOTP (RFC 6238)**, compatível com apps como Google Authenticator, Authy e similares.
- O processo de ativação deve gerar um QR Code e a chave secreta textual para registro no app.
- O código gerado pelo app é válido por **30 segundos** (janela de tolerância de 1 intervalo anterior/posterior permitida).
- Apenas um método TOTP pode estar ativo por vez por conta.

### Códigos de recuperação

- Ao ativar o 2FA (por qualquer método), o sistema deve gerar **10 códigos de recuperação** de uso único.
- Esses códigos devem ser apresentados uma única vez ao usuário, com instrução de armazená-los com segurança.
- Cada código de recuperação pode ser usado no lugar do segundo fator em caso de perda de acesso ao método configurado.
- Ao usar um código de recuperação, ele é invalidado imediatamente.
- O usuário pode regenerar todos os códigos de recuperação nas configurações da conta (ação invalida os códigos anteriores).

### Desativação do 2FA

- O usuário pode desativar o 2FA nas configurações da conta.
- A desativação exige a validação do segundo fator ativo no momento.
- Ao desativar, todos os códigos de recuperação associados são invalidados.

### Notificações de segurança

- O sistema deve enviar notificações por e-mail nas seguintes situações:
  - Ativação ou desativação do 2FA.
  - Login bem-sucedido a partir de um dispositivo/IP não reconhecido.
  - Bloqueio de conta por tentativas de login.
  - Alteração de senha.
  - Alteração de e-mail.
  - Solicitação de exclusão de conta.
