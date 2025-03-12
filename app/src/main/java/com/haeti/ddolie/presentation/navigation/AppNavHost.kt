package com.haeti.ddolie.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import com.haeti.ddolie.presentation.init.navigation.addInitialGraph
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
        NavHost(
            navController = navigator.navController,
            startDestination = navigator.startDestination,
        ) {
            addStartGraph(navController)
            addInitialGraph()
        }
    }
}
