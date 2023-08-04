package sadikahmetozdemir.rainy.core.shared.remote.daily

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Sys (

  @SerializedName("country" ) var country : String? = null

) : Parcelable