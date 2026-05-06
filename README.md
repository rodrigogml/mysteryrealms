# MysteryRealms

## Objetivo deste guia

Este README foi organizado para acelerar o onboarding de novos contribuidores.

## Pré-requisitos

Antes de iniciar, garanta que você possui:

- Java 25 (JDK)
- Maven 3.9+
- MySQL 9.0 em execução localmente
- Git

Opcional, mas recomendado:

- IDE com suporte a Java e Maven (IntelliJ IDEA ou VS Code com extensões Java)
- Cliente SQL para inspecionar o schema local

## Setup local (passo a passo)

1. **Clone o repositório**

   ```bash
   git clone <url-do-repositorio>
   cd mysteryrealms
   ```

2. **Configure variáveis de ambiente**

   Use `.env.example` como base para criar seu `.env` local.

3. **Suba e configure o MySQL**

   - Garanta que o banco `mysteryrealms` exista e esteja acessível.
   - Se necessário, ajuste usuário/senha nas variáveis abaixo.

4. **Revise configurações de aplicação**

   O Spring Boot lê `src/main/resources/application.properties`, que usa variáveis `MYSTERYREALMS_*` com fallback padrão.

5. **Instale dependências e valide build**

   ```bash
   mvn clean test
   ```

6. **Suba a aplicação localmente**

   ```bash
   mvn spring-boot:run
   ```

## Comandos de desenvolvimento

- **Executar todos os testes**

  ```bash
  mvn test
  ```

- **Executar suíte limpa (recomendado antes de PR)**

  ```bash
  mvn clean test
  ```

- **Subir aplicação local**

  ```bash
  mvn spring-boot:run
  ```

- **Gerar pacote da aplicação**

  ```bash
  mvn clean package
  ```

## Troubleshooting (erros comuns)

### 1) Falha de conexão com banco

**Sintoma:** erro de conexão no startup.

**Verificar:**

- Se o MySQL está ativo.
- Se `MYSTERYREALMS_DATASOURCE_URL`, `MYSTERYREALMS_DATASOURCE_USERNAME` e `MYSTERYREALMS_DATASOURCE_PASSWORD` estão corretas.

### 2) Falha por variáveis obrigatórias

**Sintoma:** aplicação não inicializa com erro de validação.

**Verificar:**

- `MYSTERYREALMS_MAIL_FROM`
- `MYSTERYREALMS_PUBLIC_BASE_URL`

Essas variáveis são obrigatórias e, se ausentes/inválidas, o startup falha.

### 3) Porta já em uso ao subir aplicação

**Sintoma:** erro de bind da porta HTTP.

**Verificar:**

- Se outro processo já está usando a porta padrão.
- Se necessário, altere a porta com variável de ambiente (`SERVER_PORT`) para desenvolvimento local.

### 4) Testes falhando por ambiente inconsistente

**Sintoma:** testes passam em uma máquina e falham em outra.

**Verificar:**

- Versão do Java (use Java 25, alinhado ao `pom.xml` e ao CI).
- Banco local com credenciais e timezone alinhados ao `application.properties`.
- Execução prévia de `mvn clean test` para limpar resíduos.

## Convenções de contribuição

### Branch

- Use branch a partir de `main`.
- Padrão sugerido: `tipo/descricao-curta`.
- Exemplos:
  - `feat/onboarding-readme`
  - `fix/email-validation`
  - `docs/guia-contribuicao`

### Commit

- Faça commits pequenos e objetivos.
- Mensagens no imperativo, descrevendo o que foi feito.
- Formato sugerido:
  - `feat: adiciona validação de ...`
  - `fix: corrige erro de ...`
  - `docs: atualiza onboarding no README`

### Pull Request

- Abra PR com escopo claro e limitado.
- Inclua:
  - contexto do problema;
  - solução aplicada;
  - impacto técnico;
  - evidências (comandos/testes executados).
- Antes de abrir PR, execute ao menos:

  ```bash
  mvn clean test
  ```

## Configuração de ambiente (referência rápida)

### Variáveis obrigatórias

- `MYSTERYREALMS_MAIL_FROM`: remetente usado nos e-mails transacionais.
- `MYSTERYREALMS_PUBLIC_BASE_URL`: URL pública para geração de links de confirmação.

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
