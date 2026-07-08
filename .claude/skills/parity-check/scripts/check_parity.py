#!/usr/bin/env python3
"""Compare shared UI constants and colors between wearos (Kotlin) and watchos (Swift).

wearos is the source of truth. Exits 1 on any mismatch.
Scope: generic UI constants (timing, sizes) and colors only.
"""
import pathlib
import re
import sys

ROOT = pathlib.Path(__file__).resolve().parents[4]
KT_CONST = ROOT / "wearos/app/src/main/java/com/haeti/ddolie/presentation/common/util/DdoLieConstants.kt"
SWIFT_CONST = ROOT / "watchos/DdoLie/Core/Constants.swift"
KT_COLORS = ROOT / "wearos/app/src/main/java/com/haeti/ddolie/presentation/theme/Colors.kt"
SWIFT_COLORS = ROOT / "watchos/DdoLie/Theme/Colors.swift"


def snake_to_camel(name: str) -> str:
    parts = name.lower().split("_")
    return parts[0] + "".join(p.title() for p in parts[1:])


def parse_kotlin_constants(text: str) -> dict:
    return {
        snake_to_camel(m.group(1)): float(m.group(2))
        for m in re.finditer(r"const val (\w+)\s*=\s*([\d.]+)L?", text)
    }


def parse_swift_constants(text: str) -> dict:
    return {
        m.group(1): float(m.group(2))
        for m in re.finditer(r"static let (\w+)(?::\s*\w+)?\s*=\s*([\d.]+)", text)
    }


def parse_kotlin_colors(text: str) -> dict:
    return {
        m.group(1)[0].lower() + m.group(1)[1:]: m.group(2).upper()
        for m in re.finditer(r"val (\w+)\s*=\s*Color\(0x([0-9A-Fa-f]{8})\)", text)
    }


def parse_swift_colors(text: str) -> dict:
    return {
        m.group(1): m.group(2).upper()
        for m in re.finditer(r"static let (\w+)\s*=\s*Color\(argb:\s*0x([0-9A-Fa-f]{8})\)", text)
    }


def compare(label: str, source: dict, mirror: dict) -> bool:
    ok = True
    for name, value in sorted(source.items()):
        if name not in mirror:
            print(f"[{label}] MISSING in watchos: {name} (wearos={value})")
            ok = False
        elif mirror[name] != value:
            print(f"[{label}] MISMATCH {name}: wearos={value} watchos={mirror[name]}")
            ok = False
    for name in sorted(set(mirror) - set(source)):
        print(f"[{label}] EXTRA in watchos (warning): {name}")
    return ok


def main() -> None:
    for path in (KT_CONST, SWIFT_CONST, KT_COLORS, SWIFT_COLORS):
        if not path.exists():
            print(f"missing file: {path}")
            sys.exit(1)
    ok = compare("constants", parse_kotlin_constants(KT_CONST.read_text()), parse_swift_constants(SWIFT_CONST.read_text()))
    ok &= compare("colors", parse_kotlin_colors(KT_COLORS.read_text()), parse_swift_colors(SWIFT_COLORS.read_text()))
    print("parity OK" if ok else "parity FAILED")
    sys.exit(0 if ok else 1)


if __name__ == "__main__":
    main()
