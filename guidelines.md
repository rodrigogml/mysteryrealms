# Guidelines do Repositório

## Diretrizes de organização de código, documentação e repositório

### 1. Documentação oficial do projeto
Toda documentação deverá ser escrita dentro da pasta `/docs`, em formato Markdown GitHub Flavored (`.md`).

Cada arquivo `.md` criado em `/docs` deverá ser adicionado no índice da documentação `index.md`.

Os arquivos de documentação devem seguir o padrão **camelCase**, sem uso de `-` (hífen) ou `_` (underscore) no nome do arquivo (exemplo: `guiaDeCombate.md`).

O `index.md` deve conter:
- Um link para a página principal de cada documento;
- Abaixo do link principal, uma estrutura de tópicos com links por âncora para as seções do arquivo `.md`.

#### Modelo de exemplo para o `docs/index.md`

```md
# Índice da Documentação

## [Guia de Combate](./guiaDeCombate.md)
- [Visão Geral](./guiaDeCombate.md#visão-geral)
- [Mecânicas Básicas](./guiaDeCombate.md#mecânicas-básicas)
- [Exemplos](./guiaDeCombate.md#exemplos)

## [Guia de Criação de Personagem](./guiaCriacaoDePersonagem.md)
- [Introdução](./guiaCriacaoDePersonagem.md#introdução)
- [Atributos](./guiaCriacaoDePersonagem.md#atributos)
- [Classes](./guiaCriacaoDePersonagem.md#classes)
```

### 2. Documentação legada
Em `/docs/legado/` encontramos arquivos `.wiki`, que são a documentação legada do projeto.

Esses arquivos devem ser utilizados apenas para consulta e **nunca** devem ser alterados.
