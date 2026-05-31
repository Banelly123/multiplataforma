package com.example.multiplataforma.auth

/**
 * SessionManager - El guardián de la sesión (Simulado para Multiplataforma)
 * Banelly - Módulo Auth
 */
class SessionManager {

    private var isLoggedIn = false
    private var userEmail: String? = null

    fun guardarSesion(email: String) {
        userEmail = email
        isLoggedIn = true
    }

    fun cerrarSesion() {
        userEmail = null
        isLoggedIn = false
    }

    fun haySessionActiva(): Boolean {
        return isLoggedIn
    }

    fun obtenerEmail(): String? {
        return userEmail
    }
}