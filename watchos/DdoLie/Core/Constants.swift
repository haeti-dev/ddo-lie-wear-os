import Foundation

/// Mirrors wearos DdoLieConstants.kt — values must stay identical.
/// Parity rules: .claude/rules/parity.md (checked by the parity-check skill).
enum DdoLieConstants {
    enum Measurement {
        static let initialMeasurementTimeout: Int64 = 5000
        static let finalizeDelay: Int64 = 5000
        static let heartRateMinThreshold: Double = 0.0
    }

    enum Vibration {
        static let vibrationDuration: Int64 = 1000
        static let vibrationAmplitude: Int = 200
    }

    enum Animation {
        static let resultScreenDelay: Int64 = 2000
        static let dotAnimationDelay: Int64 = 333
        static let initialScreenAnimationCycles: Int = 4
        static let initialScreenCycleSteps: Int = 8
        static let initialScreenStepDelay: Int64 = 125
        static let dotPhasesCount: Int = 3
        static let fadeTransitionMs: Int = 400
        static let initialCircleCycleMs: Int = 1000
        static let analysisRingCycles: Int = 5
        static let analysisDotPhaseRepeats: Int = 15
    }
}
