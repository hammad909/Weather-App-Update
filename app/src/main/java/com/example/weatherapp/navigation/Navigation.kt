package com.example.intraverse.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.intraverse.viewModels.AuthViewModel
import com.example.weatherapp.Screens.HomeScreen
import com.example.weatherapp.Screens.LoginScreen
import com.example.weatherapp.Screens.RegistrationScreen
import com.example.weatherapp.Screens.UserinfoScreen
import com.example.weatherapp.viewModels.WeatherViewModel


@Composable
fun MyAppNavigation(modifier: Modifier = Modifier,authViewModel: AuthViewModel, weatherViewModel: WeatherViewModel){


    val navController = rememberNavController()


    val authState by authViewModel.authState.collectAsState()

    val startDestination = if (authState.isAuthenticated) "home" else "login"
    NavHost(navController = navController, startDestination = startDestination) {

        composable("login"){
            LoginScreen(modifier,navController,authViewModel)
        }
        composable("signup"){
            RegistrationScreen(modifier,navController,authViewModel)
        }
        composable("home"){
            HomeScreen(modifier,weatherViewModel,authViewModel,navController)
        }

        composable("userInfo"){
            UserinfoScreen(navController,authViewModel)
        }


    }
}

