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
PLACEHOLDER_IMAGE_RE = re.compile(r"^NomeD[ao]", re.IGNORECASE)


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


def list_all_wiki_images() -> set[str]:
    params = {
        "action": "query",
        "list": "allimages",
        "ailimit": "500",
        "format": "json",
    }
    url = f"{BASE_API}?{urllib.parse.urlencode(params)}"

    with urllib.request.urlopen(url, timeout=30) as response:
        payload = json.load(response)

    return {item["name"] for item in payload.get("query", {}).get("allimages", [])}


def image_candidates(image_name: str) -> list[str]:
    candidates: list[str] = []
    stem = Path(image_name).stem
    suffix = Path(image_name).suffix or ".png"

    if "_" in stem:
        parent = stem.split("_", 1)[0]
        candidates.append(f"{parent}{suffix}")

    return candidates


def is_placeholder_image(image_name: str) -> bool:
    return bool(PLACEHOLDER_IMAGE_RE.match(Path(image_name).stem))


def download_image(image_name: str, wiki_images: set[str]) -> tuple[bool, str]:
    if is_placeholder_image(image_name):
        return True, f"placeholder ignorado: {image_name}"

    image_url = mediawiki_image_url(image_name)
    resolved_name = image_name

    if not image_url:
        for candidate in image_candidates(image_name):
            if candidate not in wiki_images:
                continue

            candidate_url = mediawiki_image_url(candidate)
            if candidate_url:
                image_url = candidate_url
                resolved_name = candidate
                break

    if not image_url:
        return False, f"indisponível no wiki: {image_name}"

    file_name = image_name if resolved_name != image_name else unquote(Path(urlsplit(image_url).path).name).replace(" ", "_")
    destination = IMAGES_DIR / file_name

    with urllib.request.urlopen(image_url, timeout=60) as response:
        destination.write_bytes(response.read())

    if resolved_name != image_name:
        return True, f"{file_name} (via {resolved_name})"

    return True, file_name


def main() -> int:
    if not LEGADO_DIR.exists():
        raise SystemExit(f"Diretório não encontrado: {LEGADO_DIR}")

    IMAGES_DIR.mkdir(parents=True, exist_ok=True)

    referenced_images = collect_referenced_images()
    if not referenced_images:
        print("Nenhuma referência de imagem encontrada em docs/legado/*.wiki")
        return 0

    wiki_images = list_all_wiki_images()

    downloaded = 0
    missing: list[str] = []
    skipped = 0

    for image_name in referenced_images:
        ok, result = download_image(image_name, wiki_images)
        if ok:
            if result.startswith("placeholder ignorado: "):
                skipped += 1
                print(f"[SKIP] {result}")
                continue
            downloaded += 1
            print(f"[OK] {image_name} -> images/{result}")
        else:
            missing.append(result)
            print(f"[WARN] {result}")

    print("\nResumo")
    print(f"- Referências únicas encontradas: {len(referenced_images)}")
    print(f"- Placeholders ignorados: {skipped}")
    print(f"- Imagens baixadas: {downloaded}")
    print(f"- Não encontradas no wiki: {len(missing)}")

    return 0


if __name__ == "__main__":
    raise SystemExit(main())
