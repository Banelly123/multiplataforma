package com.example.multiplataforma.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
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
import com.example.multiplataforma.auth.SessionManager // ¡NUEVA IMPORTACIÓN!

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BotonPerfilUsuario(onCerrarSesion: () -> Unit) {
    var mostrarMenuPerfil by remember { mutableStateOf(false) }

    // ==========================================
    // LEEMOS EL CORREO REAL DEL CELULAR
    // ==========================================
    val sessionManager = remember { SessionManager() }
    val correoUsuario = sessionManager.obtenerEmail() ?: "usuario@desconocido.com"

    // Variables reactivas (Inician con datos temporales y se actualizan al descargar)
    var nombreUsuario by remember { mutableStateOf("Cargando...") }
    val avataresDisponibles = listOf("🦊", "🐼", "🦁", "🐯", "🐸")
    var avatarSeleccionado by remember { mutableStateOf("🦊") }

    val coroutineScope = rememberCoroutineScope()
    val contactosService = remember { ContactosService() }

    // ¡MAGIA! Descarga los datos reales de Railway apenas se dibuja el botón
    LaunchedEffect(correoUsuario) {
        val perfilReal = contactosService.obtenerPerfil(correoUsuario)
        if (perfilReal != null) {
            nombreUsuario = perfilReal.nombre
            avatarSeleccionado = perfilReal.avatar
        }
    }

    // Botón circular en la parte superior derecha de la pantalla
    Surface(
        onClick = { mostrarMenuPerfil = true },
        modifier = Modifier.size(42.dp),
        shape = CircleShape,
        color = Color(0xFFF3E8FF),
        border = BorderStroke(2.dp, Color(0xFF7C3AED))
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(text = avatarSeleccionado, fontSize = 24.sp)
        }
    }

    // Menú desplegable desde abajo
    if (mostrarMenuPerfil) {
        ModalBottomSheet(
            onDismissRequest = { mostrarMenuPerfil = false },
            containerColor = Color.White,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Tu Perfil Anónimo", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF7C3AED))
                Spacer(modifier = Modifier.height(16.dp))

                // Avatar Seleccionado en Grande
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF3E8FF)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = avatarSeleccionado, fontSize = 40.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ¡Aquí mostramos los datos reales!
                Text(nombreUsuario, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1A1A))
                Text(correoUsuario, fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(24.dp))

                // ==========================================
                // CARRUSEL PARA ELEGIR EL AVATAR
                // ==========================================
                Text("Cambiar Avatar:", fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = Color.DarkGray)
                Spacer(modifier = Modifier.height(12.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(avataresDisponibles) { avatar ->
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(if (avatar == avatarSeleccionado) Color(0xFF9F7AEA) else Color(0xFFE2E8F0))
                                .clickable {
                                    // 1. Cambiamos la carita en la pantalla
                                    avatarSeleccionado = avatar

                                    // 2. Mandamos la carita al servidor de fondo
                                    coroutineScope.launch {
                                        contactosService.actualizarAvatarEnServidor(correoUsuario, avatar)
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = avatar, fontSize = 24.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // ==========================================
                // BOTÓN DE CERRAR SESIÓN
                // ==========================================
                Button(
                    onClick = {
                        mostrarMenuPerfil = false
                        onCerrarSesion()
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(25.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFF0F2), // Fondo rosa pastel suave
                        contentColor = Color(0xFFE11D48)    // Texto color cereza oscuro/elegante
                    ),
                    elevation = ButtonDefaults.buttonElevation(0.dp)
                ) {
                    Text("Cerrar Sesión", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }

                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}