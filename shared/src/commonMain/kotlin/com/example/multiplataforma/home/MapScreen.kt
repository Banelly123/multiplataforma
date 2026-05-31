package com.example.multiplataforma.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import kotlinx.coroutines.launch

@Composable
fun MapScreen() {
    val coroutineScope = rememberCoroutineScope()
    var mostrarDialogo by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { BarraSuperior() },
        bottomBar = { BarraNavegacion() }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 1. EL MAPA
            MapaFondo()

            // 2. TEXTOS SUPERIORES Y BANNER
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .align(Alignment.TopCenter)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Hola, estás segura aquí",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A1A)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(50))
                        .background(Color(0xFFE9D5FF))
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("📍", fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Compartiendo ubicación en tiempo real",
                        color = Color(0xFF581C87),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp
                    )
                }
            }

            // 3. BOTÓN SOS GIGANTE
            Button(
                onClick = {
                    coroutineScope.launch {
                        val exito = enviarAlertaSOS()
                        if (exito) mostrarDialogo = true
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 40.dp)
                    .size(130.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C3AED)),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 10.dp)
            ) {
                Text(
                    text = "SOS",
                    color = Color.White,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            // 4. DIÁLOGO DE ÉXITO EMERGENTE
            if (mostrarDialogo) {
                AlertDialog(
                    onDismissRequest = { mostrarDialogo = false },
                    title = { Text("¡Alerta Enviada!", fontWeight = FontWeight.Bold) },
                    text = { Text("Tus contactos de confianza han recibido tu ubicación y estado de conexión.") },
                    confirmButton = {
                        TextButton(onClick = { mostrarDialogo = false }) {
                            Text("Entendido", color = Color(0xFF7C3AED), fontWeight = FontWeight.Bold)
                        }
                    },
                    containerColor = Color.White
                )
            }
        }
    }
}

// =========================================================================
// COMPONENTES SEPARADOS
// =========================================================================

@Composable
fun BarraSuperior() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            // ESTA LÍNEA ES LA MAGIA QUE EVITA QUE CHOCQUE CON EL RELOJ/CÁMARA:
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Lado Izquierdo: Menú hamburguesa y Título
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "☰",
                fontSize = 28.sp,
                color = Color(0xFF581C87),
                modifier = Modifier.padding(end = 16.dp)
            )
            Text(
                text = "Aliadas",
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF7C3AED)
            )
        }

        // Lado Derecho: Foto de perfil con el contorno morado
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(0xFFE9D5FF))
                // ESTA LÍNEA ES LA MAGIA QUE PONE EL CONTORNO:
                .border(width = 2.dp, color = Color(0xFF7C3AED), shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("👤", fontSize = 20.sp)
        }
    }
}

@Composable
fun MapaFondo() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3E8FF))
    )
}

@Composable
fun BarraNavegacion() {
    NavigationBar(
        containerColor = Color.White,
        contentColor = Color(0xFF7C3AED)
    ) {
        NavigationBarItem(
            selected = true,
            onClick = { },
            icon = { Text("📍", fontSize = 24.sp) },
            label = { Text("Mapa") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF7C3AED),
                indicatorColor = Color(0xFFEDE9FE),
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray
            )
        )
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Text("👥", fontSize = 24.sp) },
            label = { Text("Contactos") },
            colors = NavigationBarItemDefaults.colors(unselectedIconColor = Color.Gray, unselectedTextColor = Color.Gray)
        )
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Text("🗺️", fontSize = 24.sp) },
            label = { Text("Rutas") },
            colors = NavigationBarItemDefaults.colors(unselectedIconColor = Color.Gray, unselectedTextColor = Color.Gray)
        )
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Text("⚙️", fontSize = 24.sp) },
            label = { Text("Ajustes") },
            colors = NavigationBarItemDefaults.colors(unselectedIconColor = Color.Gray, unselectedTextColor = Color.Gray)
        )
    }
}