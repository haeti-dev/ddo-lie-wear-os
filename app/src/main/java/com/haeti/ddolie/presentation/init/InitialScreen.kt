package com.haeti.ddolie.presentation.init

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Text
import androidx.wear.compose.ui.tooling.preview.WearPreviewDevices
import com.haeti.ddolie.presentation.common.component.CtaButton
import com.haeti.ddolie.presentation.common.util.toTextDp

@Composable
fun InitialScreen() {
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

@WearPreviewDevices
@Composable
fun InitialScreenPreview() {
    InitialScreen()
}