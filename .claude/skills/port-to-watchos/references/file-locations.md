# File location mapping

wearos base path: `wearos/app/src/main/java/com/haeti/ddolie/`
watchos base path: `watchos/DdoLie/` (finalized in the scaffold PR; update this table if it changes)

| Role | wearos | watchos |
|---|---|---|
| Screen | `presentation/<feature>/<Feature>Screen.kt` | `Features/<Feature>/<Feature>View.swift` |
| Route | `presentation/<feature>/navigation/` | `Navigation/Route.swift` (enum case) |
| Measurement state machine | `presentation/common/viewmodel/DdoLieViewModel.kt` | `Core/MeasurementModel.swift` |
| Heart-rate service | `presentation/common/manager/HealthServiceManager.kt` | `Core/HeartRateService.swift` |
| Constants | `presentation/common/util/DdoLieConstants.kt` | `Core/Constants.swift` |
| Colors | `presentation/theme/Colors.kt` | `Theme/Colors.swift` |
| Shared components | `presentation/common/component/` | `Components/` |
| Image assets | `app/src/main/res/drawable*/` | `Assets.xcassets` |
