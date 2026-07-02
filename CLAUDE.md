# DDolie (또라이)

A lie-detector game watch app built on heart-rate measurement. Shipped on Wear OS; a watchOS port lives in the same repo.

## Layout

- `wearos/` — Wear OS app (Kotlin, Jetpack Compose). **Source of truth for product behavior**
- `watchos/` — Apple Watch app (Swift, SwiftUI). Mirrors the wearos UX
- `.claude/rules/` — parity rules and conventions
- Each platform folder has its own CLAUDE.md with build commands and structure notes

## Core principle

User-facing copy, screen flow, colors, animation timing, and size ratios must be **identical across the two platforms**.
Only minimal layout adaptation for device shape (round vs rectangular) is allowed; circular motifs (dot ring, round button) stay circular on watchOS.
Details and the source-of-truth file map: `.claude/rules/parity.md`

When a feature/screen/string is added to wearos, port it with the `port-to-watchos` skill and verify with `parity-check`.

## Commands

- Wear OS build: `cd wearos && ./gradlew :app:assembleDebug`
- watchOS: see `watchos/CLAUDE.md` (added with the scaffold)

## Conventions

- Commits: `type : summary` with the summary written in Korean (e.g. `refactor : 측정 로직 정리`)
- Create PRs with the `/pr` skill. Details: `.claude/rules/conventions.md`
