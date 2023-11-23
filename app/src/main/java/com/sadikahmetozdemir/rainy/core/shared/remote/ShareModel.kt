package com.sadikahmetozdemir.rainy.core.shared.remote

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ShareModel(
     var shareMessage: String? = null
) : Parcelable