---
name: parity-check
description: Check parity of constants (timing/sizes), colors, copy, and screen flow between wearos and watchos. Use for requests like "패리티 체크", "양쪽 값 맞는지 확인", or as the verification step of the port-to-watchos workflow.
---

# parity-check

Verify the two platforms have not drifted apart. On any mismatch, **wearos wins** (it is the source of truth).

## Current state

`scripts/check_parity.py` will be added together with the watchos constants files (`Constants.swift`, `Colors.swift`). Until then, follow the manual procedure below.

## What to check

1. Timing/size constants: values with matching names in `DdoLieConstants.kt` ↔ `Constants.swift`
2. Colors: hex values in `Colors.kt` ↔ `Colors.swift`
3. Copy: user-facing strings per screen (identical, including line breaks)
4. Flow: route order (AppNavHost ↔ Route enum)

## Procedure

1. Read both sides and build a per-item value table
2. Report mismatches as a table (wearos value / watchos value / verdict)
3. If asked to fix, change the watchos side to match wearos
