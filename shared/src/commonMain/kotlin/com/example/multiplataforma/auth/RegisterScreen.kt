package com.example.multiplataforma.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

private val MoradoPrimario    = Color(0xFF7C3AED)   // violeta principal
private val MoradoClaro       = Color(0xFFEDE9FE)   // fondo lavanda
private val TextoOscuro       = Color(0xFF1A1A2E)   // texto principal
private val TextoGris         = Color(0xFF6B7280)   // texto secundario
private val FondoCampo        = Color(0xFFFFFFFF)   // fondo de los campos

@Composable
fun RegisterScreen(
    sessionManager: SessionManager,
    onRegistroExitoso: () -> Unit,
    onIrALogin: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var verPassword by remember { mutableStateOf(false) }
    var mensajeError by remember { mutableStateOf("") }
    var cargando by remember { mutableStateOf(false) }

    // Ámbito de corrutina necesario para ejecutar la función suspendida de Ktor
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MoradoClaro)
            .padding(horizontal = 28.dp)
            .padding(top = 48.dp, bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Logo provisional coherente con Aliadas
        Text(
            text = "◆",
            fontSize = 56.sp,
            color = MoradoPrimario
        )

        Text(
            text = "Aliadas",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MoradoPrimario
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Crea tu cuenta",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = TextoOscuro,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Únete a la red segura de confianza.",
            fontSize = 14.sp,
            color = TextoGris,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // --- CAMPO NOMBRE ---
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Nombre completo", fontSize = 14.sp, color = TextoOscuro, fontWeight = FontWeight.Medium)
        }
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            placeholder = { Text("Tu nombre", color = TextoGris) },
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MoradoPrimario,
                unfocusedBorderColor = Color(0xFFE0D9FF),
                focusedContainerColor = FondoCampo,
                unfocusedContainerColor = FondoCampo
            ),
            modifier = Modifier.fillMaxWidth().height(56.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // --- CAMPO CORREO ---
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Correo electrónico", fontSize = 14.sp, color = TextoOscuro, fontWeight = FontWeight.Medium)
        }
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text("tu@correo.com", color = TextoGris) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MoradoPrimario,
                unfocusedBorderColor = Color(0xFFE0D9FF),
                focusedContainerColor = FondoCampo,
                unfocusedContainerColor = FondoCampo
            ),
            modifier = Modifier.fillMaxWidth().height(56.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // --- CAMPO CONTRASEÑA ---
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Contraseña", fontSize = 14.sp, color = TextoOscuro, fontWeight = FontWeight.Medium)
        }
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text("••••••••", color = TextoGris) },
            singleLine = true,
            visualTransformation = if (verPassword) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { verPassword = !verPassword }) {
                    Text(text = if (verPassword) "🔓" else "🔒", fontSize = 18.sp)
                }
            },
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MoradoPrimario,
                unfocusedBorderColor = Color(0xFFE0D9FF),
                focusedContainerColor = FondoCampo,
                unfocusedContainerColor = FondoCampo
            ),
            modifier = Modifier.fillMaxWidth().height(56.dp)
        )

        // Mostrar mensajes de error dinámicos
        if (mensajeError.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = mensajeError, color = Color.Red, fontSize = 13.sp, modifier = Modifier.fillMaxWidth())
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- BOTÓN DE REGISTRARSE (INTEGRACIÓN) ---
        Button(
            onClick = {
                mensajeError = ""
                when {
                    nombre.isBlank() -> mensajeError = "Por favor ingresa tu nombre."
                    email.isBlank() -> mensajeError = "Por favor ingresa tu correo."
                    !email.contains("@") -> mensajeError = "El correo no tiene un formato válido."
                    password.isBlank() -> mensajeError = "Por favor ingresa una contraseña."
                    password.length < 6 -> mensajeError = "La contraseña debe tener al menos 6 caracteres."
                    else -> {
                        cargando = true

                        // Consumo asíncrono con Ktor Client
                        coroutineScope.launch {
                            val registroExitoso = AuthService.enviarRegistroAlServidor(nombre, email, password)
                            cargando = false

                            if (registroExitoso) {
                                // ALMACENAMIENTO LOCAL: Persistimos los datos mediante el administrador de sesiones
                                sessionManager.guardarSesion(email)
                                onRegistroExitoso() // Navegación automática a la vista principal del mapa
                            } else {
                                mensajeError = "Error al procesar el registro en el servicio de Ktor."
                            }
                        }
                    }
                }
            },
            enabled = !cargando,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(30.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MoradoPrimario,
                contentColor = Color.White
            )
        ) {
            if (cargando) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
            } else {
                Text(text = "Registrarse", fontSize = 17.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row {
            Text("¿Ya tienes una cuenta? ", fontSize = 14.sp, color = TextoGris)
            TextButton(
                onClick = { onIrALogin() },
                contentPadding = PaddingValues(0.dp)
            ) {
                Text("Inicia Sesión", fontSize = 14.sp, color = MoradoPrimario, fontWeight = FontWeight.Bold)
            }
        }
    }
}