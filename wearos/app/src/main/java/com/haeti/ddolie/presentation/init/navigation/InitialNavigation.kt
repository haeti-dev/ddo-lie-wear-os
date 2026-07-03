package com.haeti.ddolie.presentation.init.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.haeti.ddolie.presentation.common.viewmodel.DdoLieViewModel
import com.haeti.ddolie.presentation.init.InitialScreen

fun NavGraphBuilder.addInitialGraph(
    navController: NavController,
    viewModel: DdoLieViewModel,
) {
    composable<InitialRoute.Main> {
        InitialScreen(navController = navController, viewModel = viewModel)
    }
}
