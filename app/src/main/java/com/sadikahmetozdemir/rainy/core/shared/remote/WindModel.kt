package com.sadikahmetozdemir.rainy.core.shared.remote

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WindModel(

    @field:SerializedName("deg")
    val deg: Int? = null,

    @field:SerializedName("speed")
    val speed: Double? = null,

    @field:SerializedName("gust")
    val gust: Double? = null
) : Parcelable