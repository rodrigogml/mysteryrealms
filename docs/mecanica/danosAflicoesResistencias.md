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

## Compatibilidade legada

- Termo legado **Eletricidade** deve mapear para `raio`.
- Manter equivalência de exibição entre **Magia Pura** e **Mágico Puro**, preferindo **Mágico Puro**.
