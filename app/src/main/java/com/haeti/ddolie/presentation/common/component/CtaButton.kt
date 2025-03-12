package com.haeti.ddolie.presentation.common.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.Text
import com.haeti.ddolie.presentation.common.util.toTextDp
import com.haeti.ddolie.presentation.theme.DdoLieTheme

@Composable
fun CtaButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    chipColor: Color = DdoLieTheme.colors.redPrimary,
    contentColor: Color = DdoLieTheme.colors.white,
) {
    val lastClickTime = remember { mutableLongStateOf(0L) }

    val throttledClick: () -> Unit = {
        val currentTime: Long = System.currentTimeMillis()
        if (currentTime - lastClickTime.longValue >= 500L) {
            onClick()
            lastClickTime.longValue = currentTime
        }
    }

    Chip(
        onClick = throttledClick,
        colors = ChipDefaults.primaryChipColors(
            backgroundColor = chipColor,
            contentColor = contentColor,
        ),
        label = {
            Text(
                text = text,
                textAlign = TextAlign.Center,
                fontSize = 18.toTextDp,
                fontWeight = FontWeight.SemiBold,
            )
        },
        contentPadding = PaddingValues(horizontal = 25.dp, vertical = 10.dp),
        shape = RoundedCornerShape(360.dp),
    )
}

@Preview
@Composable
fun CtaButtonPreview() {
    CtaButton(text = "시작하기", onClick = {})
}