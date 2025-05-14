package com.example.weatherapp.Screens

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import com.example.intraverse.viewModels.AuthViewModel
import com.example.weatherapp.Model.WeatherModel
import com.example.weatherapp.viewModels.WeatherViewModel
import kotlinx.coroutines.launch

private const val REQUEST_CODE_LOCATION_PERMISSION = 1001

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun HomeScreen(modifier: Modifier, weatherViewModel: WeatherViewModel, authViewModel: AuthViewModel) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val permissionGranted = remember { mutableStateOf(false) }

    val weatherResult = weatherViewModel.weather.collectAsState()

    // Check if the location permission is granted
    val isPermissionGranted = ActivityCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    if (!isPermissionGranted && !permissionGranted.value) {
        // Request permission if it's not granted
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_CODE_LOCATION_PERMISSION
        )
        permissionGranted.value = true
    }

    // Fetch weather after getting permission and location
    if (isPermissionGranted) {
        coroutineScope.launch {
            val userLocation = authViewModel.getUserLocation(context)
            if (userLocation != null) {
                Log.d("UserLocation", "Location: $userLocation")
                weatherViewModel.fetchWeather(userLocation)
                Log.d("data", weatherViewModel.fetchWeather(userLocation).toString())
            } else {
                Log.d("UserLocation", "Unable to fetch location.")
            }
        }
    } else {
        Log.d("UserLocation", "Permission not granted")
    }

    MainUi(weatherResult)

}

@Composable
fun MainUi(weatherReuslt: State<WeatherModel?>){
    // Layout UI
    Column(modifier = Modifier.fillMaxWidth().padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Location Icon",
                modifier = Modifier.size(40.dp)
            )
            Log.d("WeatherAPI", "Weather data: ${weatherReuslt.value}")
            Text(weatherReuslt.value?.location?.name?: "No location found", fontSize = 30.sp)
        }
    }
}