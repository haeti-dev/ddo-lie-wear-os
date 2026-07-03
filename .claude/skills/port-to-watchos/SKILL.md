---
name: port-to-watchos
description: Port features/screens/strings/constants added or changed in wearos (Android) to watchos (Swift/SwiftUI). Use for requests like "워치OS에도 반영해줘", "애플워치에 포팅해줘", "watchos 동기화", or whenever wearos changed and platform parity must be restored.
---

# port-to-watchos

Workflow for porting wearos changes to watchos. Follows the root `.claude/rules/parity.md`.

## Precondition

`watchos/` must exist. If it doesn't, tell the user the watchOS scaffold has to land first and stop.

## Workflow

1. **Scope** — if the user names a feature, target its sources; otherwise inspect `git log --oneline -- wearos/` for changes since the last port
2. **Read the original** — read the wearos Screen / ViewModel / constants / colors and record copy, timing, colors, and flow
3. **Translate** — convert to Swift using [references/compose-swiftui-map.md](references/compose-swiftui-map.md); place files per [references/file-locations.md](references/file-locations.md)
4. **Implement** — write under `watchos/`. Copy strings verbatim. Carry numeric constants over unchanged
5. **Verify** — `xcodegen` + `xcodebuild build` succeed → run `/parity-check` → compare simulator screenshots against the wearos originals
6. **PR** — create with the `/pr` skill and attach the comparison screenshots

## Cautions

- Only layout may adapt to the device shape; circular elements stay circular
- If watchos lacks a matching API, follow the mapping table; if unmapped, ask the user instead of improvising
- Update file-locations.md if the actual structure diverges
