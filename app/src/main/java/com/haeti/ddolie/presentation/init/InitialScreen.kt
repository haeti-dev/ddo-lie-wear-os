package com.haeti.ddolie.presentation.init

import android.Manifest
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.wear.compose.material.Text
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.haeti.ddolie.R
import com.haeti.ddolie.presentation.common.component.CtaButton
import com.haeti.ddolie.presentation.common.contract.DdoLieIntent
import com.haeti.ddolie.presentation.common.contract.DdoLieSideEffect
import com.haeti.ddolie.presentation.common.util.toTextDp
import com.haeti.ddolie.presentation.common.viewmodel.DdoLieViewModel
import com.haeti.ddolie.presentation.recognition.navigation.VoiceRecognitionRoute
import com.haeti.ddolie.presentation.theme.DdoLieTheme
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun InitialScreen(
    navController: NavController,
    viewModel: DdoLieViewModel,
) {
    val permissionState = rememberPermissionState(
        permission = Manifest.permission.BODY_SENSORS,
        onPermissionResult = { granted ->
            Log.e("InitialScreen", "Permission granted: $granted")
        }
    )

    var isMeasuring by remember { mutableStateOf(false) }
    var activeCircleIndex by remember { mutableIntStateOf(0) }

    val activeCircleColor = Color(0xFFF44522)
    val inactiveCircleColor = Color(0xFF333333)
    val circleRadius = 6f

    LaunchedEffect(true) {
        if (!permissionState.status.isGranted) permissionState.launchPermissionRequest()

        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is DdoLieSideEffect.NavigateToVoiceRecognition -> {
                    navController.navigate(VoiceRecognitionRoute.Main)
                }

                is DdoLieSideEffect.ShowError -> {
                    // TODO : Show error Toast
                }

                else -> {}
            }
        }
    }

    LaunchedEffect(isMeasuring) {
        Log.e("InitialScreen", "isMeasuring: $isMeasuring")
        if (isMeasuring) {
            repeat(4) {
                repeat(8) {
                    activeCircleIndex = it
                    delay(125)
                }
            }
            // TODO : 측정 완료 로직 추가
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val centerX = size.width / 2
            val centerY = size.height / 2
            val radius = size.minDimension * 0.47f

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

        AnimatedVisibility(
            visible = !isMeasuring,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "거짓말 탐지 전\n상태를 측정하세요",
                    fontSize = 20.toTextDp,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 30.toTextDp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(18.dp))

                CtaButton(
                    text = "시작하기",
                    onClick = {
                        isMeasuring = true
                        viewModel.onIntent(DdoLieIntent.StartInitialMeasurement)
                    }
                )
            }
        }

        AnimatedVisibility(
            visible = isMeasuring,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            val dotPhases = listOf(".", "..", "...")
            var dotPhaseIndex by remember { mutableIntStateOf(0) }

            LaunchedEffect(isMeasuring) {
                if (isMeasuring) {
                    repeat(12) {
                        dotPhaseIndex = (dotPhaseIndex + 1) % 3
                        delay(333)
                    }
                }
            }

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = painterResource(R.drawable.img_graph),
                    contentDescription = "graph",
                    modifier = Modifier.fillMaxSize(),
                )

                Text(
                    text = "측정 중" + dotPhases[dotPhaseIndex],
                    fontSize = 20.toTextDp,
                    fontWeight = FontWeight.SemiBold,
                    color = DdoLieTheme.colors.white,
                )
            }
        }
    }
}
