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

private val MoradoPrimario    = Color(0xFF7C3AED)   // violeta principal
private val MoradoClaro       = Color(0xFFEDE9FE)   // fondo lavanda
private val TextoOscuro       = Color(0xFF1A1A2E)   // texto principal
private val TextoGris         = Color(0xFF6B7280)   // texto secundario
private val FondoCampo        = Color(0xFFFFFFFF)   // fondo de los campos

@Composable
fun LoginScreen(
    sessionManager: SessionManager,
    onLoginExitoso: () -> Unit
) {
    // "remember" + "mutableStateOf" hacen que la variable sea reactiva:
    // cuando cambia, la pantalla se redibuja automáticamente.
    var email by remember { mutableStateOf("") }  // campo email
    var password by remember { mutableStateOf("") }  // contraseña

    var verPassword by remember { mutableStateOf(false) }  // mostrar u ocultar contraseña

    var mensajeError by remember { mutableStateOf("") }  // texto de error (vacío = sin error)

    var cargando by remember { mutableStateOf(false) }  // true = mostrar spinner

    // Column apila sus hijos de arriba hacia abajo (como un LinearLayout vertical)
    Column(
        modifier = Modifier
            .fillMaxSize()                          // ocupa toda la pantalla
            .background(MoradoClaro)                // fondo lavanda
            .padding(horizontal = 28.dp)            // margen izquierdo y derecho
            .padding(top = 64.dp, bottom = 32.dp),  // margen arriba y abajo
        horizontalAlignment = Alignment.CenterHorizontally, // centra todo horizontalmente
        verticalArrangement = Arrangement.Top               // empieza desde arriba
    ) {

        // logo provicional
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

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = "Bienvenida de nuevo",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = TextoOscuro,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Accede a tu red segura de confianza.",
            fontSize = 14.sp,
            color = TextoGris,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Alineamos el label a la izquierda usando un Row que ocupa todo el ancho
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Correo electrónico",
                fontSize = 14.sp,
                color = TextoOscuro,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            value = email,                          // valor actual del campo
            onValueChange = { email = it },         // actualiza la variable cuando escribe
            placeholder = { Text("tu@correo.com", color = TextoGris) },
            singleLine = true,                      // no permite saltos de línea
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email   // muestra teclado de email (con @)
            ),
            shape = RoundedCornerShape(16.dp),      // bordes redondeados
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor   = MoradoPrimario,
                unfocusedBorderColor = Color(0xFFE0D9FF),
                focusedContainerColor   = FondoCampo,
                unfocusedContainerColor = FondoCampo
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Row acomoda sus hijos de izquierda a derecha
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween  // uno a cada extremo
        ) {
            Text("Contraseña", fontSize = 14.sp, color = TextoOscuro, fontWeight = FontWeight.Medium)
            TextButton(
                onClick = { },
                contentPadding = PaddingValues(0.dp)
            ) {
                Text("¿Olvidaste tu contraseña?", fontSize = 13.sp, color = MoradoPrimario)
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text("••••••••", color = TextoGris) },
            singleLine = true,
            // Aquí controlamos si se ve o no la contraseña
            visualTransformation = if (verPassword)
                VisualTransformation.None           // muestra el texto real
            else
                PasswordVisualTransformation(),     // muestra puntos •••

            // Ícono del ojo al final del campo
            trailingIcon = {
                IconButton(onClick = { verPassword = !verPassword }) {
                    Text(
                        text = if (verPassword) "🔓" else "🔒",
                        fontSize = 18.sp
                    )
                }
            },
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor      = MoradoPrimario,
                unfocusedBorderColor    = Color(0xFFE0D9FF),
                focusedContainerColor   = FondoCampo,
                unfocusedContainerColor = FondoCampo
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        )

        // Solo se muestra si mensajeError no está vacío
        if (mensajeError.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = mensajeError,
                color = Color.Red,
                fontSize = 13.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                mensajeError = ""

                // Validaciones de datos
                when {
                    email.isBlank() -> {
                        mensajeError = "Por favor ingresa tu correo."
                    }
                    !email.contains("@") -> {
                        mensajeError = "El correo no tiene un formato válido."
                    }
                    password.isBlank() -> {
                        mensajeError = "Por favor ingresa tu contraseña."
                    }
                    password.length < 6 -> {
                        mensajeError = "La contraseña debe tener al menos 6 caracteres."
                    }
                    else -> {
                        cargando = true

                        // Credenciales de prueba:
                        val emailPrueba    = "banelly@aliadas.com"
                        val passwordPrueba = "123456"

                        if (email == emailPrueba && password == passwordPrueba) {
                            sessionManager.guardarSesion(email)  // guardamos la sesión
                            cargando = false
                            onLoginExitoso()
                        } else {
                            // Credenciales incorrectas
                            cargando = false
                            mensajeError = "Correo o contraseña incorrectos."
                        }
                    }
                }
            },
            enabled = !cargando,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(30.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MoradoPrimario,
                contentColor   = Color.White
            )
        ) {
            if (cargando) {
                CircularProgressIndicator(
                    color  = Color.White,
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text     = "Iniciar Sesión",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFD1C4E9))
            Text(
                text     = "  o continua con  ",
                fontSize = 13.sp,
                color    = TextoGris
            )
            HorizontalDivider(modifier = Modifier.weight(1f), color = Color(0xFFD1C4E9))
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = { },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            border = ButtonDefaults.outlinedButtonBorder.copy(
            ),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = TextoOscuro)
        ) {
            Text("G", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFFEA4335))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Google", fontSize = 15.sp, color = TextoOscuro)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row {
            Text("¿No tienes una cuenta? ", fontSize = 14.sp, color = TextoGris)
            TextButton(
                onClick = { },
                contentPadding = PaddingValues(0.dp)
            ) {
                Text("Regístrate", fontSize = 14.sp, color = MoradoPrimario, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Al iniciar sesión, aceptas nuestros Términos y Política de Privacidad.",
            fontSize = 11.sp,
            color = TextoGris,
            textAlign = TextAlign.Center,
            lineHeight = 16.sp
        )
    }
}