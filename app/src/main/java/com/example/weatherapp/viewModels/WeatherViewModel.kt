package com.example.weatherapp.viewModels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.Model.WeatherModel
import com.example.weatherapp.Repository.RepositoryImplementation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class WeatherViewModel(private val repository: RepositoryImplementation) : ViewModel() {

        private val _weather = MutableStateFlow<WeatherModel?>(null)
        val weather: StateFlow<WeatherModel?> = _weather

        private val _isLoading = MutableStateFlow(false)
        val isLoading: StateFlow<Boolean> = _isLoading

        private val _error = MutableStateFlow<String?>(null)
        val error: StateFlow<String?> = _error

        fun fetchWeather(location: String) {
            Log.d("area", "executed within view model")
            viewModelScope.launch {
                _isLoading.value = true
                _error.value = null
                try {
                    _weather.value = repository.getWeather(location)
                    Log.d("area2", "log 2")
                } catch (e: IOException) {
                    _error.value = e.message
                 /*   throw(e)*/
                } finally {
                    _isLoading.value = false
                }
            }
        }
    }

