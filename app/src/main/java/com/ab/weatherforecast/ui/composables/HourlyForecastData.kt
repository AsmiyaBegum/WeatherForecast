package com.ab.weatherforecast.ui.composables

import androidx.compose.foundation.Image
import com.ab.weatherforecast.utils.StringConstants.DEGREE


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ab.weatherforecast.R
import com.ab.weatherforecast.ui.weatherDetail.LineChart
import com.ab.weatherforecast.ui.weatherDetail.WeatherScreen
import com.ab.weatherforecast.utils.roundTo

data class HourlyForecastData(
    val weatherType: String,
    val time: String,
    val temperature: Double,
    val fahrenheitTemp : Double
)


@Composable
fun HourlyForecastCard(data : List<HourlyForecastData>,isFahrenheit : Boolean) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.Transparent,
            shape = MaterialTheme.shapes.medium
        ) {
            Column(Modifier.padding(20.dp)) {
                Text(text = "Hourly Forecast", fontWeight = FontWeight.Bold)
                Spacer(Modifier.size(20.dp))
                Row(
                    Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(40.dp)
                ) {
                    data.forEach {
                        HourlyForecastItem(it,isFahrenheit)
                    }
                }
            }
        }
    }

}

@Composable
fun HourlyForecastItem(data: HourlyForecastData,isFahrenheit: Boolean) {
    val (type, time, temperature, fahrenheitTemp) = data
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = time,
            fontWeight = FontWeight.Bold
        )
        Image(
            modifier = Modifier.size(40.dp),
            painter = painterResource(
                when (type) {
                    "clear sky" -> R.drawable.ic_scattered_cloud
                    "few clouds" -> R.drawable.cloudy
                    "scattered clouds" -> R.drawable.ic_scattered_cloud
                    "broken clouds" -> R.drawable.ic_broken_cloud
                    "shower rain" -> R.drawable.ic_shower_rain
                    "rain" -> R.drawable.ic_rain
                    "thunderstorm" -> R.drawable.ic_thunder
                    "snow" -> R.drawable.ic_snow
                    "mist" -> R.drawable.ic_mist
                    else -> R.drawable.ic_cold_weather
                }
            ),
            contentDescription = "Weather Type"
        )

        Text(
            text = if (isFahrenheit) "${(fahrenheitTemp).roundTo(2)}°F" else "${
                temperature.roundTo(2)}°C",
            fontWeight = FontWeight.Bold
        )

        Text(
            text = type
        )
    }
}