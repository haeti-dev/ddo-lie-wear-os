package com.haeti.ddolie.presentation.analysis.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.haeti.ddolie.presentation.analysis.AnalysisScreen
import com.haeti.ddolie.presentation.common.viewmodel.DdoLieViewModel

fun NavGraphBuilder.addAnalysisGraph(
    navController: NavController,
    viewModel: DdoLieViewModel,
) {
    composable<AnalysisRoute.Main> {
        AnalysisScreen(
            navController = navController,
            viewModel = viewModel,
        )
    }
}