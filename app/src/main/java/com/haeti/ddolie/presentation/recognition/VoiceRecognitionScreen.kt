package com.haeti.ddolie.presentation.recognition

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.wear.compose.material.Text
import com.haeti.ddolie.R
import com.haeti.ddolie.presentation.analysis.navigation.AnalysisRoute
import com.haeti.ddolie.presentation.common.component.CtaButton
import com.haeti.ddolie.presentation.common.contract.DdoLieIntent
import com.haeti.ddolie.presentation.common.contract.DdoLieSideEffect
import com.haeti.ddolie.presentation.common.util.DdoLieConstants.Animation
import com.haeti.ddolie.presentation.common.viewmodel.DdoLieViewModel
import com.haeti.ddolie.presentation.theme.DdoLieTheme
import kotlinx.coroutines.delay

@Composable
fun VoiceRecognitionScreen(
    navController: NavController,
    viewModel: DdoLieViewModel,
) {
    var isTalking by remember { mutableStateOf(false) }

    LaunchedEffect(true) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is DdoLieSideEffect.NavigateToAnalysis -> {
                    navController.navigate(AnalysisRoute.Main)
                }

                else -> {}
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(R.drawable.img_background),
            contentDescription = "graph",
            modifier = Modifier.fillMaxSize(),
        )

        AnimatedVisibility(
            visible = !isTalking,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "시작 버튼을 누르고\n말해보세요",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 30.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(18.dp))

                CtaButton(
                    text = "시작하기",
                    onClick = {
                        isTalking = true
                        viewModel.onIntent(DdoLieIntent.StartVoiceRecognition)
                    }
                )
            }
        }

        AnimatedVisibility(
            visible = isTalking,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            val dotPhases = listOf(".", "..", "...")
            var dotPhaseIndex by remember { mutableIntStateOf(0) }

            LaunchedEffect(isTalking) {
                while (isTalking) {
                    dotPhaseIndex = (dotPhaseIndex + 1) % 3
                    delay(Animation.DOT_ANIMATION_DELAY)
                }
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_mask),
                    contentDescription = "mask",
                    modifier = Modifier.size(53.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "센서 작동 중" + dotPhases[dotPhaseIndex],
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = DdoLieTheme.colors.white,
                )

                Spacer(modifier = Modifier.height(20.dp))

                CtaButton(
                    text = "종료하기",
                    onClick = {
                        viewModel.onIntent(DdoLieIntent.FinishMeasurement)
                    }
                )
            }
        }
    }
}