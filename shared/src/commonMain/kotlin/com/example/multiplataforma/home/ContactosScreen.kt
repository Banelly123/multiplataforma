package com.example.multiplataforma.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.multiplataforma.core.AlmacenamientoLocal
import kotlinx.coroutines.launch

data class ContactoSeguro(
    val nombre: String,
    val numero: String,
    val parentesco: String,
    val direccion: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactosScreen(paddingValues: PaddingValues) {
    var nombre by remember { mutableStateOf("") }
    var numero by remember { mutableStateOf("") }
    var parentesco by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }

    val almacenamiento = remember { AlmacenamientoLocal() }
    val servicioContactos = remember { ContactosService() }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    var listaContactos by remember {
        mutableStateOf(cargarContactosGuardados(almacenamiento.leerContactos()))
    }


    // aquí en el Box principal

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(paddingValues)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Contactos de\nConfianza",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF1A1A1A),
                    lineHeight = 32.sp
                )
                Text(text = "Añadir", color = Color(0xFF7C3AED), fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = nombre, onValueChange = { nombre = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = Color.LightGray)
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = numero, onValueChange = { numero = it },
                label = { Text("Número de Teléfono") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = Color.LightGray)
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = parentesco, onValueChange = { parentesco = it },
                label = { Text("Parentesco") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = Color.LightGray)
            )
            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    if (nombre.isNotBlank() && numero.isNotBlank()) {
                        val nuevoContacto = ContactoSeguro(nombre, numero, parentesco, direccion)
                        val nuevaLista = listOf(nuevoContacto) + listaContactos

                        listaContactos = nuevaLista
                        almacenamiento.guardarContactos(convertirListaAString(nuevaLista))

                        coroutineScope.launch {
                            servicioContactos.enviarContactoAlServidor(nuevoContacto)
                            // borrar cualquier mensaje anterior
                            snackbarHostState.currentSnackbarData?.dismiss()
                            snackbarHostState.showSnackbar("¡Contacto $nombre guardado!")
                        }

                        nombre = ""; numero = ""; parentesco = ""; direccion = ""
                    }
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C3AED))
            ) {
                Text("Guardar Contacto Local", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(32.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(listaContactos) { contacto ->
                    val iniciales = contacto.nombre.split(" ").take(2).mapNotNull { it.firstOrNull()?.uppercase() }.joinToString("")

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier.size(50.dp).clip(CircleShape).background(Color(0xFF9F7AEA)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(iniciales, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(contacto.nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF1A1A1A))
                                Text(contacto.parentesco, color = Color(0xFF7C3AED), fontSize = 13.sp, fontWeight = FontWeight.Medium)
                                Text(contacto.numero, color = Color.Gray, fontSize = 14.sp)
                            }

                            IconButton(
                                onClick = {
                                    val listaActualizada = listaContactos.filter { it != contacto }
                                    listaContactos = listaActualizada
                                    almacenamiento.guardarContactos(convertirListaAString(listaActualizada))

                                    coroutineScope.launch {
                                        snackbarHostState.currentSnackbarData?.dismiss()
                                        snackbarHostState.showSnackbar("Contacto eliminado")
                                    }
                                }
                            ) {
                                Text("🗑️", fontSize = 20.sp)
                            }
                        }
                    }
                }
                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }

        // La notificación
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 8.dp)
        )
    }
}

fun convertirListaAString(lista: List<ContactoSeguro>): String {
    return lista.joinToString(";;") { "${it.nombre}|${it.numero}|${it.parentesco}|${it.direccion}" }
}

fun cargarContactosGuardados(textoLeido: String): List<ContactoSeguro> {
    if (textoLeido.isBlank()) {
        return listOf(
            ContactoSeguro("Elena Garcia", "+52 775 000 0000", "Mamá", "Centro, Tulancingo"),
            ContactoSeguro("Roberto López", "+52 775 111 2222", "Pareja", "Col. Vicente Guerrero")
        )
    }
    return textoLeido.split(";;").mapNotNull { fragmento ->
        val partes = fragmento.split("|")
        if (partes.size == 4) {
            ContactoSeguro(partes[0], partes[1], partes[2], partes[3])
        } else null
    }
}