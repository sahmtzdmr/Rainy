package com.sadikahmetozdemir.rainy.core.shared.repository

import com.sadikahmetozdemir.rainy.core.service.WeatherAPI
import javax.inject.Inject

class DefaultRepository @Inject constructor(private val weatherAPI: WeatherAPI) : BaseRepository() {

}