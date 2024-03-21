package com.ab.weatherforecast.domain.usecase

import com.ab.weatherforecast.domain.model.AddressResponse
import com.ab.weatherforecast.domain.model.FavoriteLocation
import com.ab.weatherforecast.domain.model.PlaceResponse
import com.ab.weatherforecast.domain.model.WeatherResponse
import com.ab.weatherforecast.domain.repository.AddressRepository
import com.ab.weatherforecast.domain.repository.FavoriteLocationRepository
import com.ab.weatherforecast.domain.repository.WeatherRepository
import javax.inject.Inject

class MapScreenUseCase @Inject constructor(private val addressRepository : AddressRepository, private  val weatherRepository: WeatherRepository,private  val favoriteLocationRepository: FavoriteLocationRepository) {

    suspend fun getAddress(lat : String,lng : String) : Result<AddressResponse> {
        return addressRepository.getAddress(lat,lng)
    }

    suspend fun getWeather(lat: String,lng : String) : Result<WeatherResponse> {
        return weatherRepository.getWeatherDetails(lat,lng)
    }

    suspend fun searchPlace(searchTerm : String) : Result<List<PlaceResponse>> {
        return addressRepository.searchPlace(searchTerm)
    }

    suspend fun saveFavoriteLocation(favoriteLocation: FavoriteLocation) {
        return favoriteLocationRepository.saveFavoriteLocation(favoriteLocation)
    }

    suspend fun getFavoriteLocation() : List<FavoriteLocation> {
        return favoriteLocationRepository.getFavoriteLocations()
    }
}