# Sistema de Marcadores

## Objetivo

Definir marcadores canônicos para controlar estado narrativo, desbloqueios e continuidade de diálogos sociais.

## Tipos de marcador

- `flag` (booleano): fato ocorreu/não ocorreu.
- `stage` (inteiro): estágio de progresso (ex.: 0..3).
- `counter` (inteiro): contagem de recorrência.

## Convenção de IDs

Formato obrigatório:

`mk_<dominio>_<descricaoCamelCase>`

Exemplos:

- `mk_kaela_confiouPrimeiroContato`
- `mk_faccaoMercadores_debitoQuitado`
- `mk_taverna_conversaSigilosaLiberada`

Regras:

- prefixo fixo `mk_`;
- domínio identifica NPC, facção, localidade ou quest;
- descrição em `camelCase`, sem acentos e sem espaços.

## Operações canônicas

- Inserir/ativar marcador.
- Remover/desativar marcador.
- Incrementar/decrementar marcador numérico.
- Validar pré-condição de diálogo com base em marcador.

## Uso no ciclo social

No passo final do ciclo, marcadores devem refletir o resultado social:

- sucesso: ativa avanços, alianças e desbloqueios;
- falha: pode ativar bloqueios, suspeitas ou rotas alternativas.

Fluxo canônico:

`Escolha de fala → Teste social → Relação/Reputação → Diário/Marcadores`

## Convenções para evitar inconsistência

- Um marcador deve ter **uma única responsabilidade**.
- Evitar sinônimos para o mesmo estado (não duplicar semântica).
- Toda regra de diálogo condicionada por marcador deve citar o ID completo.
- Alterações de marcador devem ser registradas no diário quando afetarem progressão.
