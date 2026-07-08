import Foundation

/// Heart-rate service for the simulator, which has no heart sensor.
/// Reports authorized and returns an empty stream so the whole game flow runs
/// end-to-end (the verdict takes the neutral no-data path) without HealthKit —
/// and without a workout session that would otherwise stall the simulator.
@MainActor
final class StubHeartRateService: HeartRateService {
    var isAuthorized: Bool { true }
    func requestAuthorization() async -> Bool { true }
    func heartRateMeasureStream() -> AsyncStream<MeasureMessage> {
        AsyncStream { $0.finish() }
    }
}
