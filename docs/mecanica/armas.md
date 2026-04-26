# Armas

Documento de catálogo e regras sistêmicas para armas (subtipo de Item de Mão voltado a ataque).

## Estrutura de ficha de arma

Cada arma deve incluir:
- campos comuns de Item de Mão;
- Tipo de Arma (referência para `docs/mecanica/tiposDeArmas.md`);
- dado de dano base;
- tipo de dano;
- alcance;
- perfil crítico;
- bônus de item aplicáveis (`BonusItemPrecisao`, `BonusItemDano`).

## Integração com Precisão e Dano

Padronização obrigatória:
- **Precisão** recebe apenas `BonusItemPrecisao` da arma ativa;
- **DanoFinal** recebe apenas `BonusItemDano` da arma ativa;
- bônus de classe/raça/proficiência continuam separados dos bônus de item.

## Compatibilidade por tipo

Bônus de classe e raça por **tipo de arma** devem ser aplicados como camada própria (não como bônus de item), preservando a ordem de cálculo definida em `docs/mecanica/definicaoDePersonagem.md`.

## Referências legadas consultadas

- `docs/legado/armas.wiki`
- `docs/legado/modeloDeFichaDeArmas.wiki`
