# Modificadores

Modificadores são efeitos temporários que aplicam bônus e/ou penalidades sobre atributos, habilidades e resultados de teste.

## Padrão de registro

Cada modificador deve conter:
- `id` técnico (slug sem acento em `snake_case`);
- nome de exibição;
- gatilho de aplicação;
- efeito mecânico objetivo;
- duração;
- regra de empilhamento.

## Prioridade e empilhamento

Ordem canônica de prioridade (maior para menor):
1. estado crítico de combate;
2. habilidade ativa;
3. equipamento;
4. classe;
5. raça;
6. efeito temporário genérico.

Regras:
- Modificadores de mesma origem e mesmo tipo acumulam, salvo exceção explícita.
- Se houver conflito excludente no mesmo nível, aplica-se maior magnitude absoluta.
- Empate de magnitude: vence menor duração restante.
- Novo empate: vence o efeito mais recente.

## Convenção de termos

- Atributos, habilidades, danos, aflições e resistências devem seguir exatamente o glossário canônico em `docs/mecanica/glossario.md`.
- Exibição com acento; chave técnica sem acento.

## Exemplos de IDs técnicos

- `exaustao`
- `sede`
- `sede_agravada`
- `fome`
- `fome_agravada`
- `fadiga_elevada`
- `mal_estar_temporario`
- `recuperacao_reduzida`
