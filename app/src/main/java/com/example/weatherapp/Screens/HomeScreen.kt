package com.example.weatherapp.Screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.intraverse.viewModels.AuthViewModel

@Composable
fun HomeScreen(modifier: Modifier, navController: NavController, authViewModel: AuthViewModel){

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                navController.navigate("dataEnter")
            }
        ) {
            Text(text = "Enter Data")
        }

        Button(
            onClick = {
                authViewModel.logout()
                navController.navigate("login")
            }
        ) {
            Text(text = "Logout")
        }

        Button(
            onClick = {
                navController.navigate("ResultScreen")
            }
        ) {
            Text(text = "keyword")
        }

        Button(
            onClick = {
                navController.navigate("YoutubeResult")
            }
        ) {
            Text(text = "youtube videos")
        }
    }

}