#!/usr/bin/env python3
"""
fix_tema_selector.py  —  Aplica el fix del selector de idioma (MVS_urlsIdiomas)
sobre el ZIP de un tema de gusite y genera un nuevo ZIP con sufijo _fix_YYYYMMDD.

Uso:
    python fix_tema_selector.py <ruta_al_zip>

El script detecta y corrige dos variantes del selector de idioma:

  A) Selector dinámico (itera MVS_listaidiomas, usa idiomaLista.key):
       @{${#gusuri.traduccionEnlacePie(..., idiomaLista.key, ...)}}

  B) Selector estático (botones con idioma literal hardcodeado, p.ej. 'ca', 'es'):
       @{${#gusuri.traduccionEnlacePie(..., 'ca', ...)}}

En ambos casos añade la lógica condicional MVS_urlsIdiomas con fallback a
traduccionEnlacePie para páginas sin slug variable (home, faq, agenda, etc.).
"""

import os
import re
import sys
import zipfile
import tempfile
from datetime import datetime

# ── Variante A: idioma dinámico (idiomaLista.key) ──────────────────────────

OLD_DYN = (
    "@{${#gusuri.traduccionEnlacePie("
    "#ctx.httpServletRequest.contextPath, "
    "#httpServletRequest.requestURI, "
    "idiomaLista.key,MVS_busquedaBuscador,MVS_tipobeta)}}"
)

NEW_DYN = (
    "@{${MVS_urlsIdiomas != null and MVS_urlsIdiomas[idiomaLista.key] != null"
    " ? MVS_urlsIdiomas[idiomaLista.key]"
    " : #gusuri.traduccionEnlacePie("
    "#ctx.httpServletRequest.contextPath, "
    "#httpServletRequest.requestURI, "
    "idiomaLista.key,MVS_busquedaBuscador,MVS_tipobeta)}}"
)

# ── Variante B: idioma literal hardcodeado ('ca', 'es', 'en', …) ───────────

# Coincide con: @{${#gusuri.traduccionEnlacePie(..., 'xx', ...)}}
_PATTERN_STATIC = re.compile(
    r"@\{\$\{#gusuri\.traduccionEnlacePie\("
    r"#ctx\.httpServletRequest\.contextPath, "
    r"#httpServletRequest\.requestURI, "
    r"'([a-z]{2})',"
    r"MVS_busquedaBuscador,MVS_tipobeta\)\}\}"
)


def _replace_static(m: re.Match) -> str:
    lang = m.group(1)
    return (
        f"@{{${{MVS_urlsIdiomas != null and MVS_urlsIdiomas['{lang}'] != null"
        f" ? MVS_urlsIdiomas['{lang}']"
        f" : #gusuri.traduccionEnlacePie(#ctx.httpServletRequest.contextPath,"
        f" #httpServletRequest.requestURI, '{lang}',MVS_busquedaBuscador,MVS_tipobeta)}}}}"
    )


def fix_content(content: str) -> tuple[str, int]:
    """Aplica ambas variantes. Devuelve (contenido corregido, nº sustituciones)."""
    total = 0

    # Variante A: sustitución literal
    count_a = content.count(OLD_DYN)
    if count_a:
        content = content.replace(OLD_DYN, NEW_DYN)
        total += count_a

    # Variante B: sustitución con regex (solo actúa si ya no tiene el fix)
    content, count_b = _PATTERN_STATIC.subn(_replace_static, content)
    total += count_b

    return content, total


def main():
    if len(sys.argv) != 2:
        print(f"Uso: python {os.path.basename(__file__)} <ruta_al_zip>")
        sys.exit(1)

    zip_in = sys.argv[1]
    if not os.path.isfile(zip_in):
        print(f"Error: no se encuentra '{zip_in}'")
        sys.exit(1)

    base, ext = os.path.splitext(zip_in)
    date_tag = datetime.now().strftime("%Y%m%d")
    zip_out = f"{base}_fix_{date_tag}{ext}"

    total_files = 0
    total_replacements = 0

    with tempfile.TemporaryDirectory() as tmp:
        # 1. Descomprimir
        print(f"Descomprimiendo  {zip_in} …")
        with zipfile.ZipFile(zip_in, "r") as zin:
            zin.extractall(tmp)

        # 2. Aplicar fix en todos los .html
        for dirpath, _, filenames in os.walk(tmp):
            for fname in filenames:
                if not fname.lower().endswith(".html"):
                    continue
                fpath = os.path.join(dirpath, fname)
                with open(fpath, "r", encoding="utf-8") as f:
                    original = f.read()
                fixed, count = fix_content(original)
                if count:
                    with open(fpath, "w", encoding="utf-8") as f:
                        f.write(fixed)
                    rel = os.path.relpath(fpath, tmp)
                    print(f"  [{count}x] {rel}")
                    total_files += 1
                    total_replacements += count

        # 3. Recomprimir
        print(f"\nComprimiendo     {zip_out} …")
        with zipfile.ZipFile(zip_out, "w", zipfile.ZIP_DEFLATED) as zout:
            for dirpath, _, filenames in os.walk(tmp):
                for fname in filenames:
                    fpath = os.path.join(dirpath, fname)
                    arcname = os.path.relpath(fpath, tmp)
                    zout.write(fpath, arcname)

    if total_replacements:
        print(
            f"\nOK — {total_replacements} sustitución(es) en "
            f"{total_files} archivo(s)  →  {zip_out}"
        )
    else:
        print(
            "\nAviso: no se encontró ninguna ocurrencia activa de "
            "traduccionEnlacePie en el ZIP.\n"
            f"El ZIP se ha recomprimido igualmente en {zip_out}"
        )


if __name__ == "__main__":
    main()
