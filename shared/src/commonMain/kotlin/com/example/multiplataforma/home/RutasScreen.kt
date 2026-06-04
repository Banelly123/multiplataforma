package com.example.multiplataforma.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun RutasScreen(paddingValues: PaddingValues) {
    Box(
        modifier = Modifier.fillMaxSize().padding(paddingValues),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "🗺️ Módulo de Rutas Seguras\n(En construcción)",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF581C87),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}