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
// PALETA DE COLORES PREMIUM
// ==========================================
private val MoradoProfundo = Color(0xFF4A148C)
private val LilaSuave = Color(0xFFE1BEE7)
private val AcentoPrincipal = Color(0xFF7C3AED)
private val TextoOscuro = Color(0xFF1F2937)
private val TextoGris = Color(0xFF6B7280)

@Composable
fun RegisterScreen(
    onRegistroExitoso: () -> Unit,
    onLoginClick: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var verPassword by remember { mutableStateOf(false) }
    var mensajeError by remember { mutableStateOf("") }
    var cargando by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    // FONDO INMERSIVO
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(MoradoProfundo, LilaSuave)
                )
            )
    ) {
        // ENCABEZADO (Logo y Textos)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // EL MISMO LOGO DEL LOGIN
            Image(
                painter = painterResource(Res.drawable.login), // Usa la misma imagen
                contentDescription = "Logo Oficial Aliadas",
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(26.dp))
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Crea tu cuenta",
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                letterSpacing = 1.sp
            )
            Text(
                text = "Únete a la red segura de confianza.",
                fontSize = 15.sp,
                color = Color.White.copy(alpha = 0.9f)
            )
        }

        // TARJETA BLANCA INFERIOR
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)),
            color = Color.White,
        ) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 32.dp, vertical = 32.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // --- CAMPO NOMBRE ---
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre completo") },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AcentoPrincipal,
                        unfocusedBorderColor = Color(0xFFE5E7EB),
                        focusedContainerColor = Color(0xFFF9FAFB),
                        unfocusedContainerColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

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

                Spacer(modifier = Modifier.height(16.dp))

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

                if (mensajeError.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = mensajeError, color = Color.Red, fontSize = 13.sp, modifier = Modifier.fillMaxWidth())
                }

                Spacer(modifier = Modifier.height(24.dp))

                // --- BOTÓN PRINCIPAL: REGISTRARSE ---
                Button(
                    onClick = {
                        if (nombre.isBlank() || email.isBlank() || password.isBlank()) {
                            mensajeError = "Por favor, llena todos los campos."
                        } else {
                            cargando = true
                            // Aquí se simula o conecta con tu AuthService
                            coroutineScope.launch {
                                // val exito = AuthService.registrarUsuario(nombre, email, password)
                                // if (exito) onRegistroExitoso() else mensajeError = "Error al registrar"
                                cargando = false
                                onRegistroExitoso() // <- Quitar esto cuando uses tu AuthService real
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AcentoPrincipal)
                ) {
                    if (cargando) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                    } else {
                        Text("Crear cuenta", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // --- SEPARADOR VISUAL ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Divider(modifier = Modifier.weight(1f), color = Color(0xFFE5E7EB))
                    Text(" o regístrate con ", color = TextoGris, fontSize = 14.sp, modifier = Modifier.padding(horizontal = 8.dp))
                    Divider(modifier = Modifier.weight(1f), color = Color(0xFFE5E7EB))
                }

                Spacer(modifier = Modifier.height(24.dp))

                // --- BOTÓN DE GOOGLE ---
                OutlinedButton(
                    onClick = { /* Pendiente de integrar Firebase Auth */ },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TextoOscuro)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Letra G colorida de Google
                        Text("G", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFDB4437))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Continuar con Google", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // --- REDIRECCIÓN A LOGIN ---
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("¿Ya tienes una cuenta?", fontSize = 14.sp, color = TextoGris)
                    TextButton(onClick = { onLoginClick() }) {
                        Text("Inicia Sesión", fontSize = 14.sp, color = AcentoPrincipal, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}