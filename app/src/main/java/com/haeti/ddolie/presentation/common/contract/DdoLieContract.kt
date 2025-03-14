package com.haeti.ddolie.presentation.common.contract

import com.haeti.ddolie.presentation.common.base.SideEffect
import com.haeti.ddolie.presentation.common.base.UiIntent
import com.haeti.ddolie.presentation.common.base.UiState

sealed class DdoLieIntent : UiIntent {
    data object StartInitialMeasurement : DdoLieIntent()
}

data class DdoLieState(
    val initialHeartRateAvg: Float? = null,
) : UiState

sealed class DdoLieSideEffect : SideEffect {
    data object NavigateToVoiceRecognition : DdoLieSideEffect()
    data class ShowError(val message: String) : DdoLieSideEffect()
}