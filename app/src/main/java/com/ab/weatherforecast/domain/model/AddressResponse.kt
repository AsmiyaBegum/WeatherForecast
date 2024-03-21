package com.ab.weatherforecast.domain.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class AddressResponse(
    val name: String?,
    val address: Address?,
)

data class Address(
    val road: String?,
    val country: String?,
)

@Parcelize
data class WeatherInfo(
    val location : String?,
    val lat : String,
    val long : String,
    val weather : List<WeatherItem>
) : Parcelable

@Parcelize
data class WeatherDummy(
    val location : String?,
    val lat : String,
    val long : String,
) : Parcelable


data class WeatherMetrics(
    val humidity : String,
    val feelLike : String,
    val pressure : String,
    val wind : String
)
