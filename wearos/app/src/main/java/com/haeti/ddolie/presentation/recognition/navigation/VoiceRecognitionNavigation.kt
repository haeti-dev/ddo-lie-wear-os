package com.haeti.ddolie.presentation.recognition.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.haeti.ddolie.presentation.common.viewmodel.DdoLieViewModel
import com.haeti.ddolie.presentation.recognition.VoiceRecognitionScreen

fun NavGraphBuilder.addVoiceRecognitionGraph(
    navController: NavController,
    viewModel: DdoLieViewModel,
) {
    composable<VoiceRecognitionRoute.Main> {
        VoiceRecognitionScreen(navController, viewModel)
    }
}