package com.example.weatherapp.viewModels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.Model.WeatherModel
import com.example.weatherapp.Repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class WeatherViewModel(private val repository: Repository) : ViewModel() {

        private val _weather = MutableStateFlow<WeatherModel?>(null)
        val weather: StateFlow<WeatherModel?> = _weather

        private val _isLoading = MutableStateFlow(false)
        val isLoading: StateFlow<Boolean> = _isLoading

        private val _error = MutableStateFlow<String?>(null)
        val error: StateFlow<String?> = _error

        fun fetchWeather(location: String) {
            viewModelScope.launch {
                _isLoading.value = true
                _error.value = null
                try {
                    _weather.value = repository.getWeather(location)
                } catch (e: IOException) {
                    _error.value = e.message
                } finally {
                    _isLoading.value = false
                }
            }
        }
    }

