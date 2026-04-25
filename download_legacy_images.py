#!/usr/bin/env python3
"""Baixa imagens referenciadas pela documentação legada da wiki.

Uso:
  python download_legacy_images.py
"""

from __future__ import annotations

import json
import re
import urllib.parse
import urllib.request
from pathlib import Path
from urllib.parse import unquote, urlsplit

BASE_API = "https://mysteryrealms.rodrigogml.eng.br/api.php"
ROOT = Path(__file__).resolve().parent
LEGADO_DIR = ROOT / "docs" / "legado"
WIKI_GLOB = "*.wiki"
IMAGES_DIR = LEGADO_DIR / "images"

IMAGE_TAG_RE = re.compile(r"\[\[(?:Arquivo|File|Imagem|Image):([^\]|]+)(?:\|[^\]]*)?\]\]", re.IGNORECASE)
PLACEHOLDER_IMAGE_RE = re.compile(r"^NomeDa|^NomeDo", re.IGNORECASE)

# Mapeamentos validados na wiki atual para casos em que o nome referenciado
# segue padrão de subambiente, mas apenas a imagem do ambiente pai está
# publicada.
IMAGE_ALIASES: dict[str, str] = {
    "PracaDasVozes_CirculoDosTresEcos.png": "PracaDasVozes.png",
    "BibliotecaVarnak_SalaDeRecepcao.png": "BibliotecaVarnak.png",
    "ArsenalTresLaminas_SalaoDaLoja.png": "ArsenalTresLaminas.png",
}


def collect_referenced_images() -> list[str]:
    names: list[str] = []
    seen: set[str] = set()

    for wiki_file in sorted(LEGADO_DIR.glob(WIKI_GLOB)):
        text = wiki_file.read_text(encoding="utf-8", errors="ignore")
        for match in IMAGE_TAG_RE.finditer(text):
            image_name = match.group(1).strip()
            key = image_name.casefold()
            if key in seen:
                continue
            seen.add(key)
            names.append(image_name)

    return names


def mediawiki_image_url(image_name: str) -> str | None:
    params = {
        "action": "query",
        "titles": f"File:{image_name}",
        "prop": "imageinfo",
        "iiprop": "url",
        "format": "json",
        "formatversion": "2",
    }
    url = f"{BASE_API}?{urllib.parse.urlencode(params)}"

    with urllib.request.urlopen(url, timeout=30) as response:
        payload = json.load(response)

    pages = payload.get("query", {}).get("pages", [])
    if not pages:
        return None

    image_info = pages[0].get("imageinfo")
    if not image_info:
        return None

    return image_info[0].get("url")


def image_candidates(image_name: str) -> list[str]:
    candidates: list[str] = [image_name]

    alias = IMAGE_ALIASES.get(image_name)
    if alias and alias not in candidates:
        candidates.append(alias)

    if "_" in image_name:
        parent_name = f"{image_name.split('_', 1)[0]}.png"
        if parent_name not in candidates:
            candidates.append(parent_name)

    return candidates


def download_image(image_name: str) -> tuple[bool, str]:
    if PLACEHOLDER_IMAGE_RE.match(image_name):
        return False, f"placeholder de modelo (ignorado): {image_name}"

    tried_names: list[str] = []
    image_url: str | None = None
    selected_name = image_name

    for candidate in image_candidates(image_name):
        tried_names.append(candidate)
        image_url = mediawiki_image_url(candidate)
        if image_url:
            selected_name = candidate
            break

    if not image_url:
        tried = ", ".join(tried_names)
        return False, f"indisponível no wiki: {image_name} (tentativas: {tried})"

    file_name = unquote(Path(urlsplit(image_url).path).name).replace(" ", "_")
    destination = IMAGES_DIR / file_name

    with urllib.request.urlopen(image_url, timeout=60) as response:
        destination.write_bytes(response.read())

    if selected_name != image_name:
        return True, f"{file_name} (resolvido via alias: {selected_name})"

    return True, file_name


def main() -> int:
    if not LEGADO_DIR.exists():
        raise SystemExit(f"Diretório não encontrado: {LEGADO_DIR}")

    IMAGES_DIR.mkdir(parents=True, exist_ok=True)

    referenced_images = collect_referenced_images()
    if not referenced_images:
        print("Nenhuma referência de imagem encontrada em docs/legado/*.wiki")
        return 0

    downloaded = 0
    missing: list[str] = []

    for image_name in referenced_images:
        ok, result = download_image(image_name)
        if ok:
            downloaded += 1
            print(f"[OK] {image_name} -> images/{result}")
        else:
            missing.append(result)
            print(f"[WARN] {result}")

    print("\nResumo")
    print(f"- Referências únicas encontradas: {len(referenced_images)}")
    print(f"- Imagens baixadas: {downloaded}")
    print(f"- Não encontradas no wiki: {len(missing)}")

    return 0


if __name__ == "__main__":
    raise SystemExit(main())
