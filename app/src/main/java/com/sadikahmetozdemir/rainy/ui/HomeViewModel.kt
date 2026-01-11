package com.sadikahmetozdemir.rainy.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.sadikahmetozdemir.rainy.base.BaseViewEvent
import com.sadikahmetozdemir.rainy.base.BaseViewModel
import com.sadikahmetozdemir.rainy.core.shared.remote.WeatherResponseModel
import com.sadikahmetozdemir.rainy.core.shared.remote.hourly.HourlyWeatherResponse
import com.sadikahmetozdemir.rainy.core.shared.repository.DefaultRepository
import com.sadikahmetozdemir.rainy.utils.Constants
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
    private val _hourlyWeather: MutableLiveData<HourlyWeatherResponse> = MutableLiveData()
    val hourlyWeather: LiveData<HourlyWeatherResponse> get() = _hourlyWeather
    private val _selectedHourlyWeather: MutableLiveData<com.sadikahmetozdemir.rainy.core.shared.remote.hourly.HourlyWeatherItem> = MutableLiveData()
    val selectedHourlyWeather: LiveData<com.sadikahmetozdemir.rainy.core.shared.remote.hourly.HourlyWeatherItem> get() = _selectedHourlyWeather
    private val _loading: MutableLiveData<Boolean> = MutableLiveData()
    val loading: LiveData<Boolean> get() = _loading
    
    fun setSelectedHourlyWeather(item: com.sadikahmetozdemir.rainy.core.shared.remote.hourly.HourlyWeatherItem) {
        _selectedHourlyWeather.value = item
    }



    init {
        getCurrentData(lat, lon, Constants.METRIC)
        getDailyWeather(lat, lon, cnt, Constants.METRIC)
        getHourlyWeather(lat, lon, Constants.METRIC)
    }

    fun getForecastFromRV(location: String) {
        sendRequest(request = {
            defaultRepository.getForecastData(
                location,
                Constants.METRIC
            )
        },
            success = { weatherResponse ->
                _weather.value = weatherResponse
                // Şehir bulunduğunda günlük ve saatlik hava durumunu da çek
                weatherResponse.coordModel?.lat?.toString()?.let { lat ->
                    weatherResponse.coordModel?.lon?.toString()?.let { lon ->
                        getDailyWeather(lat, lon, cnt, Constants.METRIC)
                        getHourlyWeather(lat, lon, Constants.METRIC)
                    }
                }
            },
            error = {
                it
            }
        )
    }

    fun getForecastData() = viewModelScope.launch {
        if (etCity.value?.trim()?.lowercase().isNullOrBlank()) {
            showMessage(SharedPreferenceStorage.CHECK_CITY_NAME)
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
            success = { weatherResponse ->
                _weather.value = weatherResponse
                _loading.value = false
                // Şehir bulunduğunda günlük ve saatlik hava durumunu da çek
                weatherResponse.coordModel?.lat?.toString()?.let { lat ->
                    weatherResponse.coordModel?.lon?.toString()?.let { lon ->
                        getDailyWeather(lat, lon, cnt, Constants.METRIC)
                        getHourlyWeather(lat, lon, Constants.METRIC)
                    }
                }
            },
            error = { exception ->
                _loading.value = false
                // Hata durumunda weather'ı null yap ki UI'da hata mesajı gösterilebilsin
                _weather.value = null
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
            // Loading state'ini değiştirme, çünkü ana istek zaten tamamlandı
            defaultRepository.getDailyWeather(lat, lon, cnt, units)
        },
            success = { dailyResponse ->
                _dailyWeather.value = listOf(dailyResponse)
            }, error = { 
                // Hata durumunda sessizce devam et, ana hava durumu zaten gösteriliyor
            }
        )
    }

    fun getHourlyWeather(lat: String, lon: String, units: String, cnt: Int = 24) {
        sendRequest(request = {
            // Loading state'ini değiştirme, çünkü ana istek zaten tamamlandı
            defaultRepository.getHourlyWeather(lat, lon, units, cnt)
        },
            success = { hourlyResponse ->
                _hourlyWeather.value = hourlyResponse
            }, error = { 
                // Hata durumunda sessizce devam et, ana hava durumu zaten gösteriliyor
            }
        )
    }

    companion object {
        val LAT = "lat"
        val LON = "lon"
        val CNT = "25"
    }


}