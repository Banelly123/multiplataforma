package com.example.multiplataforma.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun MapaFondo() {
    // Aquí es donde conectarás la API nativa de Google Maps.

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3E8FF))
    )
}