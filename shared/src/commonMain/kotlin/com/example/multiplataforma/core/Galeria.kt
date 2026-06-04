package com.example.multiplataforma.core

import androidx.compose.runtime.Composable

// Le prometemos a la app que habrá una función para lanzar la galería.
// Recibe una función que se ejecutará cuando elijas la foto (nos dará la ruta de la imagen).
// Y devuelve la "acción" (el clic) para abrirla.
@Composable
expect fun LanzadorDeGaleria(alSeleccionarImagen: (String) -> Unit): () -> Unit