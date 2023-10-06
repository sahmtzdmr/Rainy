package com.sadikahmetozdemir.rainy.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.sadikahmetozdemir.rainy.base.BaseViewEvent
import com.sadikahmetozdemir.rainy.base.BaseViewModel
import com.sadikahmetozdemir.rainy.core.shared.remote.WeatherResponseModel
import com.sadikahmetozdemir.rainy.core.shared.repository.DefaultRepository
import com.sadikahmetozdemir.rainy.utils.Constants
import com.sadikahmetozdemir.rainy.utils.DataHelperManager
import com.sadikahmetozdemir.rainy.utils.SharedPreferenceStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import sadikahmetozdemir.rainy.core.shared.remote.daily.DailyWeatherResponse
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private var defaultRepository: DefaultRepository,
    savedStateHandle: SavedStateHandle
) :
    BaseViewModel() {
    val lat: String = savedStateHandle.get<String>(LAT) ?: ""
    val lon: String = savedStateHandle.get<String>(LON) ?: ""
    val cnt = CNT
    val etCity = MutableLiveData("")
    private val _weather: MutableLiveData<WeatherResponseModel> = MutableLiveData()
    val weather: LiveData<WeatherResponseModel> get() = _weather
    private val _dailyWeather: MutableLiveData<List<DailyWeatherResponse>> = MutableLiveData()
    val dailyWeather: LiveData<List<DailyWeatherResponse>> get() = _dailyWeather
    private val _loading: MutableLiveData<Boolean> = MutableLiveData()
    val loading: LiveData<Boolean> get() = _loading


    init {
        getCurrentData(lat, lon, Constants.METRIC)
//        getCurrentData("44.3473", "44.3473")
        getDailyWeather(lat, lon, cnt, Constants.METRIC)
//        getDailyWeather("44.3473", "44.3473",cnt)

    }

    fun getForecastFromRV(location: String) {
        sendRequest(request = {
            defaultRepository.getForecastData(
                location,
                Constants.METRIC
            )
        },
            success =
            { _weather.value = it },
            error =
            {
                it
            }
        )

    }

    fun getForecastData() = viewModelScope.launch {
        if (etCity.value?.trim()?.lowercase().isNullOrBlank()) {
            BaseViewEvent.ShowMessage(SharedPreferenceStorage.CHECK_CITY_NAME)
            return@launch
        }
        sendRequest(
            request = {
                _loading.value = true
                defaultRepository.getForecastData(
                    etCity.value?.trim()?.lowercase().toString(),
                    Constants.METRIC
                )
            },
            success = {
                _weather.value = it
                _loading.value = false
            },
            error = {
                it
            }
        )

    }

    fun getCurrentData(lat: String, lon: String, units: String) {
        sendRequest(request = {
            _loading.value = true
            defaultRepository.getCurrentWeather(lat, lon, units)
        },
            success = {
                _weather.value = it
                _loading.value = false
            }, error = {
                it
            }
        )
    }

    fun getDailyWeather(lat: String, lon: String, cnt: String, units: String) {
        sendRequest(request = {
            _loading.value = true
            defaultRepository.getDailyWeather(lat, lon, cnt, units)
        },
            success = { dailyResponse ->
                _loading.value = false
                _dailyWeather.value = listOf(dailyResponse)
            }, error = { it }
        )
    }

    companion object {
        val LAT = "lat"
        val LON = "lon"
        val CNT = "25"
    }


}