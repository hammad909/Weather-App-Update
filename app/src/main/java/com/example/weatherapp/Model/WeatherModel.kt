package com.example.weatherapp.Model

import kotlinx.serialization.Serializable

@Serializable
data class WeatherModel(
    val current: Current,
    val forecast: Forecast,
    val location: Location
)