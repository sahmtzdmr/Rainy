package com.sadikahmetozdemir.rainy.core.shared.remote

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CloudsModel(

    @field:SerializedName("all")
    val all: Int? = null
) : Parcelable