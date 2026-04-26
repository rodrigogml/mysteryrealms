# Escudos

Documento de catálogo e regras sistêmicas para escudos (subtipo de Item de Mão voltado a defesa).

## Estrutura de ficha de escudo

Cada escudo deve incluir:
- campos comuns de Item de Mão;
- valor base de bloqueio;
- cobertura (quando aplicável);
- penalidade de destreza (se não proficiente);
- bônus condicionais;
- bônus de item aplicáveis (`BonusItemDefesa`, `BonusItemBloqueio`).

## Integração com Defesa e Bloqueio

Padronização obrigatória:
- **DefesaFinal** recebe apenas `BonusItemDefesa` dos escudos/equipamentos defensivos válidos;
- **BloqueioFinal** recebe apenas `BonusItemBloqueio` do escudo ativo;
- efeitos de substituição de bloqueio prevalecem sobre efeitos aditivos.

## Regras de acúmulo

- Não é permitido acumular dois escudos ativos para somar bloqueio.
- Se múltiplas fontes defensivas de item existirem, aplicar apenas as válidas pelo estado de equipamento e postura.

## Referências legadas consultadas

- `docs/legado/modeloDeFichaDeEscudos.wiki`
- `docs/legado/defesaEBloqueio.wiki`
