package com.example.multiplataforma.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MapScreen() {
    // Contenedor principal (simulando el fondo del mapa por ahora con un color suave)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3E8FF))
    ) {
        // Sección Superior (Textos y Banner)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .align(Alignment.TopCenter)
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "Hola, estás segura aquí",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A1A)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Banner de ubicación
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

        // Botón SOS Gigante
        Button(
            onClick = {
                // Aquí irá el consumo de API para mandar la alerta a la base de datos
                println("¡Alerta enviada!")
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 120.dp)
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
    }
}




