import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ab.weatherforecast.R
import com.ab.weatherforecast.domain.model.FavoriteLocation
import com.ab.weatherforecast.ui.base.AppComposable
import com.ab.weatherforecast.ui.composables.AddressBox
import com.ab.weatherforecast.ui.composables.BackPressHandler
import com.ab.weatherforecast.ui.mapscreen.MapScreenViewModel
import com.ab.weatherforecast.utils.Utils
import org.osmdroid.util.GeoPoint

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavouriteLocationScreen(viewModel: MapScreenViewModel = hiltViewModel(),navController: NavController) {
    val AppBarHeight = 56.dp
    val locations by viewModel.favoriteLocation.observeAsState(initial = listOf())

    viewModel.getFavoriteLocation()

    AppComposable(viewModel = viewModel) {
        val context = LocalContext.current
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(
                        text = "Favorite Locations",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                    ) },
                    navigationIcon = {
                        IconButton(onClick = {
                            navController.navigate("mapScreen")
                        }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    },
                    modifier = Modifier.background(Color.Transparent)
                )
            }
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color.Transparent
            ) {



                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = AppBarHeight),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(locations) { location ->
                        FavouriteLocationItem(location = location){
                            if(Utils.checkInternetConnection(context)){
                                navController.navigate("mapScreen/${location.lat}/${location.lon}")
                            }
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun FavouriteLocationItem(location: FavoriteLocation,onItemClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .shadow(8.dp, shape = RoundedCornerShape(corner = CornerSize(16.dp)))
            .background(
                color = Color.Transparent,
                shape = RoundedCornerShape(corner = CornerSize(16.dp))
            ),
        contentAlignment = Alignment.CenterStart
    ) {
        AddressBox(
            address = location.locationName,
            geoPoint = GeoPoint(location.lat.toDouble(), location.lon.toDouble()),

            modifier = Modifier
                .clickable(onClick = onItemClick)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}
