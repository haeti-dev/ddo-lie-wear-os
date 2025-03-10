package com.haeti.ddolie.presentation.start.navigation

import com.haeti.ddolie.presentation.navigation.NavRoute
import kotlinx.serialization.Serializable

@Serializable
sealed interface StartRoute : NavRoute {
	@Serializable
	data object Main : StartRoute
}
