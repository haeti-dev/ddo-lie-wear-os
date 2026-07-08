import SwiftUI

/// Counterpart of wearos AnalysisScreen.kt — the 5-second "detecting" phase
/// while the model finalizes the measurement.
struct AnalysisView: View {
    @Binding var phase: GamePhase
    let model: MeasurementModel

    @State private var activeCircleIndex = 0
    @State private var dotPhaseIndex = 0

    private let dotPhases = [".", "..", "..."]

    var body: some View {
        ZStack {
            DdoLieColors.black.ignoresSafeArea()

            DotRing(activeIndex: activeCircleIndex)
                .ignoresSafeArea()

            ZStack {
                Image("img_graph")
                    .resizable()
                    .scaledToFit()
                    .frame(maxWidth: .infinity, maxHeight: .infinity)
                    .ignoresSafeArea()

                (Text("거짓말").foregroundColor(DdoLieColors.redPrimary)
                    + Text(" 탐지중" + dotPhases[dotPhaseIndex]).foregroundColor(DdoLieColors.white))
                    .font(.system(size: 20, weight: .semibold))
                    .multilineTextAlignment(.center)
            }
        }
        .task {
            // Register the subscription before dispatching, so a synchronous
            // side effect can't be emitted before we're listening.
            let effects = model.sideEffects()
            model.onIntent(.finalizeMeasurement)

            for await effect in effects {
                if case .navigateToResult = effect {
                    phase = .result
                }
            }
        }
        .task {
            // Mirrors wearos: ring cycles of 8 steps (~5s of finalize delay).
            for _ in 0..<DdoLieConstants.Animation.analysisRingCycles {
                for index in 0..<DdoLieConstants.Animation.initialScreenCycleSteps {
                    activeCircleIndex = index
                    try? await Task.sleep(for: .milliseconds(DdoLieConstants.Animation.initialScreenStepDelay))
                    if Task.isCancelled { return }
                }
            }
        }
        .task {
            // Mirrors wearos: dot phase cycling.
            for _ in 0..<DdoLieConstants.Animation.analysisDotPhaseRepeats {
                dotPhaseIndex = (dotPhaseIndex + 1) % DdoLieConstants.Animation.dotPhasesCount
                try? await Task.sleep(for: .milliseconds(DdoLieConstants.Animation.dotAnimationDelay))
                if Task.isCancelled { return }
            }
        }
    }
}

#Preview {
    AnalysisView(phase: .constant(.analysis), model: MeasurementModel(heartRateService: HealthKitHeartRateService()))
}
