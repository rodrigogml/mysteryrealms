# Glossário Canônico

Este glossário define os termos canônicos de mecânica para uso em toda a documentação.

## Convenção de escrita

- **Nome de exibição (player-facing):** usa acentos e capitalização natural em português (ex.: `Percepção`, `Constituição`, `Mágico Puro`).
- **Chave técnica (engine/data):** usa **slug sem acento**, em `snake_case`, minúsculo (ex.: `percepcao`, `constituicao`, `magia_pura`).
- **Regra geral de mapeamento:** toda mecânica deve manter `nome_exibicao -> chave_tecnica` estável.
- **Sinônimos legados:** devem ser tratados como alias, sem virar novo termo canônico (ex.: `Eletricidade` é alias de `Raio`, chave `raio`).

## Atributos principais

| Exibição | Chave técnica |
|---|---|
| Força | `forca` |
| Destreza | `destreza` |
| Constituição | `constituicao` |
| Intelecto | `intelecto` |
| Percepção | `percepcao` |
| Carisma | `carisma` |
| Vontade | `vontade` |

## Habilidades canônicas

| Exibição | Chave técnica | Atributo base |
|---|---|---|
| Persuasão | `persuasao` | `carisma` |
| Intimidação | `intimidacao` | `carisma` |
| Enganação | `enganacao` | `carisma` |
| Conhecimento (Arcano) | `conhecimento_arcano` | `intelecto` |
| Conhecimento (História) | `conhecimento_historia` | `intelecto` |
| Conhecimento (Relíquias) | `conhecimento_reliquias` | `intelecto` |
| Herbalismo | `herbalismo` | `intelecto` |
| Alquimia | `alquimia` | `intelecto` |
| Furtividade | `furtividade` | `destreza` |
| Sobrevivência | `sobrevivencia` | `percepcao` |
| Manuseio de Armas | `manuseio_armas` | `forca` ou `destreza` |
| Uso de Magia | `uso_magia` | `vontade` ou `intelecto` |

## Tipos de dano

| Exibição | Chave técnica |
|---|---|
| Corte | `corte` |
| Perfuração | `perfuracao` |
| Esmagamento | `esmagamento` |
| Fogo | `fogo` |
| Gelo | `gelo` |
| Raio | `raio` |
| Ácido | `acido` |
| Mágico Puro | `magia_pura` |
| Sangramento | `sangramento` |
| Veneno (Letal) | `veneno_letal` |

## Tipos de aflição

| Exibição | Chave técnica |
|---|---|
| Psíquica | `psiquica` |
| Espiritual | `espiritual` |
| Medo | `medo` |
| Paralisia | `paralisia` |
| Cegueira | `cegueira` |
| Surdez / Mudez | `surdez_mudez` |
| Fadiga | `fadiga` |
| Doença Mágica | `doenca_magica` |
| Alucinação / Ilusão Persistente | `alucinacao_ilusao_persistente` |
| Sono / Torpor | `sono_torpor` |

## Tipos de resistência

| Exibição | Chave técnica | Cobertura |
|---|---|---|
| Corte | `corte` | dano físico cortante |
| Perfuração | `perfuracao` | dano físico perfurante |
| Esmagamento | `esmagamento` | dano físico de impacto |
| Fogo | `fogo` | dano elemental de fogo |
| Gelo | `gelo` | dano elemental de gelo |
| Raio | `raio` | dano elemental elétrico |
| Ácido | `acido` | dano corrosivo |
| Mágico Puro | `magia_pura` | dano mágico não-elemental |
| Encantamento | `encantamento` | efeitos mágicos de controle/status |
| Veneno | `veneno` | toxinas e envenenamento |
| Doença | `doenca` | doenças comuns e mágicas |
| Sangramento | `sangramento` | dano contínuo por perda de sangue |
| Fadiga | `fadiga` | acúmulo de exaustão física |
| Dor | `dor` | penalidades por dor intensa |
| Som | `som` | efeitos sonoros debilitantes |
| Confusão | `confusao` | desorientação mental |
| Ilusão | `ilusao` | distorção perceptiva |
| Controle Mental | `controle_mental` | manipulação psíquica/mágica |
| Corrupção Espiritual | `corrupcao_espiritual` | degradação espiritual/sobrenatural |

## Observação de compatibilidade legada

- `Eletricidade` (legado) deve ser mapeado para **Raio** (`raio`).
- `Magia Pura` e `Mágico Puro` são equivalentes de exibição; usar canonicamente **Mágico Puro** (`magia_pura`) nos documentos de mecânica.
