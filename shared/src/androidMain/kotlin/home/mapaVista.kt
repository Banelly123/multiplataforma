package com.example.multiplataforma.home

import android.preference.PreferenceManager
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView

@Composable
actual fun MapaFondo() {
    val context = LocalContext.current

    remember {
        val config = Configuration.getInstance()
        val appContext = context.applicationContext

        // Carga la configuración básica (esto es lo único que realmente necesita)
        config.load(appContext, PreferenceManager.getDefaultSharedPreferences(appContext))
        config.userAgentValue = appContext.packageName
    }

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { ctx ->
            MapView(ctx).apply {
                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)

                controller.apply {
                    setZoom(16.0)
                    setCenter(GeoPoint(20.0833, -98.3667)) // Tulancingo
                }
            }
        }
    )
}