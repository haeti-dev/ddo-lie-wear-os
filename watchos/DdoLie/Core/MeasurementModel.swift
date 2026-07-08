import Foundation
import Observation

/// Mirrors wearos DdoLieViewModel.kt — the measurement state machine.
/// Intents, state fields, side effects, timings, and the verdict behavior
/// follow the wearos implementation as-is (see .claude/rules/parity.md).
@MainActor
@Observable
final class MeasurementModel {
    private static let baselineFallbackSamples = 2

    private(set) var state = DdoLieState()

    @ObservationIgnored private let heartRateService: HeartRateService
    @ObservationIgnored private var measurementTask: Task<Void, Never>?
    @ObservationIgnored private var initialTask: Task<Void, Never>?
    @ObservationIgnored private var finalizeTask: Task<Void, Never>?
    @ObservationIgnored private var prewarmTask: Task<Void, Never>?
    @ObservationIgnored private var heartRates: [Double] = []
    @ObservationIgnored private var initialSamples: [Double] = []
    @ObservationIgnored private var effectContinuations: [UUID: AsyncStream<DdoLieSideEffect>.Continuation] = [:]

    init(heartRateService: HeartRateService) {
        self.heartRateService = heartRateService
    }

    var isSensorAuthorized: Bool {
        heartRateService.isAuthorized
    }

    func requestHeartRateAuthorization() async {
        _ = await heartRateService.requestAuthorization()
    }

    /// Mirrors DdoLieViewModel.sideEffect (MutableSharedFlow): each screen
    /// subscribes for its lifetime; emissions broadcast to all live subscribers.
    func sideEffects() -> AsyncStream<DdoLieSideEffect> {
        let (stream, continuation) = AsyncStream<DdoLieSideEffect>.makeStream()
        let id = UUID()
        effectContinuations[id] = continuation
        continuation.onTermination = { [weak self] _ in
            Task { @MainActor in self?.effectContinuations[id] = nil }
        }
        return stream
    }

    private func postSideEffect(_ effect: DdoLieSideEffect) {
        for continuation in effectContinuations.values {
            continuation.yield(effect)
        }
    }

    func onIntent(_ intent: DdoLieIntent) {
        switch intent {
        case .startInitialMeasurement:
            startInitialMeasurement()
        case .startVoiceRecognition:
            startContinuousMeasurement()
        case .finalizeMeasurement:
            finalizeMeasurement()
        case .finishMeasurement:
            finishMeasurement()
        }
    }

    func startPrewarm() {
        guard prewarmTask == nil else { return }
        prewarmTask = Task {
            for await _ in heartRateService.heartRateMeasureStream() {
                // discard, just keep the sensor session warm
            }
        }
    }

    func stopPrewarm() {
        prewarmTask?.cancel()
        prewarmTask = nil
    }

    private func startInitialMeasurement() {
        initialTask?.cancel()
        measurementTask?.cancel()
        heartRates.removeAll()
        initialSamples.removeAll()
        state = DdoLieState()

        initialTask = Task {
            let collectTask = Task {
                for await message in heartRateService.heartRateMeasureStream() {
                    if case .data(let values) = message,
                       let last = values.last,
                       last > DdoLieConstants.Measurement.heartRateMinThreshold {
                        initialSamples.append(last)
                    }
                }
            }
            try? await Task.sleep(for: .milliseconds(DdoLieConstants.Measurement.initialMeasurementTimeout))
            collectTask.cancel()

            let average = initialSamples.isEmpty
                ? nil
                : Float(initialSamples.reduce(0, +) / Double(initialSamples.count))
            print("DdoLieFlow: [1/3] initial - samples=\(initialSamples.count), avg=\(String(describing: average))")
            state.initialHeartRateAvg = average
            postSideEffect(.navigateToVoiceRecognition)
        }
    }

    private func startContinuousMeasurement() {
        if let task = measurementTask, !task.isCancelled { return }

        measurementTask = Task {
            for await message in heartRateService.heartRateMeasureStream() {
                if case .data(let values) = message,
                   let last = values.last,
                   last > DdoLieConstants.Measurement.heartRateMinThreshold {
                    heartRates.append(last)
                }
            }
        }
    }

    private func finishMeasurement() {
        postSideEffect(.navigateToAnalysis)
    }

    private func finalizeMeasurement() {
        guard finalizeTask == nil else { return }
        finalizeTask = Task {
            try? await Task.sleep(for: .milliseconds(DdoLieConstants.Measurement.finalizeDelay))
            measurementTask?.cancel()

            let initialAvgFromState = state.initialHeartRateAvg

            // If the initial measurement is empty, use the first samples of the
            // analysis measurement as the baseline (same fallback as wearos).
            let fallbackSamples = Array(heartRates.prefix(Self.baselineFallbackSamples))
            let baselineFromAnalysis: Float? = initialAvgFromState == nil && !fallbackSamples.isEmpty
                ? Float(fallbackSamples.reduce(0, +) / Double(fallbackSamples.count))
                : nil

            let effectiveInitialAvg = initialAvgFromState ?? baselineFromAnalysis

            // When the baseline was borrowed from the analysis samples, exclude it
            // from the comparison set.
            let comparisonSet = baselineFromAnalysis != nil
                ? Array(heartRates.dropFirst(Self.baselineFallbackSamples))
                : heartRates

            let maxHeartRate = comparisonSet.max().map(Float.init)
            let diff: Float
            if let effectiveInitialAvg, let maxHeartRate {
                diff = maxHeartRate - effectiveInitialAvg
            } else {
                diff = 0
            }

            let finalAvg = heartRates.isEmpty
                ? nil
                : Float(heartRates.reduce(0, +) / Double(heartRates.count))
            print("DdoLieFlow: [2/3] analysis - samples=\(heartRates.count), avg=\(String(describing: finalAvg)), max=\(String(describing: maxHeartRate)), baselineFallback=\(String(describing: baselineFromAnalysis))")

            let result = determineLieResult(diff: diff)

            state.finalHeartRateAvg = finalAvg
            state.isLie = result
            postSideEffect(.navigateToResult)
            stopPrewarm()
            finalizeTask = nil
        }
    }

    // Mirrors DdoLieViewModel.determineLieResult as-is.
    private func determineLieResult(diff: Float) -> LieResult {
        let heartRateScore: Float
        if diff >= 3.0 { heartRateScore = 0.85 }
        else if diff >= 2.0 { heartRateScore = 0.70 }
        else if diff >= 1.5 { heartRateScore = 0.55 }
        else if diff >= 1.0 { heartRateScore = 0.50 }
        else if diff >= 0.5 { heartRateScore = 0.40 }
        else if diff >= -0.5 { heartRateScore = 0.45 }
        else { heartRateScore = 0.30 }

        let randomFactor = Float.random(in: 0..<1)
        let finalScore = (heartRateScore * 0.6) + (randomFactor * 0.4)

        let result: LieResult = finalScore >= 0.5 ? .lie : .truth
        print("DdoLieFlow: [3/3] result - diff=\(diff), hrScore=\(heartRateScore), random=\(randomFactor), finalScore=\(finalScore), verdict=\(result)")
        return result
    }
}
