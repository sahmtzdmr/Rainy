package com.sadikahmetozdemir.rainy.core.service

import com.sadikahmetozdemir.rainy.core.shared.remote.WeatherResponseModel
import com.sadikahmetozdemir.rainy.utils.Constants
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import sadikahmetozdemir.rainy.core.shared.remote.daily.DailyWeatherResponse

interface WeatherAPI {
    @GET("weather")
    suspend fun getForecastData(
        @Query("q") location: String,
        @Query("units") units: String,
        @Query("lang") language: String = Constants.LANGUAGE,
    ): WeatherResponseModel
    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("units") units:String,
        @Query("lang") language: String = Constants.LANGUAGE,
    ): WeatherResponseModel

    @GET("find")
    suspend fun getDailyWeather(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("cnt") cnt: String,
        @Query("units") units: String,
        @Query("lang") language: String = Constants.LANGUAGE,
    ): DailyWeatherResponse
}