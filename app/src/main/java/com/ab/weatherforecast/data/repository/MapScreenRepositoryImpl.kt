package com.ab.weatherforecast.data.repository

import com.ab.weatherforecast.data.data_source.FavoriteLocationDao
import com.ab.weatherforecast.data.service.AddressService
import com.ab.weatherforecast.data.service.WeatherService
import com.ab.weatherforecast.domain.model.AddressResponse
import com.ab.weatherforecast.domain.model.FavoriteLocation
import com.ab.weatherforecast.domain.model.PlaceResponse
import com.ab.weatherforecast.domain.model.WeatherResponse
import com.ab.weatherforecast.domain.repository.AddressRepository
import com.ab.weatherforecast.domain.repository.FavoriteLocationRepository
import com.ab.weatherforecast.domain.repository.WeatherRepository
import javax.inject.Inject

class AddressRepositoryImpl @Inject constructor(private val addressService : AddressService) : AddressRepository {

    override suspend fun getAddress(lat: String, lng: String): Result<AddressResponse> {
        val result = addressService.getAddress(lat,lng)
        return Result.success(result)
    }

    override suspend fun searchPlace(searchTerm: String): Result<List<PlaceResponse>> {
        val result = addressService.searchAddress(searchTerm)
        return Result.success(result)
    }
}

class WeatherRepositoryImpl @Inject constructor(private val weatherService: WeatherService) : WeatherRepository {

    override suspend fun getWeatherDetails(lat: String, lng: String): Result<WeatherResponse> {
        val result = weatherService.getWeatherDetail(lat,lng)
        return Result.success(result)
    }

}

class FavoriteLocationRepositoryImpl @Inject constructor(private val dao: FavoriteLocationDao) : FavoriteLocationRepository {

    override suspend fun saveFavoriteLocation(favoriteLocation: FavoriteLocation) {
        dao.insertFavoriteLocation(favoriteLocation)
    }

    override suspend fun getFavoriteLocations(): List<FavoriteLocation> {
       return dao.getFavoriteLocations()
    }
}

