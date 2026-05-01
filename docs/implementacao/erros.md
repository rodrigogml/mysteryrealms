# Guideline de Erros

## Objetivo
Padronizar validação de entradas e resposta de falhas em endpoints, comandos e serviços.

## Validação explícita por camada
- **Entrada/UI ou endpoint**: validar formato básico e campos obrigatórios.
- **Aplicação/serviço**: validar regra de uso do caso de uso (ex.: e-mail duplicado).
- **Domínio**: validar invariantes do modelo.

No módulo de usuário, a classe `UserInputValidationService` centraliza as validações de entrada para a camada de aplicação.

## Mapeamento de exceções
- `ValidationException` → erro de validação com código `MR-VAL-001`.
- `DomainException` → regra de negócio com código `MR-BUS-001`.
- Exceções não mapeadas → falha sistêmica com código `MR-SYS-001`.

A conversão para payload padronizado é feita por `ErrorMapperService` no formato `ErrorResponseVO`.

## Payload padrão
```json
{
  "code": "MR-VAL-001",
  "messageKey": "user.error.emailInvalid",
  "traceId": "3b3a3f85-9d9e-4ac2-bd79-96b7f111d255"
}
```

## Exemplos
### Erro de validação de entrada
```json
{
  "code": "MR-VAL-001",
  "messageKey": "user.error.passwordTooShort",
  "traceId": "08b4c819-5efe-4f6b-8ac5-6b40cfad5952"
}
```

### Erro de regra de negócio
```json
{
  "code": "MR-BUS-001",
  "messageKey": "user.error.accountLocked",
  "traceId": "c9dd0191-7306-4f2f-8cf8-cf46f137f98f"
}
```

### Erro inesperado
```json
{
  "code": "MR-SYS-001",
  "messageKey": "system.error.unexpected",
  "traceId": "trace-unavailable"
}
```
