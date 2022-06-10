package com.sadikahmetozdemir.rainy.core.service

import com.sadikahmetozdemir.rainy.core.shared.remote.WeatherResponseModel
import com.sadikahmetozdemir.rainy.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {
    @GET("weather")
    suspend fun getForecastData(
        @Query("q") location: String,
        @Query("lang") language: String = Constants.LANGUAGE,
    ): WeatherResponseModel
}