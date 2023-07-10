package com.sadikahmetozdemir.rainy.core.shared.repository

import com.sadikahmetozdemir.rainy.core.service.WeatherAPI
import com.sadikahmetozdemir.rainy.core.shared.remote.WeatherResponseModel
import sadikahmetozdemir.rainy.core.shared.remote.daily.DailyWeatherResponse
import javax.inject.Inject

class DefaultRepository @Inject constructor(private val weatherAPI: WeatherAPI) : BaseRepository() {

    suspend fun getForecastData(location: String,units: String): WeatherResponseModel {
        return execute {
            weatherAPI.getForecastData(location,units)
        }
    }

    suspend fun getCurrentWeather(lat: String, lon: String, units: String): WeatherResponseModel {
        return execute {
            weatherAPI.getCurrentWeather(lat, lon,units)
        }
    }

    suspend fun getDailyWeather(
        lat: String,
        lon: String,
        cnt: String,
        units: String
    ): DailyWeatherResponse {
        return execute {
            weatherAPI.getDailyWeather(lat, lon, cnt,units)
        }
    }

}