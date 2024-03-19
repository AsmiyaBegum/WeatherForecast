package com.ab.weatherforecast.ui.composables

import android.graphics.Canvas
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp

@Composable
fun WeatherLineGraph() {
    val weatherData = generateWeatherData() // Generate sample weather data
    val canvasHeight = 200.dp
    val canvasWidth = 300.dp
    val iconSize = 24.dp
    val timeLabels = (0 until 24).map { it.toString().padStart(2, '0') + ":00" } // 24-hour time labels

    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Canvas(
                modifier = Modifier
                    .height(canvasHeight)
                    .width(canvasWidth)
                    .padding(vertical = 8.dp)
            ) {
                drawLineGraph(weatherData, timeLabels)
            }
        }
    }
}

fun DrawScope.drawLineGraph(weatherData: List<Float>, timeLabels: List<String>) {
    val width = size.width
    val height = size.height

    // Calculate the maximum and minimum temperatures for scaling
    val maxTemperature = weatherData.maxOrNull() ?: 0f
    val minTemperature = weatherData.minOrNull() ?: 0f

    val temperatureRange = maxTemperature - minTemperature
    val temperatureRatio = if (temperatureRange != 0f) height / temperatureRange else 1f

    // Draw the line graph
    weatherData.forEachIndexed { index, temperature ->
        val x = (index * width / (weatherData.size - 1)).toFloat()
        val y = height - (temperature - minTemperature) * temperatureRatio

        drawCircle(Color.Blue, radius = 4f, center = Offset(x, y))

        if (index < weatherData.size - 1) {
            val nextX = ((index + 1) * width / (weatherData.size - 1)).toFloat()
            val nextY = height - (weatherData[index + 1] - minTemperature) * temperatureRatio
            drawLine(Color.Blue, start = Offset(x, y), end = Offset(nextX, nextY))
        }

        // Draw time label above each point
        drawIntoCanvas {
            val paint = Paint().asFrameworkPaint()
            paint.color = Color.Black.toArgb()
            paint.textSize = 24f

            it.nativeCanvas.drawText(
                timeLabels[index],
                x - (paint.measureText(timeLabels[index]) / 2), // Center the text horizontally
                40f, // Offset for the text above the graph
                paint
            )
        }
    }
}

fun generateWeatherData(): List<Float> {
    // Generate sample weather data (temperatures)
    return listOf(20f, 22f, 18f, 25f, 21f, 23f, 19f)
}
