package com.example.multiplataforma.core

// La palabra 'expect' es la magia de Kotlin Multiplatform.
expect class AlmacenamientoLocal() {
    // Funciones de contactos (las que ya tenías)
    fun guardarContactos(contactos: String)
    fun leerContactos(): String

    // NUEVAS FUNCIONES PARA LA SESIÓN
    fun guardarEmailSesion(email: String)
    fun leerEmailSesion(): String
    fun borrarEmailSesion()
}