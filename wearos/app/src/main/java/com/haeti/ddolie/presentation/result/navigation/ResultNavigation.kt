package com.haeti.ddolie.presentation.result.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.haeti.ddolie.presentation.common.viewmodel.DdoLieViewModel
import com.haeti.ddolie.presentation.result.ResultScreen

fun NavGraphBuilder.addResultGraph(
    navController: NavController,
    viewModel: DdoLieViewModel,
) {
    composable<ResultRoute.Main> {
        ResultScreen(navController = navController, viewModel = viewModel)
    }
}