package com.example.multiplataforma.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.example.multiplataforma.auth.SessionManager // ¡Importación agregada!
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
    val contactosService = remember { ContactosService() }
    val coroutineScope = rememberCoroutineScope()
    val sessionManager = remember { SessionManager() } // ¡La variable que te faltaba!

    var listaContactos by remember {
        mutableStateOf(cargarContactosGuardados(almacenamiento.leerContactos()))
    }

    var mostrarFormulario by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
            .padding(paddingValues)
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Título y Botón Añadir
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

            Text(
                text = if (mostrarFormulario) "Cancelar" else "Añadir",
                color = if (mostrarFormulario) Color.Red else Color(0xFF7C3AED),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                modifier = Modifier.clickable {
                    mostrarFormulario = !mostrarFormulario
                    if (!mostrarFormulario) { nombre = ""; numero = ""; parentesco = ""; direccion = "" }
                }.padding(8.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // FORMULARIO
        if (mostrarFormulario) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(value = numero, onValueChange = { numero = it }, label = { Text("Número de Teléfono") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(value = parentesco, onValueChange = { parentesco = it }, label = { Text("Parentesco") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(value = direccion, onValueChange = { direccion = it }, label = { Text("Dirección") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            if (nombre.isNotBlank() && numero.isNotBlank()) {
                                val nuevoContacto = ContactoSeguro(nombre, numero, parentesco, direccion)
                                val nuevaLista = listOf(nuevoContacto) + listaContactos
                                listaContactos = nuevaLista

                                // Guardado Local
                                almacenamiento.guardarContactos(convertirListaAString(nuevaLista))

                                // Envío a Railway
                                coroutineScope.launch {
                                    val miCorreo = sessionManager.obtenerEmail() ?: ""
                                    contactosService.enviarContactoAlServidor(nuevoContacto, miCorreo)
                                }

                                nombre = ""; numero = ""; parentesco = ""; direccion = ""
                                mostrarFormulario = false
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C3AED))
                    ) {
                        Text("Guardar Contacto", fontWeight = FontWeight.Bold)
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        // LISTA DE CONTACTOS
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
                        Box(modifier = Modifier.size(50.dp).clip(CircleShape).background(Color(0xFFF3E8FF)), contentAlignment = Alignment.Center) {
                            Text(iniciales, color = Color(0xFF7C3AED), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(contacto.nombre, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF1A1A1A))
                            Text(contacto.parentesco, color = Color(0xFF7C3AED), fontSize = 13.sp)
                            Text(contacto.numero, color = Color.Gray, fontSize = 14.sp)
                        }
                        IconButton(
                            onClick = {
                                coroutineScope.launch {
                                    val exito = contactosService.eliminarContactoDelServidor(contacto.numero)
                                    if (exito) {
                                        val listaActualizada = listaContactos.filter { it != contacto }
                                        listaContactos = listaActualizada
                                        almacenamiento.guardarContactos(convertirListaAString(listaActualizada))
                                    }
                                }
                            }
                        ) {
                            Text("🗑️", fontSize = 20.sp)
                        }
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(20.dp)) }
        }
    }
}

// Funciones de ayuda
fun convertirListaAString(lista: List<ContactoSeguro>): String {
    return lista.joinToString(";;") { "${it.nombre}|${it.numero}|${it.parentesco}|${it.direccion}" }
}

fun cargarContactosGuardados(textoLeido: String): List<ContactoSeguro> {
    if (textoLeido.isBlank()) return emptyList()
    return textoLeido.split(";;").mapNotNull { fragmento ->
        val partes = fragmento.split("|")
        if (partes.size == 4) ContactoSeguro(partes[0], partes[1], partes[2], partes[3]) else null
    }
}