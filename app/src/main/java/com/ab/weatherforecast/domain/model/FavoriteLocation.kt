package com.ab.weatherforecast.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavoriteLocation (
    @PrimaryKey(autoGenerate = true) val id : Long = 0,
    var lat : String = "",
    var lon : String = "",
    var locationName : String = ""
)