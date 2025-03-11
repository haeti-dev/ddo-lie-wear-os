package com.haeti.ddolie.presentation.start

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Text
import androidx.wear.compose.ui.tooling.preview.WearPreviewDevices
import com.haeti.ddolie.R
import com.haeti.ddolie.presentation.common.util.toTextDp
import com.haeti.ddolie.presentation.theme.DdoLieTheme

@Composable
fun StartScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.img_logo),
            contentDescription = "logo",
            modifier = Modifier.size(width = 108.dp, height = 70.dp)
        )

        Text(
            text = "시작하려면 화면을\n터치해주세요",
            fontSize = 15.toTextDp,
            fontWeight = FontWeight.Bold,
            lineHeight = 23.toTextDp,
            textAlign = TextAlign.Center
        )
    }
}

@WearPreviewDevices
@Composable
fun PreviewStartScreen() {
    DdoLieTheme {
        StartScreen()
    }
}
