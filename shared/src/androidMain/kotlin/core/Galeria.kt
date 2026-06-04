package com.example.multiplataforma.core

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable

@Composable
actual fun LanzadorDeGaleria(alSeleccionarImagen: (String) -> Unit): () -> Unit {
    // Si Android Studio te subraya "rememberLauncherForActivityResult" en rojo,
    // pon el cursor encima y presiona ALT + ENTER para que la agregue automáticamente.
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            alSeleccionarImagen(uri.toString())
            println("📸 [ANDROID_NATIVO] Imagen seleccionada con éxito: $uri")
        }
    }

    return { launcher.launch("image/*") }
}