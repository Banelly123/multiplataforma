package com.example.multiplataforma.core

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.example.multiplataforma.auth.LoginScreen
import com.example.multiplataforma.auth.SessionManager

@Composable
actual fun App() {
    MaterialTheme {
        LoginScreen(
            sessionManager = SessionManager(),
            onLoginExitoso = {}
        )
    }
}
