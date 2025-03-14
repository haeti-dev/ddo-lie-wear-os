package com.haeti.ddolie.presentation.recognition

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import androidx.wear.compose.material.Text
import com.haeti.ddolie.presentation.common.util.toTextDp
import com.haeti.ddolie.presentation.common.viewmodel.DdoLieViewModel

@Composable
fun VoiceRecognitionScreen(
    navController: NavController,
    viewModel: DdoLieViewModel,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = viewModel.currentState.initialHeartRateAvg.toString(),
            fontSize = 15.toTextDp,
            fontWeight = FontWeight.Bold,
            lineHeight = 23.toTextDp,
            textAlign = TextAlign.Center
        )
    }
}