# Tipos de Armas

Tipos de Armas são agrupamentos mecânicos definidos pelo jogo para classificar armas com comportamento semelhante.

## Finalidade da classificação

- aplicar afinidade por classe/raça sem configurar arma por arma;
- padronizar bônus e penalidades por grupo;
- manter consistência de atributo principal, alcance e estilo de uso.

## Estrutura mínima de um tipo

Cada tipo deve declarar:
- Nome do Tipo
- Função (leve, pesada, distância, foco mágico etc.)
- Atributo Primário (`forca`, `destreza`, `intelecto`, etc.)
- Mãos Comuns (`1`/`2`)
- Alcance Padrão
- Perfil crítico
- Compatibilidades típicas (classe/raça)

## Uso nos sistemas

O tipo de arma influencia:
- testes de ataque e dano;
- bônus de proficiência por classe/raça;
- regras específicas de combate em `docs/mecanica/cicloDeBatalha.md`.

## Referências legadas consultadas

- `docs/legado/tiposDeArmas.wiki`
