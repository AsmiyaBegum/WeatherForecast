package com.ab.weatherforecast.ui.mapscreen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ab.weatherforecast.domain.model.AddressResponse
import com.ab.weatherforecast.domain.model.FavoriteLocation
import com.ab.weatherforecast.domain.model.PlaceResponse
import com.ab.weatherforecast.domain.model.WeatherInfo
import com.ab.weatherforecast.domain.model.WeatherResponse
import com.ab.weatherforecast.domain.usecase.MapScreenUseCase
import com.ab.weatherforecast.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapScreenViewModel @Inject constructor(private val usecase : MapScreenUseCase) : BaseViewModel(){

    private val _weatherDetail : MutableLiveData<WeatherInfo> = MutableLiveData()
    val weatherDetail : LiveData<WeatherInfo>
        get() = _weatherDetail

    private val _placeResponse : MutableLiveData<List<PlaceResponse>> = MutableLiveData()
    val placeResponse : LiveData<List<PlaceResponse>>
        get() = _placeResponse

    private val _favoriteLocations : MutableLiveData<List<FavoriteLocation>> = MutableLiveData()
    val favoriteLocation : LiveData<List<FavoriteLocation>>
        get() = _favoriteLocations

    private val searchQueryFlow = MutableSharedFlow<String>()

    init {
        getPlaceSearch()
    }

    fun getFavoriteLocation() {
        viewModelScope.launch(Dispatchers.IO){
            _favoriteLocations.postValue(usecase.getFavoriteLocation())
        }
    }

    fun bookmarkFavoriteLocation(weatherInfo: WeatherInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            if(weatherInfo?.lat?.isNotBlank() == true) {
                val favoriteLocation = FavoriteLocation(lat = weatherInfo.lat, lon =  weatherInfo.long, locationName = weatherInfo.location?:"")
                usecase.saveFavoriteLocation(favoriteLocation)
            }
        }
    }

    fun clearWeatherDetail(){
        _weatherDetail.postValue(null)
    }

    fun clearPlaceResponse(){
        _placeResponse.postValue(listOf())
    }

    fun setSearchQuery(searchTerm: String) {
        viewModelScope.launch {
            searchQueryFlow.emit(searchTerm)
        }
    }


    private fun getPlaceSearch() {
        viewModelScope.launch(Dispatchers.IO) {
            searchQueryFlow
                .debounce(300)
                .collect { searchTerm ->
                    usecase.searchPlace(searchTerm.replace(" ","+")).onSuccess {
                        _placeResponse.postValue(
                            it.take(5)
                        )
                    }.onFailure { error->
                        Log.e("Error",error.stackTraceToString())
                    }
                }

        }
    }

    fun getWeatherAndAddress(lat: String, lng: String) {
            call(
                apiCalls = listOf(
                    { usecase.getAddress(lat, lng) },
                    { usecase.getWeather(lat, lng) }
                ),
                onSuccess = { results ->
                    val addressResult = results[0] as AddressResponse
                    val weatherResult = results[1] as WeatherResponse

                    val weatherInfo = WeatherInfo(
                        location = addressResult.name?.ifBlank { addressResult.address?.road?:addressResult.address?.country },
                        lat = lat,
                        long = lng,
                        weather = weatherResult.list
                    )

                    _weatherDetail.postValue( weatherInfo)

                },
                onError = { error ->
                    Log.e("Error",error.stackTraceToString())

                })
    }


}