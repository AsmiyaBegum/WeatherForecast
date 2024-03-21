package com.ab.weatherforecast.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ab.weatherforecast.domain.model.WeatherInfo
import com.ab.weatherforecast.ui.mapscreen.MapScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherView(weatherDetail : WeatherInfo,viewModel: MapScreenViewModel,navController: NavController,onDismiss : () -> (Unit),onExpandClick : (WeatherInfo) -> (Unit)){

    ModalBottomSheet(
        sheetState = rememberModalBottomSheetState(),
        modifier = Modifier.height(400.dp),
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        scrimColor = Color.Black.copy(alpha = 0.4f),
        contentColor = MaterialTheme.colorScheme.surface,
        onDismissRequest = {
            onDismiss.invoke()
        }
    ) {

            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                BottomContent(weatherDetail,navController,false,viewModel){
                    onExpandClick(it)
                    onDismiss.invoke()
                }
            }
    }
}