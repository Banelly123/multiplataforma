package com.example.myapplicationuvu.home

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline

@Composable
fun MapaVista(
    modifier: Modifier = Modifier,
    origen: GeoPoint,
    destino: GeoPoint,
    rutaPuntos: List<GeoPoint> = emptyList(),
    context: Context
) {
    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            Configuration.getInstance().userAgentValue = ctx.packageName
            val mapView = MapView(ctx)
            mapView.setTileSource(TileSourceFactory.MAPNIK)
            mapView.setMultiTouchControls(true)
            mapView.controller.setZoom(15.0)
            mapView.controller.setCenter(origen)

            val markerOrigen = Marker(mapView)
            markerOrigen.position = origen
            markerOrigen.title = "Origen"
            mapView.overlays.add(markerOrigen)

            val markerDestino = Marker(mapView)
            markerDestino.position = destino
            markerDestino.title = "Destino"
            mapView.overlays.add(markerDestino)

            if (rutaPuntos.isNotEmpty()) {
                val polyline = Polyline()
                polyline.setPoints(rutaPuntos)
                mapView.overlays.add(polyline)
            }

            mapView
        }
    )
}
