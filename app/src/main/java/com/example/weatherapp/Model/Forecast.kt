package com.example.weatherapp.Model

import kotlinx.serialization.Serializable

@Serializable
data class Forecast(
    val forecastday: List<Forecastday>
)