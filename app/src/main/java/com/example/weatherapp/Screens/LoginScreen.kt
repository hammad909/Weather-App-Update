package com.example.weatherapp.Screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.intraverse.viewModels.AuthViewModel

@Composable
fun LoginScreen(modifier: Modifier,navController: NavController,authViewModel: AuthViewModel){

    val context = LocalContext.current
    val authState by authViewModel.authState.collectAsState()

    LaunchedEffect(authState.isAuthenticated) {
        if (authState.isAuthenticated) {
            navController.navigate("home") {
                popUpTo("login") { inclusive = true }
            }
        }
    }

    var email by remember {
        mutableStateOf("")
    }


    var password by remember {
        mutableStateOf("")
    }

    Column (modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally)
    {

        Text(text = "Login Page", fontSize = 32.sp)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = email,
            onValueChange = { email = it },
            label = {
                Text(text = "enter email")
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(value = password,
            onValueChange = { password = it },
            label = {
                Text(text = "enter password")
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            authViewModel.login(email,password)
        }) {
            Text(text = "Login")
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = {
            navController.navigate("signup")
        }) {
            Text(text = "don't have an account, Signup")
        }

        Button(onClick = {
            authViewModel.handleGoogleSignIn(context = context)
            if(authState.isAuthenticated){
                navController.navigate("home")
            }
        }) {
            Text(text = "loginWithGoogleId")
        }
    }
}