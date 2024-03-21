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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import com.ab.weatherforecast.utils.roundTo


sealed class TemperatureCategory {
    object Cold : TemperatureCategory()
    object Mild : TemperatureCategory()
    object Hot : TemperatureCategory()
}

fun categorizeTemperature(temperature: Double): TemperatureCategory {
    return when {
        temperature < 10.0 -> TemperatureCategory.Cold
        temperature in 10.0..25.0 -> TemperatureCategory.Mild
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
fun AverageTemperature(temperature: Pair<Double,Double>, isFahrenheit : (Boolean) -> (Unit)) {
//    var temperature by remember { mutableStateOf(temperature) } // Example temperature
    var isFahrenheit by remember { mutableStateOf(false) }

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 25.dp), verticalArrangement = Arrangement.Top) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 16.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "°C",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clickable {
                        isFahrenheit = false
                    }
                    .align(Alignment.CenterVertically)
            )

            Switch(
                checked = isFahrenheit,
                onCheckedChange = {
                    isFahrenheit = it
                    isFahrenheit(it)
                                  },
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp)
                    .align(Alignment.CenterVertically)

            )

            Text(
                text = "°F",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clickable {
                        isFahrenheit = true
                    }
                    .align(Alignment.CenterVertically)

            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {

            Image(
                painter = painterResource(id = getIconForTemperatureCategory(categorizeTemperature(temperature.second))),
                contentDescription = null, // Provide a proper content description
                modifier = Modifier
                    .size(160.dp)
                    .padding(start = 20.dp, end = 8.dp)
            )

            Column(modifier = Modifier
                .padding(start =8.dp,end = 25.dp)
                .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = if (isFahrenheit) "Min : ${convertToFahrenheit(temperature.first).roundTo(2)}°F" else "Min : ${temperature.first.roundTo(2)}°C",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,

                )

                Text(
                    text = if (isFahrenheit) "Max : ${convertToFahrenheit(temperature.second).roundTo(2)}°F" else "Max : ${temperature.second.roundTo(2)}°C",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                )
            }


        }
    }
}

fun convertToFahrenheit(celsius: Double): Double {
    return (celsius * 9 / 5) + 32
}