package com.haeti.ddolie.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.haeti.ddolie.presentation.navigation.AppNavHost
import com.haeti.ddolie.presentation.navigation.rememberAppNavigator
import com.haeti.ddolie.presentation.theme.DdoLieTheme

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		installSplashScreen()
		super.onCreate(savedInstanceState)

		setContent {
			DdoLieTheme {
				val navController = rememberNavController()
				val navigator = rememberAppNavigator(navController)

				AppNavHost(
					navController = navController,
					navigator = navigator,
				)
			}
		}
	}
}
