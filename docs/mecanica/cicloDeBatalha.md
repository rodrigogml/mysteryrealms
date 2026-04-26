# Ciclo de Batalha

Este documento define a sequência padrão de combate usando os mesmos nomes de exibição e chaves técnicas do glossário canônico.

## 1) Encontro

Um combate inicia ao ocorrer contato hostil por narrativa, evento fixo ou encontro aleatório.

## 2) Percepção inicial

Executar teste de percepção:
- `percepcao` do observador vs `furtividade` do oculto; ou
- `percepcao` vs `cd_ambiente`.

Quem vence detecta primeiro e pode ganhar vantagem tática (surpresa, reposicionamento, abertura de turno).

## 3) Iniciativa

Todos os envolvidos rolam:

`iniciativa = 1d20 + destreza + percepcao`

A ordem permanece fixa até fim do combate, salvo efeito explícito que altere iniciativa.

## 4) Rodadas e turnos

Cada participante atua em duas camadas:

### 4.1 Ação de pré-turno

Ação rápida que não substitui ação principal (ex.: trocar arma, consumir item simples, ajuste de posição curta).

### 4.2 Ação de turno

Ação principal da rodada (uma por turno), como:
- atacar;
- assumir postura defensiva;
- fugir;
- conjurar magia;
- apoiar aliado;
- interagir com ambiente;
- intimidar/dialogar;
- preparar ação.

### 4.3 Reação

`Reação` é uma resposta fora do próprio turno, disparada por gatilho válido e resolvida imediatamente após o evento que a acionou.

- Quantidade padrão: **1 reação por rodada** por participante.
- Recuperação: a reação gasta só é recuperada no **início do próximo turno do próprio participante**.
- Gatilhos comuns de reação:
  - ataque corpo a corpo entrando em alcance de ameaça;
  - inimigo saindo do alcance de ameaça sem usar regra de fuga;
  - conjuração hostil em alcance curto quando existir habilidade de interrupção;
  - gatilho explícito de habilidade/equipamento (contra-ataque, aparar, contramágica, etc.).

Regra de implementação: se múltiplos gatilhos ocorrerem na mesma janela, o participante escolhe **apenas 1** para consumir a reação disponível.

### 4.4 Movimento por turno (distância e custo)

Movimento é parte da economia de ação do turno e segue os custos abaixo:

- **Movimento curto (até 3m)**: gratuito, pode ser acoplado à ação de pré-turno;
- **Movimento padrão (até 9m)**: custa a **ação de turno**;
- **Movimento estendido (até 18m)**: custa a ação de turno e aplica penalidade de `-2` em testes de acerto/defesa até o início do próximo turno;
- Não é permitido combinar movimento estendido com ataque/conjuração na mesma ação de turno, salvo efeito explícito.

Conversão para grid (quando usado): 1 quadrado = 1,5m (curto = 2 quadrados, padrão = 6, estendido = 12).

## 5) Resolução mecânica

Conforme a ação, aplicar:
- testes de acerto e defesa/bloqueio;
- cálculo de dano;
- aplicação de aflições;
- mitigação por resistências.

### 5.1) Pipeline de resolução

Executar as etapas abaixo **na ordem** para manter consistência entre sistemas:

1. validar ação e alvo;
2. teste de acerto;
3. mitigação ativa (bloqueio/defesa);
4. cálculo de dano bruto;
5. aplicação de resistência por tipo;
6. aplicação de aflições e testes associados;
7. atualização de estado (PV, fadiga, duração de modificadores).

Referências obrigatórias de fórmula para evitar dupla aplicação de modificadores:
- usar **Precisão**, **Defesa (final)** e **Bloqueio (final)** conforme `docs/mecanica/definicaoDePersonagem.md`;
- usar **Dano (final)** conforme `docs/mecanica/definicaoDePersonagem.md`;
- aplicar resistências e compatibilidades de tipo conforme `docs/mecanica/danosAflicoesResistencias.md`.

Regra de integração: modificadores percentuais/flat já considerados nas fórmulas finais de `definicaoDePersonagem.md` **não** devem ser reaplicados nas etapas 5 a 7; nessas etapas aplicar apenas mitigação por resistência, aflições e atualizações de estado.

Terminologia canônica de efeitos:
- danos: `corte`, `perfuracao`, `esmagamento`, `fogo`, `gelo`, `raio`, `acido`, `magia_pura`, `sangramento`, `veneno_letal`;
- aflições: `psiquica`, `espiritual`, `medo`, `paralisia`, `cegueira`, `surdez_mudez`, `fadiga`, `doenca_magica`, `alucinacao_ilusao_persistente`, `sono_torpor`.

### 5.2) Interrupção de ação preparada

`Preparar ação` cria uma condição de disparo e uma ação futura. A ação preparada é interrompida/cancelada se qualquer condição abaixo ocorrer antes do disparo:

1. o personagem ficar sob `paralisia`, `sono_torpor` ou `cegueira` (quando a ação exigir alvo visual);
2. o personagem sofrer deslocamento forçado maior que 3m;
3. o gatilho declarado tornar-se inválido (alvo cai, sai de alcance ou perde linha de efeito);
4. o início do próximo turno do personagem ocorrer sem o gatilho ser atendido.

Regra de custo:
- preparar ação consome a **ação de turno atual**;
- se a ação preparada for cancelada, o custo não é reembolsado;
- no disparo válido, a ação acontece como reação e consome a reação da rodada.

### 5.3) Regra de fuga (teste oposto)

`Fugir` usa teste oposto entre:

- atacante da fuga: `atletismo` (forçar passagem) **ou** `acrobacia` (evasão), escolhido por quem foge;
- defensor de contenção: `atletismo` (bloqueio físico) **ou** `percepcao` (antecipação), escolhido por quem impede.

Procedimento:
1. quem foge declara direção/rota;
2. resolve-se o teste oposto;
3. em sucesso, quem foge desloca até 9m para fora da zona de ameaça e aplica estado `em_fuga` até o próximo turno;
4. em falha, não há deslocamento, a ação de turno é consumida e o oponente pode realizar 1 ataque de oportunidade imediato (sem custo de reação, por ser efeito da falha de fuga).

Custo de ação:
- `Fugir` sempre consome a **ação de turno**;
- a ação de pré-turno ainda pode ser usada normalmente no mesmo turno.

## 7) Exemplos de rodada completa

Exemplos normativos para mesa e implementação. Valores numéricos são ilustrativos; a ordem de custos é obrigatória.

### 7.1 Exemplo A — combate melee

Contexto: Guerreiro (G) adjacente ao Bandido (B), ambos já engajados.

1. **Turno de G**
   - Pré-turno: passo curto de 1,5m para flanquear (custo: movimento curto gratuito).
   - Ação de turno: `atacar` B (custo: ação de turno).
   - Resultado: B sofre dano após pipeline 5.1.
2. **Turno de B**
   - Pré-turno: sacar adaga (custo de pré-turno).
   - Ação de turno: tenta `fugir` para recuar (custo: ação de turno).
   - Teste oposto de fuga: B (`acrobacia`) vs G (`atletismo`) -> B falha.
   - Consequência: B não se move; G executa ataque de oportunidade da falha de fuga.
3. **Fim da rodada**
   - Ambos gastaram ação de turno.
   - Reações: nenhuma reação padrão foi gasta; ataque de oportunidade acima veio da regra específica de falha em fuga.

### 7.2 Exemplo B — magia e suporte

Contexto: Arcanista (A) atrás do Guardião aliado (T), inimigo Duelista (D) avança.

1. **Turno de A**
   - Pré-turno: reposiciona 3m para manter linha de visão (movimento curto gratuito).
   - Ação de turno: `preparar ação` com gatilho: "se D entrar em alcance de T, lançar escudo arcano em T" (custo: ação de turno de A).
2. **Turno de D**
   - Pré-turno: aproximação curta.
   - Ação de turno: investida de 9m até T.
   - Gatilho dispara: ação preparada de A resolve como reação (A gasta 1 reação da rodada) e concede escudo a T.
   - D conclui ataque contra T já com bônus defensivo ativo.
3. **Turno de T**
   - Pré-turno: sem custo.
   - Ação de turno: `apoiar aliado` em A (ex.: bônus situacional de cobertura).
4. **Fim da rodada**
   - A gastou ação de turno no preparo + reação no disparo.
   - Se o gatilho não tivesse ocorrido até o próximo turno de A, a ação preparada seria perdida sem reembolso.

## 8) Encerramento

O combate termina quando:
- todos os inimigos são derrotados;
- fuga é bem-sucedida;
- derrota do grupo/jogador.

Recompensas possíveis:
- XP;
- loot;
- progresso narrativo.
