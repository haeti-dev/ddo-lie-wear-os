package com.haeti.ddolie.presentation.analysis.navigation

import com.haeti.ddolie.presentation.navigation.NavRoute
import kotlinx.serialization.Serializable

@Serializable
sealed interface AnalysisRoute : NavRoute {
    @Serializable
    data object Main : AnalysisRoute
}