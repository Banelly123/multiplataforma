package com.example.multiplataforma.home

import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

// 1. Configuramos nuestro "Navegador" interno para que entienda formato JSON
val clienteHttp = HttpClient {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        })
    }
}

// 2. Función que hará el POST a la API (Marcada como 'suspend' porque tarda un poco usando internet)
suspend fun enviarAlertaSOS(): Boolean {
    return try {
        // Aquí simulamos enviar la alerta con coordenadas reales aproximadas para nuestras pruebas
        val latitud = 20.0817
        val longitud = -98.3625

        // NOTA: Esta es una URL de prueba genérica. Luego la cambiaremos por la URL real de su base de datos.
        val respuesta: HttpResponse = clienteHttp.post("https://jsonplaceholder.typicode.com/posts") {
            contentType(ContentType.Application.Json)
            setBody("""{
                "alerta": "SOS",
                "latitud": $latitud,
                "longitud": $longitud,
                "estado": "Critico"
            }""".trimIndent())
        }

        // Si el servidor responde con código 200 o 201, significa que llegó el mensaje
        respuesta.status.isSuccess()
    } catch (e: Exception) {
        println("Hubo un error de conexión: ${e.message}")
        false
    }
}
