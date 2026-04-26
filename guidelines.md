# Guidelines do Repositório

Este documento reúne as definições que devem ser seguidas integralmente em todo o repositório.

# Ordem de Prioridade
- As definições da `Mecânica do Jogo` têm a maior prioridade conceitual do projeto.
- Os `Requisitos do Sistema` devem derivar da `Mecânica do Jogo` e nunca a contradizer.
- O código do sistema deve implementar os `Requisitos do Sistema` e, indiretamente, respeitar a `Mecânica do Jogo`.
- A documentação deve refletir com precisão a `Mecânica do Jogo`, os `Requisitos do Sistema` e o código implementado, sem criar conflitos entre eles.
- Em caso de conflito entre artefatos, deve ser respeitada esta ordem de prioridade:
  1. `Mecânica do Jogo`
  2. `Requisitos do Sistema`
  3. Código do sistema
  4. Documentação de apoio da implementação
- Nenhum artefato de menor prioridade pode sobrescrever, reinterpretar ou invalidar um artefato de maior prioridade sem autorização explícita do usuário.

# Documentação
- Toda a documentação do projeto deve ficar na pasta `/docs`.
- Toda a documentação deve ser escrita na pasta `/docs`, em formato Markdown GitHub Flavored (`.md`).
- Toda criação ou alteração de arquivos de documentação, bem como de seções e subseções, deve ser refletida imediatamente em `index.md`.
- Todos os arquivos de documentação devem ter nome normalizado em estilo camelCase, sem acentos e sem uso de `-` ou `_`. Exemplo: `guiaDeCombate.md`.

## Índice da Documentação
- Dentro de `/docs` deve ser mantido o arquivo `index.md`, com o objetivo de centralizar um índice único e geral de toda a documentação da pasta, independentemente das subpastas e da divisão interna do conteúdo.
- O índice pode conter seções e subseções para separar as páginas por assuntos e tópicos, sempre visando facilitar a navegação.
- O índice deve incluir um link para todo arquivo `.md` dentro da pasta `/docs`. Abaixo de cada link, deve haver uma estrutura de tópicos com links do tipo âncora para todas as seções e subseções internas do arquivo.

Exemplo de estrutura esperada em `index.md`:

```md
# Índice da Documentação
Índice de toda a documentação do projeto/repositório.

# Mecânica do Jogo

Documentação relacionada às definições da mecânica do jogo, isto é, fórmulas, funcionamento, lógica, condições etc.

- [Página 1](./mecanica/pagina1.md)
  - [Sessão nível 1](./mecanica/pagina1.md#sessao-nivel-1)
    - [Subseção nível 1](./mecanica/pagina1.md#subsecao-nivel-1)
  - [Sessão nível 2](./mecanica/pagina1.md#sessao-nivel-2)


- [Página 2](./mecanica/pagina2.md)


# Mundo de Mystery Realms

Documentação relacionada às definições do mundo, como classes existentes e seus atributos, localidades, NPCs etc. São as definições que transformam a mecânica do jogo no ambiente jogável de Mystery Realms.

- [Página 1](./mundo/pagina1.md)


# Requisitos do Sistema

Documentação dos requisitos do sistema.

- [Página 1](./requisitos/pagina1.md)
  - [Sessão nível 1](./requisitos/pagina1.md#sessao-nivel-1)
    - [Subseção nível 1](./requisitos/pagina1.md#subsecao-nivel-1)
  - [Sessão nível 2](./requisitos/pagina1.md#sessao-nivel-2)


- [Página 2](./requisitos/pagina2.md)


# Implementação do Sistema

Documentação relevante sobre as definições da implementação do sistema.


- [Página 1](./implementacao/pagina1.md)
```

## Documentação Legada
- Em `/docs/legado/` há arquivos `.wiki`, que constituem a documentação legada inicial do projeto.
- Esses arquivos devem ser utilizados apenas para consulta e consideração na evolução da mecânica do jogo atual. **Nunca** devem ser alterados.
- A documentação legada serve como guia e referência de mecânica, mas não deve ser tratada como verdade absoluta nem como regra obrigatória. Ela pode ser evoluída, alterada ou simplificada ao formular recomendações para novas definições do projeto atual.
