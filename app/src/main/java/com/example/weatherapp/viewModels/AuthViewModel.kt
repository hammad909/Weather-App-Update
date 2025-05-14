package com.example.intraverse.viewModels

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.Model.User
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.tasks.await
import java.security.MessageDigest
import java.util.UUID
import android.Manifest
import android.location.Geocoder
import android.location.Location
import androidx.navigation.NavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.Locale


class AuthViewModel() : ViewModel() {

    private val user = MutableLiveData<User?>(null)

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error


    init {
        checkAuthStatus()
    }

    private fun checkAuthStatus() {
        _authState.value = _authState.value.copy(
            isAuthenticated = auth.currentUser != null
        )
    }

    fun login(email: String, password: String) {

        if (email.isBlank() || password.isBlank()) {
            _authState.value = _authState.value.copy(
                errorMessage = "Email and password must not be empty"
            )
            return
        }


        _authState.value = _authState.value.copy(
            isLoading = true,
            errorMessage = null
        )
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = _authState.value.copy(
                        isLoading = false,
                        isAuthenticated = true
                    )
                } else {
                    _authState.value = _authState.value.copy(
                        isLoading = false,
                        errorMessage = task.exception?.message ?: "Something went Wrong"
                    )

                }
            }

    }

    fun Signup(email: String, password: String) {

        if (email.isBlank() || password.isBlank()) {
            _authState.value = _authState.value.copy(
                errorMessage = "Email and password must not be empty"
            )
            return
        }


        _authState.value = _authState.value.copy(
            isLoading = true,
            errorMessage = null
        )
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = _authState.value.copy(
                        isLoading = false,
                        isAuthenticated = true
                    )
                } else {
                    _authState.value = _authState.value.copy(
                        isLoading = false,
                        errorMessage = task.exception?.message ?: "Something went Wrong"
                    )

                }

            }
    }

    fun handleGoogleSignIn(
        context: Context) {
        viewModelScope.launch {
            googleSignIn(context).collect { result ->
                result.fold(
                    onSuccess = { authResult ->
                        val currentUser = authResult.user
                        if (currentUser != null) {
                            user.value = User(
                                id = currentUser.uid,
                                name = currentUser.displayName!!,
                                photoUrl = currentUser.photoUrl.toString(),
                                email = currentUser.email!!
                            )

                            _authState.value = _authState.value.copy(
                                isLoading = false,
                                isAuthenticated = true
                            )

                            Toast.makeText(
                                context,
                                "Account created successfully!",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    },
                    onFailure = { e ->
                        Toast.makeText(
                            context,
                            "Something went wrong: ${e.message}",
                            Toast.LENGTH_LONG
                        ).show()
                        Log.d("Issue", "handleGoogleSignIn: ${e.message}")
                    }
                )
            }
        }
    }

    //getting Credentials with oneTapClient
    private fun googleSignIn(context: Context): Flow<Result<AuthResult>> {
        return callbackFlow {
            try {
                val credentialManager: CredentialManager = CredentialManager.create(context)

                val ranNonce: String = UUID.randomUUID().toString()
                val bytes: ByteArray = ranNonce.toByteArray()
                val md: MessageDigest = MessageDigest.getInstance("SHA-256")
                val digest: ByteArray = md.digest(bytes)
                val hashedNonce: String = digest.fold("") { str, it -> str + "%02x".format(it) }

                val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId("679088799537-ucdfj2ar1qmjetvbgnkhc5g8en798vf8.apps.googleusercontent.com")
                    .setNonce(hashedNonce)
                    .setAutoSelectEnabled(true)
                    .build()

                val request: GetCredentialRequest = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

                val result = credentialManager.getCredential(context, request)
                val credential = result.credential

                if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    val googleIdTokenCredential =
                        GoogleIdTokenCredential.createFrom(credential.data)
                    val authCredential =
                        GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)
                    val authResult = auth.signInWithCredential(authCredential).await()
                    trySend(Result.success(authResult))
                } else {
                    throw RuntimeException("Received an invalid credential type.")
                }

            } catch (e: GetCredentialCancellationException) {
                Log.d("Deadpan", "GoogleError! $e")
                trySend(Result.failure(Exception("Sign-in was canceled.")))
            } catch (e: Exception) {
                Log.d("Deadpan", "GoogleError! $e")
                trySend(Result.failure(e))
            }
            awaitClose { }
        }
    }

    //get user location
    suspend fun getUserLocation(context: Context): String? {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("UserLocation", "Permission not granted")
            return null
        }

        val fusedLocationClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(context)

        return try {
            val location: Location? = fusedLocationClient.lastLocation.await()

            if (location != null) {
                val latitude = location.latitude
                val longitude = location.longitude

                Log.d("UserLocation", "Latitude: $latitude, Longitude: $longitude")

                // Return in "lat,lon" format for WeatherAPI
                "$latitude,$longitude"
            } else {
                Log.d("UserLocation", "Location is null")
                null
            }
        } catch (e: Exception) {
            Log.e("UserLocation", "Error: ${e.message}")
            null
        }
    }



    fun logout() {
        auth.signOut()
        _authState.value = _authState.value.copy(isAuthenticated = false)
        signOut()
    }


    //signOut of user
    private fun signOut() {
        viewModelScope.launch {
            user.value = null
        }
    }

}




data class AuthState(
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val errorMessage: String? = null
)
