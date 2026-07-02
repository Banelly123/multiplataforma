package com.example.multiplataforma.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import multiplataforma.shared.generated.resources.Res
import multiplataforma.shared.generated.resources.login
import org.jetbrains.compose.resources.painterResource

// ==========================================
// PALETA DE COLORES PREMIUM Y EMPÁTICA
// ==========================================
private val MoradoProfundo = Color(0xFF4A148C) // Seguridad y fuerza
private val LilaSuave = Color(0xFFE1BEE7)      // Calidez y empatía
private val AcentoPrincipal = Color(0xFF7C3AED) // Botones interactivos
private val TextoOscuro = Color(0xFF1F2937)
private val TextoGris = Color(0xFF6B7280)

@Composable
fun LoginScreen(
    sessionManager: SessionManager,
    onLoginExitoso: () -> Unit,
    onRegistroClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var verPassword by remember { mutableStateOf(false) }
    var mensajeError by remember { mutableStateOf("") }
    var cargando by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    // 1. FONDO INMERSIVO (Estilo Instagram, colores seguros)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(MoradoProfundo, LilaSuave)
                )
            )
    ) {

        // 2. ENCABEZADO (Logo y Bienvenida flotante)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ¡AQUÍ ESTÁ TU LOGO OFICIAL!
            Image(
                painter = painterResource(Res.drawable.login),
                contentDescription = "Logo Oficial Aliadas",
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(26.dp)) // Recorta el fondo negro y lo deja curvo
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Aliadas",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                letterSpacing = 1.sp
            )
            Text(
                text = "Tu red, tu refugio.",
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.9f)
            )
        }

        // 3. TARJETA DE CONTENIDO (Estilo BBVA)
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)),
            color = Color.White,
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 32.dp, vertical = 40.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Bienvenida",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextoOscuro
                )
                Spacer(modifier = Modifier.height(32.dp))

                // --- CAMPO CORREO ---
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Correo electrónico") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AcentoPrincipal,
                        unfocusedBorderColor = Color(0xFFE5E7EB),
                        focusedContainerColor = Color(0xFFF9FAFB),
                        unfocusedContainerColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                // --- CAMPO CONTRASEÑA ---
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    singleLine = true,
                    visualTransformation = if (verPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { verPassword = !verPassword }) {
                            Text(text = if (verPassword) "🙈" else "👁️", fontSize = 18.sp)
                        }
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AcentoPrincipal,
                        unfocusedBorderColor = Color(0xFFE5E7EB),
                        focusedContainerColor = Color(0xFFF9FAFB),
                        unfocusedContainerColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                // ¿Olvidaste tu contraseña? (Alineado a la derecha)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = {}, contentPadding = PaddingValues(0.dp)) {
                        Text("¿Olvidaste tu contraseña?", fontSize = 13.sp, color = AcentoPrincipal)
                    }
                }

                if (mensajeError.isNotEmpty()) {
                    Text(text = mensajeError, color = Color.Red, fontSize = 13.sp, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(8.dp))
                } else {
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // --- BOTÓN DE INICIO DE SESIÓN ---
                Button(
                    onClick = {
                        mensajeError = ""
                        when {
                            email.isBlank() -> mensajeError = "Por favor ingresa tu correo."
                            password.isBlank() -> mensajeError = "Por favor ingresa tu contraseña."
                            else -> {
                                cargando = true
                                coroutineScope.launch {
                                    val loginExitoso = AuthService.iniciarSesion(email, password)
                                    cargando = false
                                    if (loginExitoso) {
                                        sessionManager.guardarSesion(email)
                                        onLoginExitoso()
                                    } else {
                                        mensajeError = "Correo o contraseña incorrectos."
                                    }
                                }
                            }
                        }
                    },
                    enabled = !cargando,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(20.dp), // Botón muy curvo y amigable
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AcentoPrincipal,
                        contentColor = Color.White
                    )
                ) {
                    if (cargando) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                    } else {
                        Text(text = "Entrar de forma segura", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // --- ÁREA DE REGISTRO ---
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("¿Es tu primera vez aquí?", fontSize = 14.sp, color = TextoGris)
                    TextButton(onClick = { onRegistroClick() }) {
                        Text("Crear cuenta", fontSize = 14.sp, color = AcentoPrincipal, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp)) // Espacio extra al fondo
            }
        }
    }
}