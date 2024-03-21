package com.ab.weatherforecast.ui.weatherDetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ab.weatherforecast.R
import com.ab.weatherforecast.domain.model.WeatherInfo
import com.ab.weatherforecast.ui.composables.BottomContent
import com.ab.weatherforecast.ui.composables.changeDateFormat
import com.ab.weatherforecast.ui.composables.formatCalendarDate
import com.ab.weatherforecast.utils.roundTo
import java.util.Calendar

@Composable
fun WeatherDetail(weatherInfo: WeatherInfo,navController: NavController){
    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        BottomContent(weatherInfo,navController, iFullScreen = true){

        }
    }
}


