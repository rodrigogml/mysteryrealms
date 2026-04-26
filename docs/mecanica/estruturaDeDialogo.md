# Estrutura de Diálogo

## Objetivo

Definir o formato canônico de cenas sociais, opções de fala, testes sociais e consequências sistêmicas.

## Ciclo social padronizado

Todo diálogo com impacto mecânico deve seguir, obrigatoriamente, o ciclo abaixo:

1. **Escolha de fala** pelo jogador (opção com estilo e intenção).
2. **Teste social** quando houver risco, oposição ou requisito.
3. **Alteração de relacionamento/reputação** com base no resultado.
4. **Registro no diário e em marcadores** para persistência narrativa.

Fluxo resumido:

`Escolha de fala → Teste social → Relação/Reputação → Diário/Marcadores`

## Estrutura mínima de um nó de diálogo

Cada nó deve declarar, no mínimo:

- `dialogoId`
- `npcId` (ou grupo/facção responsável)
- `texto`
- `opcoes[]`

Cada opção deve declarar:

- `opcaoId`
- `estiloFalaId`
- `testeSocial` (se aplicável)
- `efeitosSucesso`
- `efeitosFalha` (quando houver teste)

## Estrutura de opção (referência)

```yaml
dialogoId: dlg_taverna_primeiroContato_001
npcId: npc_kaelaRunaveia
texto: "A elfa observa em silêncio, aguardando sua postura."
opcoes:
  - opcaoId: op_001
    estiloFalaId: fala_diplomatica
    textoOpcao: "Quero conversar com respeito e ouvir sua versão."
    testeSocial:
      atributo: Carisma
      habilidade: Persuasao
      dificuldade: 14
    efeitosSucesso:
      relacionamento:
        alvoId: npc_kaelaRunaveia
        delta: +2
      reputacao:
        alvoId: loc_salaoDaAsaQuebrada
        delta: +1
      diario:
        entradaId: diary_kaela_primeiroContato_sucesso
      marcadores:
        - mk_kaela_confiouPrimeiroContato
    efeitosFalha:
      relacionamento:
        alvoId: npc_kaelaRunaveia
        delta: -1
      diario:
        entradaId: diary_kaela_primeiroContato_falha
```

## Regras de consistência

- O nó **sempre** deve terminar com atualização de estado social e persistência (diário/marcadores).
- Evitar efeitos “soltos” fora de `efeitosSucesso`/`efeitosFalha`.
- Se não houver teste social, aplicar diretamente os efeitos no passo 3 e ainda registrar no passo 4.

## Integrações

- Estilos de fala e valoração: `./estilosDeFalaEValoracao.md`
- Diário do jogador: `./diarioDoJogador.md`
- Sistema de marcadores: `./sistemaDeMarcadores.md`
