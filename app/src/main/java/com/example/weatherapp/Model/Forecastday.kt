package com.example.weatherapp.Model

import kotlinx.serialization.Serializable

@Serializable
data class Forecastday(
    val astro: Astro,
    val date: String,
    val date_epoch: Int,
    val day: Day,
    val hour: List<Hour>
)