# watchos — Apple Watch app

Standalone (watch-only) watchOS app. SwiftUI, min watchOS 10. Mirrors the wearos UX — see root `.claude/rules/parity.md`.

## Project generation

`DdoLie.xcodeproj` is **generated, not committed**. After cloning or changing `project.yml`:

```
cd watchos && xcodegen
```

Adding/removing Swift files also requires re-running `xcodegen` (it globs `DdoLie/`).

## Commands

- Generate project: `xcodegen`
- Build (simulator): `xcodebuild -project DdoLie.xcodeproj -scheme DdoLie -destination 'generic/platform=watchOS Simulator' build`
- Run on a simulator: build for a concrete destination, then `xcrun simctl install/launch booted com.haeti.ddolie`
- Real device runs need a signing team selected once in Xcode (Signing & Capabilities)

## Structure (DdoLie/)

- `DdoLieApp.swift` — @main entry
- `Navigation/Route.swift` — route enum + `AppNavHost` (NavigationStack); order mirrors wearos AppNavHost.kt
- `Features/<Feature>/<Feature>View.swift` — one screen per wearos `<Feature>Screen.kt`
- `Core/Constants.swift` — mirrors wearos DdoLieConstants.kt (**parity target**)
- `Core/HeartRateService.swift` — heart-rate interface; real HKWorkoutSession implementation in the heart-rate PR
- `Theme/Colors.swift` — mirrors wearos Colors.kt, same ARGB literals (**parity target**)

## Caution

- Keep copy, timing constants, and colors identical to wearos; verify with the `parity-check` skill
- Circular motifs stay circular; layouts assume the system clock is visible at the top
