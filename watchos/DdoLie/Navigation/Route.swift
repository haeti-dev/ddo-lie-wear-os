import SwiftUI

/// Mirrors the wearos navigation graph (AppNavHost.kt):
/// start → initial → voiceRecognition → analysis → result
///
/// The flow is strictly linear with no back navigation, so screens are switched
/// by state rather than a NavigationStack.
enum GamePhase {
    case start
    case initial
    case voiceRecognition
    case analysis
    case result
}

struct AppNavHost: View {
    @State private var phase: GamePhase = .start
    @State private var model = MeasurementModel(heartRateService: {
        #if targetEnvironment(simulator)
        StubHeartRateService()
        #else
        HealthKitHeartRateService()
        #endif
    }())

    var body: some View {
        ZStack {
            switch phase {
            case .start:
                StartView(phase: $phase, model: model)
            case .initial:
                InitialView(phase: $phase, model: model)
            case .voiceRecognition:
                VoiceRecognitionView(phase: $phase, model: model)
            case .analysis:
                AnalysisView(phase: $phase, model: model)
            case .result:
                ResultView(phase: $phase, model: model)
            }
        }
    }
}
