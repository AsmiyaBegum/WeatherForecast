package com.ab.weatherforecast.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


fun String.time() : String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val date = inputFormat.parse(this)
    val outputFormat = SimpleDateFormat("HH:mm a", Locale.getDefault())
   return outputFormat.format(date)
}
