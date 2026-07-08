import SwiftUI

/// Counterpart of wearos CtaButton.kt: pill chip, 18pt semibold label,
/// 25×10 content padding, 500ms click throttle.
struct CtaButton: View {
    let text: String
    var chipColor: Color = DdoLieColors.redPrimary
    var contentColor: Color = DdoLieColors.white
    var onClick: () -> Void = {}

    @State private var lastClickTime: TimeInterval = 0

    var body: some View {
        Button {
            let now = Date().timeIntervalSince1970
            guard now - lastClickTime >= 0.5 else { return }
            lastClickTime = now
            onClick()
        } label: {
            Text(text)
                .font(.system(size: 18, weight: .semibold))
                .foregroundStyle(contentColor)
                .padding(EdgeInsets(top: 10, leading: 25, bottom: 10, trailing: 25))
                .background(chipColor, in: Capsule())
        }
        .buttonStyle(.plain)
    }
}

#Preview {
    CtaButton(text: "시작하기")
}
