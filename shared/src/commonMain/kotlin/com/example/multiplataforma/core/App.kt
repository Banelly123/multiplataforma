package com.example.multiplataforma.core

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
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

        // ANIMACIÓN DE TRANSICIÓN PREMIUM
        AnimatedContent(
            targetState = pantallaActual,
            transitionSpec = {
                // Configuración de la animación:
                // Tarda 600ms, entra desde abajo y se desvanece suavemente
                (fadeIn(animationSpec = tween(600)) + slideInVertically(
                    animationSpec = tween(600),
                    initialOffsetY = { altoTotal -> altoTotal / 6 }
                )).togetherWith(
                    fadeOut(animationSpec = tween(400))
                )
            },
            label = "AnimacionDePantallas"
        ) { pantalla ->
            // Control de navegación dentro de la animación
            when (pantalla) {
                Pantalla.MAPA -> {
                    MapScreen(
                        onCerrarSesion = {
                            sessionManager.cerrarSesion()
                            pantallaActual = Pantalla.LOGIN
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
                            pantallaActual = Pantalla.REGISTRO
                        }
                    )
                }

                Pantalla.REGISTRO -> {
                    // TU PANTALLA DE EXAMEN TOTALMENTE INTEGRADA
                    RegisterScreen(
                        onRegistroExitoso = {
                            pantallaActual = Pantalla.MAPA
                        },
                        onLoginClick = {
                            pantallaActual = Pantalla.LOGIN
                        }
                    )
                }
            }
        }
    }
}