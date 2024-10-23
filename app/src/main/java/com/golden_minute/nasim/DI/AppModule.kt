package com.golden_minute.nasim.DI

import android.app.Application
import com.golden_minute.nasim.data.WeatherResponseServiceImpl
import com.golden_minute.nasim.data.data_store.CoordinateDataStore
import com.golden_minute.nasim.domain.WeatherRequestService
import com.golden_minute.nasim.domain.use_case.AppUseCases
import com.golden_minute.nasim.domain.use_case.GetCoordinate
import com.golden_minute.nasim.domain.use_case.GetWeather
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideHttpClient(): HttpClient {
        return HttpClient(Android) {
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

    @Provides
    fun provideWeatherServiceImpl(
        httpClient: HttpClient,
        coordinateDataStore: CoordinateDataStore
    ): WeatherRequestService {
        return WeatherResponseServiceImpl(httpClient, coordinateDataStore)
    }

    @Provides
    fun getAppUseCases(weatherRequestService: WeatherRequestService): AppUseCases {
        return AppUseCases(GetCoordinate(weatherRequestService), GetWeather(weatherRequestService))
    }

    @Provides
    fun provideCoordinateDataStore(app: Application): CoordinateDataStore {
        return CoordinateDataStore(app)
    }
}