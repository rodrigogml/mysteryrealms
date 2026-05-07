# Localização Inicial

Este documento define a origem canônica mínima usada para inicializar novas instâncias de mundo.

## Localidade inicial canônica

- `STARTING_LOCATION_ID`: `zona_langur_praca_das_vozes`.
- Configuração da aplicação: `mysteryrealms.world.startingLocationId`, com valor padrão igual a `STARTING_LOCATION_ID`.
- Nome exibível de referência: Praça das Vozes.
- Escopo espacial: zona navegável de Langur.
- Estado inicial por instância:
  - descoberta: `true`;
  - acessível: `true`.

## Regras de bootstrap

- Toda nova instância de mundo deve iniciar com `currentLocationId = STARTING_LOCATION_ID`.
- A localização inicial deve existir como origem navegável válida no catálogo de mundo.
- A localização inicial deve estar descoberta e acessível no estado da instância recém-criada.
- O bootstrap deve registrar uma entrada inicial de diário para rastrear a abertura da jornada.
- O bootstrap deve criar os marcadores mínimos necessários para indicar que a instância de mundo foi inicializada.

## Seed mínimo por instância

| Tipo | ID | Valor inicial | Finalidade |
|---|---|---|---|
| Localização atual | `zona_langur_praca_das_vozes` | atual, descoberta e acessível | Origem válida de navegação. |
| Diário | `diary_mundo_inicio` | `D1-00:00` | Registro inicial da jornada. |
| Marcador | `mk_mundo_instanciaCriada` | `flag = true` | Controle de bootstrap executado. |
