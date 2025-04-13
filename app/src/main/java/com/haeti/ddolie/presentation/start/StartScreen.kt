package com.haeti.ddolie.presentation.start

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.wear.compose.material.Text
import androidx.wear.compose.ui.tooling.preview.WearPreviewDevices
import com.haeti.ddolie.R
import com.haeti.ddolie.presentation.init.navigation.InitialRoute
import com.haeti.ddolie.presentation.theme.DdoLieTheme

@Composable
fun StartScreen(
    navController: NavController,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable { navController.navigate(InitialRoute.Main) },
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            DdoLieTheme.colors.redTertiary,
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )

        Box(
            modifier = Modifier
                .size((172 + 46).dp)
                .background(
                    color = DdoLieTheme.colors.redSecondary,
                    shape = CircleShape
                )
        )

        Box(
            modifier = Modifier
                .size(172.dp)
                .background(
                    color = DdoLieTheme.colors.redPrimary,
                    shape = CircleShape,
                )
        )

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
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 23.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@WearPreviewDevices
@Composable
fun PreviewStartScreen() {
    DdoLieTheme {
        StartScreen(navController = rememberNavController())
    }
}
