#!/usr/bin/env python3

from pathlib import Path
import zipfile

parser = argparse.ArgumentParser()
parser.add_argument("source")
parser.add_argument("destination")
args = parser.parse_args()

ROOT = Path(__file__).resolve().parents[2]

SOURCE_DIR = ROOT / "raw" / args.source
OUTPUT_ZIP = ROOT / "src" / "main" / "resources" / "assets" / "alchemicalwizardry" / args.destination


def main():
    if not SOURCE_DIR.is_dir():
        raise SystemExit(f"Missing source directory: {SOURCE_DIR}")

    OUTPUT_ZIP.parent.mkdir(parents=True, exist_ok=True)

    files = sorted(p for p in SOURCE_DIR.rglob("*") if p.is_file())

    with zipfile.ZipFile(OUTPUT_ZIP, "w", zipfile.ZIP_DEFLATED, compresslevel=9) as zf:
        for file in sorted(SOURCE_DIR.rglob("*")):
            if file.is_file():
                zf.write(file, file.relative_to(SOURCE_DIR))

    print(f"Compressed {len(files)} file(s) into {OUTPUT_ZIP.relative_to(ROOT)}")


if __name__ == "__main__":
    main()
