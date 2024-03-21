package com.ab.weatherforecast.ui.composables

import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
fun BackPressHandler(
    enabled: Boolean = true,
    onBackPressed: () -> Unit
) {
    val context = LocalContext.current
    val dispatcher = remember(context) {
        context.getSystemService(OnBackPressedDispatcher::class.java)
    }

    val backCallback = remember {
        object : OnBackPressedCallback(enabled) {
            override fun handleOnBackPressed() {
                onBackPressed()
            }
        }
    }

    DisposableEffect(dispatcher) {
        dispatcher.addCallback(backCallback)
        onDispose {
            backCallback.remove()
        }
    }
}
