package com.haeti.ddolie.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.tooling.preview.devices.WearDevices
import com.haeti.ddolie.R
import com.haeti.ddolie.presentation.theme.DdoLieTheme

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		installSplashScreen()

		super.onCreate(savedInstanceState)

		setTheme(android.R.style.Theme_DeviceDefault)

		setContent {
			WearApp("Android")
		}
	}
}

@Composable
fun WearApp(greetingName: String) {
	DdoLieTheme {
		Box(
			modifier = Modifier
				.fillMaxSize()
				.background(DdoLieTheme.colors.black),
			contentAlignment = Alignment.Center
		) {
			TimeText()
			Greeting(greetingName = greetingName)
		}
	}
}

@Composable
fun Greeting(greetingName: String) {
	Text(
		modifier = Modifier.fillMaxWidth(),
		textAlign = TextAlign.Center,
		color = DdoLieTheme.colors.redPrimary,
		text = stringResource(R.string.hello_world, greetingName)
	)
}

@Preview(device = WearDevices.SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
	WearApp("Preview Android")
}
