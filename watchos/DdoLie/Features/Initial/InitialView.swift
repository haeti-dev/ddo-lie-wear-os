import SwiftUI

/// Counterpart of wearos InitialScreen.kt — baseline measurement before the game.
struct InitialView: View {
    @Binding var phase: GamePhase
    let model: MeasurementModel

    @State private var isMeasuring = false
    @State private var circleStarted = false
    @State private var activeCircleIndex = -1
    @State private var dotPhaseIndex = 0

    private let dotPhases = [".", "..", "..."]

    var body: some View {
        ZStack {
            DdoLieColors.black.ignoresSafeArea()

            // Measured against the full screen (like the wearos full-size Canvas)
            // so min(w,h) hits the width and the ring spans it, clear of the text.
            DotRing(activeIndex: activeCircleIndex)
                .ignoresSafeArea()

            if !isMeasuring {
                VStack(spacing: 0) {
                    Text("거짓말 탐지 전\n상태를 측정하세요")
                        .font(.system(size: 20, weight: .semibold))
                        .lineSpacing(10)
                        .multilineTextAlignment(.center)
                        .foregroundStyle(DdoLieColors.white)

                    Spacer().frame(height: 18)

                    CtaButton(text: "시작하기") {
                        isMeasuring = true
                        model.onIntent(.startInitialMeasurement)
                    }
                }
                .transition(.opacity)
            }

            if isMeasuring {
                ZStack {
                    Image("img_graph")
                        .resizable()
                        .scaledToFit()
                        .frame(maxWidth: .infinity, maxHeight: .infinity)
                        .ignoresSafeArea()

                    Text("측정 중" + dotPhases[dotPhaseIndex])
                        .font(.system(size: 20, weight: .semibold))
                        .foregroundStyle(DdoLieColors.white)
                }
                .transition(.opacity)
            }
        }
        .animation(
            .easeInOut(duration: Double(DdoLieConstants.Animation.fadeTransitionMs) / 1000),
            value: isMeasuring
        )
        .task {
            let effects = model.sideEffects()
            Task { await model.requestHeartRateAuthorization() }

            for await effect in effects {
                if case .navigateToVoiceRecognition = effect {
                    phase = .voiceRecognition
                }
            }
        }
        .task(id: isMeasuring) {
            if isMeasuring {
                try? await Task.sleep(for: .milliseconds(DdoLieConstants.Animation.fadeTransitionMs))
                circleStarted = true
            } else {
                circleStarted = false
            }
        }
        .task(id: circleStarted) {
            guard circleStarted else {
                activeCircleIndex = -1
                return
            }
            while !Task.isCancelled {
                for index in 0..<DdoLieConstants.Animation.initialScreenCycleSteps {
                    activeCircleIndex = index
                    try? await Task.sleep(for: .milliseconds(DdoLieConstants.Animation.initialScreenStepDelay))
                    if Task.isCancelled { return }
                }
            }
        }
        .task(id: circleStarted) {
            while circleStarted, !Task.isCancelled {
                dotPhaseIndex = (dotPhaseIndex + 1) % DdoLieConstants.Animation.dotPhasesCount
                try? await Task.sleep(for: .milliseconds(DdoLieConstants.Animation.dotAnimationDelay))
            }
        }
    }
}

#Preview {
    InitialView(phase: .constant(.initial), model: MeasurementModel(heartRateService: HealthKitHeartRateService()))
}
