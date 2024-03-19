package com.ab.weatherforecast.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.ab.weatherforecast.R
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp


sealed class TemperatureCategory {
    object Cold : TemperatureCategory()
    object Mild : TemperatureCategory()
    object Hot : TemperatureCategory()
}

fun categorizeTemperature(temperature: Int): TemperatureCategory {
    return when {
        temperature < 10 -> TemperatureCategory.Cold
        temperature in 10..25 -> TemperatureCategory.Mild
        else -> TemperatureCategory.Hot
    }
}

fun getIconForTemperatureCategory(category: TemperatureCategory): Int {
    return when (category) {
        is TemperatureCategory.Cold -> R.drawable.ic_cold_weather
        is TemperatureCategory.Mild -> R.drawable.ic_mild_temperature
        is TemperatureCategory.Hot -> R.drawable.ic_hot_temperature
    }
}

@Composable
fun AverageTemperature() {
    var temperature by remember { mutableStateOf(250) } // Example temperature
    var isFahrenheit by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Top) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(end = 16.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "°C",
                color = colorResource(id = R.color.white),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { isFahrenheit = false }
                    .align(Alignment.CenterVertically)
            )

            Switch(
                checked = isFahrenheit,
                onCheckedChange = { isFahrenheit = it },
                modifier = Modifier.padding(start = 8.dp, end = 8.dp)
                    .align(Alignment.CenterVertically)

            )

            Text(
                text = "°F",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.white),
                modifier = Modifier.clickable { isFahrenheit = true }
                    .align(Alignment.CenterVertically)

            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = painterResource(id = getIconForTemperatureCategory(categorizeTemperature(temperature))),
                contentDescription = null, // Provide a proper content description
                modifier = Modifier.size(160.dp).padding(start = 20.dp, end = 8.dp, top = 4.dp)
            )

            Text(
                text = if (isFahrenheit) "${convertToFahrenheit(temperature)}°F" else "$temperature°C",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(end = 25.dp, top = 16.dp)
                    .align(Alignment.CenterVertically)
            )

        }
    }
}

fun convertToFahrenheit(celsius: Int): Int {
    return (celsius * 9 / 5) + 32
}
