package com.haeti.ddolie.presentation.analysis

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.navigation.NavController
import androidx.wear.compose.material.Text
import com.haeti.ddolie.R
import com.haeti.ddolie.presentation.common.contract.DdoLieIntent
import com.haeti.ddolie.presentation.common.util.toTextDp
import com.haeti.ddolie.presentation.common.viewmodel.DdoLieViewModel
import com.haeti.ddolie.presentation.theme.DdoLieTheme
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun AnalysisScreen(
    navController: NavController,
    viewModel: DdoLieViewModel,
) {
    // 분석 화면 진입 시 최종 측정 완료 로직 실행 (내부적으로 5초 더 측정하고 평균 계산)
    LaunchedEffect(Unit) {
        viewModel.onIntent(DdoLieIntent.FinalizeMeasurement)
    }

    // 원 애니메이션과 점 애니메이션을 위한 상태
    var activeCircleIndex by rememberSaveable { mutableIntStateOf(0) }
    val dotPhases = listOf(".", "..", "...")
    var dotPhaseIndex by rememberSaveable { mutableIntStateOf(0) }

    // FinalizeMeasurement가 진행되는 5초 동안 8개의 원이 순차적으로 켜지는 애니메이션
    LaunchedEffect(Unit) {
        // 5초 동안 (5회 순환, 1회 순환 = 8 * 125ms = 1초)
        repeat(5) {
            repeat(8) { index ->
                activeCircleIndex = index
                delay(125)
            }
        }
    }

    // 텍스트의 점 부분이 반복되는 애니메이션 (약 5초 동안)
    LaunchedEffect(Unit) {
        // 333ms마다 점이 바뀌도록 (5초 정도면 15회 반복)
        repeat(15) {
            dotPhaseIndex = (dotPhaseIndex + 1) % 3
            delay(333)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // 배경에 8개의 원을 그리는 Canvas (초기 화면과 동일한 방식)
        Canvas(modifier = Modifier.fillMaxSize()) {
            val centerX = size.width / 2
            val centerY = size.height / 2
            val radius = size.minDimension * 0.47f
            val activeCircleColor = Color(0xFFF44522)
            val inactiveCircleColor = Color(0xFF333333)
            val circleRadius = 6f

            for (index in 0 until 8) {
                val angle = Math.toRadians(270.0 + index * 45.0).toFloat()
                val x = centerX + (cos(angle) * radius)
                val y = centerY + (sin(angle) * radius)
                val color =
                    if (index == activeCircleIndex) activeCircleColor else inactiveCircleColor
                drawCircle(
                    color = color,
                    radius = circleRadius * density,
                    center = Offset(x, y)
                )
            }
        }

        // 가운데 텍스트
        // "거짓말"은 redPrimary 색, " 탐지중"과 점은 흰색으로 처리
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.img_graph),
                contentDescription = "graph",
                modifier = Modifier.fillMaxSize(),
            )

            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = DdoLieTheme.colors.redPrimary)) {
                        append("거짓말")
                    }
                    withStyle(style = SpanStyle(color = DdoLieTheme.colors.white)) {
                        append(" 탐지중")
                        append(dotPhases[dotPhaseIndex])
                    }
                },
                fontSize = 20.toTextDp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
        }
    }
}