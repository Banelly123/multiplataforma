package com.example.multiplataforma.auth

import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class RegistroRequestDto(val nombre: String, val correo: String, val contrasenia: String)

@Serializable
data class LoginRequestDto(val correo: String, val contrasenia: String)

object AuthService {

    // 1. Configuramos el cliente para que hable en JSON
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    // 2. FUNCIÓN REAL DE REGISTRO
    suspend fun enviarRegistroAlServidor(nombre: String, correo: String, contrasenia: String): Boolean {
        return try {
            val peticion = RegistroRequestDto(nombre, correo, contrasenia)

            val response = client.post("http://10.0.2.2:8080/registro") {
                contentType(ContentType.Application.Json)
                setBody(peticion)
            }

            println("✅ [RED] Registro respondió con código: ${response.status}")
            response.status.value in 200..299
        } catch (e: Exception) {
            println("❌ [RED] Error crítico en registro: ${e.message}")
            false
        }
    }

    // 3. NUEVA FUNCIÓN REAL DE LOGIN
    suspend fun iniciarSesion(correo: String, contrasenia: String): Boolean {
        return try {
            val peticion = LoginRequestDto(correo, contrasenia)

            val response = client.post("http://10.0.2.2:8080/login") {
                contentType(ContentType.Application.Json)
                setBody(peticion)
            }

            println("✅ [RED] Login respondió con código: ${response.status}")
            response.status.value in 200..299
        } catch (e: Exception) {
            println("❌ [RED] Error crítico en login: ${e.message}")
            false
        }
    }
}