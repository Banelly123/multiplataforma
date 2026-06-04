package com.example.myapplicationuvu.auth

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ── Paleta Aliadas ──────────────────────────────────────────────
private val VioletaPrimario  = Color(0xFF7C3AED)
private val VioletaOscuro    = Color(0xFF5B21B6)
private val VioletaMedio     = Color(0xFF8B5CF6)
private val FondoLavanda     = Color(0xFFEDE9FE)
private val FondoBlanco      = Color(0xFFFFFFFF)
private val TextoPrincipal   = Color(0xFF1F1A2E)
private val TextoSecundario  = Color(0xFF6B7280)
private val TextoError       = Color(0xFFDC2626)
private val BordeInput       = Color(0xFFD1D5DB)
private val BordeInputFocus  = Color(0xFF7C3AED)

// ── Credenciales de prueba (se reemplazan por backend Ktor) ─────
private const val TEST_EMAIL    = "banelly@aliadas.com"
private const val TEST_PASSWORD = "123456"

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {

    var email         by remember { mutableStateOf("") }
    var password      by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading     by remember { mutableStateOf(false) }
    var errorMessage  by remember { mutableStateOf("") }

    val focusManager = LocalFocusManager.current

    // ── Validación ───────────────────────────────────────────────
    fun validate(): Boolean {
        return when {
            email.isBlank() -> {
                errorMessage = "Por favor ingresa tu correo"
                false
            }
            !email.contains("@") -> {
                errorMessage = "El correo no tiene formato válido"
                false
            }
            password.length < 6 -> {
                errorMessage = "Mínimo 6 caracteres"
                false
            }
            else -> {
                errorMessage = ""
                true
            }
        }
    }

    fun onIniciarSesion() {
        focusManager.clearFocus()
        if (!validate()) return
        isLoading = true
        // Simulación de llamada al backend (Sprint 3 conectará con Ktor)
        if (email == TEST_EMAIL && password == TEST_PASSWORD) {
            SessionManager.guardarSesion(email)
            isLoading = false
            onLoginSuccess()
        } else {
            isLoading = false
            errorMessage = "Correo o contraseña incorrectos"
        }
    }

    // ── UI ───────────────────────────────────────────────────────
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoLavanda)
    ) {
        // Círculo decorativo superior
        Box(
            modifier = Modifier
                .size(300.dp)
                .offset(x = 120.dp, y = (-80).dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(VioletaMedio.copy(alpha = 0.18f), Color.Transparent)
                    ),
                    shape = RoundedCornerShape(50)
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(64.dp))

            // ── Logo diamante ♦ ──────────────────────────────────
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(64.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(VioletaPrimario, VioletaMedio)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
            ) {
                Text(
                    text = "♦",
                    fontSize = 30.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Aliadas",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = VioletaPrimario,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(28.dp))

            // ── Tarjeta blanca ───────────────────────────────────
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = FondoBlanco),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "Bienvenida de nuevo",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextoPrincipal
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Accede a tu red segura de confianza.",
                        fontSize = 13.sp,
                        color = TextoSecundario,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // ── Campo correo ─────────────────────────────
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Correo electrónico",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextoPrincipal
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = email,
                            onValueChange = {
                                email = it
                                errorMessage = ""
                            },
                            placeholder = {
                                Text("tu@correo.com", color = TextoSecundario, fontSize = 14.sp)
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email,
                                imeAction = ImeAction.Next
                            ),
                            keyboardActions = KeyboardActions(
                                onNext = { focusManager.moveFocus(FocusDirection.Down) }
                            ),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = BordeInputFocus,
                                unfocusedBorderColor = BordeInput,
                                focusedContainerColor = FondoBlanco,
                                unfocusedContainerColor = FondoBlanco,
                                errorBorderColor = TextoError
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            isError = errorMessage.contains("correo")
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // ── Campo contraseña ─────────────────────────
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Contraseña",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = TextoPrincipal
                            )
                            Text(
                                text = "¿Olvidaste tu contraseña?",
                                fontSize = 12.sp,
                                color = VioletaPrimario,
                                modifier = Modifier.clickable { /* TODO: flujo de recuperación */ }
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = password,
                            onValueChange = {
                                password = it
                                errorMessage = ""
                            },
                            singleLine = true,
                            visualTransformation = if (passwordVisible)
                                VisualTransformation.None
                            else
                                PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = { onIniciarSesion() }
                            ),
                            trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(
                                        imageVector = if (passwordVisible)
                                            Icons.Filled.Visibility
                                        else
                                            Icons.Filled.VisibilityOff,
                                        contentDescription = if (passwordVisible)
                                            "Ocultar contraseña"
                                        else
                                            "Mostrar contraseña",
                                        tint = TextoSecundario
                                    )
                                }
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = BordeInputFocus,
                                unfocusedBorderColor = BordeInput,
                                focusedContainerColor = FondoBlanco,
                                unfocusedContainerColor = FondoBlanco,
                                errorBorderColor = TextoError
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            isError = errorMessage.contains("contraseña") ||
                                      errorMessage.contains("caracteres")
                        )
                    }

                    // ── Mensaje de error ─────────────────────────
                    AnimatedVisibility(
                        visible = errorMessage.isNotEmpty(),
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = errorMessage,
                            color = TextoError,
                            fontSize = 12.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 6.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(22.dp))

                    // ── Botón Iniciar Sesión ─────────────────────
                    Button(
                        onClick = { onIniciarSesion() },
                        enabled = !isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = VioletaPrimario,
                            disabledContainerColor = VioletaMedio.copy(alpha = 0.6f)
                        )
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(22.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = "Iniciar Sesión",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // ── Divisor ──────────────────────────────────
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Divider(modifier = Modifier.weight(1f), color = BordeInput)
                        Text(
                            text = "  o continua con  ",
                            fontSize = 12.sp,
                            color = TextoSecundario
                        )
                        Divider(modifier = Modifier.weight(1f), color = BordeInput)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // ── Botón Google ─────────────────────────────
                    OutlinedButton(
                        onClick = { /* TODO: Google Sign-In */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, BordeInput),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = FondoBlanco
                        )
                    ) {
                        Text(
                            text = "G",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4285F4)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Google",
                            fontSize = 14.sp,
                            color = TextoPrincipal,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // ── Link de registro ─────────────────────────
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "¿No tienes una cuenta? ",
                            fontSize = 13.sp,
                            color = TextoSecundario
                        )
                        Text(
                            text = "Regístrate",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = VioletaPrimario,
                            modifier = Modifier.clickable { /* TODO: navegar a registro */ }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ── Nota de seguridad al pie ─────────────────────────
            Text(
                text = "Al iniciar sesión, aceptas nuestros Términos y Política de Privacidad.",
                fontSize = 11.sp,
                color = TextoSecundario,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
