# Imagens da documentação legada

As imagens da wiki **não são versionadas individualmente** no repositório.

Para restaurar os arquivos em `docs/legado/images/`, execute na raiz do projeto:

```bash
python download_legacy_images.py
```

O script busca todas as referências de imagem em `docs/legado/*.wiki` e baixa os arquivos disponíveis da wiki para este diretório.
