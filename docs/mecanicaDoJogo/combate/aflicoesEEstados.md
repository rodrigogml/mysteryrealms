# Aflições e Estados

## Modelo de Estado

Cada estado deve declarar:

- **Categoria**: controle, dano contínuo, penalidade, utilitário.
- **Duração**: em turnos (ou permanente até remoção).
- **Empilhamento**: substitui, acumula intensidade, acumula duração.
- **Resistência**: teste inicial e/ou recorrente por turno.
- **Remoção**: item, habilidade, descanso, evento narrativo.
- **Janela de efeito**: início de turno, durante turno, fim de turno.

## Regras Padrão

1. Estados com mesmo ID seguem a regra de empilhamento definida.
2. Se múltiplos estados alterarem o mesmo atributo, aplicar na ordem:
   1. multiplicadores
   2. bônus/penalidades fixas
   3. limites mínimos/máximos
3. Efeitos de início de turno são resolvidos antes de ações do personagem.
4. Efeitos de fim de turno são resolvidos após ação principal e reações pendentes.

## Estados Base (catálogo inicial)

- **Sangramento**: dano contínuo no início do turno.
- **Envenenado**: dano contínuo + penalidade de recuperação.
- **Atordoado**: perde ação principal no próximo turno.
- **Amedrontado**: penalidade de acerto e limitação de aproximação.
- **Cegueira**: penalidade severa de precisão e percepção visual.

## Prioridade de Processamento

1. Verificações de expiração.
2. Efeitos de início de turno.
3. Ações do turno.
4. Efeitos de fim de turno.
5. Testes de manutenção/remoção.
