import SwiftUI

/// Mirrors wearos Colors.kt — ARGB hex values must stay identical.
/// Parity rules: .claude/rules/parity.md (checked by the parity-check skill).
enum DdoLieColors {
    static let redPrimary = Color(argb: 0xFFF44522)
    static let redSecondary = Color(argb: 0x33FB4723)
    static let redTertiary = Color(argb: 0x40FB4723)
    static let green = Color(argb: 0xFF68EDCE)
    static let white = Color(argb: 0xFFFFFFFF)
    static let black = Color(argb: 0xFF000000)
}

extension Color {
    /// ARGB literal in the same 0xAARRGGBB form used by Compose's Color(...)
    init(argb: UInt32) {
        self.init(
            .sRGB,
            red: Double((argb >> 16) & 0xFF) / 255.0,
            green: Double((argb >> 8) & 0xFF) / 255.0,
            blue: Double(argb & 0xFF) / 255.0,
            opacity: Double((argb >> 24) & 0xFF) / 255.0
        )
    }
}
