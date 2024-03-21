package com.ab.weatherforecast.utils

import android.content.Context
import android.net.ConnectivityManager
import androidx.core.content.ContextCompat.getSystemService
import com.ab.weatherforecast.WeatherForecastApp
import com.ab.weatherforecast.domain.model.Weather
import com.ab.weatherforecast.domain.model.WeatherDummy
import com.ab.weatherforecast.domain.model.WeatherInfo
import com.google.gson.Gson

object Utils {

    internal fun checkInternetConnection(context: Context): Boolean {
        val connectivityManager =context
            ?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val isConnected = connectivityManager.activeNetworkInfo?.isConnected
        return isConnected ?: false
    }

}

class WeatherInfoArgType : JsonNavType<WeatherInfo>() {
    override fun fromJsonParse(value: String): WeatherInfo = Gson().fromJson(value, WeatherInfo::class.java)
    override fun WeatherInfo.getJsonParse(): String =  Gson().toJson(this)
}

fun Double.roundTo(decimals: Int): Double {
    return "%.${decimals}f".format(this).toDouble()
}

