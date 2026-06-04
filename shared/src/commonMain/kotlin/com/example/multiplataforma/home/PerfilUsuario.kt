package com.example.multiplataforma.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
// Importamos tu nuevo lanzador nativo
import com.example.multiplataforma.core.LanzadorDeGaleria

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BotonPerfilUsuario() {
    var mostrarMenuPerfil by remember { mutableStateOf(false) }

    val nombreUsuario = "Banelly Sánchez Soto"
    val correoUsuario = "banellys08@gmail.com"
    val inicial = nombreUsuario.firstOrNull()?.uppercase() ?: "A"

    var tieneFotoSubida by remember { mutableStateOf(false) }

    // ==========================================
    // ¡AQUÍ INICIAMOS EL LANZADOR DE GALERÍA!
    // ==========================================
    val abrirGaleria = LanzadorDeGaleria { uriSeleccionada ->
        // Esto pasa cuando regresas de la galería habiendo elegido una foto
        println("✅ [APP_LOCAL] Ruta de la foto: $uriSeleccionada")
        tieneFotoSubida = true
    }

    Surface(
        onClick = { mostrarMenuPerfil = true },
        modifier = Modifier.size(42.dp),
        shape = CircleShape,
        color = if (tieneFotoSubida) Color(0xFF7C3AED) else Color(0xFFE9D5FF),
        border = BorderStroke(2.dp, Color(0xFF7C3AED))
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = inicial,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = if (tieneFotoSubida) Color.White else Color(0xFF581C87)
            )
        }
    }

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
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(if (tieneFotoSubida) Color(0xFF7C3AED) else Color(0xFFE9D5FF)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = inicial,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (tieneFotoSubida) Color.White else Color(0xFF581C87)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(nombreUsuario, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1A1A))
                Text(correoUsuario, fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        // ¡AL PICAR EL BOTÓN, SE ABRE EL CELULAR!
                        abrirGaleria()
                        mostrarMenuPerfil = false
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C3AED))
                ) {
                    Text("Subir Foto de Perfil", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = {
                        println("🚪 [APP_LOCAL] Cierre de sesión simulado.")
                        mostrarMenuPerfil = false
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color.Red)
                ) {
                    Text("Cerrar Sesión", color = Color.Red, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}