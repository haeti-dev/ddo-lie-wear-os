package com.haeti.ddolie.presentation.result

import androidx.annotation.DrawableRes
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.wear.compose.material.Text
import com.haeti.ddolie.R
import com.haeti.ddolie.presentation.common.component.CtaButton
import com.haeti.ddolie.presentation.common.contract.LieResult
import com.haeti.ddolie.presentation.common.viewmodel.DdoLieViewModel
import com.haeti.ddolie.presentation.start.navigation.StartRoute
import com.haeti.ddolie.presentation.theme.DdoLieTheme
import kotlinx.coroutines.delay

@Composable
fun ResultScreen(
    navController: NavController,
    viewModel: DdoLieViewModel,
) {
    when (viewModel.currentState.isLie) {
        LieResult.TRUTH -> {
            TruthResultScreen(
                navController = navController,
            )
        }

        LieResult.LIE -> {
            LieResultScreen(
                navController = navController,
            )
        }

        null -> {
            // TODO : Handle error
        }
    }
}

@Composable
fun TruthResultScreen(
    navController: NavController,
) {
    ResultScreenContent(
        navController = navController,
        resultImg = R.drawable.img_truth,
        resultIcon = R.drawable.img_engle,
        backgroundImg = R.drawable.img_bg_true,
        resultType = LieResult.TRUTH,
        resultColor = DdoLieTheme.colors.green,
    )
}

@Composable
fun LieResultScreen(
    navController: NavController,
) {
    ResultScreenContent(
        navController = navController,
        resultImg = R.drawable.img_icon,
        resultIcon = R.drawable.img_devil,
        backgroundImg = R.drawable.img_background,
        resultType = LieResult.LIE,
        resultColor = DdoLieTheme.colors.redPrimary,
    )
}

@Composable
fun ResultScreenContent(
    navController: NavController,
    @DrawableRes resultImg: Int,
    @DrawableRes resultIcon: Int,
    @DrawableRes backgroundImg: Int,
    resultType: LieResult,
    resultColor: Color,
) {
    var showFullScreenImage by rememberSaveable { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(2000L)
        showFullScreenImage = false
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = showFullScreenImage, enter = fadeIn(), exit = fadeOut()
        ) {
            Image(
                painter = painterResource(id = resultImg),
                contentDescription = "result image",
                modifier = Modifier.fillMaxSize(),
            )
        }

        if (!showFullScreenImage) {
            Image(
                painter = painterResource(id = backgroundImg),
                contentDescription = "background image",
                modifier = Modifier.fillMaxSize(),
            )

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = resultIcon),
                    contentDescription = "Result Icon",
                    modifier = Modifier.size(50.dp),
                )

                Text(
                    text = buildAnnotatedString {
                        append("당신은\n")
                        withStyle(style = TextStyle(color = resultColor).toSpanStyle()) {
                            if (resultType == LieResult.LIE) append("거짓말")
                            else append("진실")
                        }
                        if (resultType == LieResult.LIE) append("을 하고 있어요")
                        else append("을 말하고 있어요")
                    },
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    color = DdoLieTheme.colors.white,
                )

                Spacer(modifier = Modifier.height(16.dp))

                CtaButton(
                    onClick = {
                        navController.navigate(StartRoute.Main) {
                            popUpTo(StartRoute.Main) { inclusive = true }
                        }
                    },
                    text = "다시하기",
                    chipColor = DdoLieTheme.colors.white,
                    contentColor = DdoLieTheme.colors.black,
                )
            }
        }
    }
}