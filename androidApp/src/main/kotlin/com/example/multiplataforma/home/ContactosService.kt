package com.example.multiplataforma.home

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

// 1. EL MOLDE DE DATOS PARA LA COMUNIDAD
@Serializable
data class MensajeComunidad(
    val contenido: String,
    val autorEmail: String,
    val avatarAutor: String
)

@Serializable
data class PerfilResponse(val nombre: String, val avatar: String)
class ContactosService {

    // 2. CONFIGURAMOS EL CLIENTE PARA QUE TRADUZCA EL JSON AUTOMÁTICAMENTE
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    // ==========================================
    // FUNCIONES DE CONTACTOS Y AVATAR (Lo que ya tenías funcionando)
    // ==========================================

    suspend fun enviarContactoAlServidor(contacto: ContactoSeguro, emailUsuario: String): Boolean {
        return try {
            val jsonBody = """
            {
                "nombre": "${contacto.nombre}",
                "numero": "${contacto.numero}",
                "parentesco": "${contacto.parentesco}",
                "direccion": "${contacto.direccion}",
                "emailUsuario": "$emailUsuario"
            }
            """.trimIndent()

            val response = client.post("http://10.0.2.2:8080/contactos") {
                contentType(ContentType.Application.Json)
                setBody(jsonBody)
            }
            response.status.value in 200..299
        } catch (e: Exception) {
            false
        }
    }

    suspend fun obtenerPerfil(email: String): PerfilResponse? {
        return try {
            val response = client.get("http://10.0.2.2:8080/perfil/$email")
            if (response.status.value in 200..299) {
                response.body<PerfilResponse>() // Descarga el nombre y avatar real
            } else null
        } catch (e: Exception) {
            null
        }
    }

    suspend fun actualizarAvatarEnServidor(email: String, nuevoAvatar: String): Boolean {
        return try {
            val jsonBody = """
            {
                "email": "$email",
                "nuevoAvatar": "$nuevoAvatar"
            }
            """.trimIndent()

            val response = client.post("http://10.0.2.2:8080/actualizar-avatar") {
                contentType(ContentType.Application.Json)
                setBody(jsonBody)
            }
            println("✅ [RED] Actualización de Avatar respondió con código: ${response.status}")
            response.status.value in 200..299
        } catch (e: Exception) {
            println("❌ [RED] Error al enviar avatar: ${e.message}")
            false
        }
    }

    suspend fun eliminarContactoDelServidor(numero: String): Boolean {
        return try {
            val response = client.delete("http://10.0.2.2:8080/contactos/$numero")
            println("🗑️ [RED] El servidor respondió al borrar con el código: ${response.status}")
            response.status.value in 200..299
        } catch (e: Exception) {
            println("❌ [RED] Error crítico al intentar borrar: ${e.message}")
            false
        }
    }

    // ==========================================
    // NUEVAS FUNCIONES: COMUNIDAD Y FORO
    // ==========================================

    suspend fun obtenerMensajesComunidad(): List<MensajeComunidad> {
        return try {
            val response = client.get("http://10.0.2.2:8080/comunidad")
            if (response.status.value in 200..299) {
                // Descarga la lista completa de la base de datos
                response.body<List<MensajeComunidad>>()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            println("❌ [RED] Error al descargar comunidad: ${e.message}")
            emptyList()
        }
    }

    suspend fun publicarMensaje(mensaje: MensajeComunidad): Boolean {
        return try {
            val response = client.post("http://10.0.2.2:8080/comunidad") {
                contentType(ContentType.Application.Json)
                setBody(mensaje) // Ktor lo convierte a JSON automáticamente
            }
            response.status.value in 200..299
        } catch (e: Exception) {
            println("❌ [RED] Error al publicar: ${e.message}")
            false
        }
    }
}