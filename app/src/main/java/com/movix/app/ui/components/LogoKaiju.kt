package com.movix.app.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.movix.app.R

@Composable
fun LogoAnimadoKaiju() {
    val infiniteTransition = rememberInfiniteTransition(label = "transicion_logo")

    val scale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "escala_logo"
    )

    Image(
        painter = painterResource(id = R.drawable.logo_kaiju),
        contentDescription = "Logo Animado Kaiju",
        modifier = Modifier
            .size(120.dp)
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale
            )
    )
}