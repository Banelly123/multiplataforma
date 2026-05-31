package com.example.multiplataforma.core

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import com.example.multiplataforma.auth.LoginScreen
import com.example.multiplataforma.auth.SessionManager
import com.example.multiplataforma.home.MapScreen

@Composable
fun App() {
    MaterialTheme {
        val sessionManager = remember { SessionManager() }
        var mostrarMapa by remember { mutableStateOf<Boolean>(sessionManager.haySessionActiva()) }

        if (mostrarMapa) {
            MapScreen()
        } else {
            LoginScreen(
                sessionManager = sessionManager,
                onLoginExitoso = {
                    mostrarMapa = true
                }
            )
        }
    }
}