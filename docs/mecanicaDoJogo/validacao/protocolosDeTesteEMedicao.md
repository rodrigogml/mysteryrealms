# Protocolos de Teste e Medição

## Objetivo Mecânico

Consolidar, em formato operacional único, os protocolos de teste usados para validação mecânica do jogo (checagens base, dificuldade, vantagem/desvantagem e medição por cenário).

Este documento migra e normaliza conteúdo do legado para apoiar requisitos funcionais e simulação.

---

## Modelo Base de Teste (migrado do legado)

### Quando usar habilidade vs atributo

1. Se existir habilidade específica para a ação, usar a habilidade.
2. Sem habilidade específica, usar o atributo principal coerente.
3. Em dúvida de enquadramento, registrar a escolha no log de validação para auditoria.

### Fórmula canônica de teste

`resultadoTeste = pontosBase + modificadores + 1d20`

Onde:
- `pontosBase` = habilidade ou atributo aplicável;
- `modificadores` = itens, estado, ambiente e efeitos sistêmicos;
- `1d20` = componente estocástico padrão.

### Classe de Dificuldade (CD)

A CD deve ser definida por contexto operacional e não por narrativa solta.

| Faixa de CD | Uso recomendado |
|---|---|
| 8–11 | tarefa simples sob baixa pressão |
| 12–15 | tarefa padrão com risco controlado |
| 16–19 | tarefa complexa com penalidade contextual |
| 20+ | tarefa crítica ou excepcional |

Regra: toda CD fora da faixa esperada da área deve ser justificada no artefato de teste.

### Vantagem e desvantagem

- **Vantagem:** rolar `2d20`, manter maior resultado.
- **Desvantagem:** rolar `2d20`, manter menor resultado.
- Proibido aplicar vantagem e desvantagem cumulativas no mesmo eixo; em conflito, neutralizam.

---

## Protocolo de Percepção (migrado do legado)

### Escopo

Determinar detecção pré-conflito em encontros, emboscadas e aproximações furtivas.

### Modos de resolução

1. **Percepção vs Furtividade** (entidades ativas):
   - `resultadoPercepcao = PER + 1d20`
   - `resultadoFurtividade = FUR + 1d20`
   - maior resultado detecta primeiro.
2. **Percepção vs CD de Ambiente** (alvo passivo/contextual):
   - `resultadoPercepcao = PER + 1d20`
   - sucesso quando `resultadoPercepcao >= CDambiente`.

### CD de ambiente (baseline operacional)

| Contexto | CD sugerida |
|---|---:|
| visibilidade alta / ruído baixo | 10 |
| cobertura parcial / ruído médio | 14 |
| baixa luz / obstrução relevante | 17 |
| neblina densa / tempestade / camuflagem forte | 20 |

### Regras de saída

- Sucesso de detecção concede vantagem posicional inicial (informação e preparo).
- Falha de detecção pode habilitar rodada surpresa do opositor.
- Resultado deve ser registrado no artefato de rodada com o modo usado (`vsFurtividade` ou `vsCDambiente`).

---

## Protocolo de Iniciativa (migrado do legado)

### Escopo

Definir ordem de ação após resolução de percepção/surpresa.

### Fórmula

`iniciativa = AGI + modificadores + 1d20`

### Empates

Ordem de desempate padronizada:
1. maior `AGI`;
2. maior `PER`;
3. persistindo empate, rolagem adicional apenas entre empatados.

### Relação com percepção

- A iniciativa sempre ocorre após o teste de percepção.
- Vantagem de surpresa não elimina a rolagem de iniciativa; apenas antecipa uma ação inicial.
- A ordem de iniciativa permanece fixa até mudança explícita por efeito sistêmico.

### Regras de saída

- Registrar a trilha final de ordem por participante no log da rodada.
- Registrar também se houve surpresa e quem executou ação antecipada.

---

## Protocolo de Acerto (migrado do legado)

### Escopo

Resolver se um ataque (corpo a corpo, distância ou magia de ataque direto) atinge o alvo.

### Fórmula operacional

`resultadoAcerto = precisaoTotal + modificadoresSituacionais + 1d20`

Condição de sucesso padrão:

`resultadoAcerto >= defesaTotalAlvo`

### Exceções críticas

- `20 natural`: acerto crítico automático, com gatilho de dano crítico.
- `1 natural`: falha crítica automática, com possibilidade de efeito negativo contextual.

### Regras de saída

1. Registrar se foi acerto normal, crítico ou falha crítica.
2. Encadear imediatamente o `Protocolo de Dano` em caso de acerto.
3. Em falha crítica, registrar efeito colateral aplicado (se houver).

---

## Protocolo de Dano (migrado do legado)

### Escopo

Resolver dano final após acerto, considerando bloqueio e resistência.

### Pipeline de cálculo

1. `danoBase` (arma + atributo + bônus de classe/raça + modificadores válidos).
2. Aplicar bloqueio:
   - `danoPosBloqueio = max(0, danoBase - bloqueioFixo)` **ou**
   - `danoPosBloqueio = danoBase * (1 - bloqueioPercentual)` conforme regra ativa.
3. Aplicar resistência por tipo:
   - `danoFinal = danoPosBloqueio * (1 - resistenciaTipo)`.

### Regras de saída

- `danoFinal` nunca pode ser negativo.
- Tipo de dano deve ser registrado para rastrear resistência aplicada.
- O log deve explicitar a ordem: `base -> bloqueio -> resistência`.

---

## Protocolo de Fuga (migrado do legado)

### Escopo

Resolver tentativas de sair do combate durante turno.

### Fórmulas

- `resultadoFuga = DES_jogador + 1d20 + modificadoresJogador`
- `dificuldadeFuga = DES_inimigo + 10 + modificadoresContexto`

Condição de sucesso:

`resultadoFuga >= dificuldadeFuga`

### Regras de saída

1. Sucesso: personagem deixa o combate imediatamente.
2. Falha: personagem permanece engajado e segue sujeito à sequência inimiga.
3. Registrar modificadores aplicados para auditoria de balanceamento.

---

## Protocolo de Conscientização (migrado do legado)

### Escopo

Resolver permanência de consciência após condição crítica (ex.: dano fatal/colapso).

### Fórmula

`testeConsciencia = VON + CON + 1d20 + modificadores`

CD baseline: `15`.

### Modificadores baseline

- `-2` se fadiga > 90%.
- `-2` se sofreu golpe crítico no evento imediatamente anterior.

### Regras de saída

- `testeConsciencia >= 15`: permanece consciente.
- `testeConsciencia < 15`: entra em estado inconsciente.
- Registrar gatilho causal (fadiga, crítico, ambos) no log de rodada.

---

## Protocolo de Sono (migrado do legado)

### Escopo

Validar continuidade de descanso por hora e risco de interrupção.

### Componentes de risco por hora

- `CDD`: chance de despertar por desconforto.
- `CEH`: chance de evento hostil.
- `CTIH`: chance total de interrupção por hora.

Relação operacional:

`CTIH = CDD + CEH - (CDD * CEH)`

### Regras de saída

1. Se ocorrer interrupção, registrar motivo (`desconforto` ou `hostil`).
2. Evento hostil furtivo deve encadear `Protocolo de Percepção` para determinar despertar.
3. Probabilidade de descanso completo (ex.: 8h) deve ser registrada por sessão para análise de cadência de recuperação.

---

## Protocolo de Testes Adicionais Pós-Dano (migrado do legado)

### Escopo

Capturar verificações extras disparadas após dano, sem quebrar o fluxo base de combate.

### Catálogo operacional inicial

1. Aplicação de efeitos secundários.
2. Ativação de habilidades reativas.
3. Verificação de condições críticas.
4. Efeitos em área ou cadenciados.

### Regras de saída

- Cada teste adicional deve registrar `gatilho`, `ordem`, `resultado` e `impacto`.
- Teste adicional não pode reordenar retroativamente o resultado de acerto/dano já resolvido.
- Em conflito de prioridade, prevalece a ordem canônica definida em combate.

---

## Medição para Simulação

### Métricas mínimas obrigatórias

| Métrica | Finalidade |
|---|---|
| `taxaDeSucesso` | verificar adequação da CD ao perfil da classe |
| `desvioPadraoResultado` | medir volatilidade da rolagem por cenário |
| `tempoParaMarco` | validar cadência de progressão por faixa |
| `desvioEntreClasses` | identificar distorção sistêmica entre arquétipos |

### Cenários de medição

1. **Combate-dominante**
2. **Híbrido equilibrado**
3. **Narrativo-investigativo**

Os três cenários são obrigatórios para fechamento de rodada.

---

## Critérios de Fechamento de Rodada

A rodada só pode ser concluída quando:

1. houver resultados completos para os 3 cenários;
2. cada métrica mínima obrigatória estiver preenchida;
3. existir decisão formal (`Aprovado`, `Ajuste leve`, `Reprovado`);
4. houver próxima ação definida para o ciclo seguinte.

---

## Integração com Outros Sistemas

- `progressaoDoJogo.md`: usa `tempoParaMarco` e `desvioEntreClasses` para calibragem de marcos/tetos.
- `rodadaInicialCadencia.md`: consome este protocolo como regra de execução e encerramento.
- `validacaoDeCadencia.md`: mantém o fluxo de simulação por cenário.

---

## Pronto para Requisito

Para este tópico ser considerado pronto para requisito funcional, ainda falta:

1. Executar ao menos 1 rodada completa com resultados reais usando este protocolo.
2. Definir limites numéricos-alvo de `taxaDeSucesso` por faixa/classe.
3. Versionar historicamente decisões de ajuste entre rodadas.
