package sadikahmetozdemir.rainy.core.shared.remote.daily

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class DailyWeatherResponse(

    @SerializedName("message")
    var message: String? = null,
    @SerializedName("cod")
    var cod: String? = null,
    @SerializedName("count")
    var count: Int? = null,
    @SerializedName("list")
    var list: ArrayList<WeatherList> = arrayListOf()

) : Parcelable
{
    @Parcelize
    class WeatherList(

        @SerializedName("id")
        var id: Int? = null,
        @SerializedName("name")
        var name: String? = null,
        @SerializedName("coord")
        var coord: Coord? = Coord(),
        @SerializedName("main")
        var main: Main? = Main(),
        @SerializedName("dt")
        var dt: Int? = null,
        @SerializedName("wind")
        var wind: Wind? = Wind(),
        @SerializedName("sys")
        var sys: Sys? = Sys(),
        @SerializedName("rain")
        var rain: String? = null,
        @SerializedName("snow")
        var snow: String? = null,
        @SerializedName("clouds")
        var clouds: Clouds? = Clouds(),
        @SerializedName("weather")
        var weather: ArrayList<Weather> = arrayListOf()

    ) : Parcelable

    }



