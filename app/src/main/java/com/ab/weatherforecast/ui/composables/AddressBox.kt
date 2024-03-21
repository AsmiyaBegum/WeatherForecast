package com.ab.weatherforecast.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ab.weatherforecast.R
import com.ab.weatherforecast.utils.roundTo
import org.osmdroid.util.GeoPoint

@Composable
fun AddressBox(address: String, geoPoint: GeoPoint, modifier: Modifier = Modifier) {
    val latLongString = stringResource(
        R.string.lat_long_format,
        "${geoPoint.latitude.roundTo(3)}",
        "${geoPoint.longitude.roundTo(3)}"
    )

    Surface(
        modifier = modifier,
        color = Color.Transparent // Set the background color of the Surface to transparent
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent) // Set the background color of the Column to transparent
                .padding(10.dp), // Adjust padding as needed
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = address,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp), // Adjust padding as needed
                fontWeight = FontWeight.ExtraBold,
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = latLongString,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp), // Adjust padding as needed
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )

        }
    }
}