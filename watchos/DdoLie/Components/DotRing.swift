import SwiftUI

/// The lie-detector toy's LED ring: 8 dots on a circle, one lit at a time.
/// Mirrors the Canvas ring in wearos InitialScreen/AnalysisScreen —
/// radius 0.47 × min dimension, dots every 45° starting at the top (270°),
/// 6pt dot radius. The ring stays circular on the rectangular screen
/// (see .claude/rules/parity.md).
struct DotRing: View {
    /// Index of the lit dot; pass -1 to keep all dots inactive.
    let activeIndex: Int

    private static let dotCount = 8
    private static let activeColor = DdoLieColors.redPrimary
    private static let inactiveColor = Color(argb: 0xFF333333)

    var body: some View {
        GeometryReader { geometry in
            let radius = min(geometry.size.width, geometry.size.height) * 0.47
            let center = CGPoint(x: geometry.size.width / 2, y: geometry.size.height / 2)

            ForEach(0..<Self.dotCount, id: \.self) { index in
                let angle = Angle.degrees(270 + Double(index) * 45).radians
                Circle()
                    .fill(index == activeIndex ? Self.activeColor : Self.inactiveColor)
                    .frame(width: 12, height: 12)
                    .position(
                        x: center.x + cos(angle) * radius,
                        y: center.y + sin(angle) * radius
                    )
            }
        }
    }
}

#Preview {
    ZStack {
        Color.black.ignoresSafeArea()
        DotRing(activeIndex: 0)
    }
}
