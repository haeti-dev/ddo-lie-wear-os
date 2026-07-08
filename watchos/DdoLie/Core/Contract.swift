import Foundation

/// Mirrors wearos DdoLieContract.kt.
enum LieResult {
    case lie, truth
}

enum DdoLieIntent {
    case startInitialMeasurement
    case startVoiceRecognition
    case finishMeasurement
    case finalizeMeasurement
}

struct DdoLieState {
    var initialHeartRateAvg: Float?
    var finalHeartRateAvg: Float?
    var isLie: LieResult?
}

enum DdoLieSideEffect {
    case navigateToVoiceRecognition
    case navigateToAnalysis
    case navigateToResult
    case showError(String)
}
