# Estrutura de Localização

Este documento define a hierarquia espacial e os **contratos mínimos obrigatórios** para modelagem de mapa e navegação.

## Hierarquia canônica

- Mundo
  - Continente
    - Domínio
      - Região
        - Localidade
          - Zona
            - Ambiente

`Zona` e `Ambiente` são os níveis navegáveis diretos pelo jogador.

## Contrato mínimo: Zona

| Campo | Tipo | Obrigatório | Regra |
|---|---|---|---|
| `id_zona` | string | Sim | Único no mundo. |
| `nome` | string | Sim | Nome exibível. |
| `id_localidade` | string | Sim | Localidade pai. |
| `coord_x` | decimal | Sim | Coordenada cartesiana global. |
| `coord_y` | decimal | Sim | Coordenada cartesiana global. |
| `tipo_navegavel` | enum | Sim | Valor fixo `zona`. |
| `acessivel` | boolean | Sim | Se pode ser usada como origem/destino no momento. |

## Contrato mínimo: Ambiente

| Campo | Tipo | Obrigatório | Regra |
|---|---|---|---|
| `id_ambiente` | string | Sim | Único no mundo. |
| `nome` | string | Sim | Nome exibível. |
| `id_zona` | string | Sim | Zona pai. |
| `coord_x` | decimal | Sim | Coordenada global para cálculo de distância. |
| `coord_y` | decimal | Sim | Coordenada global para cálculo de distância. |
| `tipo_navegavel` | enum | Sim | Valor fixo `ambiente`. |
| `acessivel` | boolean | Sim | Se pode ser usado como origem/destino no momento. |

## Contrato mínimo: Conexão

| Campo | Tipo | Obrigatório | Regra |
|---|---|---|---|
| `id_conexao` | string | Sim | Único no mundo. |
| `origem_id` | string | Sim | ID de Zona ou Ambiente. |
| `destinos_priorizados` | lista<string> | Sim | Pelo menos 1 destino válido. |
| `bidirecional` | boolean | Sim | Se cria fluxo reverso automático. |
| `classificacao` | enum | Sim | `pacificado`, `hostil` ou `selvagem`. |
| `penalidade_rota_pct` | decimal | Sim | Entre `0` e `15`. |
| `chance_interrupcao_km_pct` | decimal | Sim | Entre `0` e `100`. |
| `tabela_interrupcoes_id` | string | Sim | Referência para sorteio de interrupções. |

## Contrato mínimo: Cluster

Cluster é uma organização para navegação livre regional (macroestrutura de conexões).

| Campo | Tipo | Obrigatório | Regra |
|---|---|---|---|
| `id_cluster` | string | Sim | Único no escopo da localidade. |
| `nome` | string | Sim | Nome de referência operacional. |
| `id_localidade` | string | Sim | Localidade dona do cluster. |
| `pontos` | lista<string> | Sim | IDs de Zonas/Ambientes pertencentes. |
| `conexoes` | lista<string> | Sim | IDs de conexões do cluster. |
| `modo_navegacao` | enum | Sim | Ex.: `livre`, `guiada`. |

## Regras de consistência

1. Todo `origem_id` e `destino` de conexão deve existir e ser navegável.
2. Coordenadas devem estar no mesmo sistema global (malha cartesiana única).
3. Se `bidirecional = true`, a volta deve preservar validações de acesso.
4. A modelagem deve evitar contradição de custo entre rota direta e indireta.

## Exemplo Langur (recorte)

```yaml
zona:
  id_zona: zona_langur_praca_das_vozes
  nome: Praça das Vozes
  id_localidade: loc_langur
  coord_x: 0.8500
  coord_y: 0.2000
  tipo_navegavel: zona
  acessivel: true

ambiente:
  id_ambiente: amb_langur_praca_fonte_central
  nome: Fonte Central
  id_zona: zona_langur_praca_das_vozes
  coord_x: 0.8520
  coord_y: 0.2040
  tipo_navegavel: ambiente
  acessivel: true

conexao:
  id_conexao: cx_praca_para_biblioteca
  origem_id: zona_langur_praca_das_vozes
  destinos_priorizados:
    - zona_langur_biblioteca_varnak
    - zona_langur_anel_central_portao_leste
  bidirecional: true
  classificacao: pacificado
  penalidade_rota_pct: 8
  chance_interrupcao_km_pct: 6
  tabela_interrupcoes_id: int_langur_urbano

cluster:
  id_cluster: cluster_langur_anel_central
  nome: Anel Central de Langur
  id_localidade: loc_langur
  pontos:
    - zona_langur_biblioteca_varnak
    - zona_langur_conselho_dos_cinco
    - zona_langur_guilda_de_magia
  conexoes:
    - cx_praca_para_biblioteca
    - cx_biblioteca_para_conselho
  modo_navegacao: livre
```
