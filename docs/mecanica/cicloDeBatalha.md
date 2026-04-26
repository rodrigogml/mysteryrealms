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

## 6) Encerramento

O combate termina quando:
- todos os inimigos são derrotados;
- fuga é bem-sucedida;
- derrota do grupo/jogador.

Recompensas possíveis:
- XP;
- loot;
- progresso narrativo.
