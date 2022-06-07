package com.sadikahmetozdemir.rainy.shared.remote

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WeatherResponseModel(

	@field:SerializedName("visibility")
	val visibility: Int? = null,

	@field:SerializedName("timezone")
	val timezone: Int? = null,

	@field:SerializedName("main")
	val mainModel: MainModel? = null,

	@field:SerializedName("clouds")
	val cloudsModel: CloudsModel? = null,

	@field:SerializedName("sys")
	val sysModel: SysModel? = null,

	@field:SerializedName("dt")
	val dt: Int? = null,

	@field:SerializedName("coord")
	val coordModel: CoordModel? = null,

	@field:SerializedName("weather")
	val weather: List<WeatherItemModel?>? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("cod")
	val cod: Int? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("base")
	val base: String? = null,

	@field:SerializedName("wind")
	val windModel: WindModel? = null
) : Parcelable








