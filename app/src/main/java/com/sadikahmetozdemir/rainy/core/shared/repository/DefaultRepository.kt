package com.sadikahmetozdemir.rainy.core.shared.repository

import com.sadikahmetozdemir.rainy.core.service.WeatherAPI
import com.sadikahmetozdemir.rainy.core.shared.remote.WeatherResponseModel
import org.intellij.lang.annotations.Language
import sadikahmetozdemir.rainy.core.shared.remote.daily.DailyWeatherResponse
import javax.inject.Inject

class DefaultRepository @Inject constructor(private val weatherAPI: WeatherAPI) : BaseRepository() {

    suspend fun getForecastData(location: String): WeatherResponseModel {
        return execute {
            weatherAPI.getForecastData(location)
        }
    }

    suspend fun getCurrentWeather(lat: String, lon: String): WeatherResponseModel {
        return execute {
            weatherAPI.getCurrentWeather(lat, lon)
        }
    }

    suspend fun getDailyWeather(
        lat: String,
        lon: String,
        cnt: String,
    ): DailyWeatherResponse {
        return execute {
            weatherAPI.getDailyWeather(lat, lon, cnt)
        }
    }

}