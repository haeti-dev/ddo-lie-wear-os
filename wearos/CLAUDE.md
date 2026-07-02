# wearos — Wear OS app

Single module (`:app`), MVI pattern (BaseViewModel + Contract + SideEffect). This folder is the Android Studio project root.

## Structure (app/src/main/java/com/haeti/ddolie/)

- `presentation/<feature>/` — screen composable + `navigation/` (route + NavGraphBuilder extension)
  - Flow order: start → init (baseline measurement) → recognition (voice) → analysis → result
- `presentation/common/`
  - `viewmodel/DdoLieViewModel.kt` — measurement state machine (baseline → continuous measurement → verdict)
  - `manager/HealthServiceManager.kt` — Health Services heart-rate Flow (sensor registration shared via shareIn)
  - `util/DdoLieConstants.kt` — timing/threshold/animation constants (**parity target with watchos**)
  - `component/`, `base/`, `contract/`
- `presentation/theme/Colors.kt` — colors (**parity target with watchos**)

## Commands

- Build: `./gradlew :app:assembleDebug`

## Caution

Changing copy, colors, timing, or screen flow requires a watchos parity check → root `.claude/rules/parity.md`
