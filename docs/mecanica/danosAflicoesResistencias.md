# Danos, Aflições e Resistências

Este documento padroniza a taxonomia de efeitos prejudiciais e sua mitigação.

## Taxonomia de dano, aflição e resistência

- **Dano:** reduz recursos primários (ex.: pontos de vida), imediato ou contínuo.
- **Aflição:** impõe estados debilitantes comportamentais/fisiológicos.
- **Resistência:** reduz intensidade, duração ou chance de aplicação de dano/aflição.

## Tipos de dano (canônico)

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

## Tipos de aflição (canônico)

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

## Tipos de resistência (canônico)

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
| Encantamento | `encantamento` |
| Veneno | `veneno` |
| Doença | `doenca` |
| Sangramento | `sangramento` |
| Fadiga | `fadiga` |
| Dor | `dor` |
| Som | `som` |
| Confusão | `confusao` |
| Ilusão | `ilusao` |
| Controle Mental | `controle_mental` |
| Corrupção Espiritual | `corrupcao_espiritual` |

## Regras gerais de balanceamento

- Resistência de jogador normalmente limitada a 80%.
- Criaturas podem atingir 100% (imunidade).
- Imunidade idealmente deve vir acompanhada de vulnerabilidade relevante.

## Modelo de resistência de aflição

Parâmetros base para cálculo de aplicação e efeito de aflições:

- `ChanceBaseAplicacao`
- `DuracaoBase`
- `IntensidadeBase`
- `ResistenciaAflicao`

Regra padrão (com `ResistenciaAflicao` normalizada no intervalo `[0, 1]`):

- `ChanceFinal = max(min_chance, ChanceBaseAplicacao × (1 - ResistenciaAflicao))`
- `DuracaoFinal = max(1, floor(DuracaoBase × (1 - ResistenciaAflicao)))`
- `IntensidadeFinal = floor(IntensidadeBase × (1 - ResistenciaAflicao))`

Notas de implementação:

- `min_chance` evita casos de chance nula quando não há imunidade explícita.
- Para imunidade explícita, `ResistenciaAflicao = 1` e `ChanceFinal = 0`.
- Sempre arredondar para baixo (`floor`) duração e intensidade para previsibilidade sistêmica.

### Exceções por tipo de aflição

- **Medo (`medo`):** `ResistenciaAflicao` reduz principalmente `ChanceFinal` e `DuracaoFinal`; `IntensidadeFinal` pode permanecer fixa por tier da aflição.
- **Sangramento (`sangramento`):** tratar intensidade como dano por turno; `ResistenciaAflicao` reduz o dano por turno (`IntensidadeFinal`), com chance de aplicação separada se necessário.

## Compatibilidade legada

- Termo legado **Eletricidade** deve mapear para `raio`.
- Manter equivalência de exibição entre **Magia Pura** e **Mágico Puro**, preferindo **Mágico Puro**.
