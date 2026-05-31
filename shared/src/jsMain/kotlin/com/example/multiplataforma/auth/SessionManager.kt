package com.example.multiplataforma.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
actual fun rememberSessionManager(): SessionManager = remember {
    object : SessionManager {
        override fun guardarSesion(email: String) {}
        override fun cerrarSesion() {}
        override fun haySessionActiva(): Boolean = false
        override fun obtenerEmail(): String? = null
    }
}
