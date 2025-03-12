package com.haeti.ddolie.presentation.init

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Text
import androidx.wear.compose.ui.tooling.preview.WearPreviewDevices
import com.haeti.ddolie.presentation.common.component.CtaButton
import com.haeti.ddolie.presentation.common.util.toTextDp
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun InitialScreen() {
    val circleColor = Color(0xFFF44522)
    val circleRadius = 6f

    Box(modifier = Modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val centerX = size.width / 2
            val centerY = size.height / 2
            val radius = size.minDimension * 0.47f

            for (i in 0 until 8) {
                val angle = Math.toRadians(270.0 + i * 45.0).toFloat()

                val x = centerX + (cos(angle) * radius)
                val y = centerY + (sin(angle) * radius)

                drawCircle(
                    color = circleColor,
                    radius = circleRadius * density,
                    center = Offset(x, y)
                )
            }
        }

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

            CtaButton(text = "시작하기")
        }
    }
}

@WearPreviewDevices
@Composable
fun InitialScreenPreview() {
    InitialScreen()
}