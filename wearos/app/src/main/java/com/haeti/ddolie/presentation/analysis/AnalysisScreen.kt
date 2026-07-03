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
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.wear.compose.material.Text
import com.haeti.ddolie.R
import com.haeti.ddolie.presentation.common.contract.DdoLieIntent
import com.haeti.ddolie.presentation.common.contract.DdoLieSideEffect
import com.haeti.ddolie.presentation.common.viewmodel.DdoLieViewModel
import com.haeti.ddolie.presentation.result.navigation.ResultRoute
import com.haeti.ddolie.presentation.theme.DdoLieTheme
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun AnalysisScreen(
    navController: NavController,
    viewModel: DdoLieViewModel,
) {
    LaunchedEffect(Unit) {
        viewModel.onIntent(DdoLieIntent.FinalizeMeasurement)

        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is DdoLieSideEffect.NavigateToResult -> {
                    navController.navigate(ResultRoute.Main)
                }

                else -> {}
            }
        }
    }

    var activeCircleIndex by rememberSaveable { mutableIntStateOf(0) }
    val dotPhases = listOf(".", "..", "...")
    var dotPhaseIndex by rememberSaveable { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        repeat(5) {
            repeat(8) { index ->
                activeCircleIndex = index
                delay(125)
            }
        }
    }

    LaunchedEffect(Unit) {
        repeat(15) {
            dotPhaseIndex = (dotPhaseIndex + 1) % 3
            delay(333)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
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
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
        }
    }
}