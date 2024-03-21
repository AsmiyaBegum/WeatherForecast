package com.ab.weatherforecast.ui.mapscreen

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.widget.Space
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ab.weatherforecast.R
import com.ab.weatherforecast.domain.model.FavoriteLocation
import com.ab.weatherforecast.domain.model.WeatherDummy
import com.ab.weatherforecast.ui.base.AppComposable
import com.ab.weatherforecast.ui.composables.AutoCompleteTextView
import com.ab.weatherforecast.ui.composables.WeatherView
import com.ab.weatherforecast.utils.Constants
import com.ab.weatherforecast.utils.Utils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.withContext
import org.osmdroid.OsmdroidBuildInfo
import org.osmdroid.api.IGeoPoint
import org.osmdroid.api.IMapController
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay



@Composable
fun MapScreen(locationPermissionGranted: MutableState<Boolean>, navController: NavController,viewModel: MapScreenViewModel = hiltViewModel(),lat : String = "",lon : String = "") {
    var query by remember { mutableStateOf("") }
    val weatherDetail by viewModel.weatherDetail.observeAsState()
    val placeResponse by viewModel.placeResponse.observeAsState(initial = listOf())
    var mapView by remember { mutableStateOf<MapView?>(null) }



    AppComposable(viewModel = viewModel) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            val context = LocalContext.current

            OsmdroidMapView(locationPermissionGranted,lat =lat, lon = lon, viewModel = viewModel) {
                mapView = it
            }

                if (mapView != null) {
                    AutoCompleteTextView(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        query = query,
                        queryLabel = "Search Location",
                        predictions = placeResponse,
                        onQueryChanged = { query ->
                            if(Utils.checkInternetConnection(context)){
                                viewModel.setSearchQuery(query)
                            }
                        },
                        onItemClick = { place ->
                            viewModel.clearPlaceResponse()

                            val lat = place.lat.toDouble()
                            val lon = place.lon.toDouble()

                            val controller: IMapController = mapView!!.controller


                            controller.setCenter(GeoPoint(lat, lon))
                            controller.animateTo(GeoPoint(lat, lon))
                            controller.setZoom(12)

                            // Clear existing markers
                            clearMarkers(mapView!!)


        //                   Add a marker at the clicked location
                            addMarker(GeoPoint(lat, lon), mapView!!, viewModel)
                        },
                        onClearClick = {
                            viewModel.clearPlaceResponse()
                        },
                        onDoneActionClick = {

                        },
                    ) { place ->
                        // Composable to display each item in the AutoCompleteTextView dropdown
                        Text(
                            text = place.name,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }

                    MapCenterView(
                        mapView!!,
                        modifier = Modifier.align(Alignment.CenterEnd),
                        viewModel
                    ) {
                        navController.navigate("favouriteLocationScreen")
                    }
                }







            weatherDetail?.let { weather ->
                WeatherView(weather,viewModel, navController ,onDismiss = {
                    viewModel.clearWeatherDetail()
                }, onExpandClick = {
                    navController.navigate("weatherDetail/${Gson().toJson(it)}")
                })
            }
        }

    }
}

@Composable
fun MapCenterView(mapView: MapView,modifier: Modifier,viewModel: MapScreenViewModel,openFavoriteLocation: () -> (Unit)){
    Column(
        modifier = modifier
            .padding(16.dp),
    ) {
        IconButton(modifier = Modifier,
            onClick = {
                openFavoriteLocation()
            }) {
            Image(painter = painterResource(id = R.drawable.ic_bookmark), contentDescription = "fav")
        }

        Spacer(modifier = Modifier.height(10.dp))

        IconButton(modifier = Modifier,
            onClick = {
                mapView.controller.zoomIn()
            }) {
            Image(painter = painterResource(id = R.drawable.ic_zoom_in), contentDescription = "Zoomin")
        }

        Spacer(modifier = Modifier.height(10.dp))


        IconButton(modifier = Modifier,
            onClick = {
                mapView.controller.zoomOut()
            }) {
            Image(painter = painterResource(id = R.drawable.ic_zoom_out), contentDescription = "Zoomout")
        }
    }

}



private fun fetchLocation(
    fusedLocationClient: FusedLocationProviderClient,
    onLocationFetched: (Location?) -> Unit
) {
    fusedLocationClient.lastLocation
        .addOnSuccessListener { location ->
            onLocationFetched(location)
        }
        .addOnFailureListener { exception ->
            onLocationFetched(null)
            // Handle failure
        }
}

@Composable
fun OsmdroidMapView(locationPermissionGranted: MutableState<Boolean>, viewModel: MapScreenViewModel,lat: String,lon: String, getMapView : (MapView) -> Unit) : MapView{
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val mapView = CustomMapView(context)

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            mapView.setTileSource(TileSourceFactory.MAPNIK)
            mapView.setBuiltInZoomControls(false)
            mapView.setMultiTouchControls(true)

            mapView.isVerticalMapRepetitionEnabled = false
            mapView.isHorizontalMapRepetitionEnabled = false

            mapView.setOnMapClickListener(object : CustomMapView.OnMapClickListener {

                override fun onMapClick(geoPoint: IGeoPoint?) {
                    // Clear existing markers
                    clearMarkers(mapView)

                    // Add a marker at the clicked location
                    if (geoPoint != null) {
                        addMarker(geoPoint,mapView,viewModel)
                    }

                }
            })

            val mMyLocationOverlay: MyLocationNewOverlay =
                MyLocationNewOverlay(GpsMyLocationProvider(context), mapView)
            val controller: IMapController = mapView.controller

            mMyLocationOverlay.isDrawAccuracyEnabled = true

            if(lat.isNotBlank()) {
                addMarker(GeoPoint(lat.toDouble(), lon.toDouble()),mapView,viewModel)
                controller.setCenter(GeoPoint(lat.toDouble(), lon.toDouble()))
                controller.setZoom(12)
            } else if(locationPermissionGranted.value) {
                fetchLocation(fusedLocationClient) { fetchedLocation ->
                    if(fetchedLocation != null) {
                        controller.setCenter(GeoPoint(fetchedLocation.latitude, fetchedLocation.longitude))
                        controller.setZoom(12)
                    } else {
                        controller.setCenter(GeoPoint(-26.151980693572778, 27.921252008568562))
                        controller.setZoom(12)
                    }
                }
            } else {
                controller.setCenter(GeoPoint(-26.151980693572778, 27.921252008568562))
                controller.setZoom(12)
            }

            getMapView(mapView)

            mapView
        }
    )

    return mapView
}

private fun clearMarkers(mapView: MapView) {
    // Clear all overlays from the map (including markers, routes, etc.)
    mapView.getOverlays().clear()
    // Invalidate the map to refresh it
    mapView.invalidate()
}

private fun addMarker(geoPoint: IGeoPoint?, mapView: MapView, viewModel: MapScreenViewModel) {
    if(Utils.checkInternetConnection(mapView.context) && geoPoint != null) {
        val geo = GeoPoint(geoPoint.latitude, geoPoint.longitude)
        val newMarker = Marker(mapView)
        newMarker.position = geo
        mapView.overlays.add(newMarker)


        viewModel.getWeatherAndAddress(geoPoint.latitude.toString(), geoPoint.longitude.toString())
    }
}






