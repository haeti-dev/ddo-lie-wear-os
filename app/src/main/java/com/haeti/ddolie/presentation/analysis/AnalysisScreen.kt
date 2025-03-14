package com.haeti.ddolie.presentation.analysis

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.wear.compose.material.Text
import com.haeti.ddolie.presentation.common.contract.DdoLieIntent
import com.haeti.ddolie.presentation.common.viewmodel.DdoLieViewModel

@Composable
fun AnalysisScreen(
    navController: NavController,
    viewModel: DdoLieViewModel,
) {
    // 분석 화면 진입 시 최종 측정 완료 로직 실행
    LaunchedEffect(Unit) {
        viewModel.onIntent(DdoLieIntent.FinalizeMeasurement)
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        viewModel.currentState.finalHeartRateAvg?.let { avg ->
            Text(
                text = "최종 평균 심박수: $avg",
            )
        } ?: Text(
            text = "측정 중...",
        )
    }
}