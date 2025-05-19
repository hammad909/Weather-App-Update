package com.example.weatherapp.Screens

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Equalizer
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.Navigator
import coil.compose.AsyncImage
import com.example.intraverse.viewModels.AuthViewModel
import com.example.weatherapp.Model.Forecastday
import com.example.weatherapp.Model.User
import com.example.weatherapp.Model.WeatherModel
import com.example.weatherapp.viewModels.WeatherViewModel
import kotlinx.coroutines.launch

private const val REQUEST_CODE_LOCATION_PERMISSION = 1001

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun HomeScreen(
    modifier: Modifier,
    weatherViewModel: WeatherViewModel,
    authViewModel: AuthViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    val weatherResult = weatherViewModel.weather.collectAsState()
    val authInfo = authViewModel._user.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    val permissionState = remember {
        mutableStateOf(
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    // Request permission if not granted
    if (!permissionState.value) {
        LaunchedEffect(Unit) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE_LOCATION_PERMISSION
            )
        }
    }

    // Recheck permission on each recomposition
    LaunchedEffect(Unit) {
        val granted = ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (granted && !weatherResult.value?.location?.name.isNullOrEmpty().not()) {
            val userLocation = authViewModel.getUserLocation(context)
            if (userLocation != null) {
                weatherViewModel.fetchWeather(userLocation)
            }
        }

        permissionState.value = granted
    }

    MainUi(weatherResult, user = authInfo, navController)
}


@Composable
fun MainUi(weatherReuslt: State<WeatherModel?>, user: State<User?>, navController : NavController){
    // Layout UI
    Column(modifier = Modifier.fillMaxWidth().padding(8.dp) .padding(top = 20.dp)
        , horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Welcome home,",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = Color.LightGray,
                        fontSize = 12.sp
                    )
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = user.value?.name ?: "User Don't have a name",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
            }

            Icon(
                imageVector = Icons.Default.Equalizer,
                contentDescription = "Menu or Indicator",
                tint = Color.LightGray,
                modifier = Modifier.size(24.dp)
                    .clickable {
                          navController.navigate("userInfo")
                        Log.d("IconClick", "Equalizer icon clicked")
                    }
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Location Icon",
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.width(8.dp)) // spacing between icon and text

            Column {
                Text(
                    text = weatherReuslt.value?.location?.name ?: "No location found",
                    fontSize = 30.sp
                )
                Text(
                    text = weatherReuslt.value?.location?.country ?: "",
                    fontSize = 12.sp,
                    color = Color.LightGray
                )
            }
        }


        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .height(150.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF2C2C2C)),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        text = weatherReuslt.value?.current?.last_updated ?:"no data found",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color.LightGray,
                            fontSize = 12.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = weatherReuslt.value?.current?.condition?.text ?: "No data",
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.White)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "${weatherReuslt.value?.current?.temp_c?.toInt() ?: "--"}ºC",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
                AsyncImage(
                    model = "https:${weatherReuslt.value?.current?.condition?.icon}",
                    contentDescription = "Weather Icon",
                    modifier = Modifier.size(60.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2A2A)),
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)               // increase height a little
                .padding(horizontal = 21.dp, vertical = 16.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center   // vertically and horizontally center the Row
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    WeatherInfoItem(
                        icon = Icons.Filled.Air,
                        label = "Wind",
                        value = "${weatherReuslt.value?.current?.wind_kph} kph"
                    )
                    WeatherInfoItem(
                        icon = Icons.Filled.Opacity,
                        label = "Humidity",
                        value = "${weatherReuslt.value?.current?.humidity}%"
                    )
                    WeatherInfoItem(
                        icon = Icons.Filled.Cloud,
                        label = "Rain",
                        value = "${weatherReuslt.value?.current?.precip_mm} mm"
                    )
                }
            }
        }



        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Next 3 days",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val forecastDays = weatherReuslt.value?.forecast?.forecastday ?: emptyList()
            Log.d("WeatherCheck", "Days received: ${forecastDays.size}")
            items(forecastDays.size) { index ->
                val day = forecastDays[index]
                WeeklyWeatherItem(forecastDay = day)
            }
        }

    }

}

@Composable
fun WeatherInfoItem(icon: ImageVector, label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(vertical = 8.dp)  // adds space top and bottom
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color.White,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = value,
            color = Color.White,
            fontSize = 18.sp,
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = label,
            color = Color.LightGray,
            fontSize = 14.sp,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun WeeklyWeatherItem(forecastDay: Forecastday) {
    Card(
        modifier = Modifier
            .width(120.dp)
            .height(160.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2A2A)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            // Date
            Text(
                text = forecastDay.date,
                color = Color.White,
                fontSize = 14.sp
            )

            // Weather Icon
            AsyncImage(
                model = "https:${forecastDay.day.condition.icon}",
                contentDescription = "Weather Icon",
                modifier = Modifier.size(48.dp)
            )

            // Temperature
            Text(
                text = "${forecastDay.day.avgtemp_c}°C",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


