package com.ab.weatherforecast.ui.weatherDetail

import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.graphics.Typeface
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.traceEventEnd
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.colorResource

import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.ab.weatherforecast.R
import com.ab.weatherforecast.ui.composables.AddressBox
import com.ab.weatherforecast.ui.composables.AverageTemperature
import com.ab.weatherforecast.ui.composables.DayChip
import com.ab.weatherforecast.ui.composables.HourlyForecastData
import com.ab.weatherforecast.ui.composables.convertToFahrenheit
import org.osmdroid.util.GeoPoint
import kotlin.math.abs
import kotlin.math.roundToInt


@Composable
fun WeatherScreen(weatherData : List<HourlyForecastData>, isFahrenheit : Boolean) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    ){

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent),
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Weather Forecast",
                modifier = Modifier.padding(16.dp),
                fontWeight = FontWeight.Bold
            )
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.Transparent
            ) {
                LazyRow(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    item {
                        Surface(
                            modifier = Modifier
                                .width(100.dp * weatherData.size) // Set width based on data size
                                .height(300.dp)
                                .padding(top = 8.dp),
                            shape = RoundedCornerShape(16.dp),
                            color = Color.Transparent
                        ) {
                            Surface(
                                modifier = Modifier.fillMaxSize(),
                                color = Color.Transparent
                            ) {
                                LineChart(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(top = 10.dp),
                                    weatherData,
                                    isFahrenheit
                                )
                            }
                        }
                    }
                }
            }
        }

    }

}

@Composable
fun LineChart(
    modifier: Modifier,
    dailyWeather: List<HourlyForecastData>,
    isFahrenheit: Boolean
) {

    val (cur, setCur) = remember { mutableStateOf<List<HourlyForecastData?>>(listOf()) }

    var trigger by remember { mutableStateOf(0f) }

    val graphColor = colorResource(id = R.color.teal_200)
    val contentColor = if(isSystemInDarkTheme()){
        Color.White
    }else{
        Color.Black
    }

    val spacing = 100f


    DisposableEffect(dailyWeather) {
        trigger = 1f
        onDispose { }
    }

    val animateFloat by animateFloatAsState(
        targetValue = trigger,
        animationSpec = tween(1500)
    ) {
        setCur(dailyWeather)
        trigger = 0f
    }

    Canvas(modifier) {
        val spacePerHour = (size.width - spacing) / dailyWeather.size
        val increment = size.width / dailyWeather.size
        val max = dailyWeather.maxOf { it.temperature }
        val min = dailyWeather.minOf { it.temperature }
        val dy = (max - min).toFloat()

        drawIntoCanvas { canvas ->

            if (cur != dailyWeather) {
                // change visible range according to animation
                canvas.clipRect(Rect(0f, 0f, size.width * animateFloat, size.height))
            }

            val path = Path()

            val points = dailyWeather.mapIndexed { index, hourlyWeather ->
                Offset(
                    increment * index + increment / 2,
                    ((1 - (hourlyWeather.temperature - min) / dy) * (size.height * 0.3f) +
                            size.height * 0.2f).toFloat() // reserve space for drawText
                )
            }

            path.moveTo(0f, points.first().y)
            path.lineTo(points.first().x, points.first().y)

            (0..points.size - 2).forEach { index ->

                val startP = points[index]
                val endP = points[index + 1]

                val p2: Offset
                val p3: Offset
                val cx = (startP.x + endP.x) / 2
                val dy = abs((endP.y - startP.y) / 4)
                if (endP.y < startP.y) {
                    p2 = Offset(cx, startP.y - dy)
                    p3 = Offset(cx, endP.y + dy)
                } else {
                    p2 = Offset(cx, startP.y + dy)
                    p3 = Offset(cx, endP.y - dy)
                }
                path.cubicTo(p2.x, p2.y, p3.x, p3.y, endP.x, endP.y)
            }

            path.lineTo(points.last().x + increment / 2, points.last().y)
            path.lineTo(points.last().x + increment / 2, size.height)
            path.lineTo(0f, size.height)
            path.lineTo(0f, points.first().y)


            val strokePath = path
            drawPath(
                path = strokePath,
                color = graphColor,
                style = Stroke(
                    width = 2.dp.toPx(),
                    cap = StrokeCap.Round
                )
            )

            val fillPath = android.graphics.Path(strokePath.asAndroidPath()).asComposePath().apply {
                lineTo(size.width - spacePerHour, size.height - spacing)
                lineTo(spacing, size.height - spacing)
                close()
            }

            drawPath(
                path = fillPath,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        graphColor,
                        Color(android.graphics.Color.TRANSPARENT)
                    ),
                    endY = size.height - spacing
                ))


            // draw points
            canvas.drawPoints(
                PointMode.Points, points,
                androidx.compose.ui.graphics.Paint().apply {
                    strokeWidth = 8f
                    strokeCap = StrokeCap.Round
                    color = Color.Gray.copy(0.6f)
                }
            )

            val size = 12.sp.toPx()
            val textPaint = Paint().apply {
                color = contentColor.toArgb()
                typeface = Typeface.SERIF
                textAlign = Paint.Align.CENTER
                textSize = density.run { size }
            }

            dailyWeather.asSequence().zip(points.asSequence())
                .forEachIndexed { index, pair ->
                    val (weather, points) = pair
                    canvas.nativeCanvas.drawText(
                          if(isFahrenheit){
                            "${weather.fahrenheitTemp}°F"
                        }else{
                            "${weather.temperature}°C"
                        },

                        points.x - size / 2,
                        points.y - size / 1.5f,
                        textPaint
                    )

                    // Draw time text
                    val hour = weather.time
                    canvas.nativeCanvas.drawText(
                        hour,
                        points.x,
                        size + 8.dp.toPx(), // Adjust the y-coordinate for time text position
                        textPaint
                    )
                }
        }
    }
}





