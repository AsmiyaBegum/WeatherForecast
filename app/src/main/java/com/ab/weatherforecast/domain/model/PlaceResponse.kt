package com.ab.weatherforecast.domain.model

data class PlaceResponse(
    val placeId: Long,
    val licence: String,
    val osmType: String,
    val osmId: Long,
    val lat: String,
    val lon: String,
    val classType: String,
    val type: String,
    val placeRank: Int,
    val importance: Double,
    val addressType: String,
    val name: String,
    val displayName: String,
    val boundingBox: List<String>
)
