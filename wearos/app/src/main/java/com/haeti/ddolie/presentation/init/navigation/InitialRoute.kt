package com.haeti.ddolie.presentation.init.navigation

import com.haeti.ddolie.presentation.navigation.NavRoute
import kotlinx.serialization.Serializable

@Serializable
sealed interface InitialRoute : NavRoute {
    @Serializable
    data object Main : InitialRoute
}
