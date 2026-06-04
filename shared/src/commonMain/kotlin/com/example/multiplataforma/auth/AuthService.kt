package com.example.multiplataforma.auth

import kotlinx.coroutines.delay
import kotlinx.serialization.Serializable

@Serializable
data class RegistroRequestDto(
    val nombre: String,
    val correo: String,
    val contrasenia: String
)

object AuthService {

    // Función asíncrona que invoca tu pantalla al presionar el botón de "Registrarse"
    suspend fun enviarRegistroAlServidor(nombre: String, correo: String, contrasenia: String): Boolean {
        return try {
            // Construcción y simulación del envío de la solicitud mediante Ktor
            val peticionJson = RegistroRequestDto(nombre, correo, contrasenia)

            println("📡 [KTOR CLIENT] Enviando al servidor local -> Usuario: ${peticionJson.nombre} (${peticionJson.correo})")

            // Esto permite que el CircularProgressIndicator de tu pantalla gire en tiempo real
            delay(1500)

            // Retorna un código simulado equivalente a un HttpStatusCode.Created (201)
            val codigoRespuestaSimulado = 201

            // Retorna 'true' si el código es 201, validando el éxito en el cliente
            codigoRespuestaSimulado == 201

        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}