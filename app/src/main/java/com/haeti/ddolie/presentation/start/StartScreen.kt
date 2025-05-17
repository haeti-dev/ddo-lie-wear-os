package com.haeti.ddolie.presentation.start

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.wear.compose.material.Text
import androidx.wear.compose.ui.tooling.preview.WearPreviewDevices
import com.haeti.ddolie.R
import com.haeti.ddolie.presentation.common.util.toTextDp
import com.haeti.ddolie.presentation.init.navigation.InitialRoute
import com.haeti.ddolie.presentation.theme.DdoLieTheme
import com.haeti.ddolie.presentation.theme.RedPrimary
import com.haeti.ddolie.presentation.theme.RedSecondary
import com.haeti.ddolie.presentation.theme.RedTertiary

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun StartScreen(
    navController: NavController,
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .clickable { navController.navigate(InitialRoute.Main) },
        contentAlignment = Alignment.Center
    ) {

        Box(
            modifier = Modifier.size(maxWidth),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.matchParentSize()) {
                val radiusPx = size.width / 2f
                val center = Offset(radiusPx, radiusPx)

                val gradientBrush = Brush.radialGradient(
                    colorStops = arrayOf(
                        0.00f to RedTertiary,
                        0.85f to RedTertiary,
                        1.00f to Color.Black
                    ),
                    center = center,
                    radius = radiusPx
                )
                drawCircle(brush = gradientBrush, radius = radiusPx)

                drawCircle(color = RedSecondary, radius = radiusPx * 0.95f)
                drawCircle(color = RedPrimary, radius = radiusPx * 0.75f)
            }


            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.img_logo),
                    contentDescription = "logo",
                    modifier = Modifier.size(width = 108.dp, height = 70.dp)
                )

                Spacer(Modifier.height(10.dp))

                Text(
                    text = "시작하려면 화면을\n터치해주세요",
                    fontSize = 15.toTextDp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 23.toTextDp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@WearPreviewDevices
@Preview(
    name = "40mm – 396×396px",
    device = "spec:width=396px,height=396px,dpi=330", // 40mm 모델 테스트
    uiMode = Configuration.UI_MODE_TYPE_WATCH,
    showSystemUi = false,
    showBackground = false
)
@Composable
fun PreviewStartScreen() {
    DdoLieTheme {
        StartScreen(navController = rememberNavController())
    }
}
