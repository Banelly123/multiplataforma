package com.example.multiplataforma.core

// La palabra 'expect' es la magia de Kotlin Multiplatform.
// Define la "plantilla" que Android (y en el futuro iOS) están obligados a construir.
expect class AlmacenamientoLocal() {

    // Función para guardar nuestra lista de contactos (la guardaremos como un texto/String)
    fun guardarContactos(contactos: String)

    // Función para recuperar esa lista cuando la app se vuelva a abrir
    fun leerContactos(): String
}
