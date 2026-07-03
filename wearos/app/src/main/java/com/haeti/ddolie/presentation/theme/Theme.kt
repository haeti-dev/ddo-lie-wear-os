package com.haeti.ddolie.presentation.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.wear.compose.material.Colors

@Composable
fun DdoLieTheme(
	content: @Composable () -> Unit
) {
	val colors = DdoLieColors()

	CompositionLocalProvider(
		LocalDdoLieColors provides colors,
		content = content
	)
}

object DdoLieTheme {
	val colors: DdoLieColors
		@Composable
		get() = LocalDdoLieColors.current
}
