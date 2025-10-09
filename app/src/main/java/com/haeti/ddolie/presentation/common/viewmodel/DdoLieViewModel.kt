package com.haeti.ddolie.presentation.common.viewmodel

import androidx.lifecycle.viewModelScope
import com.haeti.ddolie.presentation.common.base.BaseViewModel
import com.haeti.ddolie.presentation.common.contract.DdoLieIntent
import com.haeti.ddolie.presentation.common.contract.DdoLieSideEffect
import com.haeti.ddolie.presentation.common.contract.DdoLieState
import com.haeti.ddolie.presentation.common.contract.LieResult
import com.haeti.ddolie.presentation.common.manager.HealthServiceManager
import com.haeti.ddolie.presentation.common.manager.MeasureMessage
import com.haeti.ddolie.presentation.common.util.DdoLieConstants.Measurement.FINALIZE_DELAY
import com.haeti.ddolie.presentation.common.util.DdoLieConstants.Measurement.HEART_RATE_MIN_THRESHOLD
import com.haeti.ddolie.presentation.common.util.DdoLieConstants.Measurement.INITIAL_MEASUREMENT_TIMEOUT
import kotlinx.coroutines.Job
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import kotlin.random.Random

class DdoLieViewModel(
    private val healthServiceManager: HealthServiceManager,
) : BaseViewModel<DdoLieIntent, DdoLieState, DdoLieSideEffect>(DdoLieState()) {
    var measurementJob: Job? = null
    val heartRates = mutableListOf<Double>()

    override fun onIntent(intent: DdoLieIntent) {
        when (intent) {
            is DdoLieIntent.StartInitialMeasurement -> {
                startInitialMeasurement()
            }

            is DdoLieIntent.StartVoiceRecognition -> {
                startContinuousMeasurement()
            }

            is DdoLieIntent.FinalizeMeasurement -> {
                finalizeMeasurement()
            }

            is DdoLieIntent.FinishMeasurement -> {
                finishMeasurement()
            }
        }
    }

    private fun startInitialMeasurement() {
        viewModelScope.launch {
            val heartRates = mutableListOf<Double>()

            try {
                withTimeout(INITIAL_MEASUREMENT_TIMEOUT) {
                    healthServiceManager.heartRateMeasureFlow()
                        .collect { message ->
                            when (message) {
                                is MeasureMessage.MeasureData -> {
                                    val lastValue = message.data.last().value
                                    if (lastValue > HEART_RATE_MIN_THRESHOLD) {
                                        heartRates.add(lastValue)
                                    }
                                }

                                is MeasureMessage.MeasureAvailability -> {
                                    // 센서 가용성 처리
                                }
                            }
                        }
                }
            } catch (_: TimeoutCancellationException) {
                // 타임아웃 처리
            }

            val average = if (heartRates.isNotEmpty()) heartRates.average().toFloat() else null
            intent { copy(initialHeartRateAvg = average) }
            postSideEffect(DdoLieSideEffect.NavigateToVoiceRecognition)
        }
    }

    private fun startContinuousMeasurement() {
        if (measurementJob?.isActive == true) return

        measurementJob = viewModelScope.launch {
            healthServiceManager.heartRateMeasureFlow()
                .collect { message ->
                    when (message) {
                        is MeasureMessage.MeasureData -> {
                            val lastValue = message.data.last().value
                            if (lastValue > HEART_RATE_MIN_THRESHOLD) {
                                heartRates.add(lastValue)
                            }
                        }

                        is MeasureMessage.MeasureAvailability -> {
                            // 센서 가용성 처리
                        }
                    }
                }
        }
    }

    private fun finishMeasurement() {
        postSideEffect(DdoLieSideEffect.NavigateToAnalysis)
    }

    private fun finalizeMeasurement() {
        viewModelScope.launch {
            delay(FINALIZE_DELAY)
            measurementJob?.cancel()

            // 피크 기반 차이 계산
            val maxHeartRate = if (heartRates.isNotEmpty()) {
                heartRates.maxOrNull()?.toFloat()
            } else null

            val initialAvg = currentState.initialHeartRateAvg
            val diff = if (initialAvg != null && maxHeartRate != null) {
                maxHeartRate - initialAvg
            } else 0f

            val result = determineLieResult(diff)

            val finalAvg = if (heartRates.isNotEmpty()) heartRates.average().toFloat() else null

            intent { copy(finalHeartRateAvg = finalAvg, isLie = result) }
            postSideEffect(DdoLieSideEffect.NavigateToResult)
        }
    }

    // 가중 랜덤 판정 로직
    private fun determineLieResult(diff: Float): LieResult {
        // 1. 심박수 기여도 계산 (0.0 ~ 1.0)
        val heartRateScore = when {
            diff >= 3.0f -> 0.85f   // 확실히 높음
            diff >= 2.0f -> 0.70f   // 꽤 높음
            diff >= 1.5f -> 0.55f   // 애매함
            diff >= 1.0f -> 0.50f   // 중립에 가까움
            diff >= 0.5f -> 0.40f   // 약간 변화
            diff >= -0.5f -> 0.45f  // 거의 변화 없음 (중립)
            else -> 0.30f           // 오히려 낮아짐
        }

        // 2. 랜덤 요소 (0.0 ~ 1.0)
        val randomFactor = Random.nextFloat()

        // 3. 최종 스코어 (가중 평균: 심박수 60% + 랜덤 40%)
        val finalScore = (heartRateScore * 0.6f) + (randomFactor * 0.4f)

        // 4. 판정 (임계값 0.5)
        return if (finalScore >= 0.5f) LieResult.LIE else LieResult.TRUTH
    }
}
