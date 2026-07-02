package com.example.multiplataforma.auth

import com.example.multiplataforma.core.AlmacenamientoLocal

class SessionManager {
    // Conectamos tu administrador con tu almacenamiento en disco
    private val almacenamiento = AlmacenamientoLocal()

    fun guardarSesion(email: String) {
        almacenamiento.guardarEmailSesion(email)
    }

    fun cerrarSesion() {
        almacenamiento.borrarEmailSesion()
    }

    fun haySessionActiva(): Boolean {
        // Si hay un correo guardado y no está vacío, significa que hay sesión activa
        return almacenamiento.leerEmailSesion().isNotBlank()
    }

    fun obtenerEmail(): String? {
        val email = almacenamiento.leerEmailSesion()
        return if (email.isNotBlank()) email else null
    }
}