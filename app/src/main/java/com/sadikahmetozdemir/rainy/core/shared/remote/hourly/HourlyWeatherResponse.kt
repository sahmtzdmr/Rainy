package com.sadikahmetozdemir.rainy.core.shared.remote.hourly

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class HourlyWeatherResponse(
    @SerializedName("cod")
    var cod: String? = null,
    @SerializedName("message")
    var message: Int? = null,
    @SerializedName("cnt")
    var cnt: Int? = null,
    @SerializedName("list")
    var list: ArrayList<HourlyWeatherItem> = arrayListOf(),
    @SerializedName("city")
    var city: HourlyCity? = HourlyCity()
) : Parcelable

@Parcelize
data class HourlyWeatherItem(
    @SerializedName("dt")
    var dt: Long? = null,
    @SerializedName("main")
    var main: HourlyMain? = HourlyMain(),
    @SerializedName("weather")
    var weather: ArrayList<HourlyWeather> = arrayListOf(),
    @SerializedName("clouds")
    var clouds: HourlyClouds? = HourlyClouds(),
    @SerializedName("wind")
    var wind: HourlyWind? = HourlyWind(),
    @SerializedName("visibility")
    var visibility: Int? = null,
    @SerializedName("pop")
    var pop: Double? = null,
    @SerializedName("rain")
    var rain: HourlyRain? = null,
    @SerializedName("sys")
    var sys: HourlySys? = HourlySys(),
    @SerializedName("dt_txt")
    var dtTxt: String? = null
) : Parcelable

@Parcelize
data class HourlyMain(
    @SerializedName("temp")
    var temp: Double? = null,
    @SerializedName("feels_like")
    var feelsLike: Double? = null,
    @SerializedName("temp_min")
    var tempMin: Double? = null,
    @SerializedName("temp_max")
    var tempMax: Double? = null,
    @SerializedName("pressure")
    var pressure: Int? = null,
    @SerializedName("sea_level")
    var seaLevel: Int? = null,
    @SerializedName("grnd_level")
    var grndLevel: Int? = null,
    @SerializedName("humidity")
    var humidity: Int? = null,
    @SerializedName("temp_kf")
    var tempKf: Double? = null
) : Parcelable

@Parcelize
data class HourlyWeather(
    @SerializedName("id")
    var id: Int? = null,
    @SerializedName("main")
    var main: String? = null,
    @SerializedName("description")
    var description: String? = null,
    @SerializedName("icon")
    var icon: String? = null
) : Parcelable

@Parcelize
data class HourlyClouds(
    @SerializedName("all")
    var all: Int? = null
) : Parcelable

@Parcelize
data class HourlyWind(
    @SerializedName("speed")
    var speed: Double? = null,
    @SerializedName("deg")
    var deg: Int? = null,
    @SerializedName("gust")
    var gust: Double? = null
) : Parcelable

@Parcelize
data class HourlyRain(
    @SerializedName("3h")
    var threeH: Double? = null
) : Parcelable

@Parcelize
data class HourlySys(
    @SerializedName("pod")
    var pod: String? = null
) : Parcelable

@Parcelize
data class HourlyCity(
    @SerializedName("id")
    var id: Int? = null,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("coord")
    var coord: HourlyCoord? = HourlyCoord(),
    @SerializedName("country")
    var country: String? = null,
    @SerializedName("population")
    var population: Int? = null,
    @SerializedName("timezone")
    var timezone: Int? = null,
    @SerializedName("sunrise")
    var sunrise: Long? = null,
    @SerializedName("sunset")
    var sunset: Long? = null
) : Parcelable

@Parcelize
data class HourlyCoord(
    @SerializedName("lat")
    var lat: Double? = null,
    @SerializedName("lon")
    var lon: Double? = null
) : Parcelable
