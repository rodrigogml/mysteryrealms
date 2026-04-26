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

## Estrutura de Pastas
Cada tipo de documento tem uma pasta fixa dentro de `/docs`. O agente nunca deve inferir nem criar pastas fora deste mapeamento sem autorização:

| Tipo de documento | Pasta |
|---|---|
| Mecânica do Jogo | `/docs/mecanica/` |
| Definições do Mundo (classes, atributos, localidades, NPCs etc.) | `/docs/mundo/` |
| Requisitos do Sistema | `/docs/requisitos/` |
| Documentação de apoio da implementação | `/docs/implementacao/` |
| Documentação legada (somente leitura, nunca alterar) | `/docs/legado/` |
| Índice central de toda a documentação | `/docs/index.md` |

- Se a pasta do tipo necessário ainda não existir, criá-la antes de criar o arquivo.
- Nunca criar arquivos de documentação na raiz de `/docs` (exceto `index.md`).

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

# Banco de Dados

- O banco de dados da aplicação será MySQL 9.0, acessado através do ORM/JPA padrão do Spring (Hibernate).
- O schema da aplicação será `mysteryrealms`.
- Dentro da pasta `src/main/resources/db/init` deve ser incluído o script `init.sql` de criação do banco de dados. Esse script não deve utilizar sintaxes do tipo `IF NOT EXISTS`; deve considerar sempre a criação do banco de dados do zero.
  - O script não deve conter instruções de `ALTER TABLE` ou `UPDATE` (seja de estrutura ou de dados) quando for possível corrigir diretamente o `CREATE TABLE` ou o `INSERT` original, de forma que a criação do banco seja feita corretamente desde a primeira execução. A exceção são casos de relacionamento circular, nos quais o `ALTER TABLE` é necessário após a criação das tabelas envolvidas.
- Nenhum script de atualização para bases existentes deve ser criado no repositório.
- O banco de dados deverá seguir as seguintes regras de nomenclatura:
  - Nomes de tabelas, colunas, FKs, etc. devem sempre estar em inglês. Apenas comentários, se necessários, podem estar em português.
  - Nomes de tabelas e colunas devem estar no singular e seguir o padrão camelCase sem a utilização de `_` ou `-`. Exemplos: `user`, `userGroup`.
  - O nome da coluna no banco de dados deve ser exatamente igual ao nome do campo correspondente na Entity Java, em camelCase e sem `_` ou `-`.
  - A chave primária da tabela deve se chamar `id` e ser do tipo `BIGINT AUTO_INCREMENT`.
  - FKs devem sempre ter o prefixo `id` concatenado ao nome da tabela referenciada. Exemplo: `idUser`.
    - Casos de dois ou mais relacionamentos para a mesma tabela: utilizar o prefixo `id` concatenado com o significado do relacionamento, sem a necessidade de incluir o nome da tabela. Exemplos: `idPreviousItem` e `idNextItem`, imaginando um relacionamento linear entre objetos da mesma tabela.
  - Tabelas de relacionamento N:N devem ter o nome das tabelas que relacionam separados por `_`. Exemplo: `user_userGroup`. A utilização de `_` nesse caso é uma exceção prevista, com o objetivo de facilitar a identificação visual das tabelas de relacionamento em relação às tabelas de dados.
  - Tipos de enumeração Java (`enum`) devem ser persistidos no banco pelo nome do valor da enum, em colunas do tipo `VARCHAR(50)`.
