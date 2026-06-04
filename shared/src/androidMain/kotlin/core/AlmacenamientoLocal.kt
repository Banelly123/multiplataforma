package com.example.multiplataforma.core

import java.io.File

// La palabra 'actual' conecta este archivo con la plantilla que hiciste en commonMain
actual class AlmacenamientoLocal actual constructor() {

    // Creamos un archivo real en la memoria interna del dispositivo
    private val archivo = File(System.getProperty("java.io.tmpdir"), "contactos_aliadas.txt")

    actual fun guardarContactos(contactos: String) {
        // Escribe el texto en el archivo y lo guarda permanentemente
        archivo.writeText(contactos)
        println("💾 [ANDROID_NATIVO] Datos escritos físicamente en el disco: ${archivo.absolutePath}")
    }

    actual fun leerContactos(): String {
        // Si el archivo existe, lo lee y lo devuelve. Si no, devuelve texto vacío.
        return if (archivo.exists()) {
            archivo.readText()
        } else {
            ""
        }
    }
}

