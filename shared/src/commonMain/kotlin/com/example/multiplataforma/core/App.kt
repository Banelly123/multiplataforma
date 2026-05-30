package com.example.multiplataforma.core

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.example.multiplataforma.home.MapScreen

@Composable
fun App() {
    MaterialTheme {
        // Le decimos a la app que arranque directamente en tu pantalla SOS
        MapScreen()
    }
}