package com.sadikahmetozdemir.rainy.core.service

import com.sadikahmetozdemir.rainy.core.shared.remote.WeatherResponseModel
import com.sadikahmetozdemir.rainy.utils.Constants
import retrofit2.http.GET
import kotlinx.coroutines.Deferred
import retrofit2.http.Query

interface WeatherAPI {
    @GET("weather")
    fun getForecastData(
        @Query("q") location: String,
        @Query("lang") language: String = Constants.LANGUAGE
    ): Deferred<WeatherResponseModel>
}