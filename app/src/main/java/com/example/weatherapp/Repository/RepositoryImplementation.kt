package com.example.weatherapp.Repository

import com.example.weatherapp.Model.WeatherModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import java.io.IOException

class RepositoryImplementation(
    private val httpClient: HttpClient,
    private val apiKey: String
) : Repository {

    override suspend fun getWeather(location: String): WeatherModel {
        return try {
            httpClient.get("https://api.weatherapi.com/v1/current.json") {
                parameter("key", apiKey)
                parameter("q", location)
            }.body()
        } catch (e: Exception) {
            throw IOException("Failed to fetch weather data: ${e.message}", e)
        }
    }
}
