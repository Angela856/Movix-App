package com.movix.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun IndicadorStock(
    stockActual: Int,
    stockMinimo: Int
) {
    // Definimos los textos y colores según el nivel de stock
    val (texto, colorFondo, colorTexto) = when {
        stockActual == 0 -> Triple("Stock Agotado", Color(0xFF212121), Color.White) // Estilo urbano para agotado
        stockActual <= stockMinimo -> Triple("Stock Bajo!", Color(0xFFE50914), Color.White) // Rojo alerta
        else -> Triple("Stock Normal", Color(0xFFE8F5E9), Color(0xFF2E7D32)) // Verde normal
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(colorFondo)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = texto,
            color = colorTexto,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}