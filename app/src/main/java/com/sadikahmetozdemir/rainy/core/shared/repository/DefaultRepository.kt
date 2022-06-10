package com.sadikahmetozdemir.rainy.core.shared.repository

import com.sadikahmetozdemir.rainy.core.service.WeatherAPI
import com.sadikahmetozdemir.rainy.core.shared.remote.WeatherResponseModel
import javax.inject.Inject

class DefaultRepository @Inject constructor(private val weatherAPI: WeatherAPI) : BaseRepository() {

    suspend fun getCurrentWeather(location: String): WeatherResponseModel {
        return execute {
            weatherAPI.getForecastData(location)
        }
    }

}