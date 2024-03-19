package com.ab.weatherforecast.ui.mapscreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.api.IGeoPoint
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker


@Composable
fun MapScreen(){
    OsmdroidMapView()
}

@Composable
fun OsmdroidMapView() {
    val context = LocalContext.current

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            val mapView = CustomMapView(context)
            mapView.setTileSource(TileSourceFactory.MAPNIK)
            mapView.setBuiltInZoomControls(false)
            mapView.setMultiTouchControls(true)

            mapView.setOnMapClickListener(object : CustomMapView.OnMapClickListener {

                override fun onMapClick(geoPoint: IGeoPoint?) {
                    // Clear existing markers
                    clearMarkers(mapView)

                    // Add a marker at the clicked location
                    if (geoPoint != null) {
                        addMarker(geoPoint,mapView)
                    }

                }
            })

            mapView
        }
    )
}

private fun clearMarkers(mapView: MapView) {
    // Clear all overlays from the map (including markers, routes, etc.)
    mapView.getOverlays().clear()
    // Invalidate the map to refresh it
    mapView.invalidate()
}

private fun addMarker(geoPoint: IGeoPoint?, mapView: MapView) {
    if(geoPoint!=null){
        val newMarker = Marker(mapView)
        newMarker.position = GeoPoint(geoPoint.latitude,geoPoint.longitude)
        mapView.getOverlays().add(newMarker)
        // Invalidate the map to refresh it
        mapView.invalidate()
    }

}





