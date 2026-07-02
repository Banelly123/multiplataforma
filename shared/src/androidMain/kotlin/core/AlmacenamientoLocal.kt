package com.example.multiplataforma.core

import java.io.File

actual class AlmacenamientoLocal actual constructor() {

    // Archivo físico para recordar al usuario
    private val archivoSesion = File(System.getProperty("java.io.tmpdir"), "sesion_aliadas.txt")

    // ¡MAGIA! Crea un archivo de contactos con el nombre del correo actual
    private fun obtenerArchivoContactos(): File {
        val email = if (archivoSesion.exists()) archivoSesion.readText().trim() else "invitado"
        return File(System.getProperty("java.io.tmpdir"), "contactos_$email.txt")
    }

    actual fun guardarContactos(contactos: String) {
        obtenerArchivoContactos().writeText(contactos)
        println("✅ [ANDROID_NATIVO] Datos de contactos escritos en el disco para el usuario actual.")
    }

    actual fun leerContactos(): String {
        val archivo = obtenerArchivoContactos()
        return if (archivo.exists()) archivo.readText() else ""
    }

    // ==========================================
    // FUNCIONES FÍSICAS DE SESIÓN
    // ==========================================
    actual fun guardarEmailSesion(email: String) {
        archivoSesion.writeText(email)
        println("✅ [ANDROID_NATIVO] Sesión guardada en el disco para: $email")
    }

    actual fun leerEmailSesion(): String {
        return if (archivoSesion.exists()) archivoSesion.readText() else ""
    }

    actual fun borrarEmailSesion() {
        if (archivoSesion.exists()) {
            archivoSesion.delete()
            println("🚪 [ANDROID_NATIVO] Sesión borrada del disco")
        }
    }
}