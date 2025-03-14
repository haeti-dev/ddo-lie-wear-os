package com.haeti.ddolie.presentation.common.viewmodel

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.haeti.ddolie.presentation.common.base.BaseViewModel
import com.haeti.ddolie.presentation.common.contract.DdoLieIntent
import com.haeti.ddolie.presentation.common.contract.DdoLieSideEffect
import com.haeti.ddolie.presentation.common.contract.DdoLieState
import com.haeti.ddolie.presentation.common.manager.HealthServiceManager
import com.haeti.ddolie.presentation.common.manager.MeasureMessage
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

class DdoLieViewModel(
    private val healthServiceManager: HealthServiceManager,
) : BaseViewModel<DdoLieIntent, DdoLieState, DdoLieSideEffect>(DdoLieState()) {
    override fun onIntent(intent: DdoLieIntent) {
        when (intent) {
            is DdoLieIntent.StartInitialMeasurement -> {
                startInitialMeasurement()
            }
        }
    }

    private fun startInitialMeasurement() {
        viewModelScope.launch {
            val heartRates = mutableListOf<Double>()

            try {
                withTimeout(4000L) {
                    healthServiceManager.heartRateMeasureFlow()
                        .collect { message ->
                            when (message) {
                                is MeasureMessage.MeasureData -> {
                                    val lastValue = message.data.last().value
                                    if (lastValue > 0.0) {
                                        heartRates.add(lastValue)
                                    }
                                    Log.e("ViewModel", "Heart rate data received: $lastValue")
                                }

                                is MeasureMessage.MeasureAvailability -> {
                                    // TODO: 가용성 처리
                                }
                            }
                        }
                }
            } catch (e: TimeoutCancellationException) {
                Log.e("ViewModel", "Initial measurement timed out: ${e.message}")
                // TODO: 타임아웃 처리
            }

            val average = if (heartRates.isNotEmpty()) heartRates.average().toFloat() else null
            intent { copy(initialHeartRateAvg = average) }
            postSideEffect(DdoLieSideEffect.NavigateToVoiceRecognition)
        }
    }
}