# Economia

Este documento define as regras econômicas canônicas do sistema: moedas, conversão, peso monetário, variação de preço e fatores de mercado.

## Sistema monetário

O sistema usa duas moedas-base:
- **Moeda Primária (MP):** transações de alto valor.
- **Moeda Secundária (MS):** transações fracionadas e troco.

### Taxa de conversão

- **Regra fixa:** `1 MP = 100 MS`.

### Peso de moedas

As moedas contribuem para **Carga Atual** no inventário.

| Moeda | Valor unitário | Peso por unidade |
| --- | ---: | ---: |
| MP | 1,00 | 6 g |
| MS | 0,01 | 5 g |

Regras de cálculo:
- `PesoMoedasKg = ((QtdMP × 6) + (QtdMS × 5)) / 1000`
- `CargaAtualKg = PesoEquipadosKg + PesoMochilaKg + PesoMoedasKg`

> Observação de design: moedas de menor valor são relativamente mais pesadas, incentivando conversão para moedas de maior valor.

## Notação monetária

A notação padrão é `MP$MS`:
- `10$25` = 10 MP e 25 MS
- `10$` = 10 MP
- `$25` = 25 MS

## Preço base e variações

Todo item comercializável deve possuir **Preço Base**.

Fórmula de preço aplicado:

`PrecoAplicado = PrecoBase × FatorVariacaoMonetaria`

Onde:

`FatorVariacaoMonetaria = FatorLugar × FatorReputacao × FatorRelacionamento × FatorOfertaDemanda × FatorVenda`

Faixa canônica para cada fator: **0,5 até 2,0**.

## Fatores de mercado

### Fator de Lugar
- Representa custo de vida, distância logística e acesso a recursos.
- Regra de precedência territorial: **localidade > região > macro-região**.

### Fator de Reputação
- Modifica preços conforme reputação do personagem no local.

### Fator de Relacionamento
- Modifica preços conforme vínculo com o NPC comerciante.

### Fator de Oferta e Demanda
- Ajusta preço por escassez/abundância do item no mercado local.

### Fator de Venda
- Penalidade padrão para revenda do jogador ao NPC.

## Controle econômico

Diretrizes para evitar inflação e acúmulo excessivo:
- custos recorrentes (manutenção, moradia, serviços);
- limite de ouro disponível por NPC;
- limitação de itens aceitos por comerciante;
- drenagem por itens de luxo e consumo não essencial.

## Integrações obrigatórias com ficha

Este documento é referência normativa para:
- **Moedas** e **Carga Atual** em `docs/mecanica/definicaoDePersonagem.md`;
- precificação de equipamentos em `docs/mecanica/itensDeMao.md`, `docs/mecanica/armas.md` e `docs/mecanica/escudos.md`.

## Referências legadas consultadas

- `docs/legado/economia.wiki`
