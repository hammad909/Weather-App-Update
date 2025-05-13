package com.example.weatherapp.di

import com.example.weatherapp.Repository.Repository
import com.example.weatherapp.Repository.RepositoryImplementation
import com.example.weatherapp.viewModels.WeatherViewModel
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val appModule = module {

    single {  HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }
    }

    single<String> { "d0d68e814dcd427c80a113156251105" }

    single<Repository> { RepositoryImplementation(get(), get()) }

    viewModel { WeatherViewModel(get()) }

}
