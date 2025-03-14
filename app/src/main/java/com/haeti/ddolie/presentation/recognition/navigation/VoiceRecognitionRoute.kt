package com.haeti.ddolie.presentation.recognition.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface VoiceRecognitionRoute {
    @Serializable
    data object Main : VoiceRecognitionRoute
}