# Progressão do Jogo

Este documento define a regra **canônica** de evolução de personagem por nível em Mystery Realms.

## Objetivo do sistema

A progressão é contínua (sem nível máximo fixo) e baseada em XP obtido por:
- combate;
- conclusão de objetivos/quests;
- descoberta/exploração;
- interações relevantes com NPCs.

A curva foi desenhada para:
- acelerar os níveis iniciais;
- desacelerar gradualmente a evolução em níveis médios/altos;
- permitir ajustes de balanceamento sem quebrar personagens existentes.

## Fórmula oficial de XP

### XP para avançar do nível `n` para `n+1`

`XP_nivel(n) = A * (ln(n + B))^C`

Onde:
- `n`: nível atual (`n >= 1`)
- `ln`: logaritmo natural
- `A`: fator de escala
- `B`: deslocamento horizontal
- `C`: curvatura da progressão

### XP acumulado total até o nível `n`

`XP_total(n) = soma(i=1..n) de [ A * (ln(i + B))^C ]`

> Observação: para implementação, o servidor deve usar cálculo determinístico (mesmo arredondamento em todos os ambientes) e evitar divergência entre cliente e backend.

## Parâmetros ativos (versão atual)

**Versão de balanceamento ativa:** `BAL-1.0.0`

Parâmetros canônicos:
- `A = 50`
- `B = 10`
- `C = 10`

Comportamento esperado dessa configuração:
- progresso rápido no início;
- custo crescente de XP em níveis avançados;
- manutenção de desafio de longo prazo.

## Regra de arredondamento e implementação

Para evitar inconsistência entre linguagens/plataformas:
- calcular `XP_nivel(n)` em ponto flutuante;
- aplicar `ceil` no resultado final de cada nível (sempre arredonda para cima);
- `XP_total(n)` é a soma dos `XP_nivel(i)` já arredondados.

Exemplo de referência (pseudo):

```txt
xpNivel(n) = ceil(A * pow(ln(n + B), C))
xpTotal(n) = sum(xpNivel(i), i=1..n)
```

## Como nível afeta o personagem

### 1) Atributos principais

Ao subir de nível, o personagem recebe **Pontos de Evolução de Atributo (PEA)** conforme marcos:
- níveis `2-10`: `+1 PEA` por nível;
- níveis `11-30`: `+1 PEA` a cada 2 níveis;
- níveis `31+`: `+1 PEA` a cada 3 níveis.

Regras:
- cada PEA aumenta `+1` em um atributo principal;
- nenhum atributo pode subir mais de `+2` no mesmo nível;
- limite suave por nível: `AtributoMaxNivel = 10 + floor(nivel / 5)`.

### 2) Habilidades

Nível impacta habilidades de forma indireta e direta:
- **indireta:** aumento de atributo melhora habilidades associadas;
- **direta:** a cada nível ímpar, o jogador recebe `+1 Ponto de Proficiência (PP)` para distribuir em habilidades treináveis;
- bônus de proficiência por faixa de nível:
  - nível `1-9`: `+0`
  - nível `10-24`: `+1`
  - nível `25-49`: `+2`
  - nível `50+`: `+3`

Valor final recomendado de habilidade:

`HabilidadeFinal = AtributoBase + PP_da_habilidade + BonusProficiênciaFaixa + Modificadores`

### 3) Desbloqueios

Desbloqueios mínimos por marcos de nível:
- **Nível 3:** 1º slot de habilidade ativa de classe;
- **Nível 5:** traço utilitário de classe/racial avançado;
- **Nível 8:** 2º slot de habilidade ativa;
- **Nível 12:** habilidade de assinatura (tier intermediário);
- **Nível 20:** especialização avançada (ramificação de build);
- **Nível 30+:** ciclos de maestria (repetíveis por patamar).

Cada classe/raça pode definir desbloqueios adicionais, mas **não pode remover** os marcos base acima.

## Estratégia de versionamento de balanceamento

Toda alteração de progressão deve versionar em formato semântico:

`BAL-MAJOR.MINOR.PATCH`

- `MAJOR`: quebra de compatibilidade (fórmula muda, parâmetros incompatíveis, conversão obrigatória de XP)
- `MINOR`: ajustes de curva/desbloqueio compatíveis (novos marcos, tuning de ritmo)
- `PATCH`: correções sem impacto estrutural (erro de arredondamento, texto, telemetria)

### Política operacional

1. Cada personagem armazena:
   - `xp_acumulado`
   - `nivel_atual`
   - `balance_version` aplicada no último recálculo.
2. Ao mudar versão:
   - manter `xp_acumulado` como fonte da verdade;
   - recalcular `nivel_atual` com a nova curva;
   - registrar migração no histórico de patch notes.
3. Alterações `MAJOR` exigem:
   - simulação prévia;
   - plano de migração;
   - comunicação de impacto ao jogador.

## Referências relacionadas

- Definição de Personagem: `docs/mecanica/definicaoDePersonagem.md`
- Glossário canônico: `docs/mecanica/glossario.md`
- Documento legado de origem: `docs/legado/progressaoDoJogo.wiki`
