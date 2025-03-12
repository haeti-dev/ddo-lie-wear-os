package com.haeti.ddolie.presentation.init.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.haeti.ddolie.presentation.init.InitialScreen

fun NavGraphBuilder.addInitialGraph() {
    composable<InitialRoute.Main> {
        InitialScreen()
    }
}
