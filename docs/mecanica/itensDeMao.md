# Itens de Mão

Itens de Mão são objetos empunháveis usados em combate, exploração, suporte, iluminação e interação narrativa.

## Conceito universal

Qualquer objeto que ocupe uma mão (ou duas) e produza efeito mecânico direto é um **Item de Mão**.

## Atributos comuns

Todo Item de Mão deve ter:
- Nome
- Subtipo (`arma`, `escudo`, `focoMagico`, `ferramenta`, etc.)
- Mãos Necessárias (`1` ou `2`)
- Requisitos de Uso (atributo, classe, raça, proficiência)
- Categoria de Uso (`ataque`, `defesa`, `suporte`, `iluminacao`, `manipulacao`)
- Peso (kg)
- Preço Base (MP/MS)

## Subpáginas canônicas

- [Tipos de Armas](./tiposDeArmas.md)
- [Armas](./armas.md)
- [Escudos](./escudos.md)

## Padronização de bônus de item

Para remover ambiguidades, bônus vindos de Item de Mão alimentam **somente** os canais abaixo:

- `BonusItemPrecisao` → entra em **Precisão**;
- `BonusItemDano` → entra em **DanoFinal**;
- `BonusItemDefesa` → entra em **DefesaFinal**;
- `BonusItemBloqueio` → entra em **BloqueioFinal**.

Regras:
- bônus de item podem ser `flat` (fixo) e/ou `%`;
- quando dois itens competirem no mesmo slot/efeito exclusivo, aplica-se apenas o maior bônus válido;
- efeitos de “substituição de valor” têm precedência sobre efeitos aditivos.

## Regras de equipamento

- O personagem possui até 2 mãos ocupáveis.
- Item de 2 mãos ocupa ambos os slots.
- Troca de item segue regra de ação rápida definida em `docs/mecanica/cicloDeBatalha.md`.

## Referências legadas consultadas

- `docs/legado/itensDeMao.wiki`
