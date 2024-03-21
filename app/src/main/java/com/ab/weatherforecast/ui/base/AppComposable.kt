package com.ab.weatherforecast.ui.base

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.ab.weatherforecast.utils.Utils

@Composable
fun AppComposable(
    viewModel: BaseViewModel,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val isLoading by viewModel.loading.observeAsState(initial = false)
    val showSnackBar by viewModel.showSnackBar.observeAsState(initial = Event(""))
    val isNetworkAvailableState = remember { mutableStateOf(
        Utils.checkInternetConnection(context)) }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            content()

        }
        // Check network connectivity
        LaunchedEffect(Unit) {
            while (true) {
                isNetworkAvailableState.value = !Utils.checkInternetConnection(context)
                kotlinx.coroutines.delay(1000) // Check every 1 second
            }
        }
    }
    if (isLoading) {
        Dialog(
            onDismissRequest = {}
        ) {
            Box(
                modifier = Modifier
                    .background(Color.White)
                    .padding(16.dp)
            ) {

                CircularProgressIndicator(
                    modifier = Modifier
                        .size(50.dp)
                        .align(Alignment.Center)
                )
            }
        }
    }

    if (isNetworkAvailableState.value || showSnackBar.content.isNotBlank()) {
        Box{
            Column {
                Spacer(modifier = Modifier.weight(1f))
                Snackbar(modifier = Modifier) {
                    Text(text = "Device Offline, Kindly connect network",
                        color = Color.White)
                }
            }

        }
    }
}