# Requisitos — Economia e Inventário

Requisitos funcionais do sistema para moedas, preços, itens de mão, armas, escudos e regras de equipamento.

Referências canônicas: `docs/mecanica/economia.md`, `docs/mecanica/itensDeMao.md`, `docs/mecanica/tiposDeArmas.md`, `docs/mecanica/armas.md`, `docs/mecanica/escudos.md`.

---

## RF-EI-01 — Sistema monetário

O sistema deve suportar duas moedas:

| Moeda | Chave | Peso por unidade | Valor relativo |
|---|---|---|---|
| Moeda Primária | `MP` | 6 g | `1 MP = 100 MS` |
| Moeda Secundária | `MS` | 5 g | `1 MS = 0,01 MP` |

### Regras de conversão

- Taxa fixa: `1 MP = 100 MS`.
- A conversão deve ser exata (inteira), sem frações de moeda.

### Peso de moedas na carga

```
peso_moedas_kg = ((qtd_mp × 6) + (qtd_ms × 5)) / 1000
```

Esse valor integra `carga_atual_kg` conforme RF-FP-06.4.

### Notação monetária

Formato canônico: `MP$MS`
- `10$25` = 10 MP e 25 MS.
- `10$` = 10 MP, sem MS.
- `$25` = 25 MS, sem MP.

---

## RF-EI-02 — Precificação de itens

Todo item comercializável deve ter **Preço Base** declarado.

### Fórmula de preço aplicado

```
preco_aplicado = preco_base × fator_variacao_monetaria
```

Onde:

```
fator_variacao_monetaria = fator_lugar × fator_reputacao
                         × fator_relacionamento × fator_oferta_demanda × fator_venda
```

- Faixa canônica para cada fator: `[0,5, 2,0]`.

### Fatores de mercado

| Fator | Descrição | Regra de precedência |
|---|---|---|
| `fator_lugar` | Custo de vida, logística e acesso a recursos. | Localidade > Região > Macro-região. |
| `fator_reputacao` | Reputação do personagem no local. | Baseado em RF-SS-07. |
| `fator_relacionamento` | Vínculo com o NPC comerciante. | Baseado em RF-SS-06. |
| `fator_oferta_demanda` | Escassez/abundância do item no mercado local. | Definido pelo contexto de mundo. |
| `fator_venda` | Penalidade padrão para revenda do jogador ao NPC. | Deve ser `<= 1,0` para desfavorecer revenda. |

### Diretrizes de controle econômico

- Custos recorrentes (manutenção, moradia, serviços) devem ser cobrados periodicamente.
- Limitar ouro disponível por NPC (teto de compra).
- Restringir itens aceitos por tipo de comerciante.
- Drenagem por itens de luxo e consumo não essencial.

---

## RF-EI-03 — Item de Mão: atributos comuns

Todo Item de Mão deve armazenar:

| Campo | Tipo | Obrigatório | Regra |
|---|---|---|---|
| `nome` | string | Sim | Nome exibível não vazio. |
| `subtipo` | enum | Sim | `arma`, `escudo`, `focoMagico`, `ferramenta`, etc. |
| `maos_necessarias` | inteiro | Sim | `1` ou `2`. |
| `requisitos_uso` | objeto | Não | Atributo mínimo, classe, raça ou proficiência. |
| `categoria_uso` | enum | Sim | `ataque`, `defesa`, `suporte`, `iluminacao`, `manipulacao`. |
| `peso_kg` | decimal | Sim | `>= 0`. |
| `preco_base` | objeto monetário | Sim | Em MP e/ou MS. |

---

## RF-EI-04 — Bônus de item: canais canônicos

Bônus vindos de Item de Mão alimentam **somente** os seguintes canais:

| Bônus | Canal de destino |
|---|---|
| `bonus_item_precisao` | `precisao_final` (RF-FP-06.5) |
| `bonus_item_dano` | `dano_final` (RF-FP-06.6) |
| `bonus_item_defesa` | `defesa_final` (RF-FP-06.7) |
| `bonus_item_bloqueio` | `bloqueio_final` (RF-FP-06.8) |

Regras:
- Bônus de item podem ser flat (fixo) e/ou percentual.
- Quando dois itens competirem pelo mesmo slot/efeito exclusivo, aplicar apenas o maior bônus válido.
- Efeitos de "substituição de valor" têm precedência sobre efeitos aditivos.

---

## RF-EI-05 — Regras de equipamento de mãos

- O personagem possui até 2 mãos ocupáveis.
- Item de 2 mãos ocupa ambos os slots.
- Troca de item equipado segue a regra de ação rápida (pré-turno) definida em RF-CT-06.
- Bônus de item só são computados enquanto o item estiver efetivamente equipado.

---

## RF-EI-06 — Tipos de Armas

O sistema deve suportar a classificação de tipos de armas. Cada tipo deve declarar:

| Campo | Tipo | Obrigatório |
|---|---|---|
| `nome_tipo` | string | Sim |
| `funcao` | string | Sim | Ex.: leve, pesada, distância, foco mágico. |
| `atributo_primario` | chave canônica | Sim | Ex.: `forca`, `destreza`, `intelecto`. |
| `maos_comuns` | inteiro | Sim | `1` ou `2`. |
| `alcance_padrao` | string | Sim | Curto, médio, longo, etc. |
| `perfil_critico` | objeto | Sim | Faixa de valor do dado e multiplicador. |
| `compatibilidades` | lista | Não | Classes/raças com afinidade natural. |

O tipo de arma influencia:
- Testes de ataque e dano.
- Bônus de proficiência por classe/raça.
- Regras de combate conforme RF-CT-06.

---

## RF-EI-07 — Ficha de arma

Cada arma deve incluir, além dos atributos comuns de RF-EI-03:

| Campo | Tipo | Obrigatório |
|---|---|---|
| `tipo_arma_id` | string | Sim | Referência ao tipo de arma (RF-EI-06). |
| `dado_dano_base` | string | Sim | Notação de dado, ex.: `1d8`, `2d6`. |
| `tipo_dano` | chave canônica | Sim | Conforme RF-MAR-01. |
| `alcance` | string | Sim | Curto, médio, longo, etc. |
| `perfil_critico` | objeto | Sim | Pode sobrescrever o padrão do tipo. |
| `bonus_item_precisao` | objeto | Não | Flat e/ou percentual. |
| `bonus_item_dano` | objeto | Não | Flat e/ou percentual. |

Regras de integração:
- `precisao_final` recebe apenas `bonus_item_precisao` da arma ativa.
- `dano_final` recebe apenas `bonus_item_dano` da arma ativa.
- Bônus de classe/raça/proficiência são camadas separadas dos bônus de item.

---

## RF-EI-08 — Ficha de escudo

Cada escudo deve incluir, além dos atributos comuns de RF-EI-03:

| Campo | Tipo | Obrigatório |
|---|---|---|
| `valor_base_bloqueio` | inteiro | Sim |
| `cobertura` | string | Não | Descrição de cobertura especial. |
| `penalidade_destreza` | inteiro | Não | Aplicada quando personagem não for proficiente. |
| `bonus_condicionais` | lista | Não | Bônus ativos sob condições específicas. |
| `bonus_item_defesa` | objeto | Não | Flat e/ou percentual. |
| `bonus_item_bloqueio` | objeto | Não | Flat e/ou percentual. |

Regras de acúmulo:
- `defesa_final` recebe apenas `bonus_item_defesa` dos equipamentos defensivos válidos.
- `bloqueio_final` recebe apenas `bonus_item_bloqueio` do escudo ativo.
- Não é permitido acumular dois escudos ativos para somar bloqueio.
- Se múltiplas fontes defensivas existirem, aplicar somente as válidas pelo estado de equipamento e postura.
