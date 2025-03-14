package com.haeti.ddolie.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import com.haeti.ddolie.presentation.analysis.navigation.addAnalysisGraph
import com.haeti.ddolie.presentation.common.viewmodel.DdoLieViewModel
import com.haeti.ddolie.presentation.common.viewmodel.DdoLieViewModelFactory
import com.haeti.ddolie.presentation.init.navigation.addInitialGraph
import com.haeti.ddolie.presentation.recognition.navigation.addVoiceRecognitionGraph
import com.haeti.ddolie.presentation.start.navigation.addStartGraph

@Composable
fun AppNavHost(
    navController: NavController,
    modifier: Modifier = Modifier,
    navigator: AppNavigator,
) {
    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        val context = LocalContext.current
        val viewModel: DdoLieViewModel = viewModel(
            factory = DdoLieViewModelFactory(context)
        )

        NavHost(
            navController = navigator.navController,
            startDestination = navigator.startDestination,
        ) {
            addStartGraph(navController)
            addInitialGraph(navController = navController, viewModel = viewModel)
            addVoiceRecognitionGraph(navController = navController, viewModel = viewModel)
            addAnalysisGraph(navController = navController, viewModel = viewModel)
        }
    }
}
