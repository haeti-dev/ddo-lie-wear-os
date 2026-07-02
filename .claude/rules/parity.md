# Platform parity rules

wearos (source of truth) and watchos must deliver an identical user experience. Only minimal layout adaptation for device shape (round vs rectangular) is allowed.

## Must match exactly

| Item | Source of truth (wearos) |
|---|---|
| Screen flow | `presentation/navigation/AppNavHost.kt` — start → initial measurement → voice recognition → analysis → result |
| Copy | user-facing strings in each `*Screen.kt`, verbatim including line breaks |
| Colors | `presentation/theme/Colors.kt` |
| Timing & sizes | `presentation/common/util/DdoLieConstants.kt` (measurement durations, delays, animation intervals, UI ratios) |
| Measurement/verdict behavior | follow the `presentation/common/viewmodel/DdoLieViewModel.kt` implementation as-is |
| Animations | dot-ring sequential blink speed, fade transitions, dot count |

## Design adaptation

- Circular motifs (dot ring, round start button) stay circular on watchos — never stretch them along the rectangular screen edge
- watchos layouts assume the system clock is visible at the top
- Base watchos design target: 46mm (208×248pt); scale relatively for other sizes

## Verification

- UI-changing PRs attach side-by-side screenshots of both platforms
- Check constant parity with the `/parity-check` skill
