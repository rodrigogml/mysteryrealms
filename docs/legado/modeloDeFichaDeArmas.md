A ficha de arma é utilizada para descrever armas no universo do jogo. Armas são itens de mão do subtipo **Arma** e sua função primária é o **ataque**. Podem ser usadas para combates corpo a corpo, à distância ou mágicos, variando em alcance, peso e forma de utilização.

Cada arma deve ser descrita em uma ficha própria utilizando o modelo abaixo. As instruções de preenchimento estão descritas ao lado de cada campo.

<pre>
> **Imagem de referência indisponível no export:** `NomeDaArma.png`

**Atributos do Item de Mão:**
- Incluir todos os atributos definidos na página [Modelo de Ficha de Itens de Mão](modeloDeFichaDeItensDeMao.md), na ordem e fielmente como mandam suas instruções.

**Atributos Específicos do Subtipo Arma:**
- **Qualidade:** Define a qualidade da arma, se utilizado pelo jogo. A mesma arma pode ter qualidades diferentes, como se tivessem um defeito de fabricação ou uma qualidade rara, que confere algum modificador nos seus atributos. Para definir este campo deve-se conferir a documentação de definição do jogo.
- **Tipo de Arma:** Tipo de arma ao qual pertence. Ex: Espada Curta, Machado, Cajado
- **Classe de Dano:** Tipo de dano causado. Ex: Corte, Perfuração, Esmagamento, Fogo, etc.
- **Dado de Dano Base:** Dado utilizado para calcular o dano básico. Ex: 1d6
- **Atributo Primário:** Atributo usado para cálculo de ataque e dano. Ex: Força, Destreza, Intelecto
- **Alcance:** Corpo a corpo (adjacente), Curta Distância (5m), Longa Distância (15m), etc.
- **Crítico:** Intervalo e multiplicador de acerto crítico. Ex: 19–20 / x2
- **Propriedades Especiais:** Efeitos ou regras adicionais. Ex: Pode ser arremessada, bônus contra armaduras, ataque furtivo, etc.
- **Classes e Raças com Bônus:**
:* **Classes:** Lista de classes com bônus e tipo de bônus. Ex: Duelista (+1), Ladrão (+2).
:* **Raças:** Lista de raças com bônus e tipo de bônus. Ex: Meio-elfo (+1), Anão (+1).
</pre>

# Observações Técnicas
- O campo **Tipo de Arma** deve referenciar uma entrada existente nas definições de tipo de arma do jogo.
- A **Classe de Dano** deve estar em conformidade com os tipos definidos na página [Danos e Aflições](danosEAflicoes.md).
- Armas mágicas ou com propriedades especiais devem indicar seus efeitos em **Propriedades Especiais**.
- A **Mão necessária** interfere no que pode ser carregado simultaneamente. Armas de duas mãos impedem o uso de outro item simultâneo.
- O **Peso** influencia em fadiga, velocidade e mobilidade durante o combate.

# Organização por Tipo
As armas devem estar associadas a um Tipo de Arma, que agrupa modelos com características semelhantes (ex: Espadas Curtas, Maças, Arcos Longos). Essa classificação é essencial para o cálculo de bônus por classe, talentos e habilidades.

# Tabela de Compatibilidade por Classe e Raça
As tabelas de bônus por classe e raça para cada tipo de arma devem ser documentadas pelo jogo.
