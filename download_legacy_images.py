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


def download_image(image_name: str) -> tuple[bool, str]:
    image_url = mediawiki_image_url(image_name)
    if not image_url:
        return False, f"indisponível no wiki: {image_name}"

    file_name = unquote(Path(urlsplit(image_url).path).name).replace(" ", "_")
    destination = IMAGES_DIR / file_name

    with urllib.request.urlopen(image_url, timeout=60) as response:
        destination.write_bytes(response.read())

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
