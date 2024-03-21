package com.ab.weatherforecast.data.data_source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ab.weatherforecast.domain.model.FavoriteLocation

@Database(entities = [FavoriteLocation::class], version = 1, exportSchema = true)
abstract class WeatherForecastDatabase : RoomDatabase(){

    abstract val favoriteLocationDao: FavoriteLocationDao

    companion object {
        const val DATABASE_NAME = "weather_forecast_db"
    }

}