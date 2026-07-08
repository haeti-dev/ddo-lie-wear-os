import Foundation
import HealthKit

/// Mirrors wearos MeasureMessage (HealthServiceManager.kt).
enum MeasureMessage {
    case data([Double])
    case availability(Bool)
}

@MainActor
protocol HeartRateService {
    var isAuthorized: Bool { get }
    func requestAuthorization() async -> Bool
    func heartRateMeasureStream() -> AsyncStream<MeasureMessage>
}

/// Counterpart of wearos HealthServiceManager.kt.
///
/// Real-time heart rate on watchOS requires a workout session
/// (HKWorkoutSession + HKLiveWorkoutBuilder), the equivalent of Health Services'
/// MeasureClient registration. Like the wearos `shareIn(WhileSubscribed(15s))`,
/// one session is shared by all subscribers and kept alive for 15 seconds after
/// the last one leaves, so consecutive measurement phases skip sensor warm-up.
@MainActor
final class HealthKitHeartRateService: NSObject, HeartRateService {
    private let healthStore = HKHealthStore()

    private var session: HKWorkoutSession?
    private var builder: HKLiveWorkoutBuilder?
    private var continuations: [UUID: AsyncStream<MeasureMessage>.Continuation] = [:]
    private var idleStopTask: Task<Void, Never>?

    var isAuthorized: Bool {
        healthStore.authorizationStatus(for: .workoutType()) == .sharingAuthorized
    }

    func requestAuthorization() async -> Bool {
        do {
            try await healthStore.requestAuthorization(
                toShare: [.workoutType()],
                read: [HKQuantityType(.heartRate)]
            )
            return true
        } catch {
            print("DdoLieFlow: authorization failed \(error)")
            return false
        }
    }

    func heartRateMeasureStream() -> AsyncStream<MeasureMessage> {
        let (stream, continuation) = AsyncStream<MeasureMessage>.makeStream()
        let id = UUID()
        continuations[id] = continuation
        startSessionIfNeeded()
        continuation.onTermination = { [weak self] _ in
            Task { @MainActor in
                self?.continuations[id] = nil
                self?.scheduleStopIfIdle()
            }
        }
        return stream
    }

    private func broadcast(_ message: MeasureMessage) {
        for continuation in continuations.values {
            continuation.yield(message)
        }
    }

    private func startSessionIfNeeded() {
        idleStopTask?.cancel()
        idleStopTask = nil
        guard session == nil else { return }

        let configuration = HKWorkoutConfiguration()
        configuration.activityType = .other
        configuration.locationType = .unknown

        do {
            let session = try HKWorkoutSession(healthStore: healthStore, configuration: configuration)
            let builder = session.associatedWorkoutBuilder()
            builder.dataSource = HKLiveWorkoutDataSource(
                healthStore: healthStore,
                workoutConfiguration: configuration
            )
            session.delegate = self
            builder.delegate = self
            self.session = session
            self.builder = builder

            session.startActivity(with: Date())
            builder.beginCollection(withStart: Date()) { _, error in
                if let error { print("DdoLieFlow: beginCollection failed \(error)") }
            }
        } catch {
            print("DdoLieFlow: workout session start failed \(error)")
        }
    }

    /// Mirrors shareIn(stopTimeoutMillis = 15_000): keep the session warm briefly
    /// so the next phase reuses it without re-warming the sensor.
    private func scheduleStopIfIdle() {
        guard continuations.isEmpty else { return }
        idleStopTask?.cancel()
        idleStopTask = Task { [weak self] in
            try? await Task.sleep(for: .seconds(15))
            guard !Task.isCancelled else { return }
            self?.stopSession()
        }
    }

    private func stopSession() {
        guard let session, let builder else { return }
        self.session = nil
        self.builder = nil
        session.end()
        builder.endCollection(withEnd: Date()) { _, _ in
            // The game never persists workouts to Health.
            builder.discardWorkout()
        }
    }
}

extension HealthKitHeartRateService: HKWorkoutSessionDelegate {
    nonisolated func workoutSession(
        _ workoutSession: HKWorkoutSession,
        didChangeTo toState: HKWorkoutSessionState,
        from fromState: HKWorkoutSessionState,
        date: Date
    ) {
        let available = toState == .running
        Task { @MainActor [weak self] in self?.broadcast(.availability(available)) }
    }

    nonisolated func workoutSession(_ workoutSession: HKWorkoutSession, didFailWithError error: Error) {
        print("DdoLieFlow: workout session error \(error)")
    }
}

extension HealthKitHeartRateService: HKLiveWorkoutBuilderDelegate {
    nonisolated func workoutBuilder(
        _ workoutBuilder: HKLiveWorkoutBuilder,
        didCollectDataOf collectedTypes: Set<HKSampleType>
    ) {
        let heartRateType = HKQuantityType(.heartRate)
        guard collectedTypes.contains(heartRateType) else { return }
        let bpmUnit = HKUnit.count().unitDivided(by: .minute())
        guard let bpm = workoutBuilder.statistics(for: heartRateType)?
            .mostRecentQuantity()?
            .doubleValue(for: bpmUnit)
        else { return }
        Task { @MainActor [weak self] in self?.broadcast(.data([bpm])) }
    }

    nonisolated func workoutBuilderDidCollectEvent(_ workoutBuilder: HKLiveWorkoutBuilder) {}
}
