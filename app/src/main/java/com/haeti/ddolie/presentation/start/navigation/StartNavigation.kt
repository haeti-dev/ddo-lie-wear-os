package com.haeti.ddolie.presentation.start.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.haeti.ddolie.presentation.start.StartScreen

fun NavGraphBuilder.addStartGraph(navController: NavController) {
    composable<StartRoute.Main> {
        StartScreen(navController)
    }
}
