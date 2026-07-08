import SwiftUI

/// Counterpart of wearos VoiceRecognitionScreen.kt.
/// "Voice recognition" is the product name of this phase — no actual speech
/// recognition happens; the heart rate keeps being measured while the user talks.
struct VoiceRecognitionView: View {
    @Binding var phase: GamePhase
    let model: MeasurementModel

    @State private var isTalking = false
    @State private var dotPhaseIndex = 0

    private let dotPhases = [".", "..", "..."]

    var body: some View {
        ZStack {
            DdoLieColors.black.ignoresSafeArea()

            Image("img_background")
                .resizable()
                .scaledToFill()
                .frame(maxWidth: .infinity, maxHeight: .infinity)
                .clipped()
                .ignoresSafeArea()

            if !isTalking {
                VStack(spacing: 0) {
                    Text("시작 버튼을 누르고\n말해보세요")
                        .font(.system(size: 20, weight: .semibold))
                        .lineSpacing(10)
                        .multilineTextAlignment(.center)
                        .foregroundStyle(DdoLieColors.white)

                    Spacer().frame(height: 18)

                    CtaButton(text: "시작하기") {
                        isTalking = true
                        model.onIntent(.startVoiceRecognition)
                    }
                }
                .transition(.opacity)
            }

            if isTalking {
                VStack(spacing: 0) {
                    Image("ic_mask")
                        .resizable()
                        .scaledToFit()
                        .frame(width: 53, height: 53)

                    Spacer().frame(height: 10)

                    Text("센서 작동 중" + dotPhases[dotPhaseIndex])
                        .font(.system(size: 20, weight: .semibold))
                        .foregroundStyle(DdoLieColors.white)

                    Spacer().frame(height: 20)

                    CtaButton(text: "종료하기") {
                        model.onIntent(.finishMeasurement)
                    }
                }
                .transition(.opacity)
            }
        }
        .animation(.easeInOut(duration: 0.3), value: isTalking)
        .task {
            let effects = model.sideEffects()
            for await effect in effects {
                if case .navigateToAnalysis = effect {
                    phase = .analysis
                }
            }
        }
        .task(id: isTalking) {
            while isTalking, !Task.isCancelled {
                dotPhaseIndex = (dotPhaseIndex + 1) % DdoLieConstants.Animation.dotPhasesCount
                try? await Task.sleep(for: .milliseconds(DdoLieConstants.Animation.dotAnimationDelay))
            }
        }
    }
}

#Preview {
    VoiceRecognitionView(phase: .constant(.voiceRecognition), model: MeasurementModel(heartRateService: HealthKitHeartRateService()))
}
