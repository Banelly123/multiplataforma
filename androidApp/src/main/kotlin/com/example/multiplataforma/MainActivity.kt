package com.example.multiplataforma

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.MaterialTheme
import com.example.multiplataforma.auth.LoginScreen
import com.example.multiplataforma.auth.SessionManager
import com.example.multiplataforma.home.MapScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val sessionManager = remember { SessionManager() }
                var mostrarMapa by remember { mutableStateOf(sessionManager.haySessionActiva()) }
                if (mostrarMapa) {
                    MapScreen()
                } else {
                    LoginScreen(
                        sessionManager = sessionManager,
                        onLoginExitoso = { mostrarMapa = true }
                    )
                }
            }
        }
    }
}
