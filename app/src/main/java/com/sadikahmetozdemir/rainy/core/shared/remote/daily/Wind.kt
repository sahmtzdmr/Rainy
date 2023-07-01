package sadikahmetozdemir.rainy.core.shared.remote.daily

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Wind(

    @SerializedName("speed")
    var speed: Double? = null,
    @SerializedName("deg")
    var deg: Int? = null

) : Parcelable