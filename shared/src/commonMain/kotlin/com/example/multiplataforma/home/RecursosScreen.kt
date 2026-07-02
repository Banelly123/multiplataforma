package com.example.multiplataforma.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RecursosScreen(paddingValues: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(paddingValues)
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Recursos de\nEmergencia",
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF1A1A1A),
            lineHeight = 32.sp
        )
        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            item {
                RecursoCard(titulo = "Línea Nacional", descripcion = "Marca 911 para emergencias inmediatas.", colorFondo = Color(0xFFFFE4E6), colorTexto = Color(0xFFE11D48))
            }
            item {
                RecursoCard(titulo = "Asesoría Legal", descripcion = "Conoce tus derechos e instituciones de protección.", colorFondo = Color(0xFFE0E7FF), colorTexto = Color(0xFF4338CA))
            }
            item {
                RecursoCard(titulo = "Apoyo Psicológico", descripcion = "Contacta a profesionales de la salud mental disponibles 24/7.", colorFondo = Color(0xFFDCFCE7), colorTexto = Color(0xFF15803D))
            }
        }
    }
}

@Composable
fun RecursoCard(titulo: String, descripcion: String, colorFondo: Color, colorTexto: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = colorFondo),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(titulo, fontWeight = FontWeight.Bold, color = colorTexto, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(descripcion, color = Color.DarkGray, fontSize = 14.sp)
        }
    }
}