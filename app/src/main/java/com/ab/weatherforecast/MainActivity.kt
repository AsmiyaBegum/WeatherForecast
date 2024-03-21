package com.ab.weatherforecast

import FavouriteLocationScreen
import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ab.weatherforecast.domain.model.WeatherDummy
import com.ab.weatherforecast.domain.model.WeatherInfo
import com.ab.weatherforecast.ui.mapscreen.MapScreen
import com.ab.weatherforecast.ui.splash.SplashScreen
import com.ab.weatherforecast.ui.theme.WeatherForecastTheme
import com.ab.weatherforecast.ui.weatherDetail.WeatherDetail
import com.ab.weatherforecast.utils.Constants
import com.ab.weatherforecast.utils.Utils
import com.ab.weatherforecast.utils.WeatherInfoArgType
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val locationPermissionGranted: MutableState<Boolean> = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.INTERNET), 0)
        }
        super.onCreate(savedInstanceState)
        loadMapScreen()
        requestLocationPermission()
        val ctx = applicationContext
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx))
        Configuration.getInstance().userAgentValue = "MapApp"
    }

    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(this, "Location Permission denied", Toast.LENGTH_SHORT).show()
            }
            else
            {
                ActivityCompat.requestPermissions(this as ComponentActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), Constants.LOCATION_PERMISSION_REQUEST_CODE)
            }
        } else {
            locationPermissionGranted.value = true
        }
    }

    private fun loadMapScreen() {
        setContent {
            WeatherForecastTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "splash") {
                        composable("splash"){
                            SplashScreen(navController)
                        }
                        composable("mapScreen") {
                            MapScreen(locationPermissionGranted, navController = navController)
                        }
                        composable("mapScreen/{lat}/{long}") {
                            val lat = it.arguments?.getString("lat")?:""
                            val long = it.arguments?.getString("long")?:""
                            MapScreen(locationPermissionGranted, navController = navController,lat = lat, lon = long)
                        }
                        composable("weatherDetail/{parameter}",arguments = listOf(navArgument("parameter"){
                            type = WeatherInfoArgType() })) { backStackEntry ->
                            val parameter = backStackEntry.arguments?.getString("parameter")?.let { Gson().fromJson(it, WeatherInfo::class.java) }
                            parameter?.let {
                                WeatherDetail(it,navController)
                            } ?: run {
                                // Handle case when parameter is null
                            }
                        }
                        composable("favouriteLocationScreen") {
                            FavouriteLocationScreen(navController = navController)
                        }
                    }

                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            Constants.LOCATION_PERMISSION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    locationPermissionGranted.value = true
                } else {
                    // Permission denied
                    Toast.makeText(this, "Location Permission denied", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }

}