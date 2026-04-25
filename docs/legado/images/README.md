# Imagens da documentação legada

As imagens da wiki **não são versionadas individualmente** no repositório.

Para restaurar os arquivos em `docs/legado/images/`, execute na raiz do projeto:

```bash
python download_legacy_images.py
```

O script busca todas as referências de imagem em `docs/legado/*.wiki` e baixa os arquivos disponíveis da wiki para este diretório.

Comportamentos adicionais do script:

- Ignora automaticamente placeholders de modelos (`NomeDo*.png` / `NomeDa*.png`).
- Para referências de ambientes no formato `Zona_Ambiente.png`, tenta fallback para `Zona.png` quando o arquivo específico não existe no wiki.
