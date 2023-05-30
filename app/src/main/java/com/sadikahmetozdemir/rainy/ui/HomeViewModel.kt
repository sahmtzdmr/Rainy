package com.sadikahmetozdemir.rainy.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.sadikahmetozdemir.rainy.base.BaseViewModel
import com.sadikahmetozdemir.rainy.core.shared.remote.WeatherResponseModel
import com.sadikahmetozdemir.rainy.core.shared.repository.DefaultRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import org.w3c.dom.Comment
import sadikahmetozdemir.rainy.core.shared.remote.daily.DailyWeatherResponse
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private var defaultRepository: DefaultRepository,
    savedStateHandle: SavedStateHandle
) :
    BaseViewModel() {
    val lat = savedStateHandle.get<String>(LAT) ?: ""
    val lon = savedStateHandle.get<String>(LON) ?: ""
    val cnt= CNT?: ""
    private val _weather: MutableLiveData<WeatherResponseModel> = MutableLiveData()
    val weather: LiveData<WeatherResponseModel> get() = _weather
    private val _dailyWeather: MutableLiveData<List<DailyWeatherResponse>> = MutableLiveData()
    val dailyWeather: LiveData<List<DailyWeatherResponse>> get() = _dailyWeather

    init {
        getCurrentData(lat, lon)
//        getCurrentData("44.3473", "44.3473")
        getDailyWeather(lat, lon,cnt)
//        getDailyWeather("44.3473", "44.3473",cnt)

    }

    fun getForecastData(location: String) {
        sendRequest(
            request = { defaultRepository.getForecastData(location) },
            success = {
                _weather.value = it
            },
            error = {
                it
            }
        )

    }

    fun getCurrentData(lat: String, lon: String) {
        sendRequest(request = { defaultRepository.getCurrentWeather(lat, lon) },
            success = {
                _weather.value = it
            }, error = { it }
        )
    }

    fun getDailyWeather(lat: String, lon: String, cnt: String) {
        sendRequest(request = { defaultRepository.getDailyWeather(lat, lon,cnt) },
            success = {dailyResponse->
                _dailyWeather.value= listOf(dailyResponse)
            }, error = { it }
        )
    }

    companion object {
        val LAT = "lat"
        val LON = "lon"
        val CNT= "7"
    }


}