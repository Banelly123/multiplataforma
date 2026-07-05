package com.example.multiplataforma.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComunidadScreen(paddingValues: PaddingValues) {
    val contactosService = remember { ContactosService() }
    val coroutineScope = rememberCoroutineScope()

    // Variables de estado
    var listaMensajes by remember { mutableStateOf(emptyList<MensajeComunidad>()) }
    var cargando by remember { mutableStateOf(true) }
    var nuevoMensaje by remember { mutableStateOf("") }

    // Datos temporales (En tu app completa, podrías leer esto desde SessionManager/Almacenamiento Local)
    val miEmail = "banellys08@gmail.com"
    val miAvatar = "🐼"

    // 1. DISPARADOR AUTOMÁTICO: Descarga los mensajes al entrar a la pantalla
    LaunchedEffect(Unit) {
        listaMensajes = contactosService.obtenerMensajesComunidad()
        cargando = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA)) // Fondo claro general
            .padding(paddingValues)
    ) {
        // --- CABECERA ---
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Comunidad y\nApoyo",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF1A1A1A),
                lineHeight = 32.sp
            )
            Text(
                text = "Un espacio seguro y anónimo para expresarte.",
                color = Color.Gray,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // --- ZONA DE MENSAJES (Foro Dinámico) ---
        Box(modifier = Modifier.weight(1f).padding(horizontal = 24.dp)) {
            if (cargando) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = Color(0xFF7C3AED))
            } else if (listaMensajes.isEmpty()) {
                Text(
                    "Sé la primera en escribir un mensaje de apoyo. 💜",
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Ponemos .reversed() para que los mensajes más nuevos aparezcan hasta abajo (o arriba, como prefieras)
                    items(listaMensajes) { mensaje ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Row(modifier = Modifier.padding(16.dp)) {
                                // Dibujamos el Avatar
                                Box(
                                    modifier = Modifier.size(40.dp).clip(CircleShape).background(Color(0xFFF3E8FF)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = mensaje.avatarAutor, fontSize = 20.sp)
                                }
                                Spacer(modifier = Modifier.width(12.dp))

                                // Textos (Anonimizados)
                                Column {
                                    Text("Anónimo", fontWeight = FontWeight.Bold, color = Color(0xFF7C3AED), fontSize = 15.sp)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(mensaje.contenido, color = Color(0xFF1A1A1A), fontSize = 14.sp)
                                }
                            }
                        }
                    }
                    item { Spacer(modifier = Modifier.height(16.dp)) }
                }
            }
        }

        // --- BARRA PARA ESCRIBIR UN NUEVO MENSAJE ---
        Surface(
            color = Color.White,
            shadowElevation = 8.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = nuevoMensaje,
                    onValueChange = { nuevoMensaje = it },
                    placeholder = { Text("Escribe un mensaje de apoyo...") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF7C3AED),
                        unfocusedBorderColor = Color.LightGray
                    ),
                    maxLines = 3
                )
                Spacer(modifier = Modifier.width(12.dp))

                // Botón Enviar
                FloatingActionButton(
                    onClick = {
                        if (nuevoMensaje.isNotBlank()) {
                            val mensajeAEnviar = MensajeComunidad(
                                contenido = nuevoMensaje,
                                autorEmail = miEmail,
                                avatarAutor = miAvatar
                            )
                            // Ejecutamos la subida en segundo plano
                            coroutineScope.launch {
                                val exito = contactosService.publicarMensaje(mensajeAEnviar)
                                if (exito) {
                                    // Si sube a Railway, lo agregamos a la pantalla localmente también
                                    listaMensajes = listaMensajes + mensajeAEnviar
                                    nuevoMensaje = "" // Limpiamos la caja de texto
                                }
                            }
                        }
                    },
                    containerColor = Color(0xFF7C3AED),
                    contentColor = Color.White,
                    shape = CircleShape,
                    modifier = Modifier.size(50.dp)
                ) {
                    Text("🚀", fontSize = 20.sp)
                }
            }
        }
    }
}