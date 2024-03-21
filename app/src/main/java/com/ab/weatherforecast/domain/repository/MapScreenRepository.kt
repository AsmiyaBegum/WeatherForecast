package com.ab.weatherforecast.domain.repository

import com.ab.weatherforecast.domain.model.AddressResponse
import com.ab.weatherforecast.domain.model.FavoriteLocation
import com.ab.weatherforecast.domain.model.PlaceResponse
import com.ab.weatherforecast.domain.model.WeatherResponse

interface AddressRepository {

    suspend fun getAddress(lat : String, lng : String) : Result<AddressResponse>

    suspend fun searchPlace(searchTerm : String) : Result<List<PlaceResponse>>

}

interface WeatherRepository {

    suspend fun getWeatherDetails(lat: String, lng: String) : Result<WeatherResponse>

}

interface FavoriteLocationRepository {

    suspend fun saveFavoriteLocation(favoriteLocation: FavoriteLocation)

    suspend fun getFavoriteLocations() : List<FavoriteLocation>
}