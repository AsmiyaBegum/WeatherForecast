package com.ab.weatherforecast.data.service

import com.ab.weatherforecast.BuildConfig
import com.ab.weatherforecast.domain.model.AddressResponse
import com.ab.weatherforecast.domain.model.PlaceResponse
import com.ab.weatherforecast.domain.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface AddressService {
    @GET("/reverse")
    suspend fun getAddress(@Query("lat") lat :String, @Query("lon") lng :String,@Query("format") format : String = "jsonv2") : AddressResponse

    @GET("/search")
    suspend fun searchAddress(@Query("q") searchTerm : String,@Query("format") format : String = "jsonv2") : List<PlaceResponse>
}

interface WeatherService {
    @GET("/data/2.5/forecast")
    suspend fun getWeatherDetail(@Query("lat") lat :String, @Query("lon") lng :String,@Query("appid") apiKey : String = BuildConfig.API_KEY,@Query("units") units : String ="metric") : WeatherResponse
}