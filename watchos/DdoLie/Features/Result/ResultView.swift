import SwiftUI
import WatchKit

/// Counterpart of wearos ResultScreen.kt.
struct ResultView: View {
    @Binding var phase: GamePhase
    let model: MeasurementModel

    var body: some View {
        ZStack {
            DdoLieColors.black.ignoresSafeArea()

            switch model.state.isLie {
            case .truth:
                ResultContent(
                    phase: $phase,
                    resultImg: "img_truth",
                    resultIcon: "img_engle",
                    backgroundImg: "img_bg_true",
                    resultType: .truth,
                    resultColor: DdoLieColors.green
                )
            case .lie:
                ResultContent(
                    phase: $phase,
                    resultImg: "img_icon",
                    resultIcon: "img_devil",
                    backgroundImg: "img_background",
                    resultType: .lie,
                    resultColor: DdoLieColors.redPrimary
                )
                .task {
                    // wearos plays a 1s one-shot vibration on the LIE reveal;
                    // watchOS has no duration API, so play the closest single haptic.
                    WKInterfaceDevice.current().play(.failure)
                }
            case nil:
                EmptyView() // TODO: handle error (mirrors wearos)
            }
        }
    }
}

private struct ResultContent: View {
    @Binding var phase: GamePhase
    let resultImg: String
    let resultIcon: String
    let backgroundImg: String
    let resultType: LieResult
    let resultColor: Color

    @State private var showFullScreenImage = true

    var body: some View {
        ZStack {
            if showFullScreenImage {
                Image(resultImg)
                    .resizable()
                    .scaledToFit()
                    .frame(maxWidth: .infinity, maxHeight: .infinity)
                    .ignoresSafeArea()
                    .transition(.opacity)
            }

            if !showFullScreenImage {
                Image(backgroundImg)
                    .resizable()
                    .scaledToFill()
                    .frame(maxWidth: .infinity, maxHeight: .infinity)
                    .clipped()
                    .ignoresSafeArea()

                VStack(spacing: 0) {
                    Image(resultIcon)
                        .resizable()
                        .scaledToFit()
                        .frame(width: 50, height: 50)

                    (Text("당신은\n")
                        + Text(resultType == .lie ? "거짓말" : "진실").foregroundColor(resultColor)
                        + Text(resultType == .lie ? "을 하고 있어요" : "을 말하고 있어요"))
                        .font(.system(size: 22, weight: .semibold))
                        .foregroundColor(DdoLieColors.white)
                        .multilineTextAlignment(.center)

                    Spacer().frame(height: 16)

                    CtaButton(
                        text: "다시하기",
                        chipColor: DdoLieColors.white,
                        contentColor: DdoLieColors.black
                    ) {
                        // Mirrors wearos popUpTo(StartRoute, inclusive): back to start.
                        phase = .start
                    }
                }
            }
        }
        .animation(.easeInOut(duration: 0.3), value: showFullScreenImage)
        .task {
            try? await Task.sleep(for: .milliseconds(DdoLieConstants.Animation.resultScreenDelay))
            showFullScreenImage = false
        }
    }
}

#Preview {
    ResultView(phase: .constant(.result), model: MeasurementModel(heartRateService: HealthKitHeartRateService()))
}
