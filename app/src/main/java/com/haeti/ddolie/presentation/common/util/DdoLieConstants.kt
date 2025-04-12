package com.haeti.ddolie.presentation.common.util

object DdoLieConstants {
    object Measurement {
        const val INITIAL_MEASUREMENT_TIMEOUT = 4000L
        const val FINALIZE_DELAY = 5000L
        const val HEART_RATE_MIN_THRESHOLD = 0.0
        const val LIE_THRESHOLD = 2f
    }

    object Vibration {
        const val VIBRATION_DURATION = 1000L
        const val VIBRATION_AMPLITUDE = 200
    }

    object Animation {
        const val RESULT_SCREEN_DELAY = 2000L
        const val DOT_ANIMATION_DELAY = 333L
        const val INITIAL_SCREEN_ANIMATION_CYCLES = 4
        const val INITIAL_SCREEN_CYCLE_STEPS = 8
        const val INITIAL_SCREEN_STEP_DELAY = 125L
        const val DOT_PHASES_COUNT = 3
    }
}