package com.sadikahmetozdemir.rainy.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sadikahmetozdemir.rainy.base.BaseViewModel
import com.sadikahmetozdemir.rainy.core.shared.remote.WeatherResponseModel
import com.sadikahmetozdemir.rainy.core.shared.repository.DefaultRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private var defaultRepository: DefaultRepository) :
    BaseViewModel() {
    private val _weather: MutableLiveData<WeatherResponseModel> = MutableLiveData()
    val weather: LiveData<WeatherResponseModel> get() = _weather

    fun getCurrentWeather(location: String) {
        sendRequest(
            request = { defaultRepository.getCurrentWeather(location) },
            success = {
                _weather.value = it
            },
            error = {
                it
            }
        )

    }


}