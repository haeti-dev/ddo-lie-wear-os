package com.haeti.ddolie.presentation.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val RedPrimary = Color(0xFFF44522)
val RedSecondary = Color(0x33FB4723)
val RedTertiary = Color(0x40FB4723)
val Green = Color(0xFF68EDCE)
val White = Color(0xFFFFFFFF)
val Black = Color(0xFF000000)

data class DdoLieColors(
	val redPrimary: Color = RedPrimary,
	val redSecondary: Color = RedSecondary,
	val redTertiary: Color = RedTertiary,
	val green: Color = Green,
	val white: Color = White,
	val black: Color = Black
)

val LocalDdoLieColors = staticCompositionLocalOf { DdoLieColors() }
