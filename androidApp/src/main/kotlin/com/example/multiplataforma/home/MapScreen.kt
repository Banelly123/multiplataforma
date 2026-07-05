package com.example.multiplataforma.home

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
import kotlinx.coroutines.launch

@Composable
fun MapScreen(onCerrarSesion: () -> Unit) {
    // 1. LA VARIABLE QUE CONTROLA LA NAVEGACIÓN
    var pantallaActual by remember { mutableStateOf("Mapa") }

    Scaffold(
        topBar = { BarraSuperior(onCerrarSesion) },
        bottomBar = {
            // 2. LE PASAMOS EL CONTROL A LA BARRA
            BarraNavegacion(
                pantallaSeleccionada = pantallaActual,
                alSeleccionarPantalla = { nuevaPantalla -> pantallaActual = nuevaPantalla }
            )
        }
    ) { paddingValues ->

        // 3. LA MAGIA DEL CAMBIO DE PANTALLA
        when (pantallaActual) {
            "Mapa" -> ContenidoMapa(paddingValues)
            "Contactos" -> ContactosScreen(paddingValues) // Asegúrate de tener este archivo creado
            "Comunidad" -> ComunidadScreen(paddingValues)
            "Recursos" -> RecursosScreen(paddingValues)
        }
    }
}

// -----------------------------------------------------------
// Aislamos el contenido del mapa en su propia función
// -----------------------------------------------------------
@Composable
fun ContenidoMapa(paddingValues: PaddingValues) {
    val coroutineScope = rememberCoroutineScope()
    var mostrarDialogo by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
        MapaFondo()

        Column(modifier = Modifier.fillMaxWidth().padding(24.dp).align(Alignment.TopCenter)) {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Hola, estás segura aquí", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1A1A))
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(50)).background(Color(0xFFE9D5FF)).padding(horizontal = 16.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                Text("📍", fontSize = 20.sp)
                Spacer(modifier = Modifier.width(12.dp))
                Text("Compartiendo ubicación en tiempo real", color = Color(0xFF581C87), fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
            }
        }

        Button(
            onClick = {
                coroutineScope.launch {
                    val exito = enviarAlertaSOS()
                    if (exito) mostrarDialogo = true
                }
            },
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 40.dp).size(130.dp),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C3AED)),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 10.dp)
        ) {
            Text("SOS", color = Color.White, fontSize = 36.sp, fontWeight = FontWeight.ExtraBold)
        }

        if (mostrarDialogo) {
            AlertDialog(
                onDismissRequest = { mostrarDialogo = false },
                title = { Text("¡Alerta Enviada!", fontWeight = FontWeight.Bold) },
                text = { Text("Tus contactos de confianza han recibido tu ubicación.") },
                confirmButton = { TextButton(onClick = { mostrarDialogo = false }) { Text("Entendido", color = Color(0xFF7C3AED)) } },
                containerColor = Color.White
            )
        }
    }
}

// -----------------------------------------------------------
// BARRAS
// -----------------------------------------------------------
@Composable
fun BarraSuperior(onCerrarSesion: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().background(Color.White).statusBarsPadding().padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("☰", fontSize = 28.sp, color = Color(0xFF581C87), modifier = Modifier.padding(end = 16.dp))
            Text("Aliadas", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF7C3AED))
        }
        BotonPerfilUsuario(onCerrarSesion) // Aquí le pasamos la acción final
    }
}

@Composable
fun BarraNavegacion(pantallaSeleccionada: String, alSeleccionarPantalla: (String) -> Unit) {
    NavigationBar(containerColor = Color.White, contentColor = Color(0xFF7C3AED)) {
        val colorSeleccionado = NavigationBarItemDefaults.colors(selectedIconColor = Color(0xFF7C3AED), indicatorColor = Color(0xFFEDE9FE), unselectedIconColor = Color.Gray, unselectedTextColor = Color.Gray)

        NavigationBarItem(
            selected = pantallaSeleccionada == "Mapa",
            onClick = { alSeleccionarPantalla("Mapa") },
            icon = { Text("📍", fontSize = 24.sp) },
            label = { Text("Mapa") },
            colors = colorSeleccionado
        )
        NavigationBarItem(
            selected = pantallaSeleccionada == "Contactos",
            onClick = { alSeleccionarPantalla("Contactos") },
            icon = { Text("👥", fontSize = 24.sp) },
            label = { Text("Contactos") },
            colors = colorSeleccionado
        )
        NavigationBarItem(
            selected = pantallaSeleccionada == "Comunidad",
            onClick = { alSeleccionarPantalla("Comunidad") },
            icon = { Text("💬", fontSize = 24.sp) },
            label = { Text("Comunidad") },
            colors = colorSeleccionado
        )
        NavigationBarItem(
            selected = pantallaSeleccionada == "Recursos",
            onClick = { alSeleccionarPantalla("Recursos") },
            icon = { Text("💡", fontSize = 24.sp) },
            label = { Text("Recursos") },
            colors = colorSeleccionado
        )
    }
}
