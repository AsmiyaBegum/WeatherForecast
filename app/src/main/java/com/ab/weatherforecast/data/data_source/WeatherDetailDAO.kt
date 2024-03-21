package com.ab.weatherforecast.data.data_source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ab.weatherforecast.domain.model.FavoriteLocation

@Dao
interface FavoriteLocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteLocation(favoriteLocation: FavoriteLocation)

    @Query("SELECT * FROM FavoriteLocation")
    fun getFavoriteLocations() : List<FavoriteLocation>

}