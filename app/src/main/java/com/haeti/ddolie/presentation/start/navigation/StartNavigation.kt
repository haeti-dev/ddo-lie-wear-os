package com.haeti.ddolie.presentation.start.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.haeti.ddolie.presentation.start.StartScreen

fun NavGraphBuilder.addStartGraph() {
	composable<StartRoute.Main> {
		StartScreen()
	}
}
