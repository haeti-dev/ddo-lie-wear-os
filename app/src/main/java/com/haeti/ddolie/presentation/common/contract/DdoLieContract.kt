package com.haeti.ddolie.presentation.common.contract

import com.haeti.ddolie.presentation.common.base.SideEffect
import com.haeti.ddolie.presentation.common.base.UiIntent
import com.haeti.ddolie.presentation.common.base.UiState

enum class LieResult {
    LIE, TRUTH
}

sealed class DdoLieIntent : UiIntent {
    data object StartInitialMeasurement : DdoLieIntent()
    data object StartVoiceRecognition : DdoLieIntent()
    data object FinishMeasurement : DdoLieIntent()
    data object FinalizeMeasurement : DdoLieIntent()
}

data class DdoLieState(
    val initialHeartRateAvg: Float? = null,
    val finalHeartRateAvg: Float? = null,
    val isLie: LieResult? = null,
) : UiState

sealed class DdoLieSideEffect : SideEffect {
    data object NavigateToVoiceRecognition : DdoLieSideEffect()
    data object NavigateToAnalysis : DdoLieSideEffect()
    data object NavigateToResult : DdoLieSideEffect()
    data class ShowError(val message: String) : DdoLieSideEffect()
}