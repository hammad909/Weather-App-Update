package com.example.weatherapp.Screens

import android.widget.Toast
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
fun RegistrationScreen(modifier: Modifier, navController: NavController, authViewModel: AuthViewModel){

    var email by remember {
        mutableStateOf("")
    }


    var password by remember {
        mutableStateOf("")
    }

    val authState by authViewModel.authState.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(authState.isAuthenticated) {
        if (authState.isAuthenticated) {
            navController.navigate("home") {
                popUpTo("signup") { inclusive = true }
                popUpTo("login") { inclusive = true }
            }

        }
    }

    LaunchedEffect(authState.errorMessage) {
        authState.errorMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }

    Column (modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally)
    {

        Text(text = "Signup Page", fontSize = 32.sp)

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
            authViewModel.Signup(email,password)
            if(authState.isAuthenticated){
                navController.navigate("home")
            }
        }) {
            Text(text = "SignUp")
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = {
            navController.navigate("login")
        }) {
            Text(text = "Already have an account, Login")
        }


    }
}