# MysteryRealms

## Configuração

O carregamento de configuração segue um fluxo único e previsível:

1. O Spring Boot lê `src/main/resources/application.properties`.
2. Cada propriedade usa variáveis de ambiente `MYSTERYREALMS_*` com fallback padrão.
3. As propriedades de domínio da aplicação são centralizadas e validadas em `MysteryRealmsProperties` (`prefix=mysteryrealms`).

### Variáveis obrigatórias

- `MYSTERYREALMS_MAIL_FROM`: remetente usado nos e-mails transacionais.
- `MYSTERYREALMS_PUBLIC_BASE_URL`: URL pública para geração de links de confirmação.

> Se estiver ausente ou inválida, a aplicação falha na inicialização por validação.

### Variáveis opcionais

#### Banco de dados

- `MYSTERYREALMS_DATASOURCE_URL` (padrão: `jdbc:mysql://localhost:3306/mysteryrealms?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC`)
- `MYSTERYREALMS_DATASOURCE_USERNAME` (padrão: `mysteryrealms`)
- `MYSTERYREALMS_DATASOURCE_PASSWORD` (padrão: `mysteryrealms`)

#### E-mail (SMTP)

- `MYSTERYREALMS_MAIL_HOST` (padrão: `localhost`)
- `MYSTERYREALMS_MAIL_PORT` (padrão: `587`)
- `MYSTERYREALMS_MAIL_USERNAME` (padrão: vazio)
- `MYSTERYREALMS_MAIL_PASSWORD` (padrão: vazio)
- `MYSTERYREALMS_MAIL_SMTP_AUTH` (padrão: `true`)
- `MYSTERYREALMS_MAIL_SMTP_STARTTLS` (padrão: `true`)

### Arquivo de exemplo

Use `.env.example` como referência para criar o seu `.env` local.
