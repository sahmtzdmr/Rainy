package sadikahmetozdemir.rainy.core.shared.remote.daily

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Coord (

  @SerializedName("lat" )
  var lat : Double? = null,
  @SerializedName("lon" )
  var lon : Double? = null

) : Parcelable