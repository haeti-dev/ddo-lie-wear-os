package com.haeti.ddolie.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.haeti.ddolie.presentation.start.navigation.StartRoute

class AppNavigator(
	val navController: NavHostController
) {
	private val currentDestination: NavDestination?
		@Composable get() = navController.currentBackStackEntryAsState().value?.destination

	val startDestination = StartRoute.Main

	fun navigateToStart() {
		navController.navigate(startDestination) {
			popUpTo(0) { inclusive = true }
		}
	}

	fun popBackStack() {
		navController.popBackStack()
	}
}

@Composable
fun rememberAppNavigator(navController: NavHostController = rememberNavController()): AppNavigator =
	remember(navController) { AppNavigator(navController) }
