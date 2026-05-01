# Segurança da Aplicação

## Objetivo
Este documento define o baseline de segurança para MysteryRealms, com foco em exposição de segredos, autenticação/autorização e vetores comuns de falha.

## Escopo de auditoria atual

### 1) Exposição de segredos versionados
- Não versionar credenciais reais em `application.properties`, scripts SQL, testes ou documentação.
- `src/main/resources/application.properties` e `src/main/resources/application.properties.model` devem usar somente variáveis de ambiente para credenciais sensíveis.
- Qualquer segredo previamente exposto em Git deve ser considerado comprometido e rotacionado.

### 2) Autenticação e autorização
- Fluxos sensíveis (alteração de e-mail, redefinição de senha, exclusão de conta, 2FA, regeneração de códigos de recuperação) devem validar:
  - identidade autenticada;
  - posse do recurso (`userId` da sessão igual ao `userId` alvo);
  - janela de validade para tokens temporários;
  - confirmação adicional (2FA, quando habilitado).
- Tokens devem ser aleatórios, com expiração curta e invalidação após uso.

### 3) Vetores comuns de falha
- Enumeração de contas (mensagens diferentes para e-mail existente vs inexistente).
- Falta de limitação de tentativas (brute force e credential stuffing).
- Sessões sem expiração renovável e sem invalidação explícita em logout.
- Logs contendo token, senha, secret TOTP ou código de recuperação.

## Ações obrigatórias quando houver segredo exposto
1. Revogar/rotacionar credencial imediatamente (banco, SMTP, API key, token).
2. Substituir valor versionado por variável de ambiente.
3. Registrar incidente e impacto (serviços afetados, período, responsável pela rotação).
4. Confirmar que novos segredos não ficam em artefatos de build/log.

## Regras para uso seguro de variáveis de ambiente e credenciais
- Obrigatório usar prefixo `MYSTERYREALMS_` para chaves de runtime.
- Nunca definir credenciais de produção como fallback no código.
- Separar credenciais por ambiente (dev, homolog, prod) e por serviço.
- Privilégio mínimo por usuário técnico:
  - DB: apenas schema da aplicação e operações necessárias;
  - SMTP/API: escopo mínimo de envio/leitura estritamente necessário.
- Armazenar segredos em cofre de segredos do ambiente (não em `.env` versionado).
- Rotação periódica recomendada:
  - banco e SMTP: a cada 90 dias;
  - chaves de API/tokens de integração: a cada 60 dias;
  - rotação imediata em qualquer suspeita de vazamento.

## Checklist OWASP básico

### A01 — Broken Access Control
- [ ] Toda operação sensível valida usuário autenticado.
- [ ] Toda operação sensível valida autorização por recurso (ownership/role).
- [ ] Não há endpoints confiando apenas em parâmetros do cliente (`userId`, `role`).

### A02 — Cryptographic Failures
- [ ] Senhas com hash forte (BCrypt/Argon2) e custo adequado.
- [ ] Segredos e tokens não são logados.
- [ ] Transporte externo usa TLS (SMTP STARTTLS/HTTPS).

### A03 — Injection
- [ ] Persistência usa ORM/repositório sem concatenação de query dinâmica insegura.
- [ ] Validação de entrada em campos de autenticação e perfil.

### A04 — Insecure Design
- [ ] Fluxos críticos exigem confirmação explícita (ex.: exclusão de conta).
- [ ] Token de ação crítica possui expiração curta e uso único.

### A05 — Security Misconfiguration
- [ ] Sem credenciais default em produção.
- [ ] Propriedades sensíveis vindas de ambiente.
- [ ] Mensagens de erro sem vazamento de detalhes internos.

### A07 — Identification and Authentication Failures
- [ ] Política de senha mínima aplicada.
- [ ] Proteção contra força bruta ativa.
- [ ] 2FA disponível para operações críticas.

### A09 — Security Logging and Monitoring Failures
- [ ] Eventos críticos auditáveis (login, lock, reset, exclusão de conta).
- [ ] Logs sem dados sensíveis.

## Procedimento mínimo de revisão por release
1. Executar varredura de segredos no diff.
2. Revisar fluxos de autenticação/autorização alterados.
3. Revisar validade/invalidação de tokens temporários.
4. Marcar checklist OWASP deste documento.
5. Aprovar release somente se não houver item crítico pendente.
