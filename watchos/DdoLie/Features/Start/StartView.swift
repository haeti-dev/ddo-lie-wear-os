import SwiftUI

/// Counterpart of wearos StartScreen.kt — the toy's big red button.
/// Sizes scale from the wearos 218dp base width so proportions match.
struct StartView: View {
    @Binding var phase: GamePhase
    let model: MeasurementModel

    private static let baseWidth: CGFloat = 218

    var body: some View {
        GeometryReader { geometry in
            let scale = geometry.size.width / Self.baseWidth

            ZStack {
                DdoLieColors.black.ignoresSafeArea()

                ZStack {
                    Circle()
                        .fill(
                            RadialGradient(
                                stops: [
                                    .init(color: DdoLieColors.redTertiary, location: 0.00),
                                    .init(color: DdoLieColors.redTertiary, location: 0.85),
                                    .init(color: DdoLieColors.black, location: 1.00),
                                ],
                                center: .center,
                                startRadius: 0,
                                endRadius: geometry.size.width / 2
                            )
                        )
                    Circle()
                        .fill(DdoLieColors.redSecondary)
                        .frame(width: geometry.size.width * 0.95)
                    Circle()
                        .fill(DdoLieColors.redPrimary)
                        .frame(width: geometry.size.width * 0.75)

                    VStack(spacing: 0) {
                        Image("img_logo")
                            .resizable()
                            .frame(width: 108 * scale, height: 70 * scale)

                        Spacer().frame(height: 10 * scale)

                        Text("시작하려면 화면을\n터치해주세요")
                            .font(.system(size: 15 * scale, weight: .bold))
                            .lineSpacing(8 * scale)
                            .multilineTextAlignment(.center)
                            .foregroundStyle(DdoLieColors.white)
                    }
                }
                .frame(width: geometry.size.width, height: geometry.size.width)
                .position(x: geometry.size.width / 2, y: geometry.size.height / 2)
            }
            .contentShape(Rectangle())
            .onTapGesture {
                phase = .initial
            }
        }
        .ignoresSafeArea()
        .onAppear {
            if model.isSensorAuthorized { model.startPrewarm() }
        }
        .onDisappear {
            model.stopPrewarm()
        }
    }
}

#Preview {
    StartView(phase: .constant(.start), model: MeasurementModel(heartRateService: HealthKitHeartRateService()))
}
