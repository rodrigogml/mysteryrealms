# Modelos de Ficha

Este documento define modelos mínimos de ficha para cada nível da hierarquia canônica de localização.

## Hierarquia canônica coberta

- Mundo
- Continente
- Domínio
- Região
- Localidade
- Zona
- Ambiente

## Regras de nomenclatura e IDs

As regras abaixo são compatíveis com o contrato de localização, que exige IDs únicos para níveis navegáveis e referências explícitas ao pai (`id_localidade` em Zona e `id_zona` em Ambiente).

1. Usar apenas minúsculas, números e `_`.
2. Nunca usar espaços, acentos ou caracteres especiais em IDs.
3. Prefixos obrigatórios por tipo:
   - `mundo_`
   - `cont_`
   - `dom_`
   - `reg_`
   - `loc_`
   - `zona_`
   - `amb_`
4. O sufixo do ID deve representar o caminho hierárquico mínimo.
   - Exemplo: `zona_langur_praca_das_vozes`.
5. Campos de referência devem apontar para IDs existentes:
   - Continente -> `id_mundo`
   - Domínio -> `id_continente`
   - Região -> `id_dominio`
   - Localidade -> `id_regiao`
   - Zona -> `id_localidade`
   - Ambiente -> `id_zona`
6. `tipo_navegavel` só pode existir em Zona e Ambiente, com valores fixos (`zona` e `ambiente`).

## Template mínimo por nível

### Mundo

```yaml
mundo:
  id_mundo: mundo_terras_centrais
  nome: Terras Centrais
```

### Continente

```yaml
continente:
  id_continente: cont_aurora
  nome: Aurora
  id_mundo: mundo_terras_centrais
```

### Domínio

```yaml
dominio:
  id_dominio: dom_langur
  nome: Domínio de Langur
  id_continente: cont_aurora
```

### Região

```yaml
regiao:
  id_regiao: reg_langur_central
  nome: Langur Central
  id_dominio: dom_langur
```

### Localidade

```yaml
localidade:
  id_localidade: loc_langur
  nome: Cidade de Langur
  id_regiao: reg_langur_central
```

### Zona

```yaml
zona:
  id_zona: zona_langur_praca_das_vozes
  nome: Praça das Vozes
  id_localidade: loc_langur
  coord_x: 0.8500
  coord_y: 0.2000
  tipo_navegavel: zona
  acessivel: true
```

### Ambiente

```yaml
ambiente:
  id_ambiente: amb_langur_praca_fonte_central
  nome: Fonte Central
  id_zona: zona_langur_praca_das_vozes
  coord_x: 0.8520
  coord_y: 0.2040
  tipo_navegavel: ambiente
  acessivel: true
```

## Campos obrigatórios, opcionais e validações

### Mundo

| Campo | Tipo | Obrigatório | Regra de validação |
|---|---|---|---|
| `id_mundo` | string | Sim | Único global; prefixo `mundo_`. |
| `nome` | string | Sim | Nome exibível não vazio. |
| `descricao` | string | Não | Texto curto de contexto. |

### Continente

| Campo | Tipo | Obrigatório | Regra de validação |
|---|---|---|---|
| `id_continente` | string | Sim | Único no mundo; prefixo `cont_`. |
| `nome` | string | Sim | Nome exibível não vazio. |
| `id_mundo` | string | Sim | Deve existir em `id_mundo`. |
| `tags` | lista<string> | Não | Sem duplicidade de item. |

### Domínio

| Campo | Tipo | Obrigatório | Regra de validação |
|---|---|---|---|
| `id_dominio` | string | Sim | Único no continente; prefixo `dom_`. |
| `nome` | string | Sim | Nome exibível não vazio. |
| `id_continente` | string | Sim | Deve existir em `id_continente`. |
| `governanca` | string | Não | Valor textual livre padronizado pelo projeto. |

### Região

| Campo | Tipo | Obrigatório | Regra de validação |
|---|---|---|---|
| `id_regiao` | string | Sim | Único no domínio; prefixo `reg_`. |
| `nome` | string | Sim | Nome exibível não vazio. |
| `id_dominio` | string | Sim | Deve existir em `id_dominio`. |
| `bioma` | string | Não | Recomenda-se enum interno do projeto. |

### Localidade

| Campo | Tipo | Obrigatório | Regra de validação |
|---|---|---|---|
| `id_localidade` | string | Sim | Único na região; prefixo `loc_`. |
| `nome` | string | Sim | Nome exibível não vazio. |
| `id_regiao` | string | Sim | Deve existir em `id_regiao`. |
| `populacao_estimada` | inteiro | Não | Valor maior ou igual a `0`. |

### Zona

| Campo | Tipo | Obrigatório | Regra de validação |
|---|---|---|---|
| `id_zona` | string | Sim | Único no mundo; prefixo `zona_`. |
| `nome` | string | Sim | Nome exibível não vazio. |
| `id_localidade` | string | Sim | Deve existir em `id_localidade`. |
| `coord_x` | decimal | Sim | Coordenada global cartesiana. |
| `coord_y` | decimal | Sim | Coordenada global cartesiana. |
| `tipo_navegavel` | enum | Sim | Valor fixo `zona`. |
| `acessivel` | boolean | Sim | Define disponibilidade como origem/destino. |
| `descricao` | string | Não | Texto curto de ambientação. |

### Ambiente

| Campo | Tipo | Obrigatório | Regra de validação |
|---|---|---|---|
| `id_ambiente` | string | Sim | Único no mundo; prefixo `amb_`. |
| `nome` | string | Sim | Nome exibível não vazio. |
| `id_zona` | string | Sim | Deve existir em `id_zona`. |
| `coord_x` | decimal | Sim | Coordenada global cartesiana. |
| `coord_y` | decimal | Sim | Coordenada global cartesiana. |
| `tipo_navegavel` | enum | Sim | Valor fixo `ambiente`. |
| `acessivel` | boolean | Sim | Define disponibilidade como origem/destino. |
| `descricao` | string | Não | Texto curto de ambientação. |

## Validações de consistência entre níveis

1. Toda ficha deve referenciar um pai existente e do tipo correto.
2. Não pode haver ciclo hierárquico (um nó não pode ser ancestral de si próprio).
3. O caminho completo de um Ambiente deve resolver até Mundo sem lacunas.
4. IDs de Zona e Ambiente devem ser únicos globalmente.
5. `coord_x` e `coord_y` de Zona e Ambiente devem usar a mesma malha global.
6. `tipo_navegavel` deve ser coerente com o tipo da ficha.

## Exemplos reduzidos em YAML/Markdown por tipo

### Mundo (Markdown)

```md
- id_mundo: mundo_terras_centrais
- nome: Terras Centrais
```

### Continente (YAML)

```yaml
id_continente: cont_aurora
nome: Aurora
id_mundo: mundo_terras_centrais
```

### Domínio (YAML)

```yaml
id_dominio: dom_langur
nome: Domínio de Langur
id_continente: cont_aurora
```

### Região (YAML)

```yaml
id_regiao: reg_langur_central
nome: Langur Central
id_dominio: dom_langur
```

### Localidade (Markdown)

```md
- id_localidade: loc_langur
- nome: Cidade de Langur
- id_regiao: reg_langur_central
```

### Zona (YAML)

```yaml
id_zona: zona_langur_praca_das_vozes
nome: Praça das Vozes
id_localidade: loc_langur
coord_x: 0.8500
coord_y: 0.2000
tipo_navegavel: zona
acessivel: true
```

### Ambiente (YAML)

```yaml
id_ambiente: amb_langur_praca_fonte_central
nome: Fonte Central
id_zona: zona_langur_praca_das_vozes
coord_x: 0.8520
coord_y: 0.2040
tipo_navegavel: ambiente
acessivel: true
```
