package com.haeti.ddolie.presentation.common.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

val Int.toTextDp: TextUnit
    @Composable
    get() = with(LocalDensity.current) { this@toTextDp.dp.toSp() }