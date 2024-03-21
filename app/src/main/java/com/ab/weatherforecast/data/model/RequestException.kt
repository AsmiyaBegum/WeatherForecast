package com.ab.weatherforecast.data.model

class RequestException(val code: Int, message: String) : Throwable(message)