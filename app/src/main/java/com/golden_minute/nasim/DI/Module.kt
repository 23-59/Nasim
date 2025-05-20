package com.golden_minute.nasim.DI

import com.golden_minute.nasim.data.data_store.CoordinateDataStore
import com.golden_minute.nasim.data.repository.WeatherResponseServiceImpl
import com.golden_minute.nasim.domain.repository.WeatherRequestService
import com.golden_minute.nasim.domain.use_case.AppUseCases
import com.golden_minute.nasim.domain.use_case.GetSearchedCitiesInfo
import com.golden_minute.nasim.domain.use_case.GetWeather
import com.golden_minute.nasim.presentation.main.ActivityViewModel
import com.golden_minute.nasim.presentation.onboarding.WelcomeScreenViewModel
import com.golden_minute.nasim.presentation.search.SearchScreenViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel<ActivityViewModel> { ActivityViewModel(androidApplication(), get(), get()) }

    viewModel<WelcomeScreenViewModel> { WelcomeScreenViewModel(androidApplication(), get(), get()) }

    viewModel<SearchScreenViewModel> { SearchScreenViewModel(get()) }

    single<CoordinateDataStore> { CoordinateDataStore(androidApplication()) }

    single<AppUseCases> { AppUseCases(GetSearchedCitiesInfo(get()), GetWeather(get())) }

    factory<WeatherRequestService> { WeatherResponseServiceImpl(get(), get()) }


    factory<HttpClient> {
        HttpClient(Android) {
            install(Logging) {
                level = LogLevel.ALL

            }
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }

            install(HttpTimeout) {
                requestTimeoutMillis = 20000L
                connectTimeoutMillis = 10000L
                socketTimeoutMillis = 10000L

            }
        }
    }
}