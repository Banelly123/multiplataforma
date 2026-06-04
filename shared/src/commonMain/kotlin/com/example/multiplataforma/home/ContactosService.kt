package com.example.multiplataforma.home

import kotlinx.coroutines.delay

class ContactosService {

    // consumo de API (End-Point) para tu rama de desarrollo
    suspend fun enviarContactoAlServidor(contacto: ContactoSeguro): Boolean {
        println("🌐 [API_CONTACTOS] Iniciando petición POST a https://api.aliadas.com/v1/contactos")
        println("📦 [API_CONTACTOS] Payload JSON: {\"nombre\": \"${contacto.nombre}\", \"telefono\": \"${contacto.numero}\"}")

        // Simulamos el tiempo de carga del internet
        delay(1000)

        // Simulamos que el servidor nos respondió con éxito
        println("✅ [API_CONTACTOS] Respuesta 200 OK: Contacto sincronizado en el Backend.")
        return true
    }
}