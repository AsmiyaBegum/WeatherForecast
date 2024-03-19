package com.ab.weatherforecast.ui.weatherDetail

import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Rect
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.colorResource

import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.ab.weatherforecast.R
import com.ab.weatherforecast.ui.composables.AddressBox
import com.ab.weatherforecast.ui.composables.AverageTemperature
import com.ab.weatherforecast.ui.composables.DayChip
import org.osmdroid.util.GeoPoint
import kotlin.math.roundToInt


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherDetailScreen(){
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        // Assuming you have a background image drawable in your resources
//        val backgroundImage = painterResource(id = R.drawable.ic_background)

        Box(
            modifier = Modifier.fillMaxSize()
        ){
            // Background image
            Image(
                painter = painterResource(id = R.drawable.ic_background),
                contentDescription = null, // Provide a proper content description
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )

            // Overlay with translucent color or gradient
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x80000000))
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Close button
                IconButton(
                    onClick = { /* handle close action */ },
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.End)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_close),
                        contentDescription = stringResource(id = R.string.close_icon),
                        modifier = Modifier.size(35.dp)
                    )
                }

                // Selected Address
                AddressBox("Chennai, Tamil Nadu, India", GeoPoint(13.067439,80.237617))

                // Day Chip
                DayChip()

                // Average temperature
                AverageTemperature()

                // Weather graph
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp, start = 16.dp, end = 16.dp)
                        .shadow(8.dp, shape = RoundedCornerShape(corner = CornerSize(16.dp)))
                ){
                    WeatherScreen()
                }

            }
        }
    }
}


@Composable
fun WeatherScreen(){
    val weatherData = listOf(
        "12AM" to 20f,
        "1AM" to 22f,
        "2AM" to 18f,
        "3AM" to 25f,
        "4AM" to 21f,
        "5AM" to 23f,
        "6AM" to 9f
    )

    val markerResources = listOf(
//        R.drawable., // Add your marker images here
        R.drawable.ic_mild_temperature
//        R.drawable.marker3,
        // Add more marker images as needed
    )
    val values = weatherData.mapIndexed { index, (time, temperature) ->
        Pair(index, temperature.toDouble())
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(android.graphics.Color.BLACK)),
        verticalArrangement = Arrangement.Center
    ) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Weather Forecast",
                modifier = Modifier.padding(10.dp))
        Spacer(modifier = Modifier.height(40.dp))
            LazyRow {
                item {
                    LineChart(
                        data = values,
                        iconResource = R.drawable.ic_cloud_snow,
                        modifier = Modifier
                            .width(100.dp * weatherData.size) // Set width based on data size
                            .height(300.dp)
                            .padding(top = 16.dp)
                    )
                }

        }
        Spacer(modifier = Modifier.height(40.dp))
        Divider()
        Spacer(modifier = Modifier.height(40.dp))
    }

}



//fun renderData(mChart: LineChart,weatherData : List<Pair<String, Float>>) {
//    val llXAxis = LimitLine(10f, "Index 10")
//    llXAxis.lineWidth = 4f
//    llXAxis.enableDashedLine(10f, 10f, 0f)
//    llXAxis.labelPosition = LimitLine.LimitLabelPosition.RIGHT_BOTTOM
//    llXAxis.textSize = 10f
//
//    val xAxis: XAxis = mChart.getXAxis()
//    xAxis.enableGridDashedLine(10f, 10f, 0f)
//    xAxis.axisMaximum = 10f
//    xAxis.axisMinimum = 0f
//    xAxis.setDrawLimitLinesBehindData(true)
//
//    val ll1 = LimitLine(215f, "Maximum Limit")
//    ll1.lineWidth = 4f
//    ll1.enableDashedLine(10f, 10f, 0f)
//    ll1.labelPosition = LimitLine.LimitLabelPosition.RIGHT_TOP
//    ll1.textSize = 10f
//
//    val ll2 = LimitLine(70f, "Minimum Limit")
//    ll2.lineWidth = 4f
//    ll2.enableDashedLine(10f, 10f, 0f)
//    ll2.labelPosition = LimitLine.LimitLabelPosition.RIGHT_BOTTOM
//    ll2.textSize = 10f
//
//    val leftAxis: YAxis = mChart.getAxisLeft()
//    leftAxis.removeAllLimitLines()
//    leftAxis.addLimitLine(ll1)
//    leftAxis.addLimitLine(ll2)
//    leftAxis.axisMaximum = 350f
//    leftAxis.axisMinimum = 0f
//    leftAxis.enableGridDashedLine(10f, 10f, 0f)
//    leftAxis.setDrawZeroLine(false)
//    leftAxis.setDrawLimitLinesBehindData(false)
//
//    mChart.getAxisRight().setEnabled(false)
//    setData(mChart,weatherData)
//}

//private fun setData(mChart: LineChart,weatherData : List<Pair<String, Float>>) {
//    val values = weatherData.mapIndexed { index, (time, temperature) ->
//        Entry(index.toFloat(), temperature)
//    }
//    val set1: LineDataSet
//    if (mChart.getData() != null &&
//        mChart.getData().getDataSetCount() > 0
//    ) {
//        set1 = mChart.getData().getDataSetByIndex(0) as LineDataSet
//        set1.setValues(values)
//        mChart.getData().notifyDataChanged()
//        mChart.notifyDataSetChanged()
//    } else {
//        set1 = LineDataSet(values, "Sample Data")
//        set1.setDrawIcons(false)
//        set1.enableDashedLine(10f, 5f, 0f)
//        set1.enableDashedHighlightLine(10f, 5f, 0f)
//        set1.color = R.color.white
//        set1.setCircleColor(R.color.white)
//        set1.lineWidth = 1f
//        set1.circleRadius = 3f
//        set1.setDrawCircleHole(false)
//        set1.valueTextSize = 9f
//        set1.setDrawFilled(true)
//        set1.formLineWidth = 10f
//        set1.formLineDashEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
//        set1.formSize = 15f
//
//        if (Utils.getSDKInt() >= 18) {
//            val drawable = ContextCompat.getDrawable(mChart.context, com.ab.weatherforecast.R.drawable.ic_cold_weather)
//            set1.fillDrawable = drawable
//        } else {
////            set1.fillColor = Color.DKGRAY
//        }
//        val dataSets = ArrayList<ILineDataSet>()
//        dataSets.add(set1)
//        val data = LineData(dataSets)
//        mChart.setData(data)
//    }
//}




@Composable
fun LineChart(
    data: List<Pair<Int, Double>> = emptyList(),
    iconResource: Int,
    modifier: Modifier = Modifier
) {
    val spacing = 100f
    val graphColor = colorResource(id = R.color.teal_200)
    val upperValue = remember { (data.maxOfOrNull { it.second }?.plus(1))?.roundToInt() ?: 0 }
    val lowerValue = remember { (data.minOfOrNull { it.second }?.toInt() ?: 0) }
    val density = LocalDensity.current

    val textPaint = remember(density) {
        Paint().apply {
            color = android.graphics.Color.WHITE
            textAlign = Paint.Align.CENTER
            textSize = density.run { 12.sp.toPx() }
        }
    }

    val context = LocalContext.current
    val drawable = remember(iconResource) {
        ContextCompat.getDrawable(context, iconResource)
    }

    Canvas(modifier = modifier) {
        val spacePerHour = (size.width - spacing) / data.size
        val height = size.height

        val strokePath = Path()
        data.indices.forEach { i ->
            val info = data[i]
            val ratio = (info.second - lowerValue) / (upperValue - lowerValue)

            val x1 = spacing + i * spacePerHour
            val y1 = height - spacing - (ratio * height).toFloat()

            if (i == 0) {
                strokePath.moveTo(x1, y1)
            } else {
                strokePath.lineTo(x1, y1)
            }

            // Draw y-axis number above the line point
            drawContext.canvas.nativeCanvas.drawText(
                info.second.toInt().toString(),
                x1,
                y1 - 60f, // Adjust the distance between the text and the image
                textPaint
            )

            // Draw icon above the text point
            drawable?.apply {
                val scaleFactor = 7.5f // Adjust the scale factor as needed
                val scaledWidth = (intrinsicWidth * scaleFactor / 4).toInt()
                val scaledHeight = (intrinsicHeight * scaleFactor / 4).toInt()
                setBounds(
                    (x1 - scaledWidth / 2).toInt(),
                    (y1 - scaledHeight / 2 - 10).toInt(),
                    (x1 + scaledWidth / 2).toInt(),
                    (y1 - 10).toInt()
                )
                draw(drawContext.canvas.nativeCanvas)
            }
        }

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
            )
        )
    }
}













