package com.example.weatherapp.Repository

import com.example.weatherapp.Model.WeatherModel

interface Repository {

     suspend fun getWeather(location : String): WeatherModel

}