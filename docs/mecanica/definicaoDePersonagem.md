# Definição de Personagem

Este documento consolida, em formato Markdown, as definições legadas sobre personagem (jogador), atributos, ficha técnica e componentes diretamente relacionados.

## Escopo da definição do personagem

Com base na documentação legada:
- A construção inicial do personagem é definida pela combinação de **Raça + Classe**.
- Não há distribuição livre de pontos iniciais de atributos.
- A evolução posterior ocorre pelo sistema de progressão.

## Identidade da ficha do jogador

Campos de identidade mapeados:
- Nome
- Sobrenome
- Gênero (Masculino, Feminino, Outros)
- Raça
- Classe
- Idade Inicial

## Atributos principais

Atributos-base do personagem:
- Força
- Destreza
- Constituição
- Intelecto
- Percepção
- Carisma
- Vontade

### Papel dos atributos

- **Força:** potência física, carga, dano corporal.
- **Destreza:** agilidade, reflexos, precisão motora.
- **Constituição:** robustez, vida, resistência física.
- **Intelecto:** raciocínio, memória, conhecimento técnico/arcano.
- **Percepção:** observação, detecção de ameaças e detalhes.
- **Carisma:** presença social, persuasão, influência interpessoal.
- **Vontade:** foco mental, disciplina, resistência mental.

## Habilidades associadas

Habilidades listadas na base legada:
- Persuasão [Carisma]
- Intimidação [Carisma]
- Enganação [Carisma]
- Conhecimento (Arcano, História, Relíquias) [Intelecto]
- Herbalismo / Alquimia [Intelecto]
- Furtividade [Destreza]
- Sobrevivência [Percepção]
- Manuseio de Armas [Força ou Destreza]
- Uso de Magia [Vontade ou Intelecto]

Regra geral legada: quando houver habilidade específica, usar habilidade; se não houver, usar teste direto de atributo principal.

## Estado do personagem

Atributos de estado referenciados:
- XP Acumulado
- Pontos de Vida
- Fadiga
- Moral (escala 0 a 100)

## Inventário e recursos

Campos previstos:
- Moedas
- Itens Equipados
- Itens na Mochila
- Montaria (com impacto em transporte e carga)

## Ficha técnica (atributos derivados)

### Atributos de carga

- **Capacidade de Carga Máxima (Jogador):**
  - Fórmula: `Força × 10` (kg)
- **Capacidade de Carga Máxima da Montaria:**
  - Valor vindo da ficha da montaria
- **Capacidade de Carga Atual:**
  - Montado: `(Carga Máxima da Montaria - Peso do Jogador) + Modificadores da Montaria`
  - Não montado: `(Carga Máxima Jogador + Carga Máxima Montaria) + Modificadores`
- **Carga Crítica Atual:**
  - Fórmula: `Capacidade de Carga Atual × 1,5`
- **Carga Atual:**
  - Soma dos pesos de equipados + mochila + moedas

### Peso do personagem

- **Peso (kg):**
  - Fórmula: `PesoBase(raça/gênero) × (1 + ((CON - 3) × 0,05))`

### Vida, fadiga e combate

- **Pontos de Vida Máximo:** `CON × 10`
- **Fadiga Máxima / Fadiga Mínima / Exaustão:** definidos no sistema de Fadiga, Fome e Sede
- **Precisão:** `1d20 + Destreza + Bônus de Precisão da Arma + Modificadores`
- **Dano Base:** `Dado da Arma + Modificador de Atributo + Bônus de Raça/Classe`
- **Dano Atual:** derivado de Dano Base + modificadores ativos
- **Defesa:** composta por esquiva (Destreza), equipamentos e modificadores
- **Bloqueio:** soma de bônus de bloqueio de equipamentos + modificadores

## Raças jogáveis e características-base

Raças listadas:
- Humano
- Elfo
- Meio-elfo
- Anão
- Meio-orc
- Tiefling
- Draconato
- Halfling

Cada raça define:
- distribuição-base dos 7 atributos principais;
- bônus em habilidades específicas;
- resistências e traços raciais;
- tabela de peso por gênero.

## Classes e papel mecânico

Classes listadas:
- Combate: Guerreiro, Caçador, Duelista
- Magia: Mago, Alquimista, Conjurador Elemental
- Sociais/Suporte: Bardo, Clérigo, Sábio
- Furtivas/Manipulação: Ladrão, Assassino, Ilusionista

Cada classe define:
- bônus iniciais de atributos;
- bônus em habilidades específicas;
- estilo de jogo e papel funcional.

## Relacionamento e reputação

Sistema social da ficha contempla:
- Relacionamento com NPCs (escala -100 a 100)
- Faixas: Inimigo Mortal, Hostil, Neutra, Favorável, Aliado
- Reputação por localidade e por grupos/facções

## Referências legadas consultadas

- `docs/legado/fichaDoJogador.wiki`
- `docs/legado/racas.wiki`
- `docs/legado/classes.wiki`
- `docs/legado/testesDeJogo.wiki`
- `docs/legado/moral.wiki`
- `docs/legado/modificadores.wiki`
- `docs/legado/recuperacao.wiki`
- `docs/legado/morteEDesmaios.wiki`
