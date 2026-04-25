# Auditoria de imagens pendentes (wiki legada)

Data da verificação: **2026-04-24**.

Após revisar as referências de imagem da documentação legada e consultar a API da wiki:

- `CasaDeCustodia.png` continua sem arquivo publicado.
- `MercadoDaPedraBranca.png` continua sem arquivo publicado.
- `CasaDeCustodia_SalaoDeAtendimento.png` continua sem arquivo publicado.

## Endereços validados

Os três arquivos acima não aparecem na listagem de arquivos (`list=allimages`) e também retornam sem `imageinfo` quando consultados por `titles=File:<nome-do-arquivo>`.

Links de arquivo validados (sem mídia disponível):

- `https://mysteryrealms.rodrigogml.eng.br/index.php?title=File:CasaDeCustodia.png`
- `https://mysteryrealms.rodrigogml.eng.br/index.php?title=File:MercadoDaPedraBranca.png`
- `https://mysteryrealms.rodrigogml.eng.br/index.php?title=File:CasaDeCustodia_SalaoDeAtendimento.png`

## Ajustes aplicados nos links da documentação

Foram corrigidas referências que apontavam para imagens específicas inexistentes, mas que possuem imagem-base válida no wiki:

- `PracaDasVozes_CirculoDosTresEcos.png` → `PracaDasVozes.png`
- `BibliotecaVarnak_SalaDeRecepcao.png` → `BibliotecaVarnak.png`
- `ArsenalTresLaminas_SalaoDaLoja.png` → `ArsenalTresLaminas.png`

Com isso, o download automático passou de 13 avisos para 3 avisos reais de ausência de mídia.
