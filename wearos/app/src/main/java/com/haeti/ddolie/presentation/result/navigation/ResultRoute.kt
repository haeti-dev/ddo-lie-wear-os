package com.haeti.ddolie.presentation.result.navigation

import com.haeti.ddolie.presentation.navigation.NavRoute
import kotlinx.serialization.Serializable

@Serializable
sealed interface ResultRoute : NavRoute {
    @Serializable
    data object Main : ResultRoute
}