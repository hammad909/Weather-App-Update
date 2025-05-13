package com.example.weatherapp.Repository

import com.example.weatherapp.Model.WeatherModel
import io.ktor.client.statement.HttpResponse

interface Repository {

     suspend fun getWeather(location : String): WeatherModel

}