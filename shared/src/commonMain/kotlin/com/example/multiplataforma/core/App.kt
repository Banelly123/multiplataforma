package com.example.multiplataforma.core

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import com.example.multiplataforma.auth.LoginScreen
import com.example.multiplataforma.auth.RegisterScreen
import com.example.multiplataforma.auth.SessionManager
import com.example.multiplataforma.home.MapScreen

// Definimos los tres estados posibles de navegación en tu app
private enum class Pantalla {
    LOGIN,
    REGISTRO,
    MAPA
}

@Composable
fun App() {
    MaterialTheme {
        val sessionManager = remember { SessionManager() }

        // Decidimos la pantalla inicial de forma local en base a si hay sesión activa
        var pantallaActual by remember {
            mutableStateOf(
                if (sessionManager.haySessionActiva()) Pantalla.MAPA else Pantalla.LOGIN
            )
        }

        // Control de navegación mediante estructura condicional
        when (pantallaActual) {
            Pantalla.MAPA -> {
                MapScreen(
                    onCerrarSesion = {
                        sessionManager.cerrarSesion() // Borra el archivo físico
                        pantallaActual = Pantalla.LOGIN // Te regresa al inicio
                    }
                )
            }

            Pantalla.LOGIN -> {
                LoginScreen(
                    sessionManager = sessionManager,
                    onLoginExitoso = {
                        pantallaActual = Pantalla.MAPA
                    },
                    onRegistroClick = {
                        pantallaActual = Pantalla.REGISTRO // 👈 ¡Esto cambia la pantalla dinámicamente!
                    }
                )
            }

            Pantalla.REGISTRO -> {
                // TU PANTALLA DE EXAMEN TOTALMENTE INTEGRADA
                RegisterScreen(
                    sessionManager = sessionManager,
                    onRegistroExitoso = {
                        // Cumple Fase 8: Cambia la UI de forma local al mapa tras salvar sesión
                        pantallaActual = Pantalla.MAPA
                    },
                    onIrALogin = {
                        pantallaActual = Pantalla.LOGIN // Permite regresar al Login si ya tiene cuenta
                    }
                )
            }
        }
    }
}